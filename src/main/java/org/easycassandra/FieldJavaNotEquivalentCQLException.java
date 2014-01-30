package org.easycassandra;

/**
 * Exception that indicates conditions when the object exist, but its type in
 * java isn't equivalent with CQL type.
 * @author otaviojava
 */
public class FieldJavaNotEquivalentCQLException extends EasyCassandraException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor to FieldJavaNotEquivalentCQLException.
     * @param message information to log
     */
    public FieldJavaNotEquivalentCQLException(String message) {
        super(message);

    }

}
