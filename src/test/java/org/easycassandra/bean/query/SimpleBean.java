package org.easycassandra.bean.query;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.easycassandra.Index;

/**
 * Bean to use query builder.
 * @author otaviojava
 */
@Entity(name = "simplequerybuilder")
public class SimpleBean {

    @EmbeddedId
    private SimpleID id = new SimpleID();

    @Index
    @Column(name = "namebuilder")
    private String name;

    @Column(name = "valuebuilder")
    private Double value;

    @ElementCollection
    @Column(name = "mapbuilder")
    private Map<String, String> map;

    @ElementCollection
    @Column(name = "listbuilder")
    private List<String> list;

    @ElementCollection
    @Column(name = "setbuilder")
    private Set<String> set;

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

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Set<String> getSet() {
        return set;
    }

    public void setSet(Set<String> set) {
        this.set = set;
    }


}
