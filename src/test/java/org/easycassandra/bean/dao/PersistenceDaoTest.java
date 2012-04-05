package org.easycassandra.bean.dao;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.easycassandra.bean.model.Person;
import org.easycassandra.bean.model.Sex;
import org.easycassandra.persistence.EasyCassandraException;
import org.easycassandra.persistence.EasyCassandraManager;
import org.easycassandra.persistence.JCassandra;
import org.easycassandra.persistence.Persistence;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PersistenceDaoTest {
	
	 private Persistence persistence;
	
	 private PersistenceDao<Person> dao; 	
	 
	   @Test
	    public void createJCassandraTest(){
	    
	    Assert.assertNotNull(persistence.createJCassandra("select * from Person"));
	    }
	    
	    @Test(expected=EasyCassandraException.class)
	    public void createJCassandraFailTest(){
	    
	    Assert.assertNotNull(persistence.createJCassandra("select * from Otavio"));
	    }
	    
	    @Test
	    public void runCqlTest(){
	    	JCassandra jCassandra=persistence.createJCassandra("select * from Person");
	    	Assert.assertNotNull(jCassandra.getResultList());
	    }
	    
	    @Test
	    public void runCqlListAllTest(){
	    	 
	    	JCassandra jCassandra=persistence.createJCassandra("select * from Person");
	    	
	    	List<Person> persons=jCassandra.getResultList();
	    	Assert.assertEquals(persons.size(), dao.listAll().size());
	    }
	 
	    
	    @Test
	    public void runCqlfindByIdInclude(){
	    	 
	    	JCassandra jCassandra=persistence.createJCassandra("select * from Person where id = 31 ");
	    	
	    	List<Person> persons=jCassandra.getResultList();
	    	Assert.assertEquals(persons.get(0).getName(), dao.retrieve(1l).getName());
	    }
	    
	    
	    @Test
	    public void runCqlSomeFieldsTest(){
	    	 
	    	JCassandra jCassandra=persistence.createJCassandra("select name, id, year from Person");
	    	
	    	
	    	Assert.assertNotNull(jCassandra.getResultList());
	    }
	    
	    @Test
	    public void runCqlSomeFields2Test(){
	    	 
	    	JCassandra jCassandra=persistence.createJCassandra("select name, id, year from Person");
	    	
	    	List list=jCassandra.getResultList();
	    	Assert.assertNotNull(list);
	    }
	    
	    @Test
	    public void runCqlEqualsFieldTest(){
	    	 
	    	JCassandra jCassandra=persistence.createJCassandra("select personalFile, sex, id, year from Person");
	    	
	    	List<Map<String, Object>> list=jCassandra.getResultList();
	    	Assert.assertTrue(list.get(3).get("personalFile") instanceof File);
	    }
	    
	    @Test
	    public void runCqlEqualsFieldEnumTest(){
	    	 
	    	JCassandra jCassandra=persistence.createJCassandra("select personalFile, sex, id, year from Person");
	    	
	    	List<Map<String, Object>> list=jCassandra.getResultList();
	    	Sex sex=(Sex) list.get(3).get("sex"); 
	    	Assert.assertEquals(Sex.MALE, sex);
	    }
	    
	    @Test
	    public void getSingleResultTest(){
	    	 
	    	JCassandra jCassandra=persistence.createJCassandra("select * from Person where id = 31 ");
	     	Person person=(Person)jCassandra.getSingleResult();
	    	Assert.assertEquals(person.getId(),Long.valueOf(1l));
	    }
	    
	    @Test
	    public void setFirstResultTest(){
	    	JCassandra jCassandra=persistence.createJCassandra("select * from Person ");
	    	jCassandra.setFirstResult(2);
	    	Person person=(Person)jCassandra.getResultList().get(0);
	    	Assert.assertEquals(person.getId(), dao.listAll().get(2).getId());
	    	
	    }
	    @Test(expected=EasyCassandraException.class)
	    public void setFirstResultNegativeValueTest(){
	    	JCassandra jCassandra=persistence.createJCassandra("select * from Person ");
	    	jCassandra.setFirstResult(-1);
	    	Person person=(Person)jCassandra.getResultList().get(0);
	    	Assert.assertEquals(person.getId(), dao.listAll().get(2).getId());
	    }
	    
	    @Test
	    public void setMaxResultsTest(){
	    	JCassandra jCassandra=persistence.createJCassandra("select * from Person ");
	    	jCassandra.setMaxResults(2);
	    	
	    	Assert.assertEquals(jCassandra.getResultList().size(),2);
	    	
	    }
	    @Test(expected=EasyCassandraException.class)
	    public void setMaxResultsNegativeTest(){
	    	JCassandra jCassandra=persistence.createJCassandra("select * from Person ");
	    	jCassandra.setMaxResults(-1);
	    	
	    	Assert.assertEquals(jCassandra.getResultList().size(),2);
	    	
	    }
	    
	    @Test
	    public void countTest(){
	    	JCassandra jCassandra=persistence.createJCassandra("select count(*) from Person ");
	    	Assert.assertTrue(jCassandra.getResultList().get(0) instanceof Long);
	    }
	    @BeforeClass
	    public static void initStatic(){
	    	EasyCassandraManager.addFamilyObject(Person.class);	
	    }
	    
	    @Before
	    public void init(){
	    	persistence = EasyCassandraManager.getPersistence("javabahia", "localhost", 9160);
	    	dao = new PersistenceDao<>(Person.class);
	    }
}
