package org.easycassandra.annotations.readwrite;

import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.easycassandra.annotations.read.PathRead;
import org.easycassandra.annotations.write.PathWrite;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PathReadTest {

	private PathRead fileRead;
	private PathWrite fileWrite;
	
	@Test
	public void getObjectByByteTest(){
		Path file =Paths.get("readme.txt");
		ByteBuffer byffer=fileWrite.getBytebyObject(file);
		Assert.assertNotNull(fileRead.getObjectByByte(byffer));	
	}
	
	@Test
	public void getObjectByByteSizeTest(){
		Path file =Paths.get("readme.txt");
		ByteBuffer byffer=fileWrite.getBytebyObject(file);
		Assert.assertEquals(((Path)fileRead.getObjectByByte(byffer)).toFile().length(),file.toFile().length());	
	}
	
	
	
	@Before
	public void init(){
		fileRead=new PathRead();
		fileWrite=new PathWrite();
	}
}
