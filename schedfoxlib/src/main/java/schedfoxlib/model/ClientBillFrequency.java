/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class ClientBillFrequency implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer clientBillFrequencyId;
    private String frequency;
    private String exportId;

    public ClientBillFrequency() {
    }

    public ClientBillFrequency(Record_Set rst) {
        this.clientBillFrequencyId = rst.getInt("client_bill_frequency_id");
        this.frequency = rst.getString("frequency");
        this.exportId = rst.getString("export_id");
    }

    public ClientBillFrequency(Integer clientBillFrequencyId) {
        this.clientBillFrequencyId = clientBillFrequencyId;
    }

    public ClientBillFrequency(Integer clientBillFrequencyId, String frequency) {
        this.clientBillFrequencyId = clientBillFrequencyId;
        this.frequency = frequency;
    }

    public Integer getClientBillFrequencyId() {
        return clientBillFrequencyId;
    }

    public void setClientBillFrequencyId(Integer clientBillFrequencyId) {
        this.clientBillFrequencyId = clientBillFrequencyId;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getExportId() {
        return exportId;
    }

    public void setExportId(String exportId) {
        this.exportId = exportId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientBillFrequencyId != null ? clientBillFrequencyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientBillFrequency)) {
            return false;
        }
        ClientBillFrequency other = (ClientBillFrequency) object;
        if ((this.clientBillFrequencyId == null && other.clientBillFrequencyId != null) || (this.clientBillFrequencyId != null && !this.clientBillFrequencyId.equals(other.clientBillFrequencyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return frequency;
    }

}
