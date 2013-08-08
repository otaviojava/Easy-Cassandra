/*
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
package org.easycassandra.persistence.cassandra;

import java.util.LinkedList;
import java.util.List;

import org.easycassandra.ReplicaStrategy;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/**
 * Class for manage Connections
 * 
 * @author otaviojava
 * @version 2.0
 */
public class EasyCassandraManager {
    
    private Cluster cluter;
    private String host = "";
    
    /**
     * list of classes added by EasyCassandra
     */
    private List<Class<?>> classes=new LinkedList<Class<?>>();

    /**
     * Method for create the Cassandra's Client, if the keyspace there is not,if
     * keyspace there isn't, it will created with simple strategy replica and
     * number of fator 3
     * 
     * @param host
     *            - place where is Cassandra data base
     * @param keySpace
     *            - the keyspace's name
     * @return the client bridge for the Cassandra data base
     */
    public PersistenceCassandra getPersistence(String host, String keySpace) {
        if (!this.host.equals(host)) {
            cluter = Cluster.builder().addContactPoints(host).build();
        }
        Session session = cluter.connect();
        return new PersistenceCassandraImpl(session, keySpace);
    }

    /**
     * Method for create the Cassandra's Client, if the keyspace there is not,if
     * keyspace there isn't, it will created with replacyStrategy and number of
     * factor
     * 
     * @param host
     *            - place where is Cassandra data base
     * @param keySpace
     *            - the keyspace's name
     * @param replicaStrategy
     *            - replica strategy
     * @param factor
     *            - number of the factor
     * @return the client bridge for the Cassandra data base
     */
    public PersistenceCassandra getPersistence(String host, String keySpace,ReplicaStrategy replicaStrategy, int factor) {
        cluter = Cluster.builder().addContactPoints(host).build();
        Session session = cluter.connect();
        return new PersistenceCassandraImpl(session, keySpace, replicaStrategy,factor);
    }

    public boolean addFamilyObject(Class<?> class1, String keySpace) {
        if(classes.contains(class1)){
            return true;
        }
        String familyColumn = ColumnUtil.INTANCE.getColumnFamilyName(class1);
        Session session = cluter.connect(keySpace);
        if (!ColumnUtil.INTANCE.getSchema(class1).equals("")) {
            getPersistence(host, ColumnUtil.INTANCE.getSchema(class1));

        }
        classes.add(class1);
        return new FixColumnFamily().verifyColumnFamily(session, familyColumn,class1);
    }

}
