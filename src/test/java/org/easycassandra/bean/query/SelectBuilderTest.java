package org.easycassandra.bean.query;

import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.persistence.cassandra.ResultAsyncCallBack;
import org.easycassandra.persistence.cassandra.SelectBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Query test.
 * @author otaviojava
 */
public class SelectBuilderTest {

    private static PersistenceDao<SimpleBean, Integer> dao = new PersistenceDao<>(
            SimpleBean.class);


    /**
     * select test.
     */
    @Test
    public void selectAllTest() {
        SelectBuilder<SimpleBean> select = dao.select();
        Assert.assertTrue(!select.execute().isEmpty());

    }
    /**
     * select test assync.
     */
    @Test
    public void selectAllAsyncTest() {
        SelectBuilder<SimpleBean> select = dao.select();
        select.executeAsync(new ResultAsyncCallBack<List<SimpleBean>>() {

            @Override
            public void result(List<SimpleBean> beans) {
                Assert.assertTrue(beans.size() == Constant.SIZE);
            }
        });

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
        select.lt(Constant.KEY, Constant.THREE);
        Assert.assertTrue(select.execute().size() == Constant.THREE);
    }
    /**
     * test.
     */
    @Test
    public void lteTest() {
        SelectBuilder<SimpleBean> select = dao.select();
        select.eq(Constant.NAME, Constant.NAME);
        select.lte(Constant.KEY, Constant.THREE);
        Assert.assertTrue(select.execute().size() == Constant.FOUR);
    }
    /**
     * test.
     */
    @Test
    public void gtTest() {
        SelectBuilder<SimpleBean> select = dao.select();
        select.eq(Constant.NAME, Constant.NAME);
        select.gt(Constant.KEY, Constant.THREE);
        Assert.assertTrue(select.execute().size() == Constant.SIX);
    }
    /**
     * test.
     */
    @Test
    public void gteTest() {
        SelectBuilder<SimpleBean> select = dao.select();
        select.eq(Constant.NAME, Constant.NAME);
        select.gte(Constant.KEY, Constant.THREE);
        Assert.assertTrue(select.execute().size() == Constant.SEVEN);
    }
    /**
     * test.
     */
    @Test
    public void ascTest() {
        SelectBuilder<SimpleBean> select = dao.select();
        select.eq(Constant.INDEX, Constant.ONE);
        select.asc(Constant.KEY);
        Assert.assertTrue(select.execute().get(0).getId().getIndex().equals(Constant.ONE));
    }
    /**
     * test.
     */
    @Test
    public void desTest() {
        SelectBuilder<SimpleBean> select = dao.select();
        select.eq(Constant.INDEX, Constant.THREE);
        select.desc(Constant.KEY);
        Assert.assertTrue(select.execute().get(0).getId().getIndex().equals(Constant.THREE));
    }
    /**
     * init.
     */
    @Before
    public void init() {
        Random random = new Random();
        for (int i = 0; i < Constant.SIZE; i++) {
            SimpleBean simple = new SimpleBean();
            simple.getId().setKey(i);
            simple.getId().setIndex(i);
            simple.setName(Constant.NAME);
            simple.setValue(random.nextDouble());
            dao.insert(simple);
        }
    }
    /**
     * remove all.
     */
    @BeforeClass
    public static void start() {
        dao.removeAll();
    }
}
