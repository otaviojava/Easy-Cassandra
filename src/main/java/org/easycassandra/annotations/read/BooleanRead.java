package org.easycassandra.annotations.read;

import java.nio.ByteBuffer;
import org.easycassandra.util.EncodingUtil;

/**
 *for Read Boolean
 * @see Boolean
 * @author otavio
 */
public class BooleanRead implements ReadInterface {

    @Override
    public Object getObjectByByte(ByteBuffer buffer) {
        String booleanString = EncodingUtil.byteToString(buffer);
      
        return Boolean.getBoolean(booleanString);

    }
    
}
