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
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.easycassandra.EasyCassandraException;
import org.easycassandra.annotations.read.EnumRead;
import org.easycassandra.annotations.read.ReadInterface;
import org.easycassandra.annotations.read.ReadManager;
import org.easycassandra.annotations.write.WriteInterface;
import org.easycassandra.annotations.write.WriteManager;
import org.easycassandra.util.EncodingUtil;
import org.easycassandra.util.ReflectionUtil;

/**
 *
 * @author otavio
 */
class BasePersistence {

    /**
     * Key value of write for The Cassandra persistence
     *
     * @see WriteInterface
     */
    private static Map<String, WriteInterface> writeMap;
    /**
     * Key Valeu of read for The Cassnadra persistence
     *
     * @see ReadInterface
     */
    private static Map<String, ReadInterface> readMap;
    /**
     * Class for read Byte
     *
     * @see ReadManager
     */
    private static ReadManager readManager;
    /**
     * Class for write Byte
     *
     * @see WriteManager
     */
    private static WriteManager writeManager;
    /**
     * Class with information about Column Family managed by Easy-Cassandra
     */
    private AtomicReference<ColumnFamilyIds> referenciaSuperColunas;
    /**
     * name of keyspace where the Client is
     */
    private String keyStore;

    static {
        writeMap = ReadWriteMaps.getWriteMap();
        readMap = ReadWriteMaps.getReadMap();
    }

    /**
     * Construtor for inicialize the readMap and the WriteMap
     *
     * @see BasePersistence#writeMap
     * @see BasePersistence#readMap
     */
    public BasePersistence() {
        writeManager = new WriteManager(writeMap);
        readManager = new ReadManager(readMap);
    }

    /**
     * Return the ByteBuffer with the KeyValue
     *
     * @param object - the object viewed
     * @param autoEnable - if the Keyvalue auto is enable
     * @return -The value of the KeyRow in ByteBuffer format
     * @see KeyValue @{@link EasyCassandraException - for operation
     * in EasyCassandra
     */
    protected ByteBuffer getKey(Object object, boolean autoEnable) {
        Field keyField = ColumnUtil.getKeyField(object.getClass());
        if (keyField == null) {
            throw new EasyCassandraException("You must use annotation"
                    + " @org.easycassandra.annotations.KeyValue in some"
                    + " field of the Class: " + object.getClass().getName()
                    + "  for be the keyrow in Cassandra");
        }
        String familyColumn = ColumnUtil.getColumnFamilyName(object.getClass());

        if (ColumnUtil.isGeneratedValue(keyField)) {
            ColumnUtil.setAutoCoutingKeyValue(object, keyField, familyColumn,
                    referenciaSuperColunas, keyStore);
        }

        Object keyValue = ReflectionUtil.getMethod(object, keyField);
        if (keyValue == null) {
            throw new EasyCassandraException("The key value must be not"
                    + " empty or null");
        }

        return getWriteManager().convert(keyValue);
    }

    //columns Utils
    /**
     * Create values for query e.g: (key1, key2...)
     *
     * @param keys - the values
     * @return - the String for query in
     */
    protected String createValuesIn(Object... keys) {
        StringBuilder keyNames = new StringBuilder();
        keyNames.append(" (");
        String condicion = "";
        for (Object key : keys) {
            keyNames.append(" ");
            keyNames.append(condicion);
            keyNames.append("'");
            keyNames.append(EncodingUtil.byteToString(
                    Persistence.getWriteManager().convert(key)).toString());
            keyNames.append("'");
            condicion = ",";
        }
        return keyNames.substring(0, keyNames.length()) + ") ";
    }

