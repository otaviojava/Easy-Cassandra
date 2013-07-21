package org.easycassandra.bean;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.Book;
import org.junit.Test;

public class BookDAOTest {

 private PersistenceDao<Book,String> persistence=new PersistenceDao<Book,String>(Book.class);
    
    
    @Test
        public void insertTest() {
        Book book = getBook();
        Assert.assertTrue(persistence.insert(book));
    }


    private Book getBook() {
        Book book = new Book();
        book.setName("Cassandra Guide ");
        Map<Long, String> resumeChapter=new HashMap<Long, String>();
        resumeChapter.put(1l, "this chapter describes new resources in cassandra and improvements with CQL");
        resumeChapter.put(2l, "Understanding the architecture");
        resumeChapter.put(3l, "Installing DataStax Community");
        resumeChapter.put(4l, "Upgrading Cassandra");
        resumeChapter.put(5l, "Initializing a cluster");
        resumeChapter.put(6l, "Security");
        resumeChapter.put(7l, "Database design");
        resumeChapter.put(8l, "Using the database");
        resumeChapter.put(9l, "Database internals");
        resumeChapter.put(10l, "Configuration");
        resumeChapter.put(11l, "Operations");
        resumeChapter.put(12l, "Backing up and restoring data");
        resumeChapter.put(13l, "Cassandra tools");
        resumeChapter.put(14l, "Troubleshooting");
        resumeChapter.put(14l, "Troubleshooting");
        resumeChapter.put(15l, "References");
        book.setChapterResume(resumeChapter);
        return book;
    }
    
    
    @Test
    public void retrieveTest() {
        Book book=persistence.retrieve(getBook().getName());
        Assert.assertNotNull(book);
     
    }
    
    @Test
    public void removeTest() {
        persistence.remove(getBook());
        Assert.assertNull(persistence.retrieve(getBook().getName()));
        persistence.insert(getBook());
    }
    
}
