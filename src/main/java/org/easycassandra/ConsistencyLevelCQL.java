/*
 * Copyright 2012 Otávio Gonçalves de Santana (otaviojava)
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.easycassandra;

import org.apache.cassandra.thrift.ConsistencyLevel;

/**
 * The ConsistencyLevel is an enum that controls both read and write behavior
 * based on <ReplicationFactor> in your schema definition. The different
 * consistency levels have different meanings, depending on if you're doing a
 * write or read operation. Note that if W + R > ReplicationFactor, where W is
 * the number of nodes to block for on write, and R the number to block for on
 * reads, you will have strongly consistent behavior; that is, readers will
 * always see the most recent write. Of these, the most interesting is to do
 * QUORUM reads and writes, which gives you consistency while still allowing
 * availability in the face of node failures up to half of ReplicationFactor. Of
 * course if latency is more important than consistency then you can use lower
 * values for either or both. All discussion of "nodes" here refers to nodes
 * responsible for holding data for the given key; "surrogate" nodes involved in
 * HintedHandoff do not count towards achieving the requested ConsistencyLevel.
 *
 * @author otavio
 */
public enum ConsistencyLevelCQL {

    ZERO("CONSISTENCY ZERO"),
    /**
     * Read Will return the record returned by the first replica to respond. A
     * consistency check is always done in a background thread to fix any
     * consistency issues when ConsistencyLevel.ONE is used. This means
     * subsequent calls will have correct data even if the initial read gets an
     * older value. (This is called ReadRepair)
     *
     * Write Ensure that the write has been written to at least 1 replica's
     * commit log and memory table before responding to the client.
     */
    ONE("CONSISTENCY ONE"),
    /**
     * Read Will query all replicas and return the record with the most recent
     * timestamp once it has at least a majority of replicas (N / 2 + 1)
     * reported. Again, the remaining replicas will be checked in the
     * background.
     *
     * Write Ensure that the write has been written to N / 2 + 1 replicas before
     * responding to the client.
     */
    QUORUM("CONSISTENCY QUORUM"),
    /**
     * Read Will query all replicas and return the record with the most recent
     * timestamp once all replicas have replied. Any unresponsive replicas will
     * fail the operation.
     *
     * Write Ensure that the write is written to all N replicas before
     * responding to the client. Any unresponsive replicas will fail the
     * operation.
     */
    ALL("CONSISTENCY ALL"),
    /**
     * Read Returns the record with the most recent timestamp once a majority of
     * replicas within the local datacenter have replied. Write Ensure that the
     * write has been written to <ReplicationFactor> / 2 + 1 nodes, within the
     * local datacenter (requires NetworkTopologyStrategy)
     */
    DCQUORUM("CONSISTENCY DCQUORUM"),
    /**
     * Read Returns the record with the most recent timestamp once a majority of
     * replicas within each datacenter have replied.
     *
     * Write Ensure that the write has been written to <ReplicationFactor> / 2 +
     * 1 nodes in each datacenter (requires NetworkTopologyStrategy)
     */
    DCQUORUMSYNC("CONSISTENCY DCQUORUMSYNC");
    /**
     * Value for use in CQL
     */
    private String value;

    /**
     * init method
     *
     * @param value
     */
    ConsistencyLevelCQL(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * return ConsistencyLevel for Thrift
     *
     * @return the ConsistencyLevel of Thrift
     */
    public ConsistencyLevel toConsistencyLevel() {
        switch (this) {
            case ALL:
                return ConsistencyLevel.ALL;
            case QUORUM:
                return ConsistencyLevel.QUORUM;
            case DCQUORUM:
                return ConsistencyLevel.LOCAL_QUORUM;
            case DCQUORUMSYNC:
                return ConsistencyLevel.EACH_QUORUM;
            default:
                return ConsistencyLevel.ONE;
        }

    }
}
