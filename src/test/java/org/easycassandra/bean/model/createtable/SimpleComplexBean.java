package org.easycassandra.bean.model.createtable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.easycassandra.Index;
/**
 * the SimpleComplexBean class.
 * @author otaviojava
 */
@Entity(name = "SimpleComplexBean")
public class SimpleComplexBean {

    @Id
    private Long id;

    @Index
    @Column(name = "name")
    private String name;

    @Column(name = "born")
    private Integer year;

    @Embedded
    private SubBean bean;

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

    public SubBean getBean() {
        return bean;
    }

    public void setBean(SubBean bean) {
        this.bean = bean;
    }

}
