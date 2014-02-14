package org.easycassandra.persistence.cassandra;

import java.util.List;

import org.easycassandra.ClassInformation;
import org.easycassandra.ClassInformation.KeySpaceInformation;
import org.easycassandra.ClassInformations;
import org.easycassandra.FieldInformation;
import org.easycassandra.KeyProblemsException;
import org.easycassandra.util.ReflectionUtil;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Update;

/**
 * utils to update.
 * @author otaviojava
 */
class UpdateQuery {

    private String keySpace;

    public UpdateQuery(String keySpace) {
        this.keySpace = keySpace;
    }

    public Update runUpdate(Object key, Class<?> bean) {
        if (key == null) {
            throw new KeyProblemsException(
                    "The parameter key to column family should be passed");
        }
        ClassInformation classInformations = ClassInformations.INSTACE.getClass(bean);
        KeySpaceInformation keyInformation = classInformations.getKeySpace(keySpace);

        Update update = QueryBuilder.update(keyInformation.getKeySpace(),
                        keyInformation.getColumnFamily());

        FieldInformation keyField = classInformations.getKeyInformation();
        if (classInformations.isComplexKey()) {
            runComplexKey(update, key, keyField.getSubFields().getFields());
        } else {
            update.where(QueryBuilder.eq(keyField.getName(), key));
        }
        return update;
    }

    private void runComplexKey(Update update, Object key, List<FieldInformation> fields) {

        for (FieldInformation subKey : fields) {
            update.where(QueryBuilder.eq(
                    subKey.getName(),
                    ReflectionUtil.INSTANCE.getMethod(key, subKey.getField())));
        }
    }
}
