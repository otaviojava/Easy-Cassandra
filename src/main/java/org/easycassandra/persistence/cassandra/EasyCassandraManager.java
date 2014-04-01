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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.easycassandra.ClassInformation;
import org.easycassandra.ClassInformations;
import org.easycassandra.ReplicaStrategy;
import org.easycassandra.ClassInformation.KeySpaceInformation;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;


/**
 * Class for manage Connections.
 * @author otaviojava
 * @version 2.0
 */
public class EasyCassandraManager extends AbstractCassandraFactory implements
        EasyCassandraFactory {

    private Map<String, Persistence> persistenceMap = new HashMap<String, Persistence>();
    private List<String> keySpaces = new ArrayList<>();
    /**
     * the constructor using default port.
     * @param clusterInformation {@link ClusterInformation}
     */
	public EasyCassandraManager(ClusterInformation clusterInformation) {
		super(clusterInformation);
		keySpaces.add(getKeySpace());
	}

    @Override
    public Persistence getPersistence(String keySpace) {

        if (!persistenceMap.containsKey(keySpace)) {
            Session session = getSession();
            if (!keySpaces.contains(keySpace)) {
                verifyKeySpace(keySpace, getSession());
                keySpaces.add(keySpace);
            }
            persistenceMap.put(keySpace, new PersistenceSimpleImpl(session,
                    keySpace));
        }
        return persistenceMap.get(keySpace);
    }

    @Override
    public Persistence getPersistence(String host, String keySpace,
            ReplicaStrategy replicaStrategy, int factor) {

        if (!persistenceMap.containsKey(keySpace)) {
            Cluster cluter = Cluster.builder().addContactPoints(host).build();
            Session session = cluter.connect();
            if (!keySpace.contains(keySpace)) {
                verifyKeySpace(keySpace, cluter.connect(), replicaStrategy, factor);
                keySpaces.add(keySpace);
            }
            persistenceMap.put(keySpace, new PersistenceSimpleImpl(session,
                    keySpace));
        }
        return persistenceMap.get(keySpace);
    }

	@Override
    public Persistence getPersistence() {
    	return getPersistence(getKeySpace());
    }

    @Override
    public PersistenceBuilder getBuilderPersistence() {
        return new PersistenceBuilderImpl(getSession(), getKeySpace());
    }

    @Override
    public PersistenceAsync getPersistenceAsync() {
        return new PersistenceSimpleAsyncImpl(getSession(), getKeySpace());
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
        ClassInformation classInformation = ClassInformations.INSTACE.getClass(class1);

        if (!classInformation.getSchema().equals("")) {
            getPersistence(classInformation.getSchema());

        }
        classes.add(class1);
        KeySpaceInformation key = classInformation.getKeySpace(keySpace);
        Session session = getCluster().connect(key.getKeySpace());
        return new FixColumnFamily().verifyColumnFamily(session, key.getKeySpace(),
                key.getColumnFamily(), class1);
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
