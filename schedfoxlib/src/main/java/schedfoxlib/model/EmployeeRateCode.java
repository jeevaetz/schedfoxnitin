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
public class EmployeeRateCode extends GenericRateCode implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer employeeRateCodeId;
    private int employeeId;


    public EmployeeRateCode() {
    }

    public EmployeeRateCode(Record_Set rst) {
        this.employeeRateCodeId = rst.getInt("employee_rate_code_id");
        super.setRateCodeId(rst.getInt("rate_code_id"));
        super.setPayAmount(rst.getBigDecimal("pay_amount"));
        super.setOvertimeAmount(rst.getBigDecimal("overtime_amount"));
        super.setBillAmount(rst.getBigDecimal("bill_amount"));
        super.setOvertimeBill(rst.getBigDecimal("overtime_bill"));
        super.setHourType(rst.getInt("hour_type"));
        super.setDescription(rst.getString("description"));
        this.employeeId = rst.getInt("employee_id");
    }

    public Integer getEmployeeRateCodeId() {
        return employeeRateCodeId;
    }

    public void setEmployeeRateCodeId(Integer clientRateCodeId) {
        this.employeeRateCodeId = clientRateCodeId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeRateCodeId != null ? employeeRateCodeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmployeeRateCode)) {
            return false;
        }
        EmployeeRateCode other = (EmployeeRateCode) object;
        if ((this.employeeRateCodeId == null && other.employeeRateCodeId != null) || (this.employeeRateCodeId != null && !this.employeeRateCodeId.equals(other.employeeRateCodeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.EmployeeRateCode[employeeRateCodeId=" + employeeRateCodeId + "]";
    }

    @Override
    public int getPrimaryKey() {
        return this.getEmployeeRateCodeId();
    }

    @Override
    public void setPrimaryKey(int primaryKey) {
        this.employeeRateCodeId = primaryKey;
    }
}
