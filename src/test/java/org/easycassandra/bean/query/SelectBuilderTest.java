package org.easycassandra.bean.query;

import java.util.Random;

import junit.framework.Assert;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.persistence.cassandra.SelectBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 * Query test.
 * @author otaviojava
 */
public class SelectBuilderTest {

    private PersistenceDao<SimpleBean, Integer> dao = new PersistenceDao<>(
            SimpleBean.class);


    /**
     * select test.
     */
    @Test
    public void selectAllTest() {
        SelectBuilder<SimpleBean> select = dao.select();
        Assert.assertTrue(select.execute().size() == Constant.SIZE);

    }
    /**
     * eq test.
     */
    @Test
    public void selectEqTest() {
        SelectBuilder<SimpleBean> select = dao.select();
        select.eq(Constant.NAME, Constant.NAME);
        select.eq(Constant.INDEX, Integer.valueOf(Constant.TWO));
        Assert.assertTrue(select.execute().size() == Constant.ONE);
    }
    /**
     * in test.
     */
    @Test
    public void inTest() {
        SelectBuilder<SimpleBean> select = dao.select();
        select.eq(Constant.NAME, Constant.NAME);
        select.in(Constant.INDEX, Constant.ONE, Constant.TWO, Constant.THREE);
        Assert.assertTrue(select.execute().size() == Constant.THREE);
    }
    /**
     * lt test.
     */
    @Test
    public void ltTest() {
        SelectBuilder<SimpleBean> select = dao.select();
        select.eq(Constant.NAME, Constant.NAME);
        select.lt(Constant.INDEX, Constant.THREE);
        Assert.assertTrue(select.execute().size() == Constant.THREE);
    }
    /**
     * test.
     */
    @Test
    public void lteTest() {
        SelectBuilder<SimpleBean> select = dao.select();
        select.eq(Constant.NAME, Constant.NAME);
        select.lte(Constant.INDEX, Constant.THREE);
        Assert.assertTrue(select.execute().size() == Constant.FOUR);
    }
    /**
     * test.
     */
    @Test
    public void gtTest() {
        SelectBuilder<SimpleBean> select = dao.select();
        select.eq(Constant.NAME, Constant.NAME);
        select.gt(Constant.INDEX, Constant.THREE);
        Assert.assertTrue(select.execute().size() == Constant.SIX);
    }
    /**
     * test.
     */
    @Test
    public void gteTest() {
        SelectBuilder<SimpleBean> select = dao.select();
        select.eq(Constant.NAME, Constant.NAME);
        select.gte(Constant.INDEX, Constant.THREE);
        Assert.assertTrue(select.execute().size() == Constant.SEVEN);
    }
    /**
     * test.
     */
    @Test
    public void ascTest() {
        SelectBuilder<SimpleBean> select = dao.select();
        select.eq(Constant.KEY, Constant.ONE);
        select.in(Constant.INDEX, Constant.ONE, Constant.TWO, Constant.THREE);
        select.asc(Constant.INDEX);
        Assert.assertTrue(select.execute().get(0).getId().getIndex().equals(Constant.ONE));
    }
    /**
     * test.
     */
    @Test
    public void desTest() {
        SelectBuilder<SimpleBean> select = dao.select();
        select.eq(Constant.KEY, Constant.ONE);
        select.desc(Constant.INDEX);
        Assert.assertTrue(select.execute().get(0).getId().getIndex().equals(Constant.NINE));
    }
    /**
     * init.
     */
    @Before
    public void init() {
        Random random = new Random();
        for (int i = 0; i < Constant.SIZE; i++) {
            SimpleBean simple = new SimpleBean();
            simple.getId().setKey(1);
            simple.getId().setIndex(i);
            simple.setName(Constant.NAME);
            simple.setValue(random.nextDouble());
            dao.insert(simple);
        }
    }
}
