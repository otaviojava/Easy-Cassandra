package org.easycassandra.persistence;

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
    public void getPersistClientEqualsTest(){
    	Assert.assertEquals(EasyCassandraManager.getPersistence("javabahia", "localhost", 9160).getClient(), EasyCassandraManager.getPersistence("javabahia", "localhost", 9160).getClient());
    }
    
    @Test
    public void getCreateNetworkTopologyTest() {
        Assert.assertNotNull(EasyCassandraManager.getClient("networkTest", "localhost", 9160,ReplicaStrategy.NetworkTopologyStrategy,3));
    }


    public void numberOfClientsTest(){
    	EasyCassandraManager.getPersistence("javabahia", "localhost", 9160);
    	EasyCassandraManager.getClient("javabahia", "localhost", 9160);
    	Assert.assertEquals(1, EasyCassandraManager.numberOfClients());
    }
    
    @Test
    public void getPersistenceCreateAumaticlyTest() {
        Assert.assertNotNull(EasyCassandraManager.getPersistence("unknow", "localhost", 9160));
    }
    
//    @Test(expected=Exception.class)
//    public void getPersistenceFailHostTest() {
//        Assert.assertNull(EasyCassandraManager.getPersistence("unknow", "unknow", 9160));
//    }

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
