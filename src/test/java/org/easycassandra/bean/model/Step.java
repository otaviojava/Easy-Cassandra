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

    private static final int DEFAULT_VALUE = 3;
    @Index
    @Column
    private Date date;

    /**
     * Only needed by reflection.
     */
    public Step() {

    }

    /**
     * constructor.
     * @param personId the personId
     * @param companyId the companyId
     */
    public Step(Long personId, Integer companyId) {
        setId(new IdLifestyle(personId, companyId, DEFAULT_VALUE));
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

}
