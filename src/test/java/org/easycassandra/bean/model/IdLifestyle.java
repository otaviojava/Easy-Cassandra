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
    private Integer type;

    @Column
    private Integer companyId;

    public IdLifestyle() {

    }

    public IdLifestyle(final Integer companyId, final Long personId, final Integer type) {
        this.companyId = companyId;
        this.personId = personId;
        this.type = type;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(final Long personId) {
        this.personId = personId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(final Integer type) {
        this.type = type;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(final Integer companyId) {
        this.companyId = companyId;
    }
}
