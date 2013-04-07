package org.easycassandra.persistence;

import java.io.Serializable;

import org.apache.cassandra.thrift.Cassandra.Client;

/**
 * This class store information about the conextion:
 * Host, port and keyspace
 * @author otaviojava
 *
 */
public class ConnectionInformation implements Serializable{

	private static final long serialVersionUID = -5534716353146801570L;
	/**
	 * host when there is Cassandra's conection
	 */
	private String host;
	/**
	 * tge keyspace for this conection
	 */
	private String keySpace;
	/**
	 * number of port to this conection
	 */
	private int port;
	
	private Client client;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
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
	public ConnectionInformation() {}
	public ConnectionInformation(String host, String keySpace, int port) {
		this.host = host;
		this.keySpace = keySpace;
		this.port = port;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result
				+ ((keySpace == null) ? 0 : keySpace.hashCode());
		result = prime * result + port;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConnectionInformation other = (ConnectionInformation) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (keySpace == null) {
			if (other.keySpace != null)
				return false;
		} else if (!keySpace.equals(other.keySpace))
			return false;
		if (port != other.port)
			return false;
		return true;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	
	
	
}
