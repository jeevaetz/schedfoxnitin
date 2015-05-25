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
public class ClientRateCode extends GenericRateCode implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer clientRateCodeId;
    private int clientId;


    public ClientRateCode() {
    }

    public ClientRateCode(Record_Set rst) {
        this.clientRateCodeId = rst.getInt("client_rate_code_id");
        super.setRateCodeId(rst.getInt("rate_code_id"));
        super.setPayAmount(rst.getBigDecimal("pay_amount"));
        super.setOvertimeAmount(rst.getBigDecimal("overtime_amount"));
        super.setBillAmount(rst.getBigDecimal("bill_amount"));
        super.setOvertimeBill(rst.getBigDecimal("overtime_bill"));
        super.setHourType(rst.getInt("hour_type"));
        super.setDescription(rst.getString("description"));
        this.clientId = rst.getInt("client_id");
    }

    public Integer getClientRateCodeId() {
        return clientRateCodeId;
    }

    public void setClientRateCodeId(Integer clientRateCodeId) {
        this.clientRateCodeId = clientRateCodeId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientRateCodeId != null ? clientRateCodeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientRateCode)) {
            return false;
        }
        ClientRateCode other = (ClientRateCode) object;
        if ((this.clientRateCodeId == null && other.clientRateCodeId != null) || (this.clientRateCodeId != null && !this.clientRateCodeId.equals(other.clientRateCodeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.ClientRateCode[clientRateCodeId=" + clientRateCodeId + "]";
    }

    @Override
    public int getPrimaryKey() {
        return this.getClientRateCodeId();
    }

    @Override
    public void setPrimaryKey(int primaryKey) {
        this.clientRateCodeId = primaryKey;
    }
}
