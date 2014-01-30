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
 * Class for manage Connections.
 * @author otaviojava
 * @version 2.0
 */
public class EasyCassandraManager extends AbstractCassandraFactory  {


    /**
     * the constructor using default port.
     * @param host the host
     * @param keySpace the keySpace
     */
	public EasyCassandraManager(String host, String keySpace) {
		super(host, keySpace);
	}
	/**
	 * the constructor.
	 * @param host the host
	 * @param keySpace the keyspace
	 * @param port the port
	 */
	public EasyCassandraManager(String host, String keySpace, int port) {
		super(host, keySpace, port);
	}

	/**
	 * The constructor using credentials.
	 * @param host the host
	 * @param keySpace the keySpace
	 * @param port the port
	 * @param user the user
	 * @param password the password
	 */
    public EasyCassandraManager(String host, String keySpace, int port,
            String user, String password) {
		super(host, keySpace, port, user, password);
	}
	 /**
     * Method for create the Cassandra's Client, if the keyspace there is not,if
     * keyspace there isn't, it will created with simple strategy replica and
     * number of fator 3.
     * @param host
     *            - place where is Cassandra data base
     * @param keySpace
     *            - the keyspace's name
     * @return the client bridge for the Cassandra data base
     */
    public Persistence getPersistence(String host, String keySpace) {
        Cluster cluter = getCluster();
        if (!this.getHost().equals(host)) {
            cluter = Cluster.builder().addContactPoints(host).build();
        }
        Session session = cluter.connect();
        verifyKeySpace(keySpace, cluter.connect());
        return new PersistenceSimpleImpl(session, keySpace);
    }

    /**
     * Method for create the Cassandra's Client, if the keyspace there is not,if
     * keyspace there isn't, it will created with replacyStrategy and number of
     * factor.
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
    public Persistence getPersistence(String host, String keySpace,
            ReplicaStrategy replicaStrategy, int factor) {
        Cluster cluter = Cluster.builder().addContactPoints(host).build();
        Session session = cluter.connect();
        verifyKeySpace(keySpace, cluter.connect(), replicaStrategy, factor);
        return new PersistenceSimpleImpl(session, keySpace);
    }

    /**
     * returns a persistence.
     * @return the persistence
     */
    public Persistence getPersistence() {
    	return getPersistence(getHost(), getKeySpace());
    }

    /**
     * list of classes added by Cassandra.
     */
    private List<Class<?>> classes = new LinkedList<Class<?>>();

    /**
     * add an objetc to Cassandra management.
     * @param class1 the class
     * @param keySpace the keyspace
     * @return if executed with sucesses
     */
    public boolean addFamilyObject(Class<?> class1, String keySpace) {
        if (classes.contains(class1)) {
            return true;
        }

        String familyColumn = ColumnUtil.INTANCE.getColumnFamilyNameSchema(class1);
        Session session = getCluster().connect(keySpace);
        if (!ColumnUtil.INTANCE.getSchema(class1).equals("")) {
            getPersistence(getHost(), ColumnUtil.INTANCE.getSchema(class1));

        }
        classes.add(class1);
        return new FixColumnFamily().verifyColumnFamily(session, familyColumn, class1);
    }

    /**
     * add an objetc to Cassandra management and use the default keyspace.
     * {@link EasyCassandraManager#addFamilyObject(Class, String)}
     * @param class1 the class
     * @return if executed with sucesses
     */
    public boolean addFamilyObject(Class<?> class1) {
        return addFamilyObject(class1, getKeySpace());
    }

}
