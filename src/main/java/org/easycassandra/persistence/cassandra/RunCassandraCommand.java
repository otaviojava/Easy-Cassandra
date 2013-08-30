package org.easycassandra.persistence.cassandra;

import java.util.LinkedList;
import java.util.List;

import com.datastax.driver.core.Session;

public class RunCassandraCommand {

	
	public <T> T  insert(T bean,Session session,String keySpace){
		
          new InsertQuery(keySpace).prepare(bean, session);
          return bean;
	}
	
	public <T> boolean delete(T bean,Session session,String keySpace) {
		return new DeleteQuery(keySpace).deleteByKey(bean, session);
	}
	
	public <T> List<T> findAll(Class<T> bean,Session session){
		return new FindAllQuery().listAll(bean, session);
	}
	
	public <T> T findByKey(Object key, Class<T> bean,Session session){
		return new FindByKeyQuery().findByKey(key, bean, session);
	}
	
	public boolean deleteByKey(Object key, Class<?> bean,Session session,String keySpace){
		return new DeleteQuery(keySpace).deleteByKey(key, bean, session);
	}
	public <K, T> void delete(Iterable<K> keys, Class<T> entity,Session session,String keySpace){
		
		for(K key:keys){
			deleteByKey(key, entity, session,keySpace);
		}
	}
	
	public <K> boolean deleteByKey(Iterable<K> keys, Class<?> bean,Session session,String keySpace){
		for(K key:keys){
		return new DeleteQuery(keySpace).deleteByKey(key, bean, session);
		}
		return true;
	}
	
	  /**
     * Edited by Dinusha Nandika
     * Add indexName parameter 
     */
	public <T> List<T> findByIndex(String indexName,Object index, Class<T> bean,Session session){
		return new FindByIndexQuery().findByIndex(indexName,index, bean, session);
	}
	
	public <T> List<T> findByIndex(Object index, Class<T> bean,Session session){
		return new FindByIndexQuery().findByIndex(index, bean, session);
	}
	
	public <T> Long count(Class<T> bean,Session session){
		return new CountQuery().count(bean, session);
	}
	
	public <T> boolean insert(Iterable<T> beans,Session session,String keySpace){
		  new InsertQuery(keySpace).prepare(beans, session);
		return true;
	}
	public <T> boolean delete(Iterable<T> beans,Session session,String keySpace) {
		for(T bean:beans){
			delete(bean,session,keySpace);
		}
		return true;
	}
	public <K, T> List<T> findByKeys(Iterable<K> keys, Class<T> bean,Session session){
		List<T> beans = new LinkedList<T>();

		for (K key : keys) {
			T entity = findByKey(key, bean, session);
			if (entity != null) {
				beans.add(entity);
			}
		}

		return beans;
	}
	
	public <T> void removeAll(Class<T> bean,Session session){
		 new RemoveAll().truncate(bean, session);
	}
}
