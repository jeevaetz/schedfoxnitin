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
public class CommunicationTransaction implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer communicationTransactionId;
    private int communicationSourceId;
    private Date dateOfTrans;
    private String data;
    private Integer associatedUserId;
    private Integer associatedUserType;

    public CommunicationTransaction() {
    }

    public CommunicationTransaction(Integer communicationTransactionId) {
        this.communicationTransactionId = communicationTransactionId;
    }

    public CommunicationTransaction(Integer communicationTransactionId, int communicationSourceId, Date dateOfTrans) {
        this.communicationTransactionId = communicationTransactionId;
        this.communicationSourceId = communicationSourceId;
        this.dateOfTrans = dateOfTrans;
    }

    public Integer getCommunicationTransactionId() {
        return communicationTransactionId;
    }

    public void setCommunicationTransactionId(Integer communicationTransactionId) {
        this.communicationTransactionId = communicationTransactionId;
    }

    public int getCommunicationSourceId() {
        return communicationSourceId;
    }

    public void setCommunicationSourceId(int communicationSourceId) {
        this.communicationSourceId = communicationSourceId;
    }

    public Date getDateOfTrans() {
        return dateOfTrans;
    }

    public void setDateOfTrans(Date dateOfTrans) {
        this.dateOfTrans = dateOfTrans;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (communicationTransactionId != null ? communicationTransactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CommunicationTransaction)) {
            return false;
        }
        CommunicationTransaction other = (CommunicationTransaction) object;
        if ((this.communicationTransactionId == null && other.communicationTransactionId != null) || (this.communicationTransactionId != null && !this.communicationTransactionId.equals(other.communicationTransactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.CommunicationTransaction[communicationTransactionId=" + communicationTransactionId + "]";
    }

    /**
     * @return the associatedUserId
     */
    public Integer getAssociatedUserId() {
        return associatedUserId;
    }

    /**
     * @param associatedUserId the associatedUserId to set
     */
    public void setAssociatedUserId(Integer associatedUserId) {
        this.associatedUserId = associatedUserId;
    }

    /**
     * @return the associatedUserType
     */
    public Integer getAssociatedUserType() {
        return associatedUserType;
    }

    /**
     * @param associatedUserType the associatedUserType to set
     */
    public void setAssociatedUserType(Integer associatedUserType) {
        this.associatedUserType = associatedUserType;
    }

}
