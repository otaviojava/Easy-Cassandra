package org.easycassandra.persistence.cassandra;

import java.util.List;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.policies.RetryPolicy;

/**
 * select builder, it is easy way to make query with parameters.
 * @author otaviojava
 * @param <T>
 */
public interface SelectBuilder<T> {
    /**
     * Creates an "equal" where clause stating the provided column must be
     * equal to the provided value.
     * @param name the column name
     * @param value the value
     * @return the corresponding where clause.
     */
    SelectBuilder<T> eq(String name, Object value);
    /**
     * Create an "in" where clause stating the provided column must be equal
     * to one of the provided values.
     *
     * @param name the column name
     * @param values the values
     * @return the corresponding where clause.
     */
    SelectBuilder<T> in(String name, Object... values);
    /**
     * Creates a "lesser than" where clause stating the provided column must be less than
     * the provided value.
     *
     * @param name the column name
     * @param value the value
     * @return the corresponding where clause.
     */
    SelectBuilder<T> lt(String name, Object value);

    /**
     * Creates a "lesser than or equal" where clause stating the provided column must
     * be lesser than or equal to the provided value.
     *
     * @param name the column name
     * @param value the value
     * @return the corresponding where clause.
     */
    SelectBuilder<T> lte(String name, Object value);

    /**
     * Creates a "greater than" where clause stating the provided column must
     * be greater to the provided value.
     *
     * @param name the column name
     * @param value the value
     * @return the corresponding where clause.
     */
    SelectBuilder<T> gt(String name, Object value);

    /**
     * Creates a "greater than or equal" where clause stating the provided
     * column must be greater than or equal to the provided value.
     *
     * @param name the column name
     * @param value the value
     * @return the corresponding where clause.
     */
    SelectBuilder<T> gte(String name, Object value);
    /**
     * add clause query between which value begin inclusive and value end and exclusive.
     * @param name the name of field
     * @param startValue the begin value inclusive
     * @param endValue the end value exclusive
     * @return this
     */
    SelectBuilder<T> between(String name, Object startValue, Object endValue);
    /**
     * add clause query between which value begin inclusive and value end and inclusive.
     * @param name the name of field
     * @param startValue the begin value inclusive
     * @param endValue the end value inclusive
     * @return this
     */
    SelectBuilder<T> betweenInclusive(String name, Object startValue, Object endValue);
    /**
     * add clause query between which value begin exclusive and value and exclusive.
     * @param name the name of field
     * @param startValue the begin value exclusive
     * @param endValue the end value exclusive
     * @return this
     */
    SelectBuilder<T> betweenExclusive(String name, Object startValue, Object endValue);

    /**
     * Ascending ordering for the provided column.
     * @param name the column name
     * @return the corresponding ordering
     */
    SelectBuilder<T> asc(String name);

    /**
     * Descending ordering for the provided column.
     * @param name the column name
     * @return the corresponding ordering
     */
    SelectBuilder<T> desc(String name);
    /**
     * Sets the consistency level for the query.
     * @param consistency the consistency level to set.
     * @return this object.
     */
    SelectBuilder<T> withConsistencyLevel(ConsistencyLevel consistency);
    /**
     * Enables or not tracing for this query.
     * @param tracing the tracing
     * @return Adds if true or Disables if not
     */
    SelectBuilder<T> withTracing(boolean tracing);

    /**
     * Adds an ALLOW FILTERING directive to this statement.
     * @return this
     */
    SelectBuilder<T> allowFiltering();
     /**
      * Adds a LIMIT clause to this statement.
      * @param limit limit the limit to set
      * @return this
      */
    SelectBuilder<T> withLimit(int limit);
      /**
       * Sets the query fetch size.
       * @see {@link com.datastax.driver.core.Statement#setFetchSize(int)}
       * @param fetchSize the fetch size to use.
       * @return this
       */
    SelectBuilder<T> withFetchSize(int fetchSize);
        /**
         * Sets the retry policy to use for this query.
         * @param policy the retry policy to use for this query.
         * @return this
         */
    SelectBuilder<T> withFetchSize(RetryPolicy policy);
    /**
     * run the query.
     * @return list by query
     */
    List<T> execute();
}