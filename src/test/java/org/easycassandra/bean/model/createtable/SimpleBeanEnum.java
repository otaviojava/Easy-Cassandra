package org.easycassandra.bean.model.createtable;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.Id;

public class SimpleBeanEnum {
	
	@Id
	private String id;

	@Column(name="name")
	private String name;
	
	@Enumerated
	private SimpleEnum simpleEnum;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SimpleEnum getSimpleEnum() {
		return simpleEnum;
	}

	public void setSimpleEnum(SimpleEnum simpleEnum) {
		this.simpleEnum = simpleEnum;
	}
	
	
}
