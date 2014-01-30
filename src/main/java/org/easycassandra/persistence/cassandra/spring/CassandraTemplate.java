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
 */
public interface CassandraTemplate {
    /**
     * saves the object.
     * @param entity
     *            the entity
     * @param <T>
     *            kind of class
     * @return the entity saved
     */
    <T> T save(T entity);

    /**
     * saves the objects with consistency level.
     * @param entity
     *            the entity
     * @param consistency
     *            the consistency
     * @param <T>
     *            kind of class
     * @return the entity saved
     */
    <T> T save(T entity, ConsistencyLevel consistency);

    /**
     * save a list of objects.
     * @param entities
     *            the entities
     * @param <T>
     *            kind of class
     * @return the entities saved
     */
    <T> Iterable<T> save(Iterable<T> entities);

    /**
     * saves a list of objects with consistency level.
     * @param entities
     *            the entities
     * @param consistency
     *            the consistency
     * @param <T>
     *            kind of class
     * @return the entities saved
     */
    <T> Iterable<T> save(Iterable<T> entities, ConsistencyLevel consistency);

    /**
     * delete an entity.
     * @param entity
     *            the entity
     * @param <T>
     *            kind of class
     */
    <T> void delete(T entity);

    /**
     * delete an entity with consistence level.
     * @param entity
     *            the entity
     * @param consistency
     *            the consistency
     * @param <T>
     *            kind of class
     */
    <T> void delete(T entity, ConsistencyLevel consistency);

    /**
     * delete an interable os entities.
     * @param entities
     *            the entities
     * @param <T>
     *            kind of class
     */
    <T> void delete(Iterable<T> entities);

    /**
     * delete an interable os entities with consistency level.
     * @param entities
     *            the entities
     * @param consistency
     *            the consistency
     * @param <T>
     *            kind of class
     */
    <T> void delete(Iterable<T> entities, ConsistencyLevel consistency);

    /**
     * delete an object from a key.
     * @param key
     *            the key
     * @param entity
     *            the entity
     * @param <K>
     *            kind of key
     */
    <K> void delete(K key, Class<?> entity);

    /**
     * delete an object from a key with consistency level.
     * @param key
     *            the key
     * @param entity
     *            the entity
     * @param consistency
     *            the consistency
     * @param <K>
     *            kind of key
     */
    <K> void delete(K key, Class<?> entity, ConsistencyLevel consistency);

    /**
     * delete objects from keys.
     * @param keys
     *            the keys
     * @param entity
     *            the entity
     * @param <K>
     *            kind of key
     * @param <T>
     *            kind of class
     */
    <K, T> void delete(Iterable<K> keys, Class<T> entity);

    /**
     * delete objects from keys with consistency level.
     * @param keys
     *            the keys
     * @param entity
     *            the entity
     * @param consistency
     *            the consitency
     * @param <K>
     *            kind of key
     * @param <T>
     *            kind of class
     */
    <K, T> void delete(Iterable<K> keys, Class<T> entity,
            ConsistencyLevel consistency);

    /**
     * Remove all objects in column family.
     * @param entity
     *            the entity
     * @param <T>
     *            kind of class
     */
    <T> void deleteAll(Class<T> entity);

    /**
     * update the object.
     * @param entity
     *            the entity
     * @param <T>
     *            kind of class
     * @return the object updated
     */
    <T> T update(T entity);

    /**
     * update the object.
     * @param entity
     *            the entity
     * @param consistency
     *            the consitency
     * @param <T>
     *            kind of class
     * @return the object updated
     */
    <T> T update(T entity, ConsistencyLevel consistency);

    /**
     * update these objects.
     * @param entities
     *            the entities
     * @param <T> kind of object
     * @return the entities updated
     */
    <T> Iterable<T> update(Iterable<T> entities);

    /**
     * update these objects with consistency level.
     * @param <T> kind of object
     * @param entities the entities
     * @param consistency the consistency
     * @return the entities updated
     */
    <T> Iterable<T> update(Iterable<T> entities, ConsistencyLevel consistency);

    /**
     * find one entity by key.
     * @param <T> kind of object
     * @param <K> kind of key
     * @param key the key
     * @param entity the entity
     * @return object by key, if does find will return null
     */
    <T, K> T findOne(K key, Class<T> entity);

    /**
     * find one entity by key.
     * @param <T> kind of object
     * @param <K> kind of key
     * @param key the key
     * @param entity the entity
     * @param consistency the consistency
     * @return object by key, if does find will return null
     */
    <T, K> T findOne(K key, Class<T> entity, ConsistencyLevel consistency);

