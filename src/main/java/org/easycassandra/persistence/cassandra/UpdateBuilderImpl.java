package org.easycassandra.persistence.cassandra;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easycassandra.ClassInformation;
import org.easycassandra.ClassInformation.KeySpaceInformation;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.RetryPolicy;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Update;
/**
 * {@link UpdateBuilder}.
 * @author otaviojava
 * @param <T> the kind of object
 */
public class UpdateBuilderImpl<T> implements UpdateBuilder<T> {

    private Update update;

    private Session session;

    private ClassInformation classBean;

    /**
     * constructor.
     * @param session the sesion
     * @param classBean the class bean information
     * @param update the update
     * @param keySpace the keyspace
     */
    public UpdateBuilderImpl(Session session, ClassInformation classBean,
            String keySpace, Update update) {

        this.session = session;
        this.classBean = classBean;

        if (update == null) {
            KeySpaceInformation keySpaceInformation = classBean
                    .getKeySpace(keySpace);
            this.update = QueryBuilder.update(keySpaceInformation.getKeySpace(),
                    keySpaceInformation.getColumnFamily());
        } else {
            this.update = update;
        }

    }

    @Override
    public UpdateBuilder<T> withTracing(boolean tracing) {
        if (tracing) {
            update.enableTracing();
        } else {
            update.disableTracing();
        }
        return this;
    }

    @Override
    public UpdateBuilder<T> withFetchSize(int fetchSize) {
        update.setFetchSize(fetchSize);
        return this;
    }

    @Override
    public UpdateBuilder<T> withConsistencyLevel(ConsistencyLevel consistency) {
        update.setConsistencyLevel(consistency);
        return this;
    }

    @Override
    public UpdateBuilder<T> withSerialConsistencyLevel(ConsistencyLevel serialConsistency) {
        update.setSerialConsistencyLevel(serialConsistency);
        return this;
    }

    @Override
    public UpdateBuilder<T> value(String name, Object value) {
        update.with(QueryBuilder.set(classBean.toColumn(name), value));
        return this;
    }
    @Override
    public UpdateBuilder<T> addSet(String name, Object value) {
        update.with(QueryBuilder.add(classBean.toColumn(name), value));
        return this;
    }
    @Override
    public UpdateBuilder<T> addSetAll(String name, Set<?> set) {
        update.with(QueryBuilder.addAll(classBean.toColumn(name), set));
        return this;
    }
    @Override
    public UpdateBuilder<T> removeSet(String name, Object value) {
        update.with(QueryBuilder.remove(classBean.toColumn(name), value));
        return this;
    }
    @Override
    public UpdateBuilder<T> removeSetAll(String name, Set<?> set) {
        update.with(QueryBuilder.removeAll(classBean.toColumn(name), set));
        return this;
    }
    @Override
    public UpdateBuilder<T> addList(String name, Object value) {
        update.with(QueryBuilder.append(classBean.toColumn(name), value));
        return this;
    }
    @Override
    public UpdateBuilder<T> addListAll(String name, List<?> list) {
        update.with(QueryBuilder.appendAll(classBean.toColumn(name), list));
        return this;
    }
    @Override
    public UpdateBuilder<T> addIndexList(String name, int index, Object value) {
        update.with(QueryBuilder.setIdx(classBean.toColumn(name), index, value));
        return this;
    }
    @Override
    public UpdateBuilder<T> preAddList(String name, Object value) {
        update.with(QueryBuilder.prepend(classBean.toColumn(name), value));
        return this;
    }
    @Override
    public UpdateBuilder<T> preAddListAll(String name, List<?> list) {
        update.with(QueryBuilder.prependAll(classBean.toColumn(name), list));
        return this;
    }
    @Override
    public UpdateBuilder<T> removeList(String name, Object value) {
        update.with(QueryBuilder.discard(classBean.toColumn(name), value));
        return this;
    }
    @Override
    public UpdateBuilder<T> removeListAll(String name, List<?> list) {
        update.with(QueryBuilder.discardAll(classBean.toColumn(name), list));
        return this;
    }
    @Override
    public UpdateBuilder<T> put(String name, Object key, Object value) {
        update.with(QueryBuilder.put(classBean.toColumn(name), key, value));
        return this;
    }
    @Override
    public UpdateBuilder<T> put(String name, Map<?, ?> map) {
        update.with(QueryBuilder.putAll(classBean.toColumn(name), map));
        return this;
    }
    @Override
    public UpdateBuilder<T> enumValue(String name, Enum<?> value) {
        return value(classBean.toColumn(name), value.ordinal());
    }
    @Override
    public UpdateBuilder<T> customValue(String name, Object value) {
        return customValue(classBean.toColumn(name), value,
                new Customizable.DefaultCustmomizable());
    }
    @Override
    public UpdateBuilder<T> customValue(String name, Object value, Customizable customizable) {
        return value(classBean.toColumn(name), customizable.read(value));
    }

    @Override
    public UpdateBuilder<T> setForceNoValues(boolean forceNoValues) {
        update.setForceNoValues(forceNoValues);
        return this;
    }

    @Override
    public UpdateBuilder<T> withRetryPolicy(RetryPolicy policy) {
        update.setRetryPolicy(policy);
        return this;
    }

    @Override
    public UpdateBuilder<T> withTimeStamp(long timeStamp) {
        update.using(QueryBuilder.timestamp(timeStamp));
        return this;
    }

    @Override
    public UpdateBuilder<T> withTtl(int ttl) {
        update.using(QueryBuilder.ttl(ttl));
        return this;
    }
    @Override
    public UpdateBuilder<T> whereEq(String name, Object value) {
        update.where(QueryBuilder.eq(classBean.toColumn(name), value));
        return this;
    }
    @Override
    public UpdateBuilder<T> whereIn(String name, Object... values) {
        update.where(QueryBuilder.in(classBean.toColumn(name), values));
        return this;
    }
    @Override
    public boolean execute() {
        return session.execute(update) != null;
    }

    @Override
    public void executeAsync() {
        session.executeAsync(update);
    }

    @Override
    public void executeAsync(ResultAsyncCallBack<Boolean> resultCallBack) {
        AsyncResult.INSTANCE.runUpdate(resultCallBack, session.executeAsync(update));

    }

    @Override
    public String toString() {
        return update.toString();
    }

    @Override
    public RegularStatement getStatement() {
        return update;
    }

}
