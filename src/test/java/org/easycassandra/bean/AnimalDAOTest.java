package org.easycassandra.bean;

import org.easycassandra.KeyProblemsException;
import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.Animal;
import org.junit.Assert;
import org.junit.Test;

/**
*
* @author otavio
*/
public class AnimalDAOTest {
	  private PersistenceDao<Animal,String> dao = new PersistenceDao<Animal,String>(Animal.class);
	  	@Test
	    public void insertErrorTest() {
	       Animal animal=new Animal();
	       animal.setName("Lion");
	       animal.setRace("I know no");
	       animal.setCountry("Brazil");
           Assert.assertTrue(dao.insert(animal));
	        
	     
	    }
	  	@Test(expected=KeyProblemsException.class)
	  	public void insertNullError(){
	  		Animal animal=new Animal();
		    animal.setRace("I know no");
		    animal.setCountry("Brazil");
	        Assert.assertFalse(dao.insert(animal));
	  	}
	  	@Test
	    public void listErrorTest() {
	        Assert.assertNotNull(dao.listAll());
	        
	     
	    }
	  	@Test
	  	public void listIndexInitializeTest(){
	  		Assert.assertTrue(dao.listByIndex("country","Brazil").size() >=1);
	  	}
	  
    
    
}