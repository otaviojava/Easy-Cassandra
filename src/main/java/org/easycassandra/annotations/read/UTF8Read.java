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
package org.easycassandra.annotations.read;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.easycassandra.util.EncodingUtil;

/**
 *Read in UTF8
 * @author otavio
 */
public class UTF8Read implements ReadInterface {

	/**
	 * Constante ZERO
	 */
    private static final String ZERO = "0";
    /**
     * Base 
     */
	private static final int BASE_UTF8 = 256;

	@Override
    public Object getObjectByByte(ByteBuffer buffer) {
        String value = EncodingUtil.byteToString(buffer);
        return toUTF8(value);
    }

    public String toUTF8(Object value) {
        try {
            StringBuilder string = new StringBuilder();

            byte[] bytes = value.toString().getBytes(EncodingUtil.ENCODING);
            for (int index = 0; index < bytes.length; index++) {
                String hex = Long.toHexString((bytes[index] + BASE_UTF8) % BASE_UTF8);
                if (hex.length() == 1) {
                    hex = ZERO + hex;
                }
                string.append(hex);

            }

            return string.toString();
        } catch (UnsupportedEncodingException ex) {
        }
        return null;


    }
}
