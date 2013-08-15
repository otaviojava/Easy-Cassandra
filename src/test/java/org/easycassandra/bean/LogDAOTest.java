package org.easycassandra.bean;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.Log;
import org.junit.Assert;
import org.junit.Test;

public class LogDAOTest {

	private static final String NICK_NAME = "otaviojava";
	private PersistenceDao<Log,String> dao = new PersistenceDao<Log,String>(Log.class);
	
    @Test
    public void insertTest() {
        System.out.println("inserting sample");
        Log log=new Log();
        log.setUser_uuid(NICK_NAME);
        log.setUuid(NICK_NAME.concat("1"));
        Assert.assertTrue(dao.insert(log));
    }
    
    
    @Test
    public void retrivetWithAcent(){
    	
    	for(int i =0;i< 10;i++){
    		Log log=new Log();
            log.setUser_uuid(NICK_NAME);
            log.setUuid(NICK_NAME.concat(String.valueOf(i)));
            dao.insert(log);
    	}
    	
    	Assert.assertTrue(dao.listByIndex("user_uuid",NICK_NAME).size()==10);
    }
}
