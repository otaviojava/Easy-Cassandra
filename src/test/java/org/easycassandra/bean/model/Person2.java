/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.bean.model;

import java.io.File;
import java.io.Serializable;
//import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author otavio
 */
@Entity(name = "person")
public class Person2 implements Serializable {

    private static final long serialVersionUID = 3L;
    
    private Long id;
    
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "born")
    private Integer year;
    
    
    @Enumerated
    private Sex sex;
    
    @Column(name = "file")
    private File personalFile;
    
    @Embedded
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
        final Person2 other = (Person2) obj;
        if (!StringUtils.equals(String.valueOf(this.id.intValue()), String.valueOf(other.id.intValue()))) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 3;
        return 97 * hash + Integer.valueOf(this.id.intValue());
        
    }
    
    
    
    
}
