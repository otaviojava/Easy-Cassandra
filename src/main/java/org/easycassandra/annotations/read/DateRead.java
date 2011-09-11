/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.annotations.read;

import org.easycassandra.util.EncodingUtil;
import java.nio.ByteBuffer;
import java.util.Date;

/**
 *
 * @author otavio
 */
public class DateRead implements ReadInterface {

    @Override
    public Object getObjectByByte(ByteBuffer buffer) {
        String longString = EncodingUtil.byteToString(buffer);
        Long l = Long.parseLong(longString);
        return new Date(l);

    }
}
