/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mapping;

import java.util.LinkedList;
import rmischeduleserver.mapping.gson.Location;

/**
 *
 * @author user
 */
public class GeoCodingResults {
    private LinkedList<ResultObject> results;

    public GeoCodingResults() {}

    public LinkedList<ResultObject> getResults() {
        return results;
    }

    public static class ResultObject {

        private LinkedList<Location> locations;

        public ResultObject() {}

        public LinkedList<Location> getLocations() {
            return locations;
        }
    }

    

}
