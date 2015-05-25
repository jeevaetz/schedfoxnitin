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
public class UserCommissionCaps {
    private Integer userCommissionCapId;
    private Integer userId;
    private Integer yearNumber;
    private BigDecimal yearlyCaps;

    public UserCommissionCaps() {
        yearlyCaps = new BigDecimal(0);
    }

    public UserCommissionCaps(Record_Set rst) {
        try {
            userCommissionCapId = rst.getInt("user_commission_cap_id");
        } catch (Exception e) {}
        try {
            userId = rst.getInt("user_id");
        } catch (Exception e) {}
        try {
            yearlyCaps = rst.getBigDecimal("yearly_cap");
        } catch (Exception e) {}
        try {
            yearNumber = rst.getInt("year_number");
        } catch (Exception e) {}
    }

    /**
     * @return the userCommissionCapId
     */
    public Integer getUserCommissionCapId() {
        return userCommissionCapId;
    }

    /**
     * @param userCommissionCapId the userCommissionCapId to set
     */
    public void setUserCommissionCapId(Integer userCommissionCapId) {
        this.userCommissionCapId = userCommissionCapId;
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
     * @return the yearlyCaps
     */
    public BigDecimal getYearlyCaps() {
        return yearlyCaps;
    }

    /**
     * @param yearlyCaps the yearlyCaps to set
     */
    public void setYearlyCaps(BigDecimal yearlyCaps) {
        this.yearlyCaps = yearlyCaps;
    }

    /**
     * @return the yearNumber
     */
    public Integer getYearNumber() {
        return yearNumber;
    }

    /**
     * @param yearNumber the yearNumber to set
     */
    public void setYearNumber(Integer yearNumber) {
        this.yearNumber = yearNumber;
    }


}
