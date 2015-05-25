/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class SalesItinerary implements Serializable {
    private Integer salesItineraryId;
    private Integer userId;
    private String subject;
    private Date dateOfItinerary;
    private Integer stimeOfItinerary;
    private Integer etimeOfItinerary;
    private Integer salesItineraryTypeId;
    private Date dateEntered;
    private String externalGid;
    private String meetingText;
    private Boolean active;

    private ArrayList<SalesItineraryImages> images;
    private ArrayList<SalesItineraryAudio> audioNotes;
    
    public SalesItinerary() {
        
    }
    
    public SalesItinerary(Record_Set rst) {
        try {
            salesItineraryId = rst.getInt("sales_itinerary_id");
        } catch (Exception exe) {}
        try {
            userId = rst.getInt("user_id");
        } catch (Exception exe) {}
        try {
            subject = rst.getString("subject");
        } catch (Exception exe) {}
        try {
            dateOfItinerary = rst.getDate("date_of_itinerary");
        } catch (Exception exe) {}
        try {
            stimeOfItinerary = rst.getInt("stime_of_itinerary");
        } catch (Exception exe) {}
        try {
            etimeOfItinerary = rst.getInt("etime_of_itinerary");
        } catch (Exception exe) {}
        try {
            salesItineraryTypeId = rst.getInt("sales_itinerary_type_id");
        } catch (Exception exe) {}
        try {
            dateEntered = rst.getDate("date_entered");
        } catch (Exception exe) {}
        try {
            meetingText = rst.getString("meeting_text");
        } catch (Exception exe) {}
        try {
            active = rst.getBoolean("active");
        } catch (Exception exe) {}
    }
    
    /**
     * @return the salesItineraryId
     */
    public Integer getSalesItineraryId() {
        return salesItineraryId;
    }

    /**
     * @param salesItineraryId the salesItineraryId to set
     */
    public void setSalesItineraryId(Integer salesItineraryId) {
        this.salesItineraryId = salesItineraryId;
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
     * @return the dateOfItinerary
     */
    public Date getDateOfItinerary() {
        return dateOfItinerary;
    }

    /**
     * @param dateOfItinerary the dateOfItinerary to set
     */
    public void setDateOfItinerary(Date dateOfItinerary) {
        this.dateOfItinerary = dateOfItinerary;
    }

    /**
     * @return the stimeOfItinerary
     */
    public Integer getStimeOfItinerary() {
        return stimeOfItinerary;
    }

    /**
     * @param stimeOfItinerary the stimeOfItinerary to set
     */
    public void setStimeOfItinerary(Integer stimeOfItinerary) {
        this.stimeOfItinerary = stimeOfItinerary;
    }

    /**
     * @return the etimeOfItinerary
     */
    public Integer getEtimeOfItinerary() {
        return etimeOfItinerary;
    }

    /**
     * @param etimeOfItinerary the etimeOfItinerary to set
     */
    public void setEtimeOfItinerary(Integer etimeOfItinerary) {
        this.etimeOfItinerary = etimeOfItinerary;
    }

    /**
     * @return the salesItineraryTypeId
     */
    public Integer getSalesItineraryTypeId() {
        return salesItineraryTypeId;
    }

    /**
     * @param salesItineraryTypeId the salesItineraryTypeId to set
     */
    public void setSalesItineraryTypeId(Integer salesItineraryTypeId) {
        this.salesItineraryTypeId = salesItineraryTypeId;
    }

    /**
     * @return the dateEntered
     */
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
     * @return the images
     */
    public ArrayList<SalesItineraryImages> getImages() {
        return images;
    }

    /**
     * @param images the images to set
     */
    public void setImages(ArrayList<SalesItineraryImages> images) {
        this.images = images;
    }

    /**
     * @return the audioNotes
     */
    public ArrayList<SalesItineraryAudio> getAudioNotes() {
        return audioNotes;
    }

    /**
     * @param audioNotes the audioNotes to set
     */
    public void setAudioNotes(ArrayList<SalesItineraryAudio> audioNotes) {
        this.audioNotes = audioNotes;
    }

    /**
     * @return the externalGid
     */
    public String getExternalGid() {
        return externalGid;
    }

    /**
     * @param externalGid the externalGid to set
     */
    public void setExternalGid(String externalGid) {
        this.externalGid = externalGid;
    }

    /**
     * @return the meetingText
     */
    public String getMeetingText() {
        return meetingText;
    }

    /**
     * @param meetingText the meetingText to set
     */
    public void setMeetingText(String meetingText) {
        this.meetingText = meetingText;
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
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }
}
