/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;

/**
 *
 * @author ira
 */
public class SalesItineraryAudio implements Serializable {
    private Integer salesItineraryAudioId;
    private Integer salesItineraryId;
    private byte[] audio;
    
    public SalesItineraryAudio() {
        
    }

    /**
     * @return the salesItineraryImagesId
     */
    public Integer getSalesItineraryAudioId() {
        return salesItineraryAudioId;
    }

    /**
     * @param salesItineraryImagesId the salesItineraryImagesId to set
     */
    public void setSalesItineraryAudioId(Integer salesItineraryImagesId) {
        this.salesItineraryAudioId = salesItineraryImagesId;
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
     * @return the bytea
     */
    public byte[] getAudio() {
        return audio;
    }

    /**
     * @param bytea the bytea to set
     */
    public void setAudio(byte[] bytea) {
        this.audio = bytea;
    }
}
