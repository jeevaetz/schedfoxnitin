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
public class EmployeeTypes implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer employeeTypeId;
    private String employeeType;

    public EmployeeTypes() {
    }

    public EmployeeTypes(Record_Set rst) {
        try {
            this.employeeTypeId = rst.getInt("employee_type_id");
        } catch (Exception e) {}
        try {
            this.employeeType = rst.getString("employee_type");
        } catch (Exception e) {}
    }

    public EmployeeTypes(Integer employeeTypeId) {
        this.employeeTypeId = employeeTypeId;
    }

    public EmployeeTypes(Integer employeeTypeId, String employeeType) {
        this.employeeTypeId = employeeTypeId;
        this.employeeType = employeeType;
    }

    public Integer getEmployeeTypeId() {
        return employeeTypeId;
    }

    public void setEmployeeTypeId(Integer employeeTypeId) {
        this.employeeTypeId = employeeTypeId;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeTypeId != null ? employeeTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmployeeTypes)) {
            return false;
        }
        EmployeeTypes other = (EmployeeTypes) object;
        if ((this.employeeTypeId == null && other.employeeTypeId != null) || (this.employeeTypeId != null && !this.employeeTypeId.equals(other.employeeTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.employeeType;
    }

}
