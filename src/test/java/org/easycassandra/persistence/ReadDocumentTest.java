package org.easycassandra.persistence;

import java.io.File;

import org.easycassandra.util.DomUtil;
import org.junit.Assert;
import org.junit.Test;


public class ReadDocumentTest {

	@Test
	public void readTest(){
		ReadDocument readDocument=new ReadDocument();
		Assert.assertNotNull(readDocument.read());
	}
	
	@Test
	public void readFailTest(){
		File file=new File(DomUtil.FILE);
		file.delete();
		ReadDocument readDocument=new ReadDocument();
		Assert.assertNotNull(readDocument.read());
	}
	
	@Test
	public void readWithInformationTest(){
		String index="index";
		String keyStore="keystore";
		ColumnFamilyIds columnFamilyIds=new ColumnFamilyIds();
		columnFamilyIds.getId(index,keyStore);
		DomUtil.getFileDom(columnFamilyIds);		
		ReadDocument readDocument=new ReadDocument();
		ColumnFamilyIds columnFamilyIds2=readDocument.read();
		Assert.assertEquals(columnFamilyIds.getId(index, keyStore),columnFamilyIds2.getId(index, keyStore));
		
	}
	
	@Test
	public void readWithInformationFailTest(){
		String index="index";
		String keyStore="keystore";
		ColumnFamilyIds columnFamilyIds=new ColumnFamilyIds();
		columnFamilyIds.getId(index,keyStore);
		DomUtil.getFileDom(columnFamilyIds);		
		ReadDocument readDocument=new ReadDocument();
		ColumnFamilyIds columnFamilyIds2=readDocument.read();
		columnFamilyIds.getId(index,keyStore);
		Assert.assertFalse(columnFamilyIds.getId(index, keyStore).equals(columnFamilyIds2.getId(index, keyStore)));
		
	}
	
}

