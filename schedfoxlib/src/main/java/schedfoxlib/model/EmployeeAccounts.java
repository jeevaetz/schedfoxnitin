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
public class EmployeeAccounts implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer employeeAccountId;
    private boolean allowDirectDeposit;
    private String routingNumber;
    private String accountNumber;
    private int accountType;
    private int employeeId;

    public EmployeeAccounts() {
    }

    public EmployeeAccounts(Integer employeeAccountId) {
        this.employeeAccountId = employeeAccountId;
    }

    public EmployeeAccounts(Record_Set rst) {
        try {
            this.employeeAccountId = rst.getInt("employee_account_id");
        } catch (Exception e) {
        }
        try {
            this.allowDirectDeposit = rst.getBoolean("allow_direct_deposit");
        } catch (Exception e) {
        }
        try {
            this.routingNumber = rst.getString("routing_number");
        } catch (Exception e) {
        }
        try {
            this.accountNumber = rst.getString("account_number");
        } catch (Exception e) {
        }
        try {
            this.accountType = rst.getInt("account_type");
        } catch (Exception e) {
        }
        try {
            this.employeeId = rst.getInt("employee_id");
        } catch (Exception e) {}
    }

    public Integer getEmployeeAccountId() {
        return employeeAccountId;
    }

    public void setEmployeeAccountId(Integer employeeAccountId) {
        this.employeeAccountId = employeeAccountId;
    }

    public boolean getAllowDirectDeposit() {
        return allowDirectDeposit;
    }

    public void setAllowDirectDeposit(boolean allowDirectDeposit) {
        this.allowDirectDeposit = allowDirectDeposit;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeAccountId != null ? employeeAccountId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmployeeAccounts)) {
            return false;
        }
        EmployeeAccounts other = (EmployeeAccounts) object;
        if ((this.employeeAccountId == null && other.employeeAccountId != null) || (this.employeeAccountId != null && !this.employeeAccountId.equals(other.employeeAccountId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.EmployeeAccounts[employeeAccountId=" + employeeAccountId + "]";
    }

    /**
     * @return the employeeId
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}
