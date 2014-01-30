/*
 * Copyright 2013 Otávio Gonçalves de Santana (otaviojava)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.easycassandra.persistence.cassandra;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.EnumMap;
import java.util.Map;

import org.easycassandra.FieldType;
import org.easycassandra.util.ReflectionUtil;

/**
 * create a column to cassandra checking the annotation.
 * @author otaviojava
 */
enum AddColumnUtil {
    INSTANCE;
    private Map<FieldType, AddColumn> addColumnMap;

    {
    	addColumnMap = new EnumMap<>(FieldType.class);
    	addColumnMap.put(FieldType.ENUM, new EnumAdd());
    	addColumnMap.put(FieldType.LIST, new ListAdd());
    	addColumnMap.put(FieldType.SET, new SetAdd());
    	addColumnMap.put(FieldType.MAP, new MapAdd());
    	addColumnMap.put(FieldType.COLLECTION, new CollectionAdd());
    	addColumnMap.put(FieldType.CUSTOM, new CustomAdd());
    	addColumnMap.put(FieldType.DEFAULT, new DefaultAdd());
    }

    public AddColumn factory(Field field) {
        return addColumnMap.get(FieldType.getTypeByField(field));
    }

    /**
     * {@link AddColumn} to Custom.
     * @author otaviojava
     */
    class CustomAdd implements AddColumn {

        @Override
        public String addRow(Field field,
                RelationShipJavaCassandra javaCassandra) {
            String columnName = ColumnUtil.INTANCE.getColumnName(field);
            StringBuilder row = new StringBuilder();
            row.append(columnName)
                    .append(" ")
                    .append(javaCassandra.getPreferenceCQLType(ByteBuffer.class
                            .getName())).append(",");
            return row.toString();
        }

    }

    /**
     * {@link AddColumn} to Map.
     * @author otaviojava
     */
    class MapAdd implements AddColumn {

        @Override
        public String addRow(Field field,
                RelationShipJavaCassandra javaCassandra) {
            StringBuilder row = new StringBuilder();
            String columnName = ColumnUtil.INTANCE.getColumnName(field);
            ReflectionUtil.KeyValueClass keyValueClass = ReflectionUtil.INSTANCE
                    .getGenericKeyValue(field);

            row.append(columnName)
                    .append(" map<")
                    .append(javaCassandra.getPreferenceCQLType(keyValueClass
                            .getKeyClass().getName())).append(",");
            row.append(
                    javaCassandra.getPreferenceCQLType(keyValueClass
                            .getValueClass().getName())).append(">,");
            return row.toString();
        }

    }

    /**
     * {@link AddColumn} to Set.
     * @author otaviojava
     */
    class SetAdd implements AddColumn {

        @Override
        public String addRow(Field field,
                RelationShipJavaCassandra javaCassandra) {
            StringBuilder row = new StringBuilder();
            String columnName = ColumnUtil.INTANCE.getColumnName(field);
            Class<?> clazz = ReflectionUtil.INSTANCE.getGenericType(field);
            row.append(columnName)
                    .append(" set<")
                    .append(javaCassandra.getPreferenceCQLType(clazz.getName()))
                    .append(">,");
            return row.toString();
        }

    }

    /**
     * {@link AddColumn} to List.
     * @author otaviojava
     */
    class ListAdd implements AddColumn {

        @Override
        public String addRow(Field field,
                RelationShipJavaCassandra javaCassandra) {
            StringBuilder row = new StringBuilder();
            String columnName = ColumnUtil.INTANCE.getColumnName(field);
            Class<?> clazz = ReflectionUtil.INSTANCE.getGenericType(field);
            row.append(columnName)
                    .append(" list<")
                    .append(javaCassandra.getPreferenceCQLType(clazz.getName()))
                    .append(">,");
            return row.toString();
        }

    }

    /**
     * {@link AddColumn} to enum.
     * @author otaviojava
     */
    class EnumAdd implements AddColumn {

        @Override
        public String addRow(Field field,
                RelationShipJavaCassandra javaCassandra) {
            String columnName = ColumnUtil.INTANCE.getColumnName(field);
            StringBuilder row = new StringBuilder();
            row.append(columnName)
                    .append(" ")
                    .append(javaCassandra
                            .getPreferenceCQLType(ColumnUtil.DEFAULT_ENUM_CLASS
                                    .getName())).append(",");
            return row.toString();
        }

    }

    /**
     * {@link AddColumn} to Default.
     * @author otaviojava
     */
    class DefaultAdd implements AddColumn {

        @Override
        public String addRow(Field field,
                RelationShipJavaCassandra javaCassandra) {
            String columnName = ColumnUtil.INTANCE.getColumnName(field);
            StringBuilder row = new StringBuilder();
            row.append(columnName)
                    .append(" ")
                    .append(javaCassandra.getPreferenceCQLType(field.getType()
                            .getName())).append(",");
            return row.toString();
        }

    }

    /**
     * {@link AddColumn} to Collection.
     * @author otaviojava
     */
    class CollectionAdd implements AddColumn {

        @Override
        public String addRow(Field field,
                RelationShipJavaCassandra javaCassandra) {
            AddColumn addColumn = addColumnMap.get(FieldType
                    .findCollectionbyQualifield(field));
            return addColumn.addRow(field, javaCassandra);
        }

    }

    /**
     * The contract to create a column to cassandra checking the annotation.
     * @author otaviojava
     */
    interface AddColumn {
        /**
         * create a column.
         * @param field
         *            the field
         * @param javaCassandra
         *            the {@link RelationShipJavaCassandra}
         * @return the string to query
         */
        String addRow(Field field, RelationShipJavaCassandra javaCassandra);
    }
}
