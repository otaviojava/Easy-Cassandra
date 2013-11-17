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

import com.datastax.driver.core.ConsistencyLevel;



/**
 * Base to cassandra databases, the interface has all resources that may use in
 * Cassandra database.
 * @author otaviojava
 *
 */
public interface CassandraTemplate{
         /**
         * saves the object.
         * @param entity
         * @return
         */
	    <T> T save(T entity);
	    /**
	     * saves the objects with consistency level.
	     * @param entity
	     * @param consistency
	     * @return
	     */
	    <T> T save(T entity,ConsistencyLevel consistency);

	    /**
	     * save a list of objects.
	     * @param entities
	     * @return
	     */
	    <T> Iterable<T> save(Iterable<T> entities);

	    /**
	     * saves a list of objects with consistency level
	     * @param entities
	     * @param consistency
	     * @return
	     */
	    <T> Iterable<T> save(Iterable<T> entities,ConsistencyLevel consistency);

	    /**
	     * delete an entity.
	     * @param entity
	     */
	    <T> void delete(T entity);

	    /**
	     * delete an entity with consistence level.
	     * @param entity
	     * @param consistency
	     */
	    <T> void delete(T entity,ConsistencyLevel consistency);

	    /**
	     * delete an interable os entities.
	     * @param entities
	     */
	    <T> void delete(Iterable<T> entities);

	    /**
	     * delete an interable os entities with consistency level
	     * @param entities
	     * @param consistency
	     */
	    <T> void delete(Iterable<T> entities,ConsistencyLevel consistency);

	    /**
	     * delete an object from a key.
	     * @param key
	     * @param entity
	     */
	    <K> void delete(K key, Class<?> entity);

	    /**
	     * delete an object from a key with consistency level.
	     * @param key
	     * @param entity
	     * @param consistency
	     */
	    <K> void delete(K key, Class<?> entity,ConsistencyLevel consistency);

	    /**
	     * delete objects from keys.
	     * @param keys
	     * @param entity
	     */
	    <K,T> void delete(Iterable<K>  keys, Class<T> entity);

	    /**
	     * delete objects from keys with consistency level.
	     * @param keys
	     * @param entity
	     * @param consistency
	     */
	    <K,T> void delete(Iterable<K>  keys, Class<T> entity,ConsistencyLevel consistency);

	    /**
	     * Remove all objects in column family.
	     * @param entity
	     */
	    <T> void deleteAll(Class<T> entity);


	    /**
	     * update the object.
	     * @param entity
	     * @return
	     */
	    <T> T update(T entity);

	    /**
	     * update the object.
	     * @param entity
	     * @param consistency
	     * @return
	     */
	    <T> T update(T entity,ConsistencyLevel consistency);


	    /**
	     * update these objects.
	     * @param entities
	     * @return
	     */
	    <T> Iterable<T> update(Iterable<T> entities);

	    /**
	     * update these objects with consistency level.
	     * @param entities
	     * @return
	     */
	    <T> Iterable<T> update(Iterable<T> entities,ConsistencyLevel consistency);


	    /**
	     * find one entity by key.
	     * @param key
	     * @param entity
	     * @return
	     */
	    <T,K> T findOne(K key, Class<T> entity);
	    /**
	     * find one entity by key.
	     * @param key
	     * @param entity
	     * @param consistency
	     * @return
	     */
	    <T,K> T findOne(K key, Class<T> entity,ConsistencyLevel consistency);

	    /**
	     * return all objects.
	     * @param keys
	     * @param entity
	     * @return
	     */
	    <T,K> List<T> findAll(Iterable<K> keys, Class<T> entity);

	    <T,K> List<T> findAll(Iterable<K> keys, Class<T> entity,ConsistencyLevel consistency);

	    /**
	     * returns all entities.
	     * @param entity
	     * @return
	     */
	    <T> List<T> findAll(Class<T> entity);

	    <T> List<T> findAll(Class<T> entity,ConsistencyLevel consistency);

	    /**
	     * return by index.
	     * @param index
	     * @param entity
	     * @return
	     */
	    <T,I> List<T> findByIndex(String columnName,I index,Class<T> entity);

	    <T,I> List<T> findByIndex(String columnName,I index,Class<T> entity,ConsistencyLevel consistency);

        /**
        * Find by key and annotated index.
        * @author Nenita Casuga
        * @since 10/31/2013
        */
        <T,I> List<T> findByKeyAndIndex(String key, I index,Class<T> entity);

        /**
        * Find by key and annotated index
        * @author Nenita Casuga
        * @since 10/30/2013
        */
        <T,I> List<T> findByKeyAndIndex(String key, I index,Class<T> entity,ConsistencyLevel consistency);

        /**
        * Find by key and annotated index.
        * @author Nenita Casuga
        * @since 10/31/2013
        */
        <T,I> List<T> findByKeyAndIndexRange(String key, I indexStart, I indexEnd, boolean exclusive,Class<T> entity);

        /**
        * Find by key and annotated index.
        * @author Nenita Casuga
        * @since 10/30/2013
        */
        <T,I> List<T> findByKeyAndIndexRange(String key, I indexStart, I indexEnd, boolean exclusive, Class<T> entity,ConsistencyLevel consistency);

        /**
	     * verify if exist.
	     * @param key
	     * @param entity
	     * @return
	     */
	    <K,T>boolean exist(K key, Class<T> entity);

	    <K,T>boolean exist(K key, Class<T> entity,ConsistencyLevel consistency);

	    /**
	     * execute a query.
	     * @param query
	     */
	    void executeUpdate(String query);

	    /**
	     * counts a number of rows in column family
	     * @param bean
	     * @return
	     */
	    <T>long count(Class<T> bean);

	    <T>long count(Class<T> bean,ConsistencyLevel consistency);

}
