package org.easycassandra.bean.dao;

import java.util.List;

import org.easycassandra.bean.query.SimpleID;
import org.easycassandra.persistence.cassandra.BatchBuilder;
import org.easycassandra.persistence.cassandra.DeleteBuilder;
import org.easycassandra.persistence.cassandra.InsertBuilder;
import org.easycassandra.persistence.cassandra.PersistenceAsync;
import org.easycassandra.persistence.cassandra.ResultAsyncCallBack;
import org.easycassandra.persistence.cassandra.SelectBuilder;
import org.easycassandra.persistence.cassandra.UpdateBuilder;

/**
 * Base to persistence.
 * @author otaviojava
 * @param <T> the entity
 * @param <K> the key
 */
public class PersistenceDaoAsync<T, K> {

    private PersistenceAsync persistence;
    private Class<T> baseClass;

    /**
     * the constructor.
     * @param baseClass the class
     */
    public PersistenceDaoAsync(Class<T> baseClass) {
        this.baseClass = baseClass;

        persistence = PersistenceUtil.INSTANCE.getPersistenceAsync();

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
     * @param resultCallBack the callback is invoked after the function returns
     */
    public void retrieve(K id, ResultAsyncCallBack<T> resultCallBack) {
        persistence.findByKey(id, baseClass, resultCallBack);
    }

    /**
     * listAll.
     * @param resultCallBack the callback is invoked after the function returns
     */
    public void listAll(ResultAsyncCallBack<List<T>> resultCallBack) {
        persistence.findAll(baseClass, resultCallBack);
    }
    /**
     * listByIndex.
     * @param indexName the indexName
     * @param index the index
     * @param resultCallBack the callback is invoked after the function returns
     */
    public void listByIndex(String indexName, Object index,
            ResultAsyncCallBack<List<T>> resultCallBack) {
        persistence.findByIndex(indexName, index, baseClass, resultCallBack);
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
     * @param resultCallBack the callback is invoked after the function returns
     */
    public void count(ResultAsyncCallBack<Long> resultCallBack) {
        persistence.count(baseClass, resultCallBack);
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
    /**
     * call the update.
     * @param id the id
     * @return update builder
     */
    public UpdateBuilder<T> update(SimpleID id) {

        return persistence.updateBuilder(baseClass, id);
    }

}
