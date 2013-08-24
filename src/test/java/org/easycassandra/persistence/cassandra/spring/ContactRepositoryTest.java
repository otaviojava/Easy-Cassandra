package org.easycassandra.persistence.cassandra.spring;

import java.util.UUID;

import junit.framework.Assert;

import org.easycassandra.persistence.cassandra.spring.entity.Contact;
import org.easycassandra.persistence.cassandra.spring.repository.ContactReporitory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ContactRepositoryTest {
	
	
	private ContactReporitory contactReporitory;
	
	@Test
	public void insertTest(){
		Contact contact=new Contact();
		contact.setId(UUID.randomUUID());
		contact.setName("Poliana");
		contact.setYear(20);
		Assert.assertNotNull(contactReporitory.save(contact));
		
	}
	
	@Test
	public void getFacotryTest(){
		CassandraFactorySpring cassandraFactorySpring=SpringUtil.INSTANCE.getBean(CassandraFactorySpring.class);
		Assert.assertNotNull(cassandraFactorySpring);
	}
	
	@Before
	public void initMethod(){
		contactReporitory=SpringUtil.INSTANCE.getBean(ContactReporitory.class);
	}
	@BeforeClass
	public static void init(){
		SpringUtil spring=SpringUtil.INSTANCE;
	}

}
