package org.easycassandra.bean.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

/**
 * @author Nenita A. Casuga
 *         Date 10/28/13
 */
@MappedSuperclass
public class Lifestyle {

    @EmbeddedId
    private IdLifestyle id;

    @Column
    private Double value;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public IdLifestyle getId() {
        return id;
    }

    public void setId(final IdLifestyle id) {
        this.id = id;
    }

}
