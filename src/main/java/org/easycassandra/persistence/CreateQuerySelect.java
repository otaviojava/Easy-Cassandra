/*
 * Copyright 2013 Otávio Gonçalves de Santana (otaviojava)
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
