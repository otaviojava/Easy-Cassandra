package org.easycassandra.persistence.cassandra;

import java.util.Arrays;
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
import org.junit.Before;
import org.junit.Test;
/**
 * test to Cassandra manager.
 * @author otaviojava
 */
public class EasyCassandraManagerTest {


    private static final int THREE = 3;
    private static EasyCassandraManager easyCassandraManager;

    {
        ClusterInformation clusterInformation = new ClusterInformation();
        clusterInformation.setHosts(Arrays.asList(Constants.HOST));
        clusterInformation.setKeySpace(Constants.KEY_SPACE_SIMPLE_TEST);
        easyCassandraManager = new EasyCassandraManager(clusterInformation);
    }
    /**
     * run before the test.
     */
    @Before
	public  void beforeClass() {
        easyCassandraManager.getPersistence(Constants.HOST,
                Constants.KEY_SPACE_SIMPLE_TEST,
                ReplicaStrategy.SIMPLES_TRATEGY, THREE);
	}

    /**
     * run the test.
     */
    @Test
    public void getPersistenceTest() {
        Assert.assertNotNull(easyCassandraManager.getPersistence("javabahia"));
    }
    /**
     * run the test.
     */
    @Test
    public void getCreateSimpleTest() {
        Assert.assertNotNull(easyCassandraManager.getPersistence(
                Constants.HOST, Constants.KEY_SPACE_SIMPLE_TEST,
                ReplicaStrategy.SIMPLES_TRATEGY, THREE));
    }
    /**
     * run the test.
     */
    @Test
    public void getCreateNetworkTopologyTest() {
        Assert.assertNotNull(easyCassandraManager.getPersistence(
                Constants.HOST, "simpleTest2", ReplicaStrategy.SIMPLES_TRATEGY,
                THREE));
    }


    /**
     * run the test.
     */
    @Test
    public void addColumnFamilyTest() {
        easyCassandraManager.getPersistence(Constants.HOST,
                Constants.KEY_SPACE_SIMPLE_TEST,
                ReplicaStrategy.SIMPLES_TRATEGY, THREE);
        Assert.assertTrue(easyCassandraManager.addFamilyObject(
                SimpleBean.class, "javabahia"));
    }


    private void runRemove(String columnFamily) {
        try {
            Persistence persistence = easyCassandraManager.getPersistence(
                    Constants.HOST, Constants.KEY_SPACE_SIMPLE_TEST,
                    ReplicaStrategy.SIMPLES_TRATEGY, THREE);
            persistence.executeUpdate("DROP TABLE ".concat(
                    Constants.KEY_SPACE_SIMPLE_TEST.concat(".").concat(
                            columnFamily))
                    .concat(" ;"));
        } catch (Exception exception) {
            Logger.getLogger(EasyCassandraManagerTest.class.getName()).info(
                    "Column not exist: ".concat(exception.getMessage()));
        }
    }
    /**
     * run the test.
     */
    @Test(expected = FieldJavaNotEquivalentCQLException.class)
    public void addColumnFamilyErrorTest() {
        runRemove("SimpleBeanWrong");
        Persistence persistence = easyCassandraManager.getPersistence(
                Constants.HOST, Constants.KEY_SPACE_SIMPLE_TEST,
                ReplicaStrategy.SIMPLES_TRATEGY, THREE);
        persistence.executeUpdate("create table "
                + Constants.KEY_SPACE_SIMPLE_TEST
                + ".SimpleBeanWrong( id bigint,"
                        + " name ascii, born int,  PRIMARY KEY (id) )");
        Assert.assertTrue(easyCassandraManager.addFamilyObject(
                SimpleBeanWrong.class, Constants.KEY_SPACE_SIMPLE_TEST));
    }
    /**
     * run the test.
     */
    @Test(expected = KeyProblemsException.class)
    public void addColumnFamilyKeyMandatoryTest() {
        Assert.assertTrue(easyCassandraManager.addFamilyObject(BeanWrong.class,
                Constants.KEY_SPACE_SIMPLE_TEST));
    }
    /**
     * run the test.
     */
    @Test
    public void addColumnFamilyAlterTableTest() {
        runRemove("SimpleBeanAlterTable");
        Persistence persistence = easyCassandraManager.getPersistence(
                Constants.HOST, Constants.KEY_SPACE_SIMPLE_TEST,
                ReplicaStrategy.SIMPLES_TRATEGY, THREE);
        persistence.executeUpdate("create table "
                + Constants.KEY_SPACE_SIMPLE_TEST
                + ".SimpleBeanAlterTable( id bigint, "
                + "name ascii, born int,  PRIMARY KEY (id) )");
        Assert.assertTrue(easyCassandraManager.addFamilyObject(
                SimpleBeanAlterTable.class, Constants.KEY_SPACE_SIMPLE_TEST));
    }
    /**
     * run the test.
     */
    @Test
    public void addColumnFamilyComplexTest() {
        Assert.assertTrue(easyCassandraManager.addFamilyObject(
                SimpleComplexBean.class, Constants.KEY_SPACE_SIMPLE_TEST));
    }
    /**
     * run the test.
     */
    @Test
    public void addColumnFamilyComplexIDTest() {

        Assert.assertTrue(easyCassandraManager.addFamilyObject(
                SimpleBeanComplexId.class, Constants.KEY_SPACE_SIMPLE_TEST));
    }
    /**
     * run the test.
     */
    @Test
    public void addColumnFamilySuperTest() {

        Assert.assertTrue(easyCassandraManager.addFamilyObject(
                SimpleBeanSubClass.class, Constants.KEY_SPACE_SIMPLE_TEST));
    }

    /**
     * run the test.
     */
    @Test
    public void addColumnFamilyEnumTest() {

        Assert.assertTrue(easyCassandraManager.addFamilyObject(
                SimpleBeanEnum.class, Constants.KEY_SPACE_SIMPLE_TEST));
    }

    /**
     * class to test.
     * @author otaviojava
     */
    @Entity(name = "wrong")
    public class BeanWrong {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
