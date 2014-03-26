package org.easycassandra.bean.query;

import java.util.Arrays;

import junit.framework.Assert;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.persistence.cassandra.BatchBuilder;
import org.easycassandra.persistence.cassandra.DeleteBuilder;
import org.easycassandra.persistence.cassandra.InsertBuilder;
import org.easycassandra.persistence.cassandra.ResultAsyncCallBack;
import org.easycassandra.persistence.cassandra.UpdateBuilder;
import org.junit.Test;

/**
 * to run batch test.
 * @author otaviojava
 *
 */
public class BatchBuilderTest {
    private PersistenceDao<SimpleBean, Integer> dao = new PersistenceDao<>(
            SimpleBean.class);

    /**
     * test.
     */
    @Test
    public void executeBatchTest() {
        BatchBuilder batchBuilder = prepareBatch();
        batchBuilder.execute();
    }

    /**
     * test.
     */
    @Test
    public void executeBatchAsyncTest() {
        BatchBuilder batchBuilder = prepareBatch();
        batchBuilder.executeAsync(new ResultAsyncCallBack<Boolean>() {
            @Override
            public void result(Boolean bean) {
                Assert.assertTrue(bean);
            }
        });
    }

    private BatchBuilder prepareBatch() {
        DeleteBuilder<SimpleBean> delete = dao.deleteBuilder();
        delete.whereEq(Constant.INDEX, Constant.ONE_HUNDRED_TWO)
                .whereEq(Constant.KEY, Constant.ONE_HUNDRED_TWO);

        InsertBuilder<SimpleBean> insert = dao.insertBuilder();
        insert.value(Constant.KEY, Constant.ONE_HUNDRED).value(Constant.INDEX,
                Constant.ONE_HUNDRED);
        insert.value(Constant.LIST_COLUMN,
                Arrays.asList("Poliana", "Otavio", "Love"));

        UpdateBuilder<SimpleBean> update = dao.update();
        update.whereEq(Constant.KEY, Constant.ONE).whereEq(Constant.INDEX, Constant.ONE);
        update.addList("list", "otavioList");

        BatchBuilder batchBuilder = dao.batchBuilder();

        batchBuilder.addOperations(delete, insert, update);
        return batchBuilder;
    }
}
