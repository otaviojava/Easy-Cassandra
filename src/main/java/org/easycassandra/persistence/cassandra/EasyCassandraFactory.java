package org.easycassandra.persistence.cassandra;

import org.easycassandra.ReplicaStrategy;

/**
 * Class for manage Connections.
 * @author otaviojava
 *
 */
public interface EasyCassandraFactory {

    /**
    * Method for create the Cassandra's Client, if the keyspace there is not,if
    * keyspace there isn't, it will created with simple strategy replica and
    * number of fator 3.
    * @param keySpace
    *            - the keyspace's name
    * @return the client bridge for the Cassandra data base
    */
    @Deprecated
    Persistence getPersistence(String keySpace);
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
    @Deprecated
    Persistence getPersistence(String host, String keySpace,
            ReplicaStrategy replicaStrategy, int factor);

    /**
     * returns a persistence.
     * @return the persistence
     */
    Persistence getPersistence();

    /**
     * interface to create an easy way to create query.
     * @see PersistenceBuilder
     * @return the {@link PersistenceBuilderImpl}
     */
    PersistenceBuilder getBuilderPersistence();

    /**
     * returns a persistence async.
     * @return the persistence that process async way.
     */
    PersistenceAsync getPersistenceAsync();
}
