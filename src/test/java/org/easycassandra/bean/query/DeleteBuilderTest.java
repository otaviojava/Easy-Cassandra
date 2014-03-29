package org.easycassandra.bean.query;

import junit.framework.Assert;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.Book;
import org.easycassandra.persistence.cassandra.DeleteBuilder;
import org.junit.Test;

/**
 * to test delete.
 * @author otaviojava
 */
public class DeleteBuilderTest {
    private PersistenceDao<SimpleBean, Integer> dao = new PersistenceDao<>(
            SimpleBean.class);

    private PersistenceDao<Book, String> bookDao = new PersistenceDao<>(
            Book.class);


    private static final int ONE_HUNDRED_TWO = 102;
    private static final double VALUE = 12D;

    /**
     * run the test.
     */
    @Test
    public void deleteTest() {
        SimpleID id = new SimpleID();
        id.setIndex(ONE_HUNDRED_TWO);
        id.setKey(ONE_HUNDRED_TWO);
        SimpleBean simpleBean = new SimpleBean();
        simpleBean.setId(id);
        simpleBean.setValue(VALUE);

        dao.insert(simpleBean);
        DeleteBuilder<SimpleBean> delete = dao.deleteBuilder();
        delete.whereEq(Constant.INDEX, ONE_HUNDRED_TWO)
                .whereEq(Constant.KEY, ONE_HUNDRED_TWO).execute();

        Assert.assertNull(dao.retrieve(id));
    }

    /**
     * run the test.
     */
    @Test
    public void deleteKeyTest() {
        SimpleID id = new SimpleID();
        id.setIndex(ONE_HUNDRED_TWO);
        id.setKey(ONE_HUNDRED_TWO);

        DeleteBuilder<SimpleBean> delete = dao.deleteBuilder(id);
        delete.execute();
    }
    /**
     * remove.
     * @throws Exception an exception
     */
    @Test
    public void testInQuery() throws Exception {
        DeleteBuilder<Book> delete = bookDao.deleteBuilder();

        delete.whereIn("name", new Object[] { "1", "2", "3" });
        delete.execute();
    }
}
