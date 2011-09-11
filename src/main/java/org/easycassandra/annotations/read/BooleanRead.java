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
public class BooleanRead implements ReadInterface {

    @Override
    public Object getObjectByByte(ByteBuffer buffer) {
        String booleanString = EncodingUtil.byteToString(buffer);
      
        return Boolean.getBoolean(booleanString);

    }
    
}
