/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.util.Date;
import schedfoxlib.controller.ClientControllerInterface;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;

/**
 *
 * @author user
 */
public class CommunicationSource implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer communicationSourceId;
    private int communicationTypeId;
    private String source;

    //Lazy loaded objects
    private Client clientAssociatedWith;

    public CommunicationSource() {
    }

    public CommunicationSource(Integer communicationSourceId) {
        this.communicationSourceId = communicationSourceId;
    }

    public CommunicationSource(Integer communicationSourceId, int communicationTypeId, String source) {
        this.communicationSourceId = communicationSourceId;
        this.communicationTypeId = communicationTypeId;
        this.source = source;
    }

    public CommunicationSource(Record_Set rst) {
        this.communicationSourceId = rst.getInt("communication_source_id");
        this.communicationTypeId = rst.getInt("communication_type_id");
        this.source = rst.getString("source");
    }

    public Integer getCommunicationSourceId() {
        return communicationSourceId;
    }

    public void setCommunicationSourceId(Integer communicationSourceId) {
        this.communicationSourceId = communicationSourceId;
    }

    public int getCommunicationTypeId() {
        return communicationTypeId;
    }

    public void setCommunicationTypeId(int communicationTypeId) {
        this.communicationTypeId = communicationTypeId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (communicationSourceId != null ? communicationSourceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CommunicationSource)) {
            return false;
        }
        CommunicationSource other = (CommunicationSource) object;
        if ((this.communicationSourceId == null && other.communicationSourceId != null) || (this.communicationSourceId != null && !this.communicationSourceId.equals(other.communicationSourceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.CommunicationSource[communicationSourceId=" + communicationSourceId + "]";
    }

    /**
     * Lazy loads associated employee from communication source.
     * @param companyId
     * @return
     */
    public Client getClientAssociatedWith(String companyId) {
        if (this.clientAssociatedWith == null) {
            ClientControllerInterface clientInterface = ControllerRegistryAbstract.getClientController(companyId);
            try {
                this.clientAssociatedWith = clientInterface.getClientByCommunicationSource(this);
            } catch (Exception exe) {
                this.clientAssociatedWith = new Client(new Date());
            }
        }
        return this.clientAssociatedWith;
    }

}
