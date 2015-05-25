/*
 * GetDatesForPrintDialog.java
 *
 * Created on August 18, 2005, 1:27 PM
 */
package rmischedule.xprint.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JCheckBox;

import rmischedule.components.jcalendar.JCalendarComboBox;
import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischeduleserver.control.UserController;
import schedfoxlib.model.Contact;
import schedfoxlib.model.User;

/**
 *
 * @author  Ira Juneau
 */
public class GetDatesWithUserSelectionForPrintDialog extends javax.swing.JDialog {

    private Calendar startCal;
    private Calendar endCal;
    private boolean getVals;
    private JCalendarComboBox monthPanel;
    private JCalendarComboBox startRangeCalendar;
    private JCalendarComboBox endRangeCalendar;
    private String dateString;
    private boolean specifyRange;
    public static String WEEK = "Week";
    public static String DAY = "Day";

    private String companyId;

    private ArrayList<Contact> contacts;
    private ArrayList<JCheckBox> contractChk;
    
    private String[] userTypes;
    
    public GetDatesWithUserSelectionForPrintDialog(java.awt.Frame parent, boolean modal, String val, boolean isMultipleWeeks, String companyId, boolean showDates, String... userTypes) {
        super(parent, modal);
        this.companyId = companyId;
        specifyRange = isMultipleWeeks;
        initialize(parent, modal, val);
        this.userTypes = userTypes;
        populateDMs();
        if (!showDates) {
            CardPanel.setVisible(false);
        }
    }

