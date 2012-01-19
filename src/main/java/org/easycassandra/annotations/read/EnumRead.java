/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.annotations.read;

import java.nio.ByteBuffer;
import org.easycassandra.util.EncodingUtil;

/**
 *for read Enuns
 * @author otavio
 */
public class EnumRead implements ReadInterface {
    
   @Override
    public Object getObjectByByte(ByteBuffer buffer) {
    
            String integerString = EncodingUtil.byteToString(buffer);
            return Integer.parseInt(integerString);
        
    }  
    
}
