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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import org.easycassandra.util.EncodingUtil;
import org.easycassandra.util.ReflectionUtil;

public class DefaultRead {

   
    public Object getObjectByByte(ByteBuffer byteBuffer,
            @SuppressWarnings("rawtypes") Class qualifieldName) {
        

                if(String.class.equals(qualifieldName)){
                return EncodingUtil.byteToString(byteBuffer);
                }
          if(Character.class.equals(qualifieldName)){
                return ReflectionUtil.valueOf(qualifieldName,
                        EncodingUtil.byteToString(byteBuffer).charAt(0),
                        char.class);
          }
           if(BigDecimal.class.equals(qualifieldName)){
                return ReflectionUtil.valueOf(qualifieldName,
                        Double.valueOf(EncodingUtil.byteToString(byteBuffer)),
                        double.class);
           }
            if(BigInteger.class.equals(qualifieldName)){
                return ReflectionUtil.valueOf(qualifieldName,
                        Long.valueOf(EncodingUtil.byteToString(byteBuffer)),
                        long.class);
            }
            
                return ReflectionUtil.valueOf(qualifieldName,
                        EncodingUtil.byteToString(byteBuffer));
        
    }
}
