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
public class LongRead implements ReadInterface {
   @Override
    public Object getObjectByByte(ByteBuffer buffer) {
    
            String longString = EncodingUtil.byteToString(buffer);
          
            return Long.parseLong(longString);
          
    }  
}
