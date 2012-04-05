package org.easycassandra.persistence;

import org.easycassandra.bean.model.Person;
import org.junit.Assert;
import org.junit.Test;

public class EasyCassandraManagerTest {
	
	
	@Test
	public void getCleintTest(){
		Assert.assertNotNull(EasyCassandraManager.getClient("javabahia", "localhost", 9160));
	}
	
	 
	
	@Test
	public void getPersistenceTest(){
		Assert.assertNotNull(EasyCassandraManager.getPersistence("javabahia", "localhost", 9160));
	}
	
	@Test
	public void addColumnFamilyTest(){
		
		Assert.assertTrue(EasyCassandraManager.addFamilyObject(Person.class));
	}
	
	@Test
	public void getFamilyObjectTest(){
		Assert.assertNotNull(EasyCassandraManager.getFamily("Person"));
	}
	
}
