/*
 * IndividualHistoryColumnPanel.java
 *
 * Created on December 16, 2005, 10:28 AM
 */

package rmischedule.schedule.history;
import schedfoxlib.model.ShiftTypeClass;
import rmischeduleserver.util.StaticDateTimeFunctions;
import schedfoxlib.model.util.Record_Set;
import java.awt.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import rmischeduleserver.mysqlconnectivity.queries.rate_codes.rate_code_list_query;
/**
 *
 * @author  Ira Juneau
 */
public class IndividualHistoryColumnPanel extends javax.swing.JPanel {
    
    private HistoryDetailPanel myParent;
    public ArrayList<JLabel> myListOfLabels;
    
    /** Creates new form IndividualHistoryColumnPanel */
    public IndividualHistoryColumnPanel(Record_Set history, HistoryDetailPanel myparent) {
        initComponents();
        myParent = myparent;
        myListOfLabels = new ArrayList();
        addChildrenToVector(this);
        for (int i = 0; i < myListOfLabels.size(); i++) {
            myListOfLabels.get(i).setForeground(new Color(90,90,90));
            myListOfLabels.get(i).setBorder(new MatteBorder(1,1,1,1, new Color(150,150,150)));
        }
        if (!myparent.myParent.svParent.shouldMarkUnconShifts()) {
            UnconfirmedShifts.setVisible(false);
        }
        populateData(history);
    }
    
    /**
     * Used to add all JLabels to our vector so it is damn easy to compare and change
     * them...I am lazy
     */
    private void addChildrenToVector(JPanel parent) {
        Component currComp;
        for (int i = 0; i < parent.getComponentCount(); i++) {
            currComp = parent.getComponent(i);
            try {
                myListOfLabels.add((JLabel)currComp);
            } catch (Exception e) {
                try {
                    addChildrenToVector((JPanel)currComp);
                } catch (Exception exe) {}
            }
        }
    }
    
    /**
     * This is used to check all JLabels and see when their content has changed and highlight
     * all changed fields in the color supplied...Should be supplied the historyPanel before this 
     * one and wil highlight this one depending on what does not match...
     */
    public void compareThisAndOtherPanelAndHighlightChangedFields(IndividualHistoryColumnPanel panel, Color colorToHighlight) {
        for (int i = 0; i < myListOfLabels.size(); i++) {
            if (!myListOfLabels.get(i).getText().equals(panel.myListOfLabels.get(i).getText()) && !myListOfLabels.get(i).equals(TimeChanged)) {
                myListOfLabels.get(i).setForeground(colorToHighlight);
            }
        }
    }
    
