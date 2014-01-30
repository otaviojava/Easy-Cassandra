package org.easycassandra.persistence.cassandra;

import java.lang.reflect.Field;

import org.easycassandra.CustomData;
import org.easycassandra.util.ReflectionUtil;
/**
 * utils to insert query.
 */
enum InsertColumnUtil  {
INSTANCE;

    public InsertColumn factory(Field field) {
        if (ColumnUtil.INTANCE.isEnumField(field)) {
            return new EnumInsert();
        }
        if (ColumnUtil.INTANCE.isCustom(field)) {
            return new CustomInsert();
        }

        return new DefaultInsert();
    }

    /**
     * {@link InsertColumn} to custom.
     * @author otaviojava
     */
    class CustomInsert implements InsertColumn {

        @Override
        public Object getObject(Object bean, Field field) {
            CustomData customData = field.getAnnotation(CustomData.class);
            Customizable customizable = (Customizable) ReflectionUtil.INSTANCE
                    .newInstance(customData.classCustmo());
            return customizable.read(ReflectionUtil.INSTANCE.getMethod(bean,
                    field));
        }

    }
    /**
     * {@link InsertColumn} to enum.
     * @author otaviojava
     */
    class EnumInsert implements InsertColumn {

        @Override
        public Object getObject(Object bean, Field field) {

            Enum<?> enumS = (Enum<?>) ReflectionUtil.INSTANCE.getMethod(bean,
                    field);
            return enumS.ordinal();
        }

    }

    /**
     * {@link InsertColumn} to default.
     * @author otaviojava
     */
    class DefaultInsert implements InsertColumn {

        @Override
        public Object getObject(Object bean, Field field) {
            return ReflectionUtil.INSTANCE.getMethod(bean, field);
        }

    }
    /**
     * prepare object to insertion.
     * @author otaviojava
     */
    public interface InsertColumn {

        Object getObject(Object bean, Field field);

    }
}
