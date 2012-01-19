package org.easycassandra.persistence;

import java.util.Objects;
import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.Cassandra.Client;
import org.apache.thrift.transport.TTransport;

/**
 *Information about Cassandra's Connection
 * @author otavio
 */
class Connection {

    /**
     * The host name
     */
    private String host;
    
    /**
     * The keyspace Name
     */
    private String keyspace;
    
    /**
     * The number's port
     */
    private Integer port;
    
    /**
     * @see TTransport
     */
    private TTransport transport;
    
    /**
     * @see Cassandra#Client
     */
    private Cassandra.Client client;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
    
    

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public TTransport getTransport() {
        return transport;
    }

    public void setTransport(TTransport transport) {
        this.transport = transport;
    }

    @Override
    public String toString() {
        return host + keyspace + port;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        if (obj instanceof Connection) {

            return obj.toString().equalsIgnoreCase(this.toString());
        }

        return false;
    }

    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.host);
        hash = 89 * hash + Objects.hashCode(this.keyspace);
        hash = 89 * hash + Objects.hashCode(this.port);
        return hash;
    }

    public Connection() {
    }

    public Connection(String host, String keyspace, Integer port) {
        this.host = host;
        this.keyspace = keyspace;
        this.port = port;
    }
    
    
}
