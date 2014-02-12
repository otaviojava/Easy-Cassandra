package org.easycassandra.persistence.cassandra;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.policies.RetryPolicy;
/**
 * Base to insert query on Cassandra.
 * @author otaviojava
 * @param <T>
 */
public interface InsertBuilder<T> {
    /**
     * Enables or not tracing for this query.
     * @param tracing the tracing
     * @return Adds if true or Disables if not
     */
    InsertBuilder<T> withTracing(boolean tracing);
    /**
     * Sets the query fetch size.
     * @see {@link com.datastax.driver.core.Statement#setFetchSize(int)}
     * @param fetchSize the fetch size to use.
     * @return this
     */
    InsertBuilder<T> withFetchSize(int fetchSize);

    /**
     * Sets the consistency level for the query.
     * @param consistency the consistency level to set.
     * @return this object.
     */
    InsertBuilder<T> withConsistencyLevel(ConsistencyLevel consistency);
    /**
     * Sets the 'IF NOT EXISTS' option for this INSERT statement.
     * @return this object
     */
    InsertBuilder<T> ifNotExists();
    /**
     * Allows to force this builder to not generate values (through its getValues() method).
     * @param forceNoValues  whether or not this builder may generate values.
     * @return this
     */
    InsertBuilder<T> setForceNoValues(boolean forceNoValues);
    /**
     * Sets the retry policy to use for this query.
     * @param policy the retry policy to use for this query.
     * @return this
     */
    InsertBuilder<T> withRetryPolicy(RetryPolicy policy);
    /**
     * Sets the serial consistency level for the query.
     * @param serialConsistency the serial consistency level to set.
     * @return this
     */
    InsertBuilder<T> withSerialConsistencyLevel(
            ConsistencyLevel serialConsistency);
    /**
     * Option to set the timestamp for a modification query.
     * @param timeStamp timestamp the timestamp (in microseconds)
     * @return this
     */
    InsertBuilder<T> withTimeStamp(long timeStamp);
    /**
     * Option to set the ttl for a modification query.
     * @param ttl the ttl (in seconds) to use.
     * @return this
     */
    InsertBuilder<T> withTtl(int ttl);
    /**
     * Adds a column/value pair to the values inserted by this INSERT statement.
     * @param name the name of the column to insert/update.
     * @param value the value to insert for name.
     * @return this
     */
    InsertBuilder<T> value(String name, Object value);
    /**
     * Adds a column/value pair to the values inserted by this INSERT statement.
     * @param names the names of the column to insert/update.
     * @param values the values to insert for name.
     * @return this
     */
    InsertBuilder<T> values(String[] names, Object[] values);
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
    /**
     * Adds a column/value pair to the values inserted by this INSERT statement with enum.
    *  @param name the name of the column to insert/update.
     * @param value the value to insert for name.
     * @return this
     */
    InsertBuilder<T> enumValue(String name, Enum<?> value);
    /**
     * Adds a column/value pair to the values inserted by this INSERT statement with custom default.
    *  @param name the name of the column to insert/update.
     * @param value the value to insert for name.
     * @return this
     */
    InsertBuilder<T> customValue(String name, Object value);
    /**
     * Adds a column/value pair to the values inserted by this INSERT statement with custom.
    *  @param name the name of the column to insert/update.
     * @param value the value to insert for name.
     * @param customizable the custom interface
     * @return this
     */
    InsertBuilder<T> customValue(String name, Object value, Customizable customizable);

}