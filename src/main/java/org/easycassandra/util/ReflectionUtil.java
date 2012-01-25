package org.easycassandra.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The class does getter and setter with invoke Dynamic
 * @author otavio
 */
public class ReflectionUtil {




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
        } catch (Throwable exception) {
          Logger.getLogger(ReflectionUtil.class.getName()).log(Level.SEVERE, null, exception);
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


        } catch (Throwable exception) {
            Logger.getLogger(ReflectionUtil.class.getName()).log(Level.SEVERE, null, exception);
            return false;
        }
        return true;
  }

    
}
