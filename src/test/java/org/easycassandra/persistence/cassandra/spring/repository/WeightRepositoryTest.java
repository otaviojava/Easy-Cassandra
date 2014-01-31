package org.easycassandra.persistence.cassandra.spring.repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.easycassandra.bean.model.IdLifestyle;
import org.easycassandra.bean.model.Weight;
import org.easycassandra.persistence.cassandra.spring.SpringUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * tests.
 */
public class WeightRepositoryTest {

    private WeightRepository weightRepository;

    /**
     * before the test.
     */
    @Before
    public void initMethod() {
        weightRepository = SpringUtil.INSTANCE.getBean(WeightRepository.class);
    }

    /**
     * before all test.
     */
    @BeforeClass
    public static void init() {
        SpringUtil spring = SpringUtil.INSTANCE;
    }

    /**
     * run the test.
     */
    @Test
    public void insertTest() {

        Weight weight = new Weight(99L, 22);
        Calendar cal = Calendar.getInstance();
        cal.set(2013, Calendar.DECEMBER, 11, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        weight.setDate(new Date(cal.getTimeInMillis()));
        weight.setValue(98.75);
        Assert.assertNotNull(weightRepository.save(weight));

    }
    /**
     * run the test.
     */
    @Test
    public void retrieveByKeyAndIndexTest() {
        // Find by key and index
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(2013, Calendar.DECEMBER, 11, 0, 0, 0);

        List<Weight> weights = weightRepository.findByKeyAndIndex(
                new IdLifestyle(99L, 22, 2), new Date(cal.getTimeInMillis()));

        Assert.assertTrue(weights.size() == 1);
        Assert.assertTrue(weights.get(0).getValue().doubleValue() == 98.75);
    }
}
