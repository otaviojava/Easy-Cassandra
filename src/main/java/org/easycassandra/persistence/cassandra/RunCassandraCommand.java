package org.easycassandra.persistence.cassandra;

import java.util.LinkedList;
import java.util.List;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Session;

/**
 * Facade to run Cassandra.
 * @author otaviojava
 */
public class RunCassandraCommand {

    private static final ConsistencyLevel DEFAULT_CASSANDRA_CL = ConsistencyLevel.ONE;

    /**
     * insert an object on keySpace from Session.
     * @param bean the object
     * @param session the session
     * @param keySpace the keySpace
     * @param <T> the object
     * @return the object persists on Cassandra
     */
    public <T> T  insert(T bean, Session session, String keySpace) {
        return insert(bean, session, keySpace, DEFAULT_CASSANDRA_CL);
    }

    /**
     * insert an object on keySpace from Session.
     * @param bean the object
     * @param session the session
     * @param keySpace the keySpace
     * @param consistencyLevel the consistency level of Cassandra
     * @param <T> the object
     * @return the object persists on Cassandra
     */
    public <T> T insert(T bean, Session session, String keySpace,
            ConsistencyLevel consistencyLevel) {
        new InsertQuery(keySpace).prepare(bean, session, consistencyLevel);
        return bean;
    }

    /**
     * insert a list of objects on keySpace from Session.
     * @param beans the list to persist on Cassandra
     * @param session the Session
     * @param keySpace the KeySpace
     * @param <T> kind of object
     * @return the list on the DataBase
     */
    public <T> boolean insert(Iterable<T> beans, Session session, String keySpace) {
        return insert(beans, session, keySpace, DEFAULT_CASSANDRA_CL);
    }

    /**
     * insert a list of objects on keySpace from Session.
     * @param beans the list to persist on Cassandra
     * @param session the Session
     * @param keySpace the KeySpace
     * @param consistencyLevel the consistency Leval of Cassandra
     * @param <T> kind of object
     * @return the list on the DataBase
     */
    public <T> boolean insert(Iterable<T> beans, Session session,
            String keySpace, ConsistencyLevel consistencyLevel) {
        new InsertQuery(keySpace).prepare(beans, session, consistencyLevel);
        return true;
    }

    /**
     * Execute select * from ColumnFamily on Cassandra Data Base,
     * it's respect the Cassandra architecture.
     * @param bean kind of object
     * @param session the session
     * @param keySpace the KeySpace
     * @param <T> kind of object
     * @return Possible objects on Cassandra
     */
    public <T> List<T> findAll(Class<T> bean , Session session, String keySpace) {
        return findAll(bean, session, keySpace, DEFAULT_CASSANDRA_CL);
    }

    /**
     * Execute select * from ColumnFamily on Cassandra Data Base,
     * it's respect the Cassandra architecture.
     * @param bean kind of object
     * @param session the Session
     * @param keySpace the KeySpace
     * @param <T> kind of object
     * @param consistency the consistency Leval on Cassandra
     * @return Possible objects on Cassandra
     */
    public <T> List<T> findAll(Class<T> bean, Session session, String keySpace,
            ConsistencyLevel consistency) {
        return new FindAllQuery(keySpace).listAll(bean, session, consistency);
    }

    /**
     * find an object from Cassandra by key.
     * @param key the key on Cassandra
     * @param bean the object
     * @param session the Session
     * @param keySpace the KeySpace
     * @param <T> kind of object
     * @return the object or null if there is not exists.
     */
    public <T> T findByKey(Object key, Class<T> bean, Session session,
            String keySpace) {
        return findByKey(key, bean, session, keySpace, DEFAULT_CASSANDRA_CL);
    }

    /**
     * find an object from Cassandra by key.
     * @param key the key on Cassandra
     * @param bean the object
     * @param session the Session
     * @param keySpace the KeySpace
     * @param <T> kind of object
     * @param consistency the consistency Level on Cassandra
     * @return the object or null if there is not exists.
     */
    public <T> T findByKey(Object key, Class<T> bean, Session session,
            String keySpace, ConsistencyLevel consistency) {
        return new FindByKeyQuery(keySpace).findByKey(key, bean, session,
                consistency);
    }

    /**
     * delete on object from cassandra by Key.
     * @param key the key
     * @param bean the kind of object
     * @param session the Session
     * @param keySpace the KeySpace
     * @return if execute the command
     */
    public boolean deleteByKey(Object key, Class<?> bean, Session session,
            String keySpace) {
        return deleteByKey(key, bean, session, keySpace, DEFAULT_CASSANDRA_CL);
    }

