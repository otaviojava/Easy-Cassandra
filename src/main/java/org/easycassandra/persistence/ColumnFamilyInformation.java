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
//import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

/**
 * The unit in XML Document
 *
 * @author otavio
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ColumnFamily")
public class ColumnFamilyInformation implements Serializable {

    private static final int HASH_CODE = 61;
    private static final int HASH_VALUE = 7;
    private static final long serialVersionUID = 859191542482693032L;
    /**
     * the number of the id Column Family
     */
    @XmlAttribute
    private BigInteger id;
    /**
     * name of the Column Family
     */
    @XmlAttribute
    private String columnFamilyName;
    /**
     * name of the KeyStore
     */
    @XmlAttribute
    private String keyStore;

    public BigInteger increment() {
        id = id.add(BigInteger.ONE);
        return id;
    }

    public String getColumnFamilyName() {
        return columnFamilyName;
    }

    public void setColumnFamilyName(String columnFamilyName) {
        this.columnFamilyName = columnFamilyName;
    }

    public ColumnFamilyInformation() {
        id = BigInteger.valueOf(0l);
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public ColumnFamilyInformation(String columnFamilyName, String keyStore) {
        id = BigInteger.valueOf(0l);
        this.columnFamilyName = columnFamilyName;
        this.keyStore = keyStore;
    }

    @Override
    public String toString() {
        return columnFamilyName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ColumnFamilyInformation other = (ColumnFamilyInformation) obj;
//        if (!Objects.equals(this.columnFamilyName, other.columnFamilyName)) {
//            return false;
//        }
//        if (!Objects.equals(this.keyStore, other.keyStore)) {
//            return false;
//        }
        if(!StringUtils.equals(this.columnFamilyName, other.columnFamilyName)){
        	return false;
        }
        if(StringUtils.equals(this.keyStore, other.keyStore)){
        	return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = HASH_VALUE;
        hash = HASH_CODE * hash + Integer.parseInt(this.columnFamilyName);
        return HASH_CODE * hash + Integer.parseInt(this.keyStore);
    }
}