package org.easycassandra.annotations.write;

import java.nio.ByteBuffer;
import java.nio.file.Path;

public class PathWrite implements WriteInterface{

	
	private final FileWrite fileWrite=new FileWrite();
	@Override
	public ByteBuffer getBytebyObject(Object object) {
		
	Path path=(Path) object;
	
		return fileWrite.getBytebyObject(path.toFile());
	}

}
