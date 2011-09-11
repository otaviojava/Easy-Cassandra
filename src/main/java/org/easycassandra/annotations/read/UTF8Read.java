/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.annotations.read;

import org.easycassandra.util.EncodingUtil;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 *
 * @author otavio
 */
public class UTF8Read implements ReadInterface {

    @Override
    public Object getObjectByByte(ByteBuffer buffer) {
        String value = EncodingUtil.byteToString(buffer);
        return toUTF8(value);
    }

    public String toUTF8(Object value) {
        try {
            StringBuilder string = new StringBuilder();

            byte[] b = value.toString().getBytes(EncodingUtil.encogin);
            for (int k = 0; k < b.length; k++) {
                String hex = Long.toHexString((b[k] + 256) % 256);
                if (hex.length() == 1) {
                    hex = "0" + hex;
                }
                string.append(hex);

            }

            return string.toString();
        } catch (UnsupportedEncodingException ex) {
        }
        return null;


    }
}
