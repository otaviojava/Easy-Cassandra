/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra;

/**
  * @author otavio
 */
public enum ConsistencyLevelCQL {

    ZERO("CONSISTENCY ZERO"), ONE("CONSISTENCY ONE"), QUORUM("CONSISTENCY QUORUM"), ALL("CONSISTENCY ALL"), DCQUORUM("CONSISTENCY DCQUORUM"), DCQUORUMSYNC("CONSISTENCY DCQUORUMSYNC");
    
    private String value;

    ConsistencyLevelCQL(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
