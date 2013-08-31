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

import org.easycassandra.persistence.cassandra.ColumnUtil.KeySpaceInformation;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

/**
 * Mount and run the query to returns all data from column family
 * 
 * @author otaviojava
 * 
 */
public class FindAllQuery {

	
	private String keySpace;
	
	public FindAllQuery(String keySpace){
		this.keySpace = keySpace;
	}
	
    public <T> List<T> listAll(Class<T> bean, Session session) {
    	
    	
        QueryBean byKeyBean = createQueryBean(bean);
    	return RecoveryObject.INTANCE.recoverObjet(bean, session.execute(byKeyBean.select));
    }

	protected <T> QueryBean createQueryBean(Class<T> bean) {
		QueryBean byKeyBean = prepare(new QueryBean(), bean);
        KeySpaceInformation key=ColumnUtil.INTANCE.getKeySpace(keySpace, bean);
        byKeyBean.select=QueryBuilder.select(byKeyBean.getArray()).from(key.getKeySpace(), key.getColumnFamily());
		return byKeyBean;
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
            byKeyBean.columns.add(ColumnUtil.INTANCE.getColumnName(field));
            

        }
        return byKeyBean;
    }

    protected class QueryBean {
        protected Field key;
        protected List<String> columns = new LinkedList<String>();
        protected Select select;
        
        String[] getArray(){
        	return columns.toArray(new String[0]);
        }
    }
}
