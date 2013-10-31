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
import java.util.List;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import org.easycassandra.util.ReflectionUtil;

/**
 * Query to find by primary key and index combination
 *
 * @author Nenita Casuga
 * @since 10/28/13
 */
class FindByKeyAndIndexQuery extends FindByKeyQuery {

    public FindByKeyAndIndexQuery(String keySpace) {
        super(keySpace);
    }

    public <T, I> List<T> findByKeyAndIndex(Object key, I index, Class<T> bean, Session session, ConsistencyLevel consistency) {
        Field field = ColumnUtil.INTANCE.getIndexField(bean);
        FindByIndexQuery.checkFieldNull(bean, field);
        return findByKeyAndIndex(key, field.getName(), index, bean, session, consistency);
    }

    public <T, I> List<T> findByKeyAndIndex(Object key, String indexName, I index, Class<T> bean, Session session, ConsistencyLevel consistency) {
        QueryBean byKeyBean = createQueryBean(bean, consistency);
        return executeConditions(indexName, index, null, null, key, bean, session, byKeyBean);
    }

    public <T, I> List<T> findByKeyAndIndex(Object key, I indexStart, I indexEnd, boolean inclusive, Class<T> bean, Session session, ConsistencyLevel consistency) {
        Field field = ColumnUtil.INTANCE.getIndexField(bean);
        FindByIndexQuery.checkFieldNull(bean, field);
        return findByKeyAndIndex(key, field.getName(), indexStart, indexEnd, inclusive, bean, session, consistency);
    }

    public <T, I> List<T> findByKeyAndIndex(Object key, String indexName, I indexStart, I indexEnd, boolean inclusive,
                                            Class<T> bean, Session session, ConsistencyLevel consistency) {
        QueryBean byKeyBean = createQueryBean(bean, consistency);
        return executeConditions(indexName, indexStart, indexEnd, inclusive, key, bean, session, byKeyBean);
    }

    private <T> List<T> executeConditions(String indexName, Object indexStart, Object indexEnd, Boolean inclusive, Object key,
                                          Class<T> bean, Session session, QueryBean byKeyBean) {

        // Add primary keys
        super.prepare(byKeyBean, bean);
        if (ColumnUtil.INTANCE.isEmbeddedIdField(byKeyBean.key)) {
            for (Field complexKey : ColumnUtil.INTANCE.listFields(key.getClass())) {
                byKeyBean.select.where(QueryBuilder.eq(ColumnUtil.INTANCE.getColumnName(complexKey), ReflectionUtil.INSTANCE.getMethod(key, complexKey)));
            }
        } else {
            byKeyBean.select.where(QueryBuilder.eq(ColumnUtil.INTANCE.getColumnName(byKeyBean.key), key));
        }

        // Add index criteria
        byKeyBean.key = ColumnUtil.INTANCE.getFieldByColumnName(indexName, bean);
        FindByIndexQuery.checkIndexProblem(bean, byKeyBean);

        // Add indexed key
        if (indexEnd != null && inclusive != null) {

            if (inclusive) {
                byKeyBean.select.where(QueryBuilder.gte(ColumnUtil.INTANCE.getColumnName(byKeyBean.key), indexStart));
                byKeyBean.select.where(QueryBuilder.lte(ColumnUtil.INTANCE.getColumnName(byKeyBean.key), indexEnd));
            } else {
                byKeyBean.select.where(QueryBuilder.gt(ColumnUtil.INTANCE.getColumnName(byKeyBean.key), indexStart));
                byKeyBean.select.where(QueryBuilder.lt(ColumnUtil.INTANCE.getColumnName(byKeyBean.key), indexEnd));
            }

        } else {
            byKeyBean.select.where(QueryBuilder.eq(ColumnUtil.INTANCE.getColumnName(byKeyBean.key), indexStart));
        }

        // Execute
        ResultSet resultSet = session.execute(byKeyBean.select);
        return RecoveryObject.INTANCE.recoverObjet(bean, resultSet);
    }
}
