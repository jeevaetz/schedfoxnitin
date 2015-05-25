/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class ClientDisplayContact extends Client {

    private Integer lastWeekTotal;
    private Integer currentWeekTotal;

    public ClientDisplayContact() {
        super();
    }

    public ClientDisplayContact(Date currDate) {
        super(currDate);
    }

    public ClientDisplayContact(Date currDate, Integer clientId) {
        super(currDate, clientId);
    }

    public ClientDisplayContact(Date currDate, Record_Set rst) {
        super(currDate, rst);
        try {
            this.lastWeekTotal = rst.getInt("last_week_total");
        } catch (Exception exe) {
        }
        try {
            this.currentWeekTotal = rst.getInt("current_week_total");
        } catch (Exception exe) {
        }
    }

    /**
     * @return the lastWeekTotal
     */
    public Integer getLastWeekTotal() {
        return lastWeekTotal;
    }

    /**
     * @param lastWeekTotal the lastWeekTotal to set
     */
    public void setLastWeekTotal(Integer lastWeekTotal) {
        this.lastWeekTotal = lastWeekTotal;
    }

    /**
     * @return the currentWeekTotal
     */
    public Integer getCurrentWeekTotal() {
        return currentWeekTotal;
    }

    /**
     * @param currentWeekTotal the currentWeekTotal to set
     */
    public void setCurrentWeekTotal(Integer currentWeekTotal) {
        this.currentWeekTotal = currentWeekTotal;
    }

}
