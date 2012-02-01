package org.easycassandra.annotations.write;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Map;

/**
 * This class see and verify the Object to 
 * transform in Byte
 * @author otavio
 *
 */
public class WriteManager {

	private DefaultWrite defaultWrite;
	
	private PathWrite pathWrite;
	
	private FileWrite fileWrite;
	
	private CalendarWrite calendarWrite;
	
	private Map<String, WriteInterface> writeMap;
	
	
	public ByteBuffer convert(Object value) {
	
		WriteInterface writeInterface=writeMap.get(value.getClass().getName());
		if(writeInterface !=null){
			return writeInterface.getBytebyObject(value);
		}else if(value instanceof Path){
			return pathWrite.getBytebyObject(value);
		}else if(value instanceof File){
			return fileWrite.getBytebyObject(value);
		}else if(value instanceof Calendar){
			return calendarWrite.getBytebyObject(value);
		}
		
		return defaultWrite.getBytebyObject(value);
		
	}


	public WriteManager(Map<String, WriteInterface> writeMap) {
		this.writeMap = writeMap;
		 defaultWrite=new DefaultWrite();
		 pathWrite=new PathWrite();
		 fileWrite=new FileWrite();
		 calendarWrite=new CalendarWrite();
	}



	
	
}
