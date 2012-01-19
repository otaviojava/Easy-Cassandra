package org.easycassandra.annotations.read;

import java.nio.ByteBuffer;
import org.easycassandra.util.EncodingUtil;

/**
 *for read Long
 * @see Long
 * @author otavio
 */
public class LongRead implements ReadInterface {
   @Override
    public Object getObjectByByte(ByteBuffer buffer) {
    
            String longString = EncodingUtil.byteToString(buffer);
          
            return Long.parseLong(longString);
          
    }  
}
