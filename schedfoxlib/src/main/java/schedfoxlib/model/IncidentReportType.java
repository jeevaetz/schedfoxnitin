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
public class IncidentReportType implements Serializable {
    private int incidentReportTypeId;
    private String reportType;
    private Boolean active;

    public IncidentReportType() {
        
    }
    
    public IncidentReportType(Record_Set rst) {
        try {
            incidentReportTypeId = rst.getInt("incident_report_type_id");
        } catch (Exception e) {}
        try {
            reportType = rst.getString("report_type");
        } catch (Exception e) {}
        try {
            active = rst.getBoolean("active");
        } catch (Exception e) {}
    }
    
    /**
     * @return the incidentReportTypeId
     */
    public int getIncidentReportTypeId() {
        return incidentReportTypeId;
    }

    /**
     * @param incidentReportTypeId the incidentReportTypeId to set
     */
    public void setIncidentReportTypeId(int incidentReportTypeId) {
        this.incidentReportTypeId = incidentReportTypeId;
    }

    /**
     * @return the reportType
     */
    public String getReportType() {
        return reportType;
    }

    /**
     * @param reportType the reportType to set
     */
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
    
    @Override
    public boolean equals(Object obj) {
        boolean retVal = false;
        if (obj instanceof IncidentReportType) {
            IncidentReportType compType = (IncidentReportType)obj;
            retVal = this.getIncidentReportTypeId() == compType.getIncidentReportTypeId();
        }
        return retVal;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + this.incidentReportTypeId;
        return hash;
    }
    
    @Override
    public String toString() {
        return reportType;
    }

    /**
     * @return the active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(Boolean active) {
        this.active = active;
    }
}
