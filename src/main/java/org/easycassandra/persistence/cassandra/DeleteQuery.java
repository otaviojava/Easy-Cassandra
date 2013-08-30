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

import org.easycassandra.persistence.cassandra.ColumnUtil.KeySpaceInformation;
import org.easycassandra.util.ReflectionUtil;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;

/**
 * Class to create a query to delete beans
 * 
 * @author otaviojava
 * 
 */
class DeleteQuery{

	private String keySpace;
	
	public DeleteQuery(String keySpace){
	  this.keySpace = keySpace;	
	}
    public boolean deleteByKey(Object bean, Session session) {
    	
        Field key = ColumnUtil.INTANCE.getKeyField(bean.getClass());
        if (key == null) {
            key = ColumnUtil.INTANCE.getKeyComplexField(bean.getClass());
        }
     
        return deleteByKey(ReflectionUtil.INSTANCE.getMethod(bean, key),bean.getClass(), session);
    }

    public boolean deleteByKey(Object key, Class<?> bean, Session session) {
        Delete delete=runDelete(key, bean);
        session.execute(delete);
        return true;
    }
    
    
	private Delete runDelete(Object key, Class<?> bean) {
		if (key == null) {
            throw new NullPointerException("The parameter key to column family should be passed");
        }
        
        KeySpaceInformation keyInformation=ColumnUtil.INTANCE.getKeySpace(keySpace, bean);
    	Delete delete=QueryBuilder.delete().all().from(keyInformation.getKeySpace(), keyInformation.getColumnFamily());

        Field keyField = ColumnUtil.INTANCE.getKeyField(bean);
        if (keyField != null) {
        	
           delete.where(QueryBuilder.eq(ColumnUtil.INTANCE.getColumnName(keyField), key));  
        } else {
        	runComplexKey(bean, delete, key);
        }
        
        return delete;
	}
    
    
	private void runComplexKey(Class<?> bean, Delete delete, Object key) {
		
		for(Field subKey:ColumnUtil.INTANCE.listFields(key.getClass())){
		  delete.where(QueryBuilder.eq(ColumnUtil.INTANCE.getColumnName(subKey), ReflectionUtil.INSTANCE.getMethod(key, subKey)));
		}
	}

        
    
}
