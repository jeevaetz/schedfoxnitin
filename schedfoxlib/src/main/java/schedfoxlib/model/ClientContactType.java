/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.util.Collection;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class ClientContactType implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer clientContactType;
    private String contactType;
    private Collection<ClientContact> clientContactCollection;

    public ClientContactType() {
    }

    public ClientContactType(Record_Set rst) {
        this.clientContactType = rst.getInt("client_contact_type");
        this.contactType = rst.getString("contact_type");
    }

    public ClientContactType(Integer clientContactType) {
        this.clientContactType = clientContactType;
    }

    public ClientContactType(Integer clientContactType, String contactType) {
        this.clientContactType = clientContactType;
        this.contactType = contactType;
    }

    public Integer getClientContactType() {
        return clientContactType;
    }

    public void setClientContactType(Integer clientContactType) {
        this.clientContactType = clientContactType;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public Collection<ClientContact> getClientContactCollection() {
        return clientContactCollection;
    }

    public void setClientContactCollection(Collection<ClientContact> clientContactCollection) {
        this.clientContactCollection = clientContactCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientContactType != null ? clientContactType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientContactType)) {
            return false;
        }
        ClientContactType other = (ClientContactType) object;
        if ((this.clientContactType == null && other.clientContactType != null) || (this.clientContactType != null && !this.clientContactType.equals(other.clientContactType))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getContactType();
    }

}
