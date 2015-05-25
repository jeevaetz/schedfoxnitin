/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class MobileReportGraphData implements Serializable {
    private Date weekBeginning;
    private Integer numberFilledOut;
    private String mobileFormName;
    
    public MobileReportGraphData() {
        
    }
    
    public MobileReportGraphData(Record_Set rst) {
        try {
            weekBeginning = rst.getDate("week_beginning");
        } catch (Exception exe) {}
        try {
            numberFilledOut = rst.getInt("number_filled_out");
        } catch (Exception exe) {}
        try {
            mobileFormName = rst.getString("mobile_form_name");
        } catch (Exception exe) {}
    }

    /**
     * @return the weekBeginning
     */
    public Date getWeekBeginning() {
        return weekBeginning;
    }

    /**
     * @param weekBeginning the weekBeginning to set
     */
    public void setWeekBeginning(Date weekBeginning) {
        this.weekBeginning = weekBeginning;
    }

    /**
     * @return the numberFilledOut
     */
    public Integer getNumberFilledOut() {
        return numberFilledOut;
    }

    /**
     * @param numberFilledOut the numberFilledOut to set
     */
    public void setNumberFilledOut(Integer numberFilledOut) {
        this.numberFilledOut = numberFilledOut;
    }

    /**
     * @return the mobileFormName
     */
    public String getMobileFormName() {
        return mobileFormName;
    }

    /**
     * @param mobileFormName the mobileFormName to set
     */
    public void setMobileFormName(String mobileFormName) {
        this.mobileFormName = mobileFormName;
    }
    
}
