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
public class Maneuver {

    private LinkedList<Sign> signs;
    private String narrative;
    private int direction;
    private String iconUrl;
    private double distance;
    private int time;
    private String directionName;
    private String mapUrl;

    public Maneuver() {
    }

    /**
     * @return the signs
     */
    public LinkedList<Sign> getSigns() {
        return signs;
    }

    /**
     * @param signs the signs to set
     */
    public void setSigns(LinkedList<Sign> signs) {
        this.signs = signs;
    }

    /**
     * @return the narrative
     */
    public String getNarrative() {
        return narrative;
    }

    /**
     * @param narrative the narrative to set
     */
    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    /**
     * @return the direction
     */
    public int getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * @return the iconUrl
     */
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * @param iconUrl the iconUrl to set
     */
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    /**
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * @return the time
     */
    public int getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * @return the directionName
     */
    public String getDirectionName() {
        return directionName;
    }

    /**
     * @param directionName the directionName to set
     */
    public void setDirectionName(String directionName) {
        this.directionName = directionName;
    }

    /**
     * @return the mapUrl
     */
    public String getMapUrl() {
        return mapUrl;
    }

    /**
     * @param mapUrl the mapUrl to set
     */
    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }
}
