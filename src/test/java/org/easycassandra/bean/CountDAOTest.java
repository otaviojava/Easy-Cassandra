package org.easycassandra.bean;

import junit.framework.Assert;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.Count;
import org.junit.Test;


/**
*
* @author otavio
*/
public class CountDAOTest {

	private PersistenceDao<Count> dao = new PersistenceDao<Count>(Count.class);
    
    
    @Test
    public void insertAutoTest(){
    	
    	for(int index=0;index<10;index++){
    		Count count=new Count();
    		count.setName("name"+index);
    		dao.insert(count);
    	}
    	Assert.assertTrue(dao.listAll().size()>0);
    }

    
    @Test
    public void retrieveTest(){
    	Count count=dao.retrieve(2l);
    	Assert.assertTrue(count.getName().contains("name"));
    }
    
    
    
    @Test
    public void timeStampLongTest(){
    	Count count=dao.retrieve(2l);
    	Assert.assertNotNull(count.getTimeStampLong());
    }
    
    @Test
    public void timeStampDateTest(){
    	Count count=dao.retrieve(2l);
    	Assert.assertNotNull(count.getTimeStampDate());
    }
    
    @Test
    public void timeStampCalendarTest(){
    	Count count=dao.retrieve(2l);
    	Assert.assertNotNull(count.getTimeStampCalendar());
    }
}