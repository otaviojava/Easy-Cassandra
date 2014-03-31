package org.easycassandra.persistence.cassandra;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Truncate;

/**
 * execute async truncate.
 * @author otaviojava
 */
class TruncateQueryAsync extends TruncateQuery {

    public TruncateQueryAsync(String keySpace) {
        super(keySpace);
    }
    public <T> void truncateAsync(Class<T> bean, Session session) {
        Truncate query = getQuery(bean);
        session.executeAsync(query.toString());
    }

}
