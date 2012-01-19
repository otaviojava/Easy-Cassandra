package org.easycassandra.annotations.read;

import java.nio.ByteBuffer;
import org.easycassandra.util.EncodingUtil;

/**
 *for read Double
 * @see Double
 * @author otavio
 */
public class DoubleRead implements ReadInterface {
    
   @Override
    public Object getObjectByByte(ByteBuffer buffer) {
    
            String doubleString = EncodingUtil.byteToString(buffer);
            return Double.parseDouble(doubleString);
        
    }  
}
