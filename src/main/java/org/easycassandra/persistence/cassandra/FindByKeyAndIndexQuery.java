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

import java.util.List;

import org.easycassandra.ClassInformation;
import org.easycassandra.ClassInformations;
import org.easycassandra.FieldInformation;
import org.easycassandra.util.ReflectionUtil;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;

/**
 * Query to find by primary key and index combination.
 * @author Nenita Casuga
 * @since 10/28/13
 */
class FindByKeyAndIndexQuery extends FindByKeyQuery {

    public FindByKeyAndIndexQuery(String keySpace) {
        super(keySpace);
    }

    public <T, I> List<T> findByKeyAndIndex(Object key, I index, Class<T> bean,
            Session session, ConsistencyLevel consistency) {
        ClassInformation classInformation = ClassInformations.INSTACE.getClass(bean);

        FindByIndexQuery.checkFieldNull(bean, classInformation.getFields());
        FieldInformation indexField = classInformation.getIndexFields().get(0);

        return findByKeyAndIndex(key, indexField.getName(), index, bean, session,
                consistency);
    }

    public <T, I> List<T> findByKeyAndIndex(Object key, String indexName,
            I index, Class<T> bean, Session session,
            ConsistencyLevel consistency) {
        QueryBean byKeyBean = createQueryBean(bean, consistency);
        return executeConditions(indexName, index, null, null, key, bean,
                session, byKeyBean);
    }

    public <T, I> List<T> findByKeyAndIndex(Object key, I indexStart,
            I indexEnd, boolean inclusive, Class<T> bean, Session session,
            ConsistencyLevel consistency) {
        ClassInformation classInformation = ClassInformations.INSTACE.getClass(bean);
        FindByIndexQuery.checkFieldNull(bean, classInformation.getIndexFields());
        FieldInformation indexField = classInformation.getIndexFields().get(0);
        return findByKeyAndIndex(key, indexField.getName(), indexStart, indexEnd,
                inclusive, bean, session, consistency);
    }

    public <T, I> List<T> findByKeyAndIndex(Object key, String indexName,
            I indexStart, I indexEnd, boolean inclusive, Class<T> bean,
            Session session, ConsistencyLevel consistency) {
        QueryBean byKeyBean = createQueryBean(bean, consistency);
        return executeConditions(indexName, indexStart, indexEnd, inclusive,
                key, bean, session, byKeyBean);
    }

    private <T> List<T> executeConditions(String indexName, Object indexStart,
            Object indexEnd, Boolean inclusive, Object key, Class<T> bean,
            Session session, QueryBean byKeyBean) {

        ClassInformation classInformation = ClassInformations.INSTACE.getClass(bean);
        super.prepare(byKeyBean, classInformation);
        if (classInformation.isComplexKey()) {
            for (FieldInformation complexKey : classInformation
                    .getKeyInformation().getSubFields().getFields()) {

                byKeyBean.getSelect().where(QueryBuilder.eq(complexKey.getName(),
                        ReflectionUtil.INSTANCE.getMethod(key, complexKey.getField())));
            }
        } else {
            byKeyBean.getSelect().where(QueryBuilder.eq(
                    classInformation.getKeyInformation().getName(), key));
        }

        // Add index criteria
        byKeyBean.setSearchField(classInformation.findIndexByName(indexName));
        FindByIndexQuery.checkIndexProblem(bean, byKeyBean);

        // Add indexed key
        if (indexEnd != null && inclusive != null) {

            if (inclusive) {
                byKeyBean.getSelect().where(QueryBuilder.gte(
                        byKeyBean.getSearchField().getName(),
                        indexStart));
                byKeyBean.getSelect().where(QueryBuilder.lte(
                        byKeyBean.getSearchField().getName(),
                        indexEnd));
            } else {
                byKeyBean.getSelect().where(QueryBuilder.gt(
                        byKeyBean.getSearchField().getName(),
                        indexStart));
                byKeyBean.getSelect().where(QueryBuilder.lt(
                        byKeyBean.getSearchField().getName(),
                        indexEnd));
            }

        } else {
            byKeyBean.getSelect()
                    .where(QueryBuilder.eq(byKeyBean.getSearchField().getName(),
                            indexStart));
        }

        // Execute
        ResultSet resultSet = session.execute(byKeyBean.getSelect());
        return RecoveryObject.INTANCE.recoverObjet(bean, resultSet);
    }
}
