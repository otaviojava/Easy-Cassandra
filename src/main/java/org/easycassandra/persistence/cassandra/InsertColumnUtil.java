package org.easycassandra.persistence.cassandra;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.easycassandra.CustomData;
import org.easycassandra.FieldInformation;
import org.easycassandra.FieldType;
import org.easycassandra.util.ReflectionUtil;

import com.google.gson.Gson;
/**
 * utils to insert query.
 */
@SuppressWarnings("unchecked")
enum InsertColumnUtil  {
INSTANCE;

    private Map<FieldType, InsertColumn> insertMap;

    {
        insertMap = new EnumMap<>(FieldType.class);
        insertMap.put(FieldType.ENUM, new EnumInsert());
        insertMap.put(FieldType.CUSTOM, new CustomInsert());
        insertMap.put(FieldType.LIST, new ListInsert());
        insertMap.put(FieldType.SET, new SetInsert());
        insertMap.put(FieldType.MAP, new MapInsert());
        insertMap.put(FieldType.COLLECTION, new CollectionInsert());
        insertMap.put(FieldType.DEFAULT, new DefaultInsert());

    }
    public InsertColumn factory(FieldInformation field) {
        return insertMap.get(field.getType());
    }

    /**
     * {@link InsertColumn} to custom.
     * @author otaviojava
     */
    class CustomInsert implements InsertColumn {

        @Override
        public Object getObject(Object bean, FieldInformation field) {
            CustomData customData = field.getField().getAnnotation(CustomData.class);
            Customizable customizable = (Customizable) ReflectionUtil.INSTANCE
                    .newInstance(customData.classCustmo());
            return customizable.read(ReflectionUtil.INSTANCE.getMethod(bean,
                    field.getField()));
        }

    }
    /**
     * {@link InsertColumn} to enum.
     * @author otaviojava
     */
    class EnumInsert implements InsertColumn {

        @Override
        public Object getObject(Object bean, FieldInformation field) {

            Enum<?> enumS = (Enum<?>) ReflectionUtil.INSTANCE.getMethod(bean,
                    field.getField());
            return enumS.ordinal();
        }

    }

    /**
     * {@link InsertColumn} to default.
     * @author otaviojava
     */
    class DefaultInsert implements InsertColumn {

        @Override
        public Object getObject(Object bean, FieldInformation field) {
            return ReflectionUtil.INSTANCE.getMethod(bean, field.getField());
        }

    }
    /**
     * {@link InsertColumn} to list.
     * @author otaviojava
     */
    class ListInsert implements InsertColumn {

        @Override
        public Object getObject(Object bean, FieldInformation field) {
            RelationShipJavaCassandra javaCassandra = RelationShipJavaCassandra.INSTANCE;
            if (javaCassandra.containsType(field.getKey().getName())) {
                return ReflectionUtil.INSTANCE.getMethod(bean, field.getField());
            }
            List<String> list = new LinkedList<>();
            Collection<Object> beans = (Collection<Object>) ReflectionUtil.INSTANCE
                    .getMethod(bean, field.getField());
            Gson gson = new Gson();

            for (Object newBean : beans) {
                list.add(gson.toJson(newBean));
            }
            return list;
        }

    }
    /**
     * {@link InsertColumn} to set.
     * @author otaviojava
     */
    class SetInsert implements InsertColumn {

        private ListInsert listInsert = new ListInsert();
        @Override
        public Object getObject(Object beans, FieldInformation field) {
            RelationShipJavaCassandra javaCassandra = RelationShipJavaCassandra.INSTANCE;
            if (javaCassandra.containsType(field.getKey().getName())) {
                return ReflectionUtil.INSTANCE.getMethod(beans, field.getField());
            }
            List<String> list = (List<String>) listInsert.getObject(beans, field);
            return new HashSet<>(list);
        }

    }
    /**
     * {@link InsertColumn} to set.
     * @author otaviojava
     */
    class MapInsert implements InsertColumn {

        @Override
        public Object getObject(Object beans, FieldInformation field) {
            RelationShipJavaCassandra javaCassandra = RelationShipJavaCassandra.INSTANCE;
            if (javaCassandra.containsType(field.getKey().getName())) {
                return ReflectionUtil.INSTANCE.getMethod(beans, field.getField());
            }
            Map<String, String> newMap = new HashMap<>();
            Gson gson = new Gson();
            Map<Object, Object> map = (Map<Object, Object>) ReflectionUtil.INSTANCE
                    .getMethod(beans, field.getField());
            for (Object key : map.keySet()) {
                String newKey = gson.toJson(key);
                String newValue = gson.toJson(map.get(key));
                newMap.put(newKey, newValue);
            }
            return newMap;

        }

    }

    /**
     * {@link InsertColumn} to collection.
     * @author otaviojava
     */
    class CollectionInsert implements InsertColumn {

        @Override
        public Object getObject(Object beans, FieldInformation field) {
            InsertColumn insertColumn = insertMap.get(field.getCollectionType());
            return insertColumn.getObject(beans, field);
        }

    }
    /**
     * prepare object to insertion.
     * @author otaviojava
     */
    public interface InsertColumn {

        Object getObject(Object bean, FieldInformation field);

    }
}
