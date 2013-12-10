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
package org.easycassandra.persistence.cassandra.spring;

import java.util.List;

import org.easycassandra.persistence.cassandra.CassandraFactory;
import org.easycassandra.persistence.cassandra.RunCassandraCommand;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Session;

/**
 * Class to persist information in cassandra database
 * 
 * @author otaviojava
 * 
 */
public class SimpleCassandraTemplateImpl implements CassandraTemplate {

	
	 private Session session;
	    /**
	     * when don't define the persistence will use it as keyspace
	     */
	    private String keySpace;
	
	private RunCassandraCommand command = new RunCassandraCommand();
	
	public SimpleCassandraTemplateImpl(CassandraFactory factory) {
	    this.session = factory.getSession();
        this.keySpace = factory.getKeySpace();
        setSession();
	}

	public SimpleCassandraTemplateImpl(Session session, String keySpace) {
	    this.session = session;
        this.keySpace = keySpace;
        setSession();
	}

	@Override
	public <T> T save(T entity) {
		
		return command.insert(entity, session,keySpace);
	}

	@Override
	public <T> Iterable<T> save(Iterable<T> entities) {
		command.insert(entities, session,keySpace);
		return entities;
	}

	@Override
	public <T> void delete(T entity) {
		command.delete(entity, session,keySpace);
	}

	@Override
	public <T> void delete(Iterable<T> entities) {
		command.delete(entities, session,keySpace);
	}

	@Override
	public <K> void delete(K key, Class<?> entity) {
		command.deleteByKey(key, entity, session,keySpace);
		
	}

	@Override
	public <K, T> void delete(Iterable<K> keys, Class<T> entity) {
		command.deleteByKey(keys, entity, session,keySpace);
	}

	@Override
	public <T> void deleteAll(Class<T> entity) {
		command.removeAll(entity, session);
	}

	@Override
	public <T> T update(T entity) {
		return save(entity);
	}

	@Override
	public <T> Iterable<T> update(Iterable<T> entities) {
		return save(entities);
	}

	@Override
	public <T, K> T findOne(K key, Class<T> entity) {
		return command.findByKey(key, entity, session,keySpace);
	}

	@Override
	public <T, K> List<T> findAll(Iterable<K> keys, Class<T> entity) {
		return command.findByKeys(keys, entity, session,keySpace);
	}

	@Override
	public <T> List<T> findAll(Class<T> entity) {
		return command.findAll(entity, session,keySpace);
	}

	@Override
	public <T,I> List<T> findByIndex(String indexName,I index,Class<T> entity) {
		return command.findByIndex(indexName,index, entity, session,keySpace);
	}

	@Override
	public <K,T>boolean exist(K key, Class<T> entity){
		
		return findOne(key, entity) !=null;
	}

	@Override
	public void executeUpdate(String query) {
		session.execute(query);
	}

	@Override
	public <T>long count(Class<T> entity){
		return command.count(entity, session,keySpace);
	}

	
	 private void setSession(){
	    	session.execute("use "+keySpace);
	    }

	@Override
	public <T> T save(T entity, ConsistencyLevel consistency) {
		return command.insert(entity, session, keySpace,consistency);
	}

	@Override
	public <T> Iterable<T> save(Iterable<T> entities,ConsistencyLevel consistency) {
		command.insert(entities, session, keySpace, consistency);
		return entities;
	}

	@Override
	public <T> void delete(T entity, ConsistencyLevel consistency) {
		command.delete(entity, session, keySpace,consistency);
	}

	@Override
	public <T> void delete(Iterable<T> entities, ConsistencyLevel consistency) {
		 command.delete(entities, session, keySpace,consistency);
	}

	@Override
	public <K> void delete(K key, Class<?> entity, ConsistencyLevel consistency) {
		command.deleteByKey(key, entity, session, keySpace,consistency);
	}

	@Override
	public <K, T> void delete(Iterable<K> keys, Class<T> entity,ConsistencyLevel consistency) {
		command.deleteByKey(keys, entity, session, keySpace, consistency);
	}

	@Override
	public <T> T update(T entity, ConsistencyLevel consistency) {
		return save(entity, consistency);
	}

	@Override
	public <T> Iterable<T> update(Iterable<T> entities,ConsistencyLevel consistency) {
		return save(entities, consistency);
	}

	@Override
	public <T, K> T findOne(K key, Class<T> entity, ConsistencyLevel consistency) {
		return command.findByKey(key, entity, session, keySpace,consistency);
	}

	@Override
	public <T, K> List<T> findAll(Iterable<K> keys, Class<T> entity,ConsistencyLevel consistency) {
		return command.findByKeys(keys, entity, session,keySpace,consistency);
	}

	@Override
	public <T> List<T> findAll(Class<T> entity, ConsistencyLevel consistency) {
		return command.findAll(entity, session, keySpace,consistency);
	}

	@Override
	public <T, I> List<T> findByIndex(String indexName, I index,Class<T> entity, ConsistencyLevel consistency) {
		return command.findByIndex(indexName, index, entity, session, keySpace, consistency);
	}

    /**
     * Find by key and annotated index
     *
     * @author Nenita Casuga
     * @since 10/31/2013
     */
    @Override
    public <T,I> List<T> findByKeyAndIndex(Object key, I index,Class<T> entity){
        return command.findByKeyAndIndex(key, index, entity, session, keySpace);
    }

    /**
     * Find by key and annotated index
     *
     * @author Nenita Casuga
     * @since 10/31/2013
     */
    @Override
    public <T,I> List<T> findByKeyAndIndex(Object key, I index,Class<T> entity, ConsistencyLevel consistency){
        return command.findByKeyAndIndex(key, index, entity, session, keySpace, consistency);
    }

    /**
     * Find by key and annotated index
     *
     * @author Nenita Casuga
     * @since 10/31/2013
     */
    @Override
    public <T,I> List<T> findByKeyAndIndexRange(Object key, I indexStart, I indexEnd, boolean exclusive, Class<T> entity){
        return command.findByKeyAndIndexRange(key, indexStart, indexEnd, exclusive, entity, session, keySpace);
    }

    /**
     * Find by key and annotated index
     *
     * @author Nenita Casuga
     * @since 10/31/2013
     */
    @Override
    public <T,I> List<T> findByKeyAndIndexRange(Object key, I indexStart, I indexEnd, boolean exclusive, Class<T> entity, ConsistencyLevel consistency){
        return command.findByKeyAndIndexRange(key, indexStart, indexEnd, exclusive, entity, session, keySpace, consistency);
    }

    @Override
	public <K, T> boolean exist(K key, Class<T> entity,	ConsistencyLevel consistency) {
		return findOne(key, entity, consistency) != null;
	}

	@Override
	public <T> long count(Class<T> bean, ConsistencyLevel consistency) {
		return command.count(bean, session, keySpace, consistency);
	}
}
