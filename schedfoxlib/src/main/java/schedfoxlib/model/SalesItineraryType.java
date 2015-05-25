/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class SalesItineraryType {
    private Integer salesItineraryTypeId;
    private String salesType;
    
    public SalesItineraryType() {
        
    }

    public SalesItineraryType(Record_Set rst) {
        salesItineraryTypeId = rst.getInt("sales_itinerary_type_id");
        salesType = rst.getString("sales_type");
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
     * @return the salesType
     */
    public String getSalesType() {
        return salesType;
    }

    /**
     * @param salesType the salesType to set
     */
    public void setSalesType(String salesType) {
        this.salesType = salesType;
    }
    
    @Override
    public String toString() {
        return this.salesType;
    }
}
