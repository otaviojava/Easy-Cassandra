package org.easycassandra.persistence.cassandra;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.easycassandra.ReplicaStrategy;
import org.easycassandra.persistence.cassandra.FixKeySpaceUtil.KeySpaceQueryInformation;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;

/**
 * this dto has information to build a cluster to be used on the constructors factory.
 * @author otaviojava
 */
public class ClusterInformation {

    private List<String> hosts = new LinkedList<>();

    private String keySpace;

    private String user = "";

    private String password = "";

    private Map<String, Integer> dataCenter = new HashMap<>();

    private ReplicaStrategy replicaStrategy = REPLICASTRATEGY_DEFAULT;

    private String customQuery;

    private int replicaFactor = REPLICA_FACTOR_DEFAULT;

    private int port = PORT_DEFAULT;

    private static final int PORT_DEFAULT = 9042;

    private static final int REPLICA_FACTOR_DEFAULT = 3;
    private static final ReplicaStrategy REPLICASTRATEGY_DEFAULT = ReplicaStrategy.SIMPLES_TRATEGY;


    public List<String> getHosts() {
        return hosts;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

    public String getKeySpace() {
        return keySpace;
    }

    public void setKeySpace(String keySpace) {
        this.keySpace = keySpace;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getReplicaFactor() {
        return replicaFactor;
    }

    public void setReplicaFactor(int replicaFactor) {
        this.replicaFactor = replicaFactor;
    }

    public Map<String, Integer> getDataCenter() {
        return dataCenter;
    }

    public void setDataCenter(Map<String, Integer> dataCenter) {
        this.dataCenter = dataCenter;
    }

    public ReplicaStrategy getReplicaStrategy() {
        return replicaStrategy;
    }

    public void setReplicaStrategy(ReplicaStrategy replicaStrategy) {
        this.replicaStrategy = replicaStrategy;
    }

    public String getCustomQuery() {
        return customQuery;
    }

    public void setCustomQuery(String customQuery) {
        this.customQuery = customQuery;
    }

    /**
     * new instance of ClusterInformation.
     * @return the new instance
     */
    public static ClusterInformation create() {
        return new ClusterInformation();
    }
    /**
     * set the port.
     * @param port the port
     * @return this
     */
    public ClusterInformation withPort(int port) {
        this.port = port;
        return this;
    }

    /**
     * informs the replicaFactor will be used when will generate keySpace
     * Automatically.
     * @param replicaFactor the replicaFactor
     * @return this
     */
    public ClusterInformation withReplicaFactor(int replicaFactor) {
        this.replicaFactor = replicaFactor;
        return this;
    }

    /**
     * set user to authentication.
     * @param user the user
     * @return this
     */
    public ClusterInformation withUser(String user) {
        this.user = user;
        return this;
    }
    /**
     * set the password to authentication.
     * @param password the password
     * @return this
     */
    public ClusterInformation withPassword(String password) {
        this.password = password;
        return this;
    }
    /**
     * defines the keySpace.
     * @param keySpace the keySpace
     * @return this
     */
    public ClusterInformation withKeySpace(String keySpace) {
        this.keySpace = keySpace;
        return this;
    }
    /**
     * add host to cluster, the best practice
     * say you should add all clusters in this configuration.
     * @param parameterHost the host
     * @param parameterHosts the parameterHosts
     * @return this
     */
    public ClusterInformation addHost(String parameterHost, String... parameterHosts) {
        this.hosts.add(parameterHost);
        for (String hostParamether: parameterHosts) {
            this.hosts.add(hostParamether);
        }
        return this;
    }
    /**
     * define the replica placement strategy on Cassandra and
     *  how a keyspace will create if not exists.
     * @param replicaStrategy {@link ReplicaStrategy}
     * @return this
     */
    public ClusterInformation withReplicaStrategy(
            ReplicaStrategy replicaStrategy) {
        this.replicaStrategy = replicaStrategy;
        return this;
    }
    /**
     * a custom query to create the keyspace if not exist, it is mandatory when is
     * {@link ReplicaStrategy#CUSTOM_STRATEGY}
     *  if you define and must begin with.
     * @param customQuery the create to be executed
     * @return this
     */
    public ClusterInformation withCustomQuery(String customQuery) {
        this.customQuery = customQuery;
        return this;
    }
    /**
     * Inform a replica factor to a specific data center, you should use when define
     *  {@link ReplicaStrategy#NETWORK_TOPOLOGY_STRATEGY}
     *  as Replica Strategy.
     * @param dataCenterName the data center name
     * @param factor the replica factor to data center
     * @return this.
     */
    public ClusterInformation addDataCenter(String dataCenterName, int factor) {
        this.dataCenter.put(dataCenterName, factor);
        return this;
    }
    Cluster build() {

        Builder buildCluster = Cluster.builder().withPort(port)
                .addContactPoints(hosts.toArray(new String[0]));

        if (!user.isEmpty() && !password.isEmpty()) {
            buildCluster.withCredentials(user, password);
        }
        return buildCluster.build();
    }

    KeySpaceQueryInformation toInformationKeySpace() {
        KeySpaceQueryInformation information = new KeySpaceQueryInformation();
        information.setCustomQuery(customQuery);
        information.setDataCenter(dataCenter);
        information.setFactor(replicaFactor);
        information.setKeySpace(keySpace);
        information.setReplicaStrategy(replicaStrategy);
        return information;
    }
}
