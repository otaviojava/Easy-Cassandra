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

/**
 *The ConsistencyLevel is an enum that controls both read 
 * and write behavior based on <ReplicationFactor> 
 * in your schema definition. The different consistency levels have different meanings,
 * depending on if you're doing a write or read operation. Note that if W + R > 
 * ReplicationFactor, where W is the number of nodes to block for on write, and R 
 * the number to block for on reads, you will have strongly consistent behavior;
 * that is, readers will always see the most recent write. Of these, the most 
 * interesting is to do QUORUM reads and writes, which gives you consistency 
 * while still allowing availability in the face of node failures up to half of 
 * ReplicationFactor. Of course if latency is more important than consistency 
 * then you can use lower values for either or both.
 * All discussion of "nodes" here refers to nodes responsible 
 * for holding data for the given key; "surrogate" nodes involved in
 * HintedHandoff do not count towards achieving the requested ConsistencyLevel.  
  * @author otavio
 */
public enum ConsistencyLevelCQL {

    ZERO("CONSISTENCY ZERO"),
    ONE("CONSISTENCY ONE"),
    QUORUM("CONSISTENCY QUORUM"),
    ALL("CONSISTENCY ALL"),
    DCQUORUM("CONSISTENCY DCQUORUM"),
    DCQUORUMSYNC("CONSISTENCY DCQUORUMSYNC");
    
    /**
     * Value for use in CQL
     */
    private String value;

    /**
     * init method
     * @param value 
     */
    ConsistencyLevelCQL(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
