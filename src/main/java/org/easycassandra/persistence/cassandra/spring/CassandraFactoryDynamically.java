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

import java.util.LinkedList;
import java.util.List;

import org.easycassandra.persistence.cassandra.ColumnUtil;

import com.datastax.driver.core.Session;

/**
 * Class for manage Connections.
 * @author otaviojava
 * @version 2.0
 */
public class CassandraFactoryDynamically extends AbstractCassandraFactory {

    /**
     * Constructor.
     * @param host the host
     * @param keySpace the keyspace
     */
    public CassandraFactoryDynamically(String host, String keySpace) {
		super(host, keySpace);
	}

    /**
     * Constructor.
     * @param host the host
     * @param keySpace the key space
     * @param port the port
     */
	public CassandraFactoryDynamically(String host, String keySpace, int port) {
		super(host, keySpace, port);
	}

	/**
	 * the constructor.
	 * @param host the host
	 * @param keySpace the keyspace
	 * @param port the port
	 * @param user the user
	 * @param password the password
	 */
    public CassandraFactoryDynamically(String host, String keySpace, int port,
            String user, String password) {
		super(host, keySpace, port, user, password);
	}

	/**
     * list of classes added by Cassandra.
     */
    private List<Class<?>> classes = new LinkedList<Class<?>>();

    /**
     * add class on factory.
     * @param class1 the class
     * @param keySpace the keyspace
     * @return if the process was sucess
     */
    public boolean addFamilyObject(Class<?> class1, String keySpace) {
        if (classes.contains(class1)) {
            return true;
        }
        String familyColumn = ColumnUtil.INTANCE.getColumnFamilyNameSchema(class1);
        Session session = getCluster().connect(keySpace);
        if (!ColumnUtil.INTANCE.getSchema(class1).equals("")) {
            getTemplate(getHost(), ColumnUtil.INTANCE.getSchema(class1));

        }
        classes.add(class1);
        return fixColumnFamily(session, familyColumn, class1);
    }


}
