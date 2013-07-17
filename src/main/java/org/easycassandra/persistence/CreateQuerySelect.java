package org.easycassandra.persistence;

import java.lang.reflect.Field;

import org.easycassandra.util.ReflectionUtil;

/**
 * Class to crate a sql
 * @author otaviojava
 *
 */
class CreateQuerySelect {

	
	public StringBuilder createSQLQuery(Object bean){
		StringBuilder queryInsert=new StringBuilder();
		queryInsert.append("select ");
		for(Field field:ColumnUtil.INTANCE.listFields(bean.getClass())){
			if("serialVersionUID".equals(field.getName())){
				continue;
			}
			if(ReflectionUtil.getMethod(bean, field)!=null){
				
			}
		}
		return queryInsert;
	}
}
