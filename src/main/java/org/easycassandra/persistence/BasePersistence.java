package org.easycassandra.persistence;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.cassandra.thrift.Column;
import org.easycassandra.annotations.ColumnFamilyValue;
import org.easycassandra.annotations.ColumnValue;
import org.easycassandra.annotations.EmbeddedValue;
import org.easycassandra.annotations.EnumeratedValue;
import org.easycassandra.annotations.IndexValue;
import org.easycassandra.annotations.KeyValue;
import org.easycassandra.annotations.read.EnumRead;
import org.easycassandra.annotations.read.ReadInterface;
import org.easycassandra.annotations.write.EnumWrite;
import org.easycassandra.annotations.write.WriteInterface;
import org.easycassandra.util.EncodingUtil;
import org.easycassandra.util.ReflectionUtil;

/**
 *
 * @author otavio
 */
class BasePersistence {

    /**
     * @param aLOCK_WRITE_DOCUMENT the LOCK_WRITE_DOCUMENT to set
     */
    public static void setLOCK_WRITE_DOCUMENT(AtomicBoolean aLOCK_WRITE_DOCUMENT) {
        LOCK_WRITE_DOCUMENT = aLOCK_WRITE_DOCUMENT;
    }
    /**
     * Key value of write for The Cassandra persistence
     * @see  WriteInterface
     */
    private Map<String, WriteInterface> writeMap;
    /**
     * Key Valeu of read for The Cassnadra persistence
     * @see ReadInterface
     */
    private Map<String, ReadInterface> readMap;
    /**
     * Class with information about Column Family managed by Easy-Cassandra
     */
    private AtomicReference<ColumnFamilyIds> referenciaSuperColunas;
    
    /**
     * field for lock or unlock for run 
     * the Thread
     */
     public  static  AtomicBoolean LOCK_WRITE_DOCUMENT= new AtomicBoolean(false);
    
    /**
     * Thread for write the id in the Document
     */
    private Thread writeDocumentThread;
    
    /**
     * name of keyspace where the Client is
     */
    private  String keyStore;
    

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
    @SuppressWarnings({ "rawtypes" })
	protected String getColumnFamilyName(Class object) {

        ColumnFamilyValue colunaFamilia = (ColumnFamilyValue) object.getAnnotation(ColumnFamilyValue.class);

        if (colunaFamilia != null) {
            return colunaFamilia.nome();
        }
        return null;
    }
    /**
     * verifies that the name of the annotation is empty
     * if you take the field name
     * @param field - field for viewd
     * @return The name inside annotations or the field's name
     */
    private String getColumnNanme(Field field) {
        return field.getAnnotation(ColumnValue.class).nome().equals("") ?field.getName():field.getAnnotation(ColumnValue.class).nome();
        
    }

    /**
     * verifies that the name of the annotation is empty
     * if you take the field name
     * @param field
     * @return The name inside annotations or the field's name
     */
    private String getEnumeratedName(Field field) {
        return field.getAnnotation(EnumeratedValue.class).nome().equals("") ?field.getName():field.getAnnotation(EnumeratedValue.class).nome();
        
    }

    /**
     * Get the Field of the Object from  annotation 
     * if there are not return will be null
     * @param object -  Class of the object viewed
     * @param annotation
     * @return 
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
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
    @SuppressWarnings({ "rawtypes" })
    protected Field getKeyField(Class persistenceClass) {

        return getField(persistenceClass, KeyValue.class);
    }

     /**
     *  Return the Field with the IndexValue Annotations
     * @see IndexValue
     * @param persistenceClass - Class of the object viewed
     * @return the Field if there are not will be return null
     */
    @SuppressWarnings({ "rawtypes" })
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
                id = referenciaSuperColunas.get().getId(colunaFamilia,keyStore);
              
                if(!LOCK_WRITE_DOCUMENT.get()){
                	LOCK_WRITE_DOCUMENT.set(true);
                	writeDocumentThread.start();
                }
              
