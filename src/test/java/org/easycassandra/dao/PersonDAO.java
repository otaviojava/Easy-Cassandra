/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.dao;

import java.util.List;


import org.easycassandra.bean.Person;
import org.easycassandra.persistence.EasyCassandraManager;
import org.easycassandra.persistence.Persistence;

/**
 *
 * @author otaviojava - otaviojava@java.net
 */
public class PersonDAO implements CRUD<Person> {

    private static final long serialVersionUID = 1L;
    private Persistence persistence;//classe responsável por fazer todas as rotinas no banco   

    public PersonDAO() {
        persistence = EasyCassandraManager.getPersistence("javabahia", "localhost", 9160);
    }

    @Override
    public boolean insert(Person bean) {


        return persistence.insert(bean);

    }

    @Override
    public boolean remove(Person bean) {


        return persistence.delete(bean);
    }

     @Override
    public boolean removeFromRowKey(Object rowKey) {


        return persistence.deleteByKeyValue(rowKey, Person.class);

    }

    @Override
    public boolean update(Person bean) {


        return persistence.update(bean);

    }

    @Override
    public Person retrieve(Object id) {


        return (Person) persistence.findByKey(id, Person.class);


    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Person> listAll() {


        return persistence.findAll(Person.class);

    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Person> listByIndex(Object index) {

        return persistence.findByIndex(index, Person.class);

    }
}
