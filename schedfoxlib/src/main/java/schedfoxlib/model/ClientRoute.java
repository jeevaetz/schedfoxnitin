/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class ClientRoute implements Serializable, Entity, Comparable {
    private Integer clientRouteId;
    private String routeName;
    private Date createdon;
    
    //Not always populated
    private ArrayList<Integer> clientIds;
    private ArrayList<Client> clients;
    
    public ClientRoute() {
        clients = new ArrayList<Client>();
    }

    public ClientRoute(Record_Set rst) {
        clients = new ArrayList<Client>();
        try {
            clientRouteId = rst.getInt("client_route_id");
        } catch (Exception exe) {}
        try {
            routeName = rst.getString("route_name");
        } catch (Exception exe) {}
        try {
            createdon = rst.getDate("createdon");
        } catch (Exception exe) {}
    }
    
    /**
     * @return the clientRouteId
     */
    public Integer getClientRouteId() {
        return clientRouteId;
    }

    /**
     * @param clientRouteId the clientRouteId to set
     */
    public void setClientRouteId(Integer clientRouteId) {
        this.clientRouteId = clientRouteId;
    }

    /**
     * @return the routeName
     */
    public String getRouteName() {
        return routeName;
    }

    /**
     * @param routeName the routeName to set
     */
    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    /**
     * @return the createdon
     */
    public Date getCreatedon() {
        return createdon;
    }

    /**
     * @param createdon the createdon to set
     */
    public void setCreatedon(Date createdon) {
        this.createdon = createdon;
    }

    @Override
    public String getName() {
        return this.getRouteName();
    }

    @Override
    public Integer getId() {
        return this.getClientRouteId();
    }
    
    @Override
    public String toString() {
        return this.getRouteName();
    }
    
    @Override
    public int compareTo(Object obj) {
        if (obj instanceof Entity) {
            return this.getName().compareTo(((Entity) obj).getName());
        }
        return 1;
    }

    /**
     * @return the clientIds
     */
    public ArrayList<Integer> getClientIds() {
        return clientIds;
    }

    /**
     * @param clientIds the clientIds to set
     */
    public void setClientIds(ArrayList<Integer> clientIds) {
        this.clientIds = clientIds;
    }

    /**
     * @return the clients
     */
    public ArrayList<Client> getClients() {
        return clients;
    }

    /**
     * @param clients the clients to set
     */
    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
    }
    
}
