package org.easycassandra.persistence;

import java.lang.reflect.Field;

import junit.framework.Assert;

import org.easycassandra.bean.model.Address;
import org.easycassandra.bean.model.Animal;
import org.easycassandra.bean.model.Count;
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

	@Test
	public void isKeyFieldTest(){
		
		Assert.assertTrue(ColumnUtil.isIdField(Person.class.getDeclaredFields()[1]));		
	}
	
	@Test
	public void isGeneratedValueTest(){
		
		Assert.assertTrue(ColumnUtil.isGeneratedValue(Count.class.getDeclaredFields()[1]));		
	}
	
	@Test
	public void isEmbeddedFieldTest(){
		
		Assert.assertTrue(ColumnUtil.isEmbeddedField(Person.class.getDeclaredFields()[6]));		
	}
	
	@Test
	public void isVersionFieldTest(){
		
		Assert.assertTrue(ColumnUtil.isVersionField(Count.class.getDeclaredFields()[3]));		
	}
	
	
	@Test
	public void isKeyFieldTestFail(){
		
		Assert.assertFalse(ColumnUtil.isIdField(Person.class.getDeclaredFields()[2]));		
	}
	
	@Test
	public void isNormalFieldTest(){
		
		Assert.assertTrue(ColumnUtil.isNormalField(Person.class.getDeclaredFields()[2]));		
	}
	
	@Test
	public void isNormalFieldTestFail(){
		
		Assert.assertFalse(ColumnUtil.isNormalField(Person.class.getDeclaredFields()[1]));		
	}
	
	
	@Test
	public void isSecundaryIndexFieldTest(){
		
		Assert.assertTrue(ColumnUtil.isSecundaryIndexField(Person.class.getDeclaredFields()[2]));		
	}
	
	@Test
	public void isSecundaryIndexFieldTestFail(){
		
		Assert.assertFalse(ColumnUtil.isSecundaryIndexField(Person.class.getDeclaredFields()[1]));		
	}
	

	@Test
	public void isEnumFieldTest(){
		
		Assert.assertTrue(ColumnUtil.isEnumField(Person.class.getDeclaredFields()[4]));		
	}
	
	@Test
	public void isEnumFieldTestFail(){
		
		Assert.assertFalse(ColumnUtil.isEnumField(Person.class.getDeclaredFields()[1]));		
	}
	
	
}
