/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author user
 */
public class ClientPhoneCalls implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer clientPhoneCallId;
    private int clientId;
    private int userId;
    private Date dateOfContact;

    public ClientPhoneCalls() {
    }

    public ClientPhoneCalls(Integer clientPhoneCallId) {
        this.clientPhoneCallId = clientPhoneCallId;
    }

    public ClientPhoneCalls(Integer clientPhoneCallId, int clientId, int userId, Date dateOfContact) {
        this.clientPhoneCallId = clientPhoneCallId;
        this.clientId = clientId;
        this.userId = userId;
        this.dateOfContact = dateOfContact;
    }

    public Integer getClientPhoneCallId() {
        return clientPhoneCallId;
    }

    public void setClientPhoneCallId(Integer clientPhoneCallId) {
        this.clientPhoneCallId = clientPhoneCallId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDateOfContact() {
        return dateOfContact;
    }

    public void setDateOfContact(Date dateOfContact) {
        this.dateOfContact = dateOfContact;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientPhoneCallId != null ? clientPhoneCallId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientPhoneCalls)) {
            return false;
        }
        ClientPhoneCalls other = (ClientPhoneCalls) object;
        if ((this.clientPhoneCallId == null && other.clientPhoneCallId != null) || (this.clientPhoneCallId != null && !this.clientPhoneCallId.equals(other.clientPhoneCallId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.ClientPhoneCalls[clientPhoneCallId=" + clientPhoneCallId + "]";
    }

}
