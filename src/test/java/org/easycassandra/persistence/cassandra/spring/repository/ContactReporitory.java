package org.easycassandra.persistence.cassandra.spring.repository;

import java.util.UUID;

import org.easycassandra.persistence.cassandra.spring.CassandraRepository;
import org.easycassandra.persistence.cassandra.spring.CassandraTemplate;
import org.easycassandra.persistence.cassandra.spring.entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * repository of Contact.
 */
@Repository("contactReporitory")
public class ContactReporitory extends CassandraRepository<Contact, UUID> {

	@Autowired
	private CassandraTemplate cassandraTemplate;

	@Override
	protected CassandraTemplate getCassandraTemplate() {
		return cassandraTemplate;
	}

}
