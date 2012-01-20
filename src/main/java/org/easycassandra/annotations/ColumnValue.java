/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * annotations for  identify the column in the Family Column
 * The classes can be used are:
 * @see Boolean
 * @see java#util#Date
 * @see Double
 * @see Float
 * @see Integer
 * @see Long
 * @see String
 * 
 * If you want use Enums 
 * @see EnumeratedValue
  * @author otavio
 */
@Target (ElementType.FIELD)
@Retention (RetentionPolicy.RUNTIME)
public @interface ColumnValue {
    /**
     * The valeu for the column name
     * if this no used the value is the field's name
     * @return the name of the Column  
     */
      public String nome() default "";
    
    
}
