package org.easycassandra;

import junit.framework.Assert;

import org.apache.cassandra.thrift.ConsistencyLevel;
import org.junit.Test;

public class ConsistencyLevelCQLTest {

	
	
	@Test
	public void toConsistencyLevel(){
		ConsistencyLevelCQL consistencyLevelCQL=ConsistencyLevelCQL.ALL;
		Assert.assertEquals(consistencyLevelCQL.toConsistencyLevel(), ConsistencyLevel.ALL);
	}
	
	@Test
	public void toConsistencyLevelQUORUM(){
		ConsistencyLevelCQL consistencyLevelCQL=ConsistencyLevelCQL.QUORUM;
		Assert.assertEquals(consistencyLevelCQL.toConsistencyLevel(), ConsistencyLevel.QUORUM);
	}
	
}
