package org.easycassandra.annotations.readwrite;

import java.util.Date;

import junit.framework.Assert;

import org.easycassandra.annotations.read.DateRead;
import org.easycassandra.annotations.write.DateWrite;
import org.junit.Before;
import org.junit.Test;


public class DateReadWriteTest implements ReadWriteTest{


    private DateRead dateRead;
    
    private DateWrite dateWrite;
	
    @Test
	@Override
	public void getObjectByByteAfirmativeTest() {
		Date date=new Date();
		Assert.assertEquals(date, dateRead.getObjectByByte(dateWrite.getBytebyObject(date)));
		
	}

	@SuppressWarnings("deprecation")
	@Override
	@Test
	public void getObjectByByteNegativeTest() {
		Date date=new Date();
		date.setMonth(date.getMonth()+2);
		Assert.assertFalse( date.equals(dateRead.getObjectByByte(dateWrite.getBytebyObject(new Date()))));
	}

	@Override
	@Before
	public void init() {
		dateRead=new DateRead();
		dateWrite=new DateWrite();
		
	}

	
}
