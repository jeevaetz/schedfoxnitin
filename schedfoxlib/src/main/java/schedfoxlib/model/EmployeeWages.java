/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.math.BigDecimal;
import schedfoxlib.controller.EmployeeControllerInterface;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;

/**
 *
 * @author user
 */
public class EmployeeWages implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer employeeWageId;
    private Integer employeeId;
    private BigDecimal wages;
    private Integer employeeWageTypeId;

    //Lazy loaded object
    private EmployeeWageTypes wageType;

    public EmployeeWages() {
    }

    public EmployeeWages(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public EmployeeWages(Record_Set rst) {
        try {
            this.employeeId = rst.getInt("employee_id");
        } catch (Exception e) {}
        try {
            this.employeeWageId = rst.getInt("employee_wage_id");
        } catch (Exception e) {}
        try {
            this.employeeWageTypeId = rst.getInt("employee_wage_type_id");
        } catch (Exception e) {}
        try {
            this.wages = rst.getBigDecimal("wages");
        } catch (Exception e) {}
    }

    public Integer getEmployeeWageId() {
        return employeeWageId;
    }

    public void setEmployeeWageId(Integer employeeWageId) {
        this.employeeWageId = employeeWageId;
    }

    public BigDecimal getWages() {
        return wages;
    }

    public void setWages(BigDecimal wages) {
        this.wages = wages;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return the employeeWageTypeId
     */
    public Integer getEmployeeWageTypeId() {
        return employeeWageTypeId;
    }

    /**
     * @param employeeWageTypeId the employeeWageTypeId to set
     */
    public void setEmployeeWageTypeId(Integer employeeWageTypeId) {
        this.employeeWageTypeId = employeeWageTypeId;
    }

    /**
     * @return the wageType
     */
    public EmployeeWageTypes getWageType(String companyId) {
        if (wageType == null) {
            try {
                if (this.employeeWageTypeId != null) {
                    EmployeeControllerInterface employeeInterface = ControllerRegistryAbstract.getEmployeeController(companyId);
                    wageType = employeeInterface.getWageType(employeeWageTypeId);
                }
                if (this.wageType == null) {
                    throw new Exception("");
                }
            } catch (Exception e) {
                wageType = new EmployeeWageTypes();
                wageType.setDescription("Not Set");
            }
        }
        return wageType;
    }

    /**
     * @param wageType the wageType to set
     */
    public void setWageType(EmployeeWageTypes wageType) {
        this.wageType = wageType;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeId != null ? employeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmployeeWages)) {
            return false;
        }
        EmployeeWages other = (EmployeeWages) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.EmployeeWages[employeeId=" + employeeId + "]";
    }

    

}
