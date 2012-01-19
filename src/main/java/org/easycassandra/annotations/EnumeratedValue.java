package org.easycassandra.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for be used in Enums
 * @author otavio
 */
@Target (ElementType.FIELD)
@Retention (RetentionPolicy.RUNTIME)
public @interface EnumeratedValue {
     /**
      * 
      * @return 
      */
     public String nome();
    
}
