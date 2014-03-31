package org.easycassandra.persistence.cassandra;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Select;

/**
 * count using async process.
 * @author otaviojava
 */
class CountQueryAsync extends CountQuery {

    public CountQueryAsync(String keySpace) {
        super(keySpace);
    }

    public void countAsync(Class<?> bean, Session session,
            ConsistencyLevel consistency, ResultAsyncCallBack<Long> resultCallBack) {
        Select select = prepareCount(bean, consistency);
        ResultSetFuture resultSet = session.executeAsync(select);
        AsyncResult.INSTANCE.runCount(resultCallBack, resultSet);

    }

}
