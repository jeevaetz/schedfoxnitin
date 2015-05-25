/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SetupTimeOffIntervalDialog.java
 *
 * Created on Jun 2, 2011, 3:23:50 PM
 */
package rmischedule.timeoff;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import rmischedule.main.Main_Window;
import rmischeduleserver.control.EmployeeController;
import rmischeduleserver.control.TimeOffController;
import schedfoxlib.model.EmployeeTypes;
import schedfoxlib.model.TimeOffAccrual;
import schedfoxlib.model.TimeOffSeries;
import schedfoxlib.model.TimeOffType;

/**
 *
 * @author user
 */
public class SetupTimeOffIntervalDialog extends javax.swing.JDialog {

    private AccrualTableModel accrualTableModel = new AccrualTableModel();
    private String compId;

    /** Creates new form SetupTimeOffIntervalDialog */
    public SetupTimeOffIntervalDialog(java.awt.Frame parent, boolean modal, String companyId) {
        super(parent, modal);
        initComponents();

        this.compId = companyId;

        try {
            EmployeeController empController = EmployeeController.getInstance(compId);
            ArrayList<EmployeeTypes> empTypes = empController.getEmployeeTypes();
            empTypeCombo.removeAllItems();
            for (int t = 0; t < empTypes.size(); t++) {
                empTypeCombo.addItem(empTypes.get(t));
            }
        } catch (Exception e) {
        }
        this.refreshTimeOffSeries();
    }

