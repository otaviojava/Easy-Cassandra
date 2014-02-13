package org.easycassandra.persistence.cassandra;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Batch;
import com.datastax.driver.core.querybuilder.QueryBuilder;

/**
 * @see  {@link BatchBuilder}
 * @author otaviojava
 */
public class BatchBuilderImpl implements BatchBuilder {

    private Batch batch;

    private Session session;

    /**
     * constructor.
     * @param session the session
     */
    public BatchBuilderImpl(Session session) {
        this.session = session;
        batch = QueryBuilder.batch();
    }

    @Override
    public BatchBuilder withTracing(boolean tracing) {
        if (tracing) {
            batch.enableTracing();
        } else {
            batch.disableTracing();
        }
        return this;
    }

    @Override
    public BatchBuilder withFetchSize(int fetchSize) {
        batch.setFetchSize(fetchSize);
        return this;
    }

    @Override
    public BatchBuilder withConsistencyLevel(ConsistencyLevel consistency) {
        batch.setConsistencyLevel(consistency);
        return this;
    }

    @Override
    public BatchBuilder withSerialConsistencyLevel(
            ConsistencyLevel serialConsistency) {
        batch.setSerialConsistencyLevel(serialConsistency);
        return this;
    }
    @Override
    public BatchBuilder withTimeStamp(long timeStamp) {
        batch.using(QueryBuilder.timestamp(timeStamp));
        return this;
    }

    @Override
    public BatchBuilder withTtl(int ttl) {
        batch.using(QueryBuilder.ttl(ttl));
        return this;
    }

    @Override
    public BatchBuilder addOperations(SingleInsertStatment... builders) {

        for (SingleInsertStatment alterarion: builders) {
            batch.add(alterarion.getStatement());
        }
        return this;
    }

    @Override
    public boolean execute() {
        return session.execute(batch) != null;
    }

    @Override
    public void executeAsync() {
        session.executeAsync(batch);
    }

    @Override
    public void executeAsync(ResultCallBack<Boolean> resultCallBack) {
        AsyncResult.INSTANCE.runUpdate(resultCallBack, session.executeAsync(batch));
    }

    @Override
    public String toString() {
        return batch.toString();
    }
}
