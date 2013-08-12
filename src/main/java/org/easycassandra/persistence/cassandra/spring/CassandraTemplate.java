/*
 * Copyright 2013 Otávio Gonçalves de Santana (otaviojava)
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
package org.easycassandra.persistence.cassandra.spring;

import java.util.List;



/**
 * Base to cassandra databases, the interface has all resources that may use in
 * Cassandra database
 * 
 * @author otaviojava
 * 
 */
public interface CassandraTemplate{
		/**
		 * saves the object
		 * @param entity
		 * @return
		 */
	    <T> T save(T entity);
	    
	    /**
	     * save a list of objects
	     * @param entities
	     * @return
	     */
	    <T> Iterable<T> save(Iterable<T> entities);
	    
	    
	    /**
	     * delete an entity
	     * @param entity
	     */
	    <T> void delete(T entity);
	    
	    /**
	     * delete an interable os entities
	     * @param entities
	     */
	    <T> void delete(Iterable<T> entities);
	    
	    /**
	     * delete an object from a key
	     * @param key
	     * @param entity
	     */
	    <K> void delete(K key, Class<?> entity);
	    
	    /**
	     * delete objects from keys
	     * @param keys
	     * @param entity
	     */
	    <K,T> void delete(Iterable<K>  keys, Class<T> entity);

	    /**
	     * Remove all objects in column family
	     * @param entity
	     */
	    <T> void deleteAll(Class<T> entity);
	    

	    /**
	     * update the object
	     * @param entity
	     * @return
	     */
	    <T> T update(T entity);
	    
	    
	    /**
	     * update these objects
	     * @param entities
	     * @return
	     */
	    <T> Iterable<T> update(Iterable<T> entities);
	    
	    
	    /**
	     * find one entity by key
	     * @param key
	     * @param entity
	     * @return
	     */
	    <T,K> T findOne(K key, Class<T> entity);
	    
	    /**
	     * return all objects
	     * @param keys
	     * @param entity
	     * @return
	     */
	    <T,K> List<T> findAll(Iterable<K> keys, Class<T> entity);
	    
	    /**
	     * returns all entities
	     * @param entity
	     * @return
	     */
	    <T> List<T> findAll(Class<T> entity);
	    
	    /**
	     * return by index
	     * @param index
	     * @param entity
	     * @return
	     */
	    <T,I> List<T> findByIndex(I index,Class<T> entity);
	    
	    /**
	     * verify if exist
	     * @param key
	     * @param entity
	     * @return
	     */
	    <K,T>boolean exist(K key, Class<T> entity);

	    /**
	     * execute a query
	     * @param query
	     */
	    void executeUpdate(String query);
	    
	    /**
	     * counts a number of rows in column family
	     * @param bean
	     * @return
	     */
	    <T>long count(Class<T> bean);
   
}
