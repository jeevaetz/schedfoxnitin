/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.schedule.checkincheckout.process;

import java.util.ArrayList;
import schedfoxlib.model.Company;
import schedfoxlib.model.ScheduleData;

/**
 *
 * @author ira
 */
public class LateCheckinInfo {
    private Company company;
    private ArrayList<ScheduleData> schedules;
    
    public LateCheckinInfo() {
        schedules = new ArrayList<ScheduleData>();
    }

    /**
     * @return the company
     */
    public Company getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(Company company) {
        this.company = company;
    }

    /**
     * @return the schedules
     */
    public ArrayList<ScheduleData> getSchedules() {
        return schedules;
    }

    /**
     * @param schedules the schedules to set
     */
    public void setSchedules(ArrayList<ScheduleData> schedules) {
        this.schedules = schedules;
    }
}
