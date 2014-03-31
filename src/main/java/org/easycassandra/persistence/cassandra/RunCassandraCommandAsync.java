package org.easycassandra.persistence.cassandra;

import java.util.List;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.Update;

/**
 * Facade to run Cassandra to async process.
 * @author otaviojava
 */
public class RunCassandraCommandAsync {

    private static final ConsistencyLevel DEFAULT_CASSANDRA_CL = ConsistencyLevel.ONE;

    private InsertQueryAsync insertQuery;
    private FindAllQueryAsync findAllQuery;
    private FindByKeyQueryAsync findByKeyQuery;
    private DeleteQueryAsync deleteQuery;
    private FindByIndexQueryAsync findByIndexQuery;
    private CountQueryAsync countQuery;
    private UpdateQuery updateQuery;
    private TruncateQueryAsync truncateQuery;

    /**
     * construct all class to run commands on cassandra.
     * @param keySpace the keyspace
     */
    public RunCassandraCommandAsync(String keySpace) {
        insertQuery = new InsertQueryAsync(keySpace);
        findAllQuery = new FindAllQueryAsync(keySpace);
        findByKeyQuery = new FindByKeyQueryAsync(keySpace);
        deleteQuery = new DeleteQueryAsync(keySpace);
        findByIndexQuery = new FindByIndexQueryAsync(keySpace);
        countQuery = new CountQueryAsync(keySpace);
        updateQuery = new UpdateQuery(keySpace);
        truncateQuery = new TruncateQueryAsync(keySpace);
    }

    /**
     * insert an object on keySpace from Session.
     * @param bean the object
     * @param session the session
     * @param <T> the object
     */
    public <T> void insert(T bean, Session session) {
        insert(bean, session, DEFAULT_CASSANDRA_CL);
    }

    /**
     * insert an object on keySpace from Session.
     * @param bean the object
     * @param session the session
     * @param consistencyLevel the consistency level of Cassandra
     * @param <T> the object
     */
    public <T> void insert(T bean, Session session,  ConsistencyLevel consistencyLevel) {
        insertQuery.prepareAsync(bean, session, consistencyLevel);
    }

    /**
     * insert a list of objects on keySpace from Session.
     * @param beans the list to persist on Cassandra
     * @param session the Session
     * @param <T> kind of object
     */
    public <T> void insert(Iterable<T> beans, Session session) {
        insert(beans, session, DEFAULT_CASSANDRA_CL);
    }

    /**
     * insert a list of objects on keySpace from Session.
     * @param beans the list to persist on Cassandra
     * @param session the Session
     * @param consistencyLevel the consistency Leval of Cassandra
     * @param <T> kind of object
     */
    public <T> void insert(Iterable<T> beans, Session session,
            ConsistencyLevel consistencyLevel) {
        insertQuery.prepareAsync(beans, session, consistencyLevel);
    }

    /**
     * Execute select * from ColumnFamily on Cassandra Data Base,
     * it's respect the Cassandra architecture.
     * @param bean kind of object
     * @param session the session
     * @param <T> kind of object
     * @param resultCallBack the callback is invoked after the function returns
     */
    public <T> void findAll(Class<T> bean, Session session,
            ResultAsyncCallBack<List<T>> resultCallBack) {
        findAll(bean, session, resultCallBack, DEFAULT_CASSANDRA_CL);
    }

    /**
     * Execute select * from ColumnFamily on Cassandra Data Base,
     * it's respect the Cassandra architecture.
     * @param bean kind of object
     * @param session the Session
     * @param <T> kind of object
     * @param consistency the consistency Leval on Cassandra
     * @param resultCallBack the callback is invoked after the function returns
     */
    public <T> void findAll(Class<T> bean, Session session,
            ResultAsyncCallBack<List<T>> resultCallBack,
            ConsistencyLevel consistency) {

        findAllQuery.listAllAsync(bean, session, resultCallBack, consistency);
    }

    /**
     * find an object from Cassandra by key.
     * @param key the key on Cassandra
     * @param bean the object
     * @param session the Session
     * @param <T> kind of object
     * @param resultCallBack the callback is invoked after the function returns
     */
    public <T> void findByKey(Object key, Class<T> bean, Session session,
            ResultAsyncCallBack<T> resultCallBack) {
        findByKey(key, bean, session, resultCallBack, DEFAULT_CASSANDRA_CL);
    }

