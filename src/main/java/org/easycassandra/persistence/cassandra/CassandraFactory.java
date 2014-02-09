package org.easycassandra.persistence.cassandra;

import java.util.List;

import org.easycassandra.ReplicaStrategy;

import com.datastax.driver.core.Session;
/**
 * the base of Cassandra factory.
 */
public interface CassandraFactory {

	/**
	 * returns the session of Cassandra Driver.
	 * @return session of cassandra
	 */
	Session getSession();


	/**
	 * returns the keyspace default.
	 * @return the keySpace
	 */
	String getKeySpace();

	/**
	 * returns the host default.
	 * @return the host
	 */
	List<String> getHosts();

	/**
	 * returns the port default.
	 * @return the port
	 */
	int getPort();
	/**
	 * create a new keySpace on the cluster.
	 * @param keySpace - the keySpace name
	 * @param replicaStrategy - {@link ReplicaStrategy}
	 * @param factor - the number of replica factor
	 */
	void createKeySpace(String keySpace, ReplicaStrategy replicaStrategy, int factor);
}
