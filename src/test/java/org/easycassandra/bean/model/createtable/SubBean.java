package org.easycassandra.bean.model.createtable;

import javax.persistence.Column;
/**
 * the SubBean class.
 * @author otaviojava
 */
public class SubBean {

    @Column(name = "simpleNameA")
    private String simpleNameA;

    @Column(name = "simpleNameB")
    private String simpleNameB;

    public String getSimpleNameA() {
        return simpleNameA;
    }

    public void setSimpleNameA(String simpleNameA) {
        this.simpleNameA = simpleNameA;
    }

    public String getSimpleNameB() {
        return simpleNameB;
    }

    public void setSimpleNameB(String simpleNameB) {
        this.simpleNameB = simpleNameB;
    }

}
