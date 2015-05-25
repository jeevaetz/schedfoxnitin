/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control.model;

/**
 *
 * @author ira
 */
public class DistanceInfo {
    private Distance distance;
    private Duration duration;
    
    public DistanceInfo() {
        
    }

    /**
     * @return the distance
     */
    public Distance getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    /**
     * @return the duration
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
