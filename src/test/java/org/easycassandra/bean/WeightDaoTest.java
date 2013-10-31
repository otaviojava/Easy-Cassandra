package org.easycassandra.bean;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.IdLifestyle;
import org.easycassandra.bean.model.Weight;
import org.junit.Assert;
import org.junit.Test;

/**
 * PRE-REQUISITE: Same as StepDaoTest pre-requisite
 *
 * @author Nenita Casuga
 */
public class WeightDaoTest {

    private PersistenceDao<Weight, IdLifestyle> dao = new PersistenceDao<Weight, IdLifestyle>(Weight.class);

    @Test
    public void insertTest() {
        Weight weight = new Weight(1L, 1);
        Calendar cal = Calendar.getInstance();
        cal.set(2013, Calendar.NOVEMBER, 11, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        weight.setDate(new Date(cal.getTimeInMillis()));
        weight.setValue(200.00);
        Assert.assertTrue(dao.insert(weight));

        Weight weight2 = new Weight(2L, 1);
        weight2.setDate(new Date(cal.getTimeInMillis()));
        weight2.setValue(18.75);
        Assert.assertTrue(dao.insert(weight2));
    }


    @Test
    public void retrieveByKeyAndIndexTest() {
        // Find by key and index
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(2013, Calendar.NOVEMBER, 11, 0, 0, 0);

        List<Weight> weights = dao.listByKeyAndIndex(new IdLifestyle(1L, 1, 2), new Date(cal.getTimeInMillis()));
        Assert.assertTrue(weights.size() == 1);
        Assert.assertTrue(weights.get(0).getValue().doubleValue() == 200.00);

        weights = dao.listByKeyAndIndex(new IdLifestyle(2L, 1, 2), new Date(cal.getTimeInMillis()));
        Assert.assertTrue(weights.size() == 1);
        Assert.assertTrue(weights.get(0).getValue().doubleValue() == 18.75);
    }

    @Test
    public void retrieveByKeyTest() {
        Weight weight = dao.retrieve(new IdLifestyle(1L, 1, 3));
        Assert.assertNotNull(weight);
    }

}