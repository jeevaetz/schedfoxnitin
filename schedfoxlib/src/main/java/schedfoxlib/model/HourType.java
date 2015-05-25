/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class HourType {
    private Integer hourTypeId;
    private String hourType;

    public HourType() {
        
    }

    public HourType(Record_Set rst) {
        this.hourTypeId = rst.getInt("hour_type_id");
        this.hourType = rst.getString("hour_type");
    }

    /**
     * @return the hourTypeId
     */
    public Integer getHourTypeId() {
        return hourTypeId;
    }

    /**
     * @param hourTypeId the hourTypeId to set
     */
    public void setHourTypeId(Integer hourTypeId) {
        this.hourTypeId = hourTypeId;
    }

    /**
     * @return the hourType
     */
    public String getHourType() {
        return hourType;
    }

    /**
     * @param hourType the hourType to set
     */
    public void setHourType(String hourType) {
        this.hourType = hourType;
    }

    @Override
    public String toString() {
        return this.hourType;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HourType)) {
            return false;
        }
        HourType other = (HourType) object;
        if ((this.hourTypeId == null && other.hourTypeId != null) || (this.hourTypeId != null && !this.hourTypeId.equals(other.hourTypeId))) {
            return false;
        }
        return true;
    }
}
