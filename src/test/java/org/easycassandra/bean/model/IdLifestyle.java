package org.easycassandra.bean.model;

import javax.persistence.Column;

/**
 * @author Nenita A. Casuga
 *         Date 10/28/13
 */
public class IdLifestyle {

    // No support for @Column(name =  "person_id")
    @Column
    private Long personId;

    @Column
    private Integer companyId;

    @Column
    private Integer type;

    public IdLifestyle() {

    }

    public IdLifestyle(final Long personId, final Integer companyId, final Integer type) {
        this.personId = personId;
        this.companyId = companyId;
        this.type = type;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(final Long personId) {
        this.personId = personId;
    }


    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(final Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(final Integer type) {
        this.type = type;
    }
}
