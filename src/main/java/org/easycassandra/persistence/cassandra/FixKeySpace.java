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
package org.easycassandra.persistence.cassandra;

import org.easycassandra.ReplicaStrategy;
import org.easycassandra.persistence.cassandra.FixKeySpaceUtil.CreateKeySpace;
import org.easycassandra.persistence.cassandra.FixKeySpaceUtil.CreateKeySpaceException;
import org.easycassandra.persistence.cassandra.FixKeySpaceUtil.KeySpaceQueryInformation;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.InvalidQueryException;

/**
 * This class verifies if keyspace exists and then try to create.
 * @author otaviojava
 */
class FixKeySpace {


    private static final int DEFAULT_REPLICATION_FACTOR = 3;

    private static final ReplicaStrategy DEFAULT_REPLICA_STRATEGY = ReplicaStrategy.SIMPLES_TRATEGY;

    /**
     * Verify if keySpace exist.
     * @param keySpace - nome of keyspace
     * @param session - session of Cassandra
     */
    public final void verifyKeySpace(String keySpace, Session session) {
        verifyKeySpace(keySpace, session, DEFAULT_REPLICA_STRATEGY, DEFAULT_REPLICATION_FACTOR);
    }

    public void verifyKeySpace(String keySpace, Session session,
            ReplicaStrategy replicaStrategy, int factor) {
        createKeySpace(session, keySpace, replicaStrategy, factor);
    }

    public void createKeySpace(Session session, String keySpace,
            ReplicaStrategy replicaStrategy, int factor) {

        KeySpaceQueryInformation information = getInformation(replicaStrategy,
                factor);
        createKeySpace(information, session);

    }

    public void createKeySpace(KeySpaceQueryInformation information, Session session) {

        CreateKeySpace createKeySpace = FixKeySpaceUtil.INSTANCE
                .getCreate(information.getReplicaStrategy());
        try {
            session.execute(createKeySpace.createQuery(information));
        } catch (InvalidQueryException exception) {
            error(information, createKeySpace, exception);
        }
    }

    private void error(KeySpaceQueryInformation information,
            CreateKeySpace createKeySpace, InvalidQueryException exception) {
        StringBuilder error = new StringBuilder();
        error.append(" An error happened when execute create keyspace: ")
                .append(information.getKeySpace());
        error.append(" with type: ").append(information.getReplicaStrategy());
        error.append(" With query: ").append(
                createKeySpace.createQuery(information));
        error.append(" Error cause: ").append(exception.getCause()).append(" ")
                .append(exception.getMessage());

        throw new CreateKeySpaceException(error.toString());

    }

    private KeySpaceQueryInformation getInformation(
            ReplicaStrategy replicaStrategy, int factor) {
        KeySpaceQueryInformation information = new KeySpaceQueryInformation();
        information.setReplicaStrategy(replicaStrategy);
        information.setFactor(factor);
        return information;
    }

}
