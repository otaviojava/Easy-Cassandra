package org.easycassandra.persistence;

public class EasyCassandraException extends RuntimeException {
	
	private static final long serialVersionUID = 3614847234658361197L;

	/**
	 * The Constructor for Exception
	 * @param message
	 */
	public EasyCassandraException(String message) {  
        super(message);
    }  
  

}