    /**
     * find an object from Cassandra by key.
     * @param key the key on Cassandra
     * @param bean the object
     * @param session the Session
     * @param <T> kind of object
     * @param consistency the consistency Level on Cassandra
     * @param resultCallBack the callback is invoked after the function returns
     */
    public <T> void findByKey(Object key, Class<T> bean, Session session,
            ResultAsyncCallBack<T> resultCallBack, ConsistencyLevel consistency) {
        findByKeyQuery.findByKeyAsync(key, bean, session, consistency, resultCallBack);
    }

    /**
     * delete on object from cassandra by Key.
     * @param key the key
     * @param bean the kind of object
     * @param session the Session
     */
    public void deleteByKey(Object key, Class<?> bean, Session session) {
        deleteByKey(key, bean, session, DEFAULT_CASSANDRA_CL);
    }

    /**
     * delete on object from cassandra by Key.
     * @param key the key
     * @param bean the kind of object
     * @param session the Session
     * @param consistency the consistency Level
     */
    public void deleteByKey(Object key, Class<?> bean, Session session,
             ConsistencyLevel consistency) {
        deleteQuery.deleteByKeyAsync(key, bean, session, consistency);
    }
    /**
     * delete on object from cassandra by Key.
     * @param key the key
     * @param bean the bean
     * @return the delete
     */
    public Delete runDelete(Object key, Class<?> bean) {
        return deleteQuery.runDelete(key, bean, ConsistencyLevel.ONE);
    }
    /**
     * update on object from cassandra by key.
     * @param key the key
     * @param bean the bean
     * @return the update
     */
    public Update runUpdate(Object key, Class<?> bean) {
        return updateQuery.runUpdate(key, bean);
    }
    /**
     * delete on object from cassandra by Key.
     * @param session the Session
     * @param <K> the kind of key
     * @param keys the keys
     * @param entity the entity
     * @param <T> the kind of object
     * @return if execute the command
     */
    public <K, T> boolean deleteByKey(Iterable<K> keys, Class<T> entity,
            Session session) {
        return deleteByKey(keys, entity, session, DEFAULT_CASSANDRA_CL);
    }
    /**
     * delete on object from cassandra by Key.
     * @param session the Session
     * @param consistency the consistency Level
     * @param entity the entity
     * @param <K> the keys
     * @param keys the keys values
     * @param <T> the kind of object
     * @return if execute the command
     */
    public <K, T> boolean deleteByKey(Iterable<K> keys, Class<T> entity,
            Session session,  ConsistencyLevel consistency) {
        deleteQuery.deleteByKey(keys, entity, session,
                consistency);
        return true;
    }

    /**
     * delete on object from cassandra by Key.
     * @param bean the object
     * @param session the Session
     * @param <T> kind of object
     */
    public <T> void delete(T bean, Session session) {
        delete(bean, session, DEFAULT_CASSANDRA_CL);
    }
    /**
     * delete on object from cassandra by Key.
     * @param bean the kind of object
     * @param session the Session
     * @param <T> kind of object
     * @param consistency the consistency level
     */
    public <T> void delete(T bean, Session session,
            ConsistencyLevel consistency) {
        deleteQuery.deleteByKeyAsync(bean, session, consistency);
    }

    /**
     * delete a list of beans.
     * @param beans the beans
     * @param session the Session
     * @param <T> the kind of keys
     */
    public <T> void delete(Iterable<T> beans, Session session) {
        delete(beans, session, DEFAULT_CASSANDRA_CL);
    }

    /**
     * delete a list of beans.
     * @param beans the beans
     * @param session the Session
     * @param consistency the consistency Level on Cassandra
     * @param <T> th kind of object
     */
    public <T> void delete(Iterable<T> beans, Session session,
             ConsistencyLevel consistency) {
        deleteQuery.deleteByKeyAsync(beans, session, consistency);
    }


