/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.math.BigDecimal;
import schedfoxlib.controller.BillingControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;

/**
 *
 * @author user
 */
public class GenericRateCode {
    private static final long serialVersionUID = 1L;

    private int rateCodeId;
    private BigDecimal payAmount;
    private BigDecimal overtimeAmount;
    private BigDecimal billAmount;
    private BigDecimal overtimeBill;
    private int hourType;
    private String description;

    //Lazy loaded objects
    private RateCode rateCode;
    private HourType hourTypeObj;

    public GenericRateCode() {

    }

    public int getPrimaryKey() {
        return -1;
    }

    public void setPrimaryKey(int primaryKey) {
        
    }

    public int getRateCodeId() {
        return rateCodeId;
    }

    public void setRateCodeId(int rateCodeId) {
        this.rateCodeId = rateCodeId;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getOvertimeAmount() {
        return overtimeAmount;
    }

    public void setOvertimeAmount(BigDecimal overtimeAmount) {
        this.overtimeAmount = overtimeAmount;
    }

    public BigDecimal getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(BigDecimal billAmount) {
        this.billAmount = billAmount;
    }

    public BigDecimal getOvertimeBill() {
        return overtimeBill;
    }

    public void setOvertimeBill(BigDecimal overtimeBill) {
        this.overtimeBill = overtimeBill;
    }

    public int getHourType() {
        return hourType;
    }

    public void setHourType(int hourType) {
        this.hourType = hourType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Lazy loaded rate code
     * @param company_id
     * @return
     */
    public RateCode getRateCode(int company_id) {
        if (this.rateCode == null) {
            BillingControllerInterface billingController = ControllerRegistryAbstract.getBillingController(company_id + "");
            try {
                this.rateCode = billingController.getRateCode(rateCodeId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rateCode;
    }

    public HourType getHourTypeObj(int company_id) {
        if (this.hourTypeObj == null) {
            BillingControllerInterface billingController = ControllerRegistryAbstract.getBillingController(company_id + "");
            try {
                this.hourTypeObj = billingController.getHourTypeObj(hourType);
            } catch (Exception e) {
                this.hourTypeObj = new HourType();
                this.hourTypeObj.setHourType("Not set");
                this.hourTypeObj.setHourTypeId(-1);
            }
        }
        return this.hourTypeObj;
    }
}
