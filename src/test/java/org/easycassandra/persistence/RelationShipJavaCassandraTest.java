package org.easycassandra.persistence;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class RelationShipJavaCassandraTest {

	
	@Test
	public void initTest(){
		Assert.assertNotNull(RelationShipJavaCassandra.INSTANCE);
	}
	
	@Test
	public void getJavaType(){
		String value=RelationShipJavaCassandra.INSTANCE.getJavaValue("ascii");
		Assert.assertEquals("java.lang.String", value);
	}
	
	@Test
	public void getCQLType(){
		List<String> result=RelationShipJavaCassandra.INSTANCE.getCQLType("java.lang.String");
		org.junit.Assert.assertArrayEquals(result.toArray(new String[result.size()]), new String[]{"ascii","text","varchar"});
		
	}
}
