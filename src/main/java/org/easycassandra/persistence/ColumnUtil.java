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
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.cassandra.thrift.Column;
import org.easycassandra.annotations.ColumnFamilyValue;
import org.easycassandra.annotations.ColumnValue;
import org.easycassandra.annotations.EmbeddedValue;
import org.easycassandra.annotations.EnumeratedValue;
import org.easycassandra.annotations.IndexValue;
import org.easycassandra.annotations.KeyValue;
import org.easycassandra.annotations.write.EnumWrite;
import org.easycassandra.util.EncodingUtil;
import org.easycassandra.util.ReflectionUtil;


/**
 * Class Util for Column
 * @author otavio
 *
 */
 final class ColumnUtil {

	 /**
	  * Get The Column name from an Object
	  * @param object - Class of the object viewed
	  * @return The name of Column name if there are not will be return null
	  */
	     
	 	public static String getColumnFamilyName(Class<?> object) {

	         ColumnFamilyValue colunaFamilia = (ColumnFamilyValue) object.getAnnotation(ColumnFamilyValue.class);

	         if (colunaFamilia != null) {
	             return colunaFamilia.nome().equals("") ?object.getSimpleName():colunaFamilia.nome();
	         }
	         return object.getSimpleName();
	     }
	 	
	 	
	    /**
	     * verifies that the name of the annotation is empty
	     * if you take the field name
	     * @param field - field for viewd
	     * @return The name inside annotations or the field's name
	     */
	    public static String getColumnName(Field field) {
	        return field.getAnnotation(ColumnValue.class).nome().equals("") ?field.getName():field.getAnnotation(ColumnValue.class).nome();
	        
	    }
	    

	    /**
	     * verifies that the name of the annotation is empty
	     * if you take the field name
	     * @param field
	     * @return The name inside annotations or the field's name
	     */
	    public static String getEnumeratedName(Field field) {
	        return field.getAnnotation(EnumeratedValue.class).nome().equals("") ?field.getName():field.getAnnotation(EnumeratedValue.class).nome();
	        
	    }
	    
	    
	    /**
	     *  Return the Field with the KeyValue Annotations
	     * @see KeyValue
	     * @param persistenceClass - Class of the object viewed
	     * @return the Field if there are not will be return null
	     */
	    public static Field getKeyField(Class<?> persistenceClass) {

	        return getField(persistenceClass, KeyValue.class);
	    }

	     /**
	     *  Return the Field with the IndexValue Annotations
	     * @see IndexValue
	     * @param persistenceClass - Class of the object viewed
	     * @return the Field if there are not will be return null
	     */
	    public static Field getIndexField(Class<?> persistenceClass) {
	        return getField(persistenceClass, IndexValue.class);
	    }
	    
	    /**
	     * Get the Field of the Object from  annotation 
	     * if there are not return will be null
	     * @param object -  Class of the object viewed
	     * @param annotation
	     * @return 
	     */
	     @SuppressWarnings({ "unchecked", "rawtypes" })
		public static Field getField(Class object, Class annotation) {

	        for (Field field : object.getDeclaredFields()) {
	            if (field.getAnnotation(annotation) != null) {
	                return field;
	            } else if (field.getAnnotation(EmbeddedValue.class) != null) {


	                return getField(field.getType(), annotation);

	            }

	        }
	        return null;
	    }
	
	     
	     /**
	      * create columns based on annotations in Easy-Cassandra
	      * @param object - the object viewed
	      * @return - List of the Column
	      */
	     public static List<Column> getColumns(Object object) {
	         Long timeStamp = System.currentTimeMillis();
	         List<Column> columns = new ArrayList<>();
	         Field[] fields = object.getClass().getDeclaredFields();

	         for (Field field : fields) {
	             if (field.getAnnotation(KeyValue.class) != null) {
	                 continue;
	             }
	             if (field.getAnnotation(ColumnValue.class) != null) {
	                 
	                 Column column = makeColumn(timeStamp, ColumnUtil.getColumnName(field), object, field);
	                 if (column != null) {
	                     columns.add(column);
	                 }
	             } else if (field.getAnnotation(EmbeddedValue.class) != null) {
	                 if (ReflectionUtil.getMethod(object, field) != null) {
	                     columns.addAll(getColumns(ReflectionUtil.getMethod(object, field)));
	                 }
	             } else if (field.getAnnotation(EnumeratedValue.class) != null) {
	                 Column column = new Column();
	                 column.setTimestamp(timeStamp);
	                 column.setName(EncodingUtil.stringToByte(ColumnUtil.getEnumeratedName(field)));


	                 ByteBuffer byteBuffer = new EnumWrite().getBytebyObject(ReflectionUtil.getMethod(object, field));
	                 column.setValue(byteBuffer);


	                 if (column != null) {
	                     columns.add(column);
	                 }
	             }

	         }
	         return columns;
	     }

	     /**
	      * Create a column for persist in Cassandra
	      * @param timeStamp - time in the Column
	      * @param coluna - name in the Column
	      * @param object -  the object viewed
	      * @param field - the Field viewed
	      * @return the column for the Cassandra
	      */
	     public static Column makeColumn(long timeStamp, String coluna, Object object, Field field) {

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
	     
	     
	     
	private ColumnUtil(){
		
		
	}
}
