package org.easycassandra.bean.model.createtable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
/**
 * the SimpleBeanComplexId object.
 * @author otaviojava
 */
public class SimpleBeanComplexId {

    @EmbeddedId
    private SubComplexId id;

    @Column(name = "name")
    private String name;

    @Column(name = "born")
    private Integer year;

    public SubComplexId getId() {
        return id;
    }

    public void setId(SubComplexId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

}
