/*
 * Copyright 2013 Otávio Gonçalves de Santana (otaviojava)
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

import org.easycassandra.ReplicaStrategy;

import com.datastax.driver.core.Session;

class PersistenceCassandraImpl implements PersistenceCassandra {

	private Session session;
	/**
	 * when don't define the persistence will use it as keyspace
	 */
	private String keySpace;
	
	 PersistenceCassandraImpl(Session session) {
		this.session=session;
	}
	 PersistenceCassandraImpl(Session session,String keySpace) {
			
		    this.session=session;
			
			this.keySpace=keySpace;
			new FixKeySpace().verifyKeySpace(keySpace, session);
		}
	 
	public PersistenceCassandraImpl(Session session, String keySpace,ReplicaStrategy replicaStrategy, int factor) {
		this.session=session;
		this.keySpace=keySpace;
		new FixKeySpace().verifyKeySpace(keySpace, session,replicaStrategy,factor);
	}
	public void execute(String string) {
		session.execute(string);
	}
	@Override
	public boolean insert(Object bean) {
		return new InsertQuery().prepare(bean,session);
		
	}
	@Override
	public boolean delete(Object bean) {
		
		return new DeleteQuery().deleteByKey(bean,session);
	}
	@Override
	public boolean update(Object bean) {
		return new InsertQuery().prepare(bean,session);
	}
	@Override
	public <T> List<T> findAll(Class<T> bean) {
		return new FindAllQuery().listAll(bean, session);
	}
	@Override
	public <T> T findByKey(Object key, Class<T> bean) {
		return new FindByKeyQuery().findByKey(key, bean, session);
	}
	
	@Override
	public boolean deleteByKey(Object key, Class<?> bean) {
		return new DeleteQuery().deleteByKey(key, bean,session);
	}
	
	@Override
	public boolean executeUpdate(String query) {
		session.execute(query);
		return true;
	}
	
	@Override
	public <T> List<T> findByIndex(Object index, Class<T> bean) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Long count(Class<?> bean) {
		return new CountQuery().count(bean, session);
	}
	 

}
