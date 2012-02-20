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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.cassandra.thrift.Cassandra.Client;
import org.apache.cassandra.thrift.*;
import org.apache.thrift.TException;
import org.easycassandra.ConsistencyLevelCQL;
import org.easycassandra.EasyCassandraException;
import org.easycassandra.annotations.read.UTF8Read;
import org.easycassandra.util.EncodingUtil;
import org.easycassandra.util.ReflectionUtil;


/**
 * main Class for persistence The Objects
 * @author otaviojava - otaviojava@java.net
 */
public class Persistence extends BasePersistence {

   /**
    * the value default for list the result
    */
	private static final int DEFAULT_VALUE = 10000;
	/**
	 * Client's Cassandra
	 */
    private Client client = null;
    

    /**
     * Constructor for Persistence
     * @param client
     * @param referenciaSuperColunas
     * @param keyStore
     */
    Persistence(Client client, AtomicReference<ColumnFamilyIds> referenciaSuperColunas,String keyStore) {
        this.client = client;
        setReferenciaSuperColunas(referenciaSuperColunas);
        setKeyStore(keyStore);
        setWriteDocumentThread(new Thread(new WriteDocument(getLockWrite(), referenciaSuperColunas)));
    }

    /**
     * method for retrieve Objects
     * @param condiction -condiction for search the Objects can be index and rowkey
     * @param condictionValue - value for seach in condiction
     * @param persistenceClass - type object class
     * @param consistencyLevel level consistency for retrive the information
     * @param limit length max the list
     * @return the list retrieved with length (limit)
    
     */
  
