package org.easycassandra.persistence.cassandra;

import com.datastax.driver.core.Session;
/**
 * the base of Cassandra factory.
 */
public interface CassandraFactory {

     /**
     * returns the session of Cassandra Driver.
     * @param host the host
     * @return the session
     */
    Session getSession(String host);

	/**
	 * returns the session of Cassandra Driver.
	 * @return session of cassandra
	 */
	Session getSession();

	/**
	 * returns the session of Cassandra Driver.
	 * @param host the host
	 * @param port the port
	 * @return the session
	 */
    Session getSession(String host, int port);

	/**
	 * returns the session of Cassandra Driver using credentials.
	 * @param host the host
	 * @param port the port
	 * @param user the user
	 * @param password the password
	 * @return the session with credentials
	 */
    Session getSession(String host, int port, String user, String password);
	/**
	 * returns the session of Cassandra Driver using credentials.
	 * @param host the host
	 * @param port the port
	 * @param keySpace the keySpace
	 * @param user the user
	 * @param password the password
	 * @return the sesion
	 */
    Session getSession(String host, int port, String keySpace, String user,
            String password);

	/**
	 * returns the keyspace default.
	 * @return the keySpace
	 */
	String getKeySpace();

	/**
	 * returns the host default.
	 * @return the host
	 */
	String getHost();

	/**
	 * returns the port default.
	 * @return the port
	 */
	int getPort();
}
