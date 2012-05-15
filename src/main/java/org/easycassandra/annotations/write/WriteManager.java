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
package org.easycassandra.annotations.write;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Map;

/**
 * This class see and verify the Object to transform in Byte
 *
 * @author otavio
 *
 */
public class WriteManager {

    private DefaultWrite defaultWrite;
    private PathWrite pathWrite;
    private FileWrite fileWrite;
    private CalendarWrite calendarWrite;
    private Map<String, WriteInterface> writeMap;

    public ByteBuffer convert(Object value) {
        WriteInterface writeInterface = writeMap.get(value.getClass().getName());
        if (writeInterface != null) {
            return writeInterface.getBytebyObject(value);
        } else if (value instanceof Path) {
            return pathWrite.getBytebyObject(value);
        } else if (value instanceof File) {
            return fileWrite.getBytebyObject(value);
        } else if (value instanceof Calendar) {
            return calendarWrite.getBytebyObject(value);
        }
        return defaultWrite.getBytebyObject(value);
    }

    public WriteManager(Map<String, WriteInterface> writeMap) {
        this.writeMap = writeMap;
        defaultWrite = new DefaultWrite();
        pathWrite = new PathWrite();
        fileWrite = new FileWrite();
        calendarWrite = new CalendarWrite();
    }
}
