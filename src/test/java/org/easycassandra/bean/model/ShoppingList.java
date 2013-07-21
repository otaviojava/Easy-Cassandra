package org.easycassandra.bean.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.easycassandra.annotations.ListData;

@Entity(name="shopping")
public class ShoppingList {

    @Id
    private String name;
    
    @Column(name="day")
    private Date day;
    
    @ListData(classData=String.class)
    @Column(name="frutis")
    private List<String> fruits;
    
    @Column(name="storeName")
    private String storeName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public List<String> getFruits() {
        return fruits;
    }

    public void setFruits(List<String> fruits) {
        this.fruits = fruits;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    
    
    
    
    
    
}
