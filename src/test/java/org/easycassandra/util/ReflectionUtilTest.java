package org.easycassandra.util;

import java.lang.reflect.Field;

import junit.framework.Assert;

import org.easycassandra.bean.model.Person;
import org.junit.Test;

/**
 *
 * @author otavio
 */
public class ReflectionUtilTest {

    @Test
    public void getMethodTest() {
        String value = "test";
        ObjectTest objectTest = new ObjectTest();
        objectTest.setName(value);
        Field field = null;
        for (Field f : objectTest.getClass().getDeclaredFields()) {
            field = f;
            break;
        }
        Assert.assertEquals(ReflectionUtil.getMethod(objectTest, field), value);
    }
    @Test
    public void setMethodTest() {
        String value = "test";
        ObjectTest objectTest = new ObjectTest();
        Field field = null;
        for (Field f : objectTest.getClass().getDeclaredFields()) {
            field = f;
            break;
        }
        ReflectionUtil.setMethod(objectTest, field,value);
        Assert.assertEquals(ReflectionUtil.getMethod(objectTest, field), value);
    }
    
    @Test
    public void valueofTest(){
    	
    	Assert.assertEquals(Long.valueOf("2"), ReflectionUtil.valueOf(Long.class, "2"));
    }
    
    @Test
    public void valueofIntTest(){
    	
    	Assert.assertEquals(Integer.valueOf("2"), ReflectionUtil.valueOf(Integer.class, "2"));
    }
    
    @Test
    public void valueofCharTest(){
    	Assert.assertEquals(Character.valueOf('a'), ReflectionUtil.valueOf(Character.class,'a',char.class ));
    }
    
    @Test
    public void newInscateTest(){
    	Assert.assertNotNull(ReflectionUtil.newInstance(Person.class));
    }
    
    @Test
    public void getFieldTest(){
    	Assert.assertNotNull(ReflectionUtil.getField("name",Person.class));
    }
    
    @Test
    public void getFieldNameTest(){
    	Field field=ReflectionUtil.getField("name",Person.class);
    	Assert.assertEquals(field.getName(), Person.class.getDeclaredFields()[2].getName());
    }
    public class ObjectTest {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
