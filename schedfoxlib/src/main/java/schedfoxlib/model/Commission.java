/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.math.BigDecimal;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class Commission {
    private Integer commission_id;
    private String commission_name;
    private boolean isdefault;
    private boolean active;
    private BigDecimal first_pmt_sales_percentage;
    private BigDecimal first_year_sales_percentage;
    private BigDecimal first_year_manager_percentage;
    private BigDecimal second_year_sales_percentage;
    private BigDecimal second_year_manager_percentage;


    public Commission() {
        first_pmt_sales_percentage = new BigDecimal(0);
        first_year_sales_percentage = new BigDecimal(0);
        first_year_manager_percentage = new BigDecimal(0);
        second_year_sales_percentage = new BigDecimal(0);
        second_year_manager_percentage = new BigDecimal(0);
    }

    public Commission(Record_Set rst) {
        try {
            commission_id = rst.getInt("commission_id");
        } catch (Exception e) {}
        try {
            commission_name = rst.getString("commission_name");
        } catch (Exception e) {}
        try {
            isdefault = rst.getBoolean("isdefault");
        } catch (Exception e) {}
        try {
            active = rst.getBoolean("active");
        } catch (Exception e) {}
        try {
            first_pmt_sales_percentage = rst.getBigDecimal("first_pmt_sales_percentage");
        } catch (Exception e) {}
        try {
            first_year_sales_percentage = rst.getBigDecimal("first_year_sales_percentage");
        } catch (Exception e) {}
        try {
            first_year_manager_percentage = rst.getBigDecimal("first_year_manager_percentage");
        } catch (Exception e) {}
        try {
            second_year_sales_percentage = rst.getBigDecimal("second_year_sales_percentage");
        } catch (Exception e) {}
        try {
            second_year_manager_percentage = rst.getBigDecimal("second_year_manager_percentage");
        } catch (Exception e) {}
    }

    /**
     * @return the commission_id
     */
    public Integer getCommission_id() {
        return commission_id;
    }

    /**
     * @param commission_id the commission_id to set
     */
    public void setCommission_id(Integer commission_id) {
        this.commission_id = commission_id;
    }

    /**
     * @return the commission_name
     */
    public String getCommission_name() {
        return commission_name;
    }

    /**
     * @param commission_name the commission_name to set
     */
    public void setCommission_name(String commission_name) {
        this.commission_name = commission_name;
    }

    /**
     * @return the isdefault
     */
    public boolean isIsdefault() {
        return isdefault;
    }

    /**
     * @param isdefault the isdefault to set
     */
    public void setIsdefault(boolean isdefault) {
        this.isdefault = isdefault;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @return the first_pmt_sales_percentage
     */
    public BigDecimal getFirst_pmt_sales_percentage() {
        return first_pmt_sales_percentage;
    }

    /**
     * @param first_pmt_sales_percentage the first_pmt_sales_percentage to set
     */
    public void setFirst_pmt_sales_percentage(BigDecimal first_pmt_sales_percentage) {
        this.first_pmt_sales_percentage = first_pmt_sales_percentage;
    }

    /**
     * @return the first_year_sales_percentage
     */
    public BigDecimal getFirst_year_sales_percentage() {
        return first_year_sales_percentage;
    }

    /**
     * @param first_year_sales_percentage the first_year_sales_percentage to set
     */
    public void setFirst_year_sales_percentage(BigDecimal first_year_sales_percentage) {
        this.first_year_sales_percentage = first_year_sales_percentage;
    }

    /**
     * @return the first_year_manager_percentage
     */
    public BigDecimal getFirst_year_manager_percentage() {
        return first_year_manager_percentage;
    }

    /**
     * @param first_year_manager_percentage the first_year_manager_percentage to set
     */
    public void setFirst_year_manager_percentage(BigDecimal first_year_manager_percentage) {
        this.first_year_manager_percentage = first_year_manager_percentage;
    }

    /**
     * @return the second_year_sales_percentage
     */
    public BigDecimal getSecond_year_sales_percentage() {
        return second_year_sales_percentage;
    }

    /**
     * @param second_year_sales_percentage the second_year_sales_percentage to set
     */
    public void setSecond_year_sales_percentage(BigDecimal second_year_sales_percentage) {
        this.second_year_sales_percentage = second_year_sales_percentage;
    }

    /**
     * @return the second_year_manager_percentage
     */
    public BigDecimal getSecond_year_manager_percentage() {
        return second_year_manager_percentage;
    }

    /**
     * @param second_year_manager_percentage the second_year_manager_percentage to set
     */
    public void setSecond_year_manager_percentage(BigDecimal second_year_manager_percentage) {
        this.second_year_manager_percentage = second_year_manager_percentage;
    }

    @Override
    public String toString() {
        return commission_name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Commission) {
            if (this.getCommission_id().equals(((Commission)obj).getCommission_id())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.commission_id != null ? this.commission_id.hashCode() : 0);
        return hash;
    }
}
