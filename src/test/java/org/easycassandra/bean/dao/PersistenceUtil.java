package org.easycassandra.bean.dao;

import org.easycassandra.Constants;
import org.easycassandra.persistence.cassandra.ClusterInformation;
import org.easycassandra.persistence.cassandra.EasyCassandraManager;
import org.easycassandra.persistence.cassandra.Persistence;
import org.easycassandra.persistence.cassandra.PersistenceAsync;
/**
 * UTIL to connection on Cassandra.
 * @author otaviojava
 */
enum PersistenceUtil {
INSTANCE;

    private EasyCassandraManager easyCassandraManager;


    {
        ClusterInformation clusterInformation = ClusterInformation.create()
                .addHost(Constants.HOST)
                .withKeySpace(Constants.KEY_SPACE).withUser("user").withPassword("passWord");
        easyCassandraManager = new EasyCassandraManager(clusterInformation);
    }

    public <T> void addFamilyObject(Class<T> baseClass) {
        easyCassandraManager.addFamilyObject(baseClass, Constants.KEY_SPACE);
    }

    public Persistence getPersistence() {
        return easyCassandraManager.getPersistence();
    }

    public PersistenceAsync getPersistenceAsync() {
        return easyCassandraManager.getPersistenceAsync();
    }
}
