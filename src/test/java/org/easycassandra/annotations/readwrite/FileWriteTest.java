package org.easycassandra.annotations.readwrite;

import java.io.File;

import org.easycassandra.annotations.write.FileWrite;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FileWriteTest {
	
	private FileWrite fileWrite;
	
	@Test
	public void getBytebyObjectTest(){
	File file =new File("readme.txt");
	Assert.assertNotNull(fileWrite.getBytebyObject(file));
	}

	@Test
	public void getBytebyObjectSizeTest(){
	File file =new File("readme.txt");
	Assert.assertTrue(fileWrite.getBytebyObject(file).array().length> file.length());
	}

	
	@Before
	public void init(){
		fileWrite=new FileWrite();
	}
}
