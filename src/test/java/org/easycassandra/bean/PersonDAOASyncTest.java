package org.easycassandra.bean;

import java.util.List;

import org.easycassandra.bean.dao.PersistenceDaoAsync;
import org.easycassandra.bean.model.Address;
import org.easycassandra.bean.model.Person;
import org.easycassandra.bean.model.Sex;
import org.easycassandra.persistence.cassandra.ResultAsyncCallBack;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author otavio
 */
public class PersonDAOASyncTest {

    private static final long SAMPLE_ID = 32L;
    private static final int TEN = 10;
    private static final int YEAR = 20;
    private static final long FOUR = 4L;
    private static final String NAME = "otavio teste";
    private PersistenceDaoAsync<Person, Long> dao = new PersistenceDaoAsync<>(
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
        dao.retrieve(FOUR, new ResultAsyncCallBack<Person>() {
            @Override
            public void result(Person bean) {
                Assert.assertTrue(bean.getName().toLowerCase()
                        .contains("otavio"));

            }

        });

    }
    /**
     * run the test.
     */
    @Test
    public void retrieveSubClassTest() {
        dao.retrieve(FOUR, new ResultAsyncCallBack<Person>() {
            @Override
            public void result(Person person) {
                Assert.assertNotNull(person.getAddress().getCity());

            }

        });

    }
    /**
     * run the test.
     */
    @Test
    public void retrieveEnumTest() {
        dao.retrieve(FOUR, new ResultAsyncCallBack<Person>() {
            @Override
            public void result(Person person) {
                Assert.assertEquals(person.getSex(), Sex.MALE);
            }

        });
    }
    /**
     * run the test.
     */
    @Test
    public void retrieveEnumEmptyTest() {
        dao.retrieve(FOUR, new ResultAsyncCallBack<Person>() {
            @Override
            public void result(Person person) {
                Assert.assertNotNull(person.getSex());
            }

        });
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
    }
    /**
     * run the test.
     */
    @Test
    public void cantRetrieve() {
        dao.retrieve(new Long(-1), new ResultAsyncCallBack<Person>() {
            @Override
            public void result(Person person) {
                Assert.assertNull(person);
            }

        });
    }
    /**
     * run the test.
     */
    @Test
    public void listTest() {
        Person person = getPerson();
        person.setId(1L);
        dao.insert(person);
        dao.listAll(new ResultAsyncCallBack<List<Person>>() {

            @Override
            public void result(List<Person> beans) {
                Assert.assertTrue(beans.size() > 0);
            }
        });

    }
    /**
     * run the test.
     */
    @Test
    public void listNotNull() {
        dao.listAll(new ResultAsyncCallBack<List<Person>>() {

            @Override
            public void result(List<Person> persons) {
                Assert.assertFalse(persons.contains(null));
            }
        });

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
        dao.count(new ResultAsyncCallBack<Long>() {

            @Override
            public void result(Long bean) {
                Assert.assertNotNull(bean);

            }
        });

    }
    /**
     * run the test.
     */
    @Test
    public void findIndexTest() {
        dao.listByIndex("name", NAME, new ResultAsyncCallBack<List<Person>>() {

            @Override
            public void result(List<Person> persons) {
                Assert.assertNotNull(persons);

            }
        });
    }
    /**
     * run the test.
     */
    @Test
    public void countTest() {
        dao.count(new ResultAsyncCallBack<Long>() {

            @Override
            public void result(Long bean) {
                Assert.assertTrue(bean > 0);

            }
        });
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