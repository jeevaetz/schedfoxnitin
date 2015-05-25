/*
 * EmployeeToDisplayPanel2.java
 *
 * Created on February 23, 2005, 11:26 AM
 */
/**
 *
 * @author ira
 */
package rmischedule.schedule.components.employeestodisplay;

import rmischedule.schedule.Schedule_View_Panel;
import rmischedule.schedule.components.SEmployee;
import rmischedule.components.graphicalcomponents.GraphicalListParent;
import rmischedule.components.graphicalcomponents.GraphicalListComponent;
import java.awt.Dimension;
import java.util.Vector;
import javax.swing.SwingUtilities;

/**
 *
 * @author  ira
 */
public class EmployeeToDisplayPanel extends GraphicalListParent {

    private Vector<SEmployee> myEmployees;
    private Schedule_View_Panel Parent;
    private EmployeeToDisplayPanel thisObj;

    /** Creates new form EmployeeToDisplayPanel */
    public EmployeeToDisplayPanel(Schedule_View_Panel parentView) {
        super.setUnSelectable(true);
        Parent = parentView;
        thisObj = this;
        WaitForScheduleViewToFinishLoadingThread myThread = new WaitForScheduleViewToFinishLoadingThread(Parent);
        myThread.start();

    }

    public void loadAllInfo() {
        myEmployees = Parent.getEmployeeList();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                for (int i = 0; i < myEmployees.size(); i++) {
                    String id = myEmployees.get(i).getName();
                    if (myEmployees.get(i).getId() != 0) {
                        if (myEmployees.get(i).getShifts().size() > 0) {
                            GraphicalListComponent newPanel = new GraphicalListComponent(myEmployees.get(i), thisObj, id,
                                    Parent.getLayeredPane(), Parent.FloatContainerPanel, myEmployees.get(i));
                            add(newPanel);
                        }
                    }


                }
            }
        });
    }

    public void doOnSelect() {
        Parent.setEmployeeToFilter(-1);
        Parent.orderClients(null);
    }

    public void functionToRunOnSelectedShift(Object oneToSelect, boolean selected) {
        SEmployee myEmployee = (SEmployee) oneToSelect;
        if (myEmployee.getId() == 0) {
            Parent.displayOpenShiftsOnly(true);
        } else {
            if (myEmployee.getId() != 0) {
                if (selected) {
                    Parent.setEmployeeToFilter(myEmployee.getId());
                } else {
                    Parent.setEmployeeToFilter(-1);
                    super.setSelected(0);
                    super.setHoldComp(super.getComponentObject(0));
                }
            } else {
                Parent.setEmployeeToFilter(-1);
            }
//            Parent.open_shifts_only = false;
            Parent.orderClients(null);
        }
    }

    private class WaitForScheduleViewToFinishLoadingThread extends rmischedule.schedule.ScheduleThread {

        private Schedule_View_Panel parentW;
        private boolean killMe;

        public WaitForScheduleViewToFinishLoadingThread(Schedule_View_Panel parentWin) {
            parentW = parentWin;
            registerMe(parentW);
            killMe = false;
        }

        public void run() {
            this.setPriority(Thread.MIN_PRIORITY);
            while (!parentW.isInitialized()) {
                if (killMe) {
                    return;
                }
                try {
                    sleep(600);
                } catch (Exception e) {
                }
            }
            Parent = parentW;
            loadAllInfo();
        }

        public void killMe() {
            killMe = true;
            this.interrupt();
        }
    }
}