    private void populateData(Record_Set rs) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            StringTokenizer myDateTime = new StringTokenizer(rs.getString("last_update"), ".");
            TimeChanged.setText(myDateTime.nextToken());
        } catch (Exception e) {}
        UserChanged.setText(rs.getString("user_login"));
        Employee.setText(rs.getString("ename"));
        Client.setText(rs.getString("cname"));
        int format = StaticDateTimeFunctions.MILITARY_FORMAT;
        if (!myParent.myParent.svParent.isMilitaryTimeFormat()) {
            format = StaticDateTimeFunctions.STANDARD_FORMAT;
        }
        StartTime.setText(StaticDateTimeFunctions.stringToFormattedTime(rs.getString("schedule_start"), format));
        EndTime.setText(StaticDateTimeFunctions.stringToFormattedTime(rs.getString("schedule_end"), format));
        try {
            StartLabel.setText(dateFormat.format(rs.getDate("date_started")));
        } catch (Exception exe) {}
        try {
            EndLabel.setText(dateFormat.format(rs.getDate("date_ended")));
        } catch (Exception exe) {}
        ShiftTypeClass myShiftType = new ShiftTypeClass(rs.getString("schedule_type"));
        if (myShiftType.isShiftType(ShiftTypeClass.SHIFT_NON_BILLABLE)) {
            ShiftBillable.setText("Non-Billable");
        } else {
            ShiftBillable.setText("Yes");
        }
        if (myShiftType.isShiftType(ShiftTypeClass.SHIFT_CONFIRMED)) {
            Confirmed.setText("Shift Confirmed");
        } else if (myShiftType.isShiftType(ShiftTypeClass.SHIFT_VIEWED_BY_EMPLOYEE)) {
            Confirmed.setText("Viewed by Employee");
        } else {
            Confirmed.setText("Shift Unconfirmed");
        }
        if (myShiftType.isShiftType(ShiftTypeClass.SHIFT_RECONCILED)) {
            Reconciled.setText("Reconciled");
        } else if (myShiftType.isShiftType(ShiftTypeClass.SHIFT_UNRECONCILED)) {
            Reconciled.setText("Non-Reconciled");
        } else {
            Reconciled.setText("Default");
        }
        if (myShiftType.isShiftType(ShiftTypeClass.SHIFT_NON_PAYABLE)) {
            ShiftPayable.setText("Non-Payable");
        } else {
            ShiftPayable.setText("Yes");
        }
        try {
            RateCodeLbl.setText(myParent.getRateCodes().get(rs.getInt("rate_code_id")));
        } catch (Exception exe) {}
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        DateChanged = new javax.swing.JPanel();
        TimeChanged = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        UserChanged = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        Employee = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        Client = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        StartTime = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        EndTime = new javax.swing.JLabel();
        StartDate = new javax.swing.JPanel();
        StartLabel = new javax.swing.JLabel();
        EndDate = new javax.swing.JPanel();
        EndLabel = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        ShiftBillable = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        ShiftPayable = new javax.swing.JLabel();
        UnconfirmedShifts = new javax.swing.JPanel();
        Confirmed = new javax.swing.JLabel();
        ReconciledShifts = new javax.swing.JPanel();
        Reconciled = new javax.swing.JLabel();
        RateCode = new javax.swing.JPanel();
        RateCodeLbl = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(140, 32767));
        setMinimumSize(new java.awt.Dimension(140, 21));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(140, 21));
        setLayout(new java.awt.GridLayout(1, 0));

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        DateChanged.setMaximumSize(new java.awt.Dimension(32767, 19));
        DateChanged.setMinimumSize(new java.awt.Dimension(10, 19));
        DateChanged.setOpaque(false);
        DateChanged.setPreferredSize(new java.awt.Dimension(10, 19));
        DateChanged.setLayout(new java.awt.GridLayout(1, 0));

        TimeChanged.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        DateChanged.add(TimeChanged);

        jPanel1.add(DateChanged);

        jPanel3.setMaximumSize(new java.awt.Dimension(32767, 19));
        jPanel3.setMinimumSize(new java.awt.Dimension(10, 19));
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(10, 19));
        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        UserChanged.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        jPanel3.add(UserChanged);

        jPanel1.add(jPanel3);

        jPanel4.setMaximumSize(new java.awt.Dimension(32767, 19));
        jPanel4.setMinimumSize(new java.awt.Dimension(10, 19));
        jPanel4.setOpaque(false);
        jPanel4.setPreferredSize(new java.awt.Dimension(10, 19));
        jPanel4.setLayout(new java.awt.GridLayout(1, 0));

        Employee.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        jPanel4.add(Employee);

        jPanel1.add(jPanel4);

        jPanel5.setMaximumSize(new java.awt.Dimension(32767, 19));
        jPanel5.setMinimumSize(new java.awt.Dimension(10, 19));
        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(10, 19));
        jPanel5.setLayout(new java.awt.GridLayout(1, 0));

        Client.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        jPanel5.add(Client);

        jPanel1.add(jPanel5);

        jPanel6.setMaximumSize(new java.awt.Dimension(32767, 19));
        jPanel6.setMinimumSize(new java.awt.Dimension(10, 19));
        jPanel6.setOpaque(false);
        jPanel6.setPreferredSize(new java.awt.Dimension(10, 19));
        jPanel6.setLayout(new java.awt.GridLayout(1, 0));

        StartTime.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        jPanel6.add(StartTime);

        jPanel1.add(jPanel6);

        jPanel7.setMaximumSize(new java.awt.Dimension(32767, 19));
        jPanel7.setMinimumSize(new java.awt.Dimension(10, 19));
        jPanel7.setOpaque(false);
        jPanel7.setPreferredSize(new java.awt.Dimension(10, 19));
        jPanel7.setLayout(new java.awt.GridLayout(1, 0));

        EndTime.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        jPanel7.add(EndTime);

        jPanel1.add(jPanel7);

        StartDate.setMaximumSize(new java.awt.Dimension(32767, 19));
        StartDate.setMinimumSize(new java.awt.Dimension(10, 19));
        StartDate.setOpaque(false);
        StartDate.setPreferredSize(new java.awt.Dimension(10, 19));
        StartDate.setLayout(new java.awt.GridLayout(1, 0));

        StartLabel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        StartDate.add(StartLabel);

        jPanel1.add(StartDate);

        EndDate.setMaximumSize(new java.awt.Dimension(32767, 19));
        EndDate.setMinimumSize(new java.awt.Dimension(10, 19));
        EndDate.setOpaque(false);
        EndDate.setPreferredSize(new java.awt.Dimension(10, 19));
        EndDate.setLayout(new java.awt.GridLayout(1, 0));
        EndDate.add(EndLabel);

        jPanel1.add(EndDate);

        jPanel8.setMaximumSize(new java.awt.Dimension(32767, 19));
        jPanel8.setMinimumSize(new java.awt.Dimension(10, 19));
        jPanel8.setOpaque(false);
        jPanel8.setPreferredSize(new java.awt.Dimension(10, 19));
        jPanel8.setLayout(new java.awt.GridLayout(1, 0));

        ShiftBillable.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        jPanel8.add(ShiftBillable);

        jPanel1.add(jPanel8);

        jPanel9.setMaximumSize(new java.awt.Dimension(32767, 19));
        jPanel9.setMinimumSize(new java.awt.Dimension(10, 19));
        jPanel9.setOpaque(false);
        jPanel9.setPreferredSize(new java.awt.Dimension(10, 19));
        jPanel9.setLayout(new java.awt.GridLayout(1, 0));

        ShiftPayable.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        jPanel9.add(ShiftPayable);

        jPanel1.add(jPanel9);

        UnconfirmedShifts.setMaximumSize(new java.awt.Dimension(32767, 19));
        UnconfirmedShifts.setMinimumSize(new java.awt.Dimension(10, 19));
        UnconfirmedShifts.setOpaque(false);
        UnconfirmedShifts.setPreferredSize(new java.awt.Dimension(10, 19));
        UnconfirmedShifts.setLayout(new java.awt.GridLayout(1, 0));

        Confirmed.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        UnconfirmedShifts.add(Confirmed);

        jPanel1.add(UnconfirmedShifts);

        ReconciledShifts.setMaximumSize(new java.awt.Dimension(32767, 19));
        ReconciledShifts.setMinimumSize(new java.awt.Dimension(10, 19));
        ReconciledShifts.setOpaque(false);
        ReconciledShifts.setPreferredSize(new java.awt.Dimension(10, 19));
        ReconciledShifts.setLayout(new java.awt.GridLayout(1, 0));

        Reconciled.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        ReconciledShifts.add(Reconciled);

        jPanel1.add(ReconciledShifts);

        RateCode.setMaximumSize(new java.awt.Dimension(32767, 19));
        RateCode.setMinimumSize(new java.awt.Dimension(10, 19));
        RateCode.setOpaque(false);
        RateCode.setPreferredSize(new java.awt.Dimension(10, 19));
        RateCode.setLayout(new java.awt.GridLayout());
        RateCode.add(RateCodeLbl);

        jPanel1.add(RateCode);

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Client;
    private javax.swing.JLabel Confirmed;
    private javax.swing.JPanel DateChanged;
    private javax.swing.JLabel Employee;
    private javax.swing.JPanel EndDate;
    private javax.swing.JLabel EndLabel;
    private javax.swing.JLabel EndTime;
    private javax.swing.JPanel RateCode;
    private javax.swing.JLabel RateCodeLbl;
    private javax.swing.JLabel Reconciled;
    private javax.swing.JPanel ReconciledShifts;
    private javax.swing.JLabel ShiftBillable;
    private javax.swing.JLabel ShiftPayable;
    private javax.swing.JPanel StartDate;
    private javax.swing.JLabel StartLabel;
    private javax.swing.JLabel StartTime;
    private javax.swing.JLabel TimeChanged;
    private javax.swing.JPanel UnconfirmedShifts;
    private javax.swing.JLabel UserChanged;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    // End of variables declaration//GEN-END:variables
    
}
