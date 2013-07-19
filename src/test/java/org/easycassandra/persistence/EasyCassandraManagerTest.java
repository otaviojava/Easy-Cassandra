package org.easycassandra.persistence;

import org.easycassandra.FieldJavaNotEquivalentCQLException;
import org.easycassandra.ReplicaStrategy;
import org.easycassandra.bean.model.createtable.SimpleBean;
import org.easycassandra.bean.model.createtable.SimpleBeanAlterTable;
import org.easycassandra.bean.model.createtable.SimpleBeanComplexId;
import org.easycassandra.bean.model.createtable.SimpleBeanEnum;
import org.easycassandra.bean.model.createtable.SimpleBeanSubClass;
import org.easycassandra.bean.model.createtable.SimpleBeanWrong;
import org.easycassandra.bean.model.createtable.SimpleComplexBean;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class EasyCassandraManagerTest {

    @BeforeClass
	public static void beforeClass(){
		EasyCassandraManager.getPersistence("localhost", "simpleTest",ReplicaStrategy.SimpleStrategy,3);
	}

    @Test
    public void getPersistenceTest() {
        Assert.assertNotNull(EasyCassandraManager.getPersistence("localhost", "javabahia"));
    }
    @Test
    public void getCreateSimpleTest() {
        Assert.assertNotNull(EasyCassandraManager.getPersistence("localhost", "simpleTest",ReplicaStrategy.SimpleStrategy,3));
    }
  
    
    @Test
    public void getCreateNetworkTopologyTest() {
    	Assert.assertNotNull(EasyCassandraManager.getPersistence("localhost", "simpleTest2",ReplicaStrategy.SimpleStrategy,3));
    }


    @Test
    public void numberOfClientsTest(){
    	
    }
  
    @Test
    public void addColumnFamilyTest() {
    	EasyCassandraManager.getPersistence("localhost", "simpleTest",ReplicaStrategy.SimpleStrategy,3);
        Assert.assertTrue(EasyCassandraManager.addFamilyObject(SimpleBean.class,"javabahia"));
    }
    
    @Test(expected=FieldJavaNotEquivalentCQLException.class)
    public void addColumnFamilyErrorTest() {
    	
    	Persistence persistence=EasyCassandraManager.getPersistence("localhost", "simpleTest",ReplicaStrategy.SimpleStrategy,3);
//    	persistence.executeUpdate("create table SimpleBeanWrong( id bigint, name ascii, born int,  PRIMARY KEY (id) )");
        Assert.assertTrue(EasyCassandraManager.addFamilyObject(SimpleBeanWrong.class,"simpleTest"));
    }
    
    @Test
    public void addColumnFamilyAlterTableTest() {
    	
    	Persistence persistence=EasyCassandraManager.getPersistence("localhost", "simpleTest",ReplicaStrategy.SimpleStrategy,3);
//    	persistence.executeUpdate("create table SimpleBeanAlterTable( id bigint, name ascii, born int,  PRIMARY KEY (id) )");
        Assert.assertTrue(EasyCassandraManager.addFamilyObject(SimpleBeanAlterTable.class,"simpleTest"));
    }
    
    @Test
    public void addColumnFamilyComplexTest() {
    	
        Assert.assertTrue(EasyCassandraManager.addFamilyObject(SimpleComplexBean.class,"simpleTest"));
    }
    
    @Test
    public void addColumnFamilyComplexIDTest() {
    	
        Assert.assertTrue(EasyCassandraManager.addFamilyObject(SimpleBeanComplexId.class,"simpleTest"));
    }
    
    @Test
    public void addColumnFamilySuperTest() {
    	
        Assert.assertTrue(EasyCassandraManager.addFamilyObject(SimpleBeanSubClass.class,"simpleTest"));
    }
    
    @Test
    public void addColumnFamilyEnumTest() {
    	
        Assert.assertTrue(EasyCassandraManager.addFamilyObject(SimpleBeanEnum.class,"simpleTest"));
    }
    
}
