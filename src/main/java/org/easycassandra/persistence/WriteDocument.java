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

import java.util.concurrent.atomic.AtomicReference;
import org.easycassandra.util.DomUtil;

/**
 * This class is for Write the informations about id in Document XML
 *
 * @author otavio
 *
 */
public class WriteDocument implements Runnable {

    private static final long TIME_SLEEP = 3000l;
    /**
     * Information for Write in document
     */
    private AtomicReference<ColumnFamilyIds> columnReference;

    /**
     * Constructor
     *
     * @param lock
     * @param columnReference
     */
    public WriteDocument(AtomicReference<ColumnFamilyIds> columnReference) {
        this.columnReference = columnReference;
    }

    @Override
    public void run() {
        while (true) {
              if(columnReference.get().size()>0){
            DomUtil.getFileDom(columnReference.get());
              }
            try {
                Thread.sleep(TIME_SLEEP);
            } catch (InterruptedException e) {
            }
        }
    }
}
