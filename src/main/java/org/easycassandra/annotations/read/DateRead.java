package org.easycassandra.annotations.read;

import java.nio.ByteBuffer;
import java.util.Date;
import org.easycassandra.util.EncodingUtil;

/**
 *for read Date
 * @see java#util#Date
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
