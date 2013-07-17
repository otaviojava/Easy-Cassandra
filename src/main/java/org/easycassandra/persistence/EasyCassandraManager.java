/*
 * Copyright 2013 Otávio Gonçalves de Santana (otaviojava)
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
	
	private static Cluster cluter;
	
	/**
     * Method for create the Cassandra's Client, if the keyspace there is not,if keyspace there isn't, 
     * it will created with  simple strategy replica and number of fator 3
     * @param host - place where is Cassandra data base
     * @param keySpace - the keyspace's name
     * @return the client bridge for the Cassandra data base
     */
	public static PersistenceCassandra getPersistence(String host, String keySpace) {
		cluter=Cluster.builder().addContactPoints(host).build();
		Session session=cluter.connect();
		return new PersistenceCassandraImpl(session,keySpace);
	}

	/**
	 * Method for create the Cassandra's Client, if the keyspace there is not,if keyspace there isn't,
	 * it will created with  replacyStrategy and number of factor
	 * @param host - place where is Cassandra data base
	 * @param keySpace - the keyspace's name
	 * @param replicaStrategy - replica strategy
	 * @param factor - number of the factor
	 * @return the client bridge for the Cassandra data base
	 */
	public static PersistenceCassandra getPersistence(String host, String keySpace,ReplicaStrategy replicaStrategy, int factor) {
		cluter=Cluster.builder().addContactPoints(host).build();
		Session session=cluter.connect();
		return new PersistenceCassandraImpl(session, keySpace,replicaStrategy,factor);
	}

	public static boolean addFamilyObject(Class<?> class1,String keySpace) {
		
		String familyColumn= ColumnUtil.INTANCE.getColumnFamilyName(class1);
		Session session=cluter.connect(keySpace);
		return new FixColumnFamily().verifyColumnFamily(session, familyColumn,class1);
	}

}
