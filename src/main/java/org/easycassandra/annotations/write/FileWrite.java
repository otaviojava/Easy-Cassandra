package org.easycassandra.annotations.write;

import java.io.File;
import java.nio.ByteBuffer;

import org.easycassandra.util.EncodingUtil;
import org.easycassandra.util.FileUtil;

public class FileWrite implements WriteInterface{

	@Override
	public ByteBuffer getBytebyObject(Object object) {
		
		File file=(File)object;
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append(file.getName()+"|");
		for(Byte fileByte: FileUtil.fileToByteArray(file)){
   		   stringBuilder.append(fileByte+",");
		}
	
			
		
		return EncodingUtil.stringToByte(stringBuilder.toString());
	}

}
