/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.annotations.write;

import java.nio.ByteBuffer;

/**
 *
 * @author otavio
 */
public interface WriteInterface {
    
    
    public ByteBuffer getBytebyObject(Object object);
    
}
