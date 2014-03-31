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
/**
 * Base to create query builder on persistence Cassandra.
 * @author otaviojava
 */
public interface PersistenceBuilder {
    /**
     * create the selectbuilder.
     * {@link SelectBuilder}
     * @param classBean the class with column family
     * @param <T> kind of class
     * @return the builder
     */
    <T> SelectBuilder<T> selectBuilder(Class<T> classBean);
    /**
     * create the insert builder.
     * @param classBean the class
     * @param <T> the kind of class
     * @return the builder
     */
    <T> InsertBuilder<T> insertBuilder(Class<T> classBean);
    /**
     * create the insert builder with fields no null on the object.
      * @param classBean the class
     *  @param <T> the kind of class
     * @return the builder
     */
    <T> InsertBuilder<T> insertBuilder(T classBean);

    /**
     * create the insert builder with fields no null on the object.
      * @param classBean the class
     *  @param <T> the kind of class
     * @return the builder
     */
    <T> UpdateBuilder<T> updateBuilder(Class<T> classBean);
    /**
     * create the insert builder with fields no null on the object.
      * @param classBean the class
      * @param key the key
     *  @param <T> the kind of class
     * @return the builder
     */
    <T> UpdateBuilder<T> updateBuilder(Class<T> classBean, Object key);
    /**
     * create the delete builder with fields.
      * @param classBean the class
      * @param columnNames fields to remove
     *  @param <T> the kind of class
     * @return the builder
     */
    <T> DeleteBuilder<T> deleteBuilder(Class<T> classBean, String... columnNames);
    /**
     * create the delete builder by key.
     * @param classBean the class
      *@param columnNames fields to remove
      *@param key the key
     * @param <T> the kind of class
     * @param <K> the kind of key
     * @return the builder
     */
    <T, K> DeleteBuilder<T> deleteBuilder(Class<T> classBean, K key,
            String... columnNames);
    /**
     * create a batch builder.
     * @return batch builder
     */
    BatchBuilder batchBuilder();
}
