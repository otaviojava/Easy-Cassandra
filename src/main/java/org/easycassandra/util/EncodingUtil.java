/*
 * Copyright 2012 Otávio Gonçalves de Santana (otaviojava)
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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

  
    /**
     * The default  encoding
     */
    public static final String ENCODING = "UTF-8";
    /**
     * default Charset
     * @see EncodingUtil#CHARSET
     */
    public static final Charset CHARSET = Charset.forName(ENCODING);
    
    /**
     * For encoder the String in Charset default 
     */
    public static final CharsetEncoder ENCODER = CHARSET.newEncoder();
    /**
     * for decoder the strings with charset default
     */
    public static final CharsetDecoder DECODER = CHARSET.newDecoder();
    
/**
 *  return a ByteBuffer from String
 * @param msg mensage
 * @return the ByteBuffer within String
 */
    public static ByteBuffer stringToByte(String msg) {
    	if(msg==null){
    		return null;
    	}
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
     * @return the String with the byteBuffer value
     */
    public static String byteToString(ByteBuffer buffer) {
    	if(buffer==null){
    		return null;
    	}
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
