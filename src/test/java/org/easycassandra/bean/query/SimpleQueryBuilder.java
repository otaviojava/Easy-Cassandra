package org.easycassandra.bean.query;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.easycassandra.Index;

/**
 * Bean to use query builder.
 * @author otaviojava
 */
@Entity(name = "simplequerybuilder")
public class SimpleQueryBuilder {

    @EmbeddedId
    private SimpleID id = new SimpleID();

    @Index
    @Column(name = "namebuilder")
    private String name;

    @Column(name = "valuebuilder")
    private Double value;

    public SimpleID getId() {
        return id;
    }

    public void setId(SimpleID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }


}
