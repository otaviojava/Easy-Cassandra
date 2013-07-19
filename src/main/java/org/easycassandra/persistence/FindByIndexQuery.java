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

import java.util.List;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

/**
 * Class to execute a index
 * @author otaviojava
 *
 */
 class FindByIndexQuery extends FindByKeyQuery{

	 
	 public <T> List<T> findByIndex(Object key, Class<T> bean,Session session){
			QueryBean byKeyBean = new QueryBean();
			
			byKeyBean.stringBuilder.append("select ");
			byKeyBean=prepare(byKeyBean, bean);
			byKeyBean.stringBuilder.deleteCharAt(byKeyBean.stringBuilder.length()-1); 
			byKeyBean.stringBuilder.append(" from ");
			byKeyBean.stringBuilder.append(ColumnUtil.INTANCE.getColumnFamilyName(bean));
			return executeConditions(key, bean, session, byKeyBean);
		}
	 
	 private <T> List<T> executeConditions(Object key, Class<T> bean, Session session,	QueryBean byKeyBean) {
		 	byKeyBean.key=ColumnUtil.INTANCE.getIndexField(bean);
		 	if(byKeyBean.key == null){
		 		StringBuilder erro = new StringBuilder();
		 		erro.append("Some field in a class ").append(bean.getName());
		 		erro.append(" should be a annotation: @org.easycassandra.annotations.Index ");
		 		throw new NullPointerException(erro.toString());
		 	}
			ResultSet resultSet = executeQuery(key, bean,session, byKeyBean);
			return RecoveryObject.INTANCE.recoverObjet(bean, resultSet);
		}
}
