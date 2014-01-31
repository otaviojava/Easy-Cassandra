package org.easycassandra.bean.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.easycassandra.CustomData;

/**
 * Picture object.
 * @author otaviojava
 */
@Entity(name = "picture")
public class Picture {

    @Id
    private String name;

    @Column(name = "details")
    @CustomData
    private Details detail;

    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }



    public Details getDetail() {
        return detail;
    }



    public void setDetail(Details detail) {
        this.detail = detail;
    }


    /**
     * details dto.
     * @author otaviojava
     */
    public static class Details implements Serializable {

        private static final long serialVersionUID = 1L;

        private String fileName;

        private byte[] contents;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public byte[] getContents() {
            return contents;
        }

        public void setContents(byte[] contents) {
            this.contents = contents;
        }

    }
}
