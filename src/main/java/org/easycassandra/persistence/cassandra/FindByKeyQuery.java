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

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.easycassandra.KeyProblemsException;
import org.easycassandra.util.ReflectionUtil;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

/**
 * Class to execute query for find by id
 * 
 * @author otaviojava
 * 
 */
class FindByKeyQuery extends FindAllQuery {

    public <T> T findByKey(Object key, Class<T> bean, Session session) {
        QueryBean byKeyBean = new QueryBean();

        byKeyBean.stringBuilder.append("select ");
        byKeyBean = prepare(byKeyBean, bean);
        byKeyBean.stringBuilder.deleteCharAt(byKeyBean.stringBuilder.length() - 1);
        byKeyBean.stringBuilder.append(" from ");
        byKeyBean.stringBuilder.append(ColumnUtil.INTANCE.getColumnFamilyName(bean));
        return executeConditions(key, bean, session, byKeyBean);
    }

    private <T> T executeConditions(Object key, Class<T> bean, Session session,QueryBean byKeyBean) {
        ResultSet resultSet = executeQuery(key, bean, session, byKeyBean);
        List<T> list = RecoveryObject.INTANCE.recoverObjet(bean, resultSet);
        if (!list.isEmpty()) {
            return list.get(0);
        }

        return null;
    }

    /**
     * execute the query and returns the result set
     * 
     * @param key
     *            - value of key
     * @param bean
     *            - bean represents column family
     * @param session
     * @param byKeyBean
     * @return
     */
    protected ResultSet executeQuery(Object key, Class<?> bean,Session session, QueryBean byKeyBean) {

        byKeyBean.stringBuilder.append(" where ");

        if (!key.getClass().equals(byKeyBean.key.getType())) {
            StringBuilder erro = new StringBuilder();
            erro.append("The parameter key should be the same type of the key of column family, the type passed was ");
            erro.append(key.getClass().getName()).append(" and was expected ").append(byKeyBean.key.getType().getName());
            throw new KeyProblemsException(erro.toString());
        }
        if (ColumnUtil.INTANCE.isIdField(byKeyBean.key)) {
            return executeSingleKey(key, session, byKeyBean);
        } else if (ColumnUtil.INTANCE.isEmbeddedIdField(byKeyBean.key)) {
            
            return executeComplexKey(key, session, byKeyBean);
        } else if (ColumnUtil.INTANCE.isIndexField(byKeyBean.key)) {
            return executeSingleKey(key, session, byKeyBean);
        }

        return null;
    }

    /**
     * query with just one key in column family
     * 
     * @param key
     * @param session
     * @param byKeyBean
     * @return
     */
    protected ResultSet executeSingleKey(Object key, Session session, QueryBean byKeyBean) {
        
        byKeyBean.stringBuilder.append(ColumnUtil.INTANCE.getColumnName(byKeyBean.key));
        byKeyBean.stringBuilder.append("= ? ;");
        PreparedStatement statement = session.prepare(byKeyBean.stringBuilder.toString());
        BoundStatement boundStatement = new BoundStatement(statement);
        return session.execute(boundStatement.bind(new Object[] { key }));

    }

    /**
     * query with complex query in column family
     * 
     * @param key
     * @param session
     * @param byKeyBean
     * @return
     */
    protected ResultSet executeComplexKey(Object key, Session session, QueryBean byKeyBean) {
        List<Object> objects=new LinkedList<Object>();
        int count=0;
        for(Field complexKey:ColumnUtil.INTANCE.listFields(key.getClass())){
            if(count++ >0){
                byKeyBean.stringBuilder.append(" AND ");    
            }
            
            byKeyBean.stringBuilder.append(ColumnUtil.INTANCE.getColumnName(complexKey));
            byKeyBean.stringBuilder.append("= ? ");
            objects.add(ReflectionUtil.getMethod(key, complexKey));
        }
        byKeyBean.stringBuilder.append(";");
        PreparedStatement statement = session.prepare(byKeyBean.stringBuilder.toString());
        BoundStatement boundStatement = new BoundStatement(statement);
        return session.execute(boundStatement.bind(objects.toArray()));

    }
}
