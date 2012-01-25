package org.easycassandra.persistence;

import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * The unit in XML Document
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
    private Long id;
    
    
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


	public String getKeyStore() {
		return keyStore;
	}

	public void setKeyStore(String keyStore) {
		this.keyStore = keyStore;
	}

	public ColumnFamilyInformation(String columnFamilyName,String keyStore) {
        id = new Long(0);
        this.columnFamilyName = columnFamilyName;
        this.keyStore=keyStore;
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
        if (!Objects.equals(this.columnFamilyName, other.columnFamilyName)) {
            return false;
        }
        if (!Objects.equals(this.keyStore, other.keyStore)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = HASH_VALUE;
        hash = HASH_CODE * hash + Objects.hashCode(this.columnFamilyName);
        return HASH_CODE * hash + Objects.hashCode(this.keyStore);
      
    }



	


}