package org.easycassandra.annotations.read;

import java.nio.ByteBuffer;
import java.util.Map;


/**
 * This class see and verify the Object to transform Byte to Object
 * @author otavio
 *
 */
public class ReadManager {
	
	/**
	 * the default value
	 * @see DefaultRead
	 */
	private DefaultRead defaultRead;
	
	/**
	 * For read value Path
	 * @see java#<
	 */
	private PathRead pathRead;
	
/**
 *Map with reference for transform Byte in Object
 *@see ReadInterface 	
 */
private Map<String, ReadInterface> readMap;

/**
 * verify if exist ReadInterface for transform ByteBuffer to Object
 * if not exist will return null value
 * @param byteBuffer - value
 * @param qualifieldName - the full name of the class
 * @return the object if not exist null
 */
public Object convert(ByteBuffer byteBuffer,@SuppressWarnings("rawtypes") Class qualifieldName){
	ReadInterface readInterface=readMap.get(qualifieldName.getName());
	if(readInterface!=null){
		return readInterface.getObjectByByte(byteBuffer);
	}
	return defaultRead.getObjectByByte(byteBuffer,qualifieldName);
}


	public ReadManager(Map<String, ReadInterface> readMap) {
		this.readMap=readMap;
		defaultRead=new DefaultRead();
		pathRead=new PathRead();
	}

}
