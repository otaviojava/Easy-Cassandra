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
package org.easycassandra.persistence.cassandra.spring;

import org.easycassandra.ReplicaStrategy;
import org.easycassandra.persistence.cassandra.EasyCassandraManager;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
/**
 * base of cassandra factory.
 * @author otaviojava
 *
 */
public abstract class AbstractCassandraFactory extends EasyCassandraManager implements CassandraFactorySpring{
	
	   public AbstractCassandraFactory(String host, String keySpace) {
		super(host, keySpace);
	}
	   public AbstractCassandraFactory(String host,String keySpace,int port){
		   super(host,keySpace,port);
	   }

	   public AbstractCassandraFactory(String host,String keySpace,int port, String user, String password){
			super(host, keySpace, port, user, password);
		}

	/**
     * {@link AbstractCassandraFactory#getTemplate(String, String, ReplicaStrategy, int)}.
     */
    public CassandraTemplate getTemplate(String host, String keySpace, int port) {
    	 Session session = verifyHost(host,port).connect();
         verifyKeySpace(keySpace, session);
         return new SimpleCassandraTemplateImpl(this);
	}

    /**
     * {@link AbstractCassandraFactory#getTemplate(String, String, ReplicaStrategy, int)}.
     */
    public CassandraTemplate getTemplate(String host, String keySpace) {
        return getTemplate(host, keySpace, getPort());
    }


    /**
     * Method for create the Cassandra's Client, if the keyspace there is not,if
     * keyspace there isn't, it will created with replacyStrategy and number of
     * factor.
     * @param host - place where is Cassandra data base
     * @param keySpace - the keyspace's name
     * @param replicaStrategy - replica strategy
     * @param factor - number of the factor
     * @return the client bridge for the Cassandra data base
     */
    public CassandraTemplate getTemplate(String host, String keySpace,ReplicaStrategy replicaStrategy, int factor) {
        Cluster cluter = verifyHost(host, getPort());
        verifyKeySpace(keySpace, cluter.connect(), replicaStrategy,factor);
        return new SimpleCassandraTemplateImpl(createSession(host, getPort(), keySpace),keySpace);
    }


	/**
	 * {@link AbstractCassandraFactory#verifyHost(String, int)}.
	 */
    public CassandraTemplate getTemplate(String host) {
		return getTemplate(host, getKeySpace());
	}

    /**
     * {@link AbstractCassandraFactory#verifyHost(String, int)}.
     */
	public CassandraTemplate getTemplate() {
		return getTemplate(getHost(),getKeySpace());
	}
}
