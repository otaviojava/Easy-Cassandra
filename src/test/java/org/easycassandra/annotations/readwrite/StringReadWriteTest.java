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
public class StringReadWriteTest implements ReadWriteTest{

    private DefaultRead StringRead;
    
    private DefaultWrite defaultWrite;
    
    @Override
    @Test
    public void getObjectByByteAfirmativeTest(){
    String nameTest="Easy-Cassandra";
    String stringTest=(String) StringRead.getObjectByByte(defaultWrite.getBytebyObject(nameTest),String.class);
    Assert.assertEquals(stringTest, nameTest);
       
    }
    
    @Override
    @Test
    public void getObjectByByteNegativeTest(){
    String nameTest="Easy-Cassandra";
    String stringTest=(String) StringRead.getObjectByByte(defaultWrite.getBytebyObject("java"),String.class);
    Assert.assertFalse(stringTest.equals(nameTest));
       
    }
    
    @Override
    @Before
    public void init(){
    StringRead=new DefaultRead();
    defaultWrite =new DefaultWrite();
    }
}
