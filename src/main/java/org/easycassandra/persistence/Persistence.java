/*
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

import java.util.List;

/**
 * The main interface with the basics commands for nosql databases, which has in
 * common among databases.
 * 
 * @author otaviojava - otaviojava@java.net
 */

public interface Persistence {

    boolean insert(Object bean);

    boolean delete(Object bean);

    boolean update(Object bean);

    <T> List<T> findAll(Class<T> bean);

    <T> T findByKey(Object key, Class<T> bean);

    boolean deleteByKey(Object key, Class<?> bean);

    boolean executeUpdate(String query);

}
