/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.math.BigDecimal;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class Equipment implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer equipmentId;
    private String equipmentName;
    private BigDecimal equipmentCost;
    private BigDecimal equipmentCharge;
    private Boolean active;
    private Boolean shouldBeReturned;
    private Boolean generatesDeduction;
    private Boolean isEmployeeEquip;
    private Boolean isClientEquip;

    public Equipment() {
    }

    public Equipment(Record_Set rst) {
        try {
            equipmentId = rst.getInt("equipment_id");
        } catch (Exception e) {}
        try {
            equipmentName = rst.getString("equipment_name");
        } catch (Exception e) {}
        try {
            equipmentCost = rst.getBigDecimal("equipment_cost");
        } catch (Exception e) {}
        try {
            equipmentCharge = rst.getBigDecimal("equipment_charge");
        } catch (Exception e) {}
        try {
            active = rst.getBoolean("active");
        } catch (Exception e) {}
        try {
            shouldBeReturned = rst.getBoolean("should_be_returned");
        } catch (Exception e) {}
        try {
            generatesDeduction = rst.getBoolean("generates_deduction");
        } catch (Exception e) {}
        try {
            isEmployeeEquip = rst.getBoolean("is_employee_equip");
        } catch (Exception e) {}
        try {
            isClientEquip = rst.getBoolean("is_client_equip");
        } catch (Exception e) {}
    }

    public Equipment(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Equipment(Integer equipmentId, String equipmentName) {
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
    }

    public Integer getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public BigDecimal getEquipmentCost() {
        return equipmentCost;
    }

    public void setEquipmentCost(BigDecimal equipmentCost) {
        this.equipmentCost = equipmentCost;
    }

    public BigDecimal getEquipmentCharge() {
        return equipmentCharge;
    }

    public void setEquipmentCharge(BigDecimal equipmentCharge) {
        this.equipmentCharge = equipmentCharge;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getShouldBeReturned() {
        return shouldBeReturned;
    }

    public void setShouldBeReturned(Boolean shouldBeReturned) {
        this.shouldBeReturned = shouldBeReturned;
    }

    public Boolean getGeneratesDeduction() {
        return generatesDeduction;
    }

    public void setGeneratesDeduction(Boolean generatesDeduction) {
        this.generatesDeduction = generatesDeduction;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (equipmentId != null ? equipmentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Equipment)) {
            return false;
        }
        Equipment other = (Equipment) object;
        if ((this.equipmentId == null && other.equipmentId != null) || (this.equipmentId != null && !this.equipmentId.equals(other.equipmentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.equipmentName;
    }

    /**
     * @return the isEmployeeEquip
     */
    public Boolean getIsEmployeeEquip() {
        return isEmployeeEquip;
    }

    /**
     * @param isEmployeeEquip the isEmployeeEquip to set
     */
    public void setIsEmployeeEquip(Boolean isEmployeeEquip) {
        this.isEmployeeEquip = isEmployeeEquip;
    }

    /**
     * @return the isClientEquip
     */
    public Boolean getIsClientEquip() {
        return isClientEquip;
    }

    /**
     * @param isClientEquip the isClientEquip to set
     */
    public void setIsClientEquip(Boolean isClientEquip) {
        this.isClientEquip = isClientEquip;
    }

}
