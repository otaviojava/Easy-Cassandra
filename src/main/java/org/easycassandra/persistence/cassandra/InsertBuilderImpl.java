package org.easycassandra.persistence.cassandra;

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
        insert.value(name, value);
        return this;
    }
    @Override
    public InsertBuilder<T> enumValue(String name, Enum<?> value) {
        insert.value(name, value.ordinal());
        return this;
    }
    @Override
    public InsertBuilder<T> customValue(String name, Object value) {
        return customValue(name, value, new Customizable.DefaultCustmomizable());
    }
    @Override
    public InsertBuilder<T> customValue(String name, Object value, Customizable customizable) {
        insert.value(name, customizable.read(value));
        return this;
    }
    @Override
    public InsertBuilder<T> values(String[] names, Object[] values) {
        insert.values(names, values);
        return this;
    }
    @Override
    public boolean execute() {
        return session.execute(insert) != null;
    }

    @Override
    public String toString() {
        return insert.toString();
    }
}
