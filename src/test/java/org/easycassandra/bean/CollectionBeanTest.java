package org.easycassandra.bean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.CollectionBean;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test to collection using @ElementCollection 
 */
public class CollectionBeanTest {

	  private PersistenceDao<CollectionBean,String> dao = new PersistenceDao<>(CollectionBean.class);

	  private static final UUID UUID_ID = UUID.randomUUID();

	  	@Test
	    public void insertTest() {
	  		CollectionBean bean = new CollectionBean();
	  		bean.setId(UUID_ID);
	        bean.setList(Arrays.asList("Lion", "Iron", "Animal"));
	        bean.setSet(new HashSet<>(bean.getList()));
	        Map<String, String> map = new HashMap<>();
	        map.put("1", "one");
	        map.put("2", "two");
	        map.put("3", "three");
	        map.put("4", "four");
	        map.put("5", "five");
	        bean.setMap(map);

           Assert.assertTrue(dao.insert(bean));

	    }

	  	@Test
	  	public void listAllTest() {
	  		Assert.assertNotNull(dao.listAll());
	  	}
}