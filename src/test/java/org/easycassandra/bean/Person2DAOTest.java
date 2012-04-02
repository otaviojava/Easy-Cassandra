package org.easycassandra.bean;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.Address;
import org.easycassandra.bean.model.Person2;
import org.easycassandra.bean.model.Sex;
import org.junit.Assert;
import org.junit.Test;

/**
*
* @author otavio
*/
public class Person2DAOTest {
	  private PersistenceDao<Person2> dao = new PersistenceDao<>(Person2.class);
	  	@Test
	    public void insertErrorTest() {
	        System.out.println("Inserindo exemplo");
	        Person2 person = getPerson();
	        person.setName("otavio teste");
	        person.setId(4l);
	        Address address = getAddress();
	        person.setAddress(address);
	        
	        Assert.assertFalse(dao.insert(person));
	        
	     
	    }

	    @Test
	    public void retriveKeyErrorTest(){
	    
	    	Assert.assertNull(dao.listByIndex(2));
	    }
	    
	    
	    private Address getAddress() {
	        Address address = new Address();
	        address.setCep("40243-543");
	        address.setCity("Salvaor");
	        address.setState("Bahia");
	        return address;

	    }

	    private Person2 getPerson() {
	        Person2 person = new Person2();
	        person.setYear(10);
	        person.setName("Name Person ");
	        person.setSex(Sex.MALE);

	        return person;

	    }
	    
	    
    
    
}