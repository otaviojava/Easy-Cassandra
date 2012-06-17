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

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Clas for create in XML document
 *
 * @see ColumnFamilyInformation
 * @author otavio
 */
@XmlRootElement(name = "References")
@XmlAccessorType(XmlAccessType.FIELD)
public class ColumnFamilyIds implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4263760877341591338L;
    /**
     * The unit for The Column Family
     *
     * @see ColumnFamilyIds
     */
    @XmlElementWrapper(name = "columnFamilies")
    private List<ColumnFamilyInformation> columnFamilyInformation;

    /**
     * with the name get the column family and increment one value, if there are
     * not it will be created and start with the value 1
     *
     * @param nameColumnFamily - The name of the Column Family
     * @param keyStore -
     * @return -the id of the Column Family
     */
    public BigInteger getId(String nameColumnFamily, String keyStore) {
        ColumnFamilyInformation cfr = new ColumnFamilyInformation(
                nameColumnFamily, keyStore);
        if (!columnFamilyInformation.contains(cfr)) {
            columnFamilyInformation.add(cfr);
        }
        return columnFamilyInformation.get(
                columnFamilyInformation.indexOf(cfr)).increment();
    }

    /**
     * The size of the List
     *
     * @return the size of the list
     */
    public int size() {
        return columnFamilyInformation.size();
    }

    public ColumnFamilyIds() {
        columnFamilyInformation = new ArrayList<ColumnFamilyInformation>();
    }
}
