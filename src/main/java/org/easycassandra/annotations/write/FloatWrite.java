package org.easycassandra.annotations.write;

import java.nio.ByteBuffer;
import org.easycassandra.util.EncodingUtil;

/**
 *for Write Float
 * @see Float
 * @author otavio
 */
public class FloatWrite  implements WriteInterface {

    @Override
    public ByteBuffer getBytebyObject(Object object) {
     
       
            Float f= (Float)object;
            
          
          return EncodingUtil.stringToByte(f.toString()); 
       
    }
    
}
