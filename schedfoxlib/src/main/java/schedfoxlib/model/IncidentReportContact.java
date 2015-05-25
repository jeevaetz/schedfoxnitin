/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import schedfoxlib.controller.IncidentReportControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class IncidentReportContact implements Serializable {
    private int incidentReportContactId;
    private int incidentReportId;
    private String firstName;
    private String lastName;
    private String cell;
    private String phone;
    private String phone2;
    private String address;
    private String address2;
    private String city;
    private String state;
    private String zip;

    //Lazy loaded objects
    private transient IncidentReport incidentReport;
    
    public IncidentReportContact() {
        
    }
    
    public IncidentReportContact(Record_Set rst) {
        try {
            this.incidentReportContactId = rst.getInt("incident_report_contact_id");
        } catch (Exception e) {}
        try {
            this.incidentReportId = rst.getInt("incident_report_id");
        } catch (Exception e) {}
        try {
            this.firstName = rst.getString("first_name");
        } catch (Exception e) {}
        try {
            this.lastName = rst.getString("last_name");
        } catch (Exception e) {}
        try {
            this.cell = rst.getString("cell");
        } catch (Exception e) {}
        try {
            this.phone = rst.getString("phone");
        } catch (Exception e) {}
        try {
            this.phone2 = rst.getString("phone2");
        } catch (Exception e) {}
        try {
            this.address = rst.getString("address");
        } catch (Exception e) {}
        try {
            this.address2 = rst.getString("address2");
        } catch (Exception e) {}
        try {
            this.city = rst.getString("city");
        } catch (Exception e) {}
        try {
            this.state = rst.getString("state");
        } catch (Exception e) {}
        try {
            this.zip = rst.getString("zip");
        } catch (Exception e) {}
    }
    
    /**
     * @return the incidentReportContactId
     */
    public int getIncidentReportContactId() {
        return incidentReportContactId;
    }

    /**
     * @param incidentReportContactId the incidentReportContactId to set
     */
    public void setIncidentReportContactId(int incidentReportContactId) {
        this.incidentReportContactId = incidentReportContactId;
    }

    /**
     * @return the incidentReportId
     */
    public int getIncidentReportId() {
        return incidentReportId;
    }

    /**
     * @param incidentReportId the incidentReportId to set
     */
    public void setIncidentReportId(int incidentReportId) {
        this.incidentReportId = incidentReportId;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the cell
     */
    public String getCell() {
        return cell;
    }

    /**
     * @param cell the cell to set
     */
    public void setCell(String cell) {
        this.cell = cell;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the phone2
     */
    public String getPhone2() {
        return phone2;
    }

    /**
     * @param phone2 the phone2 to set
     */
    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the address2
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * @param address2 the address2 to set
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the zip
     */
    public String getZip() {
        return zip;
    }

    /**
     * @param zip the zip to set
     */
    public void setZip(String zip) {
        this.zip = zip;
    }
    
    public IncidentReport getIncidentReport(String companyId) {
        if (this.incidentReport == null) {
            try {
                IncidentReportControllerInterface controller = ControllerRegistryAbstract.getIncidentReportInterface(companyId);
                this.incidentReport = controller.getIncidentReportById(this.incidentReportId);
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
        return this.incidentReport;
    }
}
