/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.bean.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.easycassandra.Index;

/**
 *
 * @author otavio
 */
@Entity(name = "person")
public class Person implements Serializable {

    private static final long serialVersionUID = 3L;

    @Id
    private Long id;

    @Index
    @Column(name = "name")
    private String name;

    @Column(name = "born")
    private Integer year;

    @Enumerated
    private Sex sex;


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


    @Override
	public boolean equals(Object obj) {
        if (obj instanceof Person) {
            Person other = Person.class.cast(obj);
            return new EqualsBuilder().append(id, other.id).isEquals();
        }

        return false;
    }

    @Override
    public int hashCode() {
       return new HashCodeBuilder().append(id).toHashCode();

    }

}