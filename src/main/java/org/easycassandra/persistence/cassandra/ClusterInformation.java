package org.easycassandra.persistence.cassandra;

import java.util.LinkedList;
import java.util.List;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;

/**
 * this dto has information to build a cluster to be used on the constructors factory.
 * @author otaviojava
 */
public class ClusterInformation {

    private List<String> hosts = new LinkedList<>();

    private String keySpace;

    private int port = PORT_DEFAULT;

    private int replicaFactor = REPLICA_FACTOR_DEFAULT;

    private String user = "";

    private String password = "";

    private static final int PORT_DEFAULT = 9042;

    private static final int REPLICA_FACTOR_DEFAULT = 3;

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
     * defines the keySpace default.
     * @param keySpace the keySpace
     * @return this
     */
    public ClusterInformation withDefaultKeySpace(String keySpace) {
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

    Cluster build() {

        Builder buildCluster = Cluster.builder().withPort(port)
                .addContactPoints(hosts.toArray(new String[0]));

        if (!user.isEmpty() && !password.isEmpty()) {
            buildCluster.withCredentials(user, password);
        }
        return buildCluster.build();
    }
}
