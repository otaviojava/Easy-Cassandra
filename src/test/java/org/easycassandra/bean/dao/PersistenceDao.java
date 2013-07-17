package org.easycassandra.bean.dao;

import java.util.List;

import org.easycassandra.persistence.EasyCassandraManager;
import org.easycassandra.persistence.PersistenceCassandra;

public class PersistenceDao<T> {

    private PersistenceCassandra persistence;
    private Class<T> baseClass;

    public PersistenceDao(Class<T> baseClass) {
        this.baseClass = baseClass;
        persistence = EasyCassandraManager.getPersistence("localhost", "javabahia");
        EasyCassandraManager.addFamilyObject(baseClass, "javabahia");
    }

    public boolean insert(T bean) {
        return persistence.insert(bean);
    }

    public boolean remove(T bean) {
        return persistence.delete(bean);
    }

    public boolean removeFromRowKey(Object rowKey) {
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

    public List<T> listByIndex(Object index) {
        return persistence.findByIndex(index, baseClass);
    }

    public Long count() {
        return persistence.count(baseClass);
    }
//
//    public List<T> findKeyIn(Object... key) {
//        return persistence.findByKeyIn(baseClass, key);
//    }

    public boolean executeUpdateCql(String string) {
        return persistence.executeUpdate(string);
    }

//    public List<Map<String, String>> executeCql(String string) {
//        return persistence.executeCql(string);
//    }
//
//    public JCassandra createJCassandra(String cql) {
//        return persistence.createJCassandra(cql);
//    }
}
