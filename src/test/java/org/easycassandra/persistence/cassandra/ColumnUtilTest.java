package org.easycassandra.persistence.cassandra;

import java.lang.reflect.Field;

import junit.framework.Assert;

import org.easycassandra.bean.model.Address;
import org.easycassandra.bean.model.Animal;
import org.easycassandra.bean.model.Drink;
import org.easycassandra.bean.model.Engineer;
import org.easycassandra.bean.model.Person;
import org.junit.Test;
/**
 * Test to column.
 */
public class ColumnUtilTest {
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    /**
     * run the test.
     */
	@Test
	public void getColumnFamilyNameByTableTest() {

        Assert.assertEquals("schemaA.drink",
                ColumnUtil.INTANCE.getColumnFamilyNameSchema(Drink.class));
	}
	/**
     * run the test.
     */
	@Test
	public void getColumnNameTest() {
		Field field = Person.class.getDeclaredFields()[2];
		Assert.assertEquals("name", ColumnUtil.INTANCE.getColumnName(field));
	}
	/**
     * run the test.
     */
	@Test
	public void getColumnNameTestWithDefaultTest() {
		Field field = Animal.class.getDeclaredFields()[2];
		Assert.assertEquals("race", ColumnUtil.INTANCE.getColumnName(field));
	}
	/**
     * run the test.
     */
	@Test
	public void getColumnEnumTest() {
		Field field = Person.class.getDeclaredFields()[FOUR];
		Assert.assertEquals("sex", ColumnUtil.INTANCE.getEnumeratedName(field));
	}
	/**
     * run the test.
     */
	@Test
	public void getKeyFieldTest() {
		Assert.assertNotNull(ColumnUtil.INTANCE.getKeyField(Person.class));
	}
	/**
     * run the test.
     */
	@Test
	public void getKeyFieldNullTest() {
		Assert.assertNull(ColumnUtil.INTANCE.getKeyField(Address.class));
	}
	/**
     * run the test.
     */
	@Test
	public void getIndexFieldTest() {
		Assert.assertNotNull(ColumnUtil.INTANCE.getIndexField(Person.class));
	}
	/**
     * run the test.
     */
	@Test
	public void getIndexFieldNullTest() {
		Assert.assertNull(ColumnUtil.INTANCE.getIndexField(Address.class));
	}
	/**
     * run the test.
     */
	@Test
	public void isKeyFieldTest() {

        Assert.assertTrue(ColumnUtil.INTANCE.isIdField(Person.class
                .getDeclaredFields()[1]));
	}

	/**
     * run the test.
     */
	@Test
    public void isEmbeddedFieldTest() {

        Assert.assertTrue(ColumnUtil.INTANCE.isEmbeddedField(Person.class
                .getDeclaredFields()[FIVE]));
    }
	/**
     * run the test.
     */
	@Test
	public void isKeyFieldTestFail() {

        Assert.assertFalse(ColumnUtil.INTANCE.isIdField(Person.class
                .getDeclaredFields()[2]));
	}
	/**
     * run the test.
     */
	@Test
	public void isNormalFieldTest() {

        Assert.assertTrue(ColumnUtil.INTANCE.isNormalField(Person.class
                .getDeclaredFields()[2]));
	}
	/**
     * run the test.
     */
	@Test
	public void isNormalFieldTestFail() {

        Assert.assertFalse(ColumnUtil.INTANCE.isNormalField(Person.class
                .getDeclaredFields()[1]));
	}

	/**
     * run the test.
     */
	@Test
	public void isSecundaryIndexFieldTest() {

        Assert.assertTrue(ColumnUtil.INTANCE.isSecundaryIndexField(Person.class
                .getDeclaredFields()[2]));
	}
	/**
     * run the test.
     */
	@Test
	public void isSecundaryIndexFieldTestFail() {

        Assert.assertFalse(ColumnUtil.INTANCE
                .isSecundaryIndexField(Person.class.getDeclaredFields()[1]));
	}

	/**
     * run the test.
     */
	@Test
	public void isEnumFieldTest() {

        Assert.assertTrue(ColumnUtil.INTANCE.isEnumField(Person.class
                .getDeclaredFields()[FOUR]));
	}
	/**
     * run the test.
     */
	@Test
	public void isEnumFieldTestFail() {

        Assert.assertFalse(ColumnUtil.INTANCE.isEnumField(Person.class
                .getDeclaredFields()[1]));
	}
	/**
     * run the test.
     */
	@Test
	public void isMappedSuperclassTest() {

		Assert.assertTrue(ColumnUtil.INTANCE.isMappedSuperclass(Engineer.class));
	}
	/**
     * run the test.
     */
	@Test
	public void listFieldsTest() {

		Assert.assertEquals(ColumnUtil.INTANCE.listFields(Engineer.class).size(), FIVE);
	}
}
