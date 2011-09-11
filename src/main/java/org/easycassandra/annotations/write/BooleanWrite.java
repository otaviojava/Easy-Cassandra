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
public class BooleanWrite implements WriteInterface {

    @Override
    public ByteBuffer getBytebyObject(Object object) {
        
            Boolean boo= (Boolean)object;
            
          
          return EncodingUtil.stringToByte(boo.toString()); 
        
    }
}
