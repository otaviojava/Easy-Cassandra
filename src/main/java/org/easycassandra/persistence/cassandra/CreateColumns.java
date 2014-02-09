package org.easycassandra.persistence.cassandra;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.easycassandra.ClassInformation;
import org.easycassandra.FieldInformation;

/**
 * singleto to create query on Cassnadra.
 * @author otaviojava
 */
enum CreateColumns {
    INSTANCE;

    private Map<String, List<String>> queryMap;

    {
        queryMap = Collections.synchronizedMap(new TreeMap<String, List<String>>());
    }

    public List<String> getColumns(ClassInformation classInformation) {
        String qualified  = classInformation.getClassInstance().getName();
        if (queryMap.get(qualified) == null) {
            queryMap.put(qualified, prepare(classInformation));
        }
        return queryMap.get(qualified);
    }

    private List<String> prepare(ClassInformation classInformation) {
        List<String> columns = new LinkedList<>();
        for (FieldInformation field : classInformation.getFields()) {

            if (field.isEmbedded()) {
                columns.addAll(prepare(field.getSubFields()));
                continue;
            }
            columns.add(field.getName());

        }
        return columns;
    }
}