   /**
    * find list of objects from index.
    * @param indexName name of index
    * @param index index value
    * @param bean the kind of bean
    * @param session the Session
    * @param <T> kind of object
    * @param resultCallBack the callback is invoked after the function returns
    */
    public <T> void findByIndex(String indexName, Object index,
            Class<T> bean, Session session, ResultAsyncCallBack<List<T>> resultCallBack) {
        findByIndex(indexName, index, bean, session, DEFAULT_CASSANDRA_CL,
                resultCallBack);
    }
    /**
     * find list of objects from index.
     * @param indexName name of index
     * @param index index value
     * @param bean the kind of bean
     * @param session the Session
     * @param <T> kind of object
     * @param consistency the consistency level
     * @param resultCallBack the callback is invoked after the function returns
     */
    public <T> void findByIndex(String indexName, Object index,
            Class<T> bean, Session session, ConsistencyLevel consistency,
            ResultAsyncCallBack<List<T>> resultCallBack) {
        findByIndexQuery.findByIndexAsync(indexName, index, bean, session,
                consistency, resultCallBack);
    }

    /**
     * find list of objects from index.
     * The default will be the first field
     * @param index the index value
     * @param bean the kind of bean
     * @param session the Session
     * @param <T> kind of object
     * @param consistency the consistency level of cassandra
     * @param resultCallBack the callback is invoked after the function returns
     * @author Nenita Casuga
     * @since 10/30/2013
     */
    public <T> void findByIndex(Object index, Class<T> bean,
            Session session, ConsistencyLevel consistency,
            ResultAsyncCallBack<List<T>> resultCallBack) {
        findByIndexQuery.findByIndexAsync(index, bean, session,
                consistency, resultCallBack);
    }
    /**
     * find list of objects from index.
     * The default will be the first field
     * @param index the index value
     * @param bean the kind of bean
     * @param session the Session
     * @param resultCallBack the callback is invoked after the function returns
     * @param <T> kind of object
     */
    public <T> void findByIndex(Object index, Class<T> bean,
            Session session, ResultAsyncCallBack<List<T>> resultCallBack) {
        findByIndex(index, bean, session, DEFAULT_CASSANDRA_CL, resultCallBack);
    }


    /**
     * count the entities on column families.
     * @param bean the bean
     * @param session the session
     * @param <T> kind of class
     * @param resultCallBack the callback is invoked after the function returns
     */
    public <T> void count(Class<T> bean, Session session,
            ResultAsyncCallBack<Long> resultCallBack) {
        count(bean, session, DEFAULT_CASSANDRA_CL, resultCallBack);
    }

    /**
     * count the entities on column families.
     * @param bean the bean
     * @param session the session
     * @param consistency the consistency level
     * @param <T> kind of class
     * @param resultCallBack the callback is invoked after the function returns
     */
    public <T> void count(Class<T> bean, Session session,
            ConsistencyLevel consistency,
            ResultAsyncCallBack<Long> resultCallBack) {
        countQuery.countAsync(bean, session, consistency, resultCallBack);
    }

    /**
     * find a list of object from a list of keys.
     * @param keys the keys
     * @param bean the kind of object
     * @param session the Session
     * @param <K> the kind of key
     * @param <T> the kind of object
     * @param resultCallBack the callback is invoked after the function returns
     */
    public <K, T> void findByKeys(Iterable<K> keys, Class<T> bean,
            Session session, ResultAsyncCallBack<List<T>> resultCallBack) {
        findByKeys(keys, bean, session, DEFAULT_CASSANDRA_CL, resultCallBack);
    }
    /**
     * find a list of object from a list of keys.
     * @param keys the keys
     * @param bean the kind of object
     * @param session the Session
     * @param <K> the kind of key
     * @param <T> the kind of object
     * @param consistency the consistency level
     * @param resultCallBack the callback is invoked after the function returns
     */
    public <K, T> void findByKeys(Iterable<K> keys, Class<T> bean,
            Session session, ConsistencyLevel consistency,
            ResultAsyncCallBack<List<T>> resultCallBack) {

        findByKeyQuery.findByKeysAsync(keys, bean, session, consistency,
                resultCallBack);

    }
    /**
     * create statment to insert.
     * @param bean the bean
     * @param <T> the kind of class
     * @return the insert
     */
    public <T> Insert createInsertStatment(T bean) {
        return insertQuery.createStatment(bean, ConsistencyLevel.ONE);
    }

    /**
     * remove all element on the base.
     * @param bean the column family entity
     * @param session the session
     * @param <T> kind of object
     */
    public <T> void removeAll(Class<T> bean, Session session) {
        truncateQuery.truncateAsync(bean, session);
    }
}
