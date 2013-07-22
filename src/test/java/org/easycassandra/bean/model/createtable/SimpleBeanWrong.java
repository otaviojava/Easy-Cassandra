package org.easycassandra.bean.model.createtable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.easycassandra.Index;

@Entity(name = "SimpleBeanWrong")
public class SimpleBeanWrong {

	@Id
    private Long id;
    
    @Index
    @Column(name = "name")
    private String name;
    
    @Column(name = "born")
    private String year;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
    
    
    
}
