package org.easycassandra.persistence;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * describes about the family column object with their fields
 * @author otavio
 *
 */
 class DescribeFamilyObject {
	/**
	 * Name of Column family
	 */
	private String columnFamilyName;
	
	
	private  Class<?> classFamily; 
	
	private  Map<String,DescribeField> fields;
	/**
	 * method constructor for the DescribeFamilyObject
	 * @param classColumnFamily 
	 */
	DescribeFamilyObject(Class<?> classColumnFamily){
		columnFamilyName=ColumnUtil.getColumnFamilyName(classColumnFamily);
		classFamily=classColumnFamily;
		for(Field field:classColumnFamily.getDeclaredFields()){
			if("serialVersionUID".equals(field.getName())){
				continue;
			}
			DescribeField describeField=new DescribeField();
			describeField.setTypeField(field);
			describeField.setName(field.getName());
			fields.put(describeField.getName().toLowerCase(), describeField);
		}
		
	}
	/**
	 * key-value for DescribeField
	 * @param value
	 * @return
	 */
	public DescribeField getField(String value){
		
		return fields.get(value.toLowerCase());
	}
	{fields=new HashMap<>();}
	
	public String getColumnFamilyName() {
		return columnFamilyName;
	}

	public void setColumnFamilyName(String columnFamilyName) {
		this.columnFamilyName = columnFamilyName;
	}

	public Class<?> getClassFamily() {
		return classFamily;
	}

	public void setClassFamily(Class<?> classFamily) {
		this.classFamily = classFamily;
	}

	public Map<String,DescribeField> getFields() {
		return fields;
	}

	public void setFields(Map<String,DescribeField> fields) {
		this.fields = fields;
	}
	
	
}
