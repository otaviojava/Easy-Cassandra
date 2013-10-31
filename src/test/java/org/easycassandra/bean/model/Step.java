package org.easycassandra.bean.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;

import org.easycassandra.Index;

/**
 * @author Nenita A. Casuga
 *         Date 10/28/13
 */
@Entity(name = "lifestyle")
public class Step extends Lifestyle {

    @Index
    @Column
    private Date date;

    /**
     * Only needed by reflection
     */
    public Step() {

    }


    public Step(Long personId, Integer companyId) {
        setId(new IdLifestyle(personId, companyId, 3));
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

}
