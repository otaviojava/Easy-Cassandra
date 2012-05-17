package org.easycassandra.bean;

import junit.framework.Assert;
import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.Engineer;
import org.junit.Test;

public class EngineerDaoTest {

    private PersistenceDao<Engineer> dao = new PersistenceDao<>(Engineer.class);

    @Test
    public void persistTest() {
        Assert.assertTrue(dao.insert(getEngineer()));

    }

    @Test
    public void retrieveTest() {
        Assert.assertNotNull(dao.retrieve(getEngineer().getNickName()));
    }

    @Test
    public void retriveDadAtribute() {
        Assert.assertNotNull(dao.retrieve(getEngineer().getNickName()));
    }

    private Engineer getEngineer() {
        Engineer engineer = new Engineer();
        engineer.setNickName("lekito");
        engineer.setEspecialization("computer");
        engineer.setName("Alex");
        engineer.setType("eletric");
        engineer.setSalary(34d);
        return engineer;
    }
}
