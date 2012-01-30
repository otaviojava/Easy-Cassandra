package org.easycassandra.annotations.read;

import java.nio.ByteBuffer;

import org.easycassandra.util.EncodingUtil;
import org.easycassandra.util.FileUtil;

public class FileRead implements ReadInterface{

	@Override
	public Object getObjectByByte(ByteBuffer buffer) {
		String value=EncodingUtil.byteToString(buffer);
		String nameFile=value.substring(0,value.indexOf("|"));
		String[] byteString=value.substring(value.indexOf("|")+1,value.lastIndexOf(",")).split(",");
		byte[]  bytes=new byte[byteString.length];
		
		
		for(int index=0;index<byteString.length;index++){
			bytes[index]=Byte.valueOf(byteString[index]);
		}
		
		return FileUtil.byteFromFile(bytes, nameFile);
	}

}
