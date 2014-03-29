package org.easycassandra.persistence.cassandra;

import com.datastax.driver.core.ConsistencyLevel;

/**
 * Base to cassandra databases, the interface has all resources that may use in
 * Cassandra database.
 * This class is base to both sync and async persistence on Cassandra.
 * @author otaviojava
 */
public interface BasePersistence {
    /**
     * insert a object.
     * @param bean the bean
     * @param <T> kind of object
     * @return the object persisted
     */
    <T> boolean insert(T bean);
    /**
     * insert a object.
     * @param bean the bean
     * @param <T> kind of object
     * @param consistency the consistency
     * @return the object persisted
     */
    <T> boolean insert(T bean, ConsistencyLevel consistency);
    /**
     * insert a list of objects.
     * @param beans the entities
     * @param <T> kind of object
     * @return the entities persisted
     */
    <T> boolean insert(Iterable<T> beans);
    /**
     * insert a list of objects.
     * @param beans the entities
     * @param consistency the consistency
     * @param <T> kind of object
     * @return the entities persisted
     */
    <T> boolean insert(Iterable<T> beans, ConsistencyLevel consistency);

    /**
     * remove an object.
     * @param bean the bean
     * @param <T> kind of object
     * @return if process was success
     */
    <T> boolean delete(T bean);
    /**
     * remove an object.
     * @param bean the bean
     * @param <T> kind of object
     * @param consistency the consistency
     * @return if process was success
     */
    <T> boolean delete(T bean, ConsistencyLevel consistency);
    /**
     * remove a list object.
     * @param beans the beans
     * @param <T> kind of object
     * @return if process was success
     */
    <T> boolean delete(Iterable<T> beans);
    /**
     * remove a list object.
     * @param beans the beans
     * @param <T> kind of object
     * @param consistency the consistency
     * @return if process was success
     */
    <T> boolean delete(Iterable<T> beans, ConsistencyLevel consistency);
     /**
      * update the object on cassandra.
      * @param bean the bean
      * @param <T> kind of object
      * @return the object updated.
      */
    <T> boolean update(T bean);
    /**
     * update the object on cassandra.
     * @param bean the bean
     * @param <T> kind of object
     * @param consistency the consistency
     * @return the object updated.
     */
    <T> boolean update(T bean, ConsistencyLevel consistency);
    /**
     * update the list of objects.
     * @param beans the beans
     * @param <T> kind of object
     * @return the list updated
     */
    <T> boolean update(Iterable<T> beans);
    /**
     * update the list of objects.
     * @param beans the beans
     * @param consistency the consistency
     * @param <T> kind of object
     * @return the list updated
     */
    <T> boolean update(Iterable<T> beans, ConsistencyLevel consistency);

    /**
     * delete by key.
     * @param key  the key
     * @param bean the bean
     * @param <T> kind of object
     * @param <K> kind of key
     * @return if was with success the process
     */
    <K, T> boolean deleteByKey(K key, Class<T> bean);
    /**
     * delete by key.
     * @param key  the key
     * @param bean the bean
     * @param consistency the consistency
     * @param <T> kind of object
     * @param <K> kind of key
     * @return if was with success the process
     */
    <K, T> boolean deleteByKey(K key, Class<T> bean,
            ConsistencyLevel consistency);
    /**
     * execute a query.
     * @param query the query
     * @return if was with success
     */
    boolean executeUpdate(String query);
    /**
     * remove all entities on column families.
     * @param bean the bean
     * @param <T> kind of object
     */
    <T> void removeAll(Class<T> bean);
}
