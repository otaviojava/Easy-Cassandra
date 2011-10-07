/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author otavio
 */
public class ReflectionUtil {

    private static Logger LOOGER = LoggerFactory.getLogger(ReflectionUtil.class);

    public static Object getMethod(Object object, Field attribute) {
        String attributeName=attribute.getName();
        try {
            Class clazz = object.getClass();
            String metodo = "get" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
            Object o = null;
            Method m;

            m = clazz.getMethod(metodo);

            o = m.invoke(object);

            return o;
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {

            LOOGER.error("Error in get method", ex);

        }
        return null;
    }

    public static boolean setMethod(Object object, Field attribute, Object... params) {
           String attributeName=attribute.getName();
        Class clazz = object.getClass();
        String metodo = "set" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
        Object o = null;
        Method m;
        try {

            m = clazz.getMethod(metodo, getClass(params));
            o = m.invoke(object, params);



            return false;
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOOGER.error("Error in set method", ex);
        }

        return false;
    }

    public static Object executeMethod(Object object, String method, Object... params) {
        Class clazz = object.getClass();

        Object o = null;
        Method m;
        try {

            m = clazz.getMethod(method, getClass(params));
            o = m.invoke(object, params);



            return o;
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOOGER.error("Error in execute method", ex);
        }

        return null;
    }

    private static Class[] getClass(Object... objetos) {
        Class[] classes = new Class[objetos.length];

        for (int i = 0; i < objetos.length; i++) {
            classes[i] = objetos[i].getClass();
        }
        return classes;
    }
    
     
}
