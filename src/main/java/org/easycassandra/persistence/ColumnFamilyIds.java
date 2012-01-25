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


/**
 * The Clas for create in XML document
 * @see  ColumnFamilyInformation
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
     * @see  ColumnFamilyIdsTest
     */
    @XmlElementWrapper(name = "columnFamilies")
    private List<ColumnFamilyInformation> columnFamilyInformation;

    
    
    /**
     * with the name get the column family and increment one value, 
     * if there are not it will be created and start with the value 1
     * @param nameColumnFamily - The name of the Column Family
     * @param keyStore - 
     * @return -the id of the Column Family
     */
    public Long getId(String nameColumnFamily,String keyStore) {
        ColumnFamilyInformation cfr = new ColumnFamilyInformation(nameColumnFamily,keyStore);

        if (!columnFamilyInformation.contains(cfr)) {
            columnFamilyInformation.add(cfr);
        }
        return columnFamilyInformation.get(columnFamilyInformation.indexOf(cfr)).increment();

    }

    /**
     * The size of the List
     * @return 
     */
    public int size() {

        return columnFamilyInformation.size();

    }

    public ColumnFamilyIds() {
        columnFamilyInformation = new ArrayList<>();
    }

  
    
}
