/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;

/**
 *
 * @author user
 */
public class ClientCheckinContact implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer clientCheckinContactId;
    private int clientId;
    private String clientCheckinContactType;
    private String clientCheckinContactValue;

    public ClientCheckinContact() {
    }

    public ClientCheckinContact(Integer clientCheckinContactId) {
        this.clientCheckinContactId = clientCheckinContactId;
    }

    public ClientCheckinContact(Integer clientCheckinContactId, int clientId, String clientCheckinContactType, String clientCheckinContactValue) {
        this.clientCheckinContactId = clientCheckinContactId;
        this.clientId = clientId;
        this.clientCheckinContactType = clientCheckinContactType;
        this.clientCheckinContactValue = clientCheckinContactValue;
    }

    public Integer getClientCheckinContactId() {
        return clientCheckinContactId;
    }

    public void setClientCheckinContactId(Integer clientCheckinContactId) {
        this.clientCheckinContactId = clientCheckinContactId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getClientCheckinContactType() {
        return clientCheckinContactType;
    }

    public void setClientCheckinContactType(String clientCheckinContactType) {
        this.clientCheckinContactType = clientCheckinContactType;
    }

    public String getClientCheckinContactValue() {
        return clientCheckinContactValue;
    }

    public void setClientCheckinContactValue(String clientCheckinContactValue) {
        this.clientCheckinContactValue = clientCheckinContactValue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientCheckinContactId != null ? clientCheckinContactId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientCheckinContact)) {
            return false;
        }
        ClientCheckinContact other = (ClientCheckinContact) object;
        if ((this.clientCheckinContactId == null && other.clientCheckinContactId != null) || (this.clientCheckinContactId != null && !this.clientCheckinContactId.equals(other.clientCheckinContactId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.ClientCheckinContact[clientCheckinContactId=" + clientCheckinContactId + "]";
    }

}
