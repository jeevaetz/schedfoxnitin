package schedfoxlib.model;

import java.io.Serializable;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 * Object that represents the data contained in the "checkin" table that is used to control check in and out
 * of the SchedFox system
 *
 * @author user
 */

public class CheckIn implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer checkInId;
    private int personCheckedOut;
    private long timeStampOut;
    private String shiftId;
    private Date checkinDate;
    private int startTime;
    private int endTime;
    private long timeStamp;
    private int personCheckedIn;
    private int employeeId;
    private Date checkinLastUpdated;
    //Added by dja for IVR
    private boolean checkedIn;
    private boolean checkedOut;


    public CheckIn() {
    }

    public CheckIn(Integer checkinId) {
        this.checkInId = checkinId;
    }

    public CheckIn(Integer checkinId, int personCheckedOut, long timeStampOut, String shiftId, Date checkinDate, int startTime, int endTime, long timeStamp, int personCheckedIn, int employeeId, Date checkinLastUpdated) {
        this.checkInId = checkinId;
        this.personCheckedOut = personCheckedOut;
        this.timeStampOut = timeStampOut;
        this.shiftId = shiftId;
        this.checkinDate = checkinDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeStamp = timeStamp;
        this.personCheckedIn = personCheckedIn;
        this.employeeId = employeeId;
        this.checkinLastUpdated = checkinLastUpdated;
    }

    public CheckIn(Record_Set rst){
        if(rst != null){
            if (rst.hasColumn("checkin_id")) {
                this.checkInId = rst.getInt("checkin_id");
            }
            if (rst.hasColumn("person_checked_out")) {
                this.personCheckedOut = rst.getInt("person_checked_out");
            }
            if (rst.hasColumn("time_stmp_out")) {
                String tmp = rst.getString("time_stmp_out");
                long l = 0;
                if(tmp != null){
                    try{
                        l = Long.parseLong(tmp);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    this.timeStampOut = l;
                }

            }
            if (rst.hasColumn("shift_id")) {
                this.shiftId = rst.getString("shift_id");
            }
            if (rst.hasColumn("checkin_date")) {
                this.checkinDate = rst.getDate("checkin_date");
            }
            if (rst.hasColumn("start_time")) {
                this.startTime = rst.getInt("start_time");
            }
            if (rst.hasColumn("end_time")) {
                this.endTime = rst.getInt("end_time");
            }
            if (rst.hasColumn("time_stamp")) {
                String tmp = rst.getString("time_stamp");
                long l = 0;
                if(tmp != null){
                    try{
                        l = Long.parseLong(tmp);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    this.timeStamp = l;
                }
            }

            if (rst.hasColumn("person_checked_in")) {
                this.personCheckedIn = rst.getInt("person_checked_in");
            }
            if (rst.hasColumn("employee_id")) {
                this.employeeId = rst.getInt("employee_id");
            }
            if (rst.hasColumn("checkin_last_updated")) {
                this.checkinLastUpdated = rst.getDate("checkin_last_updated");
            }
        }

    }

    public Integer getCheckinId() {
        return checkInId;
    }

    public void setCheckInId(Integer checkinId) {
        this.checkInId = checkinId;
    }

    public int getPersonCheckedOut() {
        return personCheckedOut;
    }

    public void setPersonCheckedOut(int personCheckedOut) {
        this.personCheckedOut = personCheckedOut;
    }

    public long getTimeStampOut() {
        return timeStampOut;
    }

    public void setTimeStampOut(long timeStampOut) {
        this.timeStampOut = timeStampOut;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public Date getCheckInDate() {
        return checkinDate;
    }

    public void setCheckInDate(Date checkinDate) {
        this.checkinDate = checkinDate;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getPersonCheckedIn() {
        return personCheckedIn;
    }

    public void setPersonCheckedIn(int personCheckedIn) {
        this.personCheckedIn = personCheckedIn;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Date getCheckInLastUpdated() {
        return checkinLastUpdated;
    }

    public void setCheckInLastUpdated(Date checkinLastUpdated) {
        this.checkinLastUpdated = checkinLastUpdated;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (checkInId != null ? checkInId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CheckIn)) {
            return false;
        }
        CheckIn other = (CheckIn) object;
        if ((this.checkInId == null && other.checkInId != null) || (this.checkInId != null && !this.checkInId.equals(other.checkInId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.CheckIn[checkinId=" + checkInId + "]";
    }

    /**
     * @return the checkedIn
     */
    public boolean isCheckedIn() {
        return checkedIn;
    }

    /**
     * @param checkedIn the checkedIn to set
     */
    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    /**
     * @return the checkedOut
     */
    public boolean isCheckedOut() {
        return checkedOut;
    }

    /**
     * @param checkedOut the checkedOut to set
     */
    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }

}
