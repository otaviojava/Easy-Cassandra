package org.easycassandra.bean.dao;

import java.util.List;
import java.util.Map;

import org.easycassandra.ConsistencyLevelCQL;
import org.easycassandra.persistence.EasyCassandraManager;
import org.easycassandra.persistence.JCassandra;
import org.easycassandra.persistence.Persistence;

public  class PersistenceDao<T> {
	
	   private Persistence persistence;
	   
	   @SuppressWarnings("rawtypes")
	   private Class baseClass;

	  
	    public PersistenceDao(Class<T> baseClass) {
	    	this.baseClass=baseClass;
	    	
	        persistence = EasyCassandraManager.getPersistence("javabahia", "localhost", 9160);
	    }

	    public boolean insert(T bean) {


	        return persistence.insert(bean,ConsistencyLevelCQL.ONE);

	    }

	    public boolean remove(T bean) {


	        return persistence.delete(bean);
	    }

	    public boolean removeFromRowKey(Object rowKey) {


	        return persistence.deleteByKeyValue(rowKey, baseClass);

	    }

	    public boolean update(T bean) {

	        return persistence.update(bean);

	    }

	    @SuppressWarnings("unchecked")
		public T retrieve(Object id) {

	    	return (T) persistence.findByKey(id, baseClass);


	    }

	    @SuppressWarnings("unchecked")
	    public List<T> listAll() {

	        return persistence.findAll(baseClass);

	    }

	    @SuppressWarnings("unchecked")
	    public List<T> listByIndex(Object index) {

	        return persistence.findByIndex(index, baseClass);

	    }
	    
	    public Long count() {
			return persistence.count(baseClass);
		}

	    @SuppressWarnings("unchecked")
		public List<T> findKeyIn(Object... key) {
	    	 return persistence.findByKeyIn( baseClass,key);
		}
	    
	    public boolean executeUpdateCql(String string) {
			
			return persistence.executeUpdateCql(string);
		}
	    
	    public List<Map<String, String>> executeCql(String string) {
			return persistence.executeCql(string);
		}

		public JCassandra createJCassandra(String cql) {
			
			return persistence.createJCassandra(cql);
		}
	    
	    
	    
}
