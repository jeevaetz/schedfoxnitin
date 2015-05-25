/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import schedfoxlib.controller.ClientControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;

/**
 *
 * @author ira
 */
public class CalcedLocationDistance {
    private Integer clientId;
    private Integer employeeId;
    private Integer travelDistance;
    private Integer travelDuration;
    
    //Lazy loaded object
    private Client clientObj;
    
    public CalcedLocationDistance() {
        
    }

    /**
     * @return the clientId
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
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

    /**
     * @return the travelDistance
     */
    public Integer getTravelDistance() {
        return travelDistance;
    }

    /**
     * @param travelDistance the travelDistance to set
     */
    public void setTravelDistance(Integer travelDistance) {
        this.travelDistance = travelDistance;
    }

    /**
     * @return the travelDuration
     */
    public Integer getTravelDuration() {
        return travelDuration;
    }

    /**
     * @param travelDuration the travelDuration to set
     */
    public void setTravelDuration(Integer travelDuration) {
        this.travelDuration = travelDuration;
    }

    /**
     * @return the clientObj
     */
    public Client getClientObj(String companyId) {
        if (clientObj == null) {
            try {
                ClientControllerInterface clientController = ControllerRegistryAbstract.getClientController(companyId);
                clientObj = clientController.getClientById(clientId);
            } catch (Exception exe) {}
        }
        return clientObj;
    }

    /**
     * @param clientObj the clientObj to set
     */
    public void setClientObj(Client clientObj) {
        this.clientObj = clientObj;
    }
    
}
