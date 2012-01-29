package org.easycassandra.persistence;

import java.util.HashMap;
import java.util.Map;

import org.easycassandra.annotations.read.DateRead;
import org.easycassandra.annotations.read.ReadInterface;
import org.easycassandra.annotations.write.DateWrite;
import org.easycassandra.annotations.write.WriteInterface;

/**
 *
 * @author otavio
 */
final class ReadWriteMaps {

    private static Map<String, WriteInterface> writeMap;
    private static Map<String, ReadInterface> readMap;

    public static Map<String, WriteInterface> getWriteMap() {
        if (writeMap == null) {
            initWriteMap();
        }
        return writeMap;
    }

    public static Map<String, ReadInterface> getReadMap() {
        if (readMap == null) {
            initReadMap();
        }
        return readMap;
    }

    private static void initWriteMap() {
        writeMap = new HashMap<>();
        writeMap.put("java.util.Date", new DateWrite());
      
    }

    private static void initReadMap() {
        readMap = new HashMap<>();
        readMap.put("java.util.Date", new DateRead());
    }
    
    /**
     * Singleton
     */
    private ReadWriteMaps(){
    }
}
