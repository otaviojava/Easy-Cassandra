/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.bean;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;
import org.easycassandra.annotations.ColumnFamilyValue;
import org.easycassandra.annotations.ColumnValue;
import org.easycassandra.annotations.EmbeddedValue;
import org.easycassandra.annotations.EnumeratedValue;
import org.easycassandra.annotations.IndexValue;
import org.easycassandra.annotations.KeyValue;

/**
 *
 * @author otavio
 */
@ColumnFamilyValue(nome = "person")
public class Person implements Serializable {

    private static final long serialVersionUID = 3L;
    
    @KeyValue
    private Long id;
    
    @IndexValue
    @ColumnValue(nome = "name")
    private String name;
    
    @ColumnValue(nome = "born")
    private Integer year;
    
    
    @EnumeratedValue(nome="sex")
    private Sex sex;
    
    @ColumnValue(nome = "file")
    private File personalFile;
    
    @EmbeddedValue
    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    
    public File getPersonalFile() {
		return personalFile;
	}

	public void setPersonalFile(File personalFile) {
		this.personalFile = personalFile;
	}

	public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 3;
        return 97 * hash + Objects.hashCode(this.id);
        
    }
    
    
    
    
}
