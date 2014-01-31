package org.easycassandra.bean;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.Book;
import org.junit.Test;
/**
 * the book test.
 */
public class BookDAOTest {

    private PersistenceDao<Book, String> persistence = new PersistenceDao<Book, String>(
            Book.class);
    /**
     * run the test.
     */
    @Test
    public void insertTest() {
        Book book = getBook();
        Assert.assertTrue(persistence.insert(book));
    }

    private Book getBook() {
        Book book = new Book();
        book.setName("Cassandra Guide ");
        Map<Long, String> resumeChapter = new HashMap<Long, String>();
        resumeChapter
                .put(1L,
                        "this chapter describes new resources in "
                        + "cassandra and improvements with CQL");
        resumeChapter.put(2L, "Understanding the architecture");
        resumeChapter.put(3L, "Installing DataStax Community");
        resumeChapter.put(4L, "Upgrading Cassandra");
        resumeChapter.put(5L, "Initializing a cluster");
        resumeChapter.put(6L, "Security");
        resumeChapter.put(7L, "Database design");
        resumeChapter.put(8L, "Using the database");
        resumeChapter.put(9L, "Database internals");
        resumeChapter.put(10L, "Configuration");
        resumeChapter.put(11L, "Operations");
        resumeChapter.put(12L, "Backing up and restoring data");
        resumeChapter.put(13L, "Cassandra tools");
        resumeChapter.put(14L, "Troubleshooting");
        resumeChapter.put(14L, "Troubleshooting");
        resumeChapter.put(15L, "References");
        book.setChapterResume(resumeChapter);
        return book;
    }
    /**
     * run the test.
     */
    @Test
    public void retrieveTest() {
        Book book = persistence.retrieve(getBook().getName());
        Assert.assertNotNull(book);

    }
    /**
     * run the test.
     */
    @Test
    public void removeTest() {
        persistence.remove(getBook());
        Assert.assertNull(persistence.retrieve(getBook().getName()));
        persistence.insert(getBook());
    }

}
