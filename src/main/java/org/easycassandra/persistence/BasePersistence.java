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

import javax.xml.crypto.dsig.keyinfo.KeyValue;

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
     * @see KeyValue @{@link EasyCassandraException - for operation in EasyCassandra
     */
    protected ByteBuffer getKey(Object object, boolean autoEnable) {
        Field keyField = ColumnUtil.getKeyField(object.getClass());
        if (keyField == null) {
            throw new EasyCassandraException("You must use annotation @org.easycassandra.annotations.KeyValue in some field of the Class: " + object.getClass().getName() + "  for be the keyrow in Cassandra");
        }
        String colunaFamilia = ColumnUtil.getColumnFamilyName(object.getClass());
        {


            if (ColumnUtil.isGeneratedValue(keyField)) {
                ColumnUtil.setAutoCoutingKeyValue(object, keyField, colunaFamilia, referenciaSuperColunas, keyStore);
            }

            Object keyValue = ReflectionUtil.getMethod(object, keyField);
            if (keyValue == null) {
                throw new EasyCassandraException("The key value must be not empty or null");
            }

            return getWriteManager().convert(keyValue);





        }
    }

//columns Utils
    /**
     * Create values for query e.g: (key1,key2...)
     *
     * @param keys - the values
     * @return - the String for query in
     */
    protected String createValuesIn(Object... keys) {
        StringBuilder keyNames = new StringBuilder();
        keyNames.append(" (");
        String condicion = "";
        for (Object key : keys) {
            ByteBuffer keyBuffer = getWriteManager().convert(key);
            keyNames.append(" " + condicion + "'" + EncodingUtil.byteToString(Persistence.getWriteManager().convert(key)).toString() + "'");
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
    protected List getList(List<Map<String, ByteBuffer>> colMap, Class persisteceClass) throws InstantiationException, IllegalAccessException {
        List lists = new ArrayList<>();


        for (Map<String, ByteBuffer> listMap : colMap) {
            Object bean = null;
            try {
                bean = persisteceClass.newInstance();
            } catch (InstantiationException | IllegalAccessException exception) {
                Logger.getLogger(BasePersistence.class.getName()).log(Level.SEVERE, null, exception);
            }
            bean = readObject(listMap, bean);
            lists.add(bean);
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
    protected Object readObject(Map<String, ByteBuffer> listMap, Object bean) throws InstantiationException, IllegalAccessException {
        Field[] fieldsAll = bean.getClass().getDeclaredFields();
        Boolean objNotNull = false;
        for (Field field : fieldsAll) {
            if (ColumnUtil.isIdField(field)) {
                ByteBuffer byteBuffer = listMap.get("KEY");
                setMethod(bean, field, byteBuffer);
                continue;
            } else if (ColumnUtil.isNormalField(field)) {
                ByteBuffer byteBuffer = listMap.get(ColumnUtil.getColumnName(field));
                if (setMethod(bean, field, byteBuffer)) {
                    objNotNull = true;
                }
                continue;
            } else if (ColumnUtil.isVersionField(field)) {
                ByteBuffer byteBuffer = listMap.get("TIMESTAMP");
                if (setMethod(bean, field, byteBuffer)) {
                    objNotNull = true;
                }
                continue;
            } else if (ColumnUtil.isEnumField(field)) {
                ByteBuffer byteBuffer = listMap.get(ColumnUtil.getEnumeratedName(field));
                Object value = new EnumRead(field.getType()).getObjectByByte(byteBuffer);
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
        if (objNotNull) {
            return bean;
        } else {
            return null;
        }
    }

    /**
     * Define o valor de um campo em um objeto.
     *
     * @param bean Objeto a se definir o valor do campo.
     * @param field Campo a ser definido o valor.
     * @param value Valor a se colocar no campo.
     * @return
     * <code>true</code> se e somente se o campo foi definido com o valor dado,
     * <code>false</code> caso o campo seja null ou não foi definido
     * corretamente.
     */
    private boolean setMethod(Object bean, Field field, Object value) {
        if (value != null) {
            value = value instanceof ByteBuffer
                    ? getReadManager().convert((ByteBuffer) value,
                    field.getType()) : value;
            return ReflectionUtil.setMethod(bean, field, value);
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
    public void setReferenciaSuperColunas(AtomicReference<ColumnFamilyIds> referenciaSuperColunas) {
        this.referenciaSuperColunas = referenciaSuperColunas;
    }

    /**
     * @param keyStore the keyStore to set
     */
    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }
}
