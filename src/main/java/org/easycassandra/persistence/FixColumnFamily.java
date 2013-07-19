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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.easycassandra.FieldJavaNotEquivalentCQLException;

import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.InvalidQueryException;
/**
 * Class to fix a column family
 * @author otaviojava
 *
 */
class FixColumnFamily {

	
	/**
	 * verify if exist column family and try to create
	 * @param session - bridge to cassandra data base
	 * @param familyColumn - name of family column
	 * @param class1 - bean
	 * @return - if get it or not
	 */
	public boolean verifyColumnFamily(Session session,String familyColumn,Class<?> class1){
			try{
				ResultSet resultSet=session.execute("SELECT * FROM "+familyColumn+" LIMIT 1");
				verifyRowType(resultSet,class1,session);
				findIndex(class1, session);
				return true;
			}catch(InvalidQueryException exception){
				
				if(exception.getCause().getMessage().contains("unconfigured columnfamily ")){
					Logger.getLogger(FixColumnFamily.class.getName()).info("Column family doesn't exist, try to create");
					createColumnFamily(familyColumn, class1,session);
					findIndex(class1, session);
					return true;
				}
			}
		
			return false;
	}

	/**
	 * Column family exists verify
	 * @param resultSet
	 */
	private void verifyRowType(ResultSet resultSet,Class<?> class1,Session session) {
		Map<String, String> mapNameType=new HashMap<String, String>();
		for(Definition column:resultSet.getColumnDefinitions()){
			mapNameType.put(column.getName(), column.getType().getName().name());
		}
		verifyRow(class1, session, mapNameType);
	}

	/**
	 * verify relationship beteween Java and CQL type
	 * @param class1
	 * @param session
	 * @param mapNameType
	 */
	private void verifyRow(Class<?> class1, Session session,Map<String, String> mapNameType) {
		
		for(Field field:ColumnUtil.INTANCE.listFields(class1)){
			if("serialVersionUID".equals(field.getName())){
				continue;
			}
			else if(ColumnUtil.INTANCE.isEmbeddedField(field)||ColumnUtil.INTANCE.isEmbeddedIdField(field)){
				continue;
			}
			else if(ColumnUtil.INTANCE.isEmbeddedField(field)){
				verifyRow(field.getType(), session, mapNameType);
				continue;
			}
			
			String cqlType=mapNameType.get(ColumnUtil.INTANCE.getColumnName(field).toLowerCase());
			if(cqlType == null){
				executeAlterTableAdd(class1, session, field);
				continue;
			}
			List<String> cqlTypes=null;
			
			if(ColumnUtil.INTANCE.isEnumField(field)){
				cqlTypes=RelationShipJavaCassandra.INSTANCE.getCQLType(ColumnUtil.DEFAULT_ENUM_CLASS.getName());
			}else{
				cqlTypes=RelationShipJavaCassandra.INSTANCE.getCQLType(field.getType().getName());
			}
			
				if(!cqlTypes.contains(cqlType.toLowerCase())){
				createMessageErro(class1, field,cqlType);
			}
			
		}
	}

	/**
	 * call the command to alter table adding a field 
	 * @param class1 - bean within column family
	 * @param session - bridge to cassandra
	 * @param field - field to add in column family
	 */
	private void executeAlterTableAdd(Class<?> class1, Session session,Field field) {
		StringBuilder cqlAlterTable=new StringBuilder();
		cqlAlterTable.append("ALTER TABLE ").append(ColumnUtil.INTANCE.getColumnFamilyName(class1));
		cqlAlterTable.append(" ADD ").append(ColumnUtil.INTANCE.getColumnName(field)).append(" ");
		if(ColumnUtil.INTANCE.isEnumField(field)){
			cqlAlterTable.append(RelationShipJavaCassandra.INSTANCE.getPreferenceCQLType(ColumnUtil.DEFAULT_ENUM_CLASS.getName()));
		}else{
			cqlAlterTable.append(RelationShipJavaCassandra.INSTANCE.getPreferenceCQLType(field.getType().getName()));
		}
		cqlAlterTable.append(";");
		session.execute(cqlAlterTable.toString());
	}

