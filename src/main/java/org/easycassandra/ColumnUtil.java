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
package org.easycassandra;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Version;

import org.easycassandra.util.ReflectionUtil;

/**
 * Class Util for Column.
 * @author otavio
 */
enum ColumnUtil {
    INTANCE;

    /**
     * list contains the annotations to map a bean.
     */
    private List<String> annotations;
    {
        annotations = new ArrayList<String>();
        annotations.add(Id.class.getName());
        annotations.add(ElementCollection.class.getName());
        annotations.add(SetData.class.getName());
        annotations.add(ListData.class.getName());
        annotations.add(MapData.class.getName());
        annotations.add(Column.class.getName());
        annotations.add(Embedded.class.getName());
        annotations.add(EmbeddedId.class.getName());
        annotations.add(Enumerated.class.getName());
        annotations.add(Index.class.getName());
        annotations.add(CustomData.class.getName());
        Collections.sort(annotations);
    }


    /**
     * Get The Column name from an Object.
     * @param object
     *            - Class of the object viewed
     * @return The name of Column name if there are not will be return null
     */
    public String getColumnFamilyNameSchema(Class<?> object) {
        String schema = getSchemaConcat(object);
        return schema.concat(getColumnFamily(object));
    }

    public String getColumnFamily(Class<?> object) {
        Entity columnFamily = (Entity) object.getAnnotation(Entity.class);
        Table columnFamilyTable = (Table) object.getAnnotation(Table.class);
        if (columnFamily != null) {
            return columnFamily.name().equals("") ? object.getSimpleName()
                    : columnFamily.name();
        } else if (columnFamilyTable != null) {
            return columnFamilyTable.name().equals("") ? object.getSimpleName()
                    : columnFamilyTable.name();
        }
        return object.getSimpleName();
    }

    private String getSchemaConcat(Class<?> class1) {
        String schema = getSchema(class1);
        if (!"".equals(schema)) {
            return schema.concat(".");
        }
        return "";
    }

    /**
     * return the chema looking to  class.
     * @param class1 the class
     * @return the schema
     */
    public String getSchema(Class<?> class1) {
        Table columnFamily = (Table) class1.getAnnotation(Table.class);
        if (columnFamily != null) {
            return columnFamily.schema();
        }
        return "";
    }

    /**
     * verifies that the name of the annotation is empty if you take the field
     * name.
     * @param field
     *            - field for viewd
     * @return The name inside annotations or the field's name
     */
    public String getColumnName(Field field) {
        if (field.getAnnotation(javax.persistence.Column.class) == null) {
            return field.getName();
        }
        return field.getAnnotation(javax.persistence.Column.class).name()
                .equals("") ? field.getName() : field.getAnnotation(
                javax.persistence.Column.class).name();
    }

    /**
     * verifies that the name of the annotation is empty if you take the field.
     * name
     * @param field the field
     * @return The name inside annotations or the field's name
     */
    public String getEnumeratedName(Field field) {
        if (isNormalField(field)) {
            return getColumnName(field);
        }
        return field.getName();
    }

    /**
     * Return the Field with the KeyValue Annotations.
     * @see KeyValue
     * @param persistenceClass
     *            - Class of the object viewed
     * @return the Field if there are not will be return null
     */
    public Field getKeyField(Class<?> persistenceClass) {
        Field field = getField(persistenceClass, Id.class);
        if (field == null) {
            return getField(persistenceClass.getSuperclass(), Id.class);
        }
        return field;
    }

    /**
     * Return the Field with the complex key Annotations.
     * @see KeyValue
     * @param persistenceClass
     *            - Class of the object viewed
     * @return the Field if there are not will be return null
     */
    public Field getKeyComplexField(Class<?> persistenceClass) {
        Field field = getField(persistenceClass, EmbeddedId.class);
        if (field == null) {
            return getField(persistenceClass.getSuperclass(), EmbeddedId.class);
        }
        return field;
    }

    /**
     * Return the Field with the IndexValue Annotations.
     * @see Index
     * @param persistenceClass
     *            - Class of the object viewed
     * @return the Field if there are not will be return null
     */
    public Field getIndexField(Class<?> persistenceClass) {
        return getField(persistenceClass, Index.class);
    }

    /**
     * Return the Fields with the IndexValue Annotations.
     * @author Dinusha Nandika
     * @see Index
     * @param persistenceClass
     *            - Class of the object viewed
     * @return the Fields if there are not will be return empty list
     */
    public List<FieldInformation> getIndexFields(Class<?> persistenceClass) {
        List<FieldInformation> indexFieldList = new ArrayList<>();
        for (Field field : persistenceClass.getDeclaredFields()) {
            if (field.getAnnotation(Index.class) != null) {
                indexFieldList.add(new FieldInformation(field));
            } else if (field.getAnnotation(Embedded.class) != null) {
                indexFieldList.addAll(getIndexFields(field.getType()));
            }
        }
        return indexFieldList;
    }
    /**
     * Get the Field of the Object from annotation if there are not return will.
     * be null
     * @param object
     *            - Class of the object viewed
     * @param annotation the annotation
     * @return the field with annotation and null if does not exist
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Field getField(Class object, Class annotation) {
        for (Field field : object.getDeclaredFields()) {
            if (field.getAnnotation(annotation) != null) {
                return field;
            } else if (field.getAnnotation(Embedded.class) != null) {
                return getField(field.getType(), annotation);
            }
        }
        return null;
    }

    /**
     * list the fields in the class.
     * @param class1 the class
     * @return list of the fields
     */
    public List<FieldInformation> listFields(Class<?> class1) {
        List<FieldInformation> fields = new LinkedList<>();
        feedFieldList(class1, fields);
        if (isMappedSuperclass(class1)) {
            feedFieldList(class1.getSuperclass(), fields);

        }
        return fields;
    }

