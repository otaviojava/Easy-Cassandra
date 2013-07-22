package org.easycassandra.bean.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.easycassandra.SetData;


@Entity(name="contact")
public class Contact implements Serializable {

    @Id
    @Column(name="id")
    private String name;
    
    
    @Column(name="emails")
    @SetData(classData=String.class)
    private Set<String> emails;
    
    @Column
    private String cyte;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getEmails() {
        return emails;
    }

    public void setEmails(Set<String> emails) {
        this.emails = emails;
    }

    public String getCyte() {
        return cyte;
    }

    public void setCyte(String cyte) {
        this.cyte = cyte;
    }

        
    
    
}
