package org.easycassandra.annotations.write;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;
import org.easycassandra.util.EncodingUtil;

/**
 *for Write Bate
 * @see Date
 * @author otavio
 */
public class CalendarWrite implements WriteInterface {

    @Override
    public ByteBuffer getBytebyObject(Object object) {
        
            Long date= ((Calendar)object).getTimeInMillis();
            
          
          return EncodingUtil.stringToByte(date.toString()); 
        
    }
}