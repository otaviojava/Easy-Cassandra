package org.easycassandra.bean;

import java.util.Random;

import junit.framework.Assert;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.Picture;
import org.easycassandra.bean.model.Picture.Details;
import org.junit.Test;

public class PictureDAOTest {

    private PersistenceDao<Picture,String> dao = new PersistenceDao<Picture,String>(Picture.class);
    
    
    @Test
    public void insertTest(){
        Picture picture=new Picture();
        picture.setDetail(new Details());
        picture.setName("mypicture");
        picture.getDetail().setFileName("otavio.png");
        byte[] file=new byte[200];
        new Random().nextBytes(file);
        picture.getDetail().setContents(file);
        Assert.assertTrue(dao.insert(picture));
        
    }
    
    @Test
    public void retriveTest(){
        Picture picture=dao.retrieve("mypicture");
        Assert.assertNotNull(picture);
    }
}
