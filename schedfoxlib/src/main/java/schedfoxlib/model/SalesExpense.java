/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import schedfoxlib.controller.SalesControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class SalesExpense implements java.io.Serializable {

    private Integer salesExpenseId;
    private Integer salesExpenseTypeId;
    private Integer userId;
    private BigDecimal expenseAmount;
    private Integer userApprovedBy;
    private Date dateOfExpense;
    private String notes;
    private Integer expApprovedBy;
    private Date expApprovedOn;
    private Boolean active;
    private static HashMap<Integer, SalesExpenseType> expenseTypes;
    private ArrayList<SalesExpenseImage> images;

    public SalesExpense() {
    }

    public SalesExpense(Record_Set rst) {
        try {
            this.salesExpenseId = rst.getInt("sales_expense_id");
        } catch (Exception exe) {
        }
        try {
            this.salesExpenseTypeId = rst.getInt("sales_expense_type_id");
        } catch (Exception exe) {
        }
        try {
            this.userId = rst.getInt("user_id");
        } catch (Exception exe) {
        }
        try {
            this.expenseAmount = rst.getBigDecimal("expense_amount");
        } catch (Exception exe) {
        }
        try {
            this.userApprovedBy = rst.getInt("user_approved_by");
        } catch (Exception exe) {
        }
        try {
            this.dateOfExpense = rst.getTimestamp("date_of_expense");
        } catch (Exception exe) {
        }
        try {
            this.notes = rst.getString("notes");
        } catch (Exception exe) {
        }
        try {
            this.expApprovedBy = rst.getInt("exp_approved_by");
        } catch (Exception exe) {
        }
        try {
            this.expApprovedOn = rst.getTimestamp("exp_approved_on");
        } catch (Exception exe) {
        }
        try {
            this.active = rst.getBoolean("active");
        } catch (Exception exe) {}
    }

    public ArrayList<SalesExpenseImage> getSalesImages(String companyId) {
        if (images == null) {
            SalesControllerInterface salesController = ControllerRegistryAbstract.getSalesController(companyId);
            try {
                images = salesController.getImagesForExpense(this.salesExpenseId, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return images;
    }

    public SalesExpenseType getExpenseType(String companyId) {
        if (salesExpenseTypeId != null) {
            if (expenseTypes == null) {
                SalesControllerInterface salesController = ControllerRegistryAbstract.getSalesController(companyId);
                try {
                    ArrayList<SalesExpenseType> types = salesController.getSalesExpenseTypes();
                    expenseTypes = new HashMap<Integer, SalesExpenseType>();
                    for (int t = 0; t < types.size(); t++) {
                        expenseTypes.put(types.get(t).getSalesExpenseTypeId(), types.get(t));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return expenseTypes.get(this.getSalesExpenseTypeId());
        }
        return null;
    }

    /**
     * @return the salesExpenseId
     */
    public Integer getSalesExpenseId() {
        return salesExpenseId;
    }

    /**
     * @param salesExpenseId the salesExpenseId to set
     */
    public void setSalesExpenseId(Integer salesExpenseId) {
        this.salesExpenseId = salesExpenseId;
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
     * @return the expenseAmount
     */
    public BigDecimal getExpenseAmount() {
        return expenseAmount;
    }

    /**
     * @param expenseAmount the expenseAmount to set
     */
    public void setExpenseAmount(BigDecimal expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    /**
     * @return the userApprovedBy
     */
    public Integer getUserApprovedBy() {
        return userApprovedBy;
    }

    /**
     * @param userApprovedBy the userApprovedBy to set
     */
    public void setUserApprovedBy(Integer userApprovedBy) {
        this.userApprovedBy = userApprovedBy;
    }

    /**
     * @return the dateOfExpense
     */
    public Date getDateOfExpense() {
        return dateOfExpense;
    }

    /**
     * @param dateOfExpense the dateOfExpense to set
     */
    public void setDateOfExpense(Date dateOfExpense) {
        this.dateOfExpense = dateOfExpense;
    }

    /**
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return the salesExpenseTypeId
     */
    public Integer getSalesExpenseTypeId() {
        return salesExpenseTypeId;
    }

    /**
     * @param salesExpenseTypeId the salesExpenseTypeId to set
     */
    public void setSalesExpenseTypeId(Integer salesExpenseTypeId) {
        this.salesExpenseTypeId = salesExpenseTypeId;
    }

    /**
     * @return the expApprovedBy
     */
    public Integer getExpApprovedBy() {
        return expApprovedBy;
    }

    /**
     * @param expApprovedBy the expApprovedBy to set
     */
    public void setExpApprovedBy(Integer expApprovedBy) {
        this.expApprovedBy = expApprovedBy;
    }

    /**
     * @return the expApprovedOn
     */
    public Date getExpApprovedOn() {
        return expApprovedOn;
    }

    /**
     * @param expApprovedOn the expApprovedOn to set
     */
    public void setExpApprovedOn(Date expApprovedOn) {
        this.expApprovedOn = expApprovedOn;
    }

    public boolean isApproved() {
        return this.expApprovedOn != null;
    }

    public void setApproved(boolean approved) {
    }

    /**
     * @return the images
     */
    public ArrayList<SalesExpenseImage> getImages() {
        return images;
    }

    /**
     * @param aImages the images to set
     */
    public void setImages(ArrayList<SalesExpenseImage> aImages) {
        images = aImages;
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
}
