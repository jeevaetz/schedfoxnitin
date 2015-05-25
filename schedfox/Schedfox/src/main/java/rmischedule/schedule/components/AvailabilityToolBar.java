/*
 * AvailabilityToolBar.java
 *
 * Created on July 1, 2005, 1:33 PM
 */

package rmischedule.schedule.components;
import javax.swing.JToolBar;
import rmischedule.components.graphicalcomponents.myToolBarIcons;
import rmischedule.schedule.Schedule_Employee_Availability_Split_Pane;
import rmischedule.main.Main_Window;
import java.awt.Dimension;
import java.awt.FlowLayout;
/**
 *
 * @author ira
 */
public class AvailabilityToolBar extends JToolBar {
    
    private Schedule_Employee_Availability_Split_Pane myParent;
    private myToolBarIcons availIcon;
    private myToolBarIcons employeeViewIcon;
    private myToolBarIcons shiftAlertIcon;
    private myToolBarIcons shiftConsolidateIcon;
    
    /** Creates a new instance of AvailabilityToolBar */
    public AvailabilityToolBar(Schedule_Employee_Availability_Split_Pane parent) {
        myParent = parent;
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        setFloatable(false);
        setMinimumSize(new Dimension(40,40));
        setPreferredSize(new Dimension(40,40));
        setMaximumSize(new Dimension(1000,1000));
        initComponents();
        revalidate();
    }
    
    private void initComponents() {
        availIcon = new myToolBarIcons(Main_Window.AvailabilityIcon, Main_Window.AvailabilityIcon,
                "Show Availability List", "Viewing Availability List") {

            @Override
            protected void runOnClick() {
                availIcon.setPressed(true);
                employeeViewIcon.setPressed(false);
                shiftAlertIcon.setPressed(false);
                shiftConsolidateIcon.setPressed(false);
                myParent.selectAvailability();
                myParent.parentWin.parent.AEinfo.hide(true);
            }
        };
        employeeViewIcon = new myToolBarIcons(Main_Window.EmployeeListIcon, Main_Window.EmployeeListIcon,
                "View Schedule By Employee", "Viewing Employee List") {
            @Override
            protected void runOnClick() {
                availIcon.setPressed(false);
                employeeViewIcon.setPressed(true);
                shiftAlertIcon.setPressed(false);
                shiftConsolidateIcon.setPressed(false);
                myParent.selectEmployeeShifts();
                myParent.parentWin.parent.AEinfo.hide(true);
            }
        };

        shiftAlertIcon = new myToolBarIcons(Main_Window.ShiftAlertIcon, Main_Window.ShiftAlertIcon,
                "View Alerts On Schedule", "Viewing Alerts") {
            @Override
            protected void runOnClick() {
                availIcon.setPressed(false);
                employeeViewIcon.setPressed(false);
                shiftAlertIcon.setPressed(true);
                shiftConsolidateIcon.setPressed(false);
                myParent.selectAlerts();
                myParent.parentWin.parent.AEinfo.hide(true);
            }
        };

        shiftConsolidateIcon = new myToolBarIcons(Main_Window.ConsolidateIcon, Main_Window.ConsolidateIcon,
                "View Reconcile Timesheets", "Viewing Timesheet Reconciliation") {

            @Override
            protected void runOnClick() {
                availIcon.setPressed(false);
                employeeViewIcon.setPressed(false);
                shiftAlertIcon.setPressed(false);
                shiftConsolidateIcon.setPressed(true);
                myParent.selectConsolidate();
                myParent.parentWin.parent.AEinfo.hide(true);
            }
        };

        add(availIcon);
        add(employeeViewIcon);
        //add(shiftAlertIcon);
        add(shiftConsolidateIcon);
        
        availIcon.setPressed(true);
    }
    
}
