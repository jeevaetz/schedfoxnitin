/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control.model;

import java.util.LinkedList;

/**
 *
 * @author ira
 */
public class DistanceInfoContainer {
    private LinkedList<DistanceInfo> elements;
    
    public DistanceInfoContainer() {
        
    }

    /**
     * @return the elements
     */
    public LinkedList<DistanceInfo> getElements() {
        return elements;
    }

    /**
     * @param elements the elements to set
     */
    public void setElements(LinkedList<DistanceInfo> elements) {
        this.elements = elements;
    }
}
