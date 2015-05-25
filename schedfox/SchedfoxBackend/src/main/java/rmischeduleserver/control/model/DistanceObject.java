/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control.model;

import java.util.ArrayList;
import java.util.LinkedList;

public class DistanceObject {

    private LinkedList<String> destination_addresses;
    private LinkedList<String> origin_addresses;
    private LinkedList<DistanceInfoContainer> rows;
    private String status;

    public DistanceObject() {
    }

    /**
     * @return the destination_addresses
     */
    public LinkedList<String> getDestination_addresses() {
        return destination_addresses;
    }

    /**
     * @param destination_addresses the destination_addresses to set
     */
    public void setDestination_addresses(LinkedList<String> destination_addresses) {
        this.destination_addresses = destination_addresses;
    }

    /**
     * @return the origin_addresses
     */
    public LinkedList<String> getOrigin_addresses() {
        return origin_addresses;
    }

    /**
     * @param origin_addresses the origin_addresses to set
     */
    public void setOrigin_addresses(LinkedList<String> origin_addresses) {
        this.origin_addresses = origin_addresses;
    }

    /**
     * @return the rows
     */
    public LinkedList<DistanceInfoContainer> getRows() {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(LinkedList<DistanceInfoContainer> rows) {
        this.rows = rows;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
