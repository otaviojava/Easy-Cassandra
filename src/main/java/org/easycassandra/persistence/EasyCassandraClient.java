/*
 * Copyright 2012 Otávio Gonçalves de Santana (otaviojava)
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.easycassandra.persistence;

//import java.util.Objects;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;

/**
 * Information about Cassandra's EasyCassandraClient
 *
 * @author otavio
 */
class EasyCassandraClient {

    private static final int HASH_CODE = 89;
    private static final int HASH_VALUE = 7;
    /**
     * The host name
     */
    private String host;
    /**
     * The number's port
     */
    private Integer port;
    /**
     * @see TTransport
     */
    private TTransport transport;
    
    /**
     * @see TProtocol
     */
    private TProtocol protocol;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

    
    
    public TProtocol getProtocol() {
		return protocol;
	}

	public void setProtocol(TProtocol protocol) {
		this.protocol = protocol;
	}

	@Override
    public String toString() {
        return host  + port;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        if (obj instanceof EasyCassandraClient) {

            return obj.toString().equalsIgnoreCase(this.toString());
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = HASH_VALUE;
        hash = HASH_CODE * hash + host.hashCode();
        hash = HASH_CODE * hash + (this.port);
        return hash;
    }

    public EasyCassandraClient() {
    }

    public EasyCassandraClient(String host, Integer port) {
        this.host = host;
        this.port = port;
    }
}
