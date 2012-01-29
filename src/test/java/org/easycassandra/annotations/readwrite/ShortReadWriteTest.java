package org.easycassandra.annotations.readwrite;

import junit.framework.Assert;

import org.easycassandra.annotations.read.DefaultRead;
import org.easycassandra.annotations.write.DefaultWrite;
import org.junit.Before;
import org.junit.Test;

public class ShortReadWriteTest implements ReadWriteNumber{

	private DefaultRead defaultRead;
	
	private DefaultWrite defaultWrite;
	
	@Override
	@Test
	public void primitiveAfirmativeTest() {
		
		Assert.assertEquals((short)2, defaultRead.getObjectByByte(defaultWrite.getBytebyObject((short)2),Short.class));
		
	}

	@Override
	@Test
	public void primitiveNegativeTest() {
		Assert.assertFalse(defaultRead.getObjectByByte(defaultWrite.getBytebyObject((short)2),Short.class).equals((short)3));
		
	}
	
	

	@Override
	@Test
	public void objectAfirmativeTest() {
		Assert.assertEquals(Short.valueOf("1"), defaultRead.getObjectByByte(defaultWrite.getBytebyObject(Short.valueOf("1")),Short.class));
		
	}

	@Override
	@Test
	public void objectNegativeTest() {
		Assert.assertFalse(defaultRead.getObjectByByte(defaultWrite.getBytebyObject(Short.valueOf("2")),Short.class).equals(Short.valueOf("3")));
		
	}
	


	@Override
	@Before
	public void init() {
		defaultRead=new DefaultRead();
		defaultWrite=new DefaultWrite();
		
	}

}