    /**
     * feed the list with Fields.
     * @param class1
     * @param fields
     */
    private void feedFieldList(Class<?> class1, List<FieldInformation> fields) {

        for (Field field : class1.getDeclaredFields()) {

            if (isColumnToPersist(field)) {
                fields.add(new FieldInformation(field));
            }

        }
    }

    /**
     * verify is field has some annotations within.
     * {@link #ColumnUtil#annotations}
     * @param field
     * @return
     */
    private boolean isColumnToPersist(Field field) {

        for (Annotation annotation : field.getAnnotations()) {
            int result = Collections.binarySearch(annotations, annotation
                    .annotationType().getName());
            if (result >= 0) {
                return true;
            }
        }
        return false;
    }
    /**
     * verify if this is key of the column.
     * @param field the field
     * @return is id
     */
    public boolean isIdField(Field field) {
        return field.getAnnotation(Id.class) != null;
    }

    /**
     * verify if this is index of the column.
     * @param field the feiold
     * @return if index
     */
    public boolean isIndexField(Field field) {
        return field.getAnnotation(Index.class) != null;
    }

    /**
     * verify if this is GeneratedValue of the column.
     * @param field the field
     * @return if GeneratedValue
     */
    @GeneratedValue
    public boolean isGeneratedValue(Field field) {
        return field.getAnnotation(GeneratedValue.class) != null;
    }

    /**
     * verify if this is secundary index of the column.
     * @param field the field
     * @return the exist
     */
    public boolean isSecundaryIndexField(Field field) {
        return field.getAnnotation(Index.class) != null;
    }

    /**
     * verify if this is a normal column.
     * @param field the field
     * @return if has column annotation
     */
    public boolean isNormalField(Field field) {
        return field.getAnnotation(javax.persistence.Column.class) != null;
    }

    /**
     * verify if this is a enum column.
     * @param field the field
     * @return if has Enumerated annotation
     */
    public boolean isEnumField(Field field) {
        return field.getAnnotation(Enumerated.class) != null;
    }

    /**
     * verify if this is a Embedded column.
     * @param field the field
     * @return if has Embedded annotation
     */
    public boolean isEmbeddedField(Field field) {
        return field.getAnnotation(Embedded.class) != null;
    }

    /**
     * verify if this is a Embedded id column.
     * @param field the field
     * @return if has EmbeddedId annotation
     */
    public boolean isEmbeddedIdField(Field field) {
        return field.getAnnotation(EmbeddedId.class) != null;
    }

    /**
     * verify if this is a Partkey id column.
     * @param field the field
     * @return if has PartKey annotation
     */
    public boolean isPartkeyField(Field field) {
        return field.getAnnotation(Partkey.class) != null;
    }

    /**
     * verify is this field is Cluester Ordering column.
     *
     * @param field the field
     * @return if has ClusteringOrder annotation
     */
    public boolean isClusteringOrderField(Field field) {
        return field.getAnnotation(ClusteringOrder.class) != null;
    }

    /**
     * Retrieve the cluster order.
     *
     * @param field the field
     * @return The instance of Order that indicates the order
     */
    public Order getClusterOrder(Field field) {
        Order order = null;
        if (isClusteringOrderField(field)) {
            order = field.getAnnotation(ClusteringOrder.class).order();
        }
        return order;
    }

    /**
     * verify if this is a Version column.
     * @param field the field
     * @return if has Version annotation
     */
    public boolean isVersionField(Field field) {
        return field.getAnnotation(Version.class) != null;
    }

    /**
     * verify is exist father to persist.
     * @param class1 the class
     * @return is has MappedSuperclass annotation
     */
    public boolean isMappedSuperclass(Class<?> class1) {
        return class1.getSuperclass().getAnnotation(MappedSuperclass.class) != null;
    }
    /**
     * verify if the field is a list.
     * @param field the field
     * @return if has ListData annotation
     */
    public boolean isList(Field field) {
        return field.getAnnotation(ListData.class) != null;
    }
    /**
     * verify if the field is a map.
     * @param field the field
     * @return if has MapData annotation
     */
    public boolean isMap(Field field){
        return field.getAnnotation(MapData.class) != null;
    }
    /**
     * verify if the field is a set.
     * @param field the field
     * @return if has SetData annotation
     */
    public boolean isSet(Field field) {
        return field.getAnnotation(SetData.class) != null;
    }

    /**
     * verify if the field is custom.
     * @param field the field
     * @return if has CustomData annotation
     */
    public boolean isCustom(Field field) {
        return field.getAnnotation(CustomData.class) != null;
    }
    /**
     * verify if the field is element Collection.
     * @param field the field
     * @return if has ElementCollection annotation
     */
	public boolean isElementCollection(Field field) {
		return field.getAnnotation(ElementCollection.class) != null;
	}


}