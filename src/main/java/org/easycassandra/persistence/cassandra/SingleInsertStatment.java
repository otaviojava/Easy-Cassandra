package org.easycassandra.persistence.cassandra;

import com.datastax.driver.core.RegularStatement;
/**
 * The statement to alterations on single mode (insert, update, delete).
 * @author otaviojava
 */
public interface SingleInsertStatment extends AlterationBuilder {
    /**
     *  A regular (non-prepared and non batched) CQL statement.
     * @return the statment
     */
    RegularStatement getStatement();
}
