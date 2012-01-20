package org.easycassandra.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * The field with this annotation is an index
 * Can search and retrieve information from the row like KeyValue, 
 * need also use the ColumnValue together with this annotatio
 * @see ColumnValue
 * @author otavio
 */
@Target (ElementType.FIELD)
@Retention (RetentionPolicy.RUNTIME)
public @interface IndexValue {
    
}
