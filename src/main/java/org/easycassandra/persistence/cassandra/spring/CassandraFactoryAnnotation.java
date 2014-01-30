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
package org.easycassandra.persistence.cassandra.spring;

import java.util.List;

import org.easycassandra.persistence.cassandra.ColumnUtil;
import org.springframework.beans.factory.InitializingBean;

import com.datastax.driver.core.Session;

/**
 * Add the mapped class by spring xml.
 * @author otaviojava
 */
public class CassandraFactoryAnnotation extends AbstractCassandraFactory
        implements InitializingBean {

	private List<Class<?>> annotatedClasses;

	/**
	 * constructor.
	 * @param host the host
	 * @param keySpace the keyspace
	 */
	public CassandraFactoryAnnotation(String host, String keySpace) {
		super(host, keySpace);
	}

	/**
	 * the constructor.
	 * @param host the host
	 * @param keySpace the keyspace
	 * @param port the port
	 */
	public CassandraFactoryAnnotation(String host, String keySpace, int port) {
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
	public CassandraFactoryAnnotation(String host, String keySpace, int port,
			String user, String password) {
		super(host, keySpace, port, user, password);
	}
	/**
	 * to run all classes on memory.
	 * @throws Exception wrong
	 */
	public void afterPropertiesSet() throws Exception {
        if (annotatedClasses == null) {
            return;
        }
        for (Class<?> clazz : annotatedClasses) {
            mappedBean(clazz);
        }
	}

	/**
	 * map a specific class.
	 * @param entity the entity
	 * @return the process with success or not
	 */
	public boolean mappedBean(Class<?> entity) {
        String familyColumn = ColumnUtil.INTANCE
                .getColumnFamilyNameSchema(entity);
        Session session = getCluster().connect(getKeySpace());
        if (!ColumnUtil.INTANCE.getSchema(entity).equals("")) {
            getTemplate(getHost(), ColumnUtil.INTANCE.getSchema(entity));

        }
        return fixColumnFamily(session, familyColumn, entity);
	}

	public void setAnnotatedClasses(List<Class<?>> annotatedClasses) {
		this.annotatedClasses = annotatedClasses;
	}

}
