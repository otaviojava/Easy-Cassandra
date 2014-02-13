package org.easycassandra.persistence.cassandra;

/**
 * CallBack Interface.
 * @author otaviojava
 * @param <T>
 */
public interface ResultCallBack<T> {
    /**
     * callback to result asynchronous.
     * @param bean the result
     */
    void result(T bean);
}
