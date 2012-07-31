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

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.cassandra.thrift.Cassandra.Client;

/**
 * The class to persists and retrieves objects using clients. 
 * The clients are used in randomly way.
 * @author otavio
 *
 */
public class PersistenceRandomClient extends Persistence{

	PersistenceRandomClient(List<Client> clients, AtomicReference<ColumnFamilyIds> superColumnsRef,
			String keySpace) {
		super(superColumnsRef, keySpace);
		this.clients=clients;
	}
	private List<Client> clients;
	@Override
	public Client getClient() {
		Random random=new Random();
		return clients.get(random.nextInt(clients.size()));
	}

	/**
	 * Return the number of connections in this client
	 * @return number of connection
	 */
	public int size(){
		
		return clients.size();
	}
	
}
