package org.easycassandra.bean.model.customcollection;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
/**
 * the bank operations.
 * @author otaviojava
 */
public class HistoryOperations implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID operationCode;

    private Date date;

    private Double value;

    private String byUser;

    private String fromUser;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getByUser() {
        return byUser;
    }

    public void setByUser(String byUser) {
        this.byUser = byUser;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public UUID getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(UUID operationCode) {
        this.operationCode = operationCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof HistoryOperations) {
            HistoryOperations other = HistoryOperations.class.cast(obj);
            return new EqualsBuilder().append(operationCode,
                    other.operationCode).isEquals();
        }
        return false;
    }
    @Override
    public int hashCode() {

        return new HashCodeBuilder().append(operationCode).toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
