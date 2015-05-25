/*
 * GetDatesForPrintDialog.java
 *
 * Created on August 18, 2005, 1:27 PM
 */

package rmischedule.xprint.components;
import java.util.Calendar;
import javax.swing.event.*;
import java.awt.CardLayout;

import rmischedule.components.jcalendar.JCalendarComboBox;
import rmischeduleserver.util.StaticDateTimeFunctions;

/**
 *
 * @author  Ira Juneau
 */
public class GetDatesForPrintDialog extends javax.swing.JDialog {
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

    /** Creates new form GetDatesForPrintDialog */
    public GetDatesForPrintDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        specifyRange = false;
        initialize(parent, modal, WEEK);
    }

    public GetDatesForPrintDialog(java.awt.Frame parent, boolean modal, String val) {
        super(parent, modal);
        specifyRange = false;
        initialize(parent, modal, val);
    }

    public GetDatesForPrintDialog(java.awt.Frame parent, boolean modal, String val, boolean isMultipleWeeks) {
        super(parent, modal);
        specifyRange = isMultipleWeeks;
        initialize(parent, modal, val);
    }

    public void setToPreviousWeek() {
        PreviousWeekRadio.setSelected(true);
    }

    public void setDatesManually(Calendar startDate, Calendar endDate) {
        startRangeCalendar.setCalendar(startDate);
        endRangeCalendar.setCalendar(endDate);
    }

    public void initialize(java.awt.Frame parent, boolean modal, String date) {
        initComponents();
        myButtonGroup.add(PreviousWeekRadio);
        myButtonGroup.add(CurrentWeekRadio);
        myButtonGroup.add(NextWeekRadio);
        myButtonGroup.add(OtherWeekRadio);
        monthPanel = new JCalendarComboBox();
        startRangeCalendar = new JCalendarComboBox(StaticDateTimeFunctions.getBegOfMonth(Calendar.getInstance()));
        endRangeCalendar = new JCalendarComboBox(StaticDateTimeFunctions.getEndOfMonth(Calendar.getInstance()));
        StartRangePanel.add(startRangeCalendar);
        EndRangePanel.add(endRangeCalendar);
        monthPanel.setEnabled(false);
        InsertHere.add(monthPanel);
        dateString = date;
        if (dateString.equals(WEEK)) {
            PreviousWeekRadio.setText("Previous " + dateString);
        } else {
            PreviousWeekRadio.setText("Yesterday");
        }
        CurrentWeekRadio.setText("Current " + dateString);
        if (dateString.equals(WEEK)) {
            NextWeekRadio.setText("Next " + dateString);
        } else {
            NextWeekRadio.setText("Tommorrow");
        }
        if (specifyRange) {
            CardLayout myLayout = (CardLayout)CardPanel.getLayout();
            myLayout.show(CardPanel, "rangeCard");
            SpecifyWeekPanel.setVisible(false);
        }
        OtherWeekRadio.setText("Other " + dateString);
        CalendarPanel.setBorder(new javax.swing.border.TitledBorder("Other " + dateString));
    }

    /**
     * Pass in the strings that you want to set to returns all nulls if user clicks cancel...
     */
    public Calendar[] getDatesFromDialog(Integer companyId) {
        super.setVisible(true);
        Calendar scal;
        Calendar ecal;
        Calendar[] returnCal = new Calendar[2];
        if (getVals  && dateString.equals(WEEK)) {
            scal = StaticDateTimeFunctions.getBegOfWeek(companyId);
            ecal = StaticDateTimeFunctions.getEndOfWeek(companyId);
//            ecal.add(Calendar.WEEK_OF_YEAR, - 1);
            ecal.add(Calendar.DAY_OF_WEEK,  1);
            if (NextWeekRadio.isSelected()) {
                scal.add(Calendar.WEEK_OF_YEAR, 1);
                ecal.add(Calendar.WEEK_OF_YEAR, 1);
            } else if (PreviousWeekRadio.isSelected()) {
                scal.add(Calendar.WEEK_OF_YEAR, -1);
                ecal.add(Calendar.WEEK_OF_YEAR, -1);
            } else if (OtherWeekRadio.isSelected()) {
                scal = StaticDateTimeFunctions.getBegOfWeek(StaticDateTimeFunctions.setCalendarTo(monthPanel.getCalendar()), companyId);
                ecal = StaticDateTimeFunctions.getEndOfWeek(StaticDateTimeFunctions.setCalendarTo(monthPanel.getCalendar()), companyId);
                ecal.add(Calendar.DAY_OF_YEAR, 1);
            }
        } else if (getVals) {
            scal = Calendar.getInstance();
            ecal = Calendar.getInstance();
            if (NextWeekRadio.isSelected()) {
                scal.add(Calendar.DAY_OF_YEAR, 1);
                ecal.add(Calendar.DAY_OF_YEAR, 1);
            } else if (PreviousWeekRadio.isSelected()) {
                scal.add(Calendar.DAY_OF_YEAR, -1);
                ecal.add(Calendar.DAY_OF_YEAR, -1);
            } else if (OtherWeekRadio.isSelected()) {
                Calendar tempCal = StaticDateTimeFunctions.setCalendarTo(monthPanel.getCalendar());
                scal = tempCal;
                //scal.add(Calendar.DAY_OF_YEAR, -1);
                //tempCal.add(Calendar.DAY_OF_YEAR, 1);
                ecal = StaticDateTimeFunctions.setCalendarTo(tempCal);
                ecal.add(Calendar.DAY_OF_YEAR, 1);
            }
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
        SpecifyWeekPanel = new javax.swing.JPanel();
        PreviousWeekRadio = new javax.swing.JRadioButton();
        CurrentWeekRadio = new javax.swing.JRadioButton();
        NextWeekRadio = new javax.swing.JRadioButton();
        CardPanel = new javax.swing.JPanel();
        OtherWeekSelection = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        CalendarPanel = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        OtherWeekRadio = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        InsertHere = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        WeekRangeSelection = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        StartRangePanel = new javax.swing.JPanel();
        EndRangePanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        OkBtn = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        CancelBtn = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Select Dates");
        setMinimumSize(new java.awt.Dimension(138, 190));
        setModal(true);
        setName("DatePromptDialog"); // NOI18N
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        SpecifyWeekPanel.setMaximumSize(new java.awt.Dimension(32767, 50));
        SpecifyWeekPanel.setMinimumSize(new java.awt.Dimension(10, 50));
        SpecifyWeekPanel.setPreferredSize(new java.awt.Dimension(10, 50));
        SpecifyWeekPanel.setLayout(new java.awt.GridLayout(1, 0));

        PreviousWeekRadio.setText("Previous Week");
        SpecifyWeekPanel.add(PreviousWeekRadio);

        CurrentWeekRadio.setSelected(true);
        CurrentWeekRadio.setText("Current Week");
        SpecifyWeekPanel.add(CurrentWeekRadio);

        NextWeekRadio.setText("Next Week");
        SpecifyWeekPanel.add(NextWeekRadio);

        getContentPane().add(SpecifyWeekPanel);

        CardPanel.setLayout(new java.awt.CardLayout());

        OtherWeekSelection.setMaximumSize(new java.awt.Dimension(1000, 10000));
        OtherWeekSelection.setLayout(new java.awt.GridLayout(1, 0));

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        CalendarPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Other Week"));
        CalendarPanel.setLayout(new javax.swing.BoxLayout(CalendarPanel, javax.swing.BoxLayout.LINE_AXIS));

        jPanel6.setMaximumSize(new java.awt.Dimension(100, 32767));
        jPanel6.setMinimumSize(new java.awt.Dimension(100, 33));
        jPanel6.setPreferredSize(new java.awt.Dimension(100, 33));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.Y_AXIS));

        jPanel8.setMaximumSize(new java.awt.Dimension(32767, 33));
        jPanel8.setLayout(new java.awt.GridLayout(1, 0));

        OtherWeekRadio.setText("Other Week");
        OtherWeekRadio.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                selectMonth(evt);
            }
        });
        jPanel8.add(OtherWeekRadio);

        jPanel6.add(jPanel8);
        jPanel6.add(jPanel7);

        CalendarPanel.add(jPanel6);

        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.Y_AXIS));

        InsertHere.setMaximumSize(new java.awt.Dimension(32767, 26));
        InsertHere.setMinimumSize(new java.awt.Dimension(10, 26));
        InsertHere.setPreferredSize(new java.awt.Dimension(10, 26));
        InsertHere.setLayout(new java.awt.GridLayout(1, 0));
        jPanel9.add(InsertHere);
        jPanel9.add(jPanel10);

        CalendarPanel.add(jPanel9);

        jPanel2.add(CalendarPanel);

        OtherWeekSelection.add(jPanel2);

        CardPanel.add(OtherWeekSelection, "card2");

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

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-410)/2, (screenSize.height-220)/2, 410, 220);
    }// </editor-fold>//GEN-END:initComponents

    private void selectMonth(javax.swing.event.ChangeEvent evt) {
        if (OtherWeekRadio.isSelected()) {
            monthPanel.setEnabled(true);
        } else {
            monthPanel.setEnabled(false);
        }
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
    private javax.swing.JPanel CalendarPanel;
    private javax.swing.JButton CancelBtn;
    private javax.swing.JPanel CardPanel;
    private javax.swing.JRadioButton CurrentWeekRadio;
    private javax.swing.JPanel EndRangePanel;
    private javax.swing.JPanel InsertHere;
    private javax.swing.JRadioButton NextWeekRadio;
    private javax.swing.JButton OkBtn;
    private javax.swing.JRadioButton OtherWeekRadio;
    private javax.swing.JPanel OtherWeekSelection;
    private javax.swing.JRadioButton PreviousWeekRadio;
    private javax.swing.JPanel SpecifyWeekPanel;
    private javax.swing.JPanel StartRangePanel;
    private javax.swing.JPanel WeekRangeSelection;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.ButtonGroup myButtonGroup;
    // End of variables declaration//GEN-END:variables

}
