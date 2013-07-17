package org.easycassandra.persistence;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EmbeddedId;

import org.easycassandra.KeyProblemsException;
import org.easycassandra.util.ReflectionUtil;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

 class InsertQuery {

public boolean prepare(Object bean,Session session){
	//FIXME verify is key is null
	isKeyNull(bean);
	InsertBean insertBean=new InsertBean();
	 
	createBeginInsertQuery(bean, insertBean);
	insertBean=createQueryInsert(bean,insertBean);
	createEndInsertQuery(insertBean);
	
	PreparedStatement statement=session.prepare(insertBean.query.toString());
	BoundStatement boundStatement = new BoundStatement(statement);
	session.execute(boundStatement.bind(insertBean.objets.toArray()));
	return true;
}


private void createEndInsertQuery(InsertBean insertBean) {
	insertBean.query.append(") VALUES (");
	for(int index=1;index <= insertBean.count;index++){
		if(index >1){
			insertBean.query.append(",");
		}
		insertBean.query.append(" ?");
	}
	insertBean.query.append(" );");
}

private void createBeginInsertQuery(Object bean, InsertBean insertBean) {
	insertBean.query.append("insert into ").append(ColumnUtil.INTANCE.getSchema(bean.getClass()));
	insertBean.query.append(ColumnUtil.INTANCE.getColumnFamilyName(bean.getClass()));
	insertBean.query.append("(");
}
	 
 public InsertBean createQueryInsert(Object bean,InsertBean insertBean){
	
		for(Field field:ColumnUtil.INTANCE.listFields(bean.getClass())){
			if("serialVersionUID".equals(field.getName())){
				continue;
			}
			else if(ColumnUtil.INTANCE.isEmbeddedField(field)||ColumnUtil.INTANCE.isEmbeddedIdField(field)){
				if(ReflectionUtil.getMethod(bean, field)!=null){
					insertBean=createQueryInsert(ReflectionUtil.getMethod(bean, field),insertBean);
				}
				continue;
			}
			
			else if(ReflectionUtil.getMethod(bean, field) != null){
				if(insertBean.count>0){
					insertBean.query.append(",");
				}
			insertBean.query.append(ColumnUtil.INTANCE.getColumnName(field));
			insertBean.count++;
			if(ColumnUtil.INTANCE.isEnumField(field)){
				Enum<?> enumS= (Enum<?>) ReflectionUtil.getMethod(bean, field);
				insertBean.objets.add(enumS.ordinal());
			}else{
				insertBean.objets.add(ReflectionUtil.getMethod(bean, field));
			}
			
			}
		}
		return insertBean;
 }

 /**
  * Verify if key is nut and make a exception
  * @param bean
  */
 private void isKeyNull(Object bean) {
	 Field key=ColumnUtil.INTANCE.getKeyField(bean.getClass());
	 if(key == null){
		 key = ColumnUtil.INTANCE.getField(bean.getClass(), EmbeddedId.class);
		 isKeyNull(bean,key.getType().getDeclaredFields());
	 }else{
		 isKeyNull(bean,key);
	 }
		
	}
 
 private void isKeyNull(Object bean,Field...fields){
	 for(Field field:fields){
		 if(ReflectionUtil.getMethod(bean, field) == null){
			 throw new KeyProblemsException("Key is mandatory to insert a new column family");
		 }
	 }
 }
 class InsertBean{
	 StringBuilder query=new StringBuilder();
	 int count;
	 List<Object> objets=new LinkedList<Object>();
 }
}
