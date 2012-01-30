package org.easycassandra.bean;

import java.io.File;

import org.easycassandra.bean.Address;
import org.easycassandra.bean.Person;
import org.easycassandra.bean.Sex;
import org.junit.Assert;
import org.junit.Test;

/**
*
* @author otavio
*/
public class PersonDAOTest {
	  private PersonDAO dao = new PersonDAO();

	    @Test
	    public void insertTest() {
	        System.out.println("Inserindo exemplo");
	        Person person = getPerson();
	        person.setName("otavio teste");
	        person.setId(2l);
	        Address address = getAddress();
	        person.setAddress(address);

	        Assert.assertTrue(dao.insert(person));
	    }

	    @Test
	    public void retrieveTest() {
	        Person person = dao.retrieve(4l);
	        Assert.assertEquals(person.getName(), "Name Person ");
	    }

	    @Test
	    public void overrideTest() {

	        Person person = getPerson();
	        person.setId(1l);
	        Assert.assertTrue(dao.insert(person));
	    }

	    @Test
	    public void removefromRowKeyTest() {

	        Assert.assertTrue(dao.removeFromRowKey(new Long(2)));

	    }

	    @Test
	    public void removeTest() {
	        Person person = getPerson();
	        person.setId(1l);
	        Assert.assertTrue(dao.remove(person));

	    }

	    @Test
	    public void cantRetrieve() {
	        Person person = dao.retrieve(new Long(1));
	        Assert.assertNull(person.getName());
	    }

	    @Test
	    public void listTest() {
	        Person person = getPerson();
	        person.setId(1l);
	        dao.insert(person);
	        Assert.assertTrue(dao.listAll().size() > 0);

	    }
	    
	    @Test
	    public void insertFileTest() {
	        System.out.println("Inserindo exemplo");
	        Person person = getPerson();
	        person.setName("otavio teste");
	        person.setId(2l);
	        Address address = getAddress();
	        person.setAddress(address);
	        person.setPersonalFile(new File("readme.txt"));

	        Assert.assertTrue(dao.insert(person));
	    }

	    @Test
	    public void retrieveFileTest() {
	        Person person = dao.retrieve(4l);
	        Assert.assertEquals(person.getPersonalFile().length(), new File("readme.txt").length());
	    }

	    private Address getAddress() {
	        Address address = new Address();
	        address.setCep("40243-543");
	        address.setCity("Salvaor");
	        address.setState("Bahia");
	        return address;

	    }

	    private Person getPerson() {
	        Person person = new Person();
	        person.setYear(10);
	        person.setName("Name Person ");
	        person.setSex(Sex.MALE);

	        return person;

	    }
	    
	    
    
    
}