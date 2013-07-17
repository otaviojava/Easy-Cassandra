package org.easycassandra.persistence;

import java.lang.reflect.Field;

import junit.framework.Assert;

import org.easycassandra.bean.model.Address;
import org.easycassandra.bean.model.Animal;
import org.easycassandra.bean.model.Count;
import org.easycassandra.bean.model.Drink;
import org.easycassandra.bean.model.Engineer;
import org.easycassandra.bean.model.Person;
import org.easycassandra.bean.model.Person2;
import org.junit.Test;

public class ColumnUtilTest {
	
	@Test
	public void getColumnFamilyNameTest(){
		
		
		Assert.assertEquals("person", ColumnUtil.INTANCE.getColumnFamilyName(Person.class));
	}
	@Test
	public void getColumnFamilyNameByTableTest(){
		
		
		Assert.assertEquals("drink", ColumnUtil.INTANCE.getColumnFamilyName(Drink.class));
	}
	
	@Test
	public void getColumnFamilyNameWithDefaultTest(){
		
		
		Assert.assertEquals("Animal", ColumnUtil.INTANCE.getColumnFamilyName(Animal.class));
	}
	
	@Test
	public void getColumnNameTest() {
		Field field=Person.class.getDeclaredFields()[2];
		Assert.assertEquals("name", ColumnUtil.INTANCE.getColumnName(field));
	}
	
	@Test
	public void getColumnNameTestWithDefaultTest() {
		Field field=Animal.class.getDeclaredFields()[2];
		Assert.assertEquals("race", ColumnUtil.INTANCE.getColumnName(field));
	}
	
	@Test
	public void getColumnEnumTest() {
		Field field=Person.class.getDeclaredFields()[4];
		Assert.assertEquals("sex", ColumnUtil.INTANCE.getEnumeratedName(field));
	}
	
	@Test
	public void getColumnEnumTestWithDefaultTest() {
		Field field=Person2.class.getDeclaredFields()[4];
		Assert.assertEquals("sex", ColumnUtil.INTANCE.getEnumeratedName(field));
	}
	
	@Test
	public void getKeyFieldTest(){
		Assert.assertNotNull(ColumnUtil.INTANCE.getKeyField(Person.class));
	}
	
	@Test
	public void getKeyFieldNullTest(){
		Assert.assertNull(ColumnUtil.INTANCE.getKeyField(Address.class));
	}
	
	@Test
	public void getIndexFieldTest(){
		Assert.assertNotNull(ColumnUtil.INTANCE.getIndexField(Person.class));
	}
	
	@Test
	public void getIndexFieldNullTest(){
		Assert.assertNull(ColumnUtil.INTANCE.getIndexField(Address.class));
	}

	@Test
	public void isKeyFieldTest(){
		
		Assert.assertTrue(ColumnUtil.INTANCE.isIdField(Person.class.getDeclaredFields()[1]));		
	}
	
	@Test
	public void isGeneratedValueTest(){
		
		Assert.assertTrue(ColumnUtil.INTANCE.isGeneratedValue(Count.class.getDeclaredFields()[1]));		
	}
	
	@Test
	public void isEmbeddedFieldTest(){
		
		Assert.assertTrue(ColumnUtil.INTANCE.isEmbeddedField(Person.class.getDeclaredFields()[6]));		
	}
	
	@Test
	public void isVersionFieldTest(){
		
		Assert.assertTrue(ColumnUtil.INTANCE.isVersionField(Count.class.getDeclaredFields()[3]));		
	}
	
	
	@Test
	public void isKeyFieldTestFail(){
		
		Assert.assertFalse(ColumnUtil.INTANCE.isIdField(Person.class.getDeclaredFields()[2]));		
	}
	
	@Test
	public void isNormalFieldTest(){
		
		Assert.assertTrue(ColumnUtil.INTANCE.isNormalField(Person.class.getDeclaredFields()[2]));		
	}
	
	@Test
	public void isNormalFieldTestFail(){
		
		Assert.assertFalse(ColumnUtil.INTANCE.isNormalField(Person.class.getDeclaredFields()[1]));		
	}
	
	
	@Test
	public void isSecundaryIndexFieldTest(){
		
		Assert.assertTrue(ColumnUtil.INTANCE.isSecundaryIndexField(Person.class.getDeclaredFields()[2]));		
	}
	
	@Test
	public void isSecundaryIndexFieldTestFail(){
		
		Assert.assertFalse(ColumnUtil.INTANCE.isSecundaryIndexField(Person.class.getDeclaredFields()[1]));		
	}
	

	@Test
	public void isEnumFieldTest(){
		
		Assert.assertTrue(ColumnUtil.INTANCE.isEnumField(Person.class.getDeclaredFields()[4]));		
	}
	
	@Test
	public void isEnumFieldTestFail(){
		
		Assert.assertFalse(ColumnUtil.INTANCE.isEnumField(Person.class.getDeclaredFields()[1]));		
	}
	
	@Test
	public void isMappedSuperclassTest(){
		
		Assert.assertTrue(ColumnUtil.INTANCE.isMappedSuperclass(Engineer.class));
	}
	
	@Test
	public void listFieldsTest(){
		
		Assert.assertEquals(ColumnUtil.INTANCE.listFields(Engineer.class).size(), 7);
	}
	@Test
	public void getSchemaTest(){
		Assert.assertEquals(ColumnUtil.INTANCE.getSchema(Drink.class), "schemaA");
	}
	
	@Test
	public void getSchemaNullTest(){
		Assert.assertNull(ColumnUtil.INTANCE.getSchema(Person.class));
	}
}
