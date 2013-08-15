package org.easycassandra.persistence.cassandra;

import java.util.logging.Logger;

import javax.persistence.Entity;

import org.easycassandra.Constants;
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
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class EasyCassandraManagerTest {


    private static EasyCassandraManager easyCassandraManager=new EasyCassandraManager(Constants.HOST,Constants.KEY_SPACE_SIMPLE_TEST);
    @BeforeClass
	public static void beforeClass(){
    	easyCassandraManager.getPersistence(Constants.HOST, Constants.KEY_SPACE_SIMPLE_TEST,ReplicaStrategy.SIMPLES_TRATEGY,3);
	}

    @Test
    public void getPersistenceTest() {
        Assert.assertNotNull(easyCassandraManager.getPersistence(Constants.HOST, "javabahia"));
    }
    @Test
    public void getCreateSimpleTest() {
        Assert.assertNotNull(easyCassandraManager.getPersistence(Constants.HOST, Constants.KEY_SPACE_SIMPLE_TEST,ReplicaStrategy.SIMPLES_TRATEGY,3));
    }
  
    
    @Test
    public void getCreateNetworkTopologyTest() {
    	Assert.assertNotNull(easyCassandraManager.getPersistence(Constants.HOST, "simpleTest2",ReplicaStrategy.SIMPLES_TRATEGY,3));
    }


    @Test
    public void numberOfClientsTest(){
    	
    }
  
    @Test
    public void addColumnFamilyTest() {
    	easyCassandraManager.getPersistence(Constants.HOST, Constants.KEY_SPACE_SIMPLE_TEST,ReplicaStrategy.SIMPLES_TRATEGY,3);
        Assert.assertTrue(easyCassandraManager.addFamilyObject(SimpleBean.class,"javabahia"));
    }
    
    public void runRemove(String columnFamily){
        try{
           Persistence persistence= easyCassandraManager.getPersistence(Constants.HOST, Constants.KEY_SPACE_SIMPLE_TEST,ReplicaStrategy.SIMPLES_TRATEGY,3);
           persistence.executeUpdate("DROP TABLE ".concat(columnFamily).concat(" ;"));
        }catch(Exception exception){
            Logger.getLogger(EasyCassandraManagerTest.class.getName()).info("Column not exist: ".concat(exception.getMessage()));
        }
    }
    @Test(expected=FieldJavaNotEquivalentCQLException.class)
    public void addColumnFamilyErrorTest() {
    	runRemove("SimpleBeanWrong");
    	Persistence persistence=easyCassandraManager.getPersistence(Constants.HOST, Constants.KEY_SPACE_SIMPLE_TEST,ReplicaStrategy.SIMPLES_TRATEGY,3);
    	persistence.executeUpdate("create table SimpleBeanWrong( id bigint, name ascii, born int,  PRIMARY KEY (id) )");
        Assert.assertTrue(easyCassandraManager.addFamilyObject(SimpleBeanWrong.class,Constants.KEY_SPACE_SIMPLE_TEST));
    }
    
    
    @Test(expected=KeyProblemsException.class)
    public void addColumnFamilyKeyMandatoryTest() {
        Assert.assertTrue(easyCassandraManager.addFamilyObject(BeanWrong.class,Constants.KEY_SPACE_SIMPLE_TEST));
    }
    
    @Test
    public void addColumnFamilyAlterTableTest() {
        runRemove("SimpleBeanAlterTable");
    	Persistence persistence=easyCassandraManager.getPersistence(Constants.HOST, Constants.KEY_SPACE_SIMPLE_TEST,ReplicaStrategy.SIMPLES_TRATEGY,3);
    	persistence.executeUpdate("create table SimpleBeanAlterTable( id bigint, name ascii, born int,  PRIMARY KEY (id) )");
        Assert.assertTrue(easyCassandraManager.addFamilyObject(SimpleBeanAlterTable.class,Constants.KEY_SPACE_SIMPLE_TEST));
    }
    
    @Test
    public void addColumnFamilyComplexTest() {
        Assert.assertTrue(easyCassandraManager.addFamilyObject(SimpleComplexBean.class,Constants.KEY_SPACE_SIMPLE_TEST));
    }
    
    @Test
    public void addColumnFamilyComplexIDTest() {
    	
        Assert.assertTrue(easyCassandraManager.addFamilyObject(SimpleBeanComplexId.class,Constants.KEY_SPACE_SIMPLE_TEST));
    }
    
    @Test
    public void addColumnFamilySuperTest() {
    	
        Assert.assertTrue(easyCassandraManager.addFamilyObject(SimpleBeanSubClass.class,Constants.KEY_SPACE_SIMPLE_TEST));
    }
    
    @Test
    public void addColumnFamilyEnumTest() {
    	
        Assert.assertTrue(easyCassandraManager.addFamilyObject(SimpleBeanEnum.class,Constants.KEY_SPACE_SIMPLE_TEST));
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
