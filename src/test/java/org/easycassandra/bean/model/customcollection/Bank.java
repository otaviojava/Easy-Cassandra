package org.easycassandra.bean.model.customcollection;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * the bank.
 * @author otaviojava
 */
@Entity(name = "bank")
public class Bank implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer codBank;

    @ElementCollection
    private Map<UserBank, HistoryOperations> mapBank;
    @ElementCollection
    private List<HistoryOperations> accountsList;
    @ElementCollection
    private Set<HistoryOperations> accountsSet;
    public Integer getCodBank() {
        return codBank;
    }
    public void setCodBank(Integer codBank) {
        this.codBank = codBank;
    }
    public Map<UserBank, HistoryOperations> getMapBank() {
        return mapBank;
    }
    public void setMapBank(Map<UserBank, HistoryOperations> mapBank) {
        this.mapBank = mapBank;
    }
    public List<HistoryOperations> getAccountsList() {
        return accountsList;
    }
    public void setAccountsList(List<HistoryOperations> accountsList) {
        this.accountsList = accountsList;
    }
    public Set<HistoryOperations> getAccountsSet() {
        return accountsSet;
    }
    public void setAccountsSet(Set<HistoryOperations> accountsSet) {
        this.accountsSet = accountsSet;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Bank) {
            Bank other = Bank.class.cast(obj);
            return new EqualsBuilder().append(codBank, other.codBank).isEquals();
        }
        return false;
    }
    @Override
    public int hashCode() {

        return new HashCodeBuilder().append(codBank).toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
