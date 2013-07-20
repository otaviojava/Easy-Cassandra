package org.easycassandra.bean;

import junit.framework.Assert;

import org.easycassandra.bean.dao.PersistenceDao;
import org.easycassandra.bean.model.LinuxDistribuition;
import org.easycassandra.bean.model.createtable.IdLinux;
import org.junit.Test;

public class LinuxDAOTest {

    private PersistenceDao<LinuxDistribuition> linuxDAO=new PersistenceDao<LinuxDistribuition>(LinuxDistribuition.class);
    
    @Test
    public void insertTest() {
        LinuxDistribuition linux = getUbuntu();
        Assert.assertTrue(linuxDAO.insert(linux));
    }


    @Test
    public void removeTest() {
        LinuxDistribuition ekaaty=getEkaaty();
        linuxDAO.insert(ekaaty);
        linuxDAO.remove(ekaaty);
        Assert.assertNull(linuxDAO.retrieve(ekaaty.getId()));
    }
    
    
    
    @Test
    public void retrieveTest() {
        LinuxDistribuition ubuntu=getUbuntu();
        LinuxDistribuition linux = linuxDAO.retrieve(ubuntu.getId());
        Assert.assertTrue(linux.getVersion().equals(ubuntu.getVersion()));
    }
    
    private LinuxDistribuition getUbuntu() {
        LinuxDistribuition linux=new LinuxDistribuition();
        linux.setId(new IdLinux());
        linux.getId().setKernelVersion("3.5");
        linux.getId().setName("Ubuntu");
        linux.setGuy("Unity");
        linux.setDescriptions("The most popular linux distributions around world");
        linux.setVersion("12.04");
        return linux;
    }
    
    private LinuxDistribuition getEkaaty() {
        LinuxDistribuition linux=new LinuxDistribuition();
        linux.setId(new IdLinux());
        linux.getId().setKernelVersion("3.5");
        linux.getId().setName("Ekaaty");
        linux.setGuy("KDE");
        linux.setDescriptions("The brazilian distribution, made by Brazilians by Brazilians and for Brazilians");
        linux.setVersion("5 Pataxós");
        return linux;
    }
}
