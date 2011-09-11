/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.persistence;

import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ColumnFamily")
public class ColumnFamilyInformation implements Serializable {

    @XmlAttribute
    private Long id;
    @XmlAttribute
    private String columnFamilyName;

    public Long increment() {
        return ++id;
    }

    public String getColumnFamilyName() {
        return columnFamilyName;
    }

    public void setColumnFamilyName(String columnFamilyName) {
        this.columnFamilyName = columnFamilyName;
    }

    public ColumnFamilyInformation() {
        id = new Long(0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ColumnFamilyInformation(String columnFamilyName) {
        id = new Long(0);
        this.columnFamilyName = columnFamilyName;
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

        if (this == obj) {
            return true;
        }

        if (obj instanceof ColumnFamilyInformation) {

            return columnFamilyName.equals(((ColumnFamilyInformation) obj).getColumnFamilyName());
        }

        return false;
    }

    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + Objects.hashCode(this.columnFamilyName);
        return hash;
    }
}