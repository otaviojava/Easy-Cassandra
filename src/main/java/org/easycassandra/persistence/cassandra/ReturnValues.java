package org.easycassandra.persistence.cassandra;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easycassandra.CustomData;
import org.easycassandra.FieldInformation;
import org.easycassandra.FieldType;
import org.easycassandra.util.ReflectionUtil;

import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.DataType.Name;
import com.datastax.driver.core.Row;
import com.google.gson.Gson;

/**
 * Util to return values {@link ReturnValue}.
 * @author otaviojava
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
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
    public ReturnValue factory(FieldInformation field) {
    	return returnValuesMap.get(field.getType());
    }

    /**
     * {@link ReturnValue} to Custom field.
     * @author otaviojava
     */
    class CustomReturnValue implements ReturnValue {

        @Override
        public Object getObject(Map<String, Definition> mapDefinition,
                FieldInformation field, Row row) {

            Definition column = mapDefinition.get(field.getName().toLowerCase());
            ByteBuffer buffer = (ByteBuffer) RelationShipJavaCassandra.INSTANCE
                    .getObject(row, column.getType().getName(),
                            column.getName());
            CustomData customData = field.getField().getAnnotation(CustomData.class);
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
                FieldInformation field, Row row) {
            RelationShipJavaCassandra javaCassandra = RelationShipJavaCassandra.INSTANCE;
            if (javaCassandra.containsType(field.getKey().getName())) {
                return RelationShipJavaCassandra.INSTANCE.getObject(row,
                        Name.MAP, field.getName(), field.getKey(),
                        field.getValue());
            }

            Map<String, String> map = (Map<String, String>) RelationShipJavaCassandra.INSTANCE
                    .getObject(row, Name.MAP, field.getName(), DEFAULT_COLLECTION_CLASS,
                            DEFAULT_COLLECTION_CLASS);

            Map newMap = new HashMap();
            Gson gson = new Gson();
            for (String element: map.keySet()) {
                Object newKey = gson.fromJson(element, field.getKey());
                Object newValue = gson.fromJson(map.get(element), field.getKey());
                newMap.put(newKey, newValue);
            }
            return newMap;
        }

    }

    /**
     * {@link ReturnValue} to Set field.
     * @author otaviojava
     */
    class SetReturnValue implements ReturnValue {
        @Override
        public Object getObject(Map<String, Definition> mapDefinition,
                FieldInformation field, Row row) {
            RelationShipJavaCassandra javaCassandra = RelationShipJavaCassandra.INSTANCE;
            if (javaCassandra.containsType(field.getKey().getName())) {
                return RelationShipJavaCassandra.INSTANCE.getObject(row, Name.SET,
                        field.getName().toLowerCase(), field.getKey());
            }
            Collection<String> list = (Collection<String>) RelationShipJavaCassandra.INSTANCE
                    .getObject(row, Name.SET, field.getName().toLowerCase(),
                            DEFAULT_COLLECTION_CLASS);
            Set newSet = new HashSet<>();
            Gson gson = new Gson();
            for (String element : list) {
                newSet.add(gson.fromJson(element, field.getKey()));
            }
            return newSet;
        }

    }

    /**
     * {@link ReturnValue} to List field.
     * @author otaviojava
     */
    class ListReturnValue implements ReturnValue {

        @Override
        public Object getObject(Map<String, Definition> mapDefinition,
                FieldInformation field, Row row) {

            RelationShipJavaCassandra javaCassandra = RelationShipJavaCassandra.INSTANCE;
            if (javaCassandra.containsType(field.getKey().getName())) {
                return RelationShipJavaCassandra.INSTANCE.getObject(row,
                        Name.LIST, field.getName().toLowerCase(),
                        field.getKey());
            }
            Collection<String> list = (Collection<String>) RelationShipJavaCassandra.INSTANCE
                    .getObject(row, Name.LIST, field.getName().toLowerCase(),
                            DEFAULT_COLLECTION_CLASS);

            List newList = new LinkedList<>();
            Gson gson = new Gson();
            for (String element: list) {
                newList.add(gson.fromJson(element, field.getKey()));
            }
            return newList;

        }

    }
    private static final Class<String> DEFAULT_COLLECTION_CLASS = String.class;
    /**
     * {@link ReturnValue} to Collection field.
     * @author otaviojava
     */
    class CollectionReturnValue implements ReturnValue {

        @Override
        public Object getObject(Map<String, Definition> mapDefinition,
                FieldInformation field, Row row) {
            ReturnValue returnValue = returnValuesMap.get(field.getCollectionType());
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
                FieldInformation field, Row row) {

            Integer value = (Integer) RelationShipJavaCassandra.INSTANCE
                    .getObject(row, Name.INT, field.getName().toLowerCase());
            return field.getField().getType().getEnumConstants()[value];
        }

    }

    /**
     * {@link ReturnValue} to default field.
     * @author otaviojava
     */
    class DefaultReturnValue implements ReturnValue {

        @Override
        public Object getObject(Map<String, Definition> mapDefinition,
                FieldInformation field, Row row) {
            Definition column = mapDefinition.get(field.getName().toLowerCase());
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
        Object getObject(Map<String, Definition> mapDefinition, FieldInformation field, Row row);
    }
}
