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

import org.easycassandra.util.ReflectionUtil;

import com.datastax.driver.core.Session;

/**
 * Class to create a query to delete beans
 * 
 * @author otaviojava
 * 
 */
class DeleteQuery extends FindByKeyQuery {

    public boolean deleteByKey(Object bean, Session session) {
        Field key = ColumnUtil.INTANCE.getKeyField(bean.getClass());
        if (key == null) {
            key = ColumnUtil.INTANCE.getKeyComplexField(bean.getClass());
        }
        return deleteByKey(ReflectionUtil.getMethod(bean, key),bean.getClass(), session);
    }

    public boolean deleteByKey(Object key, Class<?> bean, Session session) {
        QueryBean queryBean=new QueryBean();
        if (key == null) {
            throw new NullPointerException("The parameter key to column family should be passed");
        }

        queryBean.stringBuilder.append("DELETE FROM ");
        queryBean.stringBuilder.append(ColumnUtil.INTANCE.getColumnFamilyName(bean)).append(" WHERE ");

        Field keyField = ColumnUtil.INTANCE.getKeyField(bean);
        if (keyField != null) {
            
            queryBean.key=keyField;
            executeSingleKey(key, session, queryBean);
        } else {
            queryBean.key=ColumnUtil.INTANCE.getKeyComplexField(bean);
            executeComplexKey(key, session, queryBean);
            
        }

        return true;
    }

        
    
}
