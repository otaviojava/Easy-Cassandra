package org.easycassandra.bean.dao;

import java.io.File;
import java.util.List;
import java.util.Map;
import javax.persistence.Parameter;
import org.easycassandra.EasyCassandraException;
import org.easycassandra.bean.model.Address;
import org.easycassandra.bean.model.Person;
import org.easycassandra.bean.model.Sex;
import org.easycassandra.persistence.EasyCassandraManager;
import org.easycassandra.persistence.JCassandra;
import org.easycassandra.persistence.Persistence;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PersistenceDaoTest {

    private Persistence persistence;
    private PersistenceDao<Person> dao;

    @Test
    public void createJCassandraTest() {

        Assert.assertNotNull(persistence.createJCassandra("select * from Person"));
    }

    @Test
    public void createJCassandraFailTest() {

        Assert.assertNull(persistence.createJCassandra("select * from Otavio"));
    }

    @Test
    public void runCqlTest() {
        JCassandra jCassandra = persistence.createJCassandra("select * from Person");
        Assert.assertNotNull(jCassandra.getResultList());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void runCqlListAllTest() {

        JCassandra jCassandra = persistence.createJCassandra("select * from Person");

        List<Person> persons = jCassandra.getResultList();
        Assert.assertEquals(persons.size(), dao.listAll().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void runCqlfindByIdInclude() {

        Person p = getPerson();
        p.setId(31l);
        persistence.insert(p);
        JCassandra jCassandra = persistence.createJCassandra("select * from Person where id = 31 ");

        List<Person> persons = jCassandra.getResultList();
        Assert.assertEquals(persons.get(0).getName(), dao.retrieve(31l).getName());
    }

    @Test
    public void runCqlSomeFieldsTest() {

        JCassandra jCassandra = persistence.createJCassandra("select name, id, year from Person");


        Assert.assertNotNull(jCassandra.getResultList());
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void runCqlSomeFields2Test() {

        JCassandra jCassandra = persistence.createJCassandra("select name, id, year from Person");

        List list = jCassandra.getResultList();
        Assert.assertNotNull(list);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void runCqlEqualsFieldTest() {

        JCassandra jCassandra = persistence.createJCassandra("select personalFile, sex, id, year from Person");

        List<Map<String, Object>> list = jCassandra.getResultList();
        for (Map<String, Object> map : list) {
            if (map.get("personalFile") != null) {
                Assert.assertTrue(map.get("personalFile") instanceof File);
            }

        }

    }

    @SuppressWarnings("unchecked")
    @Test
    public void runCqlEqualsFieldEnumTest() {

        Person p = getPerson();
        p.setId(113l);
        persistence.insert(p);
        JCassandra jCassandra = persistence.createJCassandra("select personalFile, sex, id, year from Person where id = 113");

        List<Map<String, Object>> list = jCassandra.getResultList();
        Sex sex = (Sex) list.get(0).get("sex");
        Assert.assertEquals(Sex.MALE, sex);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void selectSubFieldTest() {

        JCassandra jCassandra = persistence.createJCassandra("select address.cep from Person where id = 10");

        String cep = (String) jCassandra.getSingleResult();

        Assert.assertEquals("40243-543", cep);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void selectSubClassTest() {

        JCassandra jCassandra = persistence.createJCassandra("select address from Person where id = 10");



        Assert.assertTrue(jCassandra.getSingleResult() instanceof Address);
    }

    @Test
    public void getSingleResultTest() {
        Person p = getPerson();
        p.setId(33l);
        Assert.assertTrue(persistence.insert(p));
        JCassandra jCassandra = persistence.createJCassandra("select * from Person where id = 33 ");
        Person person = (Person) jCassandra.getSingleResult();
        Assert.assertEquals(Long.valueOf(33l), person.getId());
    }

    private Person getPerson() {
        Person person = new Person();
        person.setYear(10);
        person.setName("Name Person ");
        person.setSex(Sex.MALE);
        return person;
    }

    @Test
    public void setFirstResultTest() {
        JCassandra jCassandra = persistence.createJCassandra("select * from Person ");
        jCassandra.setFirstResult(2);
        Person person = (Person) jCassandra.getResultList().get(0);
        Assert.assertEquals(person.getId(), dao.listAll().get(2).getId());

    }

    @Test(expected = IllegalArgumentException.class)
    public void setFirstResultNegativeValueTest() {
        JCassandra jCassandra = persistence.createJCassandra("select * from Person ");
        jCassandra.setFirstResult(-1);
        Person person = (Person) jCassandra.getResultList().get(0);
        Assert.assertEquals(person.getId(), dao.listAll().get(2).getId());
    }

    @Test
    public void setMaxResultsTest() {
        JCassandra jCassandra = persistence.createJCassandra("select * from Person ");
        jCassandra.setMaxResults(2);

        Assert.assertEquals(jCassandra.getResultList().size(), 2);

    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxResultsNegativeTest() {
        JCassandra jCassandra = persistence.createJCassandra("select * from Person ");
        jCassandra.setMaxResults(-1);

        Assert.assertEquals(jCassandra.getResultList().size(), 2);

    }

    @Test
    public void countTest() {
        JCassandra jCassandra = persistence.createJCassandra("select count(*) from Person ");
        Assert.assertTrue(jCassandra.getResultList().get(0) instanceof Long);
    }

    @Test
    public void whereTest() {
        JCassandra jCassandra = persistence.createJCassandra("select * from Person where id =:idParameter ");
        jCassandra.setParameter("idParameter", 10l);
        Person person = (Person) jCassandra.getSingleResult();
        Assert.assertEquals(person.getId(), Long.valueOf(10l));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whereIncopatibleFieldTest() {
        JCassandra jCassandra = persistence.createJCassandra("select * from Person where id =:id ");
        jCassandra.setParameter("id", "10");
        Person person = (Person) jCassandra.getSingleResult();
        Assert.assertEquals(person.getId(), Long.valueOf(10l));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whereFieldNotFoundTest() {
        JCassandra jCassandra = persistence.createJCassandra("select * from Person where id =:id ");
        jCassandra.setParameter("unknow", 10l);
        Person person = (Person) jCassandra.getSingleResult();
        Assert.assertEquals(person.getId(), Long.valueOf(10l));
    }

    @Test
    public void getParametersTest() {
        JCassandra jCassandra = persistence.createJCassandra("select * from Person where id =:id  and name =:name");
        Assert.assertEquals(jCassandra.getParameters().size(), 2);
    }

    @Test
    public void getParameterTest() {
        JCassandra jCassandra = persistence.createJCassandra("select * from Person where id =:id  and name =:name");
        Parameter<?> parameter = jCassandra.getParameter(0);
        Assert.assertNotNull(parameter);
    }

    @Test
    public void getParameterEqualsTest() {
        JCassandra jCassandra = persistence.createJCassandra("select * from Person where id =:id  and name =:name");
        Assert.assertEquals(jCassandra.getParameter(0).getName(), "id");
    }

    @Test
    public void getParameterNameTest() {
        JCassandra jCassandra = persistence.createJCassandra("select * from Person where id =:id  and name =:name");
        Assert.assertNotNull(jCassandra.getParameter("name"));
    }

    @Test
    public void getParameterEqualsNameTest() {
        JCassandra jCassandra = persistence.createJCassandra("select * from Person where id =:idParameter  and name =:nameParameter");
        Assert.assertEquals(jCassandra.getParameter("nameParameter").getName(), "name");
    }

    @Test
    public void getMaxResultsTest() {
        JCassandra jCassandra = persistence.createJCassandra("select * from Person where id =:id ");
        jCassandra.setMaxResults(2);
        Assert.assertEquals(jCassandra.getMaxResults(), 2);

    }

    @Test
    public void getFirstResultTest() {
        JCassandra jCassandra = persistence.createJCassandra("select * from Person where id =:id ");
        jCassandra.setFirstResult(3);

        Assert.assertEquals(jCassandra.getFirstResult(), 3);

    }

    @Test
    public void executeUpdateDeleteTest() {
        JCassandra jCassandra = persistence.createJCassandra("delete from Person where id =:id ");
        jCassandra.setParameter("id", 1l);
        Assert.assertTrue(jCassandra.executeUpdate() == 1);
    }

    @Test(expected = EasyCassandraException.class)
    public void executeUpdateInvalidWhereDeleteTest() {
        JCassandra jCassandra = persistence.createJCassandra("delete from Person where name =:id ");
        jCassandra.setParameter("id", "Otávio Santana");
        jCassandra.executeUpdate();
    }

    @Test
    public void executeUpdateUpdateTest() {
        JCassandra jCassandra = persistence.createJCassandra("update Person set name =:name where id =:id ");
        jCassandra.setParameter("id", 4l);
        jCassandra.setParameter("name", "Otavio Santana");
        Assert.assertTrue(jCassandra.executeUpdate() == 1);
    }

    @Test
    public void executeUpdateUpdate2Test() {
        JCassandra jCassandra = persistence.createJCassandra("update Person set name =:name , year =:year where id =:id ");
        jCassandra.setParameter("id", 4l);
        jCassandra.setParameter("name", "Otavio Santana");
        jCassandra.setParameter("year", 1988);
        Assert.assertTrue(jCassandra.executeUpdate() == 1);
    }

    @Test
    public void executeUpdateUpdateSubFieldTest() {
        JCassandra jCassandra = persistence.createJCassandra("update Person set name =:name , year =:year ,address.city =:city where id =:id ");
        jCassandra.setParameter("id", 4l);
        jCassandra.setParameter("name", "Otavio Santana");
        jCassandra.setParameter("year", 1988);
        jCassandra.setParameter("city", "Salvador");
        Assert.assertTrue(jCassandra.executeUpdate() == 1);
    }

    @Test
    public void executeUpdateDeleteSomeFieldTest() {
        JCassandra jCassandra = persistence.createJCassandra("delete name from Person where id =:id ");
        jCassandra.setParameter("id", 10l);
        Assert.assertTrue(jCassandra.executeUpdate() == 1);
    }

    @BeforeClass
    public static void initStatic() {
        EasyCassandraManager.addFamilyObject(Person.class);

    }

    @Before
    public void init() {
        persistence = EasyCassandraManager.getPersistence("javabahia", "localhost", 9160);
        dao = new PersistenceDao<>(Person.class);
    }
}
