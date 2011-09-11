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
public class DoubleWrite  implements WriteInterface {

    @Override
    public ByteBuffer getBytebyObject(Object object) {
     
       
            Double d= (Double)object;
            
          
          return EncodingUtil.stringToByte(d.toString()); 
       
    }
    
}