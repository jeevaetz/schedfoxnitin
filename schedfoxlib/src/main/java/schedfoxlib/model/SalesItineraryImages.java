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
public class SalesItineraryImages implements Serializable {
    private Integer salesItineraryImagesId;
    private Integer salesItineraryId;
    private byte[] image;
    
    public SalesItineraryImages() {
        
    }

    /**
     * @return the salesItineraryImagesId
     */
    public Integer getSalesItineraryImagesId() {
        return salesItineraryImagesId;
    }

    /**
     * @param salesItineraryImagesId the salesItineraryImagesId to set
     */
    public void setSalesItineraryImagesId(Integer salesItineraryImagesId) {
        this.salesItineraryImagesId = salesItineraryImagesId;
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
    public byte[] getImage() {
        return image;
    }

    /**
     * @param bytea the bytea to set
     */
    public void setImage(byte[] bytea) {
        this.image = bytea;
    }
}
