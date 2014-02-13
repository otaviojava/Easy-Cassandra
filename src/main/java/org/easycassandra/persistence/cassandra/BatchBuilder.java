package org.easycassandra.persistence.cassandra;

import com.datastax.driver.core.ConsistencyLevel;


/**
 * The BATCH statement group multiple modification statements
 * (insertions/updates and deletions) into a single statement.
 * @author otaviojava
 *
 */
public interface BatchBuilder extends AlterationBuilder {
    /**
     * Enables or not tracing for this query.
     * @param tracing the tracing
     * @return Adds if true or Disables if not
     */
    BatchBuilder withTracing(boolean tracing);
    /**
     * Sets the query fetch size.
     * @see {@link com.datastax.driver.core.Statement#setFetchSize(int)}
     * @param fetchSize the fetch size to use.
     * @return this
     */
    BatchBuilder withFetchSize(int fetchSize);

    /**
     * Sets the consistency level for the query.
     * @param consistency the consistency level to set.
     * @return this object.
     */
    BatchBuilder withConsistencyLevel(ConsistencyLevel consistency);
    /**
     * Sets the serial consistency level for the query.
     * @param serialConsistency the serial consistency level to set.
     * @return this
     */
    BatchBuilder withSerialConsistencyLevel(
            ConsistencyLevel serialConsistency);
    /**
     * Option to set the timestamp for a modification query.
     * @param timeStamp timestamp the timestamp (in microseconds)
     * @return this
     */
    BatchBuilder withTimeStamp(long timeStamp);
    /**
     * Option to set the ttl for a modification query.
     * @param ttl the ttl (in seconds) to use.
     * @return this
     */
    BatchBuilder withTtl(int ttl);
    /**
     *  Adds a new statement to this batch.
     * @param builders the statment
     * @return this
     */
    BatchBuilder addOperations(SingleInsertStatment... builders);
}
