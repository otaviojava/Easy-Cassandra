/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.annotations.read;

import java.nio.ByteBuffer;
import org.easycassandra.util.EncodingUtil;

/**
 *for read Float
 * @author otavio
 */
public class FloatRead implements ReadInterface {
    
   @Override
    public Object getObjectByByte(ByteBuffer buffer) {
    
            String floatString = EncodingUtil.byteToString(buffer);
            return Float.parseFloat(floatString);
        
    }  
}