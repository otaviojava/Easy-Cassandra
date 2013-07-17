package org.easycassandra.bean.model.createtable;

import javax.persistence.Column;

public class SubComplexId {

	@Column(name = "ida")
    private Integer ida;
    
    @Column(name = "idb")
    private String idb;

	public Integer getIda() {
		return ida;
	}

	public void setIda(Integer ida) {
		this.ida = ida;
	}

	public String getIdb() {
		return idb;
	}

	public void setIdb(String idb) {
		this.idb = idb;
	}
    
    
    
}