    private void refreshTimeOffSeries() {
        try {
            EmployeeTypes selType = (EmployeeTypes)empTypeCombo.getSelectedItem();
            String timeoffType = (String)typeCombo.getSelectedItem();

            TimeOffController timeOffController = TimeOffController.getInstance(compId);
            ArrayList<TimeOffSeries> timeOffSeries =
                    timeOffController.getAssignedTimeOffSeries(selType, timeoffType);

            seriesIdTxt.setText("");
            if (timeOffSeries.size() > 0) {
                refreshSeriesAccruals(timeOffSeries.get(0));
            } else {
                accrualTableModel.clearData();
                this.clearAccrualsTextData();

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                    "Could not retrieve time off information!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshSeriesAccruals(TimeOffSeries timeOffSeries) {
        timeOffSeries.forceRefreshAccruals();
        seriesIdTxt.setText(timeOffSeries.getTimeOffSeriesId().toString());

        ArrayList<TimeOffAccrual> timeOffAccrual = timeOffSeries.getTimeOffAccruals(compId);
        accrualTableModel.clearData();
        for (int a = 0; a < timeOffAccrual.size(); a++) {
            accrualTableModel.addAccrual(timeOffAccrual.get(a));
        }
        this.clearAccrualsTextData();
    }

    /**
     * Clears out the accrual fields
     */
    private void clearAccrualsTextData() {
        startIntervalTxt.setText("");
        endIntervalTxt.setText("");
        daysOffTxt.setText("");
        intervalTxt.setText("");
        idTxt.setText("");


        daysResetChk.setSelected(true);
        startRangeTypeCombo.setSelectedIndex(0);
        endRangeTypeCombo.setSelectedIndex(0);
        rangeTypeCombo.setSelectedIndex(0);
    }

    private void displayAccrual(TimeOffAccrual timeOff) {
        idTxt.setText(timeOff.getTimeOffAccrualId().toString());
        daysOffTxt.setText(timeOff.getDays().toString());
        try {
            String timeOffInterval = timeOff.getTimeOffInterval();
            Integer numberDays =
                    Integer.parseInt(timeOffInterval.substring(0, timeOffInterval.indexOf(" ")));
            intervalTxt.setText(numberDays.toString());

            daysResetChk.setSelected(timeOff.isTimeOffAccrual());

            String intervalType = timeOffInterval.substring(timeOffInterval.indexOf(" ") + 1);
            rangeTypeCombo.setSelectedItem(convertRange(intervalType));

            String startInterval = timeOff.getStartInterval();
            if (startInterval.equals("00:00:00")) {
                startInterval = "0 day";
            }
            Integer startDays =
                    Integer.parseInt(startInterval.substring(0, startInterval.indexOf(" ")));
            startIntervalTxt.setText(startDays.toString());
            String startType = startInterval.substring(startInterval.indexOf(" ") + 1);
            startRangeTypeCombo.setSelectedItem(convertRange(startType));

            String endInterval = timeOff.getEndInterval();
            if (endInterval.equals("00:00:00")) {
                endInterval = "0 day";
            }
            Integer endDays =
                    Integer.parseInt(endInterval.substring(0, endInterval.indexOf(" ")));
            endIntervalTxt.setText(endDays.toString());
            String endType = endInterval.substring(endInterval.indexOf(" ") + 1);
            endRangeTypeCombo.setSelectedItem(convertRange(endType));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String convertRange(String rangeIn) {
        String retVal = "";
        if (rangeIn.equalsIgnoreCase("mons") || rangeIn.equalsIgnoreCase("mon")) {
            retVal = "Month";
        } else if (rangeIn.equalsIgnoreCase("days") || rangeIn.equalsIgnoreCase("day")) {
            retVal = "Day";
        } else if (rangeIn.equalsIgnoreCase("years") || rangeIn.equalsIgnoreCase("year")) {
            retVal = "Year";
        } else {
            retVal = "Week";
        }
        return retVal;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        idTxt = new javax.swing.JTextField();
        seriesIdTxt = new javax.swing.JTextField();
        timeOffAssignment = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        empTypeCombo = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        typeCombo = new javax.swing.JComboBox();
        accrualPanel = new javax.swing.JPanel();
        firstRowPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        startIntervalTxt = new javax.swing.JTextField();
        startRangeTypeCombo = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        endIntervalTxt = new javax.swing.JTextField();
        endRangeTypeCombo = new javax.swing.JComboBox();
        daysResetChk = new javax.swing.JCheckBox();
        secondRowPanel = new javax.swing.JPanel();
        daysOffTxt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        intervalTxt = new javax.swing.JTextField();
        rangeTypeCombo = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        deleteAccrualBtn = new javax.swing.JButton();
        saveAccrualBtn = new javax.swing.JButton();
        seriesTable = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        accrualTable = new javax.swing.JTable();
        bottomPanel = new javax.swing.JPanel();
        closeBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Setup Time Accrual");
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        timeOffAssignment.setMaximumSize(new java.awt.Dimension(32767, 35));
        timeOffAssignment.setMinimumSize(new java.awt.Dimension(10, 35));
        timeOffAssignment.setPreferredSize(new java.awt.Dimension(100, 35));
        timeOffAssignment.setLayout(new javax.swing.BoxLayout(timeOffAssignment, javax.swing.BoxLayout.LINE_AXIS));

        jLabel6.setText("Employee Type");
        jLabel6.setMaximumSize(new java.awt.Dimension(90, 20));
        jLabel6.setMinimumSize(new java.awt.Dimension(90, 20));
        jLabel6.setPreferredSize(new java.awt.Dimension(90, 20));
        timeOffAssignment.add(jLabel6);

        empTypeCombo.setMaximumSize(new java.awt.Dimension(32767, 26));
        empTypeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                empTypeComboActionPerformed(evt);
            }
        });
        timeOffAssignment.add(empTypeCombo);

        jLabel7.setText("Type");
        jLabel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        jLabel7.setMaximumSize(new java.awt.Dimension(90, 20));
        jLabel7.setMinimumSize(new java.awt.Dimension(90, 20));
        jLabel7.setPreferredSize(new java.awt.Dimension(90, 20));
        timeOffAssignment.add(jLabel7);

        typeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Vacation", "Personal", "Sick Day" }));
        typeCombo.setMaximumSize(new java.awt.Dimension(32767, 26));
        typeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeComboActionPerformed(evt);
            }
        });
        timeOffAssignment.add(typeCombo);

        getContentPane().add(timeOffAssignment);

        accrualPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Series Accrual Settings"));
        accrualPanel.setLayout(new javax.swing.BoxLayout(accrualPanel, javax.swing.BoxLayout.Y_AXIS));

        firstRowPanel.setMaximumSize(new java.awt.Dimension(32767, 35));
        firstRowPanel.setMinimumSize(new java.awt.Dimension(0, 35));
        firstRowPanel.setPreferredSize(new java.awt.Dimension(373, 35));
        firstRowPanel.setLayout(new javax.swing.BoxLayout(firstRowPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel4.setText("Begins From:");
        jLabel4.setMaximumSize(new java.awt.Dimension(80, 16));
        firstRowPanel.add(jLabel4);

        startIntervalTxt.setMaximumSize(new java.awt.Dimension(40, 28));
        startIntervalTxt.setMinimumSize(new java.awt.Dimension(40, 28));
        startIntervalTxt.setPreferredSize(new java.awt.Dimension(40, 28));
        firstRowPanel.add(startIntervalTxt);

        startRangeTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Day", "Week", "Month", "Year" }));
        startRangeTypeCombo.setMaximumSize(new java.awt.Dimension(160, 28));
        firstRowPanel.add(startRangeTypeCombo);

        jLabel5.setText("Ends At:");
        jLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        jLabel5.setMaximumSize(new java.awt.Dimension(80, 16));
        firstRowPanel.add(jLabel5);

        endIntervalTxt.setMaximumSize(new java.awt.Dimension(40, 28));
        endIntervalTxt.setMinimumSize(new java.awt.Dimension(40, 28));
        endIntervalTxt.setPreferredSize(new java.awt.Dimension(40, 28));
        firstRowPanel.add(endIntervalTxt);

        endRangeTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Day", "Week", "Month", "Year" }));
        endRangeTypeCombo.setMaximumSize(new java.awt.Dimension(160, 28));
        firstRowPanel.add(endRangeTypeCombo);

        daysResetChk.setSelected(true);
        daysResetChk.setText("Days Reset");
        firstRowPanel.add(daysResetChk);

        accrualPanel.add(firstRowPanel);

        secondRowPanel.setMaximumSize(new java.awt.Dimension(32767, 35));
        secondRowPanel.setMinimumSize(new java.awt.Dimension(0, 35));
        secondRowPanel.setPreferredSize(new java.awt.Dimension(373, 35));
        secondRowPanel.setLayout(new javax.swing.BoxLayout(secondRowPanel, javax.swing.BoxLayout.LINE_AXIS));

        daysOffTxt.setMaximumSize(new java.awt.Dimension(40, 28));
        daysOffTxt.setMinimumSize(new java.awt.Dimension(40, 28));
        daysOffTxt.setPreferredSize(new java.awt.Dimension(40, 28));
        secondRowPanel.add(daysOffTxt);

        jLabel2.setText("days off for every");
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 4, 0, 0));
        jLabel2.setMaximumSize(new java.awt.Dimension(100, 28));
        jLabel2.setMinimumSize(new java.awt.Dimension(100, 16));
        jLabel2.setPreferredSize(new java.awt.Dimension(100, 16));
        secondRowPanel.add(jLabel2);

        intervalTxt.setMaximumSize(new java.awt.Dimension(40, 28));
        intervalTxt.setMinimumSize(new java.awt.Dimension(40, 28));
        intervalTxt.setPreferredSize(new java.awt.Dimension(40, 28));
        secondRowPanel.add(intervalTxt);

        rangeTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Day", "Week", "Month", "Year" }));
        rangeTypeCombo.setMaximumSize(new java.awt.Dimension(160, 28));
        secondRowPanel.add(rangeTypeCombo);

        jLabel3.setText("worked");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 4, 0, 0));
        jLabel3.setMaximumSize(new java.awt.Dimension(50, 30));
        jLabel3.setMinimumSize(new java.awt.Dimension(50, 16));
        jLabel3.setPreferredSize(new java.awt.Dimension(50, 16));
        secondRowPanel.add(jLabel3);

        deleteAccrualBtn.setText("Delete");
        deleteAccrualBtn.setMaximumSize(new java.awt.Dimension(110, 28));
        deleteAccrualBtn.setMinimumSize(new java.awt.Dimension(75, 28));
        deleteAccrualBtn.setPreferredSize(new java.awt.Dimension(75, 28));
        deleteAccrualBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteAccrualBtnActionPerformed(evt);
            }
        });
        secondRowPanel.add(deleteAccrualBtn);

        saveAccrualBtn.setText("Save");
        saveAccrualBtn.setMaximumSize(new java.awt.Dimension(110, 28));
        saveAccrualBtn.setMinimumSize(new java.awt.Dimension(75, 28));
        saveAccrualBtn.setPreferredSize(new java.awt.Dimension(75, 28));
        saveAccrualBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAccrualBtnActionPerformed(evt);
            }
        });
        secondRowPanel.add(saveAccrualBtn);

        accrualPanel.add(secondRowPanel);

        seriesTable.setLayout(new java.awt.GridLayout(1, 0));

        accrualTable.setModel(accrualTableModel);
        accrualTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                accrualTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(accrualTable);

        seriesTable.add(jScrollPane2);

        accrualPanel.add(seriesTable);

        getContentPane().add(accrualPanel);

        bottomPanel.setMaximumSize(new java.awt.Dimension(32767, 35));
        bottomPanel.setMinimumSize(new java.awt.Dimension(0, 35));
        bottomPanel.setPreferredSize(new java.awt.Dimension(373, 35));
        bottomPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        closeBtn.setText("Close");
        closeBtn.setMaximumSize(new java.awt.Dimension(80, 28));
        closeBtn.setMinimumSize(new java.awt.Dimension(80, 28));
        closeBtn.setPreferredSize(new java.awt.Dimension(80, 28));
        closeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeBtnActionPerformed(evt);
            }
        });
        bottomPanel.add(closeBtn);

        getContentPane().add(bottomPanel);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-561)/2, (screenSize.height-439)/2, 561, 439);
    }// </editor-fold>//GEN-END:initComponents

    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeBtnActionPerformed
        this.dispose();
    }//GEN-LAST:event_closeBtnActionPerformed

    private void saveAccrualBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAccrualBtnActionPerformed
        try {
            TimeOffController timeOffController = TimeOffController.getInstance(compId);

            TimeOffAccrual timeAccrual = new TimeOffAccrual();
            try {
                timeAccrual.setTimeOffAccrualId(Integer.parseInt(idTxt.getText()));
            } catch (Exception e) {
            }
            timeAccrual.setActive(true);
            try {
                timeAccrual.setDays(Integer.parseInt(daysOffTxt.getText()));
            } catch (Exception e) {
                throw new Exception("Invalid day! Please enter a valid number!");
            }
            int timeRangeValue = 0;
            try {
                timeRangeValue = Integer.parseInt(intervalTxt.getText());
            } catch (Exception e) {
                throw new Exception("Invalid time off, please enter a valid number!");
            }
            timeAccrual.setTimeOffInterval(timeRangeValue + " " + rangeTypeCombo.getSelectedItem());

            int startTimeRangeValue = 0;
            try {
                startTimeRangeValue = Integer.parseInt(startIntervalTxt.getText());
            } catch (Exception e) {
                throw new Exception("Invalid start time, please enter a valid number!");
            }
            timeAccrual.setStartInterval(startTimeRangeValue + " " + startRangeTypeCombo.getSelectedItem());

            timeAccrual.setTimeOffAccrual(daysResetChk.isSelected());

            int endTimeRangeValue = 0;
            try {
                endTimeRangeValue = Integer.parseInt(endIntervalTxt.getText());
            } catch (Exception e) {
                throw new Exception("Invalid start time, please enter a valid number!");
            }
            timeAccrual.setEndInterval(endTimeRangeValue + " " + endRangeTypeCombo.getSelectedItem());

            TimeOffSeries timeOffSeries = new TimeOffSeries();
            try {
                timeAccrual.setTimeOffSeriesId(Integer.parseInt(seriesIdTxt.getText()));
                timeOffSeries.setTimeOffSeriesId(Integer.parseInt(seriesIdTxt.getText()));
            } catch (Exception e) {
                EmployeeTypes selType = (EmployeeTypes)empTypeCombo.getSelectedItem();
                timeOffSeries.setActive(true);
                timeOffSeries.setEmployeeTypeId(selType.getEmployeeTypeId());
                String timeOffType = (String)typeCombo.getSelectedItem();
                if (timeOffType.equals("Vacation")) {
                    timeOffSeries.setTimeOffTypeId(2);
                } else if (timeOffType.equals("Sick Day")) {
                    timeOffSeries.setTimeOffTypeId(3);
                } else {
                    timeOffSeries.setTimeOffTypeId(1);
                }
                timeOffSeries.setTimeOffSeries(compId);
                timeOffSeries = timeOffController.saveNewTimeOffSeries(timeOffSeries);
                seriesIdTxt.setText(timeOffSeries.getTimeOffSeriesId() + "");
                timeAccrual.setTimeOffSeriesId(Integer.parseInt(seriesIdTxt.getText()));
            }
            
            timeOffController.saveTimeOffAccrual(timeAccrual);
            this.refreshSeriesAccruals(timeOffSeries);
            clearAccrualsTextData();
        } catch (Exception exe) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, exe.getMessage(),
                    "Error Saving!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveAccrualBtnActionPerformed

    private void accrualTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accrualTableMouseClicked
        TimeOffAccrual timeAccrual = accrualTableModel.getAccrual(accrualTable.getSelectedRow());
        this.displayAccrual(timeAccrual);
    }//GEN-LAST:event_accrualTableMouseClicked

    private void empTypeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_empTypeComboActionPerformed
        this.refreshTimeOffSeries();
    }//GEN-LAST:event_empTypeComboActionPerformed

    private void typeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeComboActionPerformed
        this.refreshTimeOffSeries();
    }//GEN-LAST:event_typeComboActionPerformed

    private void deleteAccrualBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteAccrualBtnActionPerformed
        int confirm = JOptionPane.showConfirmDialog(Main_Window.parentOfApplication,
                "Delete this accrual?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                TimeOffController timeOffController = TimeOffController.getInstance(compId);
                TimeOffAccrual timeAccrual = new TimeOffAccrual();
                timeAccrual.setTimeOffAccrualId(Integer.parseInt(idTxt.getText()));
                timeOffController.deleteAccrual(timeAccrual);
                
                TimeOffSeries timeOffSeries = new TimeOffSeries();
                timeOffSeries.setTimeOffSeriesId(Integer.parseInt(seriesIdTxt.getText()));
                this.refreshSeriesAccruals(timeOffSeries);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                        "Could not remove accrual!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_deleteAccrualBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel accrualPanel;
    private javax.swing.JTable accrualTable;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JButton closeBtn;
    private javax.swing.JTextField daysOffTxt;
    private javax.swing.JCheckBox daysResetChk;
    private javax.swing.JButton deleteAccrualBtn;
    private javax.swing.JComboBox empTypeCombo;
    private javax.swing.JTextField endIntervalTxt;
    private javax.swing.JComboBox endRangeTypeCombo;
    private javax.swing.JPanel firstRowPanel;
    private javax.swing.JTextField idTxt;
    private javax.swing.JTextField intervalTxt;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox rangeTypeCombo;
    private javax.swing.JButton saveAccrualBtn;
    private javax.swing.JPanel secondRowPanel;
    private javax.swing.JTextField seriesIdTxt;
    private javax.swing.JPanel seriesTable;
    private javax.swing.JTextField startIntervalTxt;
    private javax.swing.JComboBox startRangeTypeCombo;
    private javax.swing.JPanel timeOffAssignment;
    private javax.swing.JComboBox typeCombo;
    // End of variables declaration//GEN-END:variables
}
