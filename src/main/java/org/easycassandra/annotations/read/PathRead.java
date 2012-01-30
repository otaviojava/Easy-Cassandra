package org.easycassandra.annotations.read;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Paths;

public class PathRead implements ReadInterface{

	private final FileRead fileRead=new FileRead();
	@Override
	public Object getObjectByByte(ByteBuffer buffer) {
		File file=(File)fileRead.getObjectByByte(buffer);
		
		return Paths.get(file.getAbsolutePath());
	}

}
