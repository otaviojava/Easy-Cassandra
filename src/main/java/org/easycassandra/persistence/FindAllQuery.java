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
package org.easycassandra.persistence;

import java.lang.reflect.Field;
import java.util.List;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

/**
 * Mount and run the query to returns all data from column family
 * 
 * @author otaviojava
 * 
 */
public class FindAllQuery {

    public <T> List<T> listAll(Class<T> bean, Session session) {
        QueryBean byKeyBean = new QueryBean();

        byKeyBean.stringBuilder.append("select ");
        byKeyBean = prepare(byKeyBean, bean);
        byKeyBean.stringBuilder.deleteCharAt(byKeyBean.stringBuilder.length() - 1);
        byKeyBean.stringBuilder.append(" from ");
        byKeyBean.stringBuilder.append(ColumnUtil.INTANCE.getColumnFamilyName(bean));

        return executeConditions(bean, session, byKeyBean);
    }

    private <T> List<T> executeConditions(Class<T> bean, Session session,
            QueryBean queryBean) {
        queryBean.stringBuilder.append(";");
        ResultSet resultSet = session.execute(queryBean.stringBuilder.toString());
        return RecoveryObject.INTANCE.recoverObjet(bean, resultSet);
    }

    protected QueryBean prepare(QueryBean byKeyBean, Class<?> class1) {
        List<Field> fields = ColumnUtil.INTANCE.listFields(class1);

        for (Field field : fields) {
            
            if (ColumnUtil.INTANCE.isEmbeddedField(field)  || ColumnUtil.INTANCE.isEmbeddedIdField(field)) {
                if (ColumnUtil.INTANCE.isEmbeddedIdField(field)) {
                    byKeyBean.key = field;
                }
                byKeyBean = prepare(byKeyBean, field.getType());
                continue;
            }

            else if (ColumnUtil.INTANCE.isIdField(field)) {
                byKeyBean.key = field;
            }
            String columnName = ColumnUtil.INTANCE.getColumnName(field);

            byKeyBean.stringBuilder.append(columnName).append(" ,");

        }
        return byKeyBean;
    }

    protected class QueryBean {
        protected Field key;
        protected StringBuilder stringBuilder = new StringBuilder();
    }
}
