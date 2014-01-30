package org.easycassandra.persistence.cassandra;

import com.datastax.driver.core.Session;
/**
 * Command to remove all information on Cassandra.
 * @author otaviojava
 */
class RemoveAll {
    /**
     *truncate the column family.
     * @param bean the kind of object
     * @param session the session
     * @param <T> the kind of object
     */
    public <T> void truncate(Class<T> bean, Session session) {
        StringBuilder query = new StringBuilder();
        query.append("TRUNCATE ")
                .append(ColumnUtil.INTANCE.getColumnFamilyNameSchema(bean))
                .append(";");
        session.execute(query.toString());
    }
}
