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
		EasyCassandraManager.INSTANCE.getPersistence("localhost", "simpleTest",ReplicaStrategy.SIMPLES_TRATEGY,3);
	}

    @Test
    public void getPersistenceTest() {
        Assert.assertNotNull(EasyCassandraManager.INSTANCE.getPersistence("localhost", "javabahia"));
    }
    @Test
    public void getCreateSimpleTest() {
        Assert.assertNotNull(EasyCassandraManager.INSTANCE.getPersistence("localhost", "simpleTest",ReplicaStrategy.SIMPLES_TRATEGY,3));
    }
  
    
    @Test
    public void getCreateNetworkTopologyTest() {
    	Assert.assertNotNull(EasyCassandraManager.INSTANCE.getPersistence("localhost", "simpleTest2",ReplicaStrategy.SIMPLES_TRATEGY,3));
    }


    @Test
    public void numberOfClientsTest(){
    	
    }
  
    @Test
    public void addColumnFamilyTest() {
    	EasyCassandraManager.INSTANCE.getPersistence("localhost", "simpleTest",ReplicaStrategy.SIMPLES_TRATEGY,3);
        Assert.assertTrue(EasyCassandraManager.INSTANCE.addFamilyObject(SimpleBean.class,"javabahia"));
    }
    
    @Test(expected=FieldJavaNotEquivalentCQLException.class)
    public void addColumnFamilyErrorTest() {
    	
    	Persistence persistence=EasyCassandraManager.INSTANCE.getPersistence("localhost", "simpleTest",ReplicaStrategy.SIMPLES_TRATEGY,3);
//    	persistence.executeUpdate("create table SimpleBeanWrong( id bigint, name ascii, born int,  PRIMARY KEY (id) )");
        Assert.assertTrue(EasyCassandraManager.INSTANCE.addFamilyObject(SimpleBeanWrong.class,"simpleTest"));
    }
    
    @Test
    public void addColumnFamilyAlterTableTest() {
    	
    	Persistence persistence=EasyCassandraManager.INSTANCE.getPersistence("localhost", "simpleTest",ReplicaStrategy.SIMPLES_TRATEGY,3);
//    	persistence.executeUpdate("create table SimpleBeanAlterTable( id bigint, name ascii, born int,  PRIMARY KEY (id) )");
        Assert.assertTrue(EasyCassandraManager.INSTANCE.addFamilyObject(SimpleBeanAlterTable.class,"simpleTest"));
    }
    
    @Test
    public void addColumnFamilyComplexTest() {
    	
        Assert.assertTrue(EasyCassandraManager.INSTANCE.addFamilyObject(SimpleComplexBean.class,"simpleTest"));
    }
    
    @Test
    public void addColumnFamilyComplexIDTest() {
    	
        Assert.assertTrue(EasyCassandraManager.INSTANCE.addFamilyObject(SimpleBeanComplexId.class,"simpleTest"));
    }
    
    @Test
    public void addColumnFamilySuperTest() {
    	
        Assert.assertTrue(EasyCassandraManager.INSTANCE.addFamilyObject(SimpleBeanSubClass.class,"simpleTest"));
    }
    
    @Test
    public void addColumnFamilyEnumTest() {
    	
        Assert.assertTrue(EasyCassandraManager.INSTANCE.addFamilyObject(SimpleBeanEnum.class,"simpleTest"));
    }
    
}
