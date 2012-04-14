package org.easycassandra.persistence;

import org.easycassandra.bean.model.Person;
import org.junit.Assert;
import org.junit.BeforeClass;
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
	
	@BeforeClass
	public static void  initClass(){
		EasyCassandraManager.addFamilyObject(Person.class);
	}
	
	@Test
	public void getFamilyObjectTest(){
		Assert.assertNotNull(EasyCassandraManager.getFamily("Person"));
	}
	
	@Test
	public void getFamilyObjectSubFieldTest(){
		DescribeFamilyObject ao=EasyCassandraManager.getFamily("Person");
		Assert.assertEquals(ao.getFieldsName("cep"),"address.cep");
	}
	
	@Test
	public void getFamilyObjectSubClassTest(){
		DescribeFamilyObject ao=EasyCassandraManager.getFamily("Person");
		Assert.assertEquals(ao.getFieldsName("state , cyte , street , cep"),"address");
	}
	
}
