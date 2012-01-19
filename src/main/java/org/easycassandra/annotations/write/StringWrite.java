package org.easycassandra.annotations.write;

import java.nio.ByteBuffer;
import org.easycassandra.util.EncodingUtil;

/**
 *for Write String
 * @see String
 * @author otavio
 */
public class StringWrite implements WriteInterface{

    @Override
    public ByteBuffer getBytebyObject(Object object) {
    
          
            
          
          return EncodingUtil.stringToByte(object.toString()); 
      
    }
}
