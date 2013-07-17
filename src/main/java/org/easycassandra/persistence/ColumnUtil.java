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

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.crypto.dsig.keyinfo.KeyValue;

import org.easycassandra.annotations.Index;

/**
 * Class Util for Column
 *
 * @author otavio
 *
 */
 enum ColumnUtil {
INTANCE;

	/**
	 * The integer class is used to enum how default
	 */
	public static final Class<?> DEFAULT_ENUM_CLASS=Integer.class;
    /**
     * Get The Column name from an Object
     *
     * @param object - Class of the object viewed
     * @return The name of Column name if there are not will be return null
     */
    public String getColumnFamilyName(Class<?> object) {

        Entity columnFamily = (Entity) object.getAnnotation(Entity.class);
        Table columnFamilyTable = (Table) object.getAnnotation(Table.class);
        if (columnFamily != null) {
            return columnFamily.name().equals("")   ? object.getSimpleName() : columnFamily.name();
        }else
        if(columnFamilyTable != null){
        	return columnFamilyTable.name().equals("")   ? object.getSimpleName() : columnFamilyTable.name();
        }
        return object.getSimpleName();
    }
    
    public  String getSchema(Class<?> class1) {
    	Table columnFamily = (Table) class1.getAnnotation(Table.class);
    	 if (columnFamily != null) {
             return columnFamily.schema().equals("") ? null : columnFamily.schema().concat(".");
         }
		return "";
	}

    /**
     * verifies that the name of the annotation is empty if you take the field
     * name
     *
     * @param field - field for viewd
     * @return The name inside annotations or the field's name
     */
    public  String getColumnName(Field field) {
    	if(field.getAnnotation(javax.persistence.Column.class)==null){
    		return field.getName();
    	}
        return field.getAnnotation(javax.persistence.Column.class).name().equals("")
                ? field.getName() : field.getAnnotation(
                javax.persistence.Column.class).name();
    }

    /**
     * verifies that the name of the annotation is empty if you take the field
     * name
     *
     * @param field
     * @return The name inside annotations or the field's name
     */
    public  String getEnumeratedName(Field field) {
        if (isNormalField(field)) {
            return getColumnName(field);
        }
        return field.getName();
    }

    /**
     * Return the Field with the KeyValue Annotations
     *
     * @see KeyValue
     * @param persistenceClass - Class of the object viewed
     * @return the Field if there are not will be return null
     */
    public  Field getKeyField(Class<?> persistenceClass) {
        return getField(persistenceClass, Id.class);
    }
    /**
     * Return the Field with the complex key Annotations
     *
     * @see KeyValue
     * @param persistenceClass - Class of the object viewed
     * @return the Field if there are not will be return null
     */
    public  Field getKeyComplexField(Class<?> persistenceClass) {
        return getField(persistenceClass, EmbeddedId.class);
    }
    
    /**
     * Return the Field with the IndexValue Annotations
     *
     * @see Index
     * @param persistenceClass - Class of the object viewed
     * @return the Field if there are not will be return null
     */
    public  Field getIndexField(Class<?> persistenceClass) {
        return getField(persistenceClass, Index.class);
    }

    /**
     * Get the Field of the Object from annotation if there are not return will
     * be null
     *
     * @param object - Class of the object viewed
     * @param annotation
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public  Field getField(Class object, Class annotation) {
        for (Field field : object.getDeclaredFields()) {
            if (field.getAnnotation(annotation) != null) {
                return field;
            } else if (field.getAnnotation(Embeddable.class) != null) {
                return getField(field.getType(), annotation);
            }
        }
        return null;
    }

   

    /**
     * list the fields in the class
     * @param class1 
     * @return list of the fields
     */
    public  List<Field> listFields(Class<?> class1) {
    	List<Field> fields =new ArrayList<Field>();
    	feedFieldList(class1, fields);
    	if(isMappedSuperclass(class1)){
    		feedFieldList(class1.getSuperclass(), fields);
    			
    	}
		return fields;
	}

    /**
     * feed the list com Fields
     * @param class1
     * @param fields
     */
	private  void feedFieldList(Class<?> class1, List<Field> fields) {
		for(Field field:class1.getDeclaredFields()){
    		fields.add(field);
    	}
	}


    /**
     * verify if this is key of the column
     *
     * @param field
     * @return
     */
    public  boolean isIdField(Field field) {
        return field.getAnnotation(Id.class) != null;
    }

    @GeneratedValue
    public  boolean isGeneratedValue(Field field) {
        return field.getAnnotation(GeneratedValue.class) != null;
    }

    /**
     * verify if this is secundary index of the column
     *
     * @param field
     * @return
     */
    public  boolean isSecundaryIndexField(Field field) {
        return field.getAnnotation(Index.class) != null;
    }

    /**
     * verify if this is a normal column
     *
     * @param field
     * @return
     */
    public  boolean isNormalField(Field field) {
        return field.getAnnotation(javax.persistence.Column.class) != null;
    }

    /**
     * verify if this is a enum column
     *
     * @param field
     * @return
     */
    public  boolean isEnumField(Field field) {
        return field.getAnnotation(Enumerated.class) != null;
    }

    /**
     * verify if this is a Embedded column
     *
     * @param field
     * @return
     */
    public  boolean isEmbeddedField(Field field) {
        return field.getAnnotation(Embedded.class) != null;
    }
    /**
     * verify if this is a Embedded id column
     *
     * @param field
     * @return
     */
    public  boolean isEmbeddedIdField(Field field) {
        return field.getAnnotation(EmbeddedId.class) != null;
    }


    /**
     * verify if this is a Version column
     *
     * @param field
     * @return
     */
    public  boolean isVersionField(Field field) {
        return field.getAnnotation(Version.class) != null;
    }

    /**
     * verify is exist father to persist
     * @param class1
     * @return
     */
	public  boolean isMappedSuperclass(Class<?> class1) {
		return class1.getSuperclass().getAnnotation(MappedSuperclass.class)!=null;
	}


}