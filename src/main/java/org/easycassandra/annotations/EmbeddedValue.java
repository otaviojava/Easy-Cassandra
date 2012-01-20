package org.easycassandra.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * The class with this is annotation has fields
 * with ColumnValue inside itself, 
 * but the persistence way continues 
 * in the same Family of the Column. 
 * This annotation is to do the 
 * object's modeling easily
 * @author otavio
 */
@Target (ElementType.FIELD)
@Retention (RetentionPolicy.RUNTIME)
public @interface EmbeddedValue {
     
    
    
}
