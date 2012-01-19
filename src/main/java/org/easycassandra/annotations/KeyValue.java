package org.easycassandra.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The field with this annotation is an Key of the Row
 * @author otavio
 */
@Target (ElementType.FIELD)
@Retention (RetentionPolicy.RUNTIME)
public @interface  KeyValue {
    /**
     * If this value is true will generate auto increment, 
     * each new Key in the FamilyColumn there are a new value auto numeric
     * @return if is auto increment
     */
    public boolean auto() default false;
}
