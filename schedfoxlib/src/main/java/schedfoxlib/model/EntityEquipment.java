/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author user
 */
public interface EntityEquipment extends Serializable {
   
    /**
     * @return the primary key
     */
    public Integer getId();
    public void setId(Integer id);

    /**
     * @return the clientId
     */
    public Integer getEntityId();
    public void setEntityId(Integer entityId);

    /**
     * @return the equipmentId
     */
    public Integer getEquipmentId();
    public void setEquipmentId(Integer equipmentId);

    /**
     * @return the dateIssued
     */
    public Date getDateIssued();
    public void setDateIssued(Date dateIssued);

    /**
     * @return the issuedBy
     */
    public Integer getIssuedBy();
    public void setIssuedBy(Integer issuedBy);

    /**
     * @return the dateReturned
     */
    public Date getDateReturned();
    public void setDateReturned(Date dateReturned);

    /**
     * @return the receivedBy
     */
    public Integer getReceivedBy();
    public void setReceivedBy(Integer receivedBy);

    /**
     * @return the returnedCondition
     */
    public Integer getReturnedCondition();
    public void setReturnedCondition(Integer returnedCondition);

    /**
     * @return the uniqueId
     */
    public String getUniqueId();
    public void setUniqueId(String uniqueId);
    
    public Entity getEntity(String companyId);
    public Equipment getEquipment(String companyId);
    public void setEquipment(Equipment equipment);
}
