/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "References")
@XmlAccessorType(XmlAccessType.FIELD)
public class ColumnFamilyIds implements Serializable {

    @XmlElementWrapper(name = "columnFamilies")
    private List<ColumnFamilyInformation> columnFamilyInformation;

    public ColumnFamilyInformation getColumnFamilyReference(){
    return columnFamilyInformation.get(0);
    }
    public Long getId(String nameColumnFamily) {
        ColumnFamilyInformation cfr = new ColumnFamilyInformation(nameColumnFamily);

        if (!columnFamilyInformation.contains(cfr)) {
            columnFamilyInformation.add(cfr);
        }
        return columnFamilyInformation.get(columnFamilyInformation.indexOf(cfr)).increment();

    }

    public int size() {

        return columnFamilyInformation.size();

    }

    public ColumnFamilyIds() {
        columnFamilyInformation = new ArrayList<>();
    }

  
    
}
