package org.easycassandra.bean;

import java.util.List;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.Address;
import org.easycassandra.bean.model.Person;
import org.easycassandra.bean.model.Sex;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author otavio
 */
public class PersonDAOTest {

    private static final String NAME = "otavio teste";
	private PersistenceDao<Person,Long> dao = new PersistenceDao<Person,Long>(Person.class);

    @Test
    public void insertTest() {
    	
    	
        Person person = getPerson();
        person.setName(NAME);
        person.setId(4l);
        person.setYear(20);
        Address address = getAddress();
        person.setAddress(address);
    	
        Assert.assertTrue(dao.insert(person));
    	
    }

    @Test
    public void retrieveTest() {
        Person person = dao.retrieve(4l);
        Assert.assertTrue(person.getName().toLowerCase().contains("otavio"));
    }

    @Test
    public void retriveSubClassTest() {
        Person person = dao.retrieve(4l);

        Assert.assertNotNull(person.getAddress().getCity());
    }

    @Test
    public void retrieveEnumTest() {
        Person person = dao.retrieve(4l);
        Assert.assertEquals(person.getSex(), Sex.MALE);
    }

    @Test
    public void retrieveEnumEmptyTest() {
        Person person = dao.retrieve(4l);
        Assert.assertNotNull(person.getSex());
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
        Assert.assertTrue(dao.insert(person));
        Assert.assertTrue(dao.remove(person));
        Assert.assertNull(dao.retrieve(1l));
    }

    @Test
    public void cantRetrieve() {
        Person person = dao.retrieve(new Long(-1));
        Assert.assertNull(person);
    }

    @Test
    public void listTest() {
        Person person = getPerson();
        person.setId(1l);
        dao.insert(person);
        Assert.assertTrue(dao.listAll().size() > 0);

    }

    @Test
    public void listNotNull() {
        List<Person> persons = dao.listAll();

        Assert.assertFalse(persons.contains(null));
    }

    @Test
    public void insertFileTest() {
        System.out.println("Inserindo exemplo");
        Person person = getPerson();
        person.setName(NAME);
        person.setId(4l);
        Address address = getAddress();
        person.setAddress(address);

        Assert.assertTrue(dao.insert(person));
    }

   

    @Test
    public void countNotNullTest() {

        Assert.assertNotNull(dao.count());
    }


    @Test
    public void findIndexTest(){
    	List<Person> persons= dao.listByIndex("name",NAME);
    	Assert.assertNotNull(persons);
    }

    @Test
    public void executeUpdateCqlTest() {
        Assert.assertTrue(dao.executeUpdateCql("select * from Person"));
    }

   



    @Test
    public void countTest() {
        Assert.assertTrue(dao.count()>0);
    }
    
    @Test
    public void insertWithAcent(){
    	Person person=getPerson();
    	person.setName(NAME);
    	person.setId(32l);
    	Assert.assertTrue(dao.insert(person));
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
        person.setName(NAME);
        person.setSex(Sex.MALE);
        return person;
    }
}