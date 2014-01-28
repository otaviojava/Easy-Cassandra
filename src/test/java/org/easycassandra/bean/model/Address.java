package org.easycassandra.bean.model;

import java.io.Serializable;
//import java.util.Objects;

import javax.persistence.Column;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Address DTO.
 */
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "state")
    private String state;

    @Column(name = "cyte")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "cep")
    private String cep;

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object obj) {
    	if (obj instanceof Address) {
    		Address other = Address.class.cast(obj);
    		return new EqualsBuilder().append(city, other.city).append(cep, other.cep).isEquals();
    	}
    	return false;

    }

    @Override
    public int hashCode() {
    	return new HashCodeBuilder().append(city).append(cep).toHashCode();
    }
}
