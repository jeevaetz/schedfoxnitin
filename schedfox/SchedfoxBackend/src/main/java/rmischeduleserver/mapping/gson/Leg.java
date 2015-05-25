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
public class Leg {

    private LinkedList<Maneuver> maneuvers;

    /**
     * @return the maneuvers
     */
    public LinkedList<Maneuver> getManeuvers() {
        return maneuvers;
    }

    /**
     * @param maneuvers the maneuvers to set
     */
    public void setManeuvers(LinkedList<Maneuver> maneuvers) {
        this.maneuvers = maneuvers;
    }
}
