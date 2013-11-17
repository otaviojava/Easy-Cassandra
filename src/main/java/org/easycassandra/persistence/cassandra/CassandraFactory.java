package org.easycassandra.persistence.cassandra;

import com.datastax.driver.core.Session;
/**
 * the base of Cassandra factory.
 */
public interface CassandraFactory {

     /**
     * returns the session of Cassandra Driver.
     * @param host
     * @return
     */
     Session getSession(String host);

	/**
	 * returns the session of Cassandra Driver.
	 * @return
	 */
	Session getSession();

	/**
	 * returns the session of Cassandra Driver
	 * @param host
	 * @param port
	 * @return
	 */
	Session getSession(String host,int port);
	
	/**
	 * returns the keyspace default
	 * @return
	 */
	String getKeySpace();
	
	/**
	 * returns the host default
	 * @return
	 */
	String getHost();
	
	/**
	 * returns the port default
	 * @return
	 */
	int getPort();
}
