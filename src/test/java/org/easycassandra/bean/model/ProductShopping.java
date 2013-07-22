package org.easycassandra.bean.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.easycassandra.CustomData;

@Entity(name="product")
public class ProductShopping {

    @Id
    private String name;
    
    @CustomData(classCustmo=ProductsCustomData.class)
    private Products products;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }
    
    
}
