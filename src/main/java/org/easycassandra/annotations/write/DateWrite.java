package org.easycassandra.annotations.write;

import java.nio.ByteBuffer;
import java.util.Date;
import org.easycassandra.util.EncodingUtil;

/**
 *for Write Bate
 * @see Date
 * @author otavio
 */
public class DateWrite implements WriteInterface {

    @Override
    public ByteBuffer getBytebyObject(Object object) {
        
            Long date= ((Date)object).getTime();
            
          
          return EncodingUtil.stringToByte(date.toString()); 
        
    }
}