package org.easycassandra.annotations.write;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * This class see and verify the Object to 
 * transform in Byte
 * @author otavio
 *
 */
public class WriteManager {

	private DefaultWrite defaultWrite;
	
	private Map<String, WriteInterface> writeMap;
	
	
	public ByteBuffer convert(Object value) {
		WriteInterface writeInterface=writeMap.get(value.getClass().getName());
		if(writeInterface !=null){
			return writeInterface.getBytebyObject(value);
		}
		return defaultWrite.getBytebyObject(value);
		
	}


	public WriteManager(Map<String, WriteInterface> writeMap) {
		this.writeMap = writeMap;
		 defaultWrite=new DefaultWrite();
	}



	
	
}
