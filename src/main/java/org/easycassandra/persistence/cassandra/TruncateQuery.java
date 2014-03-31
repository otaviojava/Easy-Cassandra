package org.easycassandra.persistence.cassandra;

import org.easycassandra.ClassInformation;
import org.easycassandra.ClassInformations;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Truncate;
/**
 * Command to remove all information on Cassandra.
 * @author otaviojava
 */
class TruncateQuery {

    private String keySpace;

    public TruncateQuery(String keySpace) {
        this.keySpace = keySpace;
    }
    /**
     *truncate the column family.
     * @param bean the kind of object
     * @param session the session
     * @param <T> the kind of object
     */
    public <T> void truncate(Class<T> bean, Session session) {
        Truncate query = getQuery(bean);
        session.execute(query.toString());
    }
    protected <T> Truncate getQuery(Class<T> bean) {
        ClassInformation classInformation = ClassInformations.INSTACE
                .getClass(bean);
        Truncate query = QueryBuilder
                .truncate(keySpace, classInformation.getNameSchema());
        return query;
    }
}
