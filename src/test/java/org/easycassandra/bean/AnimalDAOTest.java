package org.easycassandra.bean;

import org.easycassandra.bean.dao.AnimalDAO;
import org.easycassandra.bean.model.Animal;
import org.junit.Assert;
import org.junit.Test;

/**
*
* @author otavio
*/
public class AnimalDAOTest {
	  private AnimalDAO dao = new AnimalDAO();
	  	@Test
	    public void insertErrorTest() {
	       Animal animal=new Animal();
	       animal.setName("Lion");
	       animal.setRace("I know no");
	       animal.setCountry("Brazil");
           Assert.assertTrue(dao.insert(animal));
	        
	     
	    }
	  	
	  	@Test
	    public void listErrorTest() {
	        Assert.assertNotNull(dao.listAll());
	        
	     
	    }
	  	@Test
	  	public void listIndexInitializeTest(){
	  		Assert.assertEquals(dao.listByIndex("Brazil").size(),1);
	  	}
	  
    
    
}