/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class ClientRouteToClient implements Serializable {
    private Integer clientRouteToClientId;
    private Integer routeId;
    private Integer clientId;
    
    public ClientRouteToClient() {
        
    }
    
    public ClientRouteToClient(Record_Set rst) {
        try {
            clientRouteToClientId = rst.getInt("client_route_to_client_id");
        } catch (Exception exe) {}
        try {
            routeId = rst.getInt("route_id");
        } catch (Exception exe) {}
        try {
            clientId = rst.getInt("client_id");
        } catch (Exception exe) {}
    }

    /**
     * @return the clientRouteToClientId
     */
    public Integer getClientRouteToClientId() {
        return clientRouteToClientId;
    }

    /**
     * @param clientRouteToClientId the clientRouteToClientId to set
     */
    public void setClientRouteToClientId(Integer clientRouteToClientId) {
        this.clientRouteToClientId = clientRouteToClientId;
    }

    /**
     * @return the routeId
     */
    public Integer getRouteId() {
        return routeId;
    }

    /**
     * @param routeId the routeId to set
     */
    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
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
}
