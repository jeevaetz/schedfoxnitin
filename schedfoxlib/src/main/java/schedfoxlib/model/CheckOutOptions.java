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
public class CheckOutOptions implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer checkOutOptionId;
    private String checkOutName;

    public CheckOutOptions() {
    }

    public CheckOutOptions(Integer checkOutOptionId) {
        this.checkOutOptionId = checkOutOptionId;
    }

    public CheckOutOptions(Record_Set rst) {
        try {
            this.checkOutOptionId = rst.getInt("check_out_option_id");
        } catch (Exception e) {}
        try {
            this.checkOutName = rst.getString("check_out_name");
        } catch (Exception e) {}
    }

    public CheckOutOptions(Integer checkOutOptionId, String checkOutName) {
        this.checkOutOptionId = checkOutOptionId;
        this.checkOutName = checkOutName;
    }

    public Integer getCheckOutOptionId() {
        return checkOutOptionId;
    }

    public void setCheckOutOptionId(Integer checkOutOptionId) {
        this.checkOutOptionId = checkOutOptionId;
    }

    public String getCheckOutName() {
        return checkOutName;
    }

    public void setCheckOutName(String checkOutName) {
        this.checkOutName = checkOutName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (checkOutOptionId != null ? checkOutOptionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CheckOutOptions)) {
            return false;
        }
        CheckOutOptions other = (CheckOutOptions) object;
        if ((this.checkOutOptionId == null && other.checkOutOptionId != null) || (this.checkOutOptionId != null && !this.checkOutOptionId.equals(other.checkOutOptionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.checkOutName;
    }

}
