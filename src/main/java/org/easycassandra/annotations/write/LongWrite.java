/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.annotations.write;

import org.easycassandra.util.EncodingUtil;
import java.nio.ByteBuffer;

/**
 *
 * @author otavio
 */
public class LongWrite implements WriteInterface {

    @Override
    public ByteBuffer getBytebyObject(Object object) {
     
       
            Long l= (Long)object;
            
          
          return EncodingUtil.stringToByte(l.toString()); 
       
    }
    
}
