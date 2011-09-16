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
}
