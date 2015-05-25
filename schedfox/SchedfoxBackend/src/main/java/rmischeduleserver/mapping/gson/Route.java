/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mapping.gson;

import java.util.LinkedList;

/**
 *
 * @author user
 */
public class Route {

    private LinkedList<Leg> legs;
    private Shape shape;
    private LinkedList<Location> locations;

    public Route() {
        
    }

    /**
     * @return the legs
     */
    public LinkedList<Leg> getLegs() {
        return legs;
    }

    /**
     * @param legs the legs to set
     */
    public void setLegs(LinkedList<Leg> legs) {
        this.legs = legs;
    }

    /**
     * @return the locations
     */
    public LinkedList<Location> getLocations() {
        return locations;
    }

    /**
     * @param locations the locations to set
     */
    public void setLocations(LinkedList<Location> locations) {
        this.locations = locations;
    }

    /**
     * @return the shape
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * @param shape the shape to set
     */
    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
