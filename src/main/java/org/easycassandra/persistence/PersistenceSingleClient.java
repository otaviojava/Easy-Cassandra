package org.easycassandra.persistence;

import java.util.concurrent.atomic.AtomicReference;

import org.apache.cassandra.thrift.Cassandra.Client;

/**
 * The class to persist and retrieve object using only
 *  a single Client in Cassandra
 * @author otavio
 *
 */
public class PersistenceSingleClient extends Persistence {
	 PersistenceSingleClient(Client client,	AtomicReference<ColumnFamilyIds> superColumnsRef, String keyStore) {
		super(superColumnsRef, keyStore);
		this.client=client;
	 }

	/**
     * Client's Cassandra
     */
    private Client client = null;

	@Override
	public  Client getClient() {
        return client;
    }

	

}
