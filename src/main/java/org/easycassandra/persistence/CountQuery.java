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

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
/**
 * Class to mount and execute a query to return the number of column in a family column
 * @author otaviojava
 *
 */
class CountQuery {

	/**
	 * return the number of row in a column family
	 * @param bean - column family
	 * @param session
	 * @return number of register in a column family
	 */
	public Long count(Class<?> bean,Session session){
		StringBuilder countQuery=new StringBuilder();
		countQuery.append("SELECT COUNT(*) FROM ");
		countQuery.append(ColumnUtil.INTANCE.getColumnFamilyName(bean)).append(";");
		ResultSet resultSet =session.execute(countQuery.toString());
		return resultSet.all().get(0).getLong(0);
	} 
}
