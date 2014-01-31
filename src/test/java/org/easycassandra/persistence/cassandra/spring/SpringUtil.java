package org.easycassandra.persistence.cassandra.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
/**
 * singleton to spring.
 */
public enum SpringUtil {
INSTANCE;

	private ApplicationContext context = new GenericXmlApplicationContext("SpringConfig.xml");

	public ApplicationContext getContext() {
		return context;
	}

    /**
     * return the bean.
     * @param entityClass the entityClass
     *@param <T> kind of object
     * @return the bean
     */
	public <T> T getBean(Class<T> entityClass) {
		return context.getBean(entityClass);
	}
}
