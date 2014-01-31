/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.annotations;

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import junit.framework.Assert;

import org.easycassandra.bean.model.Person;
import org.junit.Test;

/**
 *
 * @author otaviojava - otaviojava@java.net
 */
public class AnnotationsTest {

    /**
     * run the test.
     */
    @Test
    public void readColumnFamilyTest() {
        String value = "person";
        Assert.assertEquals(value, Person.class.getAnnotation(Entity.class).name());

    }
    /**
     * run the test.
     */
    @Test
    public void existKeyValueTest() {
        boolean existKeyValue = false;
        for (Field field : Person.class.getDeclaredFields()) {

            if (field.getAnnotation(Id.class) != null) {

                existKeyValue = true;
                break;
            }
        }

        Assert.assertTrue(existKeyValue);
    }
    /**
     * run the test.
     */
    @Test
    public void retrieveKeyValueTest() {
        for (Field field : Person.class.getDeclaredFields()) {

            if (field.getAnnotation(Id.class) != null) {

                Assert.assertTrue(true);

            }
        }

        Assert.assertFalse(false);

    }
    /**
     * run the test.
     */
    @Test
    public void countColumnValueTest() {
        int objective = 2;
        int counter = 0;

        for (Field field : Person.class.getDeclaredFields()) {

            if (field.getAnnotation(Column.class) != null) {
                counter++;
            }
        }
        Assert.assertEquals(counter, objective);
    }

}
