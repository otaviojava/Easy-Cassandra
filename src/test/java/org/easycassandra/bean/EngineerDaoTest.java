package org.easycassandra.bean;

import junit.framework.Assert;
import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.Engineer;
import org.junit.Test;
/**
 * EngineerDao test.
 * @author otaviojava
 */
public class EngineerDaoTest {

    private static final double SALARY = 34D;
    private PersistenceDao<Engineer, String> dao = new PersistenceDao<Engineer, String>(
            Engineer.class);
    /**
     * run the test.
     */
    @Test
    public void persistTest() {
        Assert.assertTrue(dao.insert(getEngineer()));

    }
    /**
     * run the test.
     */
    @Test
    public void retrieveTest() {
        Engineer engineer = dao.retrieve(getEngineer().getNickName());
        Assert.assertNotNull(engineer);
    }
    /**
     * run the test.
     */
    @Test
    public void retrieveDadAttribute() {
        Assert.assertNotNull(dao.retrieve(getEngineer().getNickName()));
    }

    private Engineer getEngineer() {
        Engineer engineer = new Engineer();
        engineer.setNickName("lekito");
        engineer.setEspecialization("computer");
        engineer.setName("Alex");
        engineer.setType("eletric");
        engineer.setSalary(SALARY);
        return engineer;
    }
}
