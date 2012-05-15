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

import java.nio.ByteBuffer;
import org.easycassandra.util.EncodingUtil;
import org.easycassandra.util.FileUtil;

public class FileRead implements ReadInterface {

    @Override
    public Object getObjectByByte(ByteBuffer buffer) {
        String value = EncodingUtil.byteToString(buffer);
        if (value.length() == 0) {
            return null;
        }
        String nameFile = value.substring(0, value.indexOf('|'));
        String[] byteString = value.substring(value.indexOf('|') + 1,
                value.lastIndexOf(',')).split(",");
        byte[] bytes = new byte[byteString.length];

        for (int index = 0; index < byteString.length; index++) {
            bytes[index] = Byte.valueOf(byteString[index]);
        }

        return FileUtil.byteFromFile(bytes, nameFile);
    }
}
