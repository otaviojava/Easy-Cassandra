package org.easycassandra.persistence.cassandra;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.EnumMap;
import java.util.Map;

import org.easycassandra.CustomData;
import org.easycassandra.FieldType;
import org.easycassandra.util.ReflectionUtil;

import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.DataType.Name;
import com.datastax.driver.core.Row;

/**
 * Util to return values {@link ReturnValue}.
 * @author otaviojava
 */
enum ReturnValues {
    INSTANCE;

    private Map<FieldType, ReturnValue> returnValuesMap;

    {
    	returnValuesMap = new EnumMap<>(FieldType.class);
    	returnValuesMap.put(FieldType.ENUM, new EnumReturnValue());
    	returnValuesMap.put(FieldType.LIST, new ListReturnValue());
    	returnValuesMap.put(FieldType.SET, new SetReturnValue());
    	returnValuesMap.put(FieldType.MAP, new MapReturnValue());
    	returnValuesMap.put(FieldType.COLLECTION, new CollectionReturnValue());
    	returnValuesMap.put(FieldType.CUSTOM, new CustomReturnValue());
    	returnValuesMap.put(FieldType.DEFAULT, new DefaultReturnValue());
    }
    public ReturnValue factory(Field field) {
    	return returnValuesMap.get(FieldType.getTypeByField(field));
    }

    /**
     * {@link ReturnValue} to Custom field.
     * @author otaviojava
     */
    class CustomReturnValue implements ReturnValue {

        @Override
        public Object getObject(Map<String, Definition> mapDefinition,
                Field field, Row row) {

            Definition column = mapDefinition.get(ColumnUtil.INTANCE
                    .getColumnName(field).toLowerCase());
            ByteBuffer buffer = (ByteBuffer) RelationShipJavaCassandra.INSTANCE
                    .getObject(row, column.getType().getName(),
                            column.getName());
            CustomData customData = field.getAnnotation(CustomData.class);
            Customizable customizable = Customizable.class
                    .cast(ReflectionUtil.INSTANCE.newInstance(customData
                            .classCustmo()));

            return customizable.write(buffer);
        }

    }
    /**
     * {@link ReturnValue} to Map field.
     * @author otaviojava
     */
    class MapReturnValue implements ReturnValue {

        @Override
        public Object getObject(Map<String, Definition> mapDefinition,
                Field field, Row row) {
            ReflectionUtil.KeyValueClass keyValueClass = ReflectionUtil.INSTANCE
                    .getGenericKeyValue(field);
            return RelationShipJavaCassandra.INSTANCE.getObject(row, Name.MAP,
                    ColumnUtil.INTANCE.getColumnName(field),
                    keyValueClass.getKeyClass(), keyValueClass.getValueClass());

        }

    }

    /**
     * {@link ReturnValue} to Set field.
     * @author otaviojava
     */
    class SetReturnValue implements ReturnValue {

        @Override
        public Object getObject(Map<String, Definition> mapDefinition,
                Field field, Row row) {
            return RelationShipJavaCassandra.INSTANCE.getObject(row, Name.SET,
                    ColumnUtil.INTANCE.getColumnName(field),
                    ReflectionUtil.INSTANCE.getGenericType(field));

        }

    }

    /**
     * {@link ReturnValue} to List field.
     * @author otaviojava
     */
    class ListReturnValue implements ReturnValue {

        @Override
        public Object getObject(Map<String, Definition> mapDefinition,
                Field field, Row row) {
            return RelationShipJavaCassandra.INSTANCE.getObject(row, Name.LIST,
                    ColumnUtil.INTANCE.getColumnName(field),
                    ReflectionUtil.INSTANCE.getGenericType(field));

        }

    }

    /**
     * {@link ReturnValue} to Collection field.
     * @author otaviojava
     */
    class CollectionReturnValue implements ReturnValue {

        @Override
        public Object getObject(Map<String, Definition> mapDefinition,
                Field field, Row row) {
            ReturnValue returnValue = returnValuesMap.get(FieldType
                    .findCollectionbyQualifield(field));
            return returnValue.getObject(mapDefinition, field, row);
        }

    }

    /**
     * {@link ReturnValue} to enum field.
     * @author otaviojava
     */
    class EnumReturnValue implements ReturnValue {

        @Override
        public Object getObject(Map<String, Definition> mapDefinition,
                Field field, Row row) {
            Integer value = (Integer) RelationShipJavaCassandra.INSTANCE
                    .getObject(row, Name.INT,
                            ColumnUtil.INTANCE.getColumnName(field));
            return field.getType().getEnumConstants()[value];
        }

    }

    /**
     * {@link ReturnValue} to default field.
     * @author otaviojava
     */
    class DefaultReturnValue implements ReturnValue {

        @Override
        public Object getObject(Map<String, Definition> mapDefinition,
                Field field, Row row) {
            Definition column = mapDefinition.get(ColumnUtil.INTANCE
                    .getColumnName(field).toLowerCase());
            return RelationShipJavaCassandra.INSTANCE.getObject(row, column
                    .getType().getName(), column.getName());
        }

    }

    /**
     * the interface is to return the object from the DataBase.
     * @author otaviojava
     */
    interface ReturnValue {
        /**
         * return the object from Cassandra.
         * @param mapDefinition - map with definition
         * @param field - the field
         * @param row - the information on cassandra
         * @return
         */
        Object getObject(Map<String, Definition> mapDefinition, Field field, Row row);
    }
}
