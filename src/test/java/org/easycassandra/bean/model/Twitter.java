package org.easycassandra.bean.model;

import java.nio.file.Path;

import org.easycassandra.annotations.ColumnFamilyValue;
import org.easycassandra.annotations.ColumnValue;
import org.easycassandra.annotations.KeyValue;

@ColumnFamilyValue
public class Twitter {

	
	 	@KeyValue
	    private String name;
	    
	  
	    @ColumnValue(name = "name")
	    private String twitte;
	    
	    
	    @ColumnValue
	    private Path path;

		public String getName() {
			return name;
		}


		public void setName(String name) {
			this.name = name;
		}


		public String getTwitte() {
			return twitte;
		}


		public void setTwitte(String twitte) {
			this.twitte = twitte;
		}


		
		
		public Path getPath() {
			return path;
		}


		public void setPath(Path path) {
			this.path = path;
		}


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result
					+ ((twitte == null) ? 0 : twitte.hashCode());
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
			Twitter other = (Twitter) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (twitte == null) {
				if (other.twitte != null)
					return false;
			} else if (!twitte.equals(other.twitte))
				return false;
			return true;
		}
	    
	    
	
}
