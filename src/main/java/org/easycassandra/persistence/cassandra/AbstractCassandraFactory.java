package org.easycassandra.persistence.cassandra;

import java.util.List;

import org.easycassandra.ReplicaStrategy;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
/**
 * Template of CassandraFactory.
 * @author otaviojava
 */
class AbstractCassandraFactory implements CassandraFactory {

   /**
     * Constructor to Factory.
     * @param clusterInformation {@link ClusterInformation}
     */
    public AbstractCassandraFactory(ClusterInformation clusterInformation) {
        this.clusterInformation = clusterInformation;
		initConnection();
	}

    private ClusterInformation clusterInformation;

    private Cluster cluster;

    private Session session;

    protected Cluster getCluster() {
    	return cluster;
    }

    @Override
    public List<String> getHosts() {
    	return clusterInformation.getHosts();
    }

    @Override
    public String getKeySpace() {
		return clusterInformation.getKeySpace();
	}

    @Override
    public int getPort() {
    	return clusterInformation.getPort();
    }

    @Override
    public Session getSession() {
    	return session;
    }

    protected boolean fixColumnFamily(Session session, String familyColumn,
            Class<?> class1) {
        return new FixColumnFamily().verifyColumnFamily(session,
                clusterInformation.getKeySpace(), familyColumn, class1);
	}

    @Override
    public void createKeySpace(String keySpace,
            ReplicaStrategy replicaStrategy, int factor) {
        verifyKeySpace(keySpace, getSession(), replicaStrategy, factor);
    }

    @Override
    public void close() {
        if (!cluster.isClosed()) {
            cluster.close();
        }
        if (!session.isClosed()) {
            session.close();
        }
    }

    protected void verifyKeySpace(String keySpace, Session session,
            ReplicaStrategy replicaStrategy, int factor) {
		new FixKeySpace().verifyKeySpace(keySpace, session, replicaStrategy, factor);
	}
	protected void verifyKeySpace(String keySpace, Session session) {
		new FixKeySpace().verifyKeySpace(keySpace, session);
	}

	/**
	 * init the default connection.
	 */
    private  void initConnection() {
        cluster = clusterInformation.build();
        session = cluster.connect();
        new FixKeySpace().createKeySpace(clusterInformation.toInformationKeySpace(), getSession());
    }

}
