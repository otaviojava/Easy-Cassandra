package org.easycassandra.annotations.readwrite;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.easycassandra.annotations.read.DateRead;
import org.easycassandra.annotations.read.ReadInterface;
import org.easycassandra.annotations.read.ReadManager;
import org.easycassandra.util.EncodingUtil;
import org.junit.Before;
import org.junit.Test;

public class ReadManagerTest {

	public ReadManager readManager;
	
	@Test
	public void convertTest(){
		String test="test";
		
		Assert.assertEquals(readManager.convert(EncodingUtil.stringToByte(test),String.class), test);
		
	}
	
	@Test
	public void convertFailTest(){
		String test="test";
		
		Assert.assertNull(readManager.convert(EncodingUtil.stringToByte(test),ReadManagerTest.class));
		
	}
	
	@Test
	public void convertLongTest(){
		String test="2"; 
		Assert.assertEquals(readManager.convert(EncodingUtil.stringToByte(test),Long.class), Long.valueOf(test));	
	}
	
	@Test
	public void convertIntegerTest(){
		String test="2"; 
		Assert.assertEquals(readManager.convert(EncodingUtil.stringToByte(test),Integer.class), Integer.valueOf(test));	
	}
	
	
	@Test
	public void convertDoubleTest(){
		String test="2"; 
		Assert.assertEquals(readManager.convert(EncodingUtil.stringToByte(test),Double.class), Double.valueOf(test));	
	}
	
	
	@Test
	public void convertFloatTest(){
		String test="2"; 
		Assert.assertEquals(readManager.convert(EncodingUtil.stringToByte(test),Float.class), Float.valueOf(test));	
	}
	
	
	@Test
	public void convertBooleanTest(){
		String test="2"; 
		Assert.assertEquals(readManager.convert(EncodingUtil.stringToByte(test),Boolean.class), Boolean.valueOf(test));	
	}
	
	
	@Test
	public void convertShortTest(){
		String test="2"; 
		Assert.assertEquals(readManager.convert(EncodingUtil.stringToByte(test),Short.class), Short.valueOf(test));	
	}
	
	@Test
	public void convertByteTest(){
		String test="2"; 
		Assert.assertEquals(readManager.convert(EncodingUtil.stringToByte(test),Byte.class), Byte.valueOf(test));	
	}
	
	

	@Test
	public void convertCharacterTest(){
		String test="2"; 
		Assert.assertEquals(readManager.convert(EncodingUtil.stringToByte(test),Character.class), Character.valueOf(test.charAt(0)));	
		
	}
	
	@Before
	public void init(){
		
		Map<String, ReadInterface> readMap = new HashMap<String, ReadInterface>();
		readMap.put("java.util.Date", new DateRead());
		readManager=new ReadManager(readMap);
		
	}
	
}
