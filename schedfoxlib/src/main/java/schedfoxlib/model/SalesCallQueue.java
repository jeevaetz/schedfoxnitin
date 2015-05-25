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
public class SalesCallQueue {
    private Integer salesCallQueueId;
    private Integer userId;
    private Integer contactType;
    private Integer contactId;
    
    public SalesCallQueue() {
        
    }

    public SalesCallQueue(Record_Set rst) {
        salesCallQueueId = rst.getInt("sales_call_queue_id");
        userId = rst.getInt("user_id");
        contactType = rst.getInt("contact_type");
        contactId = rst.getInt("contact_id");
    }
    
    /**
     * @return the salesCallQueueId
     */
    public Integer getSalesCallQueueId() {
        return salesCallQueueId;
    }

    /**
     * @param salesCallQueueId the salesCallQueueId to set
     */
    public void setSalesCallQueueId(Integer salesCallQueueId) {
        this.salesCallQueueId = salesCallQueueId;
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
     * @return the contactType
     */
    public Integer getContactType() {
        return contactType;
    }

    /**
     * @param contactType the contactType to set
     */
    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    /**
     * @return the contactId
     */
    public Integer getContactId() {
        return contactId;
    }

    /**
     * @param contactId the contactId to set
     */
    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }
}