	/**
	 * Field Java isn't equivalents with CQL type create error mensage
	 * @param class1
	 * @param field
	 * @param cqlTypes
	 */
	private void createMessageErro(Class<?> class1, Field field,String cqlType) {
		StringBuilder erroMensage=new StringBuilder();
		erroMensage.append("In the objetc ").append(class1.getName());
		erroMensage.append(" the field ").append(field.getName());
		erroMensage.append(" of the type ").append(field.getType().getName());
		erroMensage.append(" isn't equivalent with CQL type ").append(cqlType);
		erroMensage.append(" was expected: ").append(RelationShipJavaCassandra.INSTANCE.getJavaValue(cqlType.toLowerCase()));
		throw new FieldJavaNotEquivalentCQLException(erroMensage.toString());
	}

	/**
	 * Column family doen'snt exist create with this method
	 * @param familyColumn - name of column family
	 * @param class1 - bean
	 * @param session bridge of cassandra data base
	 */
	private void createColumnFamily(String familyColumn, Class<?> class1,Session session) {
		StringBuilder cqlCreateTable=new StringBuilder();
		cqlCreateTable.append("create table ");
		
		cqlCreateTable.append(familyColumn).append("( ");
		RelationShipJavaCassandra javaCassandra=RelationShipJavaCassandra.INSTANCE;
		boolean isComplexID=createQueryCreateTable(class1, cqlCreateTable, javaCassandra);
		if(isComplexID){
			Field keyField=ColumnUtil.INTANCE.getKeyComplexField(class1);
			cqlCreateTable.append(" PRIMARY KEY (");
			boolean firstTime=true;
			for(Field subKey:keyField.getType().getDeclaredFields()){
				if(firstTime){
					cqlCreateTable.append(subKey.getName());
					firstTime=false;
				}else{
					cqlCreateTable.append(",").append(subKey.getName());
				}
			}
			cqlCreateTable.append(") );");
		}else{
			Field keyField=ColumnUtil.INTANCE.getKeyField(class1);
			cqlCreateTable.append(" PRIMARY KEY (").append(keyField.getName()).append(") );");	
		}
		
		session.execute(cqlCreateTable.toString());
	}

	/**
	 * create a query to create table and return if exist a complex id or not
	 * @param class1
	 * @param cqlCreateTable
	 * @param javaCassandra
	 * @return 
	 */
	private boolean createQueryCreateTable(Class<?> class1,StringBuilder cqlCreateTable,RelationShipJavaCassandra javaCassandra) {
		boolean isComplexID=false;
		for(Field field:ColumnUtil.INTANCE.listFields(class1)){
			if("serialVersionUID".equals(field.getName())){
				continue;
			}
			if(ColumnUtil.INTANCE.isEnumField(field)){
				
				String columnName=ColumnUtil.INTANCE.getColumnName(field);
				cqlCreateTable.append(columnName).append(" ").append(javaCassandra.getPreferenceCQLType(ColumnUtil.DEFAULT_ENUM_CLASS.getName())).append(", ");
				continue;
			}
			if(ColumnUtil.INTANCE.isEmbeddedField(field)||ColumnUtil.INTANCE.isEmbeddedIdField(field)){
				
				isComplexID=createQueryCreateTable(field.getType(), cqlCreateTable, javaCassandra);
				if(ColumnUtil.INTANCE.isEmbeddedIdField(field)){
					isComplexID=true;
				}
				continue;
			}
			
			String columnName=ColumnUtil.INTANCE.getColumnName(field);
			cqlCreateTable.append(columnName).append(" ").append(javaCassandra.getPreferenceCQLType(field.getType().getName())).append(", ");
			
		}
		return isComplexID;
	}
	/**
	 * Find if exists 
	 */
	private void findIndex(Class<?> familyColumn,Session session){
		StringBuilder createIndexQuery = new StringBuilder();
		Field index=ColumnUtil.INTANCE.getIndexField(familyColumn);
		if(index ==null){return;}
		createIndexQuery.append("CREATE INDEX ");
		createIndexQuery.append(ColumnUtil.INTANCE.getColumnName(index)).append("INDEX ON ");
		createIndexQuery.append(ColumnUtil.INTANCE.getColumnFamilyName(familyColumn));
		createIndexQuery.append(" (").append(ColumnUtil.INTANCE.getColumnName(index)).append(");");
		try{
		session.execute(createIndexQuery.toString());
		}catch(InvalidQueryException exception){
			Logger.getLogger(FixColumnFamily.class.getName()).info("Index already exists");
		}
	}
}
