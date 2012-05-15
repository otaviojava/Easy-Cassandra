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
package org.easycassandra.annotations.read;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Paths;

public class PathRead implements ReadInterface {

    private final FileRead fileRead = new FileRead();

    @Override
    public Object getObjectByByte(ByteBuffer buffer) {
        File file = (File) fileRead.getObjectByByte(buffer);
        return Paths.get(file.getAbsolutePath());
    }
}
