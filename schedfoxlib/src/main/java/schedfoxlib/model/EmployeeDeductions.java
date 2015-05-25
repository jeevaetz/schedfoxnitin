/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.math.BigDecimal;
import schedfoxlib.controller.EmployeeControllerInterface;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.controller.UserControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;

/**
 *
 * @author user
 */
public class EmployeeDeductions implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer employeeDeductionId;
    private Integer employeeId;
    private BigDecimal amount;
    private BigDecimal balance;
    private Integer employeeDeductionTypeId;

    private Boolean deductionWrittenOff;
    private Integer writtenOffBy;

    private User writtenOffUser;

    //Lazy loaded object
    private EmployeeDeductionTypes deductionType;

    public EmployeeDeductions() {
    }

    public EmployeeDeductions(Integer employeeDeductionId) {
        this.employeeDeductionId = employeeDeductionId;
    }

    public EmployeeDeductions(Record_Set rst) {
        try {
            this.employeeDeductionId = rst.getInt("employee_deduction_id");
        } catch (Exception e) {}
        try {
            this.amount = rst.getBigDecimal("amount");
        } catch (Exception e) {}
        try {
            this.balance = rst.getBigDecimal("balance");
        } catch (Exception e) {}
        try {
            this.employeeDeductionTypeId = rst.getInt("employee_deduction_type_id");
        } catch (Exception e) {}
        try {
            this.employeeId = rst.getInt("employee_id");
        } catch (Exception e) {}
        try {
            this.writtenOffBy = rst.getInt("written_off_by");
        } catch (Exception e) {}
        try {
            this.deductionWrittenOff = rst.getBoolean("deduction_written_off");
        } catch (Exception e) {}

    }

    public Integer getEmployeeDeductionId() {
        return employeeDeductionId;
    }

    public void setEmployeeDeductionId(Integer employeeDeductionId) {
        this.employeeDeductionId = employeeDeductionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeDeductionId != null ? employeeDeductionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmployeeDeductions)) {
            return false;
        }
        EmployeeDeductions other = (EmployeeDeductions) object;
        if ((this.employeeDeductionId == null && other.employeeDeductionId != null) || (this.employeeDeductionId != null && !this.employeeDeductionId.equals(other.employeeDeductionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.EmployeeDeductions[employeeDeductionId=" + employeeDeductionId + "]";
    }

    /**
     * @return the employeeDeductionTypeId
     */
    public Integer getEmployeeDeductionTypeId() {
        return employeeDeductionTypeId;
    }

    /**
     * @param employeeDeductionTypeId the employeeDeductionTypeId to set
     */
    public void setEmployeeDeductionTypeId(Integer employeeDeductionTypeId) {
        this.employeeDeductionTypeId = employeeDeductionTypeId;
    }

    /**
     * @return the employeeId
     */
    public Integer getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public EmployeeDeductionTypes getEmployeeDeductionTypes(String companyId) {
        if (this.getDeductionType() == null) {
            try {
                if (this.employeeDeductionId != null) {
                    EmployeeControllerInterface empInterface = ControllerRegistryAbstract.getEmployeeController(companyId);
                    this.setDeductionType(empInterface.getEmployeeDeductionType(this.employeeDeductionTypeId));
                }
                if (this.getDeductionType() == null) {
                    throw new Exception("");
                }
            } catch (Exception e) {
                setDeductionType(new EmployeeDeductionTypes());
                getDeductionType().setDescription("Not Set");
            }
        }
        return getDeductionType();
    }

    /**
     * @return the writtenOffBy
     */
    public Integer getWrittenOffBy() {
        return writtenOffBy;
    }

    /**
     * @param writtenOffBy the writtenOffBy to set
     */
    public void setWrittenOffBy(Integer writtenOffBy) {
        this.writtenOffBy = writtenOffBy;
    }

    /**
     * @return the deductionType
     */
    public EmployeeDeductionTypes getDeductionType() {
        return deductionType;
    }

    /**
     * @param deductionType the deductionType to set
     */
    public void setDeductionType(EmployeeDeductionTypes deductionType) {
        this.deductionType = deductionType;
    }

    /**
     * @return the deductionWrittenOff
     */
    public Boolean getDeductionWrittenOff() {
        return deductionWrittenOff;
    }

    /**
     * @param deductionWrittenOff the deductionWrittenOff to set
     */
    public void setDeductionWrittenOff(Boolean deductionWrittenOff) {
        this.deductionWrittenOff = deductionWrittenOff;
    }

    public User getWrittenOffUser() {
        if (this.writtenOffUser == null) {
            try {
                UserControllerInterface userController = ControllerRegistryAbstract.getUserController("");
                writtenOffUser = userController.getUserById(this.writtenOffBy);
            } catch (Exception e) {}
        }
        return this.writtenOffUser;
    }
}
