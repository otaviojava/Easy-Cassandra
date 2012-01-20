package org.easycassandra.annotations.readwrite;

import junit.framework.Assert;

import org.easycassandra.annotations.read.LongRead;
import org.easycassandra.annotations.write.LongWrite;
import org.junit.Before;
import org.junit.Test;

public class LongReadWrite implements ReadWriteNumber{

	private LongRead longRead;
	
	private LongWrite longWrite;
	
	@Override
	@Test
	public void primitiveAfirmativeTest() {
		
		Assert.assertEquals(2l, longRead.getObjectByByte(longWrite.getBytebyObject(2l)));
		
	}

	@Override
	@Test
	public void primitiveNegativeTest() {
		Assert.assertFalse(longRead.getObjectByByte(longWrite.getBytebyObject(2l)).equals(3l));
		
	}
	
	

	@Override
	@Test
	public void objectAfirmativeTest() {
		Assert.assertEquals(Long.valueOf(2), longRead.getObjectByByte(longWrite.getBytebyObject(Long.valueOf(2))));
		
	}

	@Override
	@Test
	public void objectNegativeTest() {
		Assert.assertFalse(longRead.getObjectByByte(longWrite.getBytebyObject(Long.valueOf(2))).equals(Long.valueOf(3)));
		
	}
	


	@Override
	@Before
	public void init() {
		longRead=new LongRead();
		longWrite=new LongWrite();
		
	}

}
