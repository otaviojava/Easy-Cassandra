package org.easycassandra.bean.model.createtable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.easycassandra.Index;
/**
 * the SimpleBeanAlterTable class.
 * @author otaviojava
 */
@Entity(name = "SimpleBeanAlterTable")
public class SimpleBeanAlterTable {

    @Id
    private Long id;

    @Index
    @Column(name = "name")
    private String name;

    @Column(name = "born")
    private Integer year;

    @Column(name = "nickname")
    private String nickName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}
