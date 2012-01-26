package org.easycassandra.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *transform ByteBuffer and String vice versa
 * @author otavio
 */
public final class EncodingUtil {

  
    
    public static final String ENCODING = "UTF-8";
    
    public static final Charset CHARSET = Charset.forName(ENCODING);
    
    public static final CharsetEncoder ENCODER = CHARSET.newEncoder();
    
    public static final CharsetDecoder DECODER = CHARSET.newDecoder();
    
/**
 *  return a ByteBuffer from String
 * @param msg mensage
 * @return 
 */
    public static ByteBuffer stringToByte(String msg) {
        try {
            return ENCODER.encode(CharBuffer.wrap(msg));
        } catch (Exception exception) {
            Logger.getLogger(EncodingUtil.class.getName()).log(Level.SEVERE, null, exception);
            
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
            int oldPosition = buffer.position();
            data = DECODER.decode(buffer).toString();

            buffer.position(oldPosition);
        } catch (Exception exception) {
               Logger.getLogger(EncodingUtil.class.getName()).log(Level.SEVERE, null, exception);
            return "";
        }
        return data;
    }

    /**
     * Singleton
     */
    private EncodingUtil() {
    }
    
     
}
