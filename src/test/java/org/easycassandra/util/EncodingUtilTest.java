/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.util;

import java.nio.ByteBuffer;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author otavio
 */
public class EncodingUtilTest {

    @Test
    public void stringToByteTest() {

        Assert.assertEquals(EncodingUtil.stringToByte("teste"), ByteBuffer.wrap("teste".getBytes()));
        
    }
    
    @Test
    public void stringToByteFailTest() {

        Assert.assertFalse(EncodingUtil.stringToByte("teste2").equals(ByteBuffer.wrap("teste".getBytes())));
        
    }
    
    @Test
    public void byteToStringTest(){
    	String testValue="Cassandra";
    	Assert.assertEquals(EncodingUtil.byteToString(EncodingUtil.stringToByte(testValue)),testValue);
    }
    
    @Test
    public void byteToStringFailTest(){
    	String testValue="Cassandra";
    	Assert.assertFalse(EncodingUtil.byteToString(EncodingUtil.stringToByte(testValue)).equals("Java Test"));
    }
    
}
