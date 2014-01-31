package org.easycassandra.bean.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.easycassandra.Index;
/**
 * log class.
 * @author otaviojava
 */
@Entity
public class Log implements Serializable {

    private static final long serialVersionUID = 3L;

    @Id
    private String uuid;

    @Index
    @Column(name = "user_uuid")
    private String userUUid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserUUid() {
        return userUUid;
    }

    public void setUserUUid(String userUUid) {
        this.userUUid = userUUid;
    }

}
