package org.easycassandra.annotations.write;

import java.nio.ByteBuffer;
import org.easycassandra.util.EncodingUtil;

/**
 *for Write Long
 * @see Long
 * @author otavio
 */
public class LongWrite implements WriteInterface {

    @Override
    public ByteBuffer getBytebyObject(Object object) {
     
       
            Long l= (Long)object;
            
          
          return EncodingUtil.stringToByte(l.toString()); 
       
    }
    
}
