package org.easycassandra.bean.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.easycassandra.Index;

@Entity
public class Animal implements Serializable {

	
	 	/**
	 * 
	 */
	private static final long serialVersionUID = -6438374234472941694L;


		@Id
	    private String name;
	    
	  
	    @Column
	    private String race;
	    
	    @Index
	    @Column
	    private String country;


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}


		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Animal other = (Animal) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}


		public String getName() {
			return name;
		}


		public void setName(String name) {
			this.name = name;
		}


		public String getRace() {
			return race;
		}


		public void setRace(String race) {
			this.race = race;
		}


		public String getCountry() {
			return country;
		}


		public void setCountry(String country) {
			this.country = country;
		}
	    
	    


	    
	
}
