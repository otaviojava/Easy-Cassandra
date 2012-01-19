package org.easycassandra.annotations.write;

import java.nio.ByteBuffer;
import org.easycassandra.util.EncodingUtil;

/**
 *for Write Dobule
 * @see Double
 * @author otavio
 */
public class DoubleWrite  implements WriteInterface {

    @Override
    public ByteBuffer getBytebyObject(Object object) {
     
       
            Double d= (Double)object;
            
          
          return EncodingUtil.stringToByte(d.toString()); 
       
    }
    
}