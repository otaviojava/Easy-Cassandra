/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target (ElementType.TYPE)
@Retention (RetentionPolicy.RUNTIME)
public @interface ColumnFamilyValue {
    public String nome();
}
