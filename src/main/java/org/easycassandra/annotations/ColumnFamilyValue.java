package org.easycassandra.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotations for  identify the family column name
 * @author otavio
 */
@Target (ElementType.TYPE)
@Retention (RetentionPolicy.RUNTIME)
public @interface ColumnFamilyValue {
    /**
     * The valeu for the Column Family name
     * if this no used the value is the field's name
     * @return the name of the Column Family 
     */
     String nome() default "";
    
}
