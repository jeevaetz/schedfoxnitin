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
public class ClientBilling implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer clientBillingId;
    private boolean clientIsBillable;
    private int clientBillFrequencyId;
    private int clientId;
    private int salesTaxId;
    private boolean billOvertime;

    public ClientBilling() {
    }

    public ClientBilling(Integer clientBillingId) {
        this.clientBillingId = clientBillingId;
    }

    public ClientBilling(Integer clientBillingId, boolean clientIsBillable, int clientBillFrequencyId, int clientId, int salesTaxId) {
        this.clientBillingId = clientBillingId;
        this.clientIsBillable = clientIsBillable;
        this.clientBillFrequencyId = clientBillFrequencyId;
        this.clientId = clientId;
        this.salesTaxId = salesTaxId;
    }

    public Integer getClientBillingId() {
        return clientBillingId;
    }

    public void setClientBillingId(Integer clientBillingId) {
        this.clientBillingId = clientBillingId;
    }

    public boolean getClientIsBillable() {
        return clientIsBillable;
    }

    public void setClientIsBillable(boolean clientIsBillable) {
        this.clientIsBillable = clientIsBillable;
    }

    public int getClientBillFrequencyId() {
        return clientBillFrequencyId;
    }

    public void setClientBillFrequencyId(int clientBillFrequencyId) {
        this.clientBillFrequencyId = clientBillFrequencyId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getSalesTaxId() {
        return salesTaxId;
    }

    public void setSalesTaxId(int salesTaxId) {
        this.salesTaxId = salesTaxId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientBillingId != null ? clientBillingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientBilling)) {
            return false;
        }
        ClientBilling other = (ClientBilling) object;
        if ((this.clientBillingId == null && other.clientBillingId != null) || (this.clientBillingId != null && !this.clientBillingId.equals(other.clientBillingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.ClientBilling[clientBillingId=" + clientBillingId + "]";
    }

    /**
     * @return the billOvertime
     */
    public boolean isBillOvertime() {
        return billOvertime;
    }

    /**
     * @param billOvertime the billOvertime to set
     */
    public void setBillOvertime(boolean billOvertime) {
        this.billOvertime = billOvertime;
    }

}
