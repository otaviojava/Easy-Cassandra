package org.easycassandra.bean;

import java.util.Random;

import junit.framework.Assert;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.Picture;
import org.easycassandra.bean.model.Picture.Details;
import org.junit.Test;
/**
 * the PictureDAO test.
 * @author otaviojava
 */
public class PictureDAOTest {

    private static final int SIZE = 200;
    private PersistenceDao<Picture, String> dao = new PersistenceDao<Picture, String>(
            Picture.class);
    /**
     * run the test.
     */
    @Test
    public void insertTest() {
        Picture picture = new Picture();
        picture.setDetail(new Details());
        picture.setName("mypicture");
        picture.getDetail().setFileName("otavio.png");
        byte[] file = new byte[SIZE];
        new Random().nextBytes(file);
        picture.getDetail().setContents(file);
        Assert.assertTrue(dao.insert(picture));

    }
    /**
     * run the test.
     */
    @Test
    public void retrieveTest() {
        Picture picture = dao.retrieve("mypicture");
        Assert.assertNotNull(picture);
    }
}
