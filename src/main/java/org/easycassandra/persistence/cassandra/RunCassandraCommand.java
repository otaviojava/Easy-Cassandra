package org.easycassandra.persistence.cassandra;

import java.util.LinkedList;
import java.util.List;

import com.datastax.driver.core.Session;

public class RunCassandraCommand {

	
	public <T> T  insert(T bean,Session session){
          new InsertQuery().prepare(bean, session);
          return bean;
	}
	
	public <T> boolean delete(T bean,Session session) {
		return new DeleteQuery().deleteByKey(bean, session);
	}
	
	public <T> List<T> findAll(Class<T> bean,Session session){
		return new FindAllQuery().listAll(bean, session);
	}
	
	public <T> T findByKey(Object key, Class<T> bean,Session session){
		return new FindByKeyQuery().findByKey(key, bean, session);
	}
	
	public boolean deleteByKey(Object key, Class<?> bean,Session session){
		return new DeleteQuery().deleteByKey(key, bean, session);
	}
	public <K, T> void delete(Iterable<K> keys, Class<T> entity,Session session){
		
		for(K key:keys){
			deleteByKey(keys, entity, session);
		}
	}
	
	public <K> boolean deleteByKey(Iterable<K> keys, Class<?> bean,Session session){
		for(K key:keys){
		return new DeleteQuery().deleteByKey(key, bean, session);
		}
		return true;
	}
	
	public <T> List<T> findByIndex(Object index, Class<T> bean,Session session){
		return new FindByIndexQuery().findByIndex(index, bean, session);
	}
	
	public <T> Long count(Class<T> bean,Session session){
		return new CountQuery().count(bean, session);
	}
	
	public <T> boolean insert(Iterable<T> beans,Session session){
		for(T bean:beans){
			insert(bean,session);
		}
		return true;
	}
	public <T> boolean delete(Iterable<T> beans,Session session) {
		for(T bean:beans){
			delete(bean,session);
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
