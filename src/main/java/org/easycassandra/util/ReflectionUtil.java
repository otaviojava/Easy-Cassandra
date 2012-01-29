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
public final class ReflectionUtil {




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
    
    
    /**
     * method for use the value Of of the Object, for this the class must be 
     * the static method value of and return a new instance of itself
     * @param classValue -  the reference of the Class
     * @param value -the value
     * @param refence - the Class Reference
     * @return the instance of the object
     */
	public static Object valueOf(Class classValue, Object value,Class refence) {
		 MethodType methodType= MethodType.methodType(classValue,refence);
		  MethodHandle methodHandle;
		try {
			methodHandle = MethodHandles.publicLookup().findStatic(classValue, "valueOf", methodType);
			MethodHandle printer=MethodHandles.insertArguments(methodHandle, 0, value);
		
			return printer.invoke();
		}catch (Throwable exception) {
            Logger.getLogger(ReflectionUtil.class.getName()).log(Level.SEVERE, null, exception);
            return null;
        }
		
		
	}
	
	/**
	 * @see ReflectionUtil#valueOf(Class, Object, Class)
	 * @param classValue
	 * @param value
	 * @return
	 */
	public static Object valueOf(Class classValue, Object value) {
		return valueOf(classValue, value, value.getClass());
	}
    

    /**
     * Singleton
     */
    private ReflectionUtil(){
    }


    
}
