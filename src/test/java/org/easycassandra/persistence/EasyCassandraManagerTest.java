package org.easycassandra.persistence;

import org.easycassandra.EasyCassandraException;
import org.easycassandra.ReplicaStrategy;
import org.easycassandra.bean.model.Person;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class EasyCassandraManagerTest {

    @Test
    public void getClientTest() {
        Assert.assertNotNull(EasyCassandraManager.getClient("javabahia", "localhost", 9160));
    }

    @Test
    public void getPersistenceTest() {
        Assert.assertNotNull(EasyCassandraManager.getPersistence("javabahia", "localhost", 9160));
    }
    @Test
    public void getCreateSimpleTest() {
        Assert.assertNotNull(EasyCassandraManager.getClient("simpleTest", "localhost", 9160,ReplicaStrategy.SimpleStrategy,3));
    }
    @Test
    public void getCreateTwoSimpleTest() {
        Assert.assertNotNull(EasyCassandraManager.getClient("simpleTest3", "localhost", 9160,ReplicaStrategy.SimpleStrategy,3));
        Assert.assertNotNull(EasyCassandraManager.getClient("simpleTest2", "localhost", 9160,ReplicaStrategy.SimpleStrategy,3));
    }
    
    @Test
    public void getCreateNetworkTopologyTest() {
        Assert.assertNotNull(EasyCassandraManager.getClient("networkTest", "localhost", 9160,ReplicaStrategy.NetworkTopologyStrategy,3));
    }


    @Test
    public void numberOfClientsTest(){
    	EasyCassandraManager.closeClients();
    	EasyCassandraManager.getPersistence("javabahia", "localhost", 9160);
    	EasyCassandraManager.getClient("javabahia", "localhost", 9160);
    	Assert.assertEquals(1, EasyCassandraManager.numberOfClients());
    }
    
    @Test
    public void getPersistenceRandomTest(){
    	EasyCassandraManager.getPersistence("javabahia", "localhost", 9160);
    	Assert.assertNotNull(EasyCassandraManager.getPersistenceRandom("javabahia"));
    }
    @Test(expected=EasyCassandraException.class)
    public void getPersistenceRandomNumberTest(){
    	
    	EasyCassandraManager.closeClients();
    	Assert.assertNotNull(EasyCassandraManager.getPersistenceRandom("javabahia"));
    }
    @Test
    public void getPersistenceRandomInstanceTest(){
    	EasyCassandraManager.getPersistence("javabahia", "localhost", 9160);
    	Assert.assertTrue(EasyCassandraManager.getPersistenceRandom("javabahia") instanceof PersistenceRandom);
    }
    
    @Test
    public void getPersistenceSequencialTest(){
    	EasyCassandraManager.getPersistence("javabahia", "localhost", 9160);
    	Assert.assertNotNull(EasyCassandraManager.getPersistenceSequencial("javabahia"));
    }
    @Test(expected=EasyCassandraException.class)
    public void getPersistenceSequencialNumberTest(){
    	EasyCassandraManager.closeClients();
    	Assert.assertNotNull(EasyCassandraManager.getPersistenceSequencial("javabahia"));
    }
    @Test
    public void getPersistenceSequencialInstanceTest(){
    	EasyCassandraManager.getPersistence("javabahia", "localhost", 9160);
    	Assert.assertTrue(EasyCassandraManager.getPersistenceSequencial("javabahia") instanceof PersistenceSequencial);
    }

    
    
    @Test
    public void getPersistenceCreateAumaticlyTest() {
        Assert.assertNotNull(EasyCassandraManager.getPersistence("unknow", "localhost", 9160));
    }
    

    @Test
    public void addColumnFamilyTest() {

        Assert.assertTrue(EasyCassandraManager.addFamilyObject(Person.class));
    }

    @BeforeClass
    public static void initClass() {
        EasyCassandraManager.addFamilyObject(Person.class);
    }

    @Test
    public void getFamilyObjectTest() {
        Assert.assertNotNull(EasyCassandraManager.getFamily("Person"));
    }

    @Test
    public void getFamilyObjectSubFieldTest() {
        DescribeFamilyObject ao = EasyCassandraManager.getFamily("Person");
        Assert.assertEquals(ao.getFieldsName("cep"), "address.cep");
    }

    @Test
    public void getFamilyObjectSubClassTest() {
        DescribeFamilyObject ao = EasyCassandraManager.getFamily("Person");
        Assert.assertEquals(ao.getFieldsName("state , cyte , street , cep"), "address");
    }
}
