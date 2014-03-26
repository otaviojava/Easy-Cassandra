package org.easycassandra.bean;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.Log;
import org.junit.Assert;
import org.junit.Test;
/**
 * the LogDAO test.
 * @author otaviojava
 */
public class LogDAOTest {

    private static final int LOOP_SIZE = 10;
    private static final String NICK_NAME = "otaviojava";
    private PersistenceDao<Log, String> dao = new PersistenceDao<Log, String>(
            Log.class);
    /**
     * run the test.
     */
    @Test
    public void insertTest() {
        Log log = new Log();
        log.setUserUUid(NICK_NAME);
        log.setUuid(NICK_NAME.concat("1"));
        Assert.assertTrue(dao.insert(log));
    }
    /**
     * run the test.
     */
    @Test
    public void retrieveWithListIndex() {

        for (int i = 0; i < LOOP_SIZE; i++) {
            Log log = new Log();
            log.setUserUUid(NICK_NAME);
            log.setUuid(NICK_NAME.concat(String.valueOf(i)));
            dao.insert(log);
        }

        Assert.assertTrue(dao.listByIndex("user_uuid", NICK_NAME).size() == LOOP_SIZE);
    }
}
