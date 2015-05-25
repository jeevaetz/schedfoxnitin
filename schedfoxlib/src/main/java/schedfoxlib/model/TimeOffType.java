/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class TimeOffType implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer timeOffTypeId;
    private String timeoff;

    public TimeOffType() {
    }

    public TimeOffType(Record_Set rst) {
        try {
            timeOffTypeId = rst.getInt("time_off_type_id");
        } catch (Exception e) {}
        try {
            timeoff = rst.getString("timeoff");
        } catch (Exception e) {}
    }

    public TimeOffType(Integer timeOffTypeId) {
        this.timeOffTypeId = timeOffTypeId;
    }

    public TimeOffType(Integer timeOffTypeId, String timeoff) {
        this.timeOffTypeId = timeOffTypeId;
        this.timeoff = timeoff;
    }

    public Integer getTimeOffTypeId() {
        return timeOffTypeId;
    }

    public void setTimeOffTypeId(Integer timeOffTypeId) {
        this.timeOffTypeId = timeOffTypeId;
    }

    public String getTimeoff() {
        return timeoff;
    }

    public void setTimeoff(String timeoff) {
        this.timeoff = timeoff;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (timeOffTypeId != null ? timeOffTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TimeOffType)) {
            return false;
        }
        TimeOffType other = (TimeOffType) object;
        if ((this.timeOffTypeId == null && other.timeOffTypeId != null) || (this.timeOffTypeId != null && !this.timeOffTypeId.equals(other.timeOffTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return timeoff;
    }

}
