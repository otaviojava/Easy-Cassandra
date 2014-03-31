package org.easycassandra.persistence.cassandra;

import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.Assert;

import org.easycassandra.ReplicaStrategy;
import org.easycassandra.persistence.cassandra.FixKeySpaceUtil.CreateKeySpace;
import org.easycassandra.persistence.cassandra.FixKeySpaceUtil.CreateKeySpaceException;
import org.easycassandra.persistence.cassandra.FixKeySpaceUtil.KeySpaceQueryInformation;
import org.junit.Test;

/**
 * the fix keySpace class.
 * @author otaviojava
 */
public class FixKeySpaceUtilTest {


    private static final int THREE = 3;
    private static final String CUSTOM_QUERY = "CREATE KEYSPACE IF NOT EXISTS"
            + " javabahia WITH replication = {'class': 'SimpleStrategy' ,"
            + " 'replication_factor': 3};";
    private static final String CUSTOM_QUERY_ERRO_TEST = "CREATE KEYSPACE"
            + "  javabahia WITH replication"
            + " = {'class': 'SimpleStrategy' , 'replication_factor': 3};";
    private static final String SIMPLE_TEST = "CREATE KEYSPACE IF NOT EXISTS javabahia"
            + " WITH replication"
            + " = {'class': 'SimpleStrategy' , 'replication_factor': 3};";
    private static final String NETWORK_DC_TEST = "CREATE KEYSPACE IF NOT EXISTS javabahia WITH "
            + "replication = {'class': 'NetworkTopologyStrategy'  , "
            + "'DC1' : 1,'DC2' : 3 };";
    private static final String NETWORK_TEST = "CREATE KEYSPACE IF NOT EXISTS "
            + "javabahia WITH replication ="
            + " {'class': 'NetworkTopologyStrategy'  };";

    /**
     * test.
     */
    @Test
    public void networkdTest() {
        KeySpaceQueryInformation query = new KeySpaceQueryInformation();
        query.setReplicaStrategy(ReplicaStrategy.NETWORK_TOPOLOGY_STRATEGY);
        query.setKeySpace("javabahia");
        CreateKeySpace createKeySpace = FixKeySpaceUtil.INSTANCE
                .getCreate(query.getReplicaStrategy());
        Assert.assertEquals(
                createKeySpace.createQuery(query),
                NETWORK_TEST);

    }

    /**
     * test.
     */
    @Test
    public void networkdTestDC() {
        KeySpaceQueryInformation query = new KeySpaceQueryInformation();
        query.setReplicaStrategy(ReplicaStrategy.NETWORK_TOPOLOGY_STRATEGY);
        query.setKeySpace("javabahia");
        Map<String, Integer> map = new LinkedHashMap<>();
        query.setDataCenter(map);
        query.getDataCenter().put("DC1", 1);
        query.getDataCenter().put("DC2", THREE);
        CreateKeySpace createKeySpace = FixKeySpaceUtil.INSTANCE
                .getCreate(query.getReplicaStrategy());
        Assert.assertEquals(
                createKeySpace.createQuery(query),
                NETWORK_DC_TEST);

    }
    /**
     * test.
     */
    @Test
    public void simpleTest() {
        KeySpaceQueryInformation query = new KeySpaceQueryInformation();
        query.setReplicaStrategy(ReplicaStrategy.SIMPLES_TRATEGY);
        query.setKeySpace("javabahia");
        query.setFactor(THREE);
        CreateKeySpace createKeySpace = FixKeySpaceUtil.INSTANCE
                .getCreate(query.getReplicaStrategy());
        Assert.assertEquals(
                createKeySpace.createQuery(query),
                SIMPLE_TEST);

    }
    /**
     * test.
     */
    @Test (expected = CreateKeySpaceException.class)
    public void customMandatoryTest() {
        KeySpaceQueryInformation query = new KeySpaceQueryInformation();
        query.setReplicaStrategy(ReplicaStrategy.CUSTOM_STRATEGY);
        query.setKeySpace("javabahia");
        CreateKeySpace createKeySpace = FixKeySpaceUtil.INSTANCE
                .getCreate(query.getReplicaStrategy());
        createKeySpace.createQuery(query);

    }
    /**
     * test.
     */
    @Test (expected = CreateKeySpaceException.class)
    public void customWithOutBeginTest() {
        KeySpaceQueryInformation query = new KeySpaceQueryInformation();
        query.setReplicaStrategy(ReplicaStrategy.CUSTOM_STRATEGY);
        query.setKeySpace("javabahia");
        query.setCustomQuery(CUSTOM_QUERY_ERRO_TEST);
        CreateKeySpace createKeySpace = FixKeySpaceUtil.INSTANCE
                .getCreate(query.getReplicaStrategy());
        createKeySpace.createQuery(query);

    }

    /**
     * test.
     */
    @Test
    public void customTest() {
        KeySpaceQueryInformation query = new KeySpaceQueryInformation();
        query.setReplicaStrategy(ReplicaStrategy.CUSTOM_STRATEGY);
        query.setKeySpace("javabahia");
        query.setCustomQuery(CUSTOM_QUERY);
        CreateKeySpace createKeySpace = FixKeySpaceUtil.INSTANCE
                .getCreate(query.getReplicaStrategy());

        Assert.assertEquals(createKeySpace.createQuery(query), query.getCustomQuery());

    }
}
