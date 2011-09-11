/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.annotations.write;

import org.easycassandra.annotations.write.WriteInterface;
import org.easycassandra.util.EncodingUtil;
import java.nio.ByteBuffer;

/**
 *
 * @author otavio
 */
public class StringWrite implements WriteInterface{

    @Override
    public ByteBuffer getBytebyObject(Object object) {
    
          
            
          
          return EncodingUtil.stringToByte(object.toString()); 
      
    }
}
