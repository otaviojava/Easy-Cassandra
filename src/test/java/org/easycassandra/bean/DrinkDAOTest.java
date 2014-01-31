package org.easycassandra.bean;

import junit.framework.Assert;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.Drink;
import org.easycassandra.bean.model.Person;
import org.junit.Test;
/**
 * DrinkDAO test.
 * @author otaviojava
 */
public class DrinkDAOTest {

    private PersistenceDao<Drink, Long> dao = new PersistenceDao<Drink, Long>(
            Drink.class);

    private PersistenceDao<Person, Long> personDao = new PersistenceDao<Person, Long>(
            Person.class);
    /**
     * run the test.
     */
    @Test
    public void insertTest() {
        Drink drink = new Drink();
        drink.setId(1L);
        drink.setFlavor("orange");
        drink.setName("cup of juice");

        Person person = new Person();
        person.setId(1L);
        person.setName("otávio");
        person.setYear(19);
        personDao.insert(person);

        Assert.assertTrue(dao.insert(drink));
    }
    /**
     * run the test.
     */
    @Test
    public void retrieveTest() {
        Drink drink = dao.retrieve(1L);

        Assert.assertNotNull(drink);
    }

}