    private void populateDMs() {
        UserController userController = new UserController(companyId);
        contacts = new ArrayList<Contact>();
        try {
            for (int u = 0; u < this.userTypes.length; u++) {
                contacts.addAll(userController.getUsersByType(userTypes[u], -1 + ""));
            }
            
            HashMap<Integer, Contact> usersHash = new HashMap<Integer, Contact>();
            for (int c = 0; c < contacts.size(); c++) {
                usersHash.put(contacts.get(c).getPrimaryId(), contacts.get(c));
            }
            contacts.clear();
            
            Iterator<Contact> iterator = usersHash.values().iterator();
            while (iterator.hasNext()) {
                contacts.add(iterator.next());
            }
            Collections.sort(contacts);
            
            contractChk = new ArrayList<JCheckBox>();
            final JCheckBox selectAll = new JCheckBox("Select All");
            selectAll.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int c = 0; c < contractChk.size(); c++) {
                        contractChk.get(c).setSelected(selectAll.isSelected());
                    }
                }
            });
            selectPanel.add(selectAll);
            for (int c = 0; c < contacts.size(); c++) {
                User currUser = (User)contacts.get(c);
                JCheckBox box = new JCheckBox(currUser.getFullName());
                selectPanel.add(box);
                contractChk.add(box);
            }
        } catch (Exception e) {
        }
    }

    public ArrayList<Integer> getSelectedUserIds() {
        ArrayList<Integer> retVal = new ArrayList<Integer>();
        for (int c = 0; c < contractChk.size(); c++) {
            User selUser = (User)contacts.get(c);
            if (contractChk.get(c).isSelected()) {
                retVal.add(selUser.getPrimaryId());
            }
        }
        return retVal;
    }

    public void setDatesManually(Calendar startDate, Calendar endDate) {
        startRangeCalendar.setCalendar(startDate);
        endRangeCalendar.setCalendar(endDate);
    }

    public void initialize(java.awt.Frame parent, boolean modal, String date) {
        initComponents();
        monthPanel = new JCalendarComboBox();
        startRangeCalendar = new JCalendarComboBox(StaticDateTimeFunctions.getBegOfMonth(Calendar.getInstance()));
        endRangeCalendar = new JCalendarComboBox(StaticDateTimeFunctions.getEndOfMonth(Calendar.getInstance()));
        StartRangePanel.add(startRangeCalendar);
        EndRangePanel.add(endRangeCalendar);
        monthPanel.setEnabled(false);
        dateString = date;
    }

    /**
     * Pass in the strings that you want to set to returns all nulls if user clicks cancel...
     */
    public Calendar[] getDatesFromDialog(Integer companyId) {
        super.setVisible(true);
        Calendar scal;
        Calendar ecal;
        Calendar[] returnCal = new Calendar[2];
        if (getVals && dateString.equals(WEEK)) {
            scal = StaticDateTimeFunctions.getBegOfWeek(companyId);
            ecal = StaticDateTimeFunctions.getEndOfWeek(companyId);
//            ecal.add(Calendar.WEEK_OF_YEAR, - 1);
            ecal.add(Calendar.DAY_OF_WEEK, 1);

        } else if (getVals) {
            scal = Calendar.getInstance();
            ecal = Calendar.getInstance();

        } else {
            scal = null;
            ecal = null;
        }
        if (specifyRange) {
            scal = StaticDateTimeFunctions.setCalendarTo(startRangeCalendar.getCalendar());
            ecal = StaticDateTimeFunctions.setCalendarTo(endRangeCalendar.getCalendar());
        }
        returnCal[0] = scal;
        returnCal[1] = ecal;
        return returnCal;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        myButtonGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        CardPanel = new javax.swing.JPanel();
        WeekRangeSelection = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        StartRangePanel = new javax.swing.JPanel();
        EndRangePanel = new javax.swing.JPanel();
        bottomPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        selectPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        OkBtn = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        CancelBtn = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Select Dates and Users");
        setMinimumSize(new java.awt.Dimension(138, 190));
        setModal(true);
        setName("DatePromptDialog"); // NOI18N
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        CardPanel.setMaximumSize(new java.awt.Dimension(2147483647, 150));
        CardPanel.setMinimumSize(new java.awt.Dimension(82, 150));
        CardPanel.setPreferredSize(new java.awt.Dimension(82, 150));
        CardPanel.setLayout(new java.awt.CardLayout());

        WeekRangeSelection.setBorder(javax.swing.BorderFactory.createTitledBorder("Specify A Date Range"));
        WeekRangeSelection.setLayout(new java.awt.GridLayout(1, 0));

        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

        jPanel11.setMaximumSize(new java.awt.Dimension(48, 2800));
        jPanel11.setLayout(new java.awt.GridLayout(2, 1));

        jLabel2.setText("Start Date");
        jPanel11.add(jLabel2);

        jLabel3.setText("End Date");
        jPanel11.add(jLabel3);

        jPanel5.add(jPanel11);

        jPanel12.setLayout(new java.awt.GridLayout(2, 1));

        StartRangePanel.setLayout(new java.awt.GridLayout(1, 0));
        jPanel12.add(StartRangePanel);

        EndRangePanel.setLayout(new java.awt.GridLayout(1, 0));
        jPanel12.add(EndRangePanel);

        jPanel5.add(jPanel12);

        WeekRangeSelection.add(jPanel5);

        CardPanel.add(WeekRangeSelection, "rangeCard");

        getContentPane().add(CardPanel);

        bottomPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Select Users"));
        bottomPanel.setLayout(new java.awt.GridLayout(1, 0));

        selectPanel.setMaximumSize(new java.awt.Dimension(6000, 46000));
        selectPanel.setLayout(new javax.swing.BoxLayout(selectPanel, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(selectPanel);

        bottomPanel.add(jScrollPane1);

        getContentPane().add(bottomPanel);

        jPanel3.setMaximumSize(new java.awt.Dimension(32767, 40));
        jPanel3.setMinimumSize(new java.awt.Dimension(10, 40));
        jPanel3.setPreferredSize(new java.awt.Dimension(10, 40));

        OkBtn.setText("OK");
        OkBtn.setMaximumSize(new java.awt.Dimension(80, 24));
        OkBtn.setMinimumSize(new java.awt.Dimension(80, 24));
        OkBtn.setPreferredSize(new java.awt.Dimension(80, 24));
        OkBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OkBtnActionPerformed(evt);
            }
        });
        jPanel3.add(OkBtn);

        jPanel4.setPreferredSize(new java.awt.Dimension(200, 10));
        jPanel3.add(jPanel4);

        CancelBtn.setText("Cancel");
        CancelBtn.setMaximumSize(new java.awt.Dimension(80, 24));
        CancelBtn.setMinimumSize(new java.awt.Dimension(80, 24));
        CancelBtn.setPreferredSize(new java.awt.Dimension(80, 24));
        CancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelBtnActionPerformed(evt);
            }
        });
        jPanel3.add(CancelBtn);

        getContentPane().add(jPanel3);

        setSize(new java.awt.Dimension(410, 419));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void selectMonth(javax.swing.event.ChangeEvent evt) {
        monthPanel.setEnabled(false);
    }

    private void OkBtnActionPerformed(java.awt.event.ActionEvent evt) {
        getVals = true;
        dispose();
    }

    private void CancelBtnActionPerformed(java.awt.event.ActionEvent evt) {
        getVals = false;
        dispose();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CancelBtn;
    private javax.swing.JPanel CardPanel;
    private javax.swing.JPanel EndRangePanel;
    private javax.swing.JButton OkBtn;
    private javax.swing.JPanel StartRangePanel;
    private javax.swing.JPanel WeekRangeSelection;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.ButtonGroup myButtonGroup;
    private javax.swing.JPanel selectPanel;
    // End of variables declaration//GEN-END:variables
}
