package org.easycassandra.persistence;

import org.easycassandra.annotations.read.BooleanRead;
import org.easycassandra.annotations.read.DateRead;
import org.easycassandra.annotations.read.DoubleRead;
import org.easycassandra.annotations.read.FloatRead;
import org.easycassandra.annotations.read.IntegerRead;
import org.easycassandra.annotations.read.LongRead;
import org.easycassandra.annotations.read.ReadInterface;
import org.easycassandra.annotations.read.StringRead;
import org.easycassandra.annotations.write.BooleanWrite;

import org.easycassandra.annotations.write.DoubleWrite;
import org.easycassandra.annotations.write.FloatWrite;



import java.util.HashMap;
import java.util.Map;
import org.easycassandra.annotations.write.DateWrite;
import org.easycassandra.annotations.write.IntegerWrite;
import org.easycassandra.annotations.write.LongWrite;
import org.easycassandra.annotations.write.StringWrite;
import org.easycassandra.annotations.write.WriteInterface;

/**
 *
 * @author otavio
 */
class ReadWriteMaps {

    protected static Map<String, WriteInterface> writeMap;
    protected static Map<String, ReadInterface> readMap;

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
        writeMap.put("java.lang.String", new StringWrite());
        writeMap.put("java.lang.Long", new LongWrite());
        writeMap.put("java.lang.Double", new DoubleWrite());
        writeMap.put("java.lang.Float", new FloatWrite());
        writeMap.put("java.lang.Integer", new IntegerWrite());
        writeMap.put("java.util.Date", new DateWrite());
        writeMap.put("java.lang.Boolean", new BooleanWrite());
    }

    private static void initReadMap() {
        readMap = new HashMap<>();
        readMap.put("java.lang.String", new StringRead());
        readMap.put("java.lang.Long", new LongRead());
        readMap.put("java.lang.Double", new DoubleRead());
        readMap.put("java.lang.Float", new FloatRead());
        readMap.put("java.lang.Integer", new IntegerRead());
        readMap.put("java.util.Date", new DateRead());
        readMap.put("java.lang.Boolean", new BooleanRead());
    }
}
