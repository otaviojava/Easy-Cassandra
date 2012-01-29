package org.easycassandra.annotations.readwrite;

import junit.framework.Assert;

import org.easycassandra.annotations.read.DefaultRead;
import org.easycassandra.annotations.write.DefaultWrite;
import org.junit.Before;
import org.junit.Test;

public class FloatReadWriteTest implements ReadWriteNumber{

	private DefaultRead defaultRead;
	
	private DefaultWrite defaultWrite;
	
	@Override
	@Test
	public void primitiveAfirmativeTest() {
		
		Assert.assertEquals(2.2f, defaultRead.getObjectByByte(defaultWrite.getBytebyObject(2.2f),Float.class));
		
	}

	@Override
	@Test
	public void primitiveNegativeTest() {
		Assert.assertFalse(defaultRead.getObjectByByte(defaultWrite.getBytebyObject(2f),Float.class).equals(3f));
		
	}
	
	

	@Override
	@Test
	public void objectAfirmativeTest() {
		Assert.assertEquals(Float.valueOf(1.42f), defaultRead.getObjectByByte(defaultWrite.getBytebyObject(Float.valueOf(1.42f)),Float.class));
		
	}

	@Override
	@Test
	public void objectNegativeTest() {
		Assert.assertFalse(defaultRead.getObjectByByte(defaultWrite.getBytebyObject(Float.valueOf(2f)),Float.class).equals(Float.valueOf(3f)));
		
	}
	


	@Override
	@Before
	public void init() {
		defaultRead=new DefaultRead();
		defaultWrite=new DefaultWrite();
		
	}

}
