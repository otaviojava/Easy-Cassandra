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

    public AddColumn factory(Field field){
		return addColumnMap.get(FieldType.getTypeByField(field));
    }

class CustomAdd implements AddColumn{

    @Override
    public String addRow(Field field, RelationShipJavaCassandra javaCassandra) {
        String columnName = ColumnUtil.INTANCE.getColumnName(field);
        StringBuilder row = new StringBuilder();
        row.append(columnName).append(" ").append(javaCassandra.getPreferenceCQLType(ByteBuffer.class.getName())).append(",");
        return row.toString();
    }

}

class MapAdd implements AddColumn{

    @Override
    public String addRow(Field field, RelationShipJavaCassandra javaCassandra) {
        StringBuilder row = new StringBuilder();
        String columnName = ColumnUtil.INTANCE.getColumnName(field);
        ReflectionUtil.KeyValueClass keyValueClass = ReflectionUtil.INSTANCE.getGenericKeyValue(field);

        row.append(columnName).append(" map<").append(javaCassandra.getPreferenceCQLType(keyValueClass.getKeyClass().getName())).append(",");
        row.append(javaCassandra.getPreferenceCQLType(keyValueClass.getValueClass().getName())).append(">,");
        return row.toString();
    }

}

class SetAdd implements AddColumn {

    @Override
    public String addRow(Field field, RelationShipJavaCassandra javaCassandra) {
        StringBuilder row = new StringBuilder();
        String columnName = ColumnUtil.INTANCE.getColumnName(field);
        Class<?> clazz = ReflectionUtil.INSTANCE.getGenericType(field);
        row.append(columnName).append(" set<").append(javaCassandra.getPreferenceCQLType(clazz.getName())).append(">,");
        return row.toString();
    }

}
class ListAdd implements AddColumn{

    @Override
    public String addRow(Field field, RelationShipJavaCassandra javaCassandra) {
        StringBuilder row = new StringBuilder();
        String columnName = ColumnUtil.INTANCE.getColumnName(field);
        Class<?> clazz = ReflectionUtil.INSTANCE.getGenericType(field);
        row.append(columnName).append(" list<").append(javaCassandra.getPreferenceCQLType(clazz.getName())).append(">,");
        return row.toString();
    }

}

class EnumAdd implements AddColumn{

    @Override
    public String addRow(Field field,RelationShipJavaCassandra javaCassandra) {
        String columnName = ColumnUtil.INTANCE.getColumnName(field);
        StringBuilder row = new StringBuilder();
        row.append(columnName).append(" ").append(javaCassandra.getPreferenceCQLType(ColumnUtil.DEFAULT_ENUM_CLASS.getName())).append(",");
        return row.toString();
    }

}

class DefaultAdd implements AddColumn{

    @Override
    public String addRow(Field field, RelationShipJavaCassandra javaCassandra) {
        String columnName = ColumnUtil.INTANCE.getColumnName(field);
        StringBuilder row = new StringBuilder();
        row.append(columnName).append(" ").append(javaCassandra.getPreferenceCQLType(field.getType().getName())).append(",");
        return row.toString();
    }

}

class CollectionAdd implements AddColumn {

	@Override
	public String addRow(Field field, RelationShipJavaCassandra javaCassandra) {
		AddColumn addColumn =  addColumnMap.get(FieldType.findCollectionbyQualifield(field));
		return addColumn.addRow(field, javaCassandra);
	}

}
/**
 * The contract to create a column to cassandra checking the annotation.
 * @author otaviojava
 *
 */
interface AddColumn{
    /**
     * create a column.
     * @param field
     * @param javaCassandra
     * @return
     */
    String addRow(Field field,RelationShipJavaCassandra javaCassandra); 
}
}
