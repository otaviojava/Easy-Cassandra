package org.easycassandra.persistence;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.cassandra.thrift.Column;
import org.easycassandra.annotations.*;
import org.easycassandra.annotations.read.EnumRead;
import org.easycassandra.annotations.read.ReadInterface;
import org.easycassandra.annotations.write.EnumWrite;
import org.easycassandra.annotations.write.WriteInterface;
import org.easycassandra.util.EncodingUtil;
import org.easycassandra.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author otavio
 */
class BasePersistence {
    /**
     * Logger
     */
    private static Logger LOOGER = LoggerFactory.getLogger(BasePersistence.class);
    /**
     * Key value of write for The Cassandra persistence
     * @see  WriteInterface
     */
    protected Map<String, WriteInterface> writeMap;
    /**
     * Key Valeu of read for The Cassnadra persistence
     * @see ReadInterface
     */
    protected Map<String, ReadInterface> readMap;
    /**
     * Class with information about Column Family managed by Easy-Cassandra
     */
    protected AtomicReference<ColumnFamilyIds> referenciaSuperColunas;

    /**
     * Construtor for inicialize the readMap and the WriteMap
     * @see BasePersistence#writeMap
     * @see BasePersistence#readMap
     */
    public BasePersistence() {
        writeMap = ReadWriteMaps.getWriteMap();

        readMap = ReadWriteMaps.getReadMap();

    }
/**
 * Get The Column name from an Object
 * @param object - Class of the object viewed
 * @return The name of Column name if there are not will be return null
 */
    protected String getColumnFamilyName(Class object) {

        ColumnFamilyValue colunaFamilia = (ColumnFamilyValue) object.getAnnotation(ColumnFamilyValue.class);

        if (colunaFamilia != null) {
            return colunaFamilia.nome();
        }
        return null;
    }

    /**
     * Get the Field of the Object from  annotation 
     * if there are not return will be null
     * @param object -  Class of the object viewed
     * @param annotation
     * @return 
     */
    private Field getField(Class object, Class annotation) {

        for (Field field : object.getDeclaredFields()) {
            if (field.getAnnotation(annotation) != null) {
                return field;
            } else if (field.getAnnotation(EmbeddedValue.class) != null) {


                return getField(ReflectionUtil.getMethod(object, field).getClass(), annotation);

            }

        }
        return null;
    }

    /**
     *  Return the Field with the KeyValue Annotations
     * @see KeyValue
     * @param persistenceClass - Class of the object viewed
     * @return the Field if there are not will be return null
     */
    protected Field getKeyField(Class persistenceClass) {

        return getField(persistenceClass, KeyValue.class);
    }

     /**
     *  Return the Field with the IndexValue Annotations
     * @see IndexValue
     * @param persistenceClass - Class of the object viewed
     * @return the Field if there are not will be return null
     */
    protected Field getIndexField(Class persistenceClass) {
        return getField(persistenceClass, IndexValue.class);
    }

    /**
     * Return the ByteBuffer with the KeyValue
     * @param object - the object viewed
     * @param autoEnable - if the Keyvaleu auto is enable
     * @return -The value of the KeyRow in ByteBuffer format
     * @see KeyValue
     * @throws IOException 
     */
    protected ByteBuffer getKey(Object object, boolean autoEnable) throws IOException {
        Field keyField = getKeyField(object.getClass());
        String colunaFamilia = getColumnFamilyName(object.getClass());
        if (keyField != null) {
            ByteBuffer data = null;
            KeyValue chave = keyField.getAnnotation(KeyValue.class);

            Long id = null;
            if (chave.auto() && autoEnable) {
                id = referenciaSuperColunas.get().getId(colunaFamilia);

                ReflectionUtil.setMethod(object, keyField, id);
            } else {
                id = (Long) ReflectionUtil.getMethod(object, keyField);
            }
            data = writeMap.get(keyField.getType().getName()).getBytebyObject(id);
            return data;

        }


        return null;
    }
    
/**
 * The names of the fields with annotations of the Easy-Cassandra
 * It uses the getColumnNames in the Try
 * @param object - the object viewed
 * @return  the DecoratorColumnNames
 * @see DecoratorColumnNames
 * @see BasePersistence#getColumnNames(java.lang.Class) 
 */
    protected DecoratorColumnNames columnNames(Class object) {

        try {
            return getColumnNames(object);
        } catch (IllegalAccessException | InstantiationException ex) {


            return null;
        }
    }

    /**
     * The names of the fields with annotations of the Easy-Cassandra
     * @param object -- the object viewed
     * @return -  the DecoratorColumnNames
     * @see DecoratorColumnNames
     * @throws InstantiationException
     * @throws IllegalAccessException 
     */
    protected DecoratorColumnNames getColumnNames(Class object) throws InstantiationException, IllegalAccessException {
        DecoratorColumnNames names = new DecoratorColumnNames();
        Field[] fields = object.getDeclaredFields();

        for (Field f : fields) {
            if (f.getAnnotation(KeyValue.class) != null) {
                continue;
            } else if (f.getAnnotation(ColumnValue.class) != null || f.getAnnotation(EnumeratedValue.class) != null) {
                names.add(f.getAnnotation(ColumnValue.class) != null ? f.getAnnotation(ColumnValue.class).nome() : f.getAnnotation(EnumeratedValue.class).nome());
            } else if (f.getAnnotation(EmbeddedValue.class) != null) {
                 names.addAll(getColumnNames(f.getType()).getNames());
            }


        }

        return names;
    }
//columns Utils

