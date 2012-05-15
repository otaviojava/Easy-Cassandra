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
package org.easycassandra.annotations.write;

import java.nio.ByteBuffer;
import org.easycassandra.util.EncodingUtil;

/**
 * for Write Enums
 *
 * @see Enum
 * @author otavio
 */
public class EnumWrite implements WriteInterface {

    @Override
    @SuppressWarnings("rawtypes")
    public ByteBuffer getBytebyObject(Object object) {
        Enum enumObject = (Enum) object;
        Integer index = 0;
        Object[] enums = enumObject.getClass().getEnumConstants();
        for (int i = 0; i < enums.length; i++) {
            if (enumObject.equals(enums[i])) {
                index = i;
                break;
            }
        }
        return EncodingUtil.stringToByte(index.toString());
    }
}
