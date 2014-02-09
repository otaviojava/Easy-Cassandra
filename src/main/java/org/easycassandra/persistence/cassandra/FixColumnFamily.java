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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.easycassandra.ClassInformation;
import org.easycassandra.ClassInformations;
import org.easycassandra.FieldInformation;
import org.easycassandra.FieldJavaNotEquivalentCQLException;
import org.easycassandra.KeyProblemsException;
import org.easycassandra.persistence.cassandra.AddColumnUtil.AddColumn;
import org.easycassandra.persistence.cassandra.VerifyRowUtil.VerifyRow;

import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.InvalidQueryException;

/**
 * Class to fix a column family.
 * @author otaviojava
 */
class FixColumnFamily {

    private static final String QUERY_PRIMARY_KEY = " PRIMARY KEY (";

    /**
     * verify if exist column family and try to create.
     * @param session
     *            - bridge to cassandra data base
     * @param familyColumn
     *            - name of family column
     * @param class1
     *            - bean
     * @return - if get it or not
     */
    public boolean verifyColumnFamily(Session session, String familyColumn,
            Class<?> class1) {
        try {
            ResultSet resultSet = session.execute("SELECT * FROM " + familyColumn + " LIMIT 1");
            verifyRowType(resultSet, class1, session);
            findIndex(class1, session);
            return true;
        } catch (InvalidQueryException exception) {

            if (exception.getCause().getMessage().contains("unconfigured columnfamily ")) {
                Logger.getLogger(FixColumnFamily.class.getName()).info(
                        "Column family doesn't exist, try to create");
                createColumnFamily(familyColumn, class1, session);
                findIndex(class1, session);
                return true;
            }
        }

        return false;
    }

    /**
     * Column family exists verify.
     * @param resultSet
     */
    private void verifyRowType(ResultSet resultSet, Class<?> class1, Session session) {
        Map<String, String> mapNameType = new HashMap<String, String>();
        for (Definition column : resultSet.getColumnDefinitions()) {
            mapNameType
                    .put(column.getName(), column.getType().getName().name());
        }
        ClassInformation classInformation = ClassInformations.INSTACE.getClass(class1);
        verifyRow(classInformation, session, mapNameType);
    }

    /**
     * verify relationship beteween Java and CQL type.
     * @param class1
     * @param session
     * @param mapNameType
     */
    private void verifyRow(ClassInformation classInformation, Session session,
            Map<String, String> mapNameType) {

        for (FieldInformation fieldInformation : classInformation.getFields()) {

            if (fieldInformation.isKeyCheck()) {
                continue;
            } else if (fieldInformation.isEmbedded() && !fieldInformation.isKeyCheck()) {
                verifyRow(fieldInformation.getSubFields(), session, mapNameType);
                continue;
            }

            String cqlType = mapNameType.get(fieldInformation.getName().toLowerCase());
            if (cqlType == null) {
                executeAlterTableAdd(classInformation, session, fieldInformation);
                continue;
            }

            VerifyRow verifyRow = VerifyRowUtil.INTANCE.factory(fieldInformation);
            List<String> cqlTypes = verifyRow.getTypes(fieldInformation.getField());

            if (!cqlTypes.contains(cqlType.toLowerCase())) {
                createMessageErro(classInformation, fieldInformation, cqlType);
            }

        }
    }

    /**
     * call the command to alter table adding a field.
     * @param class1
     *            - bean within column family
     * @param session
     *            - bridge to cassandra
     * @param field
     *            - field to add in column family
     */
    private void executeAlterTableAdd(ClassInformation classInformation, Session session,
            FieldInformation field) {
        StringBuilder cqlAlterTable = new StringBuilder();
        cqlAlterTable.append("ALTER TABLE ").append(classInformation.getNameSchema());
        cqlAlterTable.append(" ADD ");
        AddColumn addColumn = AddColumnUtil.INSTANCE.factory(field);
        cqlAlterTable.append(addColumn.addRow(field, RelationShipJavaCassandra.INSTANCE));
        cqlAlterTable.deleteCharAt(cqlAlterTable.length() - 1);
        cqlAlterTable.append(";");
        session.execute(cqlAlterTable.toString());
    }

    /**
     * Field Java isn't equivalents with CQL type create error mensage.
     * @param classInformation
     * @param field
     * @param cqlTypes
     */
    private void createMessageErro(ClassInformation classInformation,
            FieldInformation field, String cqlType) {
        StringBuilder erroMensage = new StringBuilder();
        erroMensage.append("In the objetc ").append(classInformation.getClass().getName());
        erroMensage.append(" the field ").append(field.getName());
        erroMensage.append(" of the type ").append(field.getField().getType().getName());
        erroMensage.append(" isn't equivalent with CQL type ").append(cqlType);
        erroMensage.append(" was expected: ").append(
                RelationShipJavaCassandra.INSTANCE.getJavaValue(cqlType
                        .toLowerCase()));
        throw new FieldJavaNotEquivalentCQLException(erroMensage.toString());
    }

