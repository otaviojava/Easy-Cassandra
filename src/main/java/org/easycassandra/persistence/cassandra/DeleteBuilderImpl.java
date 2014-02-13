package org.easycassandra.persistence.cassandra;

import org.easycassandra.ClassInformation;
import org.easycassandra.ClassInformation.KeySpaceInformation;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.RetryPolicy;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;
/**
 * {@link DeleteBuilder}.
 * @author otaviojava
 * @param <T> the kind of object
 */
/**
 * {@link DeleteBuilder}.
 * @author otaviojava
 * @param <T> the kind of object
 */
public class DeleteBuilderImpl<T> implements DeleteBuilder<T> {

    private Delete delete;
    private Session session;

    private ClassInformation classBean;


    /**
     * Constructor.
     * @param session the session
     * @param classBean the class
     * @param keySpace the keyspace name
     * @param delete the delete
     * @param columnNames the columns to remove
     */
    public DeleteBuilderImpl(Session session, ClassInformation classBean,
            String keySpace, Delete delete, String... columnNames) {
        this.session = session;
        KeySpaceInformation keySpaceInformation = classBean
                .getKeySpace(keySpace);

        this.classBean = classBean;
        if (delete == null) {
            this.delete = QueryBuilder.delete().from(
                    keySpaceInformation.getKeySpace(),
                    keySpaceInformation.getColumnFamily());
        } else {
            this.delete = delete;
        }

    }

    @Override
    public DeleteBuilder<T> withTracing(boolean tracing) {
        if (tracing) {
            delete.enableTracing();
        } else {
            delete.disableTracing();
        }
        return this;
    }

    @Override
    public DeleteBuilder<T> withFetchSize(int fetchSize) {
        delete.setFetchSize(fetchSize);
        return this;
    }

    @Override
    public DeleteBuilder<T> withConsistencyLevel(ConsistencyLevel consistency) {
        delete.setConsistencyLevel(consistency);
        return this;
    }

    @Override
    public DeleteBuilder<T> withSerialConsistencyLevel(ConsistencyLevel serialConsistency) {
        delete.setSerialConsistencyLevel(serialConsistency);
        return this;
    }

    @Override
    public DeleteBuilder<T> setForceNoValues(boolean forceNoValues) {
        delete.setForceNoValues(forceNoValues);
        return this;
    }

    @Override
    public DeleteBuilder<T> withRetryPolicy(RetryPolicy policy) {
        delete.setRetryPolicy(policy);
        return this;
    }

    @Override
    public DeleteBuilder<T> withTimeStamp(long timeStamp) {
        delete.using(QueryBuilder.timestamp(timeStamp));
        return this;
    }

    @Override
    public DeleteBuilder<T> withTtl(int ttl) {
        delete.using(QueryBuilder.ttl(ttl));
        return this;
    }
    @Override
    public boolean execute() {
        return session.execute(delete) != null;
    }
    @Override
    public void executeAsync() {
        session.executeAsync(delete);
    }

    @Override
    public void executeAsync(ResultAsyncCallBack<Boolean> resultCallBack) {
        AsyncResult.INSTANCE.runUpdate(resultCallBack, session.executeAsync(delete));

    }
    @Override
    public DeleteBuilder<T> whereEq(String name, Object value) {
        delete.where(QueryBuilder.eq(classBean.toColumn(name), value));
        return this;
    }
    @Override
    public DeleteBuilder<T> whereIn(String name, Object... values) {
        delete.where(QueryBuilder.eq(classBean.toColumn(name), values));
        return this;
    }

    @Override
    public String toString() {
        return delete.toString();
    }

    @Override
    public RegularStatement getStatement() {
        return delete;
    }
}
