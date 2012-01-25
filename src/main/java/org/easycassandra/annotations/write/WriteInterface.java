package org.easycassandra.annotations.write;

import java.nio.ByteBuffer;

/**
 *interface for write some information  from Object
 * @see Object
 * @author otavio
 */
public interface WriteInterface {
    
    /**
     * 
     * @param object
     * @return
     */
     ByteBuffer getBytebyObject(Object object);
    
}