    /**
     * create columns based on annotations in Easy-Cassandra
     * @param object - the object viewed
     * @return - List of the Column
     */
    protected List<Column> getColumns(Object object) {
        Long timeStamp = System.currentTimeMillis();
        List<Column> columns = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.getAnnotation(KeyValue.class) != null) {
                continue;
            }
            if (field.getAnnotation(ColumnValue.class) != null) {
                Column column = makeColumn(timeStamp, field.getAnnotation(ColumnValue.class).nome(), object, field);
                if (column != null) {
                    columns.add(column);
                }
            } else if (field.getAnnotation(EmbeddedValue.class) != null) {
                if (ReflectionUtil.getMethod(object, field) != null) {
                    columns.addAll(getColumns(ReflectionUtil.getMethod(object, field)));
                }
            } else if (field.getAnnotation(EnumeratedValue.class) != null) {
                Column column = new Column();
                column.setTimestamp(timeStamp);
                column.setName(EncodingUtil.stringToByte(field.getAnnotation(EnumeratedValue.class).nome()));


                ByteBuffer byteBuffer = new EnumWrite().getBytebyObject(ReflectionUtil.getMethod(object, field));
                column.setValue(byteBuffer);


                if (column != null) {
                    columns.add(column);
                }
            }

        }
        return columns;
    }

    /**
     * Create a column for persist in Cassandra
     * @param timeStamp - time in the Column
     * @param coluna - name in the Column
     * @param object -  the object viewed
     * @param field - the Field viewed
     * @return the column for the Cassandra
     */
    protected Column makeColumn(long timeStamp, String coluna, Object object, Field field) {

        Object subObject = ReflectionUtil.getMethod(object, field);
        if (subObject != null) {
            Column column = new Column();

            column.setTimestamp(timeStamp);
            column.setName(EncodingUtil.stringToByte(coluna));


            ByteBuffer byteBuffer = writeMap.get(field.getType().getName()).getBytebyObject(subObject);
            column.setValue(byteBuffer);

            return column;
        } else {
            return null;
        }
    }

    //read objetct
    /**
     * The List Of the Class with information from Cassandra
     * @param colMap - Key Map with column name and value
     * @param persisteceClass - Class for the List
     * @return - List of the persisteceClass
     * @throws InstantiationException
     * @throws IllegalAccessException 
     */
    protected List getList(List<Map<String, ByteBuffer>> colMap, Class persisteceClass) throws InstantiationException, IllegalAccessException {
        List lists = new ArrayList<>();
        Object bean = null;
        try {
            bean = persisteceClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
        }

        for (Map<String, ByteBuffer> listMap : colMap) {
            readObject(listMap, bean);
            lists.add(bean);
        }
        return lists;
    }
/**
 * The List Of the Class with information from Cassandra
 * @param listMap - Key Map with column name and value
 * @param bean -  The Object created
 * @throws InstantiationException
 * @throws IllegalAccessException 
 */
    protected void readObject(Map<String, ByteBuffer> listMap, Object bean) throws InstantiationException, IllegalAccessException {
        Field[] fieldsAll = bean.getClass().getDeclaredFields();
        for (Field field : fieldsAll) {

            if (field.getAnnotation(KeyValue.class) != null) {
                ByteBuffer byteBuffer = listMap.get("KEY");

                ReflectionUtil.setMethod(bean, field, readMap.get(field.getType().getName()).getObjectByByte(byteBuffer));
                continue;
            } else if (field.getAnnotation(ColumnValue.class) != null) {
                ByteBuffer byteBuffer = listMap.get(field.getAnnotation(ColumnValue.class).nome());
                if (byteBuffer != null) {
                    ReflectionUtil.setMethod(bean, field, readMap.get(field.getType().getName()).getObjectByByte(byteBuffer));
                }

            } else if (field.getAnnotation(EmbeddedValue.class) != null) {

                Object subObject = field.getType().newInstance();

                readObject(listMap, subObject);

                ReflectionUtil.setMethod(bean, field, subObject);
            } else if (field.getAnnotation(EnumeratedValue.class) != null) {

                ByteBuffer bb = listMap.get(field.getAnnotation(EnumeratedValue.class).nome());
                if (bb != null) {

                    Object[] enums = field.getType().getEnumConstants();
                    Integer index = (Integer) new EnumRead().getObjectByByte(bb);

                    ReflectionUtil.setMethod(bean, field, enums[index]);
                }

            }
        }
    }
}
