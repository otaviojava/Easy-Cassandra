package org.easycassandra.util;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

public class FileUtilTest {

	private final String nomeArquivo="arquivotxt";
	
	@Test
	public void fileToByteArrayTest(){
		File file =new File("readme.txt");
		Assert.assertEquals(file.length(), FileUtil.fileToByteArray(file).length);
	}

	@Test
	public void byteFromFileTest() {
		File file =new File("readme.txt");
		
		File test=FileUtil.byteFromFile(FileUtil.fileToByteArray(file), nomeArquivo);
		test.deleteOnExit();
		Assert.assertEquals(file.length(), test.length());
	}
	
}
