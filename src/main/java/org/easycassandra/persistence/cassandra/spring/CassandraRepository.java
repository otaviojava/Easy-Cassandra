package org.easycassandra.persistence.cassandra.spring;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import org.springframework.data.repository.CrudRepository;
/**
 * abstract template to Spring repository.
 * @author otaviojava
 * @param <T> the entity
 * @param <K> the key
 */
public abstract class CassandraRepository<T, K extends Serializable> implements
        CrudRepository<T, K> {

    /**
     * @return the cassandra tempalte
     */
	protected abstract CassandraTemplate getCassandraTemplate();

	private Class<T> beanClass;

	@Override
	public <S extends T> S save(S entity) {
		return getCassandraTemplate().save(entity);
	}

	@Override
	public <S extends T> Iterable<S> save(Iterable<S> entities) {
		return getCassandraTemplate().save(entities);
	}

	@Override
	public T findOne(K id) {
		return getCassandraTemplate().findOne(id, beanClass);
	}

	@Override
	public boolean exists(K id) {

		return findOne(id) != null;
	}

	@Override
	public Iterable<T> findAll() {
		return getCassandraTemplate().findAll(beanClass);
	}

	@Override
	public Iterable<T> findAll(Iterable<K> ids) {
		return getCassandraTemplate().findAll(ids, beanClass);
	}

	@Override
	public long count() {
		return getCassandraTemplate().count(beanClass);
	}

	@Override
	public void delete(K id) {
		getCassandraTemplate().delete(id, beanClass);
	}

	@Override
	public void delete(T entity) {
		getCassandraTemplate().delete(entity);

	}

	@Override
	public void delete(Iterable<? extends T> entities) {
		getCassandraTemplate().delete(entities);

	}

	@Override
	public void deleteAll() {
		getCassandraTemplate().deleteAll(beanClass);
	}

	/**
	 * constructor.
	 */
	@SuppressWarnings("unchecked")
	public CassandraRepository() {
        ParameterizedType genericType = (ParameterizedType) this.getClass()
                .getGenericSuperclass();
        this.beanClass = (Class<T>) genericType.getActualTypeArguments()[0];
	}

}
