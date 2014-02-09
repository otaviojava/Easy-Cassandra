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

import org.easycassandra.persistence.cassandra.ClusterInformation;
import org.easycassandra.persistence.cassandra.EasyCassandraManager;
/**
 * base of cassandra factory.
 * @author otaviojava
 */
public abstract class AbstractCassandraFactory extends EasyCassandraManager
        implements CassandraFactorySpring {
    /**
     * the Constructor.
     * @param clusterInformation {@link ClusterInformation}
     */
    public AbstractCassandraFactory(ClusterInformation clusterInformation) {
        super(clusterInformation);
    }


    /**
     * @param keySpace the keySpace
     * @return cassandra tempalte
     */
    public CassandraTemplate getTemplate(String keySpace) {
        verifyKeySpace(keySpace, getSession());
        return new SimpleCassandraTemplateImpl(getSession(), keySpace);
    }

    /**
     * {@link AbstractCassandraFactory#verifyHost(String, int)}.
     * @return the cassandra template
     */
    public CassandraTemplate getTemplate() {
        return getTemplate(getKeySpace());
    }
}
