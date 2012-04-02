package org.easycassandra.persistence;

import java.lang.reflect.Field;



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
	if(ColumnUtil.isKeyField(field)){
	this.typeField=TypeField.KEY;
	realCqlName="KEY";
		}else if(ColumnUtil.isSecundaryIndexField(field)){
			this.typeField=TypeField.INDEX;
			realCqlName=ColumnUtil.getColumnName(field);
		}
		else if(ColumnUtil.isNormalField(field)){
			this.typeField=TypeField.NORMAL;
			realCqlName=ColumnUtil.getColumnName(field);
		}else if(ColumnUtil.isEnumField(field)){
			this.typeField=TypeField.ENUM;
			realCqlName=ColumnUtil.getEnumeratedName(field);
		}
	}	
	
	

	public String getRealCqlName() {
		return realCqlName;
	}


	public void setRealCqlName(String realCqlName) {
		this.realCqlName = realCqlName;
	}


	

	/**
     * The possible type for the fields
     * @author otavio
     *
     */
    enum TypeField{NORMAL,KEY,INDEX,ENUM    }
}
