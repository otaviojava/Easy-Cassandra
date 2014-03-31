package org.easycassandra.persistence.cassandra;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Session;

/**
 * Execute insert async.
 * @author otaviojava
 */
class InsertQueryAsync extends InsertQuery {

    InsertQueryAsync(String keySpace) {
        super(keySpace);
    }

    public <T> void prepareAsync(T bean, Session session,
            ConsistencyLevel consistency) {

        session.executeAsync(createStatment(bean, consistency));
    }

    public <T> void prepareAsync(Iterable<T> beans, Session session,
            ConsistencyLevel consistency) {

        for (T bean : beans) {
            prepare(bean, session, consistency);
        }
    }
}
