package org.easycassandra.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class does getter and setter with invoke Dynamic
 * @author otavio
 */
public class ReflectionUtil {

    private static Logger LOOGER = LoggerFactory.getLogger(ReflectionUtil.class);

//    public static Object executeMethod(Object object, String method, Object... params) {
//        Class clazz = object.getClass();
//
//        Object o = null;
//        Method m;
//        try {
//
//            m = clazz.getMethod(method, getClass(params));
//            o = m.invoke(object, params);
//
//
//
//            return o;
//        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
//            LOOGER.error("Error in execute method", ex);
//        }
//
//        return null;
//    }
//
//    private static Class[] getClass(Object... objetos) {
//        Class[] classes = new Class[objetos.length];
//
//        for (int i = 0; i < objetos.length; i++) {
//            classes[i] = objetos[i].getClass();
//        }
//        return classes;
//    }
//  public static Object getMethod(Object object, Field attribute) {
//      String attributeName=attribute.getName();  
//      try {
//            Class clazz = object.getClass();
//            String metodo = "get" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
//            Object o = null;
//            Method m;
//
//            m = clazz.getMethod(metodo);
//
//            o = m.invoke(object);
//
//            return o;
//        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
//
//
//        }
//        return null;
//    }
//
//    public static boolean setMethod(Object object, Field attribute, Object params) {
//        String attributeName=attribute.getName(); 
//        Class clazz = object.getClass();
//        String metodo = "set" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
//        Object o = null;
//        Method m;
//        try {
//
//            m = clazz.getMethod(metodo, getClass(params));
//            o = m.invoke(object, params);
//
//
//
//            return false;
//        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
//    
//        }
//
//        return false;
//    }

    /**
     * Return The Object from the Field
     * @param object
     * @param field
     * @return 
     */
    public static Object getMethod(Object object, Field field) {
       
        try {
            String fieldName = field.getName();
            MethodType methodType = MethodType.methodType(field.getType());
            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            MethodHandle methodHandle = MethodHandles.publicLookup().bind(object, methodName, methodType);

            return methodHandle.invoke();
        } catch (Throwable ex) {
            LOOGER.error("Error in execute getter", ex);
        }
        return null;
    }

    /**
     * Set the field in the Object
     * @param object
     * @param field
     * @param value
     * @return 
     */
    public static boolean setMethod(Object object, Field field, Object value) {
      
        try {
            String fieldName = field.getName();
            MethodType methodType = MethodType.methodType(void.class, field.getType());
            String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            MethodHandle methodHandle = MethodHandles.publicLookup().bind(object, methodName, methodType);
            MethodHandle printer = MethodHandles.insertArguments(methodHandle, 0, value);

            printer.invoke();


        } catch (Throwable ex) {
            LOOGER.error("Error in execute setter", ex);
            return false;
        }
        return true;
  }

    
}
