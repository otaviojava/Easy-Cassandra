package org.easycassandra.annotations.readwrite;

import junit.framework.Assert;

import org.easycassandra.annotations.read.IntegerRead;
import org.easycassandra.annotations.write.IntegerWrite;
import org.junit.Before;
import org.junit.Test;

public class IntegerReadWrite implements ReadWriteNumber{

	private IntegerRead integerRead;
	
	private IntegerWrite integerWrite;
	
	@Override
	@Test
	public void primitiveAfirmativeTest() {
		
		Assert.assertEquals(2, integerRead.getObjectByByte(integerWrite.getBytebyObject(2)));
		
	}

	@Override
	@Test
	public void primitiveNegativeTest() {
		Assert.assertFalse(integerRead.getObjectByByte(integerWrite.getBytebyObject(2)).equals(3));
		
	}
	
	

	@Override
	@Test
	public void objectAfirmativeTest() {
		Assert.assertEquals(Integer.valueOf(2), integerRead.getObjectByByte(integerWrite.getBytebyObject(Integer.valueOf(2))));
		
	}

	@Override
	@Test
	public void objectNegativeTest() {
		Assert.assertFalse(integerRead.getObjectByByte(integerWrite.getBytebyObject(Integer.valueOf(2))).equals(Integer.valueOf(3)));
		
	}
	


	@Override
	@Before
	public void init() {
		integerRead=new IntegerRead();
		integerWrite=new IntegerWrite();
		
	}

}
