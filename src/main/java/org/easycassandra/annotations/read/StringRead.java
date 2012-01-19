/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.annotations.read;

import java.nio.ByteBuffer;
import org.easycassandra.util.EncodingUtil;

/**
 *for read String
 * @see String
 * @author otavio
 */
public class StringRead implements ReadInterface {
    @Override
    public Object getObjectByByte(ByteBuffer buffer) {
       
        return EncodingUtil.byteToString(buffer);
        
       
        
        
        
    }  
}
