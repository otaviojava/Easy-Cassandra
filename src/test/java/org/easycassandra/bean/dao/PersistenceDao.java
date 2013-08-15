package org.easycassandra.bean.dao;

import java.util.List;

import org.easycassandra.Constants;
import org.easycassandra.persistence.cassandra.EasyCassandraManager;
import org.easycassandra.persistence.cassandra.Persistence;

public class PersistenceDao<T,K> {

    private Persistence persistence;
    private Class<T> baseClass;
    private static EasyCassandraManager easyCassandraManager=new EasyCassandraManager(Constants.HOST,Constants.KEY_SPACE); 

    public PersistenceDao(Class<T> baseClass) {
        this.baseClass = baseClass;
        
        persistence = easyCassandraManager.getPersistence(Constants.HOST, Constants.KEY_SPACE);
        easyCassandraManager.addFamilyObject(baseClass, Constants.KEY_SPACE);
        
    }

    public boolean insert(T bean) {
        return persistence.insert(bean);
    }

    public boolean remove(T bean) {
        return persistence.delete(bean);
    }

    public boolean removeFromRowKey(K rowKey) {
        return persistence.deleteByKey(rowKey, baseClass);
    }

    public boolean update(T bean) {
        return persistence.update(bean);
    }

    public T retrieve(Object id) {
    	
        return   persistence.findByKey(id, baseClass);
    }

    public List<T> listAll() {
        return persistence.findAll(baseClass);
    }

    public List<T> listByIndex(String indexName,Object index) {
        return persistence.findByIndex(indexName,index, baseClass);
    }

    public Long count() {
        return persistence.count(baseClass);
    }

    public boolean executeUpdateCql(String string) {
        return persistence.executeUpdate(string);
    }

}
