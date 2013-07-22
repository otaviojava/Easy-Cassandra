package org.easycassandra.persistence.cassandra;

import java.lang.reflect.Field;

import org.easycassandra.CustomData;
import org.easycassandra.util.ReflectionUtil;

enum InsertColumnUtil  {
INSTANCE;

public InsertColumn factory(Field field){
    if (ColumnUtil.INTANCE.isEnumField(field)) {
        return new EnumInsert();
    }
    if(ColumnUtil.INTANCE.isCustom(field)){
        return new CustomInsert();
    }
    
    return new DefaultInsert();
}


class CustomInsert implements InsertColumn{

    @Override
    public Object getObject(Object bean, Field field) {
       CustomData customData=field.getAnnotation(CustomData.class);
       Customizable customizable=(Customizable) ReflectionUtil.newInstance(customData.classCustmo());
       return customizable.read(ReflectionUtil.getMethod(bean, field));
    }
    
    
}

class EnumInsert implements InsertColumn{

    @Override
    public Object getObject(Object bean, Field field) {
        Enum<?> enumS = (Enum<?>) ReflectionUtil.getMethod(bean,field);
        return enumS.ordinal();
    }
    
}
class DefaultInsert implements InsertColumn{

    @Override
    public Object getObject(Object bean, Field field) {
        return ReflectionUtil.getMethod(bean, field);
    }
    
}

public interface InsertColumn{
    
    Object getObject(Object bean,Field field);
    
}
}
