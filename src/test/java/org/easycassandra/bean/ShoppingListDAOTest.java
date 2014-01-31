package org.easycassandra.bean;

import java.util.Date;
import java.util.LinkedList;

import junit.framework.Assert;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.ShoppingList;
import org.junit.Test;

/**
 * ShoppingListDAOTest.
 * @author otaviojava
 */
public class ShoppingListDAOTest {

    private PersistenceDao<ShoppingList, String> persistence =
            new PersistenceDao<ShoppingList, String>(
            ShoppingList.class);
    /**
     * run the test.
     */
    @Test
    public void insertTest() {

        Assert.assertTrue(persistence.insert(getPolianaShoppingList()));
    }

    private ShoppingList getPolianaShoppingList() {
        ShoppingList shopping = new ShoppingList();
        shopping.setDay(new Date());
        shopping.setName("Poliana");
        shopping.setFruits(new LinkedList<String>());
        shopping.getFruits().add("Orange");
        shopping.getFruits().add("beans");
        shopping.getFruits().add("rice");
        return shopping;
    }
    /**
     * run the test.
     */
    @Test
    public void retrieveTest() {
        ShoppingList list = persistence.retrieve(getPolianaShoppingList()
                .getName());
        Assert.assertNotNull(list);
    }
    /**
     * run the test.
     */
    @Test
    public void removeTest() {
        ShoppingList list = persistence.retrieve(getPolianaShoppingList()
                .getName());
        persistence.remove(list);
        list = persistence.retrieve(getPolianaShoppingList().getName());
        Assert.assertNull(list);
    }

}
