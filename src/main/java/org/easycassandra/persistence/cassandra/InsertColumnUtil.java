package org.easycassandra.persistence.cassandra;

import java.util.EnumMap;
import java.util.Map;

import org.easycassandra.CustomData;
import org.easycassandra.FieldInformation;
import org.easycassandra.FieldType;
import org.easycassandra.util.ReflectionUtil;
/**
 * utils to insert query.
 */
enum InsertColumnUtil  {
INSTANCE;

    private Map<FieldType, InsertColumn> insertMap;

    {
        insertMap = new EnumMap<>(FieldType.class);
        insertMap.put(FieldType.ENUM, new EnumInsert());
        insertMap.put(FieldType.CUSTOM, new CustomInsert());
        insertMap.put(FieldType.LIST, new DefaultInsert());
        insertMap.put(FieldType.SET, new DefaultInsert());
        insertMap.put(FieldType.MAP, new DefaultInsert());
        insertMap.put(FieldType.COLLECTION, new DefaultInsert());
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
     * prepare object to insertion.
     * @author otaviojava
     */
    public interface InsertColumn {

        Object getObject(Object bean, FieldInformation field);

    }
}
