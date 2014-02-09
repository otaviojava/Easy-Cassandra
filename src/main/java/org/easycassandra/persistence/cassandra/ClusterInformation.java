package org.easycassandra.persistence.cassandra;

import java.util.Collections;
import java.util.List;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;

/**
 * this dto has information to build a cluster to be used on the constructors factory.
 * @author otaviojava
 */
public class ClusterInformation {

    private List<String> hosts = Collections.<String>emptyList();

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

    Cluster build() {

        Builder buildCluster = Cluster.builder().withPort(port)
                .addContactPoints(hosts.toArray(new String[0]));

        if (!user.isEmpty() && !password.isEmpty()) {
            buildCluster.withCredentials(user, password);
        }
        return buildCluster.build();
    }
}
