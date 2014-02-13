package org.easycassandra.bean.dao;

import java.util.List;

import org.easycassandra.Constants;
import org.easycassandra.persistence.cassandra.BatchBuilder;
import org.easycassandra.persistence.cassandra.ClusterInformation;
import org.easycassandra.persistence.cassandra.DeleteBuilder;
import org.easycassandra.persistence.cassandra.EasyCassandraManager;
import org.easycassandra.persistence.cassandra.InsertBuilder;
import org.easycassandra.persistence.cassandra.Persistence;
import org.easycassandra.persistence.cassandra.SelectBuilder;
import org.easycassandra.persistence.cassandra.UpdateBuilder;

/**
 * Base to persistence.
 * @author otaviojava
 * @param <T> the entity
 * @param <K> the key
 */
public class PersistenceDao<T, K> {

    private Persistence persistence;
    private Class<T> baseClass;
    private static EasyCassandraManager easyCassandraManager;

    {
        ClusterInformation clusterInformation = new ClusterInformation();
        clusterInformation.addHost(Constants.HOST).withDefaultKeySpace(Constants.KEY_SPACE);
        easyCassandraManager = new EasyCassandraManager(clusterInformation);
    }
    /**
     * the constructor.
     * @param baseClass the class
     */
    public PersistenceDao(Class<T> baseClass) {
        this.baseClass = baseClass;

        persistence = easyCassandraManager.getPersistence();
        easyCassandraManager.addFamilyObject(baseClass, Constants.KEY_SPACE);

    }
    /**
     * insert.
     * @param bean the bean
     * @return if was with success
     */
    public boolean insert(T bean) {
        return persistence.insert(bean);
    }
    /**
     * remove.
     * @param bean the bean
     * @return if was with success
     */
    public boolean remove(T bean) {
        return persistence.delete(bean);
    }
    /**
     * removeFromRowKey.
     * @param rowKey the rowkey
     * @return if was with success
     */
    public boolean removeFromRowKey(K rowKey) {
        return persistence.deleteByKey(rowKey, baseClass);
    }
    /**
     * update.
     * @param bean the bean
     * @return the bean
     */
    public boolean update(T bean) {
        return persistence.update(bean);
    }
    /**
     * retrieve.
     * @param id the id
     * @return the object
     */
    public T retrieve(Object id) {
        return   persistence.findByKey(id, baseClass);
    }

    /**
     * listAll.
     * @return list all
     */
    public List<T> listAll() {
        return persistence.findAll(baseClass);
    }
    /**
     * listByIndex.
     * @param indexName the indexName
     * @param index the index
     * @return the list
     */
    public List<T> listByIndex(String indexName, Object index) {
        return persistence.findByIndex(indexName, index, baseClass);
    }

    /**
     * Find by key and annotated index.
     * @param id the id
     * @param index the index
     * @author Nenita Casuga
     * @since 10/30/2013
     * @return the list
     */
    public List<T> listByKeyAndIndex(Object id, Object index) {
        return persistence.findByKeyAndIndex(id, index, baseClass);
    }

    /**
     * listByKeyAndIndexRange.
     * @param id the id
     * @param indexStart the indexStart
     * @param indexEnd the indexEnd
     * @param inclusive the inclusive
     * @author Nenita Casuga
     * @since 10/30/201
     * @return the list
     */
    public List<T> listByKeyAndIndexRange(Object id, Object indexStart,
            Object indexEnd, boolean inclusive) {
        return persistence.findByKeyAndIndexRange(id, indexStart, indexEnd,
                inclusive, baseClass);
    }
    /**
     * execute a query.
     * @param query the query
     * @return if was with success
     */
    public boolean executeUpdate(String query) {
    	return persistence.executeUpdate(query);
    }
    /**
     * count.
     * @return the count.
     */
    public Long count() {
        return persistence.count(baseClass);
    }
    /**
     * execute the query.
     * @param string the query
     * @return if was with success
     */
    public boolean executeUpdateCql(String string) {
        return persistence.executeUpdate(string);
    }
    /**
     * call select.
     * @return select
     */
    public SelectBuilder<T> select() {
        return persistence.selectBuilder(baseClass);
    }
    /**
     * call the update bean.
     * @return update
     */
    public UpdateBuilder<T> update() {
        return persistence.updateBuilder(baseClass);
    }
    /**
     * call the insert bean.
     * @return insert
     */
    public InsertBuilder<T> insertBuilder() {
        return persistence.insertBuilder(baseClass);
    }
    /**
     * call the insert bean.
     * @param bean the bean
     * @return insert
     */
    public InsertBuilder<T> insertBuilder(T bean) {
        return persistence.insertBuilder(bean);
    }
    /**
     *call the delete.
     * @param columnNames the column names
     * @return delete
     */
    public DeleteBuilder<T> deleteBuilder(String... columnNames) {
        return persistence.deleteBuilder(baseClass, columnNames);
    }
    /**
     * call the delete.
     * @param id the id
     * @param columnNames the column names
     * @return delete buider
     */
    public DeleteBuilder<T> deleteBuilder(Object id, String... columnNames) {
        return persistence.deleteBuilder(baseClass, id, columnNames);
    }
    /**
     * remove all.
     */
    public void removeAll() {
        persistence.removeAll(baseClass);
    }
    /**
     *call the delete.
     * @return delete
     */
    public BatchBuilder batchBuilder() {
        return persistence.batchBuilder();
    }

}
