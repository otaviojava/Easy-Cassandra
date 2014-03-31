package org.easycassandra.persistence.cassandra;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.easycassandra.EasyCassandraException;
import org.easycassandra.ReplicaStrategy;

/**
 * util to create keyspace.
 * @author otaviojava
 *
 */
enum FixKeySpaceUtil {
    INSTANCE;


    public CreateKeySpace getCreate(ReplicaStrategy replicaStrategy) {
        switch (replicaStrategy) {
            case CUSTOM_STRATEGY:
                return new CustomKeySpace();
            case NETWORK_TOPOLOGY_STRATEGY:
                return new NetworkKeySpcae();
            case SIMPLES_TRATEGY:
            default:
                return new SimpleKeySpace();
        }
    }

    /**
     * interface to create a keySpace.
     * @author otaviojava
     */
    public interface CreateKeySpace {
        String createQuery(KeySpaceQueryInformation information);
    }

    /**
     * create keySpace on SimpleStrategy.
     * @author otaviojava
     */
    class SimpleKeySpace implements CreateKeySpace {
        private static final String CREATE_KEY_SPACE_CQL = "CREATE KEYSPACE IF NOT EXISTS "
                + ":keySpace WITH replication = {'class': :replication , 'replication_factor': "
                + ":factor};";

        @Override
        public String createQuery(KeySpaceQueryInformation information) {
            return CREATE_KEY_SPACE_CQL.replace(":keySpace", information.keySpace)
                    .replace(":replication", information.replicaStrategy.getValue())
                    .replace(":factor", String.valueOf(information.factor));
        }

    }
    /**
     * create keySpace on give a query customize.
     * @author otaviojava
     */
    class CustomKeySpace implements CreateKeySpace {

        private static final String BEGIN_QUERY = "CREATE KEYSPACE IF NOT EXISTS";
        private static final String ERRO_QUERY_MANDATORY = "When you define custom type to Create "
                + "keyspace you must inform the query.";
        private static final String ERRO_QUERY_BEGIN_MANDATORY = "On custom query you must use "
                + BEGIN_QUERY + " to create your query.";

        @Override
        public String createQuery(KeySpaceQueryInformation information) {
            if (information.customQuery == null
                    || information.customQuery.isEmpty()) {
                throw new CreateKeySpaceException(ERRO_QUERY_MANDATORY);
            }
            if (!findBeginQuery(information)) {
                throw new CreateKeySpaceException(ERRO_QUERY_BEGIN_MANDATORY);
            }
            return information.customQuery;
        }

        private boolean findBeginQuery(KeySpaceQueryInformation information) {
            return Pattern
                    .compile(Pattern.quote(BEGIN_QUERY),
                            Pattern.CASE_INSENSITIVE).matcher(information.customQuery)
                    .find();
        }
    }
    /**
     * Exception to create keySpace.
     * @author otaviojava
     */
    static class CreateKeySpaceException extends EasyCassandraException {

        private static final long serialVersionUID = 1L;

        public CreateKeySpaceException(String message) {
            super(message);
        }
    }
    /**
     * create keySpace on NetworkTopologyStrategy.
     * @author otaviojava
     */
    class NetworkKeySpcae implements CreateKeySpace {
        private static final String CREATE_KEY_SPACE_CQL = "CREATE KEYSPACE IF NOT EXISTS "
                + ":keySpace WITH replication = {'class': :replication :dc_factor };";
        @Override
        public String createQuery(KeySpaceQueryInformation information) {
            StringBuilder dataCenterQuery = new StringBuilder(" ");
            if (!information.getDataCenter().isEmpty()) {
                dataCenterQuery.append(", ");
                Map<String, Integer> dataCenter = information.getDataCenter();
                for (String key : dataCenter.keySet()) {
                    dataCenterQuery.append(getDataCenterQuery(dataCenter, key));
                }
            }
            dataCenterQuery.deleteCharAt(dataCenterQuery.length() - 1);
            return CREATE_KEY_SPACE_CQL
                    .replace(":keySpace", information.keySpace)
                    .replace(":replication",
                            information.replicaStrategy.getValue())
                    .replace(":dc_factor", dataCenterQuery.toString());
        }

        private String getDataCenterQuery(Map<String, Integer> dataCenter,
                String key) {
            Integer factor = dataCenter.get(key);
            String dataQuery = "'".concat(key).concat("' : ")
                    .concat(factor.toString()).concat(",");
            return dataQuery;
        }

    }

    /**
     * informations to create a keySpace.
     * @author otaviojava
     */
    public static class KeySpaceQueryInformation {
        private String keySpace;
        private ReplicaStrategy replicaStrategy;
        private int factor;
        private String customQuery;
        private Map<String, Integer> dataCenter;

        public String getKeySpace() {
            return keySpace;
        }
        public void setKeySpace(String keySpace) {
            this.keySpace = keySpace;
        }
        public ReplicaStrategy getReplicaStrategy() {
            return replicaStrategy;
        }
        public void setReplicaStrategy(ReplicaStrategy replicaStrategy) {
            this.replicaStrategy = replicaStrategy;
        }
        public int getFactor() {
            return factor;
        }
        public void setFactor(int factor) {
            this.factor = factor;
        }
        public String getCustomQuery() {
            return customQuery;
        }
        public void setCustomQuery(String customQuery) {
            this.customQuery = customQuery;
        }
        public Map<String, Integer> getDataCenter() {
            if (dataCenter == null) {
                dataCenter = new HashMap<>();
            }
            return dataCenter;
        }
        public void setDataCenter(Map<String, Integer> dataCenter) {
            this.dataCenter = dataCenter;
        }
    }
}
