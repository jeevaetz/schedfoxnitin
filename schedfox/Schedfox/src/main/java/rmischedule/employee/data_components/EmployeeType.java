/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.employee.data_components;

import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class EmployeeType {
    private int employeeTypeId;
    private String employeeType;

    public EmployeeType() {
        employeeTypeId = 0;
        employeeType = "";
    }

    public EmployeeType(Record_Set rst) {
        employeeTypeId = rst.getInt("employee_type_id");
        employeeType = rst.getString("employee_type");
    }

    /**
     * @return the employeeTypeId
     */
    public int getEmployeeTypeId() {
        return employeeTypeId;
    }

    /**
     * @param employeeTypeId the employeeTypeId to set
     */
    public void setEmployeeTypeId(int employeeTypeId) {
        this.employeeTypeId = employeeTypeId;
    }

    /**
     * @return the employeeType
     */
    public String getEmployeeType() {
        return employeeType;
    }

    /**
     * @param employeeType the employeeType to set
     */
    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    @Override
    public boolean equals(Object obj) {
        boolean retVal = false;
        if (obj instanceof EmployeeType) {
            EmployeeType compObj = (EmployeeType)obj;
            retVal = this.getEmployeeTypeId() == compObj.getEmployeeTypeId();
        } else if (obj instanceof Integer) {
            Integer compObj = (Integer)obj;
            retVal = this.getEmployeeTypeId() == compObj.intValue();
        }
        return retVal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.employeeTypeId;
        return hash;
    }

    @Override
    public String toString() {
        return this.employeeType;
    }

}
