package org.easycassandra.persistence.cassandra.spring.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.easycassandra.Index;


/**
 *
 * @author otavio
 */
@Entity(name = "contact")
public class Contact implements Serializable {

    private static final long serialVersionUID = 3L;

    @Id
    private UUID id;

    @Index
    @Column(name = "name")
    private String name;

    @Column(name = "born")
    private Integer year;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

	@Override
	public int hashCode() {
	    return new HashCodeBuilder().append(id).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Contact) {
		    Contact other = Contact.class.cast(obj);
		    return new EqualsBuilder().append(id, other.id).isEquals();
		}
		return false;
	}

}