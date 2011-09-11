/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.util;

import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author otavio
 */
public class ReflectionUtilTest {

    
    @Test
    public void getMethodTest(){
    String value="test";
    ObjectTest objectTest=new ObjectTest();
    objectTest.setName(value);
    
    Assert.assertEquals(ReflectionUtil.getMethod(objectTest, "name"), value);
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
