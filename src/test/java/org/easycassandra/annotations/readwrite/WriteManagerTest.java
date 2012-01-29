package org.easycassandra.annotations.readwrite;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.easycassandra.annotations.write.DateWrite;
import org.easycassandra.annotations.write.DefaultWrite;
import org.easycassandra.annotations.write.WriteInterface;
import org.easycassandra.annotations.write.WriteManager;
import org.easycassandra.util.EncodingUtil;
import org.junit.Before;
import org.junit.Test;

public class WriteManagerTest {

	private WriteManager writeManager;

	
	
	@Test
	public void convertTest(){
		String test="test";
		
		Assert.assertEquals(writeManager.convert(test), EncodingUtil.stringToByte(test));
		
	}
	
	@Test
	public void convertDateTest(){
		Date test=new Date();
		Assert.assertEquals(writeManager.convert(test), new DateWrite().getBytebyObject(test));
		
	}
	
	
	@Test
	public void convertIntegerTest(){
		
		Assert.assertEquals(writeManager.convert(2), new DefaultWrite().getBytebyObject(2));
		
	}
	@Test
	public void convertLongTest(){
		Assert.assertEquals(writeManager.convert(2l), new DefaultWrite().getBytebyObject(2l));	}
	
		
	@Test
	public void convertDoubleTest(){
		Assert.assertEquals(writeManager.convert(2d), new DefaultWrite().getBytebyObject(2d));	
	}
	
	
	@Test
	public void convertFloatTest(){
		Assert.assertEquals(writeManager.convert(2f), new DefaultWrite().getBytebyObject(2f));	
	}
	
	
	@Test
	public void convertBooleanTest(){
		Assert.assertEquals(writeManager.convert(true), new DefaultWrite().getBytebyObject(true));	

	}
	
	
	@Test
	public void convertShortTest(){
		Assert.assertEquals(writeManager.convert((short)2), new DefaultWrite().getBytebyObject((short)2));	
	
	}
	
	@Test
	public void convertCharacterTest(){
		Assert.assertEquals(writeManager.convert((char)2), new DefaultWrite().getBytebyObject((char)2));	
	
	}
	
	@Test
	public void convertByteTest(){
		Assert.assertEquals(writeManager.convert((byte)2), new DefaultWrite().getBytebyObject((byte)2));	
	
	}
	
	@Test
	public void convertBigDecimalTest(){
		Assert.assertEquals(writeManager.convert(BigDecimal.valueOf(2d)), new DefaultWrite().getBytebyObject(BigDecimal.valueOf(2d)));	
	
	}
	
	@Test
	public void convertBigIntegerTest(){
		Assert.assertEquals(writeManager.convert(BigInteger.valueOf(2l)), new DefaultWrite().getBytebyObject(BigInteger.valueOf(2l)));	
	}
	
	
	
	
	@Before
	public void init(){
		
		Map<String, WriteInterface> writeMap = new HashMap<>();
		writeMap.put("java.util.Date", new DateWrite());
		writeManager = new WriteManager(writeMap);
	}
	
}
