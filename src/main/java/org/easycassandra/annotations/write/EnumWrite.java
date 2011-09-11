/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.annotations.write;

import org.easycassandra.util.EncodingUtil;
import java.nio.ByteBuffer;

/**
 *
 * @author otavio
 */
public class EnumWrite implements WriteInterface {

    @Override
    public ByteBuffer getBytebyObject(Object object) {
        
            Enum enumObject= (Enum)object;
            Integer index=0;
            Object[] enums=enumObject.getClass().getEnumConstants();
            for(int i=0;i<enums.length;i++){
                if(enumObject.equals(enums[i])){
                index=i;
                break;
                }
                
            }
            
          
          return EncodingUtil.stringToByte(index.toString()); 
        
    }
}
