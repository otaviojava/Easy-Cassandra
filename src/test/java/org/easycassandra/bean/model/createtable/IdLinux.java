package org.easycassandra.bean.model.createtable;

import javax.persistence.Column;
/**
 * the IdLinux class.
 * @author otaviojava
 */
public class IdLinux {

    @Column
    private String name;

    @Column
    private String kernelVersion;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKernelVersion() {
        return kernelVersion;
    }

    public void setKernelVersion(String kernelVersion) {
        this.kernelVersion = kernelVersion;
    }

}
