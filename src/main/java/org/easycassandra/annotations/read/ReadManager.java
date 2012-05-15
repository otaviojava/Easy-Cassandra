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
import java.util.Map;

/**
 * This class see and verify the Object to transform Byte to Object
 *
 * @author otavio
 *
 */
public class ReadManager {

    /**
     * the default value
     *
     * @see DefaultRead
     */
    private DefaultRead defaultRead;
    /**
     * Map with reference for transform Byte in Object
     *
     * @see ReadInterface
     */
    private Map<String, ReadInterface> readMap;

    /**
     * verify if exist ReadInterface for transform ByteBuffer to Object if not
     * exist will return null value
     *
     * @param byteBuffer - value
     * @param qualifieldName - the full name of the class
     * @return the object if not exist null
     */
    public Object convert(ByteBuffer byteBuffer,
            @SuppressWarnings("rawtypes") Class qualifieldName) {
        ReadInterface readInterface = readMap.get(qualifieldName.getName());
        if (readInterface != null) {
            return readInterface.getObjectByByte(byteBuffer);
        }
        return defaultRead.getObjectByByte(byteBuffer, qualifieldName);
    }

    public ReadManager(Map<String, ReadInterface> readMap) {
        this.readMap = readMap;
        defaultRead = new DefaultRead();
    }
}
