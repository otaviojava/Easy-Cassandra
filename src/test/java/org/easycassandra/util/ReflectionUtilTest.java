/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.util;

import java.lang.reflect.Field;
import junit.framework.Assert;
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
