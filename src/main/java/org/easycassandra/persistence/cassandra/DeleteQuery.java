/*
 * Copyright 2013 Otávio Gonçalves de Santana (otaviojava)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.easycassandra.persistence.cassandra;

import java.util.LinkedList;
import java.util.List;

import org.easycassandra.ClassInformation;
import org.easycassandra.ClassInformation.KeySpaceInformation;
import org.easycassandra.ClassInformations;
import org.easycassandra.FieldInformation;
import org.easycassandra.KeyProblemsException;
import org.easycassandra.util.ReflectionUtil;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;

/**
 * Class to create a query to delete beans.
 * @author otaviojava
 */
class DeleteQuery {

	private String keySpace;

    public DeleteQuery(String keySpace) {
        this.keySpace = keySpace;
    }

    public <T> boolean deleteByKey(T bean, Session session,
            ConsistencyLevel consistency) {
        ClassInformation classInformation = ClassInformations.INSTACE.getClass(bean.getClass());
        FieldInformation keyField = classInformation.getKeyInformation();

        return deleteByKey(ReflectionUtil.INSTANCE.getMethod(bean, keyField.getField()),
                bean.getClass(), session, consistency);
    }

    public <T> boolean deleteByKey(Iterable<T> beans, Session session,
            ConsistencyLevel consistency) {

        List<Object> keys = new LinkedList<Object>();
        Class<?> beanClass = null;
        for (T bean : beans) {

            if (beanClass == null) {
                beanClass = bean.getClass();
            }

        }
        deleteByKey(keys, beanClass, session, consistency);
        return true;
    }

    public <K> boolean deleteByKey(K key, Class<?> bean, Session session,
            ConsistencyLevel consistency) {
        Delete delete = runDelete(key, bean, consistency);
        session.execute(delete);
        return true;
    }

    public <K> boolean deleteByKey(Iterable<K> keys, Class<?> bean,
            Session session, ConsistencyLevel consistency) {

        for (K key : keys) {
            session.execute(runDelete(key, bean, consistency));

        }
        return true;
    }


    public Delete runDelete(Object key, Class<?> bean,
            ConsistencyLevel consistency) {
        if (key == null) {
            throw new KeyProblemsException(
                    "The parameter key to column family should be passed");
        }
        ClassInformation classInformations = ClassInformations.INSTACE.getClass(bean);
        KeySpaceInformation keyInformation = classInformations.getKeySpace(keySpace);
        Delete delete = QueryBuilder
                .delete()
                .all()
                .from(keyInformation.getKeySpace(),
                        keyInformation.getColumnFamily());

        FieldInformation keyField = classInformations.getKeyInformation();
        if (classInformations.isComplexKey()) {
            runComplexKey(delete, key, keyField.getSubFields().getFields());
        } else {
            delete.where(QueryBuilder.eq(keyField.getName(), key));
        }

        delete.setConsistencyLevel(consistency);
        return delete;
    }

    private void runComplexKey(Delete delete, Object key, List<FieldInformation> fields) {

        for (FieldInformation subKey : fields) {
            delete.where(QueryBuilder.eq(
                    subKey.getName(),
                    ReflectionUtil.INSTANCE.getMethod(key, subKey.getField())));
        }
    }

}
