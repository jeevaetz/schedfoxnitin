/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class AccessIndividuals implements Serializable {

    private Integer accessIndividualId;
    private Integer accessIndividualTypeId;
    private Integer clientId;
    private String firstName;
    private String lastName;
    private Boolean active;
    private Date startDateAccess;
    private Date endDateAccess;
    private Boolean tagWritten;

    public AccessIndividuals() {
        active = true;
    }

    public AccessIndividuals(Record_Set rst) {
        accessIndividualId = rst.getInt("access_individual_id");
        accessIndividualTypeId = rst.getInt("access_individual_type_id");
        firstName = rst.getString("first_name");
        lastName = rst.getString("last_name");
        active = rst.getBoolean("active");
        clientId = rst.getInt("client_id");
        try {
            startDateAccess = rst.getDate("start_date_access");
        } catch (Exception exe) {}
        try {
            endDateAccess = rst.getDate("end_date_access");
        } catch (Exception exe) {}
        tagWritten = rst.getBoolean("tag_written");
    }

    /**
     * @return the accessIndividualId
     */
    public Integer getAccessIndividualId() {
        return accessIndividualId;
    }

    /**
     * @param accessIndividualId the accessIndividualId to set
     */
    public void setAccessIndividualId(Integer accessIndividualId) {
        this.accessIndividualId = accessIndividualId;
    }

    /**
     * @return the accessIndividualTypeId
     */
    public Integer getAccessIndividualTypeId() {
        return accessIndividualTypeId;
    }

    /**
     * @param accessIndividualTypeId the accessIndividualTypeId to set
     */
    public void setAccessIndividualTypeId(Integer accessIndividualTypeId) {
        this.accessIndividualTypeId = accessIndividualTypeId;
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
     * @return the startDateAccess
     */
    public Date getStartDateAccess() {
        return startDateAccess;
    }

    /**
     * @param startDateAccess the startDateAccess to set
     */
    public void setStartDateAccess(Date startDateAccess) {
        this.startDateAccess = startDateAccess;
    }

    /**
     * @return the endDateAccess
     */
    public Date getEndDateAccess() {
        return endDateAccess;
    }

    /**
     * @param endDateAccess the endDateAccess to set
     */
    public void setEndDateAccess(Date endDateAccess) {
        this.endDateAccess = endDateAccess;
    }

    /**
     * @return the tagWritten
     */
    public Boolean getTagWritten() {
        return tagWritten;
    }

    /**
     * @param tagWritten the tagWritten to set
     */
    public void setTagWritten(Boolean tagWritten) {
        this.tagWritten = tagWritten;
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

}
