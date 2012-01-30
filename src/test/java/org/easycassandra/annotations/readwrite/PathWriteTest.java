package org.easycassandra.annotations.readwrite;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.easycassandra.annotations.write.PathWrite;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PathWriteTest {
	
	private PathWrite fileWrite;
	
	@Test
	public void getBytebyObjectTest(){
	Path path =Paths.get("readme.txt");
	path.getClass().getName();
	Assert.assertNotNull(fileWrite.getBytebyObject(path));
	}

	@Test
	public void getBytebyObjectSizeTest(){
	Path file =Paths.get("readme.txt");
	Assert.assertTrue(fileWrite.getBytebyObject(file).array().length> file.toFile().length());
	}

	
	@Before
	public void init(){
		fileWrite=new PathWrite();
	}
}
