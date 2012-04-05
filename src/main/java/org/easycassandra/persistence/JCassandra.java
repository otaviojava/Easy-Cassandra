package org.easycassandra.persistence;

import java.util.List;

/**
 * Inteface to run CQL
 * @author otavio
 *
 */
public interface JCassandra  {

	/**
	 * Execute a cql and return the result results as a List.
	 * @return result in list
	 */
	List getResultList();

	/**
	 * Execute a cql that returns a single result.
	 * @return the single result
	 */
	Object getSingleResult();
	
	/**
	 * Set the position of the first result to retrieve.
	 * @param startPosition -  the start position of the first result, numbered from 0
	 */
	void setFirstResult(int startPosition);

	/**
	 * Set the maximum number of results to retrieve.
	 * @param maxResult
	 */
	void setMaxResults(int maxResult);

}
