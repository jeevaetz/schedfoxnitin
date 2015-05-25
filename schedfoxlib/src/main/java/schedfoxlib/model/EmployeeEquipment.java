/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.util.Date;
import schedfoxlib.controller.EmployeeControllerInterface;
import schedfoxlib.controller.EquipmentControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;
import schedfoxlib.model.util.Record_Set;
/**
 *
 * @author user
 */
public class EmployeeEquipment implements EntityEquipment, Serializable {
    private static final long serialVersionUID = 1L;

    private Integer employeeEquipmentId;
    private Integer employeeId;
    private Integer equipmentId;
    private Date dateIssued;
    private int issuedBy;
    private Date dateReturned;
    private Integer receivedBy;
    private Integer returnedCondition;
    private Date returnWaivedOn;
    private Integer returnWaivedBy;

    //Lazy Loaded objects
    private Employee emp;
    private Equipment equipment;
    private EmployeeDeductions empDeduct;

    public EmployeeEquipment() {
    }

    public EmployeeEquipment(Record_Set rst) {
        try {
            employeeEquipmentId = rst.getInt("employee_equipment_id");
        } catch (Exception e) {}
        try {
            employeeId = rst.getInt("employee_id");
        } catch (Exception e) {}
        try {
            equipmentId = rst.getInt("equipment_id");
        } catch (Exception e) {}
        try {
            dateIssued = rst.getDate("date_issued");
        } catch (Exception e) {}
        try {
            issuedBy = rst.getInt("issued_by");
        } catch (Exception e) {}
        try {
            dateReturned = rst.getDate("date_returned");
        } catch (Exception e) {}
        try {
            receivedBy = rst.getInt("received_by");
        } catch (Exception e) {}
        try {
            returnedCondition = rst.getInt("returned_condition");
        } catch (Exception e) {}
        try {
            if (rst.hasColumn("employee_first_name")) {
                this.emp = new Employee(new Date(), rst);
            }
        } catch (Exception e) {}
        try {
            this.returnWaivedBy = rst.getInt("return_waived_by");
        } catch (Exception e) {}
        try {
            this.returnWaivedOn = rst.getDate("return_waived_on");
        } catch (Exception e) {}
        try {
            this.equipment = new Equipment(rst);
        } catch (Exception e) {}
        try {
            this.empDeduct = new EmployeeDeductions(rst);
        } catch (Exception e) {}
    }

    public EmployeeEquipment(Integer employeeEquipmentId) {
        this.employeeEquipmentId = employeeEquipmentId;
    }

    public EmployeeEquipment(Integer employeeEquipmentId, int employeeId, int equipmentId, Date dateIssued, int issuedBy) {
        this.employeeEquipmentId = employeeEquipmentId;
        this.employeeId = employeeId;
        this.equipmentId = equipmentId;
        this.dateIssued = dateIssued;
        this.issuedBy = issuedBy;
    }

    public Integer getEmployeeEquipmentId() {
        return employeeEquipmentId;
    }

    public void setEmployeeEquipmentId(Integer employeeEquipmentId) {
        this.employeeEquipmentId = employeeEquipmentId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public Integer getEquipmentId() {
        return equipmentId;
    }

    @Override
    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    @Override
    public Date getDateIssued() {
        return dateIssued;
    }

    @Override
    public void setDateIssued(Date dateIssued) {
        this.dateIssued = dateIssued;
    }

    @Override
    public Integer getIssuedBy() {
        return issuedBy;
    }

    @Override
    public void setIssuedBy(Integer issuedBy) {
        this.issuedBy = issuedBy;
    }

    @Override
    public Date getDateReturned() {
        return dateReturned;
    }

    @Override
    public void setDateReturned(Date dateReturned) {
        this.dateReturned = dateReturned;
    }

    public Integer getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(Integer receivedBy) {
        this.receivedBy = receivedBy;
    }

    public Integer getReturnedCondition() {
        return returnedCondition;
    }

    public void setReturnedCondition(Integer returnedCondition) {
        this.returnedCondition = returnedCondition;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeEquipmentId != null ? employeeEquipmentId.hashCode() : 0);
        return hash;
    }

    public Employee getEmployee(String companyId) {
        if (emp == null) {
            EmployeeControllerInterface empController = ControllerRegistryAbstract.getEmployeeController(companyId);
            try {
                emp = empController.getEmployeeById(employeeId);
            } catch (Exception e) {}
        }
        return emp;
    }

    /**
     * @return the equipment
     */
    public Equipment getEquipment() {
        return equipment;
    }

    public EmployeeDeductions getDeduction() {
        return this.empDeduct;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmployeeEquipment)) {
            return false;
        }
        EmployeeEquipment other = (EmployeeEquipment) object;
        if ((this.employeeEquipmentId == null && other.employeeEquipmentId != null) || (this.employeeEquipmentId != null && !this.employeeEquipmentId.equals(other.employeeEquipmentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.EmployeeEquipment[employeeEquipmentId=" + employeeEquipmentId + "]";
    }

    @Override
    public Integer getId() {
        return this.getEmployeeEquipmentId();
    }

    @Override
    public void setId(Integer id) {
        this.setEmployeeEquipmentId(id);
    }

    @Override
    public Integer getEntityId() {
        return this.getEmployeeId();
    }

    @Override
    public void setEntityId(Integer entityId) {
        this.setEmployeeId(entityId);
    }

    @Override
    public String getUniqueId() {
        return "";
    }

    @Override
    public void setUniqueId(String uniqueId) {
       
    }

    @Override
    public Entity getEntity(String companyId) {
        return this.getEmployee(companyId);
    }

    @Override
    public Equipment getEquipment(String companyId) {
        if (equipment == null) {
            EquipmentControllerInterface equipController = ControllerRegistryAbstract.getEquipmentController(companyId);
            try {
                equipment = equipController.getEquipmentById(equipmentId);
            } catch (Exception e) {}
        }
        return equipment;

    }

    @Override
    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    /**
     * @return the returnWaivedOn
     */
    public Date getReturnWaivedOn() {
        return returnWaivedOn;
    }

    /**
     * @param returnWaivedOn the returnWaivedOn to set
     */
    public void setReturnWaivedOn(Date returnWaivedOn) {
        this.returnWaivedOn = returnWaivedOn;
    }

    /**
     * @return the returnWaivedBy
     */
    public Integer getReturnWaivedBy() {
        return returnWaivedBy;
    }

    /**
     * @param returnWaivedBy the returnWaivedBy to set
     */
    public void setReturnWaivedBy(Integer returnWaivedBy) {
        this.returnWaivedBy = returnWaivedBy;
    }

    

}
