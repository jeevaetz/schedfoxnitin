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
public class EmployeeDeductionTypes implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer employeeDeductionTypeId;
    private boolean oneTime;
    private String description;

    public EmployeeDeductionTypes() {
    }

    public EmployeeDeductionTypes(Integer employeeDeductionTypeId) {
        this.employeeDeductionTypeId = employeeDeductionTypeId;
    }

    public EmployeeDeductionTypes(Record_Set rst) {
        try {
            this.employeeDeductionTypeId = rst.getInt("employee_deduction_type_id");
        } catch (Exception e) {}
        try {
            this.oneTime = rst.getBoolean("one_time");
        } catch (Exception e) {}
        try {
            this.description = rst.getString("description");
        } catch (Exception e) {}
    }

    public Integer getEmployeeDeductionTypeId() {
        return employeeDeductionTypeId;
    }

    public void setEmployeeDeductionTypeId(Integer employeeDeductionTypeId) {
        this.employeeDeductionTypeId = employeeDeductionTypeId;
    }

    public boolean getOneTime() {
        return oneTime;
    }

    public void setOneTime(boolean oneTime) {
        this.oneTime = oneTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeDeductionTypeId != null ? employeeDeductionTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Integer) {
            return this.employeeDeductionTypeId.equals(object);
        }
        if (!(object instanceof EmployeeDeductionTypes)) {
            return false;
        }
        EmployeeDeductionTypes other = (EmployeeDeductionTypes) object;
        if ((this.employeeDeductionTypeId == null && other.employeeDeductionTypeId != null) || (this.employeeDeductionTypeId != null && !this.employeeDeductionTypeId.equals(other.employeeDeductionTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return description;
    }
}
