package org.easycassandra.bean.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.easycassandra.annotations.Index;

@Entity
public class Log implements Serializable {
	
private static final long serialVersionUID = 3L;

@Id
private String uuid;

@Index
@Column(name = "user_uuid")
private String user_uuid;

public String getUuid() {
	return uuid;
}

public void setUuid(String uuid) {
	this.uuid = uuid;
}

public String getUser_uuid() {
	return user_uuid;
}

public void setUser_uuid(String user_uuid) {
	this.user_uuid = user_uuid;
}



}
