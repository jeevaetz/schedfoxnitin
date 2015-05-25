/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import schedfoxlib.controller.EmployeeControllerInterface;
import schedfoxlib.controller.IncidentReportControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class IncidentReport extends OfficerCommunication implements Serializable, Comparable {
    private Integer incidentReportId;
    private Integer incidentReportTypeId;
    private Integer clientId;
    private Integer employeeId;
    private String shiftId;
    private String incident;
    private Date dateEntered;
    private Integer userId;
    private Boolean clientVisible;
    private String incidentNumberOverride;

    private Integer imageCount;
    
    //Lazy Loaded Objects
    private transient ArrayList<IncidentReportDocument> documents;
    private transient ArrayList<IncidentReportContact> contacts;
    private transient Employee officer;
    private IncidentReportType incidentType;
    
    public IncidentReport() {
        
    }
    
    public IncidentReport(Record_Set rst) {
        try {
            this.incidentReportId = rst.getInt("incident_report_id");
        } catch (Exception e) {}
        try {
            this.incidentReportTypeId = rst.getInt("incident_report_type_id");
        } catch (Exception e) {}
        try {
            this.clientId = rst.getInt("client_id");
        } catch (Exception e) {}
        try {
            this.employeeId = rst.getInt("employee_id");
        } catch (Exception e) {}
        try {
            this.shiftId = rst.getString("shift_id");
        } catch (Exception e) {}
        try {
            this.incident = rst.getString("incident");
        } catch (Exception e) {}
        try {
            this.dateEntered = rst.getTimestamp("date_entered");
        } catch (Exception e) {}
        try {
            this.userId = rst.getInt("user_id");
        } catch (Exception e) {}
        try {
            this.clientVisible = rst.getBoolean("client_visible");
        } catch (Exception e) {}
        try {
            this.imageCount = rst.getInt("image_count");
        } catch (Exception e) {}
        try {
            this.incidentNumberOverride = rst.getString("incident_number_override");
        } catch (Exception e) {}
    }
    
    public String getReadableIncidentId() {
        if (this.incidentNumberOverride == null || this.incidentNumberOverride.length() == 0) {
            return this.incidentReportId + "";
        }
        return this.incidentNumberOverride;
    }
    
    /**
     * @return the incidentReportId
     */
    public Integer getIncidentReportId() {
        return incidentReportId;
    }

    /**
     * @param incidentReportId the incidentReportId to set
     */
    public void setIncidentReportId(Integer incidentReportId) {
        this.incidentReportId = incidentReportId;
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
     * @return the employeeId
     */
    public Integer getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return the shiftId
     */
    public String getShiftId() {
        return shiftId;
    }

    /**
     * @param shiftId the shiftId to set
     */
    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    /**
     * @return the incident
     */
    public String getIncident() {
        return incident;
    }

    /**
     * @param incident the incident to set
     */
    public void setIncident(String incident) {
        this.incident = incident;
    }

    /**
     * @return the dateEntered
     */
    @Override
    public Date getDateEntered() {
        return dateEntered;
    }

    /**
     * @param dateEntered the dateEntered to set
     */
    public void setDateEntered(Date dateEntered) {
        this.dateEntered = dateEntered;
    }

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return the clientVisible
     */
    public Boolean getClientVisible() {
        return clientVisible;
    }

    /**
     * @param clientVisible the clientVisible to set
     */
    public void setClientVisible(Boolean clientVisible) {
        this.clientVisible = clientVisible;
    }
    
    public ArrayList<IncidentReportDocument> getIncidentReportDocuments() {
        return this.documents;
    }
    
    public void setIncidentReportDocuments(ArrayList<IncidentReportDocument> documents) {
        this.documents = documents;
    }
    
    public ArrayList<IncidentReportDocument> getIncidentReportDocuments(String companyId) {
        if (this.documents == null) {
            try {
                IncidentReportControllerInterface controller = ControllerRegistryAbstract.getIncidentReportInterface(companyId);
                this.documents = controller.getIncidentReportDocumentsForIncident(this.incidentReportId, false);
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
        return this.documents;
    }
    
    public ArrayList<IncidentReportContact> getIncidentReportContacts(String companyId) {
        if (this.contacts == null) {
            try {
                IncidentReportControllerInterface controller = ControllerRegistryAbstract.getIncidentReportInterface(companyId);
                if (this.incidentReportId != null) {
                    this.contacts = controller.getIncidentReportContactsForIncident(this.incidentReportId);
                } else {
                    this.contacts = new ArrayList<IncidentReportContact>();
                }
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
        return this.contacts;
    }

    /**
     * @return the incidentReportTypeId
     */
    public Integer getIncidentReportTypeId() {
        return incidentReportTypeId;
    }

    /**
     * @param incidentReportTypeId the incidentReportTypeId to set
     */
    public void setIncidentReportTypeId(Integer incidentReportTypeId) {
        this.incidentReportTypeId = incidentReportTypeId;
    }

    @Override
    public String getType() {
        return "Incident Report";
    }

    @Override
    public String getText() {
        return this.getIncident();
    }

    @Override
    public Integer getEnteredBy() {
        return this.getUserId();
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
     * Lazy loads our officer object - if it has not been loaded yet
     * @param companyId
     * @return 
     */
    public Employee getOfficer(String companyId) {
        if (this.getOfficer() == null) {
            try {
                EmployeeControllerInterface userController = ControllerRegistryAbstract.getEmployeeController(companyId);
                this.setOfficer(userController.getEmployeeById(this.employeeId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.getOfficer();
    }

    public IncidentReportType getIncidentType(String companyId) {
        if (this.incidentType == null) {
            try {
                IncidentReportControllerInterface incidentController = ControllerRegistryAbstract.getIncidentReportInterface(companyId);
                this.incidentType= incidentController.getIncidentReportTypeById(this.incidentReportTypeId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.incidentType;
    }
    
    /**
     * @return the incidentType
     */
    public IncidentReportType getIncidentType() {
        return incidentType;
    }

    /**
     * @param incidentType the incidentType to set
     */
    public void setIncidentType(IncidentReportType incidentType) {
        this.incidentType = incidentType;
    }

    @Override
    public Integer getId() {
        return this.incidentReportId;
    }

    /**
     * @return the imageCount
     */
    public Integer getImageCount() {
        return imageCount;
    }

    /**
     * @param imageCount the imageCount to set
     */
    public void setImageCount(Integer imageCount) {
        this.imageCount = imageCount;
    }

    @Override
    public ArrayList<ImageInterface> getImages(String companyId) {
        ArrayList<ImageInterface> retVal = new ArrayList<ImageInterface>();
        ArrayList<IncidentReportDocument> tempVal = this.getIncidentReportDocuments(companyId);
        if (tempVal != null) {
            retVal.addAll(tempVal);
        }
        return retVal;
    }

    @Override
    public int compareTo(Object obj) {
        try {
            IncidentReport compareTo = (IncidentReport)obj;
            return -1 * this.getDateEntered().compareTo(compareTo.getDateEntered());
        } catch (Exception exe) {
            return 1;
        }
    }

    /**
     * @return the incidentNumberOverride
     */
    public String getIncidentNumberOverride() {
        return incidentNumberOverride;
    }

    /**
     * @param incidentNumberOverride the incidentNumberOverride to set
     */
    public void setIncidentNumberOverride(String incidentNumberOverride) {
        this.incidentNumberOverride = incidentNumberOverride;
    }
}