    //read objetct
    /**
     * The List Of the Class with information from Cassandra
     *
     * @param colMap - Key Map with column name and value
     * @param persisteceClass - Class for the List
     * @return - List of the persisteceClass
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected List getList(List<Map<String, ByteBuffer>> colMap,
            Class persisteceClass) throws InstantiationException,
            IllegalAccessException {
        List lists = new ArrayList();

        for (Map<String, ByteBuffer> listMap : colMap) {
            Object bean = null;
            try {
                bean = persisteceClass.newInstance();
            } catch (Exception ex) {
                Logger.getLogger(
                        BasePersistence.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
            bean = readObject(listMap, bean);
            if(bean!=null){
            lists.add(bean);
            }
        }
        return lists;
    }

    /**
     * The List Of the Class with information from Cassandra
     *
     * @param listMap - Key Map with column name and value
     * @param bean - The Object created
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    protected Object readObject(Map<String, ByteBuffer> listMap, Object bean)
            throws InstantiationException, IllegalAccessException {
        Boolean objNotNull = false;
        for (Field field : ColumnUtil.listFields(bean.getClass())) {
            if (ColumnUtil.isIdField(field)) {
                ByteBuffer bb = listMap.get("KEY");
                setMethod(bean, field, bb);
                continue;
            } else if (ColumnUtil.isNormalField(field)) {
                ByteBuffer bb = listMap.get(ColumnUtil.getColumnName(field));
                objNotNull = setObjectNotNull(bean, objNotNull, field, bb);
                continue;
            } else if (ColumnUtil.isVersionField(field)) {
                ByteBuffer bb = listMap.get("TIMESTAMP");
                objNotNull = setObjectNotNull(bean, objNotNull, field, bb);
                continue;
            } else if (ColumnUtil.isEnumField(field)) {
                ByteBuffer bb = listMap.get(ColumnUtil.getEnumeratedName(field));
                Object value = new EnumRead(field.getType()).getObjectByByte(bb);
                if (setMethod(bean, field, value)) {
                    objNotNull = true;
                }
                continue;
            } else if (ColumnUtil.isEmbeddedField(field)) {
                Object value = field.getType().newInstance();
                value = readObject(listMap, value);
                if (setMethod(bean, field, value)) {
                    objNotNull = true;
                }
                continue;
            }
        }
        return isValidObject(bean, objNotNull);
    }

    /**
     * Verify if the object is null and set the value in the field.
     *
     * @param bean
     * @param objNotNull
     * @param field
     * @param byteBuffer
     * @return
     */
    private Boolean setObjectNotNull(Object bean, Boolean objNotNull,
            Field field, ByteBuffer byteBuffer) {
        if (setMethod(bean, field, byteBuffer)) {
            return true;
        }
        return objNotNull;
    }

    /**
     * Verify if the object is empty, and empty return null
     *
     * @param bean - the object
     * @param objNotNull - if the object is empty or not
     * @return the object or null
     */
    private Object isValidObject(Object bean, Boolean objNotNull) {
        if (objNotNull) {
            return bean;
        } else {
            return null;
        }
    }

    /**
     * Set the value of a field in an object.
     *
     * @param bean Object to be set the value of a field.
     * @param field Field to be set the value.
     * @param value Value to set in the field.
     * @return
     * <code>true</code> if and only if the value was set in the field,
     * <code>false</code> otherwise or the value is null;
     */
    private boolean setMethod(Object bean, Field field, Object value) {
        if (value != null) {
            return ReflectionUtil.setMethod(bean, field,
                    value instanceof ByteBuffer
                    ? getReadManager().convert((ByteBuffer) value,
                    field.getType()) : value);
        } else {
            return false;
        }
    }

    /**
     * the with synchronized for the KeySpace
     *
     * @return Keyspace's name
     */
    public synchronized String getKeySpace() {
        return keyStore;
    }

    /**
     * get of readManager
     *
     * @see BasePersistence#readManager
     * @return the readManager
     */
    public static ReadManager getReadManager() {
        return readManager;
    }

    /**
     * get of writeManager
     *
     * @see BasePersistence#writeManager
     * @return the writeManager
     */
    public static WriteManager getWriteManager() {
        return writeManager;
    }

    /**
     * @param referenciaSuperColunas the referenciaSuperColunas to set
     */
    public void setReferenciaSuperColunas(
            AtomicReference<ColumnFamilyIds> referenciaSuperColunas) {
        this.referenciaSuperColunas = referenciaSuperColunas;
    }

    /**
     * @param keyStore the keyStore to set
     */
    public void setKeySpace(String keyStore) {
        this.keyStore = keyStore;
    }
}
