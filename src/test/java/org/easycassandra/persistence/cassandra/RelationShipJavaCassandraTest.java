package org.easycassandra.persistence.cassandra;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
/**
 * test the RelationShipJavaCassandra.
 * @author otaviojava
 */
public class RelationShipJavaCassandraTest {

    /**
     * run the test.
     */
	@Test
	public void initTest() {
		Assert.assertNotNull(RelationShipJavaCassandra.INSTANCE);
	}
	/**
     * run the test.
     */
	@Test
	public void getJavaType() {
		String value = RelationShipJavaCassandra.INSTANCE.getJavaValue("ascii");
		Assert.assertEquals("java.lang.String", value);
	}
	/**
     * run the test.
     */
	@Test
	public void getCQLType() {
        List<String> result = RelationShipJavaCassandra.INSTANCE
                .getCQLType("java.lang.String");
        org.junit.Assert.assertArrayEquals(
                result.toArray(new String[result.size()]), new String[] {
                    "ascii", "text", "varchar" });

	}
}
