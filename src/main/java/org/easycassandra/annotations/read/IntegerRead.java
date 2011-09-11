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
public class IntegerRead implements ReadInterface {

    @Override
    public Object getObjectByByte(ByteBuffer buffer) {

        String integerString = EncodingUtil.byteToString(buffer);
        return Integer.parseInt(integerString);

    }
}
