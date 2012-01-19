package org.easycassandra.annotations.write;

import java.nio.ByteBuffer;
import org.easycassandra.util.EncodingUtil;

/**
 *for Write Integer
 * @see Integer
 * @author otavio
 */
public class IntegerWrite implements WriteInterface {

    @Override
    public ByteBuffer getBytebyObject(Object object) {
      
      
            Integer integer= (Integer)object;
            
          
          return EncodingUtil.stringToByte(integer.toString()); 
      
    }
}
