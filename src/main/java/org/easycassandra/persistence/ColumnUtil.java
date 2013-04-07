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
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.crypto.dsig.keyinfo.KeyValue;

import org.apache.cassandra.thrift.Column;
import org.easycassandra.EasyCassandraException;
import org.easycassandra.annotations.Index;
import org.easycassandra.annotations.write.EnumWrite;
import org.easycassandra.util.EncodingUtil;
import org.easycassandra.util.ReflectionUtil;

/**
 * Class Util for Column
 *
 * @author otavio
 *
 */
final class ColumnUtil {

    /**
     * Get The Column name from an Object
     *
     * @param object - Class of the object viewed
     * @return The name of Column name if there are not will be return null
     */
    public static String getColumnFamilyName(Class<?> object) {

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
    
    public static String getSchema(Class<?> class1) {
    	Table columnFamily = (Table) class1.getAnnotation(Table.class);
    	 if (columnFamily != null) {
             return columnFamily.schema().equals("") ? null : columnFamily.schema();
         }
		return null;
	}

    /**
     * verifies that the name of the annotation is empty if you take the field
     * name
     *
     * @param field - field for viewd
     * @return The name inside annotations or the field's name
     */
    public static String getColumnName(Field field) {
        return field.getAnnotation(
                javax.persistence.Column.class).name().equals("")
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
    public static String getEnumeratedName(Field field) {
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
    public static Field getKeyField(Class<?> persistenceClass) {
        return getField(persistenceClass, Id.class);
    }

    /**
     * Return the Field with the IndexValue Annotations
     *
     * @see Index
     * @param persistenceClass - Class of the object viewed
     * @return the Field if there are not will be return null
     */
    public static Field getIndexField(Class<?> persistenceClass) {
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
    public static Field getField(Class object, Class annotation) {
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
     * create columns based on annotations in Easy-Cassandra
     *
     * @param object - the object viewed
     * @return - List of the Column
     */
    public static List<Column> getColumns(Object object) {
        Long timeStamp = System.currentTimeMillis();
        List<Column> columns = new ArrayList<Column>();
        List<Field> fields = listFields(object.getClass());
      
        for (Field field : fields) {
            if (field.getName().equals("serialVersionUID")
                    || field.getAnnotation(Id.class) != null
                    || ReflectionUtil.getMethod(object, field) == null) {
                continue;
            }
            if (ColumnUtil.isNormalField(field)) {
                Column column = makeColumn(timeStamp,
                        ColumnUtil.getColumnName(field), object, field);
                addColumn(columns, column);
            } else if (ColumnUtil.isEmbeddedField(field)) {
                if (ReflectionUtil.getMethod(object, field) != null) {
                    columns.addAll(getColumns(
                            ReflectionUtil.getMethod(object, field)));
                }
            } else if (ColumnUtil.isEnumField(field)) {
                Column column = doEnumColumn(object, timeStamp, field);
                addColumn(columns, column);
            }
        }
        return columns;
    }

    /**
     * list the fields in the class
     * @param class1 
     * @return list of the fields
     */
    public static List<Field> listFields(Class<?> class1) {
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
	private static void feedFieldList(Class<?> class1, List<Field> fields) {
		for(Field field:class1.getDeclaredFields()){
    		fields.add(field);
    	}
	}

	/**
     * Do enum column
     *
     * @param object - the value
     * @param timeStamp - the time stamp
     * @param field - the field
     * @return - the column to enum value
     */
    private static Column doEnumColumn(Object object, Long timeStamp, Field field) {
        Column column = new Column();
        column.setTimestamp(timeStamp);
        column.setName(EncodingUtil.stringToByte(
                ColumnUtil.getEnumeratedName(field)));
        ByteBuffer byteBuffer = new EnumWrite().getBytebyObject(
                ReflectionUtil.getMethod(object, field));
        column.setValue(byteBuffer);
        return column;
    }

    /**
     * Verify is exist column and then add in the list
     *
     * @param columns - list of column
     * @param column - value
     */
    private static void addColumn(List<Column> columns, Column column) {
        if (column != null) {
            columns.add(column);
        }
    }

    /**
     * Create a column for persist in Cassandra
     *
     * @param timeStamp - time in the Column
     * @param coluna - name in the Column
     * @param object - the object viewed
     * @param field - the Field viewed
     * @return the column for the Cassandra
     */
    public static Column makeColumn(long timeStamp, String coluna,Object object, Field field) {

        Object subObject = ReflectionUtil.getMethod(object, field);
        if (subObject != null) {
            Column column = new Column();
            column.setTimestamp(timeStamp);
            column.setName(EncodingUtil.stringToByte(coluna));
            column.setValue(BasePersistence.getWriteManager().convert(subObject));
            return column;
        } else {
            return null;
        }
    }

    /**
     * The method for set the new KeyValue in auto counting mode
     *
     * @param object - the object
     * @param keyField - the key
     * @param columnFamily - the name of column
     * @param superColumnRef - reference of super column
     * @param keyStore - the name of key Store
     */
    public static void setAutoCoutingKeyValue(Object object, Field keyField,
            String columnFamily, AtomicReference<ColumnFamilyIds> superColumnRef,
            String keyStore) {
        if (!contains(keyField.getType())) {
            throw new EasyCassandraException(" There are not supported "
                    + "auto counting  for this class, see: java.lang.String,"
                    + " java.lang.Long, java.lang.Integer, java.lang.Byte,"
                    + " java.lang.Short, java.math.BigInteger ");
        }
        Object id = superColumnRef.get().getId(columnFamily, keyStore);
        if (String.class.equals(keyField.getType())) {
            id = id.toString();
        } else if (!BigInteger.class.equals(keyField.getType())) {
            id = ReflectionUtil.valueOf(keyField.getType(), id.toString());
        }
        ReflectionUtil.setMethod(object, keyField, id);
    }

    /**
     * Verify is the key Class are supported
     *
     * @param clazz - the class for verify
     * @return if Easy-Cassandra has supported or not
     */
    private static boolean contains(Class<?> clazz) {
        Class<?>[] classes = {String.class, Long.class, Integer.class,
            Byte.class, Short.class, BigInteger.class};
        for (Class<?> claZZ : classes) {
            if (clazz.equals(claZZ)) {
                return true;
            }
        }
        return false;
    }

    /**
     * verify if this is key of the column
     *
     * @param field
     * @return
     */
    public static boolean isIdField(Field field) {
        return field.getAnnotation(Id.class) != null;
    }

    @GeneratedValue
    public static boolean isGeneratedValue(Field field) {
        return field.getAnnotation(GeneratedValue.class) != null;
    }

    /**
     * verify if this is secundary index of the column
     *
     * @param field
     * @return
     */
    public static boolean isSecundaryIndexField(Field field) {
        return field.getAnnotation(Index.class) != null;
    }

    /**
     * verify if this is a normal column
     *
     * @param field
     * @return
     */
    public static boolean isNormalField(Field field) {
        return field.getAnnotation(javax.persistence.Column.class) != null;
    }

    /**
     * verify if this is a enum column
     *
     * @param field
     * @return
     */
    public static boolean isEnumField(Field field) {
        return field.getAnnotation(Enumerated.class) != null;
    }

    /**
     * verify if this is a Embedded column
     *
     * @param field
     * @return
     */
    public static boolean isEmbeddedField(Field field) {
        return field.getAnnotation(Embedded.class) != null;
    }

    /**
     * verify if this is a Version column
     *
     * @param field
     * @return
     */
    public static boolean isVersionField(Field field) {
        return field.getAnnotation(Version.class) != null;
    }

    private ColumnUtil() {
    }

    /**
     * verify is exist father to persist
     * @param class1
     * @return
     */
	public static boolean isMappedSuperclass(Class<?> class1) {
		return class1.getSuperclass().getAnnotation(MappedSuperclass.class)!=null;
	}

	
}
