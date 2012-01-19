package org.easycassandra.annotations.write;

import java.nio.ByteBuffer;
import org.easycassandra.util.EncodingUtil;

/**
 *for Write Enums
 * @see Enum
 * @author otavio
 */
public class EnumWrite implements WriteInterface {

    @Override
    @SuppressWarnings("rawtypes")
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
