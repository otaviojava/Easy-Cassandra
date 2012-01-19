package org.easycassandra.annotations.write;

import java.nio.ByteBuffer;

/**
 *interface for write some information  from Object
 * @see Object
 * @author otavio
 */
public interface WriteInterface {
    
    
    public ByteBuffer getBytebyObject(Object object);
    
}
