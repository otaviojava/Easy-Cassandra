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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.Cassandra.Client;
import org.apache.cassandra.thrift.Compression;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.SchemaDisagreementException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.easycassandra.EasyCassandraException;
import org.easycassandra.ReplicaStrategy;
import org.easycassandra.util.DomUtil;

/**
 * Class for manage Connections
 *
 * @author otavio
 */
public final class EasyCassandraManager {
    /**
     * the default size to replica fator in Easy-Cassandra
     */
    private static final int REPLICA_FATOR_DEFAULT = 3;

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
     * Method for create the Cassandra's Client, if the keyspace there is not,if keyspace there isn't, 
     * it will created with  simple strategy replica and number of fator 3
     * @param keySpace - the keyspace's name
     * @param host - place where is Cassandra data base
     * @param port -  number of the port to connect in cassandra 
     * @return the client bridge for the Cassandra data base
     */
    public static Client getClient(String keySpace, String host, int port){
    	return getClient(keySpace, host, port,ReplicaStrategy.SimpleStrategy, REPLICA_FATOR_DEFAULT);
    }
    /**
     * Method for create the Cassandra's Client
     * if keyspace there isn't, it will created
     * with strategy replica and number for informed in this method
     * @param keySpace - name of keySpace in Cassandra
     * @param host - host that Cassandra be
     * @param port - number of port to communication with Cassandra DataBase
     * @param replicaStrategy -strategy determines how replicas for a keyspac
     * @param replicaFator - number to the fator in replica  
     * @return the Cassandra's Client
     */
    public static Client getClient(String keySpace, String host, int port,ReplicaStrategy replicaStrategy,int replicaFator) {
    	EasyCassandraClient easyClient = new EasyCassandraClient(host, port);
        if (conections.contains(easyClient)) {
        	easyClient= conections.get(conections.indexOf(easyClient));
        	
        	return makeConnection(keySpace, replicaStrategy, replicaFator,easyClient);
        	
        	
        }
        easyClient.setTransport(new TFramedTransport(new TSocket(host, port)));
        easyClient.setProtocol(new TBinaryProtocol(easyClient.getTransport()));
        
        return makeConnection(keySpace, replicaStrategy, replicaFator,easyClient);
    }
	private static Client makeConnection(String keySpace,
			ReplicaStrategy replicaStrategy, int replicaFator,
			EasyCassandraClient conection) {
		Cassandra.Client client=null;
		try {
        	
			client = new Cassandra.Client(conection.getProtocol());
             if(!conection.getTransport().isOpen()){
            	 conection.getTransport().open();
             }
             client.set_keyspace(keySpace);
             addConnection(conection);
            
            return client;
        }catch (InvalidRequestException exception){
             if(((InvalidRequestException)exception).getWhy().contains("Keyspace ".concat(keySpace).concat(" does not exist"))){
        	       try {
                    createKeySpace(keySpace, replicaStrategy, replicaFator, client);
                    addConnection(conection);
                    return client;
                } catch (Exception e) {
                    Logger.getLogger(EasyCassandraManager.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
        catch (Exception exception) {	
            Logger.getLogger(EasyCassandraManager.class.getName()).log(Level.SEVERE, null, exception);
            
        }
        return null;
	}
    
    /**
     * Create the keyspace
     * @param keySpace
     * @param replicaStrategy
     * @param replicaFator
     * @param client
     * @throws InvalidRequestException
     * @throws UnavailableException
     * @throws TimedOutException
     * @throws SchemaDisagreementException
     * @throws TException
     */
	private static void createKeySpace(String keySpace,
			ReplicaStrategy replicaStrategy, int replicaFator,
			Cassandra.Client client) throws InvalidRequestException,
			UnavailableException, TimedOutException,
			SchemaDisagreementException, TException {
		StringBuilder cql=new StringBuilder();
		 cql.append(" CREATE KEYSPACE ");
		 cql.append(keySpace);
		 cql.append(" WITH strategy_class = ");
		 cql.append(replicaStrategy.getValue());
		 if(ReplicaStrategy.SimpleStrategy.equals(replicaStrategy)){
		 cql.append(" AND strategy_options:replication_factor = ");
		 cql.append(replicaFator);
		 cql.append(" ; ");
		 } 
		 
             client.execute_cql_query(ByteBuffer.wrap(cql.toString().getBytes()),Compression.NONE);
             client.set_keyspace(keySpace);
	}

    /**
     * Add connection in EasyCassandraManager
     * @param conection -
     * @param transport -
     * @param protocol  - 
     * @param client    -
     */
	private static void addConnection(EasyCassandraClient conection) {
		 
		  if (!conections.contains(conection)) {
		 conections.add(conection);
		  }
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
    	Client client=getClient(keySpace,host, port);
    	if(client==null){
    		return null;
    	}
       return new PersistenceSingleClient(client, referenceSuperColunms, keySpace);
       
    }
    /**
     * get the Persistence with many clients and randomly access
     * @param keySpace -keySpace's name
     * @return - the Persistence
     */
	public static Persistence getPersistenceRandom(String keySpace) {
	
		return new PersistenceRandomClient(getListClient(keySpace),referenceSuperColunms, keySpace);
}
	/**
     * get the Persistence with many clients and sequential access
     * @param keySpace -keySpace's name
     * @return - the Persistence
     */
	public static Persistence getPersistenceSequencial(String keySpace) {
	
		return new PersistenceSequencialClient(getListClient(keySpace),referenceSuperColunms, keySpace);
}
	
	/**
	 * make the list of client from the keyspace's name
	 * @param keySpace - the keyspace's name
	 * @return the client's list
	 */
	private static List<Client> getListClient(String keySpace) {
		List<Client> clients=new ArrayList<Cassandra.Client>();
		if(conections.size()<1){
			throw new EasyCassandraException("You should add client connections in EasyCassandraManager");
			
		}
		for(EasyCassandraClient easyClient:conections){
			clients.add(makeConnection(keySpace, ReplicaStrategy.SimpleStrategy, REPLICA_FATOR_DEFAULT, easyClient));
		}
		return clients;
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
        if(referenceSuperColunms.get().size()>0){
        DomUtil.getFileDom(referenceSuperColunms.get());
        }
        conections=new ArrayList<EasyCassandraClient>();
    }
    
    /**
     * 
     * @return number of client within EasyCassandraManager
     */
	public static int numberOfClients() {
		
		return conections.size();
	}

        
        private EasyCassandraManager(){
        }

}
