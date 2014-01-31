package org.easycassandra.bean;

import junit.framework.Assert;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.ProductShopping;
import org.easycassandra.bean.model.Products;
import org.junit.Test;
/**
 * ProcutShoppingDAO.
 * @author otaviojava
 */
public class ProcutShoppingDAO {
    private static final double VALUE = 1000D;
    private PersistenceDao<ProductShopping, String> dao =
            new PersistenceDao<ProductShopping, String>(
            ProductShopping.class);
    /**
     * run the test.
     */
    @Test
    public void insertTest() {
        ProductShopping shopping = new ProductShopping();
        shopping.setName("notebook");
        Products products = new Products();
        products.setNome("Dell Ubuntu");
        products.setCountry("Brazil");
        products.setValue(VALUE);
        shopping.setProducts(products);
        Assert.assertTrue(dao.insert(shopping));

    }
    /**
     * run the test.
     */
    @Test
    public void retrieveTest() {
        ProductShopping shopping = dao.retrieve("notebook");
        Assert.assertNotNull(shopping);

    }
}
