package org.easycassandra.annotations.write;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *for Write Key
 * @author otavio
 */
public class KeyWrite implements WriteInterface {

    @Override
    public ByteBuffer getBytebyObject(Object object) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeLong((Long) object);

            // etc.  
            dos.flush();
            byte[] data = bos.toByteArray();
            return ByteBuffer.wrap(data);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
