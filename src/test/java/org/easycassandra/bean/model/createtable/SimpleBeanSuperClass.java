package org.easycassandra.bean.model.createtable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class SimpleBeanSuperClass {


	@Column(name = "super")
    private String superBean;

	public String getSuperBean() {
		return superBean;
	}

	public void setSuperBean(String superBean) {
		this.superBean = superBean;
	}
	
	
		
}
