package org.easycassandra;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * data structure that contains information of the class.
 * @author otaviojava
 */
public class ClassInformation {

    private String schema;

    private String name;

    private String nameSchema;

    private List<FieldInformation> fields;

    private List<FieldInformation> indexFields;

    private FieldInformation keyInformation;

    private Class<?> classInstance;

    private Map<String, String> fieldColumnMap;

    private boolean complexKey;

    public String getSchema() {
        return schema;
    }

    public List<FieldInformation> getFields() {
        return fields;
    }

    public FieldInformation getKeyInformation() {
        return keyInformation;
    }

    public boolean isComplexKey() {
        return complexKey;
    }

    public String getName() {
        return name;
    }

    public String getNameSchema() {
        return nameSchema;
    }

    public List<FieldInformation> getIndexFields() {
        return indexFields;
    }

    public Class<?> getClassInstance() {
        return classInstance;
    }

    /**
     * create all information of the class.
     * @param clazz the class
     */
    ClassInformation(Class<?> clazz) {
        findKey(clazz);
        this.nameSchema = ColumnUtil.INTANCE.getColumnFamilyNameSchema(clazz);
        this.schema = ColumnUtil.INTANCE.getSchema(clazz);
        this.name = ColumnUtil.INTANCE.getColumnFamily(clazz);
        this.classInstance = clazz;
        this.fields = ColumnUtil.INTANCE.listFields(clazz);
        this.indexFields = ColumnUtil.INTANCE.getIndexFields(clazz);
        this.fieldColumnMap = new TreeMap<>();
        runFeildColumnMap(this);
        Collections.sort(indexFields);
    }

    private void runFeildColumnMap(ClassInformation classInformation) {
        for (FieldInformation field : classInformation.getFields()) {

            if (field.isEmbedded()) {
                runFeildColumnMap(field.getSubFields());
                continue;
            }
            fieldColumnMap.put(field.getField().getName(), field.getName());

        }
    }

    private void findKey(Class<?> clazz) {
        Field key = ColumnUtil.INTANCE.getKeyField(clazz);
        if (key == null) {
            key = ColumnUtil.INTANCE.getKeyComplexField(clazz);
            complexKey = true;
        }
        if (key != null) {
            keyInformation = new FieldInformation(key);
        }
    }
    /**
     * returns the keySpace information.
     * @param keySpace the keySpace
     * @return {@link KeySpaceInformation}
     */
    public KeySpaceInformation getKeySpace(String keySpace) {
        KeySpaceInformation key = new KeySpaceInformation();
        if ("".equals(schema)) {
            key.keySpace = keySpace;
        } else {
            key.keySpace = schema;
        }
        key.columnFamily = name;
        return key;
    }
    /**
     * The dto to information.
     * @author otaviojava
     */
    public class KeySpaceInformation {
        private String keySpace;

        private String columnFamily;

        public String getKeySpace() {
            return keySpace;
        }

        public String getColumnFamily() {
            return columnFamily;
        }

    }
    /**
     * return the column's name from field name.
     * @param fieldName the property on the class
     * @return the column name on Cassandra to this field
     */
    public String toColumn(String fieldName) {
        return fieldColumnMap.get(fieldName);
    }
    /**
     * find out a field on the field indx by index.
     * @param indexName the indexName
     * @return the index information or null if not find out
     */
    public FieldInformation findIndexByName(String indexName) {
        int index = Collections.binarySearch(indexFields, new FieldInformation(indexName));
        if (index >= 0) {
            return indexFields.get(index);
        }

        return null;
    }
    @Override
    public String toString() {

        return nameSchema;
    }

}
