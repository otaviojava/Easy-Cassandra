package org.easycassandra.persistence;

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
	
	
}
