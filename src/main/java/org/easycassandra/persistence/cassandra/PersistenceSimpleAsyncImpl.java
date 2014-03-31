package org.easycassandra.persistence.cassandra;

import java.util.List;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Session;

/**
 * @see {@link PersistenceAsync}
 * @author otaviojava
 *
 */
public class PersistenceSimpleAsyncImpl extends PersistenceBuilderImpl implements PersistenceAsync {

    private Session session;

    private RunCassandraCommandAsync command;

    PersistenceSimpleAsyncImpl(Session session, String keySpace) {
        super(session, keySpace);
        this.session = session;
        command = new RunCassandraCommandAsync(keySpace);
    }

    @Override
    public <T> boolean insert(T bean) {
        command.insert(bean, session);
        return true;
    }

    @Override
    public <T> boolean insert(T bean, ConsistencyLevel consistency) {
        command.insert(bean, session, consistency);
        return true;
    }

    @Override
    public <T> boolean insert(Iterable<T> beans) {
        command.insert(beans, session);
        return true;
    }

    @Override
    public <T> boolean insert(Iterable<T> beans, ConsistencyLevel consistency) {
        command.insert(beans, session, consistency);
        return true;
    }

    @Override
    public <T> boolean delete(T bean) {
        command.delete(bean, session);
        return true;
    }

    @Override
    public <T> boolean delete(T bean, ConsistencyLevel consistency) {
        command.delete(bean, session, consistency);
        return true;
    }

    @Override
    public <T> boolean delete(Iterable<T> beans) {
        command.delete(beans, session);
        return false;
    }

    @Override
    public <T> boolean delete(Iterable<T> beans, ConsistencyLevel consistency) {
        command.delete(beans, session, consistency);
        return false;
    }

    @Override
    public <K, T> boolean deleteByKey(K key, Class<T> bean) {
        command.deleteByKey(key, bean, session);
        return true;
    }

    @Override
    public <K, T> boolean deleteByKey(K key, Class<T> bean,
            ConsistencyLevel consistency) {
        command.deleteByKey(key, bean, session, consistency);
        return true;
    }

    @Override
    public <T> boolean update(T bean) {

        return insert(bean);
    }

    @Override
    public <T> boolean update(T bean, ConsistencyLevel consistency) {
        return insert(bean, consistency);
    }

    @Override
    public <T> boolean update(Iterable<T> beans) {
        return insert(beans);
    }

    @Override
    public <T> boolean update(Iterable<T> beans, ConsistencyLevel consistency) {
        return insert(beans, consistency);
    }

    @Override
    public boolean executeUpdate(String query) {
        session.executeAsync(query);
        return true;
    }

    @Override
    public <T> void removeAll(Class<T> bean) {
        command.removeAll(bean, session);
    }

    @Override
    public <T> void findAll(Class<T> bean,
            ResultAsyncCallBack<List<T>> resultCallBack) {
        command.findAll(bean, session, resultCallBack);
    }

    @Override
    public <T> void findAll(Class<T> bean, ConsistencyLevel consistency,
            ResultAsyncCallBack<List<T>> resultCallBack) {

        command.findAll(bean, session, resultCallBack, consistency);
    }

    @Override
    public <K, T> void findByKeys(Iterable<K> keys, Class<T> bean,
            ResultAsyncCallBack<List<T>> resultCallBack) {
        command.findByKeys(keys, bean, session, resultCallBack);
    }

    @Override
    public <K, T> void findByKeys(Iterable<K> keys, Class<T> bean,
            ConsistencyLevel consistency,
            ResultAsyncCallBack<List<T>> resultCallBack) {
        command.findByKeys(keys, bean, session, consistency, resultCallBack);

    }

    @Override
    public <K, T> void findByKey(K key, Class<T> bean,
            ResultAsyncCallBack<T> resultCallBack) {
        command.findByKey(key, bean, session, resultCallBack);
    }

    @Override
    public <K, T> void findByKey(K key, Class<T> bean,
            ConsistencyLevel consistency, ResultAsyncCallBack<T> resultCallBack) {

        command.findByKey(key, bean, session, resultCallBack, consistency);
    }

    @Override
    public <T, I> void findByIndex(String indexName, I index, Class<T> bean,
            ResultAsyncCallBack<List<T>> resultCallBack) {

        command.findByIndex(indexName, index, bean, session, resultCallBack);
    }

    @Override
    public <T, I> void findByIndex(String indexName, I index, Class<T> bean,
                       ConsistencyLevel consistency, ResultAsyncCallBack<List<T>> resultCallBack) {
        command.findByIndex(indexName, index, bean, session, consistency,
                resultCallBack);
    }

    @Override
    public <T, I> void findByIndex(I index, Class<T> bean,
            ResultAsyncCallBack<List<T>> resultCallBack) {

        command.findByIndex(index, bean, session, resultCallBack);
    }

    @Override
    public <T, I> void findByIndex(I index, Class<T> bean,
            ResultAsyncCallBack<List<T>> resultCallBack,
            ConsistencyLevel consistency) {
        command.findByIndex(index, bean, session, resultCallBack);
    }

    @Override
    public <T> void count(Class<T> bean,
            ResultAsyncCallBack<Long> resultCallBack) {
        command.count(bean, session, resultCallBack);
    }

    @Override
    public <T> void count(Class<T> bean,
             ConsistencyLevel consistency, ResultAsyncCallBack<Long> resultCallBack) {
        command.count(bean, session, consistency, resultCallBack);
    }


}
