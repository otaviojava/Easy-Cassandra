package org.easycassandra.persistence.cassandra.spring;

import java.util.UUID;

import junit.framework.Assert;

import org.easycassandra.persistence.cassandra.spring.entity.Contact;
import org.easycassandra.persistence.cassandra.spring.repository.ContactReporitory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 *  test to ContactRepository.
 */
public class ContactRepositoryTest {

	private static final int TWENTY = 20;
    private ContactReporitory contactReporitory;
	/**
     * run the test.
     */
	@Test
	public void insertTest() {
        Contact contact = new Contact();
		contact.setId(UUID.randomUUID());
		contact.setName("Poliana");
		contact.setYear(TWENTY);
		Assert.assertNotNull(contactReporitory.save(contact));

	}
	/**
	 * run the test.
	 */
	@Test
	public void getFacotryTest() {
        CassandraFactorySpring cassandraFactorySpring = SpringUtil.INSTANCE
                .getBean(CassandraFactorySpring.class);
		Assert.assertNotNull(cassandraFactorySpring);
	}
	/**
     * before the test.
     */
	@Before
	public void initMethod() {
        contactReporitory = SpringUtil.INSTANCE
                .getBean(ContactReporitory.class);
	}
	/**
     * run the all test.
     */
	@BeforeClass
	public static void init() {
        SpringUtil spring = SpringUtil.INSTANCE;
	}

}