    /**
     * delete on object from cassandra by Key.
     * @param key the key
     * @param bean the kind of object
     * @param session the Session
     * @param keySpace the KeySpace
     * @param consistency the consistency Level
     * @return if execute the command
     */
    public boolean deleteByKey(Object key, Class<?> bean, Session session,
            String keySpace, ConsistencyLevel consistency) {
        return new DeleteQuery(keySpace).deleteByKey(key, bean, session,
                consistency);
    }
    /**
     * delete on object from cassandra by Key.
     * @param session the Session
     * @param keySpace the KeySpace
     * @param <K> the kind of key
     * @param keys the keys
     * @param entity the entity
     * @param <T> the kind of object
     * @return if execute the command
     */
    public <K, T> boolean deleteByKey(Iterable<K> keys, Class<T> entity,
            Session session, String keySpace) {
        return deleteByKey(keys, entity, session, keySpace,
                DEFAULT_CASSANDRA_CL);
    }
    /**
     * delete on object from cassandra by Key.
     * @param session the Session
     * @param keySpace the KeySpace
     * @param consistency the consistency Level
     * @param entity the entity
     * @param <K> the keys
     * @param keys the keys values
     * @param <T> the kind of object
     * @return if execute the command
     */
    public <K, T> boolean deleteByKey(Iterable<K> keys, Class<T> entity,
            Session session, String keySpace, ConsistencyLevel consistency) {
        new DeleteQuery(keySpace).deleteByKey(keys, entity, session,
                consistency);
        return true;
    }

    /**
     * delete on object from cassandra by Key.
     * @param bean the object
     * @param session the Session
     * @param keySpace the KeySpace
     * @param <T> kind of object
     * @return if execute the command
     */
    public <T> boolean delete(T bean, Session session, String keySpace) {
        return delete(bean, session, keySpace, DEFAULT_CASSANDRA_CL);
    }
    /**
     * delete on object from cassandra by Key.
     * @param bean the kind of object
     * @param session the Session
     * @param keySpace the KeySpace
     * @param <T> kind of object
     * @param consistency the consistency level
     * @return if execute the command
     */
    public <T> boolean delete(T bean, Session session, String keySpace,
            ConsistencyLevel consistency) {
        return new DeleteQuery(keySpace)
                .deleteByKey(bean, session, consistency);
    }

    /**
     * delete a list of beans.
     * @param beans the beans
     * @param session the Session
     * @param keySpace the KeySpace
     * @param <T> the kind of keys
     * @return if execute with success
     */
    public <T> boolean delete(Iterable<T> beans, Session session, String keySpace) {
        return delete(beans, session, keySpace, DEFAULT_CASSANDRA_CL);
    }

    /**
     * delete a list of beans.
     * @param beans the beans
     * @param session the Session
     * @param keySpace the KeySpace
     * @param consistency the consistency Level on Cassandra
     * @param <T> th kind of object
     * @return if execute with Sucess
     */
    public <T> boolean delete(Iterable<T> beans, Session session,
            String keySpace, ConsistencyLevel consistency) {
        return new DeleteQuery(keySpace).deleteByKey(beans, session,
                consistency);
    }


   /**
    * find list of objects from index.
    * @param indexName name of index
    * @param index index value
    * @param bean the kind of bean
    * @param session the Session
    * @param keySpace the KeySpace
    * @param <T> kind of object
    * @return the list with index from Cassandra
    */
    public <T> List<T> findByIndex(String indexName, Object index,
            Class<T> bean, Session session, String keySpace) {
        return findByIndex(indexName, index, bean, session, keySpace,
                DEFAULT_CASSANDRA_CL);
    }
    /**
     * find list of objects from index.
     * @param indexName name of index
     * @param index index value
     * @param bean the kind of bean
     * @param session the Session
     * @param keySpace the KeySpace
     * @param <T> kind of object
     * @param consistency the consistency level
     * @return the list with index from Cassandra
     */
    public <T> List<T> findByIndex(String indexName, Object index,
            Class<T> bean, Session session, String keySpace,
            ConsistencyLevel consistency) {
        return new FindByIndexQuery(keySpace).findByIndex(indexName, index,
                bean, session, consistency);
    }

    /**
     * find list of objects from index.
     * The default will be the first field
     * @param index the index value
     * @param bean the kind of bean
     * @param session the Session
     * @param keySpace the keySpace
     * @param <T> kind of object
     * @param consistency the consistency level of cassandra
     * @author Nenita Casuga
     * @since 10/30/2013
     * @return the list with index from Cassandra
     */
    public <T> List<T> findByIndex(Object index, Class<T> bean,
            Session session, String keySpace, ConsistencyLevel consistency) {
        return new FindByIndexQuery(keySpace).findByIndex(index, bean, session,
                consistency);
    }
    /**
     * find list of objects from index.
     * The default will be the first field
     * @param index the index value
     * @param bean the kind of bean
     * @param session the Session
     * @param keySpace the keySpace
     * @param <T> kind of object
     * @return the list with index from Cassandra
     */
    public <T> List<T> findByIndex(Object index, Class<T> bean,
            Session session, String keySpace) {
        return findByIndex(index, bean, session, keySpace, DEFAULT_CASSANDRA_CL);
    }


