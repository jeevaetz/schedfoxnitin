/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author dalbers
 */
public class Caller {
    private Integer ID;
    private String callerID;
    private Integer employeeID;
    private Integer locationID;
    private long timeStamp;


    public Caller() {

    }

    public Caller(Record_Set rst) {

        if (rst.hasColumn("id")) {
            this.ID = rst.getInt("id");
        }
        if (rst.hasColumn("employeeID")) {
            this.employeeID = rst.getInt("employeeID");
        }
        if (rst.hasColumn("locationID")) {
            this.locationID = rst.getInt("locationID");
        }
        if (rst.hasColumn("CID")) {
            this.callerID = rst.getString("CID");
        }
        if (rst.hasColumn("timestamp")) {
            String time = rst.getString("timestamp");
            try {
                this.timeStamp = Long.valueOf(time);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    
    /**
     * @return the callerID
     */
    public String getCallerID() {
        return callerID;
    }

    /**
     * @param callerID the callerID to set
     */
    public void setCallerID(String callerID) {
        this.callerID = callerID;
    }

    /**
     * @return the employeeID
     */
    public Integer getEmployeeID() {
        return employeeID;
    }

    /**
     * @param employeeID the employeeID to set
     */
    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    /**
     * @return the timeStamp
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * @param timeStamp the timeStamp to set
     */
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * @return the ID
     */
    public Integer getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(Integer ID) {
        this.ID = ID;
    }

    /**
     * @return the locationID
     */
    public Integer getLocationID() {
        return locationID;
    }

    /**
     * @param locationID the locationID to set
     */
    public void setLocationID(Integer locationID) {
        this.locationID = locationID;
    }

}
