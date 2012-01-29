package org.easycassandra.annotations.read;

import java.nio.ByteBuffer;

import org.easycassandra.util.EncodingUtil;
import org.easycassandra.util.ReflectionUtil;

public class DefaultRead {
public static final String STRING_FULL="java.lang.String";
public static final String CHARACTER_FULL="java.lang.Character";
public static final String BIGDECIMAL_FULL="java.math.BigDecimal";
public static final String BIGINTEGER_FULL="java.math.BigInteger";

	public Object getObjectByByte(ByteBuffer byteBuffer,Class qualifieldName){
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
