package org.easycassandra.persistence;

import java.util.List;

public interface PersistenceCassandra extends Persistence{

	<T> List<T> findByIndex(Object index,Class<T> bean);
	
	Long count(Class<?> bean);
}
