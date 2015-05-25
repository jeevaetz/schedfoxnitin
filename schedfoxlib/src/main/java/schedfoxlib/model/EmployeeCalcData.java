/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class EmployeeCalcData {

    private String firstDate;
    private String lastDate;
    private Integer totalMinutes;
    private Integer averageMinutes;

    public EmployeeCalcData() {

    }

    public EmployeeCalcData(Record_Set rs) {
        try {
            firstDate = rs.getString("first_date");
        } catch (Exception e) {
            firstDate = "Has Not Worked";
        }
        try {
            lastDate = rs.getString("last_date");
        } catch (Exception e) {
            lastDate = "Has Not Worked";
        }
        try {
            totalMinutes = (Integer.parseInt(rs.getString("totalminutes")) / 60);
        } catch (Exception e) {
            totalMinutes = null;
        }
//        try {
//            AverageHours.setText((Integer.parseInt(rs.getString("average")) / 60) + "");
//        } catch (Exception e) {
//            AverageHours.setText("Has Not Worked");
//        }
    }

    /**
     * @return the firstDate
     */
    public String getFirstDate() {
        return firstDate;
    }

    /**
     * @param firstDate the firstDate to set
     */
    public void setFirstDate(String firstDate) {
        this.firstDate = firstDate;
    }

    /**
     * @return the lastDate
     */
    public String getLastDate() {
        return lastDate;
    }

    /**
     * @param lastDate the lastDate to set
     */
    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    /**
     * @return the totalMinutes
     */
    public Integer getTotalMinutes() {
        return totalMinutes;
    }

    /**
     * @param totalMinutes the totalMinutes to set
     */
    public void setTotalMinutes(Integer totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    /**
     * @return the averageMinutes
     */
    public Integer getAverageMinutes() {
        return averageMinutes;
    }

    /**
     * @param averageMinutes the averageMinutes to set
     */
    public void setAverageMinutes(Integer averageMinutes) {
        this.averageMinutes = averageMinutes;
    }
}
