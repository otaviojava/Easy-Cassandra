package org.easycassandra.persistence.cassandra;

import java.util.List;

import org.easycassandra.ClassInformation;
import org.easycassandra.ClassInformation.KeySpaceInformation;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.RetryPolicy;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

/**
 * select builder, it is easy way to make query with parameters.
 * @author otaviojava
 * @param <T> the kind of class
 */
public class SelectBuilderImpl <T> implements SelectBuilder<T> {

    private Select select;

    private Session session;

    private ClassInformation classBean;

    /**
     * Constructor.
     * @param session the session
     * @param classBean the class
     * @param keySpace the keySpace
     */
    public SelectBuilderImpl(Session session, ClassInformation classBean,
            String keySpace) {
        this.session = session;

        this.classBean = classBean;

        KeySpaceInformation keySpaceInformation = classBean.getKeySpace(keySpace);
        select = QueryBuilder.select(CreateColumns.INSTANCE
                .getColumns(classBean).toArray(new String[0])).from(
                        keySpaceInformation.getKeySpace(), keySpaceInformation.getColumnFamily());
    }

    @Override
    public SelectBuilder<T> eq(String name, Object value) {
        select.where(QueryBuilder.eq(classBean.toColumn(name), value));
        return this;
    }

    @Override
    public SelectBuilder<T> in(String name, Object... values) {
        select.where(QueryBuilder.in(classBean.toColumn(name), values));
        return this;
    }

    @Override
    public SelectBuilder<T> lt(String name, Object value) {

        select.where(QueryBuilder.lt(classBean.toColumn(name), value));
        return this;
    }

    @Override
    public SelectBuilder<T> lte(String name, Object value) {
        select.where(QueryBuilder.lte(classBean.toColumn(name), value));
        return this;
    }

    @Override
    public SelectBuilder<T> gt(String name, Object value) {
        select.where(QueryBuilder.gt(classBean.toColumn(name), value));
        return this;
    }

    @Override
    public SelectBuilder<T> gte(String name, Object value) {
        select.where(QueryBuilder.gte(classBean.toColumn(name), value));
        return this;
    }

    @Override
    public SelectBuilder<T> between(String name, Object startValue,
            Object endValue) {
        return lte(name, startValue).gt(name, endValue);
    }

    @Override
    public SelectBuilder<T> betweenInclusive(String name, Object startValue,
            Object endValue) {
        return lte(name, startValue).gte(name, endValue);
    }

    @Override
    public SelectBuilder<T> betweenExclusive(String name, Object startValue,
            Object endValue) {
        return lt(name, startValue).gt(name, endValue);
    }

    @Override
    public SelectBuilder<T> asc(String name) {
        select.orderBy(QueryBuilder.asc(classBean.toColumn(name)));
        return this;
    }

    @Override
    public SelectBuilder<T> desc(String name) {
        select.orderBy(QueryBuilder.desc(classBean.toColumn(name)));
        return this;
    }

    @Override
    public SelectBuilder<T> withConsistencyLevel(ConsistencyLevel consistency) {
        select.setConsistencyLevel(consistency);
        return this;
    }

    @Override
    public SelectBuilder<T> withTracing(boolean tracing) {
        if (tracing) {
            select.enableTracing();
        } else {
            select.disableTracing();
        }
        return this;
    }
    @Override
    public SelectBuilder<T> allowFiltering() {
        select.allowFiltering();
        return this;
    }

    @Override
    public SelectBuilder<T> withLimit(int limit) {
        select.limit(limit);
        return this;
    }

    @Override
    public SelectBuilder<T> withFetchSize(int fetchSize) {
        select.setFetchSize(fetchSize);
        return this;
    }

    @Override
    public SelectBuilder<T> withRetryPolicy(RetryPolicy policy) {
        select.setRetryPolicy(policy);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> execute() {
        ResultSet resultSet = session.execute(select);
        return (List<T>) RecoveryObject.INTANCE.recoverObjet(classBean.getClassInstance(),
                resultSet);
    }
    @Override
    public void executeAssync(ResultCallBack<List<T>> callBack) {
        ResultSetFuture resultSet = session.executeAsync(select);
        AsyncResult.INSTANCE.runSelect(callBack, resultSet, classBean.getClassInstance());
    }
    @Override
    public String toString() {
        return select.toString();
    }

}