	protected List retriveObject(String condiction, String condictionValue,  Class persistenceClass, ConsistencyLevelCQL consistencyLevel, int limit) {
        try {
            StringBuilder cql = new StringBuilder();

            cql.append("select * from ");    
            cql.append(getColumnFamilyName(persistenceClass));
            cql.append(" USING ").append(consistencyLevel.getValue()).append(" ");   //padra One
            cql.append(" where ");
            cql.append(" ").append(condiction).append(" =");
            cql.append("'");
            cql.append(condictionValue);
            cql.append("' ");
            cql.append("LIMIT ").append(limit);//default 10000


            CqlResult executeCqlQuery = executeCQL(cql.toString());
            return listbyQuery(executeCqlQuery, persistenceClass);
        } catch (Exception exception) {
         Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, exception);
         return new ArrayList<>();
        }
       
        
    }

    //insert commands
    /**
     * the method for insert the Object
     * @param object - the  Object for be insert in Cassandra 
     * the default  of consistency Level is ONE (ConsistencyLevel.ONE)
     * @return the result of insertion
     * @see #insert(java.lang.Object, org.apache.cassandra.thrift.ConsistencyLevel) 
     */
    public boolean  insert(Object object) {
        return insert(object, ConsistencyLevel.ONE);
    }

    /**
     * the method for insert the Object
     * @param object - the  Object for be insert in Cassandra 
     * @param consistencyLevel - Level consistency for be insering in Thrift
     * @return the result of insertion
     */
    public boolean  insert(Object object, ConsistencyLevel consistencyLevel) {
        return insert(object, consistencyLevel, true);
    }

    /**
     * the method for insert the Object
     * @param object - the  Object for be insert in Cassandra 
     * @param consistencyLevel -Level consistency for be insering in Thrift
     * @param autoEnable - if use the auto_increment or not
     * @return the result of insertion
     */
    public boolean  insert(Object object, ConsistencyLevel consistencyLevel, boolean autoEnable) {
        try {


            ByteBuffer rowid = getKey(object, autoEnable);

            ColumnParent columnParent = new ColumnParent(getColumnFamilyName(object.getClass()));

            for (Column column : getColumns(object)) {

                client.insert(rowid, columnParent, column, consistencyLevel);

            }
        } catch (Exception exception) {
            Logger logger=Logger.getLogger(Persistence.class.getName());
            logger.log(Level.SEVERE, null, exception);
            if(createColumnFamily(exception,logger)){
            	logger.log(Level.INFO, "Column Family created with success, now trying execute the command again");
            	return insert(object,consistencyLevel,autoEnable);
            }
            return false;
        }
        return true;
    }
    
    /**
     * the method for insert the Object
     * @param object - the  Object for be insert in Cassandra 
     * @param consistencyLevel - Level consistency for be insering
     * @return the result of insertion
     */
    public boolean  insert(Object object, ConsistencyLevelCQL consistencyLevel) {
    	return insert(object, consistencyLevel.toConsistencyLevel(), true);
    }
    
    /**
     * the method for insert the Object
     * @param object - the  Object for be insert in Cassandra 
     * @param consistencyLevel -Level consistency for be insering
     * @param autoEnable - if use the auto_increment or not
     * @return the result of insertion
     */
    public boolean  insert(Object object, ConsistencyLevelCQL consistencyLevel, boolean autoEnable) {
    	return insert(object, consistencyLevel.toConsistencyLevel(), autoEnable);
    }
    

    /**
     * @param cql - Cassandra Query Language 
     * @return  the result executing query
     */
    public CqlResult executeCQL(String cql) {
        try {
            return client.execute_cql_query(ByteBuffer.wrap(cql.getBytes()), Compression.NONE);
        } catch (InvalidRequestException | UnavailableException | TimedOutException | SchemaDisagreementException | TException exception) {
        Logger logger=Logger.getLogger(Persistence.class.getName());
        logger.log(Level.SEVERE, null, exception);
        
        if(createColumnFamily(exception,logger)){
        	logger.log(Level.WARNING, "Column Family created with success, now trying execute the command again");
        	return executeCQL(cql);
        }
        
        
        }
        return null;
    }

    /**
     * the method for retrieve the object in Cassandra
     * @param persistenceClass - type of class for be retrieve
     * the default  of consistency Level is ONE (ConsistencyLevel.ONE)
     * The default lenght list is 10.000
     * @see  #findAll(java.lang.Class, org.easycassandra.ConsistencyLevelCQL, int) 
     * @return the list with Object is retrive
     */
    public List findAll(Class persistenceClass) {
        return findAll(persistenceClass, ConsistencyLevelCQL.ONE, DEFAULT_VALUE);
    }

    /**
     * the method for retrieve the object in Cassandra
     * @param persistenceClass - type of class for be retrieve
     * @param limit - lenght the list
     * The default  of consistency Level is ONE (ConsistencyLevel.ONE)
     * @see  #findAll(java.lang.Class, org.easycassandra.ConsistencyLevelCQL, int) 
     * @return the list with Object is retrive
     */    
    
	public List findAll(Class persistenceClass, int limit) {
        return findAll(persistenceClass, ConsistencyLevelCQL.ONE, limit);
    }

    /**
     * the method for retrieve the object in Cassandra
     * @param persistenceClass -  type of class for be retrieve
     * @param consistencyLevel - Level of consitency for retrive the Object
     * @see  #findAll(java.lang.Class, org.easycassandra.ConsistencyLevelCQL, int) 
     * @return the list with Object is retrive
     */
    public List findAll(Class persistenceClass, ConsistencyLevelCQL consistencyLevel) {
        return findAll(persistenceClass, consistencyLevel, DEFAULT_VALUE);
    }

    /**
     * the method for retrieve the object in Cassandra
     * @param persistenceClass -  type of class for be retrieve
     * @param consistencyLevel - Level of consitency for retrive the Object
     * @param limit - lenght the list
     * @return the list with Object is retrive
     */
    public List findAll(Class persistenceClass, ConsistencyLevelCQL consistencyLevel, int limit) {
        

        try {

            StringBuilder cql = new StringBuilder();
            cql.append(" select  * ");
            cql.append(" from ");
            cql.append(getColumnFamilyName(persistenceClass));
            cql.append(" USING ").append(consistencyLevel.getValue()).append(" ");   //padra One
            cql.append("LIMIT ").append(limit);//padrao 10000
            CqlResult executeCqlQuery = executeCQL(cql.toString());
            return listbyQuery(executeCqlQuery, persistenceClass);
        } catch (Exception exception) {
            Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, exception);
            
        }

        return new ArrayList<>();
    }

    /**
     * The method for retrive the object from the result of Cassandra Query Language
     * @param resultCQL - The result of Cassandra Query Language
     * @param persistenceClass - The kind for retrieve  the Class
     * @return The result of List
     * @throws NumberFormatException
     * @throws InstantiationException
     * @throws IllegalAccessException 
     */
    public List listbyQuery(CqlResult resultCQL, Class persistenceClass) throws InstantiationException, IllegalAccessException  {
        List<Map<String, ByteBuffer>> listMap = new ArrayList<>();

        for (CqlRow row : resultCQL.rows) {
            Map<String, ByteBuffer> mapColumn = new HashMap<>();

            for (Column cl : row.getColumns()) {

                mapColumn.put(EncodingUtil.byteToString(cl.name), cl.value);

            }
            listMap.add(mapColumn);
        }

        return getList(listMap, persistenceClass);

    }

    /**
     * The method for retrive a object from rowkey
     * @param key - The value of rowkey
     * @param persistenceClass - The kind class
     * The default  of consistency Level is ONE (ConsistencyLevel.ONE)
     * @return - The object from key
     * @see #findByKey(java.lang.Object, java.lang.Class, org.easycassandra.ConsistencyLevelCQL) 
     */
    public Object findByKey(Object key, Class persistenceClass) {
        return findByKey(key, persistenceClass, ConsistencyLevelCQL.ONE);
    }

    /**
     * The method for retrive a object from rowkey
     * @param key - The value of rowkey
     * @param persistenceClass - The kind class
     * @param consistencyLevel - The consistency Level
     * @return - The object from key
     */
    public Object findByKey(Object key, Class persistenceClass, ConsistencyLevelCQL consistencyLevel) {
        int limit = 1;
        

           
        ByteBuffer keyBuffer = getWriteManager().convert(key);
        String keyString = new UTF8Read().getObjectByByte(keyBuffer).toString();
        String condicao = "KEY";

	List objects =  retriveObject(condicao, keyString, persistenceClass, consistencyLevel, limit);
        if (objects.size() > 0) {
            return objects.get(0);
        }
        return null;


    }

    //delete comand
    /**
     * Delete the Object from the key value
     * @param keyValue - The key value
     * @param objectClass  - The Kind of class
     * @return the result of deletion
     */
    public boolean  deleteByKeyValue(Object keyValue, Class objectClass) {
    	
        ByteBuffer keyBuffer = getWriteManager().convert(keyValue);
        String keyString = new UTF8Read().getObjectByByte(keyBuffer).toString();


     return   runDeleteCqlCommand(keyString, objectClass);
    }

    /**
     * Delete the Object from the key value
     * @param keyObject - The Object for be delete
     * @return the result of deletion
     */
    public boolean  delete(Object keyObject) {
        Field keyField = getKeyField(keyObject.getClass());
        ByteBuffer keyBuffer = getWriteManager().convert(ReflectionUtil.getMethod(keyObject, keyField));
        String keyString = new UTF8Read().getObjectByByte(keyBuffer).toString();

      return  runDeleteCqlCommand(keyString, keyObject.getClass());


    }

    /**
     * Create the cql and remove the Object 
     * @param keyValue - The value for row key
     * @param persistenceClass - The kind object
     * @return the result of deletion
     */
    protected boolean  runDeleteCqlCommand(String keyValue, Class persistenceClass) {

        StringBuilder cql = new StringBuilder();
        cql.append("delete ");
        cql.append(" from ");
        cql.append(getColumnFamilyName(persistenceClass));
        cql.append(" where KEY = '");
        cql.append(keyValue);
        cql.append("'");
        CqlResult cqlResult = executeCQL(cql.toString());
        return cqlResult!=null;
    }

    //find index
    /**
     * Find list objects from index
     * @param index - the index value
     * @param objectClass - Kind the Object
     * the default  of consistency Level is ONE (ConsistencyLevel.ONE)
     * The default lenght list is 10.000
     * @see #findByIndex(java.lang.Object, java.lang.Class, org.easycassandra.ConsistencyLevelCQL, int) 
     * @return list retrieve from the value index
     * 
     */
    public List findByIndex(Object index, Class objectClass) {
        return findByIndex(index, objectClass, ConsistencyLevelCQL.ONE);
    }

    /**
     * Find list objects from index
     * @param index - the index value
     * @param objectClass - Kind the Object
     * @param consistencyLevel - The consistency Level
     * @see #findByIndex(java.lang.Object, java.lang.Class, org.easycassandra.ConsistencyLevelCQL, int) 
     * The default lenght list is 10.000
     * @return list retrieve from the value index
     */
    public List findByIndex(Object index, Class objectClass, ConsistencyLevelCQL consistencyLevel) {
        return findByIndex(index, objectClass, consistencyLevel, DEFAULT_VALUE);
    }

    /**
     * Find list objects from index
     * @param index - the index value
     * @param objectClass - kind the Object
     * @param consistencyLevelCQL  - The consistency Level
     * @param limit - The length of List
     * @return  list retrieve from the value index
     */
    public List findByIndex(Object index, Class objectClass, ConsistencyLevelCQL consistencyLevelCQL, int limit) {

        String indexString = index.toString();
       Field indexField= getIndexField(objectClass);
       if(indexField==null){
    	   
    	  try{
    	   throw new EasyCassandraException("You must use annotation @org.easycassandra.annotations.IndexValue in some field of the Class:" +objectClass.getName()+ " for list by index ");
    	  }catch (EasyCassandraException exception) {
    		  Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, exception);
    		  return null;
		}
    	 
       }
        		
        String condicao = getColumnName(indexField);
        return retriveObject(condicao, indexString,  objectClass, consistencyLevelCQL, limit);



    }

    /**
     * Update the Object
     * The default  of consistency Level is ONE (ConsistencyLevel.ONE)
     * @see #update(java.lang.Object, org.easycassandra.ConsistencyLevelCQL) 
     * @param object = The Object will updated
     * @return the result of update data 
     */
    public boolean  update(Object object) {
        return update(object, ConsistencyLevel.ONE);
    }

    /**
     * Update the Object
     * @param object - The Object will updated
     * @param consistencyLevel - Th consistency Level
     * @return the result of update data 
     */
    public boolean  update(Object object, ConsistencyLevel consistencyLevel) {
         return insert(object, consistencyLevel, true);
    }

	public Long count(Class<?> clazz) {
		 StringBuilder cql = new StringBuilder();
	        cql.append("SELECT count(*) ");
	        cql.append(" from ");
	        cql.append(getColumnFamilyName(clazz));
	        
	        CqlResult cqlResult = executeCQL(cql.toString());
	        return cqlResult.rows.get(0).getColumns().get(0).value.asLongBuffer().get();
	        
	}

  // treatment exception
	
	/**
	 * 
	 * @param exception
	 * @return
	 */
    private boolean createColumnFamily(Exception exception, Logger logger) {
    	logger.log(Level.WARNING, "Verify if exist and trying create the Column Family");
		if (exception instanceof InvalidRequestException&&((InvalidRequestException) exception).getWhy().contains("unconfigured columnfamily ")) {
		
                    logger.log(Level.INFO, "not exist column Family, now try make it");
                    String columnFamily = ((InvalidRequestException) exception).getWhy().replace("unconfigured columnfamily ", "");
                    String query = " CREATE COLUMNFAMILY " + columnFamily
                            + " (KEY text PRIMARY KEY); ";
                    return executeCQL(query) != null;
		
		}

		return false;

	}
}
