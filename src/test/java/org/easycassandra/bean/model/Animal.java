package org.easycassandra.bean.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.easycassandra.Index;
/**
 * the Animal class.
 * @author otaviojava
 */
@Entity
public class Animal implements Serializable {

	private static final long serialVersionUID = -6438374234472941694L;

    @Id
    private String name;

    @Column
    private String race;

    @Index
    @Column
    private String country;

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Animal) {
            Animal other = Animal.class.cast(obj);
            return new EqualsBuilder().append(name, other.name).isEquals();
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
