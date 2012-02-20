package org.easycassandra.bean;

import junit.framework.Assert;

import org.easycassandra.bean.dao.CountDAO;
import org.easycassandra.bean.model.Count;
import org.junit.Test;


/**
*
* @author otavio
*/
public class CountDAOTest {

    private CountDAO dao = new CountDAO();
    
    
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
    
    
}