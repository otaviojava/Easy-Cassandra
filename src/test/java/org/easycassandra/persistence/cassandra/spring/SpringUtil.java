package org.easycassandra.persistence.cassandra.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public enum SpringUtil {
INSTANCE;

	private ApplicationContext context = new GenericXmlApplicationContext("SpringConfig.xml");

	public ApplicationContext getContext(){
		return context;
	}

	public <T> T getBean(Class<T> entityClass){
		return context.getBean(entityClass);
	}
}
