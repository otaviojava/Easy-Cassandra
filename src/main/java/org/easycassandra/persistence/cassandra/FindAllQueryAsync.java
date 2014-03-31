package org.easycassandra.persistence.cassandra;

import java.util.List;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;


/**
 * Execute findAll Async.
 * @author otaviojava
 */
class FindAllQueryAsync extends FindAllQuery {

    public FindAllQueryAsync(String keySpace) {
        super(keySpace);
    }

    public <T> void listAllAsync(Class<T> bean, Session session,
            ResultAsyncCallBack<List<T>> resultCallBack,
            ConsistencyLevel consistency) {

        QueryBean byKeyBean = createQueryBean(bean, consistency);
        ResultSetFuture resultSet = session.executeAsync(byKeyBean.getSelect());
        AsyncResult.INSTANCE.runSelect(resultCallBack, resultSet, bean);
    }
}
