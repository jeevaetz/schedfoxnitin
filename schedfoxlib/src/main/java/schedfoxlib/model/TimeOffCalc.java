/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.math.BigDecimal;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class TimeOffCalc {
    private Date startDate;
    private Date endDate;
    private int daysAccrued;
    private BigDecimal daysTaken;
    private BigDecimal hoursAccrued;
    private String datesTaken;
    private boolean isExpired;

    public TimeOffCalc(Record_Set rst) {
        try {
            startDate = rst.getDate("start_date");
        } catch (Exception e) {}
        try {
            endDate = rst.getDate("end_date");
        } catch (Exception e) {}
        try {
            daysAccrued = rst.getInt("days_accrued");
        } catch (Exception e) {}
        try {
            datesTaken = rst.getString("dates_taken");
        } catch (Exception e) {}
        try {
            isExpired = rst.getBoolean("is_expired");
        } catch (Exception e) {}
        try {
            daysTaken = rst.getBigDecimal("days_taken");
        } catch (Exception e) {}
        try {
            hoursAccrued = rst.getBigDecimal("hours_accrued");
            hoursAccrued = hoursAccrued.setScale(2, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {}
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the daysAccrued
     */
    public int getDaysAccrued() {
        return daysAccrued;
    }

    /**
     * @param daysAccrued the daysAccrued to set
     */
    public void setDaysAccrued(int daysAccrued) {
        this.daysAccrued = daysAccrued;
    }

    /**
     * @return the datesTaken
     */
    public String getDatesTaken() {
        return datesTaken;
    }

    /**
     * @param datesTaken the datesTaken to set
     */
    public void setDatesTaken(String datesTaken) {
        this.datesTaken = datesTaken;
    }

    /**
     * @return the isExpired
     */
    public boolean isIsExpired() {
        return isExpired;
    }

    /**
     * @param isExpired the isExpired to set
     */
    public void setIsExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }

    /**
     * @return the daysTaken
     */
    public BigDecimal getDaysTaken() {
        return daysTaken;
    }

    /**
     * @param daysTaken the daysTaken to set
     */
    public void setDaysTaken(BigDecimal daysTaken) {
        this.daysTaken = daysTaken;
    }

    /**
     * @return the hoursAccrued
     */
    public BigDecimal getHoursAccrued() {
        return hoursAccrued;
    }

    /**
     * @param hoursAccrued the hoursAccrued to set
     */
    public void setHoursAccrued(BigDecimal hoursAccrued) {
        this.hoursAccrued = hoursAccrued;
    }
}