    /**
     * Column family doen'snt exist create with this method.
     * @param familyColumn
     *            - name of column family
     * @param class1
     *            - bean
     * @param session
     *            bridge of cassandra data base
     */
    private void createColumnFamily(String familyColumn, Class<?> class1,
            Session session) {
        StringBuilder cqlCreateTable = new StringBuilder();
        cqlCreateTable.append("create table ");

        cqlCreateTable.append(familyColumn).append("( ");
        ClassInformation classInformation = ClassInformations.INSTACE.getClass(class1);
        createQueryCreateTable(classInformation, cqlCreateTable);
        if (classInformation.isComplexKey()) {
            addComlexID(classInformation, cqlCreateTable);
        } else {
            addSimpleId(classInformation, cqlCreateTable);
        }

        session.execute(cqlCreateTable.toString());
    }

    /**
     * add in the query a simple id.
     * @param class1
     * @param cqlCreateTable
     */
    private void addSimpleId(ClassInformation classInformation, StringBuilder cqlCreateTable) {
        FieldInformation keyField = classInformation.getKeyInformation();
        if (keyField == null) {
            createErro(classInformation);
        }
        cqlCreateTable.append(QUERY_PRIMARY_KEY)
                .append(keyField.getName())
                .append(") );");
    }


    /**
     * add in the query a complex key.
     * @param classInformation
     * @param cqlCreateTable
     */
    private void addComlexID(ClassInformation classInformation, StringBuilder cqlCreateTable) {

        FieldInformation keyField = classInformation.getKeyInformation();
        cqlCreateTable.append(QUERY_PRIMARY_KEY);
        boolean firstTime = true;
        if (keyField == null) {
            createErro(classInformation);
        }
        for (FieldInformation subKey : keyField.getSubFields().getFields()) {
            if (firstTime) {
                cqlCreateTable.append(subKey.getName());
                firstTime = false;
            } else {
                cqlCreateTable.append(",").append(subKey.getName());
            }
        }
        cqlCreateTable.append(") );");
    }

    /**
     * @param bean
     */
    private void createErro(ClassInformation bean) {

        StringBuilder erroMensage = new StringBuilder();

        erroMensage.append("the bean ").append(bean.getNameSchema());
        erroMensage
                .append(" hasn't  a field with id annotation, "
                        + "you may to use either javax.persistence.Id");
        erroMensage.append(" to simple id or javax.persistence.EmbeddedId");
        erroMensage
                .append(" to complex id, another object with one "
                        + "or more fields annotated with java.persistence.Column.");
        throw new KeyProblemsException(erroMensage.toString());
    }

    /**
     * create a query to create table and return if exist a complex id or not.
     * @param class1
     * @param cqlCreateTable
     * @param javaCassandra
     * @return
     */
    private void createQueryCreateTable(ClassInformation class1,
            StringBuilder cqlCreateTable) {
        for (FieldInformation field : class1.getFields()) {
            if (field.isEmbedded()) {

                createQueryCreateTable(field.getSubFields(), cqlCreateTable);
                continue;
            }

            AddColumn addColumn = AddColumnUtil.INSTANCE.factory(field);
            cqlCreateTable.append(addColumn.addRow(field,
                    RelationShipJavaCassandra.INSTANCE));
        }
    }

    /**
     * Find if exists.
     * REMARK edited by : Dinusha Nandika;
     */
    private void findIndex(Class<?> familyColumn, Session session) {
        ClassInformation classInformation = ClassInformations.INSTACE.getClass(familyColumn);
        if (classInformation.getIndexFields().size() == 0) {
            return;
        }
        for (FieldInformation index : classInformation.getIndexFields()) {
        	createIndex(classInformation, session, index);
		}
    }

    /**
     * method to create index.
     * @param familyColumn
     * @param session
     * @param index
     */
	private void createIndex(ClassInformation familyColumn, Session session,
	        FieldInformation index) {
		StringBuilder createIndexQuery;
		createIndexQuery = new StringBuilder();
		createIndexQuery.append("CREATE INDEX ");
		createIndexQuery.append(index.getName()).append(" ON ");
		createIndexQuery.append(familyColumn.getNameSchema());
		createIndexQuery.append(" (").append(index.getName()).append(");");
		try {
			session.execute(createIndexQuery.toString());
		} catch (InvalidQueryException exception) {
			Logger.getLogger(FixColumnFamily.class.getName()).info("Index already exists");
		}
	}
}
