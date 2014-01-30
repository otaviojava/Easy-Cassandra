package org.easycassandra.persistence.cassandra;

import org.easycassandra.ReplicaStrategy;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
/**
 * Template of CassandraFactory.
 * @author otaviojava
 *
 */
class AbstractCassandraFactory implements CassandraFactory{

	public AbstractCassandraFactory(String host,String keySpace){
		this.hostDefault = host;
		this.keySpaceDefault = keySpace;
		this.port = PORT_DEFAULT;
		initConection();
	}
	public AbstractCassandraFactory(String host,String keySpace,int port){
		this.hostDefault = host;
		this.keySpaceDefault = keySpace;
		this.port = port;
		initConection();
	}

	public AbstractCassandraFactory(String host,String keySpace,int port, String user, String password){
		this.hostDefault = host;
		this.keySpaceDefault = keySpace;
		this.port = port;
		initConectionCredentials(user, password);
	}

	private static final int PORT_DEFAULT=9042;

    private Cluster cluter;

    private String hostDefault;

    private String keySpaceDefault;

    private int port;


    protected Cluster getCluster(){
    	return cluter;
    }

    @Override
    public String getHost(){
    	return hostDefault;
    }

    @Override
    public String getKeySpace() {
		return keySpaceDefault;
	}

    @Override
    public int getPort(){
    	return port;
    }

    @Override
    public Session getSession(String host) {
    	return verifyHost(host, port).connect();
    }

    @Override
    public Session getSession() {
    	return verifyHost(hostDefault, port).connect();
    }
    @Override
    public Session getSession(String host, int port) {
    	return createSession(host, port, keySpaceDefault);
    }

	@Override
	public Session getSession(String host, int port, String user,
			String password) {
		
    	return getSession(host, port, keySpaceDefault, user, password);
	}
	@Override
	public Session getSession(String host, int port, String keySpace,
			String user, String password) {
		Session session = verifyHost(host, port, user, password).connect();
		new FixKeySpace().verifyKeySpace(keySpaceDefault, session );
    	return session;
	}

    protected Session createSession(String host, int port, String keySpace){
    	new FixKeySpace().verifyKeySpace(keySpace, verifyHost(host,port).connect());
    	return verifyHost(host,port).connect();
    }

    /**
     * verifies if the host is equals host exists,
     * if different will create a other new cluster.
     * @param host
     */
	protected Cluster verifyHost(String host,int port) {
		if (!this.hostDefault.equals(host)) {
             return Cluster.builder().withPort(port).addContactPoints(host).build();
        }
		return cluter;
	}
	
	protected Cluster verifyHost(String host,int port, String user, String password) {
		if (!this.hostDefault.equals(host)) {
             return Cluster.builder().withPort(port).addContactPoints(host).withCredentials(user, password).build();
        }
		return cluter;
	}
	protected boolean fixColumnFamily(Session session,String familyColumn,Class<?> class1){
		return new FixColumnFamily().verifyColumnFamily(session, familyColumn,class1);
	}


	protected void verifyKeySpace(String keySpace,Session session,ReplicaStrategy replicaStrategy, int factor) {
		new FixKeySpace().verifyKeySpace(keySpace, session, replicaStrategy,factor);
	}
	protected void verifyKeySpace(String keySpace,Session session) {
		new FixKeySpace().verifyKeySpace(keySpace, session);
	}

	/**
	 * init the default connection.
	 */
    private  void initConection(){
    	 cluter = Cluster.builder().withPort(port).addContactPoints(hostDefault).build();
    	 new FixKeySpace().verifyKeySpace(keySpaceDefault, getSession());
    }
    
    private  void initConectionCredentials(String user, String password){
   	 cluter = Cluster.builder().withPort(port).withCredentials(user, password).addContactPoints(hostDefault).build();
   	 new FixKeySpace().verifyKeySpace(keySpaceDefault, getSession());
   }

}
