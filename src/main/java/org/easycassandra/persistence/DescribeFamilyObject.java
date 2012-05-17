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
import java.util.HashMap;
import java.util.Map;

/**
 * describes about the family column object with their fields
 *
 * @author otavio
 *
 */
class DescribeFamilyObject {

    /**
     * Name of Column family
     */
    private String columnFamilyName;
    /**
     * map when key is column in Cassandra database and the value is the
     * Object's name
     */
    private Map<String, String> columnName;
    private Class<?> classFamily;
    private Map<String, DescribeField> fields;

    /**
     * method constructor for the DescribeFamilyObject
     *
     * @param classColumnFamily
     */
    DescribeFamilyObject(Class<?> classColumnFamily) {
        columnFamilyName = ColumnUtil.getColumnFamilyName(classColumnFamily);
        classFamily = classColumnFamily;
        for (Field field : ColumnUtil.listFields(classColumnFamily)) {
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }
            genereteDescribeField(field, "");
        }
    }

    /**
     * Create the describe field
     *
     * @param field
     * @param level
     * @return the describe field object
     */
    private DescribeField genereteDescribeField(Field field, String level) {
        if (ColumnUtil.isEmbeddedField(field)) {
            DescribeField describeField = new DescribeField();
            describeField.setTypeField(field);
            describeField.setName(level.concat(field.getName()));
            for (Field subField : field.getType().getDeclaredFields()) {
                if ("serialVersionUID".equals(subField.getName())) {
                    continue;
                }
                describeField.add(genereteDescribeField(subField,
                        level.concat(field.getName() + ".")));
            }
            columnName.put(describeField.getRealCqlName(),
                    describeField.getName());
            fields.put(describeField.getName(), describeField);
            return describeField;
        }
        DescribeField describeField = new DescribeField();
        describeField.setTypeField(field);
        describeField.setName(level.concat(field.getName()));
        columnName.put(describeField.getRealCqlName(), describeField.getName());
        fields.put(describeField.getName(), describeField);
        return describeField;
    }

    /**
     * return the Object's name
     *
     * @param column
     * @return
     */
    public String getFieldsName(String column) {
        return columnName.get(column);
    }

    /**
     * key-value for DescribeField
     *
     * @param value
     * @return
     */
    public DescribeField getField(String value) {

        return fields.get(value);
    }

    public String getColumnFamilyName() {
        return columnFamilyName;
    }

    public void setColumnFamilyName(String columnFamilyName) {
        this.columnFamilyName = columnFamilyName;
    }

    public Class<?> getClassFamily() {
        return classFamily;
    }

    public void setClassFamily(Class<?> classFamily) {
        this.classFamily = classFamily;
    }

    public Map<String, DescribeField> getFields() {
        return fields;
    }

    public void setFields(Map<String, DescribeField> fields) {
        this.fields = fields;
    }

    {
        fields = new HashMap<>();
        columnName = new HashMap<>();
    }
}
