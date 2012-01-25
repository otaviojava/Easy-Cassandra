package org.easycassandra.persistence;

import junit.framework.Assert;

import org.junit.Test;

public class ColumnFamilyIdsTest {
	
	@Test
	public void getIdTest(){
		ColumnFamilyIds columnFamilyIds=new ColumnFamilyIds();
		columnFamilyIds.getId("index","keystore");
		Assert.assertEquals(columnFamilyIds.getId("index","keystore"),Long.valueOf(2));
	}
	
	@Test
	public void getIdFailTest(){
		ColumnFamilyIds columnFamilyIds=new ColumnFamilyIds();
		
		Assert.assertFalse(columnFamilyIds.getId("index","keystore").equals(Long.valueOf(2)));
	}

	@Test
	public void getIdFailTest2(){
		ColumnFamilyIds columnFamilyIds=new ColumnFamilyIds();
		columnFamilyIds.getId("index","keystore");
		Assert.assertFalse(columnFamilyIds.getId("index","keystore2").equals(Long.valueOf(2)));
	}
	
}
