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

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EmbeddedId;

import org.easycassandra.KeyProblemsException;
import org.easycassandra.persistence.cassandra.InsertColumnUtil.InsertColumn;
import org.easycassandra.util.ReflectionUtil;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

/**
 * Class to mounts and runs the query to insert a row in a column family
 * 
 * @author otaviojava
 * 
 */
class InsertQuery {

    public boolean prepare(Object bean, Session session) {

        isKeyNull(bean);
        InsertBean insertBean = new InsertBean();

        createBeginInsertQuery(bean, insertBean);
        insertBean = createQueryInsert(bean, insertBean);
        createEndInsertQuery(insertBean);

        PreparedStatement statement = session.prepare(insertBean.query.toString());
        BoundStatement boundStatement = new BoundStatement(statement);
        session.execute(boundStatement.bind(insertBean.objets.toArray()));
        return true;
    }

    private void createEndInsertQuery(InsertBean insertBean) {
        insertBean.query.append(") VALUES (");
        for (int index = 1; index <= insertBean.count; index++) {
            if (index > 1) {
                insertBean.query.append(",");
            }
            insertBean.query.append(" ?");
        }
        insertBean.query.append(" );");
    }

    private void createBeginInsertQuery(Object bean, InsertBean insertBean) {
        insertBean.query.append("insert into ");
        insertBean.query.append(ColumnUtil.INTANCE.getColumnFamilyName(bean.getClass()));
        insertBean.query.append("(");
    }

    public InsertBean createQueryInsert(Object bean, InsertBean insertBean) {

        for (Field field : ColumnUtil.INTANCE.listFields(bean.getClass())) {
            
            if (ColumnUtil.INTANCE.isEmbeddedField(field) || ColumnUtil.INTANCE.isEmbeddedIdField(field)) {
                if (ReflectionUtil.getMethod(bean, field) != null) {
                    insertBean = createQueryInsert(ReflectionUtil.getMethod(bean, field), insertBean);
                }
                continue;
            }

            else if (ReflectionUtil.getMethod(bean, field) != null) {
                if (insertBean.count > 0) {
                    insertBean.query.append(",");
                }
                insertBean.query.append(ColumnUtil.INTANCE.getColumnName(field));
                insertBean.count++;
                InsertColumn insertColumn=InsertColumnUtil.INSTANCE.factory(field);
                insertBean.objets.add(insertColumn.getObject(bean, field));

            }
        }
        return insertBean;
    }

    /**
     * Verify if key is nut and make a exception
     * 
     * @param bean
     */
    private void isKeyNull(Object bean) {
        Field key = ColumnUtil.INTANCE.getKeyField(bean.getClass());
        if (key == null) {
            
            key = ColumnUtil.INTANCE.getField(bean.getClass(), EmbeddedId.class);
            isKeyNull(ReflectionUtil.getMethod(bean, key), key.getType().getDeclaredFields());
        } else {
            isKeyNull(bean, key);
        }

    }

    private void isKeyNull(Object bean, Field... fields) {
        for (Field field : fields) {
            if (ReflectionUtil.getMethod(bean, field) == null) {
                throw new KeyProblemsException("Key is mandatory to insert a new column family");
            }
        }
    }

    private class InsertBean {
        private StringBuilder query = new StringBuilder();
        private int count;
        private List<Object> objets = new LinkedList<Object>();
    }
}
