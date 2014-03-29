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

import org.easycassandra.persistence.cassandra.BatchBuilder;
import org.easycassandra.persistence.cassandra.BuilderPersistence;
import org.easycassandra.persistence.cassandra.BuilderPersistenceImpl;
import org.easycassandra.persistence.cassandra.CassandraFactory;
import org.easycassandra.persistence.cassandra.DeleteBuilder;
import org.easycassandra.persistence.cassandra.InsertBuilder;
import org.easycassandra.persistence.cassandra.RunCassandraCommand;
import org.easycassandra.persistence.cassandra.SelectBuilder;
import org.easycassandra.persistence.cassandra.UpdateBuilder;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Session;

/**
 * Class to persist information in cassandra database.
 * @author otaviojava
 */
public class SimpleCassandraTemplateImpl implements CassandraTemplate {


    private Session session;
    /**
     * when don't define the persistence will use it as keyspace.
     */
    private String keySpace;

	private RunCassandraCommand command;

	private BuilderPersistence builderPersistence;

	/**
	 * return a simple template of cassandra.
	 * @param factory the factory
	 */
    public SimpleCassandraTemplateImpl(CassandraFactory factory) {
	    this.session = factory.getSession();
        this.keySpace = factory.getKeySpace();
        command = new RunCassandraCommand(keySpace);
        builderPersistence = new BuilderPersistenceImpl(session, keySpace);
	}

    /**
     * constructor using sessin and keySpace.
     * @param session the session
     * @param keySpace the keyspace
     */
	public SimpleCassandraTemplateImpl(Session session, String keySpace) {
	    this.session = session;
        this.keySpace = keySpace;
        command = new RunCassandraCommand(keySpace);
        builderPersistence = new BuilderPersistenceImpl(session, keySpace);
	}

	@Override
	public <T> T save(T entity) {

		return command.insert(entity, session);
	}

	@Override
	public <T> Iterable<T> save(Iterable<T> entities) {
		command.insert(entities, session);
		return entities;
	}

	@Override
	public <T> void delete(T entity) {
		command.delete(entity, session);
	}

	@Override
	public <T> void delete(Iterable<T> entities) {
		command.delete(entities, session);
	}

	@Override
	public <K> void delete(K key, Class<?> entity) {
		command.deleteByKey(key, entity, session);

	}

	@Override
	public <K, T> void delete(Iterable<K> keys, Class<T> entity) {
		command.deleteByKey(keys, entity, session);
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
		return command.findByKey(key, entity, session);
	}

	@Override
	public <T, K> List<T> findAll(Iterable<K> keys, Class<T> entity) {
		return command.findByKeys(keys, entity, session);
	}

	@Override
	public <T> List<T> findAll(Class<T> entity) {
		return command.findAll(entity, session);
	}

	@Override
    public <T, I> List<T> findByIndex(String indexName, I index, Class<T> entity) {
        return command.findByIndex(indexName, index, entity, session);
    }

	@Override
    public <K, T> boolean exist(K key, Class<T> entity) {

		return findOne(key, entity) != null;
	}

	@Override
	public void executeUpdate(String query) {
		session.execute(query);
	}

	@Override
	public <T>long count(Class<T> entity) {
		return command.count(entity, session);
	}


	@Override
	public <T> T save(T entity, ConsistencyLevel consistency) {
		return command.insert(entity, session, consistency);
	}

	@Override
	public <T> Iterable<T> save(Iterable<T> entities, ConsistencyLevel consistency) {
		command.insert(entities, session, consistency);
		return entities;
	}

	@Override
	public <T> void delete(T entity, ConsistencyLevel consistency) {
		command.delete(entity, session, consistency);
	}

	@Override
	public <T> void delete(Iterable<T> entities, ConsistencyLevel consistency) {
        command.delete(entities, session, consistency);
	}

	@Override
	public <K> void delete(K key, Class<?> entity, ConsistencyLevel consistency) {
		command.deleteByKey(key, entity, session, consistency);
	}

