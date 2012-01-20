package org.easycassandra.annotations.readwrite;

import junit.framework.Assert;

import org.easycassandra.annotations.read.BooleanRead;
import org.easycassandra.annotations.write.BooleanWrite;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author otavio
 */
public class BooleanReadWriteTest implements ReadWriteNumber{

    private BooleanRead booleanRead;
    
    private BooleanWrite booleanWrite;
    
    @Override
    @Test
    public void primitiveAfirmativeTest(){
    boolean booleanTest=(Boolean) booleanRead.getObjectByByte(booleanWrite.getBytebyObject(true));
    Assert.assertTrue(booleanTest);
       
    }
    
    @Override
    @Test
    public void primitiveNegativeTest(){
    boolean booleanTest=(Boolean) booleanRead.getObjectByByte(booleanWrite.getBytebyObject(false));
    Assert.assertFalse(booleanTest);
       
    }
  
	@Override
	@Test
	public void objectAfirmativeTest() {
		  	Boolean booleanTest=(Boolean) booleanRead.getObjectByByte(booleanWrite.getBytebyObject(Boolean.TRUE));
		    Assert.assertTrue(booleanTest);
		
	}

	@Override
	@Test
	public void objectNegativeTest() {
		   Boolean booleanTest=(Boolean) booleanRead.getObjectByByte(booleanWrite.getBytebyObject(Boolean.FALSE));
		    Assert.assertFalse(booleanTest);
		
	}
	
	  
    @Override
    @Before
    public void init(){
    booleanRead=new BooleanRead();
    booleanWrite =new BooleanWrite();
    }

}
