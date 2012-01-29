package org.easycassandra.annotations.readwrite;

import junit.framework.Assert;

import org.easycassandra.annotations.read.DefaultRead;
import org.easycassandra.annotations.write.DefaultWrite;
import org.junit.Before;
import org.junit.Test;

public class DoubleReadWriteTest implements ReadWriteNumber{

	private DefaultRead defaultRead;
	
	private DefaultWrite defaultWrite;
	
	@Override
	@Test
	public void primitiveAfirmativeTest() {
		
		Assert.assertEquals(2d, defaultRead.getObjectByByte(defaultWrite.getBytebyObject(2d),Double.class));
		
	}

	@Override
	@Test
	public void primitiveNegativeTest() {
		Assert.assertFalse(defaultRead.getObjectByByte(defaultWrite.getBytebyObject(2d),Double.class).equals(3d));
		
	}
	
	

	@Override
	@Test
	public void objectAfirmativeTest() {
		Assert.assertEquals(Double.valueOf(2d), defaultRead.getObjectByByte(defaultWrite.getBytebyObject(Double.valueOf(2d)),Double.class));
		
	}

	@Override
	@Test
	public void objectNegativeTest() {
		Assert.assertFalse(defaultRead.getObjectByByte(defaultWrite.getBytebyObject(Double.valueOf(2d)),Double.class).equals(Double.valueOf(3d)));
		
	}
	


	@Override
	@Before
	public void init() {
		defaultRead=new DefaultRead();
		defaultWrite=new DefaultWrite();
		
	}

}
