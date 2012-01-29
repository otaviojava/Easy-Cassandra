package org.easycassandra.annotations.write;

import java.nio.ByteBuffer;

import org.easycassandra.util.EncodingUtil;

/**
 *for Write Object in mode default
 *it only execute the toString Method
 * @see String
 * @author otavio
 */
public class DefaultWrite implements WriteInterface{

    @Override
    public ByteBuffer getBytebyObject(Object object) {
    
          
            
          
          return EncodingUtil.stringToByte(object.toString()); 
      
    }
}
