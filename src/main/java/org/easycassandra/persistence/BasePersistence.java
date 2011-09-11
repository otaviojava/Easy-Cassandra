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

    protected ByteBuffer getKey(Object object) throws IOException {
        Field keyField = getKeyField(object.getClass());
        String colunaFamilia = getColumnFamilyName(object.getClass());
        if (keyField != null) {
            ByteBuffer data = null;
            KeyValue chave = keyField.getAnnotation(KeyValue.class);

            Long id = null;
            if (chave.auto()) {
                id = referenciaSuperColunas.get().getId(colunaFamilia);

                ReflectionUtil.setMethod(object, keyField.getName(), id);
            } else {
                id = (Long) ReflectionUtil.getMethod(object, keyField.getName());
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
            } else if (f.getAnnotation(ColumnValue.class) != null) {
                names.add(f.getName());
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
        Field[] fields1 = object.getClass().getDeclaredFields();

        for (Field f : fields1) {
            if (f.getAnnotation(KeyValue.class) != null) {
                continue;
            }
            if (f.getAnnotation(ColumnValue.class) != null) {
                Column column = makeColumn(timeStamp, f.getAnnotation(ColumnValue.class).nome(), object, f);
                if (column != null) {
                    columns.add(column);
                }
            } else if (f.getAnnotation(EmbeddedValue.class) != null) {

                columns.addAll(getColumns(ReflectionUtil.getMethod(object, f.getName())));
            } else if (f.getAnnotation(EnumeratedValue.class) != null) {
                Column column = new Column();
                column.setTimestamp(timeStamp);
                column.setName(EncodingUtil.stringToByte(f.getAnnotation(EnumeratedValue.class).nome()));


                ByteBuffer byteBuffer = new EnumWrite().getBytebyObject(ReflectionUtil.getMethod(object, f.getName()));
                column.setValue(byteBuffer);


                if (column != null) {
                    columns.add(column);
                }
            }

        }
        return columns;
    }

    protected Column makeColumn(long timeStamp, String coluna, Object object, Field f) {

        Object o = ReflectionUtil.getMethod(object, f.getName());
        if (o != null) {
            Column column = new Column();

            column.setTimestamp(timeStamp);
            column.setName(EncodingUtil.stringToByte(coluna));


            ByteBuffer byteBuffer = writeMap.get(f.getType().getName()).getBytebyObject(o);
            column.setValue(byteBuffer);

            return column;
        } else {
            return null;
        }
    }

//util to Update command
    protected List<String> prepareCQLtoUpdate(Object object) {

        List<String> strings = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field f : fields) {
            if (f.getAnnotation(KeyValue.class) != null) {
                continue;
            }
            if (f.getAnnotation(ColumnValue.class) != null) {
                if (ReflectionUtil.getMethod(object, f.getName()) == null) {
                    continue;
                }
                String string = " " + f.getName() + " = '" + ReflectionUtil.getMethod(object, f.getName()) + "' ";
                strings.add(string);

            } else if (f.getAnnotation(EmbeddedValue.class) != null) {

                strings.addAll(prepareCQLtoUpdate(ReflectionUtil.getMethod(object, f.getName())));
            } else if (f.getAnnotation(EnumeratedValue.class) != null) {
                Enum enumObject = (Enum) ReflectionUtil.getMethod(object, f.getName());
                Integer index = 0;
                Object[] enums = enumObject.getClass().getEnumConstants();
                for (int i = 0; i < enums.length; i++) {
                    if (enumObject.equals(enums[i])) {
                        index = i;
                        break;
                    }

                }

                String string = " " + f.getAnnotation(EnumeratedValue.class).nome() + " = '" + index + "' ";
                strings.add(string);


            }

        }
        return strings;
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
        for (Field f : fieldsAll) {

            if (f.getAnnotation(KeyValue.class) != null) {
                ByteBuffer bb = listMap.get("KEY");

                ReflectionUtil.setMethod(bean, f.getName(), readMap.get(f.getType().getName()).getObjectByByte(bb));
                continue;
            } else if (f.getAnnotation(ColumnValue.class) != null) {
                ByteBuffer bb = listMap.get(f.getAnnotation(ColumnValue.class).nome());
                if (bb != null) {
                    ReflectionUtil.setMethod(bean, f.getName(), readMap.get(f.getType().getName()).getObjectByByte(bb));
                }

            } else if (f.getAnnotation(EmbeddedValue.class) != null) {

                Object subObject = f.getType().newInstance();

                readObject(listMap, subObject);

                ReflectionUtil.setMethod(bean, f.getName(), subObject);
            } else if (f.getAnnotation(EnumeratedValue.class) != null) {

                ByteBuffer bb = listMap.get(f.getAnnotation(EnumeratedValue.class).nome());
                if (bb != null) {

                    Object[] enums = f.getType().getEnumConstants();
                    Integer index = (Integer) new EnumRead().getObjectByByte(bb);

                    ReflectionUtil.setMethod(bean, f.getName(), enums[index]);
                }

            }
        }
    }
}