                ReflectionUtil.setMethod(object, keyField, id);
            } else {
                id = (Long) ReflectionUtil.getMethod(object, keyField);
            }
            data = getWriteMap().get(keyField.getType().getName()).getBytebyObject(id);
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
    @SuppressWarnings({ "rawtypes" })
    protected DecoratorColumnNames columnNames(Class object) {

        try {
            return getColumnNames(object);
        } catch (IllegalAccessException | InstantiationException exception) {
  Logger.getLogger(BasePersistence.class.getName()).log(Level.SEVERE, null, exception);
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
    @SuppressWarnings({ "rawtypes" })
    protected DecoratorColumnNames getColumnNames(Class object) throws InstantiationException, IllegalAccessException {
        DecoratorColumnNames names = new DecoratorColumnNames();
        Field[] fields = object.getDeclaredFields();

        for (Field field : fields) {
            if (field.getAnnotation(KeyValue.class) != null) {
                continue;
            } else if (field.getAnnotation(ColumnValue.class) != null || field.getAnnotation(EnumeratedValue.class) != null) {
                names.add(field.getAnnotation(ColumnValue.class) != null ? getColumnNanme(field) : getEnumeratedName(field));
            } else if (field.getAnnotation(EmbeddedValue.class) != null) {
                 names.addAll(getColumnNames(field.getType()).getNames());
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
                
                Column column = makeColumn(timeStamp, getColumnNanme(field), object, field);
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
                column.setName(EncodingUtil.stringToByte(getEnumeratedName(field)));


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


            ByteBuffer byteBuffer = getWriteMap().get(field.getType().getName()).getBytebyObject(subObject);
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
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected List getList(List<Map<String, ByteBuffer>> colMap, Class persisteceClass) throws InstantiationException, IllegalAccessException {
        List lists = new ArrayList<>();
       

        for (Map<String, ByteBuffer> listMap : colMap) {
        	 Object bean = null;
             try {
                 bean = persisteceClass.newInstance();
             } catch (InstantiationException | IllegalAccessException exception) {
                 Logger.getLogger(BasePersistence.class.getName()).log(Level.SEVERE, null, exception);
             }
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

                ReflectionUtil.setMethod(bean, field, getReadMap().get(field.getType().getName()).getObjectByByte(byteBuffer));
                continue;
            } else if (field.getAnnotation(ColumnValue.class) != null) {
                
                ByteBuffer byteBuffer = listMap.get(getColumnNanme(field));
                if (byteBuffer != null) {
                    ReflectionUtil.setMethod(bean, field, getReadMap().get(field.getType().getName()).getObjectByByte(byteBuffer));
                }

            } else if (field.getAnnotation(EmbeddedValue.class) != null) {

                Object subObject = field.getType().newInstance();

                readObject(listMap, subObject);

                ReflectionUtil.setMethod(bean, field, subObject);
            } else if (field.getAnnotation(EnumeratedValue.class) != null) {

                ByteBuffer bb = listMap.get(getEnumeratedName(field));
                if (bb != null) {

                    Object[] enums = field.getType().getEnumConstants();
                    Integer index = (Integer) new EnumRead().getObjectByByte(bb);

                    ReflectionUtil.setMethod(bean, field, enums[index]);
                }

            }
        }
    }
    
    
      /**
     * the with synchronized for the KeySpace
     * @return
     */
	 public synchronized String getKeySpace() {
		return keyStore;
	}

    /**
     * @return the writeMap
     */
    public Map<String, WriteInterface> getWriteMap() {
        return writeMap;
    }

    /**
     * @return the readMap
     */
    public Map<String, ReadInterface> getReadMap() {
        return readMap;
    }

    /**
     * @param referenciaSuperColunas the referenciaSuperColunas to set
     */
    public void setReferenciaSuperColunas(AtomicReference<ColumnFamilyIds> referenciaSuperColunas) {
        this.referenciaSuperColunas = referenciaSuperColunas;
    }

    /**
     * @param writeDocumentThread the writeDocumentThread to set
     */
    public void setWriteDocumentThread(Thread writeDocumentThread) {
        this.writeDocumentThread = writeDocumentThread;
    }

    /**
     * @param keyStore the keyStore to set
     */
    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }
         
         
}
