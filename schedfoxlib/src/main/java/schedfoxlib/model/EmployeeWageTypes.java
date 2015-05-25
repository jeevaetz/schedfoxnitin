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
public class EmployeeWageTypes implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer employeeWageTypeId;
    private String description;
    private boolean oneTime;

    public EmployeeWageTypes() {
    }

    public EmployeeWageTypes(Integer employeeWageTypeId) {
        this.employeeWageTypeId = employeeWageTypeId;
    }

    public EmployeeWageTypes(Record_Set rst) {
        try {
            this.employeeWageTypeId = rst.getInt("employee_wage_type_id");
        } catch (Exception e) {}
        try {
            this.description = rst.getString("description");
            } catch (Exception e) {}
        try {
            this.oneTime = rst.getBoolean("one_time");
        } catch (Exception e) {}
    }

    public Integer getEmployeeWageTypeId() {
        return employeeWageTypeId;
    }

    public void setEmployeeWageTypeId(Integer employeeWageTypeId) {
        this.employeeWageTypeId = employeeWageTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getOneTime() {
        return oneTime;
    }

    public void setOneTime(boolean oneTime) {
        this.oneTime = oneTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeWageTypeId != null ? employeeWageTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Integer) {
            return this.getEmployeeWageTypeId().equals(object);
        }
        if (!(object instanceof EmployeeWageTypes)) {
            return false;
        }
        EmployeeWageTypes other = (EmployeeWageTypes) object;
        if ((this.employeeWageTypeId == null && other.employeeWageTypeId != null) || (this.employeeWageTypeId != null && !this.employeeWageTypeId.equals(other.employeeWageTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.description;
    }

}
