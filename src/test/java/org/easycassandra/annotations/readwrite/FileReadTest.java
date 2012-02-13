package org.easycassandra.annotations.readwrite;

import java.io.File;
import java.nio.ByteBuffer;

import org.easycassandra.annotations.read.FileRead;
import org.easycassandra.annotations.write.FileWrite;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FileReadTest {

	private FileRead fileRead;
	private FileWrite fileWrite;
	
	@Test
	public void getObjectByByteTest(){
		File file =new File("readme.txt");
		ByteBuffer byffer=fileWrite.getBytebyObject(file);
		Assert.assertNotNull(fileRead.getObjectByByte(byffer));	
	}
	
	@Test
	public void getObjectByByteSizeTest(){
		File file =new File("readme.txt");
		ByteBuffer byffer=fileWrite.getBytebyObject(file);
		Assert.assertEquals(((File)fileRead.getObjectByByte(byffer)).length(),file.length());	
	}
	
	
	
	@Before
	public void init(){
		fileRead=new FileRead();
		fileWrite=new FileWrite();
	}
}
