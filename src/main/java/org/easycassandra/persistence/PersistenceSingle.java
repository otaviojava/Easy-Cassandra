package org.easycassandra.persistence;

import java.util.concurrent.atomic.AtomicReference;

import org.apache.cassandra.thrift.Cassandra.Client;

/**
 * The class to persist and retrieve object using only
 *  a single Client in Cassandra
 * @author otavio
 *
 */
public class PersistenceSingle extends Persistence {
	
	 PersistenceSingle(Client client, AtomicReference<ColumnFamilyIds> superColumnsRef, ConnectionInformation connectionInformation) {
		super(superColumnsRef, connectionInformation);
		this.client=client;
	 }
	 /**
	  * 
	  */
	 

	/**
     * Client's Cassandra
     */
    private Client client = null;

	@Override
	public  Client getClient() {
        return client;
    }

    @Override
    public int size() {
        return 1;
    }


}
