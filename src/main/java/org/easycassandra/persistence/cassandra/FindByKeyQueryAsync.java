package org.easycassandra.persistence.cassandra;

import java.util.LinkedList;
import java.util.List;

import org.easycassandra.FieldInformation;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;

/**
 * find by key async.
 * @author otaviojava
 */
class FindByKeyQueryAsync extends FindByKeyQuery {

    public FindByKeyQueryAsync(String keySpace) {
        super(keySpace);
    }

    public <T> void findByKeyAsync(Object key, Class<T> bean, Session session,
            ConsistencyLevel consistency, ResultAsyncCallBack<T> resultCallBack) {

        QueryBean byKeyBean = createQueryBean(bean, consistency);
        executeConditions(key, bean, session, byKeyBean, resultCallBack);
    }

    public <T, K> void findByKeysAsync(Iterable<K> keys, Class<T> bean, Session session,
            ConsistencyLevel consistency, ResultAsyncCallBack<List<T>> resultCallBack) {

        QueryBean byKeyBean = createQueryBean(bean, consistency);
        List<ResultSetFuture> results = new LinkedList<>();
        for (K key : keys) {
            ResultSetFuture resultSet = executeQueryAsync(key, bean, session,
                    byKeyBean);
            results.add(resultSet);
        }
        AsyncResult.INSTANCE.runKeys(resultCallBack, results, bean);
    }


    private <T> void executeConditions(Object key, Class<T> bean, Session session,
            QueryBean byKeyBean, ResultAsyncCallBack<T> resultCallBack) {
        ResultSetFuture resultSet = executeQueryAsync(key, bean, session,
                byKeyBean);
        AsyncResult.INSTANCE.runKey(resultCallBack, resultSet, bean);
    }


    private ResultSetFuture executeQueryAsync(Object key, Class<?> bean,
            Session session, QueryBean byKeyBean) {

        FieldInformation fieldInformation = findKey(key, bean, byKeyBean);
        if (fieldInformation.isEmbedded()) {
            return executeComplexKeyAsync(key, session, byKeyBean);
        } else {
            return executeSingleKeyAsync(key, session, byKeyBean);
        }
    }

    private ResultSetFuture executeComplexKeyAsync(Object key, Session session,
            QueryBean byKeyBean) {
        executeEqKey(key, byKeyBean);
        return session.executeAsync(byKeyBean.getSelect());

    }

    private ResultSetFuture executeSingleKeyAsync(Object key, Session session,
            QueryBean byKeyBean) {

        byKeyBean.getSelect().where(QueryBuilder.eq(
                byKeyBean.getSearchField().getName(), key));
        return session.executeAsync(byKeyBean.getSelect());

    }
}
