package org.easycassandra.bean;

import java.util.List;

import org.easycassandra.Constants;
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

    private static final long SAMPLE_ID = 32L;
    private static final int TEN = 10;
    private static final int YEAR = 20;
    private static final long FOUR = 4L;
    private static final String NAME = "otavio teste";
    private PersistenceDao<Person, Long> dao = new PersistenceDao<Person, Long>(
            Person.class);
    /**
     * run the test.
     */
    @Test
    public void insertTest() {

        Person person = getPerson();
        person.setName(NAME);
        person.setId(FOUR);
        person.setYear(YEAR);
        Address address = getAddress();
        person.setAddress(address);

        Assert.assertTrue(dao.insert(person));

    }
    /**
     * run the test.
     */
    @Test
    public void retrieveTest() {
        Person person = dao.retrieve(FOUR);
        Assert.assertTrue(person.getName().toLowerCase().contains("otavio"));
    }
    /**
     * run the test.
     */
    @Test
    public void retrieveSubClassTest() {
        Person person = dao.retrieve(FOUR);

        Assert.assertNotNull(person.getAddress().getCity());
    }
    /**
     * run the test.
     */
    @Test
    public void retrieveEnumTest() {
        Person person = dao.retrieve(FOUR);
        Assert.assertEquals(person.getSex(), Sex.MALE);
    }
    /**
     * run the test.
     */
    @Test
    public void retrieveEnumEmptyTest() {
        Person person = dao.retrieve(FOUR);
        Assert.assertNotNull(person.getSex());
    }
    /**
     * run the test.
     */
    @Test
    public void overrideTest() {

        Person person = getPerson();
        person.setId(1L);
        Assert.assertTrue(dao.insert(person));
    }
    /**
     * run the test.
     */
    @Test
    public void removeFromRowKeyTest() {
        Assert.assertTrue(dao.removeFromRowKey(new Long(2)));
    }
    /**
     * run the test.
     */
    @Test
    public void removeTest() {
        Person person = getPerson();
        person.setId(1L);
        Assert.assertTrue(dao.insert(person));
        Assert.assertTrue(dao.remove(person));
        Assert.assertNull(dao.retrieve(1L));
    }
    /**
     * run the test.
     */
    @Test
    public void cantRetrieve() {
        Person person = dao.retrieve(new Long(-1));
        Assert.assertNull(person);
    }
    /**
     * run the test.
     */
    @Test
    public void listTest() {
        Person person = getPerson();
        person.setId(1L);
        dao.insert(person);
        Assert.assertTrue(dao.listAll().size() > 0);

    }
    /**
     * run the test.
     */
    @Test
    public void listNotNull() {
        List<Person> persons = dao.listAll();

        Assert.assertFalse(persons.contains(null));
    }
    /**
     * run the test.
     */
    @Test
    public void insertFileTest() {
        Person person = getPerson();
        person.setName(NAME);
        person.setId(FOUR);
        Address address = getAddress();
        person.setAddress(address);

        Assert.assertTrue(dao.insert(person));
    }
    /**
     * run the test.
     */
    @Test
    public void countNotNullTest() {

        Assert.assertNotNull(dao.count());
    }
    /**
     * run the test.
     */
    @Test
    public void findIndexTest() {
        List<Person> persons = dao.listByIndex("name", NAME);
        Assert.assertNotNull(persons);
    }
    /**
     * run the test.
     */
    @Test
    public void executeUpdateCqlTest() {
        Assert.assertTrue(dao.executeUpdateCql("select * from "
                + Constants.KEY_SPACE + ".Person"));
    }
    /**
     * run the test.
     */
    @Test
    public void countTest() {
        Assert.assertTrue(dao.count() > 0);
    }
    /**
     * run the test.
     */
    @Test
    public void insertWithPerson() {
        Person person = getPerson();
        person.setName(NAME);
        person.setId(SAMPLE_ID);
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
        person.setYear(TEN);
        person.setName(NAME);
        person.setSex(Sex.MALE);
        return person;
    }
}