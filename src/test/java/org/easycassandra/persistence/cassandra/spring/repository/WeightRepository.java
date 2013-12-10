package org.easycassandra.persistence.cassandra.spring.repository;

import java.util.Date;
import java.util.List;

import org.easycassandra.bean.model.IdLifestyle;
import org.easycassandra.bean.model.Weight;
import org.easycassandra.persistence.cassandra.spring.CassandraRepository;
import org.easycassandra.persistence.cassandra.spring.CassandraTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("weightRepository")
public class WeightRepository extends CassandraRepository<Weight, IdLifestyle> {

    @Autowired
    private CassandraTemplate cassandraTemplate;

    @Override
    public CassandraTemplate getCassandraTemplate() {
        return cassandraTemplate;
    }

    public List<Weight> findByKeyAndIndex(final IdLifestyle key, final Date date) {
        return cassandraTemplate.findByKeyAndIndex(key, date, Weight.class);
    }
}
