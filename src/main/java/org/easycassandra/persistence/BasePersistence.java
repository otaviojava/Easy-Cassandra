/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.persistence;

import org.easycassandra.annotations.KeyValue;
import org.easycassandra.annotations.ColumnValue;

import org.easycassandra.annotations.ColumnFamilyValue;
import org.easycassandra.annotations.EmbeddedValue;
import org.easycassandra.annotations.IndexValue;

import org.easycassandra.annotations.read.ReadInterface;
import org.easycassandra.util.EncodingUtil;
import org.easycassandra.util.ReflectionUtil;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.cassandra.thrift.Column;
import org.easycassandra.annotations.EnumeratedValue;
import org.easycassandra.annotations.read.EnumRead;
import org.easycassandra.annotations.write.EnumWrite;
import org.easycassandra.annotations.write.WriteInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author otavio
 */
class BasePersistence {

    private static Logger LOOGER = LoggerFactory.getLogger(BasePersistence.class);
    protected Map<String, WriteInterface> writeMap;
    protected Map<String, ReadInterface> readMap;
    protected AtomicReference<ColumnFamilyIds> referenciaSuperColunas;

    public BasePersistence() {
        writeMap = ReadWriteMaps.getWriteMap();

        readMap = ReadWriteMaps.getReadMap();

    }

    protected String getColumnFamilyName(Class object) {

        ColumnFamilyValue colunaFamilia = (ColumnFamilyValue) object.getAnnotation(ColumnFamilyValue.class);

        if (colunaFamilia != null) {
            return colunaFamilia.nome();
        }
        return null;
    }

    private Field getField(Class persistenceClass, Class annotation) {

        for (Field f : persistenceClass.getDeclaredFields()) {
            if (f.getAnnotation(annotation) != null) {
                return f;
            } else if (f.getAnnotation(EmbeddedValue.class) != null) {
                String tipo = f.getType().getName();
                try {
                    return getField(f.getType().newInstance().getClass(), annotation);
                } catch (InstantiationException | IllegalAccessException ex) {
                    LOOGER.error("Error during getField", ex);
                }
            }

        }
        return null;
    }

    protected Field getKeyField(Class persistenceClass) {

        return getField(persistenceClass, KeyValue.class);
    }

    protected Field getIndexField(Class persistenceClass) {
        return getField(persistenceClass, IndexValue.class);
    }

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

    protected DecoratorColumnNames columnNames(Class object) {

        try {
            return getColumnNames(object);
        } catch (IllegalAccessException | InstantiationException ex) {


            return null;
        }
    }

    protected DecoratorColumnNames getColumnNames(Class object) throws InstantiationException, IllegalAccessException {
        DecoratorColumnNames names = new DecoratorColumnNames();
        Field[] fields = object.getDeclaredFields();

        for (Field f : fields) {
            if (f.getAnnotation(KeyValue.class) != null) {
                continue;
            } else if (f.getAnnotation(ColumnValue.class) != null || f.getAnnotation(EnumeratedValue.class) != null) {
                names.add(f.getAnnotation(ColumnValue.class) != null ? f.getAnnotation(ColumnValue.class).nome() : f.getAnnotation(EnumeratedValue.class).nome());
            } else if (f.getAnnotation(EmbeddedValue.class) != null) {
                Object subObject = f.getType().newInstance();
                names.addAll(getColumnNames(subObject.getClass()).getNames());
            }


        }

        return names;
    }
//columns Utils

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

    protected Column makeColumn(long timeStamp, String coluna, Object object, Field field) {

        Object o = ReflectionUtil.getMethod(object, field);
        if (o != null) {
            Column column = new Column();

            column.setTimestamp(timeStamp);
            column.setName(EncodingUtil.stringToByte(coluna));


            ByteBuffer byteBuffer = writeMap.get(field.getType().getName()).getBytebyObject(o);
            column.setValue(byteBuffer);

            return column;
        } else {
            return null;
        }
    }

    //read objetct
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

    protected void readObject(Map<String, ByteBuffer> listMap, Object bean) throws InstantiationException, IllegalAccessException {
        Field[] fieldsAll = bean.getClass().getDeclaredFields();
        for (Field field : fieldsAll) {

            if (field.getAnnotation(KeyValue.class) != null) {
                ByteBuffer bb = listMap.get("KEY");

                ReflectionUtil.setMethod(bean, field, readMap.get(field.getType().getName()).getObjectByByte(bb));
                continue;
            } else if (field.getAnnotation(ColumnValue.class) != null) {
                ByteBuffer bb = listMap.get(field.getAnnotation(ColumnValue.class).nome());
                if (bb != null) {
                    ReflectionUtil.setMethod(bean, field, readMap.get(field.getType().getName()).getObjectByByte(bb));
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
