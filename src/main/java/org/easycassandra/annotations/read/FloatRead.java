/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.annotations.read;

import org.easycassandra.util.EncodingUtil;
import java.nio.ByteBuffer;

/**
 *
 * @author otavio
 */
public class FloatRead implements ReadInterface {
    
   @Override
    public Object getObjectByByte(ByteBuffer buffer) {
    
            String floatString = EncodingUtil.byteToString(buffer);
            return Float.parseFloat(floatString);
        
    }  
}