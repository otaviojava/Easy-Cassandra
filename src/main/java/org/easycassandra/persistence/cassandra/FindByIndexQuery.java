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
package org.easycassandra.persistence.cassandra;

import java.util.List;

import org.easycassandra.ClassInformation;
import org.easycassandra.ClassInformations;
import org.easycassandra.FieldInformation;
import org.easycassandra.IndexProblemException;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

/**
 * Class to execute a index.
 * @author otaviojava
 */
class FindByIndexQuery extends FindByKeyQuery {

	public FindByIndexQuery(String keySpace) {
		super(keySpace);
	}

    public <T, I> List<T> findByIndex(I index, Class<T> bean, Session session,
            ConsistencyLevel consistency) {
        ClassInformation classInformation = ClassInformations.INSTACE.getClass(bean);
        List<FieldInformation> fields = classInformation.getIndexFields();
        checkFieldNull(bean, fields);
        return findByIndex(fields.get(0).getName(), index, bean, session, consistency);
    }

    /**
     * Edited by Nenita Casuga to make method static and package protected so it can be reused.
     *
     */
	static <T> void checkFieldNull(Class<T> bean, List<FieldInformation> fields) {
		if (fields.isEmpty()) {
			StringBuilder erro = new StringBuilder();
			erro.append("No found some field with @org.easycassandra.Index within ");
			erro.append(bean.getName()).append(" class.");
			throw new IndexProblemException(erro.toString());
		}
	}

    public <T, I> List<T> findByIndex(String indexName, I key, Class<T> bean,
            Session session, ConsistencyLevel consistency) {
        QueryBean byKeyBean = createQueryBean(bean, consistency);
        return executeConditions(indexName, key, bean, session, byKeyBean);
    }

    private <T> List<T> executeConditions(String indexName, Object key,
            Class<T> bean, Session session, QueryBean byKeyBean) {

        ClassInformation classInformation = ClassInformations.INSTACE.getClass(bean);

        byKeyBean.setSearchField(classInformation.findIndexByName(indexName));
        checkIndexProblem(bean, byKeyBean);
        ResultSet resultSet = executeQuery(key, bean, session, byKeyBean);
        return RecoveryObject.INTANCE.recoverObjet(bean, resultSet);
    }
    protected void defineSearchField(QueryBean byKeyBean, Class<?> bean) {
    }
    /**
     * Edited by Nenita Casuga to make method static and package protected so it
     * can be reused.
     */
    static <T> void checkIndexProblem(Class<T> bean, QueryBean byKeyBean) {
        if (byKeyBean.getSearchField() == null) {
            StringBuilder erro = new StringBuilder();
            erro.append("Some field in a class ").append(bean.getName());
            erro.append(" should be a annotation: @org.easycassandra.annotations.Index ");
            throw new IndexProblemException(erro.toString());
        }
    }
}
