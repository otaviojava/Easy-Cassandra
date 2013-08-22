package org.easycassandra.persistence.cassandra;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.ByteBuffer;
import java.util.Map;

import org.easycassandra.CustomData;
import org.easycassandra.util.ReflectionUtil;

import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.DataType.Name;
import com.datastax.driver.core.Row;

enum ReturnValues {
    INSTANCE;

    public ReturnValue factory(Field field) {
        if (ColumnUtil.INTANCE.isEnumField(field)) {
            return new EnumReturnValue();
        }
        if (ColumnUtil.INTANCE.isList(field)) {
            return new ListReturnValue();
        }
        if (ColumnUtil.INTANCE.isSet(field)) {
            return new SetReturnValue();
        }
        if (ColumnUtil.INTANCE.isMap(field)) {
            return new MapReturnValue();
        }
        if(ColumnUtil.INTANCE.isCustom(field)){
            return new CustomReturnValue();
        }

        return new DefaultGetObject();
    }

    
    class CustomReturnValue implements ReturnValue{

        @Override
        public Object getObject(Map<String, Definition> mapDefinition,Field field, Row row) {
            Definition column = mapDefinition.get(ColumnUtil.INTANCE.getColumnName(field).toLowerCase());
            ByteBuffer buffer= (ByteBuffer)RelationShipJavaCassandra.INSTANCE.getObject(row, column.getType().getName(), column.getName());
            CustomData customData=field.getAnnotation(CustomData.class);
            Customizable customizable=Customizable.class.cast(ReflectionUtil.INSTANCE.newInstance(customData.classCustmo()));
            
            return customizable.write(buffer);
        }
        
        
    }
    class MapReturnValue implements ReturnValue {

        @Override
        public Object getObject(Map<String, Definition> mapDefinition,Field field, Row row) {
        	ParameterizedType genericType=(ParameterizedType)field.getGenericType();
            Class<?> keyClass=(Class<?>) genericType.getActualTypeArguments()[0];
            Class<?> valueClass=(Class<?>) genericType.getActualTypeArguments()[1];
            return RelationShipJavaCassandra.INSTANCE.getObject(row, Name.MAP,ColumnUtil.INTANCE.getColumnName(field),keyClass, valueClass);

        }

    }

    class SetReturnValue implements ReturnValue {

        @Override
        public Object getObject(Map<String, Definition> mapDefinition,Field field, Row row) {
        	ParameterizedType genericType=(ParameterizedType)field.getGenericType();
            Class<?> clazz=(Class<?>) genericType.getActualTypeArguments()[0];
            return RelationShipJavaCassandra.INSTANCE.getObject(row, Name.SET,ColumnUtil.INTANCE.getColumnName(field),clazz);

        }

    }

    class ListReturnValue implements ReturnValue {

        @Override
        public Object getObject(Map<String, Definition> mapDefinition,Field field, Row row) {
            ParameterizedType genericType=(ParameterizedType)field.getGenericType();
            Class<?> clazz=(Class<?>) genericType.getActualTypeArguments()[0];
            return RelationShipJavaCassandra.INSTANCE.getObject(row, Name.LIST, ColumnUtil.INTANCE.getColumnName(field),clazz);

        }

    }

    class EnumReturnValue implements ReturnValue {

        @Override
        public Object getObject(Map<String, Definition> mapDefinition, Field field, Row row) {
            Integer value = (Integer) RelationShipJavaCassandra.INSTANCE.getObject(row, Name.INT,ColumnUtil.INTANCE.getColumnName(field));
            return field.getType().getEnumConstants()[value];
        }

    }

    class DefaultGetObject implements ReturnValue {

        @Override
        public Object getObject(Map<String, Definition> mapDefinition,Field field, Row row) {
            Definition column = mapDefinition.get(ColumnUtil.INTANCE.getColumnName(field).toLowerCase());
            return RelationShipJavaCassandra.INSTANCE.getObject(row, column.getType().getName(), column.getName());
        }

    }

    interface ReturnValue {
        Object getObject(Map<String, Definition> mapDefinition, Field field,Row row);
    }
}
