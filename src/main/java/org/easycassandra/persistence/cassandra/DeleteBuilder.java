package org.easycassandra.persistence.cassandra;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.policies.RetryPolicy;
/**
 * Base to insert query on Cassandra.
 * @author otaviojava
 * @param <T>
 */
public interface DeleteBuilder<T> {
    /**
     * Enables or not tracing for this query.
     * @param tracing the tracing
     * @return Adds if true or Disables if not
     */
    DeleteBuilder<T> withTracing(boolean tracing);
    /**
     * Sets the query fetch size.
     * @see {@link com.datastax.driver.core.Statement#setFetchSize(int)}
     * @param fetchSize the fetch size to use.
     * @return this
     */
    DeleteBuilder<T> withFetchSize(int fetchSize);

    /**
     * Sets the consistency level for the query.
     * @param consistency the consistency level to set.
     * @return this object.
     */
    DeleteBuilder<T> withConsistencyLevel(ConsistencyLevel consistency);
    /**
     * Allows to force this builder to not generate values (through its getValues() method).
     * @param forceNoValues  whether or not this builder may generate values.
     * @return this
     */
    DeleteBuilder<T> setForceNoValues(boolean forceNoValues);
    /**
     * Sets the retry policy to use for this query.
     * @param policy the retry policy to use for this query.
     * @return this
     */
    DeleteBuilder<T> withRetryPolicy(RetryPolicy policy);
    /**
     * Sets the serial consistency level for the query.
     * @param serialConsistency the serial consistency level to set.
     * @return this
     */
    DeleteBuilder<T> withSerialConsistencyLevel(
            ConsistencyLevel serialConsistency);
    /**
     * Option to set the timestamp for a modification query.
     * @param timeStamp timestamp the timestamp (in microseconds)
     * @return this
     */
    DeleteBuilder<T> withTimeStamp(long timeStamp);
    /**
     * Option to set the ttl for a modification query.
     * @param ttl the ttl (in seconds) to use.
     * @return this
     */
    DeleteBuilder<T> withTtl(int ttl);
    /**
     * Creates an "equal" where clause.
     * @param name the column name
     * @param value the value
     * @return this
     */
    DeleteBuilder<T> whereEq(String name, Object value);
    /**
     * Create an "in" where clause stating the provided column must be equal
     * to one of the provided values.
     * @param name the column name
     * @param values the values
     * @return this
     */
    DeleteBuilder<T> whereIn(String name, Object... values);
    /**
     * execute the query.
     * @return if run with success
     */
    boolean execute();
    /**
     * Executes the provided query asynchronously.
     * @return if run with success
     */
    boolean executeAsync();

}