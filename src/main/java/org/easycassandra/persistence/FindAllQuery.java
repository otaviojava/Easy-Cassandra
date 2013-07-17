package org.easycassandra.persistence;

import java.lang.reflect.Field;
import java.util.List;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class FindAllQuery {

	public <T> List<T> listAll(Class<T> bean,Session session){
		QueryBean byKeyBean = new QueryBean();
		
		byKeyBean.stringBuilder.append("select ");
		byKeyBean=prepare(byKeyBean, bean);
		byKeyBean.stringBuilder.deleteCharAt(byKeyBean.stringBuilder.length()-1); 
		byKeyBean.stringBuilder.append(" from ").append(ColumnUtil.INTANCE.getSchema(bean));
		byKeyBean.stringBuilder.append(ColumnUtil.INTANCE.getColumnFamilyName(bean));
		
		return executeConditions(bean, session, byKeyBean);
	}

	private <T> List<T> executeConditions(Class<T> bean, Session session,	QueryBean queryBean) {
		queryBean.stringBuilder.append(";");
		ResultSet resultSet=session.execute(queryBean.stringBuilder.toString());
		return RecoveryObject.INTANCE.recoverObjet(bean, resultSet);
	}

	
	protected QueryBean prepare(QueryBean byKeyBean,Class<?> class1){
		List<Field> fields=ColumnUtil.INTANCE.listFields(class1);
		
		for(Field field:fields){
			if("serialVersionUID".equals(field.getName())){
				continue;
			}
			if(ColumnUtil.INTANCE.isEmbeddedField(field)||ColumnUtil.INTANCE.isEmbeddedIdField(field)){
				if(ColumnUtil.INTANCE.isEmbeddedIdField(field)){
					byKeyBean.key=field;
				}
				byKeyBean=prepare(byKeyBean, field.getType());
				continue;
			}
			
			else if(ColumnUtil.INTANCE.isIdField(field)){
				byKeyBean.key=field;
			}
			String columnName=ColumnUtil.INTANCE.getColumnName(field);
			
			byKeyBean.stringBuilder.append(columnName).append(" ,");
			
			
		}
		return byKeyBean;
	}
	
	class QueryBean{
		Field key;
		StringBuilder stringBuilder=new StringBuilder();
	}
}
