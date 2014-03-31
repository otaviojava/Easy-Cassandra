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
package org.easycassandra.persistence.cassandra;

import java.util.List;

import com.datastax.driver.core.ConsistencyLevel;

/**
 * Base to cassandra databases, the interface has all resources that may use in
 * Cassandra database.
 * @author otaviojava
 */
public interface Persistence extends BasePersistence, PersistenceBuilder  {


    /**
     * execute select * from column family.
     * @param bean the bean
     * @param <T> kind of object
     * @return the result of the select
     */
    <T> List<T> findAll(Class<T> bean);
    /**
     * execute select * from column family.
     * @param bean the bean
     * @param <T> kind of object
     * @param consistency the consistency
     * @return the result of the select
     */
    <T> List<T> findAll(Class<T> bean, ConsistencyLevel consistency);
    /**
     * find objects by keys.
     * @param keys the keys
     * @param bean the bean
     * @param <T> kind of object
     * @param <K> kind of key
     * @return object by keys
     */
    <K, T> List<T> findByKeys(Iterable<K> keys, Class<T> bean);
    /**
     * find objects by keys.
     * @param keys the keys
     * @param bean the bean
     * @param <T> kind of object
     * @param <K> kind of key
     * @param consistency the consistency
     * @return object by keys
     */
    <K, T> List<T> findByKeys(Iterable<K> keys, Class<T> bean,
            ConsistencyLevel consistency);

    /**
     * find object by key.
     * @param key the key
     * @param bean the bean
     * @param <T> kind of object
     * @param <K> kind of key
     * @return object by keys
     */
    <K, T> T findByKey(K key, Class<T> bean);
    /**
     * find object by key.
     * @param key the key
     * @param bean the bean
     * @param <T> kind of object
     * @param <K> kind of key
     * @param consistency the consistency
     * @return object by keys
     */
    <K, T> T findByKey(K key, Class<T> bean, ConsistencyLevel consistency);

    /**
     * return by index.
     * @param indexName the indexName
     * @param <T> kind of object
     * @param <I> kind of index
     * @param index the index
     * @param bean the bean
     * @return entities by index
     */
    <T, I> List<T> findByIndex(String indexName, I index, Class<T> bean);
    /**
     * return by index.
     * @param <T> kind of object
     * @param <I> kind of index
     * @param index the index
     * @param bean the bean
     * @param indexName the indexName
     * @param consistency the consistency
     * @return entities by index
     */
    <T, I> List<T> findByIndex(String indexName, I index, Class<T> bean,
            ConsistencyLevel consistency);
    /**
     * return by index.
     * @param <T> kind of object
     * @param <I> kind of index
     * @param index the index
     * @param bean the bean
     * @return entities by index
     */
    <T, I> List<T> findByIndex(I index, Class<T> bean);
    /**
     * return by index.
     * @param <T> kind of object
     * @param <I> kind of index
     * @param index the index
     * @param bean the bean
     * @param consistency the consistency
     * @return entities by index
     */
    <T, I> List<T> findByIndex(I index, Class<T> bean,
            ConsistencyLevel consistency);

    /**
     * Find by key and annotated index.
     * @param key the key
     * @author Nenita Casuga
     * @param <T> kind of object
     * @param index the index
     * @param bean the bean
     * @param <I> kind of index
     * @since 10/31/2013
     * @return entities by index
     */
    <T, I> List<T> findByKeyAndIndex(Object key, I index, Class<T> bean);

    /**
     * Find by key and annotated index.
     * @author Nenita Casuga
     * @param <T> kind of object
     * @param <I> kind of index
     * @param indexStart the indexStart
     * @param indexEnd the indexEnd
     * @param id the id
     * @param bean the bean
     * @param inclusive the inclusive
     * @since 10/31/2013
     * @return entities by index
     */
    <T, I> List<T> findByKeyAndIndexRange(Object id, Object indexStart,
            Object indexEnd, boolean inclusive, Class<T> bean);
    /**
     * counts a number of rows in column family.
     * @param bean the bean
     * @param <T> kind of entity
     * @return the value of entities on column family
     */
    <T> Long count(Class<T> bean);
    /**
     * counts a number of rows in column family.
     * @param bean the bean
     * @param <T> kind of entity
     * @param consistency the consistency
     * @return the value of entities on column family
     */
    <T> Long count(Class<T> bean, ConsistencyLevel consistency);
}

