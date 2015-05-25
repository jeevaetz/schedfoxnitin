/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.schedule.checkincheckout.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;
import rmischeduleserver.control.ScheduleController;
import schedfoxlib.model.Company;
import schedfoxlib.model.ScheduleData;

/**
 *
 * @author ira
 */
public class LateCheckInProcessThread extends Thread {

    private HashMap<String, ScheduleData> notifiedSchedules;

    public void run() {
        notifiedSchedules = new HashMap<String, ScheduleData>();
        while (true) {
            try {
                boolean isDispatcher = Main_Window.parentOfApplication.isUserAMemberOfGroups(new Connection(), "Dispatcher");

                if (isDispatcher) {
                    Vector<Company> companies = Main_Window.getActiveListOfCompanies();

                    HashMap<Integer, LateCheckinInfo> lateCheckinInfo = new HashMap<Integer, LateCheckinInfo>();
                    for (int c = 0; c < companies.size(); c++) {
                        Company comp = companies.get(c);
                        if (comp.getAlertLateCheckin() == true) {
                            ScheduleController schedController = new ScheduleController(comp.getCompId());
                            try {
                                ArrayList<ScheduleData> schedules = schedController.getNonCheckedInSchedules(15, 120);
                                for (int s = 0; s < schedules.size(); s++) {
                                    ScheduleData currSched = schedules.get(s);
                                    boolean hasAccessToBranch = false;
                                    for (int b = 0; b < comp.getBranches().size(); b++) {
                                        if (comp.getBranches().get(b).getBranchId().equals(currSched.getBranchId())) {
                                            hasAccessToBranch = true;
                                        }
                                    }
                                    if (notifiedSchedules.get(currSched.getScheduleId()) == null && hasAccessToBranch) {
                                        if (lateCheckinInfo.get(Integer.parseInt(comp.getCompId())) == null) {
                                            lateCheckinInfo.put(Integer.parseInt(comp.getCompId()), new LateCheckinInfo());
                                        }
                                        notifiedSchedules.put(currSched.getScheduleId(), currSched);
                                        LateCheckinInfo lateCheckin = lateCheckinInfo.get(Integer.parseInt(comp.getCompId()));
                                        lateCheckin.setCompany(comp);
                                        lateCheckin.getSchedules().add(currSched);
                                    }
                                }
                            } catch (Exception exe) {
                                exe.printStackTrace();
                            }
                        }
                    }

                    if (lateCheckinInfo.size() > 0) {
                        LateCheckinAlert lateAlert = new LateCheckinAlert(Main_Window.parentOfApplication, true);
                        lateAlert.setAlertData(lateCheckinInfo);
                        lateAlert.setVisible(true);
                    }
                }
            } catch (Exception exe) {

            }
            try {
                Thread.sleep(1000 * 60);
            } catch (Exception exe) {
            }
        }
    }
}
