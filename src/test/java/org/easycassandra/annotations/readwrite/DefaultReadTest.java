package org.easycassandra.annotations.readwrite;

import java.math.BigDecimal;
import java.math.BigInteger;

import junit.framework.Assert;

import org.easycassandra.annotations.read.DefaultRead;
import org.easycassandra.util.EncodingUtil;
import org.junit.Before;
import org.junit.Test;

public class DefaultReadTest {
	public DefaultRead readManager;
	
	
	
	@Test
	public void getObjectByByteFailTest(){
		String test="test";
		
		Assert.assertNull(readManager.getObjectByByte(EncodingUtil.stringToByte(test),ReadManagerTest.class));
		
	}
	
	@Test
	public void getObjectByByteLongTest(){
		String test="2"; 
		Assert.assertEquals(readManager.getObjectByByte(EncodingUtil.stringToByte(test),Long.class), Long.valueOf(test));	
	}
	
	@Test
	public void getObjectByByteIntegerTest(){
		String test="2"; 
		Assert.assertEquals(readManager.getObjectByByte(EncodingUtil.stringToByte(test),Integer.class), Integer.valueOf(test));	
	}
	
	
	@Test
	public void getObjectByByteDoubleTest(){
		String test="2"; 
		Assert.assertEquals(readManager.getObjectByByte(EncodingUtil.stringToByte(test),Double.class), Double.valueOf(test));	
	}
	
	
	@Test
	public void getObjectByByteFloatTest(){
		String test="2"; 
		Assert.assertEquals(readManager.getObjectByByte(EncodingUtil.stringToByte(test),Float.class), Float.valueOf(test));	
	}
	
	
	@Test
	public void getObjectByByteBooleanTest(){
		String test="2"; 
		Assert.assertEquals(readManager.getObjectByByte(EncodingUtil.stringToByte(test),Boolean.class), Boolean.valueOf(test));	
	}
	
	
	@Test
	public void getObjectByByteShortTest(){
		String test="2"; 
		Assert.assertEquals(readManager.getObjectByByte(EncodingUtil.stringToByte(test),Short.class), Short.valueOf(test));	
	}
	
	@Test
	public void getObjectByByteTest(){
		String test="test";
		
		Assert.assertEquals(readManager.getObjectByByte(EncodingUtil.stringToByte(test),String.class), test);
		
	}
	
	@Test
	public void getObjectByByteByteTest(){
		String test="2"; 
		Assert.assertEquals(readManager.getObjectByByte(EncodingUtil.stringToByte(test),Byte.class), Byte.valueOf(test));	
	}
	
	

	@Test
	public void getObjectByByteCharacterTest(){
		String test="2"; 
		Assert.assertEquals(readManager.getObjectByByte(EncodingUtil.stringToByte(test),Character.class), Character.valueOf(test.charAt(0)));	
		
	}
	
	@Test
	public void getObjectByByteBigDecimalTest(){
		Double test=2d; 
		Assert.assertEquals(readManager.getObjectByByte(EncodingUtil.stringToByte(test.toString()),BigDecimal.class), BigDecimal.valueOf(test));	
		
	}
	
	@Test
	public void getObjectByByteBigIntegerTest(){
		Long test=2l; 
		Assert.assertEquals(readManager.getObjectByByte(EncodingUtil.stringToByte(test.toString()),BigInteger.class), BigInteger.valueOf(test));	
		
	}
	
	@Before
	public void init(){
		
		
		readManager=new DefaultRead();
		
	}
}
