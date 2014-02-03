/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.easycassandra.persistence.cassandra;

import java.util.Arrays;
import java.util.List;

import org.easycassandra.ClassInformation;
import org.easycassandra.ClassInformation.KeySpaceInformation;
import org.easycassandra.ClassInformations;
import org.easycassandra.FieldInformation;
import org.easycassandra.KeyProblemsException;
import org.easycassandra.persistence.cassandra.InsertColumnUtil.InsertColumn;
import org.easycassandra.util.ReflectionUtil;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;

/**
 * Class to mounts and runs the query to insert a row in a column family.
 * @author otaviojava
 */
class InsertQuery {

    private String keySpace;

    InsertQuery(String keySpace) {
        this.keySpace = keySpace;
    }

    public <T> boolean prepare(T bean, Session session, ConsistencyLevel consistency) {

        session.execute(createStatment(bean, consistency));
        return true;
    }
    public <T> boolean prepare(Iterable<T> beans, Session session, ConsistencyLevel consistency) {

        for (T bean:beans) {
        	prepare(bean, session, consistency);
        }
        return true;
    }


    private <T> Statement createStatment(T bean, ConsistencyLevel consistency) {
        ClassInformation classInformation = ClassInformations.INSTACE.getClass(bean.getClass());
        isKeyNull(bean, classInformation);
        KeySpaceInformation key = classInformation.getKeySpace(keySpace);
        Insert insert = QueryBuilder.insertInto(key.getKeySpace(), key.getColumnFamily());
        insert = createInsert(bean, insert, classInformation);
        insert.setConsistencyLevel(consistency);
        return insert;
    }


    private <T> Insert createInsert(T bean, Insert insert, ClassInformation classInformation) {

        for (FieldInformation field : classInformation.getFields()) {

            if (field.isEmbedded()) {
                if (ReflectionUtil.INSTANCE.getMethod(bean, field.getField()) != null) {

                    ClassInformation subClassInformation =
                            ClassInformations.INSTACE.getClass(field.getField().getType());

                    insert = createInsert(
                            ReflectionUtil.INSTANCE.getMethod(bean, field.getField()),
                            insert, subClassInformation);
                }
                continue;
            } else if (ReflectionUtil.INSTANCE.getMethod(bean, field.getField()) != null) {
                InsertColumn insertColumn = InsertColumnUtil.INSTANCE.factory(field);
                insert.value(field.getName(), insertColumn.getObject(bean, field));

            }
        }
        return insert;
    }

    private void isKeyNull(Object bean, ClassInformation classInformation) {

        if (classInformation.isComplexKey()) {

            FieldInformation keyInformation = classInformation.getKeyInformation();

            verifyKeyNull(ReflectionUtil.INSTANCE.getMethod(bean,
                    keyInformation.getField()), keyInformation.getSubFields()
                    .getFields());
        } else {
            verifyKeyNull(bean, Arrays.asList(classInformation.getKeyInformation()));
        }

    }

    private void verifyKeyNull(Object bean, List<FieldInformation> fields) {
        for (FieldInformation field : fields) {
            if (ReflectionUtil.INSTANCE.getMethod(bean, field.getField()) == null) {
                throw new KeyProblemsException("Key is mandatory to insert a new column family,"
                        + "check: " + field.getName());
            }
        }
    }


}
