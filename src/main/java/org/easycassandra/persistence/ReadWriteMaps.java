/*
 * Copyright 2012 Otávio Gonçalves de Santana (otaviojava)
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.easycassandra.persistence;

import java.util.HashMap;
import java.util.Map;
import org.easycassandra.annotations.read.*;
import org.easycassandra.annotations.write.DateWrite;
import org.easycassandra.annotations.write.WriteInterface;

/**
 * Class for persist the default Read and Write Interfaces.
 *
 * @author otavio
 */
final class ReadWriteMaps {

    /**
     * key-value for WriteInterface
     *
     * @see WriteInterface
     */
    private static Map<String, WriteInterface> writeMap;
    /**
     * key-value for ReadInterface
     *
     * @see ReadInterface
     */
    private static Map<String, ReadInterface> readMap;

    /**
     * flywith in writeMap
     *
     * @return
     */
    public static Map<String, WriteInterface> getWriteMap() {
        if (writeMap == null) {
            initWriteMap();
        }
        return writeMap;
    }

    /**
     * flywith in readMap
     *
     * @return
     */
    public static Map<String, ReadInterface> getReadMap() {
        if (readMap == null) {
            initReadMap();
        }
        return readMap;
    }

    /**
     * init the Write map
     *
     * @see ReadWriteMaps#writeMap
     */
    private static void initWriteMap() {
        writeMap = new HashMap<String, WriteInterface>();
        writeMap.put("java.util.Date", new DateWrite());
    }

    /**
     * init the readMapd
     *
     * @see ReadWriteMaps#readMap
     */
    private static void initReadMap() {
        readMap = new HashMap<String, ReadInterface>();
        readMap.put("java.util.Date", new DateRead());
        readMap.put("java.io.File", new FileRead());
        readMap.put("java.nio.file.Path", new PathRead());
        readMap.put("java.util.Calendar", new CalendarRead());
    }

    /**
     * Singleton
     */
    private ReadWriteMaps() {
    }
}
