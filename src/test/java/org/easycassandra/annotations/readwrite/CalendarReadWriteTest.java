package org.easycassandra.annotations.readwrite;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.GregorianCalendar;

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

	@Override
	@Test
	public void getObjectByByteNegativeTest() {
		Calendar date=new GregorianCalendar();
		date.add(Calendar.YEAR, 2);
		ByteBuffer bufferCalendar=calendarWrite.getBytebyObject(Calendar.getInstance());
		Calendar other=(Calendar)calendarRead.getObjectByByte(bufferCalendar);
		Assert.assertFalse( date.equals(other));
	}

	@Override
	@Before
	public void init() {
		calendarRead=new CalendarRead();
		calendarWrite=new CalendarWrite();
		
	}

	
}
