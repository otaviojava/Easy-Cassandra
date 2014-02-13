package org.easycassandra.persistence.cassandra;
/**
 * Builders of alterations on the Cassandra Database (update, insert, delete, batch).
 * @author otaviojava
 */
public interface AlterationBuilder {
    /**
     * execute the query.
     * @return if run with success
     */
    boolean execute();

    /**
     * update the provided query asynchronously.
     */
    void executeAsync();
    /**
     * execute the process asynchronous.
     * @param resultCallBack the callback
     */
    void executeAsync(ResultCallBack<Boolean> resultCallBack);
}
