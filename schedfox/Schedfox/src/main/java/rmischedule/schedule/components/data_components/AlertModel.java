/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.schedule.components.data_components;

import java.awt.event.ActionListener;
import schedfoxlib.model.Client;
import schedfoxlib.model.Employee;

/**
 *
 * @author ira
 */
public class AlertModel {
    private String alertText;
    private ActionListener alertAction;
    private Employee employee;
    private Client client;

    public AlertModel() {
        
    }
    
    /**
     * @return the alertText
     */
    public String getAlertText() {
        return alertText;
    }

    /**
     * @param alertText the alertText to set
     */
    public void setAlertText(String alertText) {
        this.alertText = alertText;
    }

    /**
     * @return the alertAction
     */
    public ActionListener getAlertAction() {
        return alertAction;
    }

    /**
     * @param alertAction the alertAction to set
     */
    public void setAlertAction(ActionListener alertAction) {
        this.alertAction = alertAction;
    }

    /**
     * @return the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }
    
    
}
