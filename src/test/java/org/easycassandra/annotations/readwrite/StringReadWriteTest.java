package org.easycassandra.annotations.readwrite;

import junit.framework.Assert;

import org.easycassandra.annotations.read.StringRead;
import org.easycassandra.annotations.write.StringWrite;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author otavio
 */
public class StringReadWriteTest implements ReadWriteTest{

    private StringRead StringRead;
    
    private StringWrite StringWrite;
    
    @Override
    @Test
    public void getObjectByByteAfirmativeTest(){
    String nameTest="Easy-Cassandra";
    String stringTest=(String) StringRead.getObjectByByte(StringWrite.getBytebyObject(nameTest));
    Assert.assertEquals(stringTest, nameTest);
       
    }
    
    @Override
    @Test
    public void getObjectByByteNegativeTest(){
    String nameTest="Easy-Cassandra";
    String stringTest=(String) StringRead.getObjectByByte(StringWrite.getBytebyObject("java"));
    Assert.assertFalse(stringTest.equals(nameTest));
       
    }
    
    @Override
    @Before
    public void init(){
    StringRead=new StringRead();
    StringWrite =new StringWrite();
    }
}
