package org.easycassandra.bean;

import junit.framework.Assert;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.Primitive;
import org.junit.Test;


/**
*
* @author otavio
*/
public class PrimitiveDAOTest {

	private PersistenceDao<Primitive> dao = new PersistenceDao<Primitive>(Primitive.class);
    
    
    @Test
    public void insertTest(){
    Primitive primitive=new Primitive();
    primitive.setId(1);
    primitive.setDoubleValue(1d);
    primitive.setFloatValue(2f);
    primitive.setIntegerValue(2);
    primitive.setLongValue(2l);
    primitive.setBooleanValue(true);
    primitive.setTypeName("integer");
    Assert.assertTrue(dao.insert(primitive));
    }
    
    @Test
    public void retrieveDoubleTest(){
    	Primitive primitive=(Primitive)dao.retrieve(1);
    	Assert.assertFalse(primitive.getDoubleValue()==0d);
    	
    }
    @Test
    public void retrieveDoubleEqualTest(){
    	Primitive primitive=(Primitive)dao.retrieve(1);
    	Assert.assertTrue(primitive.getDoubleValue()==1d);
    	
    }
    
    
    @Test
    public void retrieveFloatTest(){
    	Primitive primitive=(Primitive)dao.retrieve(1);
    	Assert.assertFalse(primitive.getFloatValue()==0f);
    	
    }
    
    @Test
    public void retrieveFloatEqualTest(){
    	Primitive primitive=(Primitive)dao.retrieve(1);
    	Assert.assertTrue(primitive.getFloatValue()==2f);
    	
    }

    @Test
    public void retrieveIntegerTest(){
    	Primitive primitive=(Primitive)dao.retrieve(1);
    	Assert.assertFalse(primitive.getIntegerValue()==0);
    	
    }
    @Test
    public void retrieveIntegerEqualTest(){
    	Primitive primitive=(Primitive)dao.retrieve(1);
    	Assert.assertTrue(primitive.getIntegerValue()==2);
    	
    }
    

    @Test
    public void retrieveLongTest(){
    	Primitive primitive=(Primitive)dao.retrieve(1);
    	Assert.assertFalse(primitive.getLongValue()==0);
    	
    }
    @Test
    public void retrieveLongEqualTest(){
    	Primitive primitive=(Primitive)dao.retrieve(1);
    	Assert.assertTrue(primitive.getLongValue()==2);
    	
    }

    @Test
    public void retrievebooleanTest(){
    	Primitive primitive=(Primitive)dao.retrieve(1);
    	Assert.assertFalse(!primitive.getBooleanValue());
    	
    }
    
    @Test
    public void retrievebooleanEqualTest(){
    	Primitive primitive=(Primitive)dao.retrieve(1);
    	Assert.assertTrue(primitive.getBooleanValue());
    	
    }
    
}