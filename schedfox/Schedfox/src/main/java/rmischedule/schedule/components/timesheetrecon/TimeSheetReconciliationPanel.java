/*
 * TimeSheetReconciliationPanel.java
 *
 * Created on April 25, 2005, 8:50 AM
 */

package rmischedule.schedule.components.timesheetrecon;

import rmischedule.schedule.Schedule_View_Panel;
import rmischedule.schedule.components.*;
import rmischedule.components.graphicalcomponents.GraphicalListParent;
import rmischedule.components.graphicalcomponents.GraphicalListComponent;
import java.awt.Dimension;
import javax.swing.JOptionPane;
import rmischedule.main.Main_Window;
import java.util.Vector;
/**
 *
 * @author ira
 */
public class TimeSheetReconciliationPanel extends GraphicalListParent {
    
    private Schedule_View_Panel Parent;
    private Vector<SMainComponent> myClients;
    
    /** Creates a new instance of TimeSheetReconciliationPanel */
    public TimeSheetReconciliationPanel(Schedule_View_Panel parent) {
        super.setUnSelectable(true);
        Parent = parent;
        WaitForScheduleViewToFinishLoadingThread myThread = new WaitForScheduleViewToFinishLoadingThread(parent);
        myThread.start();
    }
    
    public void loadAllInfo() {
        myClients = Parent.getClientVector();
        String id;
        for (int i = 0; i < myClients.size(); i++) {
            SMainComponent client = myClients.get(i);
            id = client.getClientName();
            if (client instanceof SClient) {
                if (!client.getClientData().getClientId().equals(0)) {
                        EmployeeTimeSheetReconPanel newPanel = new EmployeeTimeSheetReconPanel(client, this, id,
                                Parent.getLayeredPane(), Parent.getContentPane(), client);
                        newPanel.allowUnSelected = false;
                        this.add(newPanel);
                }
            }
        }
    }
    
    public void scanAndRemoveEmpFromReconciliation(SEmployee empToScan) {
        //removeItem(empToScan);
    }
    
    public void doOnSelect() {
        Parent.setClientToFilter(-1);
        Parent.orderClients(null);
    }
    
    public void functionToRunOnSelectedShift(Object oneToSelect, boolean selected) {
        SClient myClient = (SClient)oneToSelect;
        if (selected) {
            
            if(selected){
                Parent.setClientToFilter(Integer.parseInt(myClient.getId()));
            }else{
                Parent.setClientToFilter(-1);
                super.setSelected(0);
                super.setHoldComp(super.getComponentObject(0));
            }
            
            Parent.orderClients(null);
        } else {
            JOptionPane.showConfirmDialog(Main_Window.parentOfApplication, "Do you want to reconcile the shifts for " + myClient.getClientName() + "\n" +
                    " for the current week?", "Confirm Reconcilliation!", JOptionPane.YES_NO_OPTION);
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
        
        public void killMe() {
            killMe = true;
            this.interrupt();
        }
        
        public void run() {
            this.setPriority(Thread.MIN_PRIORITY);
            while (parentW == null ||
                    !parentW.isInitialized() ||
                    parentW.getScrollPane() == null ||
                    !parentW.isVisible()) {
                if (killMe) {
                    return;
                }
                try {
                    sleep(100);
                } catch (Exception e) {}
            }
            Parent = parentW;
            loadAllInfo();
        }
    }
}
