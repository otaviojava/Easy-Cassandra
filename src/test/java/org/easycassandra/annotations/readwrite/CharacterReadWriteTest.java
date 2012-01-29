package org.easycassandra.annotations.readwrite;

import junit.framework.Assert;

import org.easycassandra.annotations.read.DefaultRead;
import org.easycassandra.annotations.write.DefaultWrite;
import org.junit.Before;
import org.junit.Test;

public class CharacterReadWriteTest implements ReadWriteNumber{

	private DefaultRead defaultRead;
	
	private DefaultWrite defaultWrite;
	
	@Override
	@Test
	public void primitiveAfirmativeTest() {
		
		Assert.assertEquals((char)2, defaultRead.getObjectByByte(defaultWrite.getBytebyObject((char)2),Character.class));
		
	}

	@Override
	@Test
	public void primitiveNegativeTest() {
		Assert.assertFalse(defaultRead.getObjectByByte(defaultWrite.getBytebyObject((char)2),Character.class).equals(((char)3)));
		
	}
	
	

	@Override
	@Test
	public void objectAfirmativeTest() {
		Character charA='s';
		Assert.assertEquals(charA, defaultRead.getObjectByByte(defaultWrite.getBytebyObject(charA),Character.class));
		
	}

	@Override
	@Test
	public void objectNegativeTest() {
		Character charA='s';
		Character charb='b';
		Assert.assertFalse(defaultRead.getObjectByByte(defaultWrite.getBytebyObject(charA),Character.class).equals(charb));
		
	}
	


	@Override
	@Before
	public void init() {
		defaultRead=new DefaultRead();
		defaultWrite=new DefaultWrite();
		
	}

}
