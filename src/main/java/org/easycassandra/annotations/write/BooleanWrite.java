package org.easycassandra.annotations.write;

import java.nio.ByteBuffer;
import org.easycassandra.util.EncodingUtil;

/**
 *for Write Boolean
 * @see Boolean
 * @author otavio
 */
public class BooleanWrite implements WriteInterface {

    @Override
    public ByteBuffer getBytebyObject(Object object) {
        
            Boolean boo= (Boolean)object;
            
          
          return EncodingUtil.stringToByte(boo.toString()); 
        
    }
}
