package org.easycassandra.bean;

import junit.framework.Assert;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.Professional;
import org.junit.Test;

public class ProfessionalDAOTest {

private PersistenceDao<Professional,String> persistence=new PersistenceDao<Professional,String>(Professional.class);
    
    
        @Test
        public void insertTest() {
        Professional professional = getProfessional();
        Assert.assertTrue(persistence.insert(professional));
        }


    private Professional getProfessional() {
        Professional professional=new Professional();
        professional.setName("Programmer");
        professional.setSalary(123d);
        return professional;
    }
    
    
    @Test
    public void retrieveTest() {
        Professional professional=persistence.retrieve(getProfessional().getName());
        Assert.assertNotNull(professional);
        Assert.assertNull(professional.getSalaryYear());
     
    }
   
    
}
