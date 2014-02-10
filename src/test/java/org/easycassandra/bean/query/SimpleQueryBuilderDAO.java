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
public class SimpleQueryBuilderDAO {
    private static final String KEY = "key";
    private static final int NINE = 9;
    private static final int SEVEN = 7;
    private static final int SIX = 6;
    private static final int FOUR = 4;
    private static final String INDEX = "index";
    private static final String NAME = "name";
    private static final int THREE = 3;
    private static final int TWO = 2;
    private static final int ONE = 1;
    private static final int SIZE = 10;
    private PersistenceDao<SimpleQueryBuilder, Integer> dao = new PersistenceDao<>(
            SimpleQueryBuilder.class);


    /**
     * select test.
     */
    @Test
    public void selectAllTest() {
        SelectBuilder<SimpleQueryBuilder> select = dao.select();
        Assert.assertTrue(select.execute().size() == SIZE);

    }
    /**
     * eq test.
     */
    @Test
    public void selectEqTest() {
        SelectBuilder<SimpleQueryBuilder> select = dao.select();
        select.eq(NAME, NAME);
        select.eq(INDEX, Integer.valueOf(TWO));
        Assert.assertTrue(select.execute().size() == ONE);
    }
    /**
     * in test.
     */
    @Test
    public void inTest() {
        SelectBuilder<SimpleQueryBuilder> select = dao.select();
        select.eq(NAME, NAME);
        select.in(INDEX, ONE, TWO, THREE);
        Assert.assertTrue(select.execute().size() == THREE);
    }
    /**
     * lt test.
     */
    @Test
    public void ltTest() {
        SelectBuilder<SimpleQueryBuilder> select = dao.select();
        select.eq(NAME, NAME);
        select.lt(INDEX, THREE);
        Assert.assertTrue(select.execute().size() == THREE);
    }
    /**
     * test.
     */
    @Test
    public void lteTest() {
        SelectBuilder<SimpleQueryBuilder> select = dao.select();
        select.eq(NAME, NAME);
        select.lte(INDEX, THREE);
        Assert.assertTrue(select.execute().size() == FOUR);
    }
    /**
     * test.
     */
    @Test
    public void gtTest() {
        SelectBuilder<SimpleQueryBuilder> select = dao.select();
        select.eq(NAME, NAME);
        select.gt(INDEX, THREE);
        Assert.assertTrue(select.execute().size() == SIX);
    }
    /**
     * test.
     */
    @Test
    public void gteTest() {
        SelectBuilder<SimpleQueryBuilder> select = dao.select();
        select.eq(NAME, NAME);
        select.gte(INDEX, THREE);
        Assert.assertTrue(select.execute().size() == SEVEN);
    }
    /**
     * test.
     */
    @Test
    public void ascTest() {
        SelectBuilder<SimpleQueryBuilder> select = dao.select();
        select.eq(KEY, ONE);
        select.in(INDEX, ONE, TWO, THREE);
        select.asc(INDEX);
        Assert.assertTrue(select.execute().get(0).getId().getIndex().equals(ONE));
    }
    /**
     * test.
     */
    @Test
    public void desTest() {
        SelectBuilder<SimpleQueryBuilder> select = dao.select();
        select.eq(KEY, ONE);
        select.desc(INDEX);
        Assert.assertTrue(select.execute().get(0).getId().getIndex().equals(NINE));
    }
    /**
     * init.
     */
    @Before
    public void init() {
        Random random = new Random();
        for (int i = 0; i < SIZE; i++) {
            SimpleQueryBuilder simple = new SimpleQueryBuilder();
            simple.getId().setKey(1);
            simple.getId().setIndex(i);
            simple.setName(NAME);
            simple.setValue(random.nextDouble());
            dao.insert(simple);
        }
    }
}
