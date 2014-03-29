/*
 * Copyright 2014 Otávio Gonçalves de Santana (otaviojava)
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

import org.easycassandra.ClassInformation;
import org.easycassandra.ClassInformations;
import org.easycassandra.ClassInformation.KeySpaceInformation;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Update;
/**
 * simple implementation to Builders query.
 * @see BuilderPersistence.
 * @author otaviojava
 */
public class BuilderPersistenceImpl implements BuilderPersistence {

    private Session session;

    private String keySpace;

    private RunCassandraCommand command;

    /**
     * constructor.
     * @param session - the sesion
     * @param keySpace - the keyspace
     */
    public BuilderPersistenceImpl(Session session, String keySpace) {
        this.session = session;
        this.keySpace = keySpace;
        command = new RunCassandraCommand(keySpace);
    }

    @Override
    public <T> SelectBuilder<T> selectBuilder(Class<T> classBean) {
        return new SelectBuilderImpl<>(session,
                ClassInformations.INSTACE.getClass(classBean), keySpace);
    }

    @Override
    public <T> InsertBuilder<T> insertBuilder(Class<T> classBean) {
        ClassInformation classInformation = ClassInformations.INSTACE.getClass(classBean);
        KeySpaceInformation key = classInformation.getKeySpace(keySpace);
        Insert insert = QueryBuilder.insertInto(key.getKeySpace(), key.getColumnFamily());
        return new InsertBuilderImpl<>(insert, session, classInformation);
    }

    @Override
    public <T> InsertBuilder<T> insertBuilder(T classBean) {
        ClassInformation classInformation = ClassInformations.INSTACE
                .getClass(classBean.getClass());
        return new InsertBuilderImpl<>(command.createInsertStatment(classBean),
                session, classInformation);
    }

    @Override
    public <T> UpdateBuilder<T> updateBuilder(Class<T> classBean) {
        ClassInformation classInformation = ClassInformations.INSTACE.getClass(classBean);
        return new UpdateBuilderImpl<>(session, classInformation, keySpace, null);
    }

    @Override
    public <T> UpdateBuilder<T> updateBuilder(Class<T> classBean, Object key) {
        ClassInformation classInformation = ClassInformations.INSTACE.getClass(classBean);
        Update update = command.runUpdate(key, classBean);
        return new UpdateBuilderImpl<>(session, classInformation, keySpace,
                update);
    }

    @Override
    public <T> DeleteBuilder<T> deleteBuilder(Class<T> classBean,
            String... columnNames) {

        ClassInformation classInformation = ClassInformations.INSTACE.getClass(classBean);
        return new DeleteBuilderImpl<>(session, classInformation, keySpace,
                null, columnNames);
    }

    @Override
    public <T, K> DeleteBuilder<T> deleteBuilder(Class<T> classBean, K key,
            String... columnNames) {

        ClassInformation classInformation = ClassInformations.INSTACE
                .getClass(classBean);

        return new DeleteBuilderImpl<>(session, classInformation, keySpace,
                command.runDelete(key, classBean), columnNames);
    }

    @Override
    public BatchBuilder batchBuilder() {

        return new BatchBuilderImpl(session);
    }

}
