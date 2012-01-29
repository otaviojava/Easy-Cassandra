package org.easycassandra.annotations.readwrite;

import junit.framework.Assert;

import org.easycassandra.annotations.read.DefaultRead;
import org.easycassandra.annotations.write.DefaultWrite;
import org.junit.Before;
import org.junit.Test;

public class LongReadWriteTest implements ReadWriteNumber{

	private DefaultRead defaultRead;
	
	private DefaultWrite defaultWrite;
	
	@Override
	@Test
	public void primitiveAfirmativeTest() {
		
		Assert.assertEquals(2l, defaultRead.getObjectByByte(defaultWrite.getBytebyObject(2l),Long.class));
		
	}

	@Override
	@Test
	public void primitiveNegativeTest() {
		Assert.assertFalse(defaultRead.getObjectByByte(defaultWrite.getBytebyObject(2l),Long.class).equals(3l));
		
	}
	
	

	@Override
	@Test
	public void objectAfirmativeTest() {
		Assert.assertEquals(Long.valueOf(2), defaultRead.getObjectByByte(defaultWrite.getBytebyObject(Long.valueOf(2)),Long.class));
		
	}

	@Override
	@Test
	public void objectNegativeTest() {
		Assert.assertFalse(defaultRead.getObjectByByte(defaultWrite.getBytebyObject(Long.valueOf(2)),Long.class).equals(Long.valueOf(3)));
		
	}
	


	@Override
	@Before
	public void init() {
		defaultRead=new DefaultRead();
		defaultWrite=new DefaultWrite();
		
	}

}
