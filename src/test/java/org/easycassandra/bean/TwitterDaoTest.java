package org.easycassandra.bean;

import java.nio.file.Paths;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.Twitter;
import org.junit.Assert;
import org.junit.Test;

public class TwitterDaoTest {

	private PersistenceDao<Twitter> dao = new PersistenceDao<Twitter>(Twitter.class);	
	
	@Test
	public void insertTest(){
		Twitter twitter=new Twitter();
		twitter.setName("otaviojava");
		twitter.setTwitte("I use Easy-Cassandra");
		Assert.assertTrue(dao.insert(twitter));
	}
	
	@Test
	public void retriveTest(){
		Twitter twitter=dao.retrieve("otaviojava");
		
		Assert.assertEquals("I use Easy-Cassandra", twitter.getTwitte());
	}
	
	
	@Test
	public void insertPathTest(){
		Twitter twitter=new Twitter();
		twitter.setName("poliana");
		twitter.setTwitte("I use Easy-Cassandra");
		twitter.setPath(Paths.get("readme.txt"));
		Assert.assertTrue(dao.insert(twitter));
	}
	
	
	@Test
	public void retrivePathTest(){
		Twitter twitter=dao.retrieve("poliana");
		
		Assert.assertEquals(Paths.get("readme.txt").toFile().getAbsolutePath(), twitter.getPath().toFile().getAbsolutePath());
	}
}
