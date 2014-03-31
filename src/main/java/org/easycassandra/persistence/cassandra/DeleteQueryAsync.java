package org.easycassandra.persistence.cassandra;

import java.util.LinkedList;
import java.util.List;

import org.easycassandra.ClassInformation;
import org.easycassandra.ClassInformations;
import org.easycassandra.FieldInformation;
import org.easycassandra.util.ReflectionUtil;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Delete;

/**
 * execute delete on async way.
 * @author otaviojava
 *
 */
class DeleteQueryAsync extends DeleteQuery {

    public DeleteQueryAsync(String keySpace) {
        super(keySpace);
    }

    public <K> void deleteByKeyAsync(K key, Class<?> bean, Session session,
            ConsistencyLevel consistency) {
        Delete delete = runDelete(key, bean, consistency);
        session.executeAsync(delete);
    }

    public <T> void deleteByKeyAsync(T bean, Session session,
            ConsistencyLevel consistency) {
        ClassInformation classInformation = ClassInformations.INSTACE.getClass(bean.getClass());
        FieldInformation keyField = classInformation.getKeyInformation();

        deleteByKeyAsync(
                ReflectionUtil.INSTANCE.getMethod(bean, keyField.getField()),
                bean.getClass(), session, consistency);
    }

    public <T> void deleteByKeyAsync(Iterable<T> beans, Session session,
            ConsistencyLevel consistency) {

        List<Object> keys = new LinkedList<Object>();
        Class<?> beanClass = null;
        for (T bean : beans) {

            if (beanClass == null) {
                beanClass = bean.getClass();
            }
        }
        deleteByKeyAsync(keys, beanClass, session, consistency);
    }

    public <K> void deleteByKeyAsync(Iterable<K> keys, Class<?> bean,
            Session session, ConsistencyLevel consistency) {


        for (K key : keys) {
            session.executeAsync(runDelete(key, bean, consistency));
        }
    }

}
