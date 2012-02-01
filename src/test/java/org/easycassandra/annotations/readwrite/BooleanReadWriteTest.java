package org.easycassandra.annotations.readwrite;

import junit.framework.Assert;

import org.easycassandra.annotations.read.DefaultRead;
import org.easycassandra.annotations.write.DefaultWrite;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author otavio
 */
public class BooleanReadWriteTest implements ReadWriteNumber{

    private DefaultRead defaultRead;
    
    private DefaultWrite defaultWrite;
    
    @Override
    @Test
    public void primitiveAfirmativeTest(){
    boolean booleanTest=(Boolean) defaultRead.getObjectByByte(defaultWrite.getBytebyObject(true),Boolean.class);
    Assert.assertTrue(booleanTest);
       
    }
    
    @Override
    @Test
    public void primitiveNegativeTest(){
    boolean booleanTest=(Boolean) defaultRead.getObjectByByte(defaultWrite.getBytebyObject(false),Boolean.class);
    Assert.assertFalse(booleanTest);
     
    }
  
	@Override
	@Test
	public void objectAfirmativeTest() {
		  	Boolean booleanTest=(Boolean) defaultRead.getObjectByByte(defaultWrite.getBytebyObject(Boolean.TRUE),Boolean.class);
		    Assert.assertTrue(booleanTest);
		
	}

	@Override
	@Test
	public void objectNegativeTest() {
		   Boolean booleanTest=(Boolean) defaultRead.getObjectByByte(defaultWrite.getBytebyObject(Boolean.FALSE),Boolean.class);
		    Assert.assertFalse(booleanTest);
		
	}
	
	  
    @Override
    @Before
    public void init(){
    defaultRead=new DefaultRead();
    defaultWrite =new DefaultWrite();
    }

}
