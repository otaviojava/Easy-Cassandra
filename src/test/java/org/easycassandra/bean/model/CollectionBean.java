package org.easycassandra.bean.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "collection")
public class CollectionBean {

	@Id
	private UUID id;

	@ElementCollection
	@Column(name = "mapBean")
	private Map<String, String> map;

	@ElementCollection
	@Column(name = "setBean")
	private Set<String> set;

	@ElementCollection
	@Column(name = "listBean")
	private List<String> list;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public Set<String> getSet() {
		return set;
	}

	public void setSet(Set<String> set) {
		this.set = set;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}
	
	
	
}
