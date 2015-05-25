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
public class RateCode implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer rateCodeId;
    private String rateCodeName;
    private String uskedRateCode;

    public RateCode() {
    }

    public RateCode(Record_Set rst) {
        this.rateCodeId = rst.getInt("rate_code_id");
        this.rateCodeName = rst.getString("rate_code_name");
        this.uskedRateCode = rst.getString("usked_rate_code");
    }

    public RateCode(Integer rateCodeId) {
        this.rateCodeId = rateCodeId;
    }

    public RateCode(Integer rateCodeId, String rateCodeName, String uskedRateCode) {
        this.rateCodeId = rateCodeId;
        this.rateCodeName = rateCodeName;
        this.uskedRateCode = uskedRateCode;
    }

    public Integer getRateCodeId() {
        return rateCodeId;
    }

    public void setRateCodeId(Integer rateCodeId) {
        this.rateCodeId = rateCodeId;
    }

    public String getRateCodeName() {
        return rateCodeName;
    }

    public void setRateCodeName(String rateCodeName) {
        this.rateCodeName = rateCodeName;
    }

    public String getUskedRateCode() {
        return uskedRateCode;
    }

    public void setUskedRateCode(String uskedRateCode) {
        this.uskedRateCode = uskedRateCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rateCodeId != null ? rateCodeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RateCode)) {
            return false;
        }
        RateCode other = (RateCode) object;
        if ((this.rateCodeId == null && other.rateCodeId != null) || (this.rateCodeId != null && !this.rateCodeId.equals(other.rateCodeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.rateCodeName;
    }

}
