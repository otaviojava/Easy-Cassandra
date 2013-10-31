package org.easycassandra.bean;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.IdLifestyle;
import org.easycassandra.bean.model.Step;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Nenita Casuga
 *         Date 10/30/2013
 */
public class StepDaoTest {
    private PersistenceDao<Step, IdLifestyle> dao = new PersistenceDao<Step, IdLifestyle>(Step.class);

    /*
     * CREATE TABLE lifestyle (personid bigint, companyid int, type int, date timestamp, value double,
     * primary key ((personid, companyid, type),date));
     */
    @Test
    public void insertTest() {
        Step step = new Step(1L, 1);

        Calendar cal = Calendar.getInstance();
        cal.set(2013, Calendar.NOVEMBER, 11, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        step.setDate(new Date(cal.getTimeInMillis()));
        step.setValue(125.00);
        Assert.assertTrue(dao.insert(step));

        Step step2 = new Step(2L, 1);
        step2.setDate(new Date(cal.getTimeInMillis()));
        step2.setValue(175.50);
        Assert.assertTrue(dao.insert(step2));

        // Note that this will only work if the table was created using
        // CREATE TABLE (person_id, type_id... primary key((person_id, type_id), date));
        // Otherwise, the second write will overwrite the first one and since we can't control the create table
        // here, comment out this test
        //Calendar cal2 = Calendar.getInstance();
        //cal2.set(2013, Calendar.NOVEMBER, 2, 0, 0, 0);
        //cal2.set(Calendar.MILLISECOND, 0);
        //Step step3 = new Step(1);
        //step3.setDate(new Date(cal2.getTimeInMillis()));
        //step3.setValue(187.25);
        //Assert.assertTrue(dao.insert(step3));
    }

    @Test
    public void retrieveByKeyAndIndexTest() {
        // Find by key and index
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(2013, Calendar.NOVEMBER, 11, 0, 0, 0);

        List<Step> steps = dao.listByKeyAndIndex(new IdLifestyle(1L, 1, 3), new Date(cal.getTimeInMillis()));
        Assert.assertTrue(steps.size() == 1);
        Assert.assertTrue(steps.get(0).getValue().doubleValue() == 125.00);

        // Note that this will only work if the table was created using
        // CREATE TABLE (person_id, type_id... primary key((person_id, type_id), date));
        // Otherwise, the second write will overwrite the first one and since we can't control the create table
        // here, comment out this test
        //Calendar cal2 = Calendar.getInstance();
        //cal2.set(2013, Calendar.NOVEMBER, 2, 0, 0, 0);
        //cal2.set(Calendar.MILLISECOND, 0);
        //steps = dao.listByKeyAndIndex(new IdLifestyle(1, 3), new Date(cal2.getTimeInMillis()));
        //Assert.assertTrue(steps.size() == 1);
        //Assert.assertTrue(steps.get(0).getValue().doubleValue() == 187.25);

        steps = dao.listByKeyAndIndex(new IdLifestyle(2L, 1, 3), new Date(cal.getTimeInMillis()));
        Assert.assertTrue(steps.size() == 1);
        Assert.assertTrue(steps.get(0).getValue().doubleValue() == 175.50);
    }

    @Test
    public void retrieveByKeyTest() {

        // This will return the last inserted record if there are multiple match on the PK
        Step step = dao.retrieve(new IdLifestyle(1L, 1, 3));
        Assert.assertNotNull(step);
    }
}