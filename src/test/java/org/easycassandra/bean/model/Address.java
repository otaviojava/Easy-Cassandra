/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.bean.model;

import java.io.Serializable;
import java.util.Objects;
import org.easycassandra.annotations.ColumnValue;

/**
 *
 * não é necessário colocar nenhuma identificação na classe
 */
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ColumnValue(nome="state")
    private String state;
    
    @ColumnValue(nome="cyte")
    private String city;
    
    @ColumnValue(nome="street")
    private String street;
    
    @ColumnValue(nome="cep")
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

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Address other = (Address) obj;
        if (!Objects.equals(this.city, other.city)) {
            return false;
        }
        if (!Objects.equals(this.cep, other.cep)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.city);
        hash = 97 * hash + Objects.hashCode(this.cep);
        return hash;
    }
    
    
    
    
}
