package org.easycassandra.bean;

import java.util.List;


import org.easycassandra.bean.Person;
import org.easycassandra.persistence.EasyCassandraManager;
import org.easycassandra.persistence.Persistence;

/**
*
* @author otaviojava - otaviojava@java.net
*/
public class PersonDAO{

    private Persistence persistence;//classe responsável por fazer todas as rotinas no banco

    public PersonDAO() {
        persistence = EasyCassandraManager.getPersistence("javabahia", "localhost", 9160);
    }

    public boolean insert(Person bean) {


        return persistence.insert(bean);

    }

    public boolean remove(Person bean) {


        return persistence.delete(bean);
    }

    public boolean removeFromRowKey(Object rowKey) {


        return persistence.deleteByKeyValue(rowKey, Person.class);

    }

    public boolean update(Person bean) {


        return persistence.update(bean);

    }

    public Person retrieve(Object id) {


        return (Person) persistence.findByKey(id, Person.class);


    }

    @SuppressWarnings("unchecked")
    public List<Person> listAll() {


        return persistence.findAll(Person.class);

    }

    @SuppressWarnings("unchecked")
    public List<Person> listByIndex(Object index) {

        return persistence.findByIndex(index, Person.class);

    }
}