/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import schedfoxlib.controller.EmployeeControllerInterface;
import schedfoxlib.controller.OfficerDailyReportControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class OfficerDailyReport implements Serializable {

    private Integer officerDailyReportId;
    private Integer employee_id;
    private Integer clientEquipmentId;
    private Integer clientId;
    private Integer shiftId;
    private Date loggedIn;
    private Date loggedOut;
    private Boolean active;
    
    //Lazy Loaded Objects
    private Employee officer;
    private Client client;
    private ArrayList<OfficerDailyReportText> reportTexts;
    private ClientEquipment clientEquipment;
    
    //Not always populated
    private Integer incidentCount;
    private Integer waypointScan;

    public OfficerDailyReport() {
        this.active = true;
    }

    public OfficerDailyReport(Record_Set rst) {
        try {
            officerDailyReportId = rst.getInt("officer_daily_report_id");
        } catch (Exception exe) {
        }
        try {
            employee_id = rst.getInt("employee_id");
        } catch (Exception exe) {
        }
        try {
            clientId = rst.getInt("client_id");
        } catch (Exception exe) {
        }
        try {
            clientEquipmentId = rst.getInt("client_equipment_id");
        } catch (Exception exe) {
        }
        try {
            shiftId = rst.getInt("shift_id");
        } catch (Exception exe) {
        }
        try {
            loggedIn = rst.getTimestamp("logged_in");
        } catch (Exception exe) {
        }
        try {
            loggedOut = rst.getTimestamp("logged_out");
        } catch (Exception exe) {
        }
        try {
            active = rst.getBoolean("active");
        } catch (Exception exe) {}
        try {
            incidentCount = rst.getInt("incident_count");
        } catch (Exception exe) {}
        try {
            waypointScan = rst.getInt("waypoint_count");
        } catch (Exception exe) {}
    }

    /**
     * @return the officerDailyReportId
     */
    public Integer getOfficerDailyReportId() {
        return officerDailyReportId;
    }

    /**
     * @param officerDailyReportId the officerDailyReportId to set
     */
    public void setOfficerDailyReportId(Integer officerDailyReportId) {
        this.officerDailyReportId = officerDailyReportId;
    }

    /**
     * @return the employee_id
     */
    public Integer getEmployee_id() {
        return employee_id;
    }

    /**
     * @param employee_id the employee_id to set
     */
    public void setEmployee_id(Integer employee_id) {
        this.employee_id = employee_id;
    }

    /**
     * @return the clientId
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the shiftId
     */
    public Integer getShiftId() {
        return shiftId;
    }

    /**
     * @param shiftId the shiftId to set
     */
    public void setShiftId(Integer shiftId) {
        this.shiftId = shiftId;
    }

    /**
     * Lazy loads our officer object - if it has not been loaded yet
     * @param companyId
     * @return 
     */
    public Employee getOfficer(String companyId) {
        if (this.getOfficer() == null) {
            try {
                EmployeeControllerInterface userController = ControllerRegistryAbstract.getEmployeeController(companyId);
                this.setOfficer(userController.getEmployeeById(this.employee_id));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.getOfficer();
    }
    
    public ArrayList<OfficerDailyReportText> getReportTexts(String companyId) {
        if (this.reportTexts == null) {
            try {
                OfficerDailyReportControllerInterface officerController = ControllerRegistryAbstract.getOfficerDailyReportController(companyId);
                this.reportTexts = officerController.getTextForReport(this.officerDailyReportId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.reportTexts;
    }

    public ArrayList<OfficerDailyReportText> getReportTexts() {
        return this.reportTexts;
    }
    
    public void setReportTexts(ArrayList<OfficerDailyReportText> reportTexts) {
        this.reportTexts = reportTexts;
    }

    /**
     * @return the loggedIn
     */
    public Date getLoggedIn() {
        return loggedIn;
    }

    /**
     * @param loggedIn the loggedIn to set
     */
    public void setLoggedIn(Date loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
     * @return the loggedOut
     */
    public Date getLoggedOut() {
        return loggedOut;
    }

    /**
     * @param loggedOut the loggedOut to set
     */
    public void setLoggedOut(Date loggedOut) {
        this.loggedOut = loggedOut;
    }

    /**
     * @return the officer
     */
    public Employee getOfficer() {
        return officer;
    }

    /**
     * @param officer the officer to set
     */
    public void setOfficer(Employee officer) {
        this.officer = officer;
    }

    /**
     * @return the client_equipment_id
     */
    public Integer getClientEquipmentId() {
        return clientEquipmentId;
    }

    /**
     * @param client_equipment_id the client_equipment_id to set
     */
    public void setClientEquipmentId(Integer clientEquipmentId) {
        this.clientEquipmentId = clientEquipmentId;
    }

    /**
     * @return the clientEquipment
     */
    public ClientEquipment getClientEquipment() {
        return clientEquipment;
    }

    /**
     * @param clientEquipment the clientEquipment to set
     */
    public void setClientEquipment(ClientEquipment clientEquipment) {
        this.clientEquipment = clientEquipment;
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

    /**
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * @return the incidentCount
     */
    public Integer getIncidentCount() {
        return incidentCount;
    }

    /**
     * @param incidentCount the incidentCount to set
     */
    public void setIncidentCount(Integer incidentCount) {
        this.incidentCount = incidentCount;
    }

    /**
     * @return the waypointScan
     */
    public Integer getWaypointScan() {
        return waypointScan;
    }

    /**
     * @param waypointScan the waypointScan to set
     */
    public void setWaypointScan(Integer waypointScan) {
        this.waypointScan = waypointScan;
    }

}
