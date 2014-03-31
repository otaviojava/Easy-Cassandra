package org.easycassandra.persistence.cassandra;

import java.util.List;

import org.easycassandra.ClassInformation;
import org.easycassandra.ClassInformations;
import org.easycassandra.FieldInformation;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;

/**
 * find by index async.
 * @author otaviojava
 */
class FindByIndexQueryAsync extends FindByIndexQuery {

    public FindByIndexQueryAsync(String keySpace) {
        super(keySpace);
    }

    public <T, I> void findByIndexAsync(String indexName, I key, Class<T> bean,
            Session session, ConsistencyLevel consistency,
            ResultAsyncCallBack<List<T>> resultCallBack) {
        QueryBean byKeyBean = createQueryBean(bean, consistency);
        ResultSetFuture result = executeConditions(indexName, key, bean, session, byKeyBean);
        AsyncResult.INSTANCE.runSelect(resultCallBack, result, bean);
    }

    public <T, I> void findByIndexAsync(I index, Class<T> bean,
            Session session, ConsistencyLevel consistency,
            ResultAsyncCallBack<List<T>> resultCallBack) {
        ClassInformation classInformation = ClassInformations.INSTACE
                .getClass(bean);
        List<FieldInformation> fields = classInformation.getIndexFields();
        checkFieldNull(bean, fields);
        findByIndexAsync(fields.get(0).getName(), index, bean, session,
                consistency, resultCallBack);
    }

    private <T> ResultSetFuture executeConditions(String indexName, Object key,
            Class<T> bean, Session session, QueryBean byKeyBean) {

        prepareIndex(indexName, bean, byKeyBean);
        byKeyBean.getSelect().where(QueryBuilder.eq(
                byKeyBean.getSearchField().getName(), key));

        return session.executeAsync(byKeyBean.getSelect());
    }
}
