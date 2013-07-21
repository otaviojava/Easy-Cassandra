package org.easycassandra.bean;

import java.util.Date;
import java.util.LinkedList;

import junit.framework.Assert;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.ShoppingList;
import org.junit.Test;

public class ShoppingListDAOTest {

    private PersistenceDao<ShoppingList,String> persistence=new PersistenceDao<ShoppingList,String>(ShoppingList.class);
    
    
    @Test
    public void insertTest() {
        
        Assert.assertTrue(persistence.insert(getPolianaShoppingList()));
    }
    private ShoppingList getPolianaShoppingList() {
        ShoppingList shopping=new ShoppingList();
        shopping.setDay(new Date());
        shopping.setName("Poliana");
        shopping.setFruits(new LinkedList<String>());
        shopping.getFruits().add("Orange");
        shopping.getFruits().add("beans");
        shopping.getFruits().add("rice");
        return shopping;
    }
    
    @Test
    public void retrieveTest() {
     ShoppingList list=persistence.retrieve(getPolianaShoppingList().getName());
     Assert.assertNotNull(list);
    }
    @Test
    public void removeTest() {
        ShoppingList list=persistence.retrieve(getPolianaShoppingList().getName());
        persistence.remove(list);
        list=persistence.retrieve(getPolianaShoppingList().getName());
        Assert.assertNull(list);
    }
    
    
    
}
