package org.easycassandra.bean;

import junit.framework.Assert;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.ProductShopping;
import org.easycassandra.bean.model.Products;
import org.junit.Test;

public class ProcutShoppingDAO {
    private PersistenceDao<ProductShopping,String> dao = new PersistenceDao<ProductShopping,String>(ProductShopping.class);
    
    @Test
    public void insertTest(){
        ProductShopping shopping=new ProductShopping();
        shopping.setName("notebook");
        Products products=new Products();
        products.setNome("Dell Ubuntu");
        products.setCountry("Brazil");
        products.setValue(1000d);
        shopping.setProducts(products);
        Assert.assertTrue(dao.insert(shopping));
        
    }
    
    @Test
    public void retrieveTest(){
        ProductShopping shopping=dao.retrieve("notebook");
        Assert.assertNotNull(shopping);
        
    }
}
