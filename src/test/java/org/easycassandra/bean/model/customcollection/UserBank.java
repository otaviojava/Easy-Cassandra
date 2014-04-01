package org.easycassandra.bean.model.customcollection;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * user account.
 * @author otaviojava
 */
public class UserBank implements Serializable{

    private static final long serialVersionUID = 1L;

    private String byUser;

    private String fromUser;

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserBank) {
            UserBank other = UserBank.class.cast(obj);
            return new EqualsBuilder().append(byUser,
                    other.byUser).append(fromUser, other.fromUser).isEquals();
        }
        return false;
    }
    @Override
    public int hashCode() {

        return new HashCodeBuilder().append(fromUser).append(byUser).toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }



}
