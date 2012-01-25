/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.annotations.read;

import java.nio.ByteBuffer;

/**
 *interface for read some information  * from ByteBuffer
 * @see ByteBuffer
 * @author otavio
 */
public interface ReadInterface {
 /**
  * 
  * @param buffer
  * @return
  */
	Object getObjectByByte(ByteBuffer buffer);   
}
