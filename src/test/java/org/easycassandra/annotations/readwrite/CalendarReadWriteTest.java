package org.easycassandra.annotations.readwrite;

import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.easycassandra.annotations.read.CalendarRead;
import org.easycassandra.annotations.write.CalendarWrite;
import org.junit.Before;
import org.junit.Test;


public class CalendarReadWriteTest implements ReadWriteTest{


    private CalendarRead calendarRead;
    
    private CalendarWrite calendarWrite;
	
    @Test
	@Override
	public void getObjectByByteAfirmativeTest() {
		Calendar date=Calendar.getInstance();
		Assert.assertEquals(date, calendarRead.getObjectByByte(calendarWrite.getBytebyObject(date)));
		
	}

	@SuppressWarnings("deprecation")
	@Override
	@Test
	public void getObjectByByteNegativeTest() {
		Calendar date=Calendar.getInstance();
		date.set(Calendar.MONTH, 2);
		Assert.assertFalse( date.equals(calendarRead.getObjectByByte(calendarWrite.getBytebyObject(Calendar.getInstance()))));
	}

	@Override
	@Before
	public void init() {
		calendarRead=new CalendarRead();
		calendarWrite=new CalendarWrite();
		
	}

	
}
