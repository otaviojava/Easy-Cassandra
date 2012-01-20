package org.easycassandra.annotations.readwrite;

import junit.framework.Assert;

import org.easycassandra.annotations.read.DoubleRead;
import org.easycassandra.annotations.write.DoubleWrite;
import org.junit.Before;
import org.junit.Test;

public class DoubleReadWrite implements ReadWriteNumber{

	private DoubleRead doubleRead;
	
	private DoubleWrite doubleWrite;
	
	@Override
	@Test
	public void primitiveAfirmativeTest() {
		
		Assert.assertEquals(2d, doubleRead.getObjectByByte(doubleWrite.getBytebyObject(2d)));
		
	}

	@Override
	@Test
	public void primitiveNegativeTest() {
		Assert.assertFalse(doubleRead.getObjectByByte(doubleWrite.getBytebyObject(2d)).equals(3d));
		
	}
	
	

	@Override
	@Test
	public void objectAfirmativeTest() {
		Assert.assertEquals(Double.valueOf(2d), doubleRead.getObjectByByte(doubleWrite.getBytebyObject(Double.valueOf(2d))));
		
	}

	@Override
	@Test
	public void objectNegativeTest() {
		Assert.assertFalse(doubleRead.getObjectByByte(doubleWrite.getBytebyObject(Double.valueOf(2d))).equals(Double.valueOf(3d)));
		
	}
	


	@Override
	@Before
	public void init() {
		doubleRead=new DoubleRead();
		doubleWrite=new DoubleWrite();
		
	}

}
