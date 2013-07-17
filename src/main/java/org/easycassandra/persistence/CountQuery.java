package org.easycassandra.persistence;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

class CountQuery {

	
	public Long count(Class<?> bean,Session session){
		StringBuilder countQuery=new StringBuilder();
		countQuery.append("SELECT COUNT(*) FROM ");
		countQuery.append(ColumnUtil.INTANCE.getSchema(bean));
		countQuery.append(ColumnUtil.INTANCE.getColumnFamilyName(bean)).append(";");
		ResultSet resultSet =session.execute(countQuery.toString());
		return resultSet.all().get(0).getLong(0);
	} 
}
