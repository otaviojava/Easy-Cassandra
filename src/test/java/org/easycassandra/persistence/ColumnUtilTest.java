package org.easycassandra.persistence;

import java.lang.reflect.Field;

import junit.framework.Assert;

import org.easycassandra.bean.model.Address;
import org.easycassandra.bean.model.Animal;
import org.easycassandra.bean.model.Person;
import org.easycassandra.bean.model.Person2;
import org.junit.Test;

public class ColumnUtilTest {
	
	@Test
	public void getColumnFamilyNameTest(){
		
		
		Assert.assertEquals("person", ColumnUtil.getColumnFamilyName(Person.class));
	}
	
	@Test
	public void getColumnFamilyNameWithDefaultTest(){
		
		
		Assert.assertEquals("Animal", ColumnUtil.getColumnFamilyName(Animal.class));
	}
	
	@Test
	public void getColumnNameTest() {
		Field field=Person.class.getDeclaredFields()[2];
		Assert.assertEquals("name", ColumnUtil.getColumnName(field));
	}
	
	@Test
	public void getColumnNameTestWithDefaultTest() {
		Field field=Animal.class.getDeclaredFields()[2];
		Assert.assertEquals("race", ColumnUtil.getColumnName(field));
	}
	
	@Test
	public void getColumnEnumTest() {
		Field field=Person.class.getDeclaredFields()[4];
		Assert.assertEquals("sex", ColumnUtil.getEnumeratedName(field));
	}
	
	@Test
	public void getColumnEnumTestWithDefaultTest() {
		Field field=Person2.class.getDeclaredFields()[4];
		Assert.assertEquals("sex", ColumnUtil.getEnumeratedName(field));
	}
	
	@Test
	public void getKeyFieldTest(){
		Assert.assertNotNull(ColumnUtil.getKeyField(Person.class));
	}
	
	@Test
	public void getKeyFieldNullTest(){
		Assert.assertNull(ColumnUtil.getKeyField(Address.class));
	}
	
	@Test
	public void getIndexFieldTest(){
		Assert.assertNotNull(ColumnUtil.getIndexField(Person.class));
	}
	
	@Test
	public void getIndexFieldNullTest(){
		Assert.assertNull(ColumnUtil.getIndexField(Address.class));
	}

	
}
