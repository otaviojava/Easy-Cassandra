package org.easycassandra.bean.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.easycassandra.Index;

/**
 * the Drink class.
 * @author otaviojava
 */
@Table(name = " drink", schema = "schemaA")
public class Drink implements Serializable {


    private static final long serialVersionUID = -4750871137268349630L;

    @Id
    private Long id;

    @Index
    @Column(name = "name")
    private String name;

    @Column(name = "flavor")
    private String flavor;

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

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

}
