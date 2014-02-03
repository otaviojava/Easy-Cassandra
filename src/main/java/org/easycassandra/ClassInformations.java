package org.easycassandra;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 *  Class utils to {@link ClassInformation}.
 * @author otaviojava
 */
public enum ClassInformations {

    INSTACE;
    /**
     * field that contains all class on Cassandra.
     * Key - qualifield
     * value - the wrapper of class
     */
    private Map<String, ClassInformation> classMap;

    /**
     * The integer class is used to enum how default.
     */
    public static final Class<?> DEFAULT_ENUM_CLASS = Integer.class;

    /**
     * return the wrapper of class.
     * @param clazz class to find
     * @return {@link ClassInformation}
     */
    public ClassInformation getClass(Class<?> clazz) {

        if (classMap.get(clazz.getName()) == null) {
            classMap.put(clazz.getName(), new ClassInformation(clazz));
        }
        return classMap.get(clazz.getName());
    }

    {
        classMap = Collections.synchronizedMap(new TreeMap<String, ClassInformation>());
    }
}
