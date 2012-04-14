/*
 * Copyright 2012 Otávio Gonçalves de Santana (otaviojava)
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
import java.util.ArrayList;
import java.util.List;



/**
 * describes about the fields in family column 
 * @author otavio
 *
 */
class DescribeField {

	private String name;
	
	private String realCqlName;
	
	private Class<?> classField;

	private TypeField typeField;
	
	private List<DescribeField> children;


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Class<?> getClassField() {
		return classField;
	}

	public void setClassField(Class<?> classField) {
		this.classField = classField;
	
	}


	public TypeField getTypeField() {
		return typeField;
	}

	public void setTypeField(Field field) {
	this.classField = field.getType();
	if(ColumnUtil.isIdField(field)){
	this.typeField=TypeField.KEY;
	realCqlName="KEY";
		}else if(ColumnUtil.isSecundaryIndexField(field)){
			this.typeField=TypeField.INDEX;
			realCqlName=ColumnUtil.getColumnName(field);
		}
		else if(ColumnUtil.isNormalField(field)){
			this.typeField=TypeField.NORMAL;
			realCqlName=ColumnUtil.getColumnName(field);
		}
		else if(ColumnUtil.isEmbeddedField(field)){
			this.typeField=TypeField.SUBCLASS;
		}
		else if(ColumnUtil.isEnumField(field)){
			this.typeField=TypeField.ENUM;
			realCqlName=ColumnUtil.getEnumeratedName(field);
		}
	}	
	
	

	public String getRealCqlName() {
		if(children==null){
		return realCqlName;
		}
		StringBuilder realsNames=new StringBuilder();
		String aux="";
		for(DescribeField describeField:children){
			realsNames.append(aux+describeField.getRealCqlName());
			aux=" , ";
		}
		return realsNames.toString();
	}


	public void setRealCqlName(String realCqlName) {
		this.realCqlName = realCqlName;
	}


	

	public List<DescribeField> getChildren() {
		return children;
	}


	public void setChildren(List<DescribeField> children) {
		this.children = children;
	}

	/**
	 * add field for children
	 * @param describeField
	 */
	public void add(DescribeField describeField){
		if(children==null){
			children=new ArrayList<>();
		}
		children.add(describeField);
	}

 @Override
public String toString() {

	return name;
}
	/**
     * The possible type for the fields
     * @author otavio
     *
     */
    enum TypeField{NORMAL,KEY,INDEX,ENUM,SUBCLASS    }
    /**
     * verify if has children
     * @return
     */
	public boolean hasChildren() {
		
		return children!=null;
	}
}
