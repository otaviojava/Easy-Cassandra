package org.easycassandra.persistence.cassandra;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.policies.RetryPolicy;
/**
 * Base to insert query on Cassandra.
 * @author otaviojava
 * @param <T>
 */
public interface UpdateBuilder<T> {
    /**
     * Enables or not tracing for this query.
     * @param tracing the tracing
     * @return Adds if true or Disables if not
     */
    UpdateBuilder<T> withTracing(boolean tracing);
    /**
     * Sets the query fetch size.
     * @see {@link com.datastax.driver.core.Statement#setFetchSize(int)}
     * @param fetchSize the fetch size to use.
     * @return this
     */
    UpdateBuilder<T> withFetchSize(int fetchSize);

    /**
     * Sets the consistency level for the query.
     * @param consistency the consistency level to set.
     * @return this object.
     */
    UpdateBuilder<T> withConsistencyLevel(ConsistencyLevel consistency);
    /**
     * Allows to force this builder to not generate values (through its getValues() method).
     * @param forceNoValues  whether or not this builder may generate values.
     * @return this
     */
    UpdateBuilder<T> setForceNoValues(boolean forceNoValues);
    /**
     * Sets the retry policy to use for this query.
     * @param policy the retry policy to use for this query.
     * @return this
     */
    UpdateBuilder<T> withRetryPolicy(RetryPolicy policy);
    /**
     * Sets the serial consistency level for the query.
     * @param serialConsistency the serial consistency level to set.
     * @return this
     */
    UpdateBuilder<T> withSerialConsistencyLevel(
            ConsistencyLevel serialConsistency);
    /**
     * Option to set the timestamp for a modification query.
     * @param timeStamp timestamp the timestamp (in microseconds)
     * @return this
     */
    UpdateBuilder<T> withTimeStamp(long timeStamp);
    /**
     * Option to set the ttl for a modification query.
     * @param ttl the ttl (in seconds) to use.
     * @return this
     */
    UpdateBuilder<T> withTtl(int ttl);
    /**
     * Simple "set" assignment of a value to a column. This will generate: name = value.
     * @param name the name of the column to insert/update.
     * @param value the value to insert for name.
     * @return this
     */
    UpdateBuilder<T> value(String name, Object value);
    /**
     * Adds a value to a set column. This will generate: name = name + {value}}.
     * @param name the column name
     * @param value the value to add
     * @return this
     */
    UpdateBuilder<T> addSet(String name, Object value);
    /**
     * Adds a set of values to a set column. This will generate: name = name + set.
     * @param name name the column name, must be set
     * @param set the set of values to append
     * @return this
     */
    UpdateBuilder<T> addSetAll(String name, Set<?> set);
    /**
     * Remove a value from a set column. This will generate: name = name - {value}}.
     * @param name the column name (must be of type set).
     * @param value the value to remove
     * @return this
     */
    UpdateBuilder<T> removeSet(String name, Object value);
    /**
     * Remove a set of values from a set column. This will generate: name = name - set.
     * @param name the column name (must be of type set).
     * @param set the of value to remove
     * @return this
     */
    UpdateBuilder<T> removeSetAll(String name, Set<?> set);
    /**
     * Append a value to a list column.
     * This will generate: {@code name = name + [value]}.
     * @param name the column name (must be of type list).
     * @param value the value to append
     * @return this
     */
    UpdateBuilder<T> addList(String name, Object value);
    /**
     * Append a list of values to a list column.
     * This will generate: {@code name = name + list}.
     * @param name the column name (must be of type list).
     * @param list the list of values to append
     * @return this
     */
    UpdateBuilder<T> addListAll(String name, List<?> list);
    /**
     * Sets a list column value by index.
     * This will generate: {@code name[idx] = value}.
     * @param name the column name (must be of type list).
     * @param index the index to set
     * @param value the value to set
     * @return this
     */
    UpdateBuilder<T> addIndexList(String name, int index, Object value);
    /**
     * Prepend a value to a list column.
     * This will generate: {@code name = [ value ] + name}.
     * @param name the column name (must be of type list).
     * @param value the value to prepend
     * @return this
     */
    UpdateBuilder<T> preAddList(String name, Object value);
    /**
     * Prepend a list of values to a list column.
     * This will generate: {@code name = list + name}.
     * @param name the column name (must be of type list).
     * @param list the list of values to prepend.
     * @return this
     */
    UpdateBuilder<T> preAddListAll(String name, List<?> list);
    /**
     * Discard a value from a list column.
     * This will generate: {@code name = name - [value]}.
     * @param name the column name (must be of type list).
     * @param value the value to discard
     * @return this
     */
    UpdateBuilder<T> removeList(String name, Object value);
    /**
     * Discard a list of values to a list column.
     * This will generate: {@code name = name - list}.
     * @param name the column name (must be of type list).
     * @param list the list of values to discard
     * @return this
     */
    UpdateBuilder<T> removeListAll(String name, List<?> list);
    /**
     * Puts a new key/value pair to a map column.
     * This will generate: {@code name[key] = value}.
     * @param name the column name (must be of type map).
     * @param key the key to put
     * @param value the value to put
     * @return this
     */
    UpdateBuilder<T> put(String name, Object key, Object value);
    /**
     * Puts a map of new key/value pairs to a map column.
     * This will generate: {@code name = name + map}.
     * @param name the column name (must be of type map).
     * @param map the map of key/value pairs to put
     * @return this
     */
    UpdateBuilder<T> put(String name, Map<?, ?> map);
    /**
     * Simple "set" assignment of a value to a column with enum.
     * @param name the name of the column to insert/update.
     * @param value the value to insert for name.
     * @return this
     */
    UpdateBuilder<T> enumValue(String name, Enum<?> value);
    /**
     * Simple "set" assignment of a value to a column with custom default.
     * @param name the name of the column to insert/update.
     * @param value the value to insert for name.
     * @return this
     */
    UpdateBuilder<T> customValue(String name, Object value);
    /**
     * Simple "set" assignment of a value to a column with custom.
     * @param name the name of the column to insert/update.
     * @param value the value to insert for name.
     * @param customizable the custom class
     * @return this
     */
    UpdateBuilder<T> customValue(String name, Object value, Customizable customizable);
    /**
     * Creates an "equal" where clause.
     * @param name the column name
     * @param value the value
     * @return this
     */
    UpdateBuilder<T> whereEq(String name, Object value);
    /**
     * Create an "in" where clause stating the provided column must be equal
     * to one of the provided values.
     * @param name the column name
     * @param values the values
     * @return this
     */
    UpdateBuilder<T> whereIn(String name, Object... values);
    /**
     * execute the query.
     * @return if run with success
     */
    boolean execute();

}