package org.easycassandra.persistence.cassandra.spring;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.easycassandra.bean.model.IdLifestyle;
import org.easycassandra.bean.model.Step;
import org.easycassandra.persistence.cassandra.spring.repository.StepRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StepRepositoryTest {

    private StepRepository stepRepository;

    @Before
    public void initMethod() {
        stepRepository = SpringUtil.INSTANCE.getBean(StepRepository.class);
    }

    @BeforeClass
    public static void init() {
        SpringUtil spring = SpringUtil.INSTANCE;
    }

    @Test
    public void insertTest() {

        Step step = new Step(100L, 21);
        Calendar cal = Calendar.getInstance();
        cal.set(2013, Calendar.DECEMBER, 11, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        step.setDate(new Date(cal.getTimeInMillis()));
        step.setValue(99.9);
        Assert.assertNotNull(stepRepository.save(step));

    }

    @Test
    public void retrieveByKeyAndIndexTest() {
        // Find by key and index
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(2013, Calendar.DECEMBER, 11, 0, 0, 0);

        List<Step> steps = stepRepository.findByKeyAndIndex(new IdLifestyle(100L, 21, 3), new Date(cal.getTimeInMillis()));
        Assert.assertTrue(steps.size() == 1);
        Assert.assertTrue(steps.get(0).getValue().doubleValue() == 99.9);

    }

}
