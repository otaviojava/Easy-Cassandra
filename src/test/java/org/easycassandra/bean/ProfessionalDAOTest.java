package org.easycassandra.bean;

import junit.framework.Assert;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.Professional;
import org.junit.Test;
/**
 * ProfessionalDAOTest.
 * @author otaviojava
 */
public class ProfessionalDAOTest {

    private static final double SALARY = 123D;
    private PersistenceDao<Professional, String> persistence =
            new PersistenceDao<Professional, String>(
            Professional.class);
    /**
     * run the test.
     */
    @Test
    public void insertTest() {
        Professional professional = getProfessional();
        Assert.assertTrue(persistence.insert(professional));
    }

    private Professional getProfessional() {
        Professional professional = new Professional();
        professional.setName("Programmer");
        professional.setSalary(SALARY);
        return professional;
    }
    /**
     * run the test.
     */
    @Test
    public void retrieveTest() {
        Professional professional = persistence.retrieve(getProfessional()
                .getName());
        Assert.assertNotNull(professional);
        Assert.assertNull(professional.getSalaryYear());

    }

}
