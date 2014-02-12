package org.easycassandra.bean.query;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.persistence.cassandra.InsertBuilder;
import org.junit.Test;

/**
 * Bean to use query builder.
 * @author otaviojava
 */
public class InsertBuilderTest {
    private static final double VALUE = 12d;
    private static final int ONE_HUNDRED_ONE = 101;
    private static final String SET_COLUMN = "set";
    private static final String LIST_COLUMN = "list";
    private static final int ONE_HUNDRED = 100;
    private PersistenceDao<SimpleBean, Integer> dao = new PersistenceDao<>(
            SimpleBean.class);

    /**
     * run test.
     */
    @Test
    public void insertTest() {
        InsertBuilder<SimpleBean> insert = dao.insertBuilder();
        insert.value(Constant.KEY, ONE_HUNDRED).value(Constant.INDEX, ONE_HUNDRED);
        insert.value(LIST_COLUMN, Arrays.asList("Poliana", "Otavio", "Love"));
        Set<String> set = new HashSet<>();
        set.add("Linda");

        Map<String, String> map = new HashMap<>();
        map.put("love", "Otavio and Poliana");

        insert.value(SET_COLUMN, set);
        insert.value("map", map);

        insert.execute();

        SimpleID id = new SimpleID();
        id.setIndex(ONE_HUNDRED);
        id.setKey(ONE_HUNDRED);

        SimpleBean bean = dao.retrieve(id);

        Assert.assertTrue(bean != null);

    }
    /**
     * run test.
     */
    @Test
    public void updateaddListTest() {
        SimpleID id = new SimpleID();
        id.setIndex(ONE_HUNDRED_ONE);
        id.setKey(ONE_HUNDRED_ONE);
        SimpleBean simpleBean = new SimpleBean();
        simpleBean.setId(id);
        simpleBean.setValue(VALUE);

        InsertBuilder<SimpleBean> insert = dao.insertBuilder(simpleBean);
        insert.execute();


        SimpleBean beanBD = dao.retrieve(id);
        Assert.assertTrue(beanBD != null);
    }
}
