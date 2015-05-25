/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mapping.gson;

/**
 *
 * @author user
 */
public class Location {

    private LatLong latLng;
    private LatLong displayLatLng;
    private String mapUrl;

    public Location() {
    }

    public LatLong getLatLong() {
        return latLng;
    }
}
