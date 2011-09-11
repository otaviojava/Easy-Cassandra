/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.annotations.read;

import java.nio.ByteBuffer;

/**
 *
 * @author otavio
 */
public interface ReadInterface {
 public Object getObjectByByte(ByteBuffer buffer);   
}
