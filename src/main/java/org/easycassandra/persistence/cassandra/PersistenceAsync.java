package org.easycassandra.persistence.cassandra;

import java.util.List;

import com.datastax.driver.core.ConsistencyLevel;

/**
 * Do the same of {@link Persistence}, but all process are async.
 * @author otaviojava
 */
public interface PersistenceAsync extends BasePersistence, BuilderPersistence {


    /**
     * execute select * from column family.
     * @param bean the bean
     * @param resultCallBack the callback is invoked after the function returns
     * @param <T> kind of object
     */
    <T> void findAll(Class<T> bean, ResultAsyncCallBack<List<T>> resultCallBack);
    /**
     * execute select * from column family.
     * @param bean the bean
     * @param <T> kind of object
     * @param consistency the consistency
     * @param resultCallBack the callback is invoked after the function returns
     */
    <T> void findAll(Class<T> bean, ConsistencyLevel consistency,
            ResultAsyncCallBack<List<T>> resultCallBack
            );
    /**
     * find objects by keys.
     * @param keys the keys
     * @param bean the bean
     * @param <T> kind of object
     * @param <K> kind of key
     * @param resultCallBack the callback is invoked after the function returns
     */
    <K, T> void findByKeys(Iterable<K> keys, Class<T> bean,
            ResultAsyncCallBack<List<T>> resultCallBack);
    /**
     * find objects by keys.
     * @param keys the keys
     * @param bean the bean
     * @param <T> kind of object
     * @param <K> kind of key
     * @param consistency the consistency
     * @param resultCallBack the callback is invoked after the function returns
     */
    <K, T> void findByKeys(Iterable<K> keys, Class<T> bean,
            ConsistencyLevel consistency,
            ResultAsyncCallBack<List<T>> resultCallBack);

    /**
     * find object by key.
     * @param key the key
     * @param bean the bean
     * @param <T> kind of object
     * @param <K> kind of key
     * @param resultCallBack the callback is invoked after the function returns
     */
    <K, T> void findByKey(K key, Class<T> bean, ResultAsyncCallBack<T> resultCallBack);
    /**
     * find object by key.
     * @param key the key
     * @param bean the bean
     * @param <T> kind of object
     * @param <K> kind of key
     * @param consistency the consistency
     * @param resultCallBack the callback is invoked after the function returns
     */
    <K, T> void findByKey(K key, Class<T> bean, ConsistencyLevel consistency,
            ResultAsyncCallBack<T> resultCallBack);

    /**
     * return by index.
     * @param indexName the indexName
     * @param <T> kind of object
     * @param <I> kind of index
     * @param index the index
     * @param bean the bean
     * @param resultCallBack the callback is invoked after the function returns
     */
    <T, I> void findByIndex(String indexName, I index, Class<T> bean,
            ResultAsyncCallBack<List<T>> resultCallBack);
    /**
     * return by index.
     * @param <T> kind of object
     * @param <I> kind of index
     * @param index the index
     * @param bean the bean
     * @param indexName the indexName
     * @param consistency the consistency
     * @param resultCallBack the callback is invoked after the function returns
     */
    <T, I> void findByIndex(String indexName, I index, Class<T> bean,
            ConsistencyLevel consistency,
            ResultAsyncCallBack<List<T>> resultCallBack);
    /**
     * return by index.
     * @param <T> kind of object
     * @param <I> kind of index
     * @param index the index
     * @param bean the bean
     * @param resultCallBack the callback is invoked after the function returns
     */
    <T, I> void findByIndex(I index, Class<T> bean, ResultAsyncCallBack<List<T>> resultCallBack);
    /**
     * return by index.
     * @param <T> kind of object
     * @param <I> kind of index
     * @param index the index
     * @param bean the bean
     * @param resultCallBack the callback is invoked after the function returns
     * @param consistency the consistency
     */
    <T, I> void findByIndex(I index, Class<T> bean, ResultAsyncCallBack<List<T>> resultCallBack,
            ConsistencyLevel consistency);

    /**
     * counts a number of rows in column family.
     * @param bean the bean
     * @param <T> kind of entity
     * @param resultCallBack the callback is invoked after the function returns
     */
    <T> void count(Class<T> bean, ResultAsyncCallBack<Long> resultCallBack);
    /**
     * counts a number of rows in column family.
     * @param bean the bean
     * @param <T> kind of entity
     * @param consistency the consistency
     * @param resultCallBack the callback is invoked after the function returns
     */
    <T> void count(Class<T> bean, ConsistencyLevel consistency,
            ResultAsyncCallBack<Long> resultCallBack);
}
