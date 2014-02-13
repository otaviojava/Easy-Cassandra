package org.easycassandra.persistence.cassandra;

import org.easycassandra.ClassInformation;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.RetryPolicy;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
/**
 * {@link InsertBuilder}.
 * @author otaviojava
 * @param <T> the kind of object
 */
public class InsertBuilderImpl<T> implements InsertBuilder<T> {

    private Insert insert;
    private Session session;
    private ClassInformation classBean;

    /**
     * Constructor.
     * @param insert the insert
     * @param session the session
     * @param classBean the class bean
     */
    public InsertBuilderImpl(Insert insert, Session session, ClassInformation classBean) {
        this.insert = insert;
        this.session = session;
        this.classBean = classBean;
    }

    @Override
    public InsertBuilder<T> withTracing(boolean tracing) {
        if (tracing) {
            insert.enableTracing();
        } else {
            insert.disableTracing();
        }
        return this;
    }

    @Override
    public InsertBuilder<T> withFetchSize(int fetchSize) {
        insert.setFetchSize(fetchSize);
        return this;
    }

    @Override
    public InsertBuilder<T> withConsistencyLevel(ConsistencyLevel consistency) {
        insert.setConsistencyLevel(consistency);
        return this;
    }

    @Override
    public InsertBuilder<T> withSerialConsistencyLevel(ConsistencyLevel serialConsistency) {
        insert.setSerialConsistencyLevel(serialConsistency);
        return this;
    }

    @Override
    public InsertBuilder<T> ifNotExists() {
        insert.ifNotExists();
        return this;
    }

    @Override
    public InsertBuilder<T> setForceNoValues(boolean forceNoValues) {
        insert.setForceNoValues(forceNoValues);
        return this;
    }

    @Override
    public InsertBuilder<T> withRetryPolicy(RetryPolicy policy) {
        insert.setRetryPolicy(policy);
        return this;
    }

    @Override
    public InsertBuilder<T> withTimeStamp(long timeStamp) {
        insert.using(QueryBuilder.timestamp(timeStamp));
        return this;
    }

    @Override
    public InsertBuilder<T> withTtl(int ttl) {
        insert.using(QueryBuilder.ttl(ttl));
        return this;
    }
    @Override
    public InsertBuilder<T> value(String name, Object value) {
        insert.value(classBean.toColumn(name), value);
        return this;
    }
    @Override
    public InsertBuilder<T> enumValue(String name, Enum<?> value) {
        insert.value(classBean.toColumn(name), value.ordinal());
        return this;
    }
    @Override
    public InsertBuilder<T> customValue(String name, Object value) {
        return customValue(classBean.toColumn(name), value,
                new Customizable.DefaultCustmomizable());
    }
    @Override
    public InsertBuilder<T> customValue(String name, Object value, Customizable customizable) {
        insert.value(classBean.toColumn(name), customizable.read(value));
        return this;
    }
    @Override
    public InsertBuilder<T> values(String[] names, Object[] values) {
        insert.values(classBean.toColumn(names), values);
        return this;
    }
    @Override
    public boolean execute() {
        return session.execute(insert) != null;
    }
    @Override
    public void executeAsync() {
        session.executeAsync(insert);
    }

    @Override
    public void executeAsync(ResultCallBack<Boolean> resultCallBack) {
        AsyncResult.INSTANCE.runUpdate(resultCallBack, session.executeAsync(insert));

    }
    @Override
    public String toString() {
        return insert.toString();
    }
}