	@Override
	public <K, T> void delete(Iterable<K> keys, Class<T> entity, ConsistencyLevel consistency) {
		command.deleteByKey(keys, entity, session, consistency);
	}

	@Override
	public <T> T update(T entity, ConsistencyLevel consistency) {
		return save(entity, consistency);
	}

	@Override
	public <T> Iterable<T> update(Iterable<T> entities, ConsistencyLevel consistency) {
		return save(entities, consistency);
	}

	@Override
	public <T, K> T findOne(K key, Class<T> entity, ConsistencyLevel consistency) {
		return command.findByKey(key, entity, session, consistency);
	}

	@Override
	public <T, K> List<T> findAll(Iterable<K> keys, Class<T> entity, ConsistencyLevel consistency) {
        return command.findByKeys(keys, entity, session, consistency);
	}

	@Override
	public <T> List<T> findAll(Class<T> entity, ConsistencyLevel consistency) {
        return command.findAll(entity, session, consistency);
	}

	@Override
    public <T, I> List<T> findByIndex(String indexName, I index,
            Class<T> entity, ConsistencyLevel consistency) {
        return command.findByIndex(indexName, index, entity, session,
                consistency);
    }

    @Override
    public <T, I> List<T> findByKeyAndIndex(Object key, I index, Class<T> entity) {
        return command.findByKeyAndIndex(key, index, entity, session);
    }

    @Override
    public <T, I> List<T> findByKeyAndIndex(Object key, I index,
            Class<T> entity, ConsistencyLevel consistency) {
        return command.findByKeyAndIndex(key, index, entity, session,
                consistency);
    }

    @Override
    public <T, I> List<T> findByKeyAndIndexRange(Object key, I indexStart,
            I indexEnd, boolean exclusive, Class<T> entity) {
        return command.findByKeyAndIndexRange(key, indexStart, indexEnd,
                exclusive, entity, session);
    }

    @Override
    public <T, I> List<T> findByKeyAndIndexRange(Object key, I indexStart,
            I indexEnd, boolean exclusive, Class<T> entity,
            ConsistencyLevel consistency) {
        return command.findByKeyAndIndexRange(key, indexStart, indexEnd,
                exclusive, entity, session, consistency);
    }

    @Override
	public <K, T> boolean exist(K key, Class<T> entity,	ConsistencyLevel consistency) {
		return findOne(key, entity, consistency) != null;
	}

	@Override
	public <T> long count(Class<T> bean, ConsistencyLevel consistency) {
		return command.count(bean, session, consistency);
	}

	@Override
    public <T> SelectBuilder<T> selectBuilder(Class<T> classBean) {
        return builderPersistence.selectBuilder(classBean);
    }

    @Override
    public <T> InsertBuilder<T> insertBuilder(Class<T> classBean) {
        return builderPersistence.insertBuilder(classBean);
    }

    @Override
    public <T> InsertBuilder<T> insertBuilder(T classBean) {
        return builderPersistence.insertBuilder(classBean);
    }

    @Override
    public <T> UpdateBuilder<T> updateBuilder(Class<T> classBean) {
        return builderPersistence.updateBuilder(classBean);
    }


    @Override
    public <T> UpdateBuilder<T> updateBuilder(Class<T> classBean, Object key) {
        return builderPersistence.updateBuilder(classBean, key);
    }

    @Override
    public <T> DeleteBuilder<T> deleteBuilder(Class<T> classBean, String... columnNames) {
        return builderPersistence.deleteBuilder(classBean, columnNames);
    }

    @Override
    public <T, K> DeleteBuilder<T> deleteBuilder(Class<T> classBean, K key,
            String... columnNames) {

        return builderPersistence.deleteBuilder(classBean, key, columnNames);
    }

    @Override
    public BatchBuilder batchBuilder() {
        return builderPersistence.batchBuilder();
    }

}
