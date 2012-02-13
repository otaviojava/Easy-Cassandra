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
import org.easycassandra.util.ReflectionUtil;

public class DefaultRead {
public static final String STRING_FULL="java.lang.String";
public static final String CHARACTER_FULL="java.lang.Character";
public static final String BIGDECIMAL_FULL="java.math.BigDecimal";
public static final String BIGINTEGER_FULL="java.math.BigInteger";

	public Object getObjectByByte(ByteBuffer byteBuffer,@SuppressWarnings("rawtypes") Class qualifieldName){
		switch (qualifieldName.getName()) {
		case STRING_FULL:
			return EncodingUtil.byteToString(byteBuffer);
			case CHARACTER_FULL:
				return ReflectionUtil.valueOf(qualifieldName, EncodingUtil.byteToString(byteBuffer).charAt(0),char.class);
			case BIGDECIMAL_FULL:
				return ReflectionUtil.valueOf(qualifieldName, Double.valueOf(EncodingUtil.byteToString(byteBuffer)),double.class);
			case BIGINTEGER_FULL:
				return ReflectionUtil.valueOf(qualifieldName, Long.valueOf(EncodingUtil.byteToString(byteBuffer)),long.class);
		default:
			return ReflectionUtil.valueOf(qualifieldName, EncodingUtil.byteToString(byteBuffer));
		}
		
	
	}
	
}
