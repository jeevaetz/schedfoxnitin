/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;

/**
 *
 * @author ira
 */
public class SalesDeviceBundle implements Serializable {
    private ClientEquipment clientEquipment;
    private User user;

    /**
     * @return the clientEquipment
     */
    public ClientEquipment getClientEquipment() {
        return clientEquipment;
    }

    /**
     * @param clientEquipment the clientEquipment to set
     */
    public void setClientEquipment(ClientEquipment clientEquipment) {
        this.clientEquipment = clientEquipment;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
}
