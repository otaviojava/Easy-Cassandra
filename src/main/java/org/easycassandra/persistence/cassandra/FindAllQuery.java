/*
 * Copyright 2013 Otávio Gonçalves de Santana (otaviojava)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.easycassandra.persistence.cassandra;

import java.util.LinkedList;
import java.util.List;

import org.easycassandra.ClassInformation;
import org.easycassandra.ClassInformation.KeySpaceInformation;
import org.easycassandra.ClassInformations;
import org.easycassandra.FieldInformation;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

/**
 * Mount and run the query to returns all data from column family.
 * @author otaviojava
 */
public class FindAllQuery {


    private String keySpace;

    /**
     * constructor.
     * @param keySpace
     *            the keyspace
     */
    public FindAllQuery(String keySpace) {
        this.keySpace = keySpace;
    }
    /**
     * list using select * from object.
     * @param bean the bean
     * @param session the sesion
     * @param consistency the consistency
     * @param <T> kind of class
     * @return the entities executing select * from
     */
    public <T> List<T> listAll(Class<T> bean, Session session,
            ConsistencyLevel consistency) {

        QueryBean byKeyBean = createQueryBean(bean, consistency);
        return RecoveryObject.INTANCE.recoverObjet(bean,
                session.execute(byKeyBean.select));
    }
    /**
     * create the {@link QueryBean}.
     * @param bean the bean
     * @param consistency the consistency
     * @param <T> kind of class
     * @return {@link QueryBean}
     */
    protected <T> QueryBean createQueryBean(Class<T> bean,
            ConsistencyLevel consistency) {
        ClassInformation classInformation = ClassInformations.INSTACE.getClass(bean);
        QueryBean byKeyBean = prepare(new QueryBean(), classInformation);
        KeySpaceInformation keySpaceInformation = classInformation.getKeySpace(keySpace);
        byKeyBean.select = QueryBuilder.select(byKeyBean.getArray()).from(
                keySpaceInformation.getKeySpace(), keySpaceInformation.getColumnFamily());
        byKeyBean.select.setConsistencyLevel(consistency);
        return byKeyBean;
    }

    /**
     * prepare {@link QueryBean}.
     * @param byKeyBean the byKeyBean
     * @param classInformation the class
     * @return {@link QueryBean} prepared
     */
    protected QueryBean prepare(QueryBean byKeyBean,
            ClassInformation classInformation) {

        for (FieldInformation field : classInformation.getFields()) {

            if (field.isEmbedded()) {
                byKeyBean = prepare(byKeyBean, field.getSubFields());
                continue;
            }
            byKeyBean.columns.add(field.getName());

        }
        return byKeyBean;
    }

    /**
     * Dto to QueryBean.
     * @author otaviojava
     */
    protected class QueryBean {
        private List<String> columns = new LinkedList<String>();
        private Select select;
        private FieldInformation searchField;

        public FieldInformation getSearchField() {
            return searchField;
        }

        public void setSearchField(FieldInformation searchField) {
            this.searchField = searchField;
        }

        public List<String> getColumns() {
            return columns;
        }

        public void setColumns(List<String> columns) {
            this.columns = columns;
        }

        public Select getSelect() {
            return select;
        }

        public void setSelect(Select select) {
            this.select = select;
        }

        String[] getArray() {
            return columns.toArray(new String[0]);
        }
    }
}
