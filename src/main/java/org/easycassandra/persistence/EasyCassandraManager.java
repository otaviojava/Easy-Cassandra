/*
 * Copyright 2012 Otávio Gonçalves de Santana (otaviojava)
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.easycassandra.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.Cassandra.Client;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.easycassandra.util.DomUtil;

/**
 * Class for manage Connections
 *
 * @author otavio
 */
public final class EasyCassandraManager {

    /**
     * List with all Connections
     */
    private static List<EasyCassandraClient> conections;
    /**
     * information about id Column Family
     */
    private static AtomicReference<ColumnFamilyIds> referenceSuperColunms;
    /**
     * Describe the family columns Objects
     */
    private static Map<String, DescribeFamilyObject> familyObjects;

    static {
        conections = new ArrayList<EasyCassandraClient>();
        referenceSuperColunms = new AtomicReference<ColumnFamilyIds>();
        ReadDocument readDocument = new ReadDocument();
        referenceSuperColunms.set(readDocument.read());
        familyObjects = new HashMap<String, DescribeFamilyObject>();
    }

    /**
     * Method for create the Cassandra's Client
     *
     * @param keySpace - name of keySpace in Cassandra
     * @param host - host that Cassandra be
     * @param port - number of port to communication with Cassandra DataBase
     * @return the Cassandra's Client
     */
    public static Client getClient(String keySpace, String host, int port) {
        EasyCassandraClient conection = new EasyCassandraClient(host,
                keySpace, port);
        if (conections.contains(conection)) {
            return conections.get(conections.indexOf(conection)).getClient();
        }
        try {
            TTransport transport = new TFramedTransport(new TSocket(host, port));
            TProtocol protocol = new TBinaryProtocol(transport);
            Cassandra.Client client = new Cassandra.Client(protocol);
            transport.open();
            client.set_keyspace(keySpace);
            conection.setClient(client);
            conection.setTransport(transport);
            conections.add(conection);
            return client;
        } catch (Exception exception) {
            Logger.getLogger(EasyCassandraManager.class.getName()).log(Level.SEVERE, null, exception);
        }
        return null;
    }

    /**
     * Class for create the persistence from Client
     *
     * @param keySpace
     * @param host
     * @param port
     * @see Persistence
     * @see EasyCassandraManager#getClient(String, String, int)
     * @return Persistence from the Param's values
     */
    public static Persistence getPersistence(String keySpace, String host,
            int port) {
        Persistence persistence = new Persistence(getClient(keySpace,
                host, port), referenceSuperColunms, keySpace);
        return persistence;
    }

    /**
     * Add object for CQL in objects
     *
     * @param classColumnFamily
     * @return
     */
    public static boolean addFamilyObject(Class<?> classColumnFamily) {
        DescribeFamilyObject describeFamilyObject =
                new DescribeFamilyObject(classColumnFamily);
        familyObjects.put(describeFamilyObject.getClassFamily().getSimpleName(),
                describeFamilyObject);
        return true;
    }

    /**
     * Return the DescribeFamilyObject
     *
     * @param string - name of Column Family
     * @return
     */
    static DescribeFamilyObject getFamily(String string) {
        return familyObjects.get(string);
    }

    /**
     * Method for close all Cleint in the list
     *
     * @see EasyCassandraManager#conections
     */
    public static void closeClients() {
        for (EasyCassandraClient conexao : conections) {
            conexao.getTransport().close();
        }
        DomUtil.getFileDom(referenceSuperColunms.get());
    }

    private EasyCassandraManager() {
    }
}
