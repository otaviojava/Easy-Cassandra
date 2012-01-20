package org.easycassandra.annotations.readwrite;

import junit.framework.Assert;

import org.easycassandra.annotations.read.FloatRead;
import org.easycassandra.annotations.write.FloatWrite;
import org.junit.Before;
import org.junit.Test;

public class FloatReadWrite implements ReadWriteNumber{

	private FloatRead floatRead;
	
	private FloatWrite floatWrite;
	
	@Override
	@Test
	public void primitiveAfirmativeTest() {
		
		Assert.assertEquals(2.2f, floatRead.getObjectByByte(floatWrite.getBytebyObject(2.2f)));
		
	}

	@Override
	@Test
	public void primitiveNegativeTest() {
		Assert.assertFalse(floatRead.getObjectByByte(floatWrite.getBytebyObject(2f)).equals(3f));
		
	}
	
	

	@Override
	@Test
	public void objectAfirmativeTest() {
		Assert.assertEquals(Float.valueOf(1.42f), floatRead.getObjectByByte(floatWrite.getBytebyObject(Float.valueOf(1.42f))));
		
	}

	@Override
	@Test
	public void objectNegativeTest() {
		Assert.assertFalse(floatRead.getObjectByByte(floatWrite.getBytebyObject(Float.valueOf(2f))).equals(Float.valueOf(3f)));
		
	}
	


	@Override
	@Before
	public void init() {
		floatRead=new FloatRead();
		floatWrite=new FloatWrite();
		
	}

}