    /**
     * return all objects.
     * @param <T> kind of object
     * @param <K> kind of key
     * @param keys the keys
     * @param entity the entity
     * @return find all.
     */
    <T, K> List<T> findAll(Iterable<K> keys, Class<T> entity);

    /**
     * return all objects.
     * @param <T> kind of object
     * @param <K> kind of key
     * @param keys the keys
     * @param entity the entity
     * @param consistency the consistency
     * @return find all.
     */
    <T, K> List<T> findAll(Iterable<K> keys, Class<T> entity,
            ConsistencyLevel consistency);

    /**
     * returns all entities.
     * @param <T> kind of object
     * @param entity the entity
     * @return all object on cassandra
     */
    <T> List<T> findAll(Class<T> entity);

    /**
     * returns all entities.
     * @param <T> kind of object
     * @param entity the entity
     * @param consistency the consistency
     * @return all object on cassandra
     */
    <T> List<T> findAll(Class<T> entity, ConsistencyLevel consistency);

    /**
     * return by index.
     * @param <T> kind of object
     * @param <I> kind of index
     * @param index the index
     * @param entity the entity
     * @param columnName the columnName
     * @return entities by index
     */
    <T, I> List<T> findByIndex(String columnName, I index, Class<T> entity);
    /**
     * return by index.
     * @param <T> kind of object
     * @param <I> kind of index
     * @param index the index
     * @param entity the entity
     * @param columnName the columnName
     * @param consistency the consistency
     * @return entities by index
     */
    <T, I> List<T> findByIndex(String columnName, I index, Class<T> entity,
            ConsistencyLevel consistency);

    /**
     * Find by key and annotated index.
     * @param key the key
     * @param index the index
     * @param entity the entity
     * @author Nenita Casuga
     * @param <T> kind of object
     * @param <I> kind of index
     * @since 10/31/2013
     * @return entities by index
     */
    <T, I> List<T> findByKeyAndIndex(Object key, I index, Class<T> entity);

    /**
     * Find by key and annotated index.
     * @param key the key
     * @param index the index
     * @param entity the entity
     * @author Nenita Casuga
     * @param <T> kind of object
     * @param <I> kind of index
     * @param consistency the
     * @since 10/31/2013
     * @return entities by index
     */
    <T, I> List<T> findByKeyAndIndex(Object key, I index, Class<T> entity,
            ConsistencyLevel consistency);

    /**
     * Find by key and annotated index.
     * @param key the key
     * @param entity the entity
     * @author Nenita Casuga
     * @param <T> kind of object
     * @param <I> kind of index
     * @param exclusive the exclusive
     * @param indexStart the indexStart
     * @param indexEnd the indexEnd
     * @since 10/31/2013
     * @return entities by index
     */
    <T, I> List<T> findByKeyAndIndexRange(Object key, I indexStart, I indexEnd,
            boolean exclusive, Class<T> entity);

    /**
     * Find by key and annotated index.
     * @param key the key
     * @param entity the entity
     * @author Nenita Casuga
     * @param <T> kind of object
     * @param <I> kind of index
     * @param exclusive the exclusive
     * @param indexStart the indexStart
     * @param indexEnd the indexEnd
     * @param consistency the consistency
     * @since 10/31/2013
     * @return entities by index
     */
    <T, I> List<T> findByKeyAndIndexRange(Object key, I indexStart, I indexEnd,
            boolean exclusive, Class<T> entity, ConsistencyLevel consistency);

    /**
     * verify if exist.
     * @param <T> kind of object
     * @param <K> kind of key
     * @param key the key
     * @param entity the entity
     * @return if exist
     */
    <K, T> boolean exist(K key, Class<T> entity);
    /**
     * verify if exist.
     * @param <T> kind of object
     * @param <K> kind of key
     * @param key the key
     * @param entity the entity
     * @param consistency the consistency
     * @return if exist
     */
    <K, T> boolean exist(K key, Class<T> entity, ConsistencyLevel consistency);

    /**
     * execute a query.
     * @param query the query
     */
    void executeUpdate(String query);

    /**
     * counts a number of rows in column family.
     * @param bean the bean
     * @param <T> kind of entity
     * @return the value of entities on column family
     */
    <T> long count(Class<T> bean);
    /**
     * counts a number of rows in column family.
     * @param bean the bean
     * @param <T> kind of entity
     * @param consistency the consistency
     * @return the value of entities on column family
     */
    <T> long count(Class<T> bean, ConsistencyLevel consistency);

}
