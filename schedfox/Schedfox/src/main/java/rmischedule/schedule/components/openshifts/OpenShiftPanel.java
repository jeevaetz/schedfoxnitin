/*
 * OpenShiftPanel.java
 *
 * Created on December 21, 2004, 9:56 AM
 */

package rmischedule.schedule.components.openshifts;
import rmischedule.schedule.Schedule_View_Panel;
import rmischedule.schedule.components.*;
import rmischedule.main.Main_Window;
import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischedule.schedule.Schedule_Employee_Availability_Split_Pane;
import java.util.Vector;
import java.util.Calendar;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author  ira
 */
public class OpenShiftPanel extends javax.swing.JPanel {
    
    private Vector openShifts;
    private Vector alertShifts;
    private Schedule_View_Panel Parent;
    private Schedule_Employee_Availability_Split_Pane aspParent;
    private boolean isDoneRecursion;
    
    /** Creates new form OpenShiftPanel */
    public OpenShiftPanel(Schedule_View_Panel parent, Schedule_Employee_Availability_Split_Pane asp) {
        initComponents();
        Parent = parent;
        aspParent = asp;
        alertShifts = new Vector(20);
        openShifts = new Vector(20);
        runMe();
    }
    
    public void runMe() {
        waitForParentToFinishLoadingThread myThread = new waitForParentToFinishLoadingThread();
        myThread.start();
    }
    
    public void doOnSelect() {
        Parent.setEmployeeToFilter(-1);
        Parent.orderClients(null);
    }
    
    public void parseData() {
//        int buffer = Main_Window.parentOfApplication.options.getIntOption("openshiftimer") * 60;
//        alertShifts.removeAllElements();
//        for (int i = 0; i < openShifts.size(); i++) {
//            Calendar now = Calendar.getInstance();
//            int currTime = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE);
//            isDoneRecursion = false;
//            try {
//                testCurrentShift((DShift)openShifts.get(i), now, currTime, buffer);
//            } catch (Exception e) {
//                //System.out.println("Master get date still broken");
//            }
//        }
//        if (alertShifts.size() > 0) {
//            aspParent.toggleAlarmMode(alertShifts.size() + " Open Shifts Are Within " + Main_Window.parentOfApplication.options.getIntOption("openshiftimer") + " Hours of Starting! ");
//        }
//        DisplayPanel.removeAll();
//        for (int i = 0; i < alertShifts.size(); i++) {
//            IndividualOpenShift newOpenShift = new IndividualOpenShift(((DShift)alertShifts.get(i)), Parent);
//            DisplayPanel.add(newOpenShift);
//        }
//        revalidate();
//        try {
//            //Parent.sv.shb.setOpenShiftPanel(this);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
    }
    
    /**
     * Recursive method used to add shifts that should be in alert mode to vector
     */
    private void testCurrentShift(DShift day, Calendar date, int now, int buffer) {
        if (StaticDateTimeFunctions.convertCalendarToSchedFormat(date).equals(day.getDateString()) &&
                !isDoneRecursion) {
            if (day.getStartTime() < now + buffer) {
                alertShifts.add(day);
                isDoneRecursion = true;
            }
        }
        if (now + buffer > 1440 && !isDoneRecursion) {
            now-=1440;
            date.add(Calendar.DAY_OF_MONTH, 1);
            testCurrentShift(day, date, now, buffer);
        }
    }
    
    private class waitForParentToFinishLoadingThread extends rmischedule.schedule.ScheduleThread {
        private boolean stop;
        
        public waitForParentToFinishLoadingThread() {
            registerMe(Parent);
            
        }
        
        public void killMe() {
            stop = true;
            this.interrupt();
        }
        
        public void run() {
            this.setPriority(Thread.MIN_PRIORITY);
            stop = false;
            while (!stop) {
                try {
                    stop = !Parent.isInitialized();
                } catch (Exception e) {
                    stop = false;
                }
                try {
                    sleep(80);
                } catch (Exception e) {}
            }
            this.setPriority(Thread.NORM_PRIORITY);
            openShifts.removeAllElements();
            openShifts = Parent.getAllOpenShifts();
            parseData();
            
        }
    }
    
    class MyCellRenderer extends JLabel implements ListCellRenderer {
        public Component getListCellRendererComponent(JList list,Object value, int index, boolean isSelected, boolean cellHasFocus) {   // the list and the cell have the focus
            String s = ((DShift)(value)).getClient().getClientName();
            if (isSelected) {
                //setBackground(new Color(153,51,0));
                //setForeground(list.getSelectionForeground());
                setForeground(new Color(255,102,102));
                setBackground(list.getBackground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setText(s);
            return this;
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        TitlePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        DisplayPanel = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        TitlePanel.setLayout(new java.awt.GridLayout(1, 0));

        TitlePanel.setFont(new java.awt.Font("Arial Black", 0, 14));
        TitlePanel.setMaximumSize(new java.awt.Dimension(34768, 25));
        TitlePanel.setMinimumSize(new java.awt.Dimension(34, 25));
        TitlePanel.setPreferredSize(new java.awt.Dimension(34, 25));
        jLabel1.setFont(new java.awt.Font("Arial Black", 0, 14));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Open Shifts");
        TitlePanel.add(jLabel1);

        add(TitlePanel);

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        DisplayPanel.setLayout(new javax.swing.BoxLayout(DisplayPanel, javax.swing.BoxLayout.Y_AXIS));

        DisplayPanel.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        DisplayPanel.setMaximumSize(new java.awt.Dimension(40000, 400000));
        jPanel1.add(DisplayPanel);

        add(jPanel1);

    }
    // </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel DisplayPanel;
    private javax.swing.JPanel TitlePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    
}