    /**
     * find list of objects from index.
     * The default will be the first field
     * @param index the index value
     * @param bean the kind of bean
     * @param session the Session
     * @param key the key
     * @param keySpace the keySpace
     * @param <T> kind of object
     * @param <I> the index
     * @author Nenita Casuga
     * @since 10/30/2013
     * @return the list with index from Cassandra
     */
    public <T, I> List<T> findByKeyAndIndex(Object key, I index, Class<T> bean,
            Session session, String keySpace) {
        return new FindByKeyAndIndexQuery(keySpace).findByKeyAndIndex(key,
                index, bean, session, DEFAULT_CASSANDRA_CL);
    }

    /**
     * Find by primary key and index.
     * @param index the index value
     * @param bean the kind of bean
     * @param session the Session
     * @param key the key
     * @param keySpace the keySpace
     * @param <T> kind of object
     * @param consistency the consistency level
     * @param <I> the index
     * @author Nenita Casuga
     * @since 10/30/2013
     * @return the list with index from Cassandra
     */
    public <T, I> List<T> findByKeyAndIndex(Object key, I index, Class<T> bean,
            Session session, String keySpace, ConsistencyLevel consistency) {
        return new FindByKeyAndIndexQuery(keySpace).findByKeyAndIndex(key,
                index, bean, session, consistency);
    }

    /**
     * Find by primary key and index range.
     * @param bean the kind of bean
     * @param session the Session
     * @param key the key
     * @param keySpace the keySpace
     * @param <T> kind of object
     * @param <I> the index
     * @param indexEnd the indexEnd
     * @param indexStart the indexStart
     * @param inclusive if inclusive or not
     * @author Nenita Casuga
     * @since 10/30/2013
     * @return the list with index from Cassandra
     */
    public <T, I> List<T> findByKeyAndIndexRange(Object key, I indexStart,
            I indexEnd, boolean inclusive, Class<T> bean, Session session,
            String keySpace) {
        return new FindByKeyAndIndexQuery(keySpace).findByKeyAndIndex(key,
                indexStart, indexEnd, inclusive, bean, session,
                DEFAULT_CASSANDRA_CL);
    }

    /**
     * Find by primary key and index range.
     * @param bean the kind of bean
     * @param session the Session
     * @param key the key
     * @param keySpace the keySpace
     * @param <T> kind of object
     * @param <I> the index
     * @param indexEnd the indexEnd
     * @param indexStart the indexStart
     * @param inclusive if inclusive or not
     * @param consistency the consistency level
     * @author Nenita Casuga
     * @since 10/30/2013
     * @return the list with index from Cassandra
     */
    public <T, I> List<T> findByKeyAndIndexRange(Object key, I indexStart,
            I indexEnd, boolean inclusive, Class<T> bean, Session session,
            String keySpace, ConsistencyLevel consistency) {
        return new FindByKeyAndIndexQuery(keySpace).findByKeyAndIndex(key,
                indexStart, indexEnd, inclusive, bean, session, consistency);
    }

    /**
     * count the entities on column families.
     * @param bean the bean
     * @param session the session
     * @param keySpace the KeySpace
     * @param <T> kind of class
     * @return the number of entities on column families
     */
    public <T> Long count(Class<T> bean, Session session, String keySpace) {
        return count(bean, session, keySpace, DEFAULT_CASSANDRA_CL);
    }

    /**
     * count the entities on column families.
     * @param bean the bean
     * @param session the session
     * @param keySpace the KeySpace
     * @param consistency the consistency level
     * @param <T> kind of class
     * @return the number of entities on column families
     */
    public <T> Long count(Class<T> bean, Session session, String keySpace,
            ConsistencyLevel consistency) {
        return new CountQuery(keySpace).count(bean, session, consistency);
    }

    /**
     * find a list of object from a list of keys.
     * @param keys the keys
     * @param bean the kind of object
     * @param session the Session
     * @param keySpace the KeySpace
     * @param <K> the kind of key
     * @param <T> the kind of object
     * @return the list from keys
     */
    public <K, T> List<T> findByKeys(Iterable<K> keys, Class<T> bean,
            Session session, String keySpace) {
        return findByKeys(keys, bean, session, keySpace, DEFAULT_CASSANDRA_CL);
    }
    /**
     * find a list of object from a list of keys.
     * @param keys the keys
     * @param bean the kind of object
     * @param session the Session
     * @param keySpace the KeySpace
     * @param <K> the kind of key
     * @param <T> the kind of object
     * @param consistency the consistency level
     * @return the list from keys
     */
    public <K, T> List<T> findByKeys(Iterable<K> keys, Class<T> bean,
            Session session, String keySpace, ConsistencyLevel consistency) {
        List<T> beans = new LinkedList<T>();

        for (K key : keys) {
            T entity = findByKey(key, bean, session, keySpace, consistency);
            if (entity != null) {
                beans.add(entity);
            }
        }

        return beans;
    }

    /**
     * remove all element on the base.
     * @param bean the column family entity
     * @param session the session
     * @param <T> kind of object
     */
    public <T> void removeAll(Class<T> bean, Session session) {
        new RemoveAll().truncate(bean, session);
    }
}
