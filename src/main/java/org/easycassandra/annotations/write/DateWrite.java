/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.annotations.write;

import org.easycassandra.annotations.write.WriteInterface;
import org.easycassandra.util.EncodingUtil;
import java.lang.Long;
import java.nio.ByteBuffer;
import java.util.Date;

/**
 *
 * @author otavio
 */
public class DateWrite implements WriteInterface {

    @Override
    public ByteBuffer getBytebyObject(Object object) {
        
            Long date= ((Date)object).getTime();
            
          
          return EncodingUtil.stringToByte(date.toString()); 
        
    }
}