/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class ClientEquipmentVendor implements Serializable {
    private Integer clientEquipmentVendorId;
    private String vendorName;
    
    public ClientEquipmentVendor() {
        
    }
    
    public ClientEquipmentVendor(Record_Set rst) {
        try {
            clientEquipmentVendorId = rst.getInt("client_equipment_vendor_id");
        } catch (Exception exe) {}
        try {
            vendorName = rst.getString("vendor_name");
        } catch (Exception exe) {}
    }

    /**
     * @return the clientEquipmentVendorId
     */
    public Integer getClientEquipmentVendorId() {
        return clientEquipmentVendorId;
    }

    /**
     * @param clientEquipmentVendorId the clientEquipmentVendorId to set
     */
    public void setClientEquipmentVendorId(Integer clientEquipmentVendorId) {
        this.clientEquipmentVendorId = clientEquipmentVendorId;
    }

    /**
     * @return the vendorName
     */
    public String getVendorName() {
        return vendorName;
    }

    /**
     * @param vendorName the vendorName to set
     */
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}
