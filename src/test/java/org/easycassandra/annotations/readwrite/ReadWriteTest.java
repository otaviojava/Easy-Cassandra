package org.easycassandra.annotations.readwrite;


public interface ReadWriteTest {

	/**
	 * Test for afirmate
	 */
	public void getObjectByByteAfirmativeTest();
	
	/**
	 * 
	 */
	public void getObjectByByteNegativeTest();
	
	/**
	 * for initialize some objects
	 */
	
	public void init();
}
