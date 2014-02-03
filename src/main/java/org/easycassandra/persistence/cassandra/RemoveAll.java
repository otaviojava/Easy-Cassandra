package org.easycassandra.persistence.cassandra;

import org.easycassandra.ClassInformation;
import org.easycassandra.ClassInformations;

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
        ClassInformation classInformation = ClassInformations.INSTACE.getClass(bean);
        StringBuilder query = new StringBuilder();
        query.append("TRUNCATE ")
                .append(classInformation.getNameSchema())
                .append(";");
        session.execute(query.toString());
    }
}
