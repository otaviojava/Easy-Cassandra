package org.easycassandra.persistence.cassandra;

import java.lang.reflect.Field;
import java.util.Map;

import org.easycassandra.annotations.ListData;
import org.easycassandra.annotations.MapData;
import org.easycassandra.annotations.SetData;

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

        return new DefaultGetObject();
    }

    class MapReturnValue implements ReturnValue {

        @Override
        public Object getObject(Map<String, Definition> mapDefinition,Field field, Row row) {
            MapData mapData = field.getAnnotation(MapData.class);
            return RelationShipJavaCassandra.INSTANCE.getObject(row, Name.MAP,ColumnUtil.INTANCE.getColumnName(field),mapData.classKey(), mapData.classValue());

        }

    }

    class SetReturnValue implements ReturnValue {

        @Override
        public Object getObject(Map<String, Definition> mapDefinition,Field field, Row row) {
            SetData setData = field.getAnnotation(SetData.class);
            return RelationShipJavaCassandra.INSTANCE.getObject(row, Name.SET,ColumnUtil.INTANCE.getColumnName(field),setData.classData());

        }

    }

    class ListReturnValue implements ReturnValue {

        @Override
        public Object getObject(Map<String, Definition> mapDefinition,Field field, Row row) {
            ListData listData = field.getAnnotation(ListData.class);
            return RelationShipJavaCassandra.INSTANCE.getObject(row, Name.LIST, ColumnUtil.INTANCE.getColumnName(field),listData.classData());

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
