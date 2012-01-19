package org.easycassandra.annotations.read;

import java.nio.ByteBuffer;
import org.easycassandra.util.EncodingUtil;

/**
 *for read Integer
 * @see Integer
 * @author otavio
 */
public class IntegerRead implements ReadInterface {

    @Override
    public Object getObjectByByte(ByteBuffer buffer) {

        String integerString = EncodingUtil.byteToString(buffer);
        return Integer.parseInt(integerString);

    }
}
