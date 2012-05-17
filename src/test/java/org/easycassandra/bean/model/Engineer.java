package org.easycassandra.bean.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Engineer extends Worker {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6643883283637783076L;

	@Id
	private String nickName;
	
	@Column
	private String type;
	
	@Column
	private String especialization;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEspecialization() {
		return especialization;
	}

	public void setEspecialization(String especialization) {
		this.especialization = especialization;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	
	
	
}
