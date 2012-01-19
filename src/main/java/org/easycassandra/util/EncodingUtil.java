package org.easycassandra.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *transform ByteBuffer and String vice versa
 * @author otavio
 */
public class EncodingUtil {

    private static Logger LOOGER = LoggerFactory.getLogger(EncodingUtil.class);
    
    public static final String encogin = "UTF-8";
    
    public static final Charset charset = Charset.forName(encogin);
    
    public static final CharsetEncoder encoder = charset.newEncoder();
    
    public static final CharsetDecoder decoder = charset.newDecoder();
    
/**
 *  return a ByteBuffer from String
 * @param msg mensage
 * @return 
 */
    public static ByteBuffer stringToByte(String msg) {
        try {
            return encoder.encode(CharBuffer.wrap(msg));
        } catch (Exception e) {
            LOOGER.error("Error during encode", e);
        }
        return null;
    }

    /**
     * Return the String from ByteBuffer
     * @param buffer
     * @return 
     */
    public static String byteToString(ByteBuffer buffer) {
        String data = "";
        try {
            int old_position = buffer.position();
            data = decoder.decode(buffer).toString();

            buffer.position(old_position);
        } catch (Exception e) {
            LOOGER.error("Error during dencode", e);
            return "";
        }
        return data;
    }
}
