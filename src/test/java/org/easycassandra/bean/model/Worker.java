package org.easycassandra.bean.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


/**
 *  worker class.
 * @author otaviojava
 */
@MappedSuperclass
public class Worker implements Serializable {

	private static final long serialVersionUID = -5568409094833637814L;

	@Column
	private String name;

	@Column
	private Double salary;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

}
