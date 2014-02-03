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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.easycassandra.ClassInformation;
import org.easycassandra.ClassInformations;
import org.easycassandra.FieldInformation;
import org.easycassandra.persistence.cassandra.ReturnValues.ReturnValue;
import org.easycassandra.util.ReflectionUtil;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

/**
 * class until to convert a row in cassandra to a especific object.
 * @author otaviojava
 */
enum RecoveryObject {

    INTANCE;

    @SuppressWarnings("unchecked")
    public <T> List<T> recoverObjet(Class<T> bean, ResultSet resultSet) {

        List<T> listObjList = new LinkedList<T>();
        for (Row row : resultSet.all()) {
            Map<String, Definition> mapDefinition = createMapDefinition(row.getColumnDefinitions());

            Object newObjetc = createObject(bean, row, mapDefinition);
            listObjList.add((T) newObjetc);
        }

        return listObjList;
    }

    private <T> Object createObject(Class<T> bean, Row row,
            Map<String, Definition> mapDefinition) {
        Object newObjetc = ReflectionUtil.INSTANCE.newInstance(bean);
        ClassInformation classInformation = ClassInformations.INSTACE.getClass(bean);
        for (FieldInformation field : classInformation.getFields()) {
            if (field.isEmbedded()) {
                Object value = createObject(field.getField().getType(), row, mapDefinition);
                ReflectionUtil.INSTANCE.setMethod(newObjetc, field.getField(), value);
                continue;
            }
            ReturnValue returnValue = ReturnValues.INSTANCE.factory(field);
            Object value = returnValue.getObject(mapDefinition, field, row);
            ReflectionUtil.INSTANCE.setMethod(newObjetc, field.getField(), value);
        }
        return newObjetc;
    }

    private Map<String, Definition> createMapDefinition(ColumnDefinitions columnDefinitions) {
        Map<String, Definition> map = new HashMap<String, ColumnDefinitions.Definition>();

        for (Definition column : columnDefinitions) {
            map.put(column.getName(), column);
        }
        return map;
    }

}
