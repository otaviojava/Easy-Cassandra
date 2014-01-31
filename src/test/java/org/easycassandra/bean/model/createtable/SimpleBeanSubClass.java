package org.easycassandra.bean.model.createtable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * the SimpleBeanSubClass class.
 * @author otaviojava
 */
@Entity(name = "SimpleBeanSubClass")
public class SimpleBeanSubClass extends SimpleBeanSuperClass {

    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "born")
    private Integer year;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
