package org.easycassandra.bean.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.easycassandra.bean.model.createtable.IdLinux;

@Entity(name="linux")
public class LinuxDistribuition {

    @EmbeddedId
    private IdLinux id;
    
    @Column
    private String guy;
    
    @Column
    private String version;
    
    @Column(name="descriptions")
    private String descriptions;

    public IdLinux getId() {
        return id;
    }

    public void setId(IdLinux id) {
        this.id = id;
    }


    public String getGuy() {
        return guy;
    }

    public void setGuy(String guy) {
        this.guy = guy;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    
}
