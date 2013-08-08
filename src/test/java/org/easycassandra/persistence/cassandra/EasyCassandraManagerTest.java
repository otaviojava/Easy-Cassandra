package org.easycassandra.persistence.cassandra;

import java.util.logging.Logger;

import javax.persistence.Entity;

import org.easycassandra.FieldJavaNotEquivalentCQLException;
import org.easycassandra.KeyProblemsException;
import org.easycassandra.ReplicaStrategy;
import org.easycassandra.bean.model.createtable.SimpleBean;
import org.easycassandra.bean.model.createtable.SimpleBeanAlterTable;
import org.easycassandra.bean.model.createtable.SimpleBeanComplexId;
import org.easycassandra.bean.model.createtable.SimpleBeanEnum;
import org.easycassandra.bean.model.createtable.SimpleBeanSubClass;
import org.easycassandra.bean.model.createtable.SimpleBeanWrong;
import org.easycassandra.bean.model.createtable.SimpleComplexBean;
import org.easycassandra.persistence.Persistence;
import org.easycassandra.persistence.cassandra.EasyCassandraManager;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class EasyCassandraManagerTest {

    private static final String COLUMN_FAMILY = "simpleTest";
    private static final String HOST_TEST = "localhost";
    private static EasyCassandraManager easyCassandraManager=new EasyCassandraManager();
    @BeforeClass
	public static void beforeClass(){
    	easyCassandraManager.getPersistence(HOST_TEST, COLUMN_FAMILY,ReplicaStrategy.SIMPLES_TRATEGY,3);
	}

    @Test
    public void getPersistenceTest() {
        Assert.assertNotNull(easyCassandraManager.getPersistence(HOST_TEST, "javabahia"));
    }
    @Test
    public void getCreateSimpleTest() {
        Assert.assertNotNull(easyCassandraManager.getPersistence(HOST_TEST, COLUMN_FAMILY,ReplicaStrategy.SIMPLES_TRATEGY,3));
    }
  
    
    @Test
    public void getCreateNetworkTopologyTest() {
    	Assert.assertNotNull(easyCassandraManager.getPersistence(HOST_TEST, "simpleTest2",ReplicaStrategy.SIMPLES_TRATEGY,3));
    }


    @Test
    public void numberOfClientsTest(){
    	
    }
  
    @Test
    public void addColumnFamilyTest() {
    	easyCassandraManager.getPersistence(HOST_TEST, COLUMN_FAMILY,ReplicaStrategy.SIMPLES_TRATEGY,3);
        Assert.assertTrue(easyCassandraManager.addFamilyObject(SimpleBean.class,"javabahia"));
    }
    
    public void runRemove(String columnFamily){
        try{
           Persistence persistence= easyCassandraManager.getPersistence(HOST_TEST, COLUMN_FAMILY,ReplicaStrategy.SIMPLES_TRATEGY,3);
           persistence.executeUpdate("DROP TABLE ".concat(columnFamily).concat(" ;"));
        }catch(Exception exception){
            Logger.getLogger(EasyCassandraManagerTest.class.getName()).info("Column not exist: ".concat(exception.getMessage()));
        }
    }
    @Test(expected=FieldJavaNotEquivalentCQLException.class)
    public void addColumnFamilyErrorTest() {
    	runRemove("SimpleBeanWrong");
    	Persistence persistence=easyCassandraManager.getPersistence(HOST_TEST, COLUMN_FAMILY,ReplicaStrategy.SIMPLES_TRATEGY,3);
    	persistence.executeUpdate("create table SimpleBeanWrong( id bigint, name ascii, born int,  PRIMARY KEY (id) )");
        Assert.assertTrue(easyCassandraManager.addFamilyObject(SimpleBeanWrong.class,COLUMN_FAMILY));
    }
    
    
    @Test(expected=KeyProblemsException.class)
    public void addColumnFamilyKeyMandatoryTest() {
        Assert.assertTrue(easyCassandraManager.addFamilyObject(BeanWrong.class,COLUMN_FAMILY));
    }
    
    @Test
    public void addColumnFamilyAlterTableTest() {
        runRemove("SimpleBeanAlterTable");
    	Persistence persistence=easyCassandraManager.getPersistence(HOST_TEST, COLUMN_FAMILY,ReplicaStrategy.SIMPLES_TRATEGY,3);
    	persistence.executeUpdate("create table SimpleBeanAlterTable( id bigint, name ascii, born int,  PRIMARY KEY (id) )");
        Assert.assertTrue(easyCassandraManager.addFamilyObject(SimpleBeanAlterTable.class,COLUMN_FAMILY));
    }
    
    @Test
    public void addColumnFamilyComplexTest() {
        Assert.assertTrue(easyCassandraManager.addFamilyObject(SimpleComplexBean.class,COLUMN_FAMILY));
    }
    
    @Test
    public void addColumnFamilyComplexIDTest() {
    	
        Assert.assertTrue(easyCassandraManager.addFamilyObject(SimpleBeanComplexId.class,COLUMN_FAMILY));
    }
    
    @Test
    public void addColumnFamilySuperTest() {
    	
        Assert.assertTrue(easyCassandraManager.addFamilyObject(SimpleBeanSubClass.class,COLUMN_FAMILY));
    }
    
    @Test
    public void addColumnFamilyEnumTest() {
    	
        Assert.assertTrue(easyCassandraManager.addFamilyObject(SimpleBeanEnum.class,COLUMN_FAMILY));
    }
    
    @Entity(name="wrong")
    public class BeanWrong{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        
    }
}
