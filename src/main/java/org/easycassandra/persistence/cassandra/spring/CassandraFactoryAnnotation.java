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

import org.easycassandra.ClassInformation;
import org.easycassandra.ClassInformations;
import org.easycassandra.ClassInformation.KeySpaceInformation;
import org.easycassandra.persistence.cassandra.ClusterInformation;
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
     * Constructor to Factory.
     * @param clusterInformation {@link ClusterInformation}
     */
	public CassandraFactoryAnnotation(ClusterInformation clusterInformation) {
        super(clusterInformation);
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
	    ClassInformation classInformation = ClassInformations.INSTACE.getClass(entity);
        String familyColumn = classInformation.getNameSchema();
        if (!classInformation.getSchema().equals("")) {
            getTemplate(classInformation.getSchema());

        }
        KeySpaceInformation key = classInformation.getKeySpace(getKeySpace());
        Session session = getCluster().connect(key.getKeySpace());
        return fixColumnFamily(session, familyColumn, entity);
	}

	public void setAnnotatedClasses(List<Class<?>> annotatedClasses) {
		this.annotatedClasses = annotatedClasses;
	}

}
