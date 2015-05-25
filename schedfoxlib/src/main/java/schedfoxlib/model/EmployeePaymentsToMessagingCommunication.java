/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;

/**
 *
 * @author ira
 */
public class EmployeePaymentsToMessagingCommunication implements Serializable {
    
    private Integer employeePaymentsToMessagingCommunication;
    private Integer employeePaymentsId;
    private Integer messagingCommunication;
    
    public EmployeePaymentsToMessagingCommunication() {
        
    }

    /**
     * @return the employeePaymentsToMessagingCommunication
     */
    public Integer getEmployeePaymentsToMessagingCommunication() {
        return employeePaymentsToMessagingCommunication;
    }

    /**
     * @param employeePaymentsToMessagingCommunication the employeePaymentsToMessagingCommunication to set
     */
    public void setEmployeePaymentsToMessagingCommunication(Integer employeePaymentsToMessagingCommunication) {
        this.employeePaymentsToMessagingCommunication = employeePaymentsToMessagingCommunication;
    }

    /**
     * @return the employeePaymentsId
     */
    public Integer getEmployeePaymentsId() {
        return employeePaymentsId;
    }

    /**
     * @param employeePaymentsId the employeePaymentsId to set
     */
    public void setEmployeePaymentsId(Integer employeePaymentsId) {
        this.employeePaymentsId = employeePaymentsId;
    }

    /**
     * @return the messagingCommunication
     */
    public Integer getMessagingCommunication() {
        return messagingCommunication;
    }

    /**
     * @param messagingCommunication the messagingCommunication to set
     */
    public void setMessagingCommunication(Integer messagingCommunication) {
        this.messagingCommunication = messagingCommunication;
    }
}
