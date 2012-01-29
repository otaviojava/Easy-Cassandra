package org.easycassandra.annotations.readwrite;

import junit.framework.Assert;

import org.easycassandra.annotations.read.DefaultRead;
import org.easycassandra.annotations.write.DefaultWrite;
import org.junit.Before;
import org.junit.Test;

public class ByteReadWriteTest implements ReadWriteNumber{

	private DefaultRead defaultRead;
	
	private DefaultWrite defaultWrite;
	
	@Override
	@Test
	public void primitiveAfirmativeTest() {
		
		Assert.assertEquals((byte)2, defaultRead.getObjectByByte(defaultWrite.getBytebyObject((byte)2),Byte.class));
		
	}

	@Override
	@Test
	public void primitiveNegativeTest() {
		Assert.assertFalse(defaultRead.getObjectByByte(defaultWrite.getBytebyObject((byte)2),Byte.class).equals(((byte)3)));
		
	}
	
	

	@Override
	@Test
	public void objectAfirmativeTest() {
		Assert.assertEquals(Byte.valueOf("1"), defaultRead.getObjectByByte(defaultWrite.getBytebyObject(Byte.valueOf("1")),Byte.class));
		
	}

	@Override
	@Test
	public void objectNegativeTest() {
		Assert.assertFalse(defaultRead.getObjectByByte(defaultWrite.getBytebyObject(Byte.valueOf("2")),Byte.class).equals(Byte.valueOf("3")));
		
	}
	


	@Override
	@Before
	public void init() {
		defaultRead=new DefaultRead();
		defaultWrite=new DefaultWrite();
		
	}

}
