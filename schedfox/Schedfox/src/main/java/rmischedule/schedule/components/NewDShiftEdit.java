/*
 * NewDShiftEdit.java
 *
 * Created on April 5, 2005, 12:07 PM
 */
package rmischedule.schedule.components;

import schedfoxlib.model.ShiftOptionsClass;
import schedfoxlib.model.ShiftTypeClass;
import rmischeduleserver.util.StaticDateTimeFunctions;
import com.creamtec.ajaxswing.AjaxSwingManager;
import com.creamtec.ajaxswing.core.AjaxSwingProperties;
import java.awt.BorderLayout;
import rmischedule.main.Main_Window;
import rmischedule.security.*;
import rmischedule.schedule.Schedule_View_Panel;
import rmischedule.schedule.components.graphiccomponents.*;
import rmischedule.components.graphicalcomponents.*;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
import rmischedule.components.*;
import rmischedule.schedule.history.*;

import java.awt.Dimension;
import java.awt.CardLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.event.*;

/**
 *
 * @author ira
 */
public class NewDShiftEdit extends javax.swing.JDialog {

    private TimeEditTextField myEntryTextField;
    private TimeEditTextField myExitTextField;
    private TimeEditTextField myBillBreakStart;
    private TimeEditTextField myPayBreakStart;
    private Rate_Code_LB lbRateCode;
    private DShift editingShift;
    private SShift editingSShift;
    public Schedule_View_Panel svParent;
    private int trainerId;
    private String trainerName;
    private HistoryDetailPanel myHistPanel;
    private JCheckBox[] myWeekCheckBoxes = {new JCheckBox("Mon"), new JCheckBox("Tues"), new JCheckBox("Wed"),
        new JCheckBox("Thur"), new JCheckBox("Fri"), new JCheckBox("Sat"),
        new JCheckBox("Sun")};
    private int[] dow;

    /**
     * Creates new form NewDShiftEdit
     */
    public NewDShiftEdit(Main_Window parent, boolean modal, Schedule_View_Panel p, SShift sp) {
        super(parent, modal);
        svParent = p;
        lbRateCode = new Rate_Code_LB(p.getCompany());

        initComponents();

        pnEmpBillStart.setVisible(false);
        pnLocationBreakStart.setVisible(false);

        try {
            cbPayOvertime.setVisible(Main_Window.parentOfApplication.isUserAMemberOfGroups(p.getConnection(), "ADMIN", "Payroll"));
            cbBillOT.setVisible(Main_Window.parentOfApplication.isUserAMemberOfGroups(p.getConnection(), "ADMIN", "Payroll"));
        } catch (Exception exe) {
        }

        Calendar startDate
                = StaticDateTimeFunctions.getBegOfWeek(Calendar.getInstance(), Integer.parseInt(svParent.getCompany()));
        myWeekCheckBoxes = new JCheckBox[7];
        dow = new int[7];
        SimpleDateFormat myFormat = new SimpleDateFormat("EEE");
        for (int i = 0; i < 7; i++) {
            myWeekCheckBoxes[i] = new JCheckBox(myFormat.format(startDate.getTime()));
            dow[i] = startDate.get(Calendar.DAY_OF_WEEK);
            startDate.add(Calendar.DAY_OF_WEEK, 1);
        }

        pnRateCode.add(lbRateCode);

        RadioButtonGroup.add(DeleteFutureWeekRadio);
        RadioButtonGroup.add(DeleteCurrentWeekRadio);
        RadioButtonGroup.add(ApplyToCurrentWeekRadio);
        RadioButtonGroup.add(ApplyToFutureWeekRadio);

        for (int i = 0; i < myWeekCheckBoxes.length; i++) {
            myWeekCheckBoxes[i].setOpaque(false);
            DaysOfWeekPanel.add(myWeekCheckBoxes[i]);
        }
        if (sp.myShift instanceof DShift || sp.getClient() instanceof SClient) {
            updateValues(sp);
        }
        myHistPanel = new HistoryDetailPanel(this);
        HistoryPanel.add(myHistPanel);
        myTabbedPane.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (myTabbedPane.getSelectedIndex() == 2) {
                    try {
                        myHistPanel.populateDataWithGivenGroup(editingShift.getRealShiftId());
                        DateTrainCardPanel.setMaximumSize(new Dimension(10000, 0));
                        DateTrainCardPanel.setPreferredSize(new Dimension(100, 0));
                        DateTrainCardPanel.setMinimumSize(new Dimension(0, 0));
                    } catch (Exception exe) {
                    }
                } else {
                    DateTrainCardPanel.setMaximumSize(new Dimension(32767, 660));
                    DateTrainCardPanel.setPreferredSize(new Dimension(269, 100));
                    DateTrainCardPanel.setMinimumSize(new Dimension(269, 100));
                }
            }
        });

    }

    public DShift getEditingShift() {
        return this.editingShift;
    }

    /**
     * Called on loading the window, clears old data and displays new data
     * depending on DShift info from the SShift clicked
     */
    public void updateValues(SShift sp) {
        myEntryTextField = new TimeEditTextField();
        myExitTextField = new TimeEditTextField();
        myBillBreakStart = new TimeEditTextField();
        myPayBreakStart = new TimeEditTextField();

        if (sp.myShift != null) {
            try {
                setTitle(sp.myShift.getDateString() + " - " + sp.myShift.getClient().getFullClientName());
            } catch (Exception exe) {
                setTitle("Edit Shift");
            }
        } else {
            setTitle("Edit Shift");
        }

        if (sp.myShift instanceof DShift || sp.getClient() instanceof SClient) {
            editingSShift = sp;
            DShift currShift = (DShift) sp.myShift;
            try {
                if (sp.myShift != null) {
                    editingShift = currShift.clone();
                } else {
                    editingShift = null;
                }
            } catch (Exception e) {
            }

            EntryPanel.removeAll();
            ExitPanel.removeAll();
            EntryPanel.add(myEntryTextField);
            ExitPanel.add(myExitTextField);
            pnLocationBreakStart.removeAll();
            pnEmpBillStart.removeAll();
            pnLocationBreakStart.add(myBillBreakStart, BorderLayout.CENTER);
            pnEmpStart.add(myPayBreakStart, BorderLayout.CENTER);

            cbConfirm.setVisible(false);

            //Show all radio buttons initially in case they have been hidden
            DeleteFutureWeekRadio.setVisible(true);
            DeleteCurrentWeekRadio.setVisible(true);
            ApplyToCurrentWeekRadio.setVisible(true);
            ApplyToFutureWeekRadio.setVisible(true);

            CardLayout myLayout = (CardLayout) DateTrainCardPanel.getLayout();
            trainerId = 0;
            if (sp.myShift != null) {
                DeleteCurrentWeekRadio.setVisible(true);
                if (sp.myShift.getType() != null) {
                    if (sp.myShift.getType().isShiftType(ShiftTypeClass.SHIFT_UNCONFIRMED)
                            && svParent.shouldMarkUnconShifts()) {
                        cbConfirm.setSelected(false);
                        cbConfirm.setVisible(true);
                    }
                    if (sp.myShift.getType().isShiftType(ShiftTypeClass.SHIFT_EXTRA_COVERAGE_SHIFT)) {
                        cbExtraCoverage.setSelected(true);
                    }
                    if (sp.myShift.getType().isShiftType(ShiftTypeClass.SHIFT_TRAINING_SHIFT)
                            || sp.myShift.getType().isShiftType(ShiftTypeClass.SHIFT_TRAINER_SHIFT)) {
                        if (sp.myShift.getType().isShiftType(ShiftTypeClass.SHIFT_TRAINING_SHIFT)) {
                            cbTraining.setSelected(true);
                        } else {
                            cbTrainer.setSelected(true);
                        }
                        trainerId = sp.myShift.getType().getTrainerId();
                        showTrainingPanel();
                        setTrainerInfo(trainerId, svParent.hash_employees.get(new Integer(trainerId)).getName());
                    } else {
                        cbTraining.setSelected(false);
                        cbTrainer.setSelected(false);
                        myLayout.show(DateTrainCardPanel, "dateranges");
                    }
                    if (sp.myShift.isMaster()) {
                        DeleteFutureWeekRadio.setVisible(true);
                        ApplyToFutureWeekRadio.setText("Apply Shift Changes For Current And Future Weeks");
                        DeleteCurrentWeekRadio.setText("Delete Shift For This Week Only");
                    } else {
                        ApplyToFutureWeekRadio.setText("Apply Shift Changes And Make This Shift Permanent");
                        DeleteCurrentWeekRadio.setText("Delete Current Shift");
                        DeleteFutureWeekRadio.setVisible(false);
                    }
                } else {
                    cbConfirm.setSelected(false);
                    cbTraining.setSelected(false);
                    cbTrainer.setSelected(false);
                    myLayout.show(DateTrainCardPanel, "dateranges");

                }
                cbDoNotBill.setSelected((sp.myShift.getType().isShiftType(ShiftTypeClass.SHIFT_NON_BILLABLE)));
                cbDoNotPay.setSelected((sp.myShift.getType().isShiftType(ShiftTypeClass.SHIFT_NON_PAYABLE)));

                cbDoNotBill.setEnabled(Main_Window.parentOfApplication.isUserAMemberOfGroups(svParent.getConnection(), "ADMIN", "Payroll", "Scheduling Manager"));
                cbDoNotPay.setEnabled(Main_Window.parentOfApplication.isUserAMemberOfGroups(svParent.getConnection(), "ADMIN", "Payroll", "Scheduling Manager"));
                
                cbPayOvertime.setSelected(false);
                cbBillOT.setSelected(false);
                
                try {
                    cbPayOvertime.setSelected(sp.myShift.getType().isShiftType(ShiftTypeClass.SHIFT_PAYABLE_OT));
                    cbBillOT.setSelected(sp.myShift.getType().isShiftType(ShiftTypeClass.SHIFT_BILLABLE_OT));
                } catch (Exception exe) {
                }

                cbTrainer.setSelected((sp.myShift.getType().isShiftType(ShiftTypeClass.SHIFT_TRAINER_SHIFT)));
                cbTraining.setSelected((sp.myShift.getType().isShiftType(ShiftTypeClass.SHIFT_TRAINING_SHIFT)));
                cbExtraCoverage.setSelected((sp.myShift.getType().isShiftType(ShiftTypeClass.SHIFT_EXTRA_COVERAGE_SHIFT)));

                if (currShift.getRateCode() > 0) {
                    lbRateCode.setRateCode(String.valueOf(currShift.getRateCode()));
                } else {
                    lbRateCode.setDefault();
                }

                efEmpBreakLen.setText(String.valueOf(currShift.getPayBreakLength()));
                efLocBreakLen.setText(String.valueOf(currShift.getBillBreakLength()));

                if (currShift.getPayBreakLength() > 0) {
                    pnEmpBreakTime.setVisible(true);
                    cbHasPaidBreak.setSelected(true);
                } else {
                    pnEmpBreakTime.setVisible(false);
                    cbHasPaidBreak.setSelected(false);
                }

                if (currShift.getBillBreakLength() > 0) {
                    pnLocationBreakBill.setVisible(true);
                    cbHasBreakBilled.setSelected(true);
                } else {
                    pnLocationBreakBill.setVisible(false);
                    cbHasBreakBilled.setSelected(false);
                }

                myBillBreakStart.setValue(currShift.getBillBreakStart() + "");
                myPayBreakStart.setValue(currShift.getPayBreakStart() + "");

                myEntryTextField.setValue(sp.myShift.getStartTime() + "");
                myExitTextField.setValue(sp.myShift.getEndTime() + "");
                DaysOfWeekPanel.setVisible(false);
            } else {
                cbConfirm.setSelected(false);
                cbTraining.setSelected(false);
                cbTrainer.setSelected(false);
                cbExtraCoverage.setSelected(false);

                cbDoNotBill.setSelected(false);
                cbDoNotPay.setSelected(false);

                if (sp.getClient().isDefaultToNonBillable()) {
                    cbDoNotBill.setSelected(true);
                }

                pnEmpBreakTime.setVisible(false);
                cbHasPaidBreak.setSelected(false);

                pnLocationBreakBill.setVisible(false);
                cbHasBreakBilled.setSelected(false);

                if (sp.getClient().getRate() == null || sp.getClient().getRate() == 0) {
                    lbRateCode.setDefault();
                } else {
                    lbRateCode.setRateCode(sp.getClient().getRate().toString());
                }

                efEmpBreakLen.setText("0000");
                efLocBreakLen.setText("0000");

                DeleteFutureWeekRadio.setVisible(false);
                DeleteCurrentWeekRadio.setVisible(false);
                cbTraining.setSelected(false);
                ApplyToFutureWeekRadio.setText("Create New Permanent Shift");
                ApplyToCurrentWeekRadio.setText("Create New Temporary Shift For This Week");
                DeleteCurrentWeekRadio.setText("Delete Current Shift");
                myEntryTextField.setValue("720");
                myExitTextField.setValue("720");
                DaysOfWeekPanel.setVisible(true);
                for (int i = 0; i < myWeekCheckBoxes.length; i++) {
                    myWeekCheckBoxes[i].setSelected(false);
                }
                myWeekCheckBoxes[sp.getOffset()].setSelected(true);

                pnLocationBreakBill.setVisible(false);
                pnEmpBreakTime.setVisible(false);
            }

            if (!Main_Window.parentOfApplication.checkSecurity(security_detail.MODULES.SCHEDULING_EDIT, security_detail.ACCESS.MODIFY)) {
                DeleteCurrentWeekRadio.setSelected(true);
                ApplyToFutureWeekRadio.setEnabled(false);
                ApplyToCurrentWeekRadio.setEnabled(false);
            } else {
                ApplyToFutureWeekRadio.setEnabled(true);
                ApplyToCurrentWeekRadio.setEnabled(true);
                ApplyToFutureWeekRadio.setSelected(true);
            }
            if (ApplyToCurrentWeekRadio.isVisible()) {
                ApplyToCurrentWeekRadio.setSelected(true);
            }
            if (!svParent.shouldMarkUnconShifts()) {
                cbConfirm.setVisible(false);
            }
            if (cbTraining.isSelected() || cbTrainer.isSelected()) {
                showTrainingPanel();
            }
            //Take away apply to future if we are in the past to reduce bugs
            if (sp.isPast()) {
                ApplyToFutureWeekRadio.setVisible(false);
            }
            pnGeneral.requestFocus();
            myTabbedPane.setSelectedIndex(0);
            myEntryTextField.requestFocus();

            disableOkCancelButtons(false);
        }
    }

    /**
     * Enable or disable all action buttons...
     */
    private void disableOkCancelButtons(boolean disable) {
        OkButton.setEnabled(!disable);
        DeleteButon.setEnabled(!disable);
        cbConfirm.setEnabled(!disable);
        cbTraining.setEnabled(!disable);
        cbExtraCoverage.setEnabled(!disable);
        myEntryTextField.setEnabled(!disable);
        myExitTextField.setEnabled(!disable);
        ApplyToCurrentWeekRadio.setEnabled(!disable);
        ApplyToFutureWeekRadio.setEnabled(!disable);
        DeleteCurrentWeekRadio.setEnabled(!disable);
        DeleteFutureWeekRadio.setEnabled(!disable);
    }

    private void changeFutureShifts() {
        if (JOptionPane.showConfirmDialog(Main_Window.parentOfApplication, "Are you sure?", "Confirm Make Perm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        if (editingShift == null) {
            createShiftsForCheckBoxesSelected(true);
        } else {
            editShift(true);
        }
        setVisible(false);
    }

    /**
     * Creates either temp or master shifts for selected checkboxes...
     */
    private void createShiftsForCheckBoxesSelected(boolean createMasterShifts) {
        DShift ds;
        RunQueriesEx myQuery = new RunQueriesEx();
        ShiftTypeClass myTempClass = getShiftType(createMasterShifts);
        long blankGroupId = System.currentTimeMillis();
        for (int i = 0; i < myWeekCheckBoxes.length; i++) {
            if (myWeekCheckBoxes[i].isSelected()) {
                ds = this.getShiftInformation(myTempClass, i);
                QueryGenerateShift myCreateShift = new QueryGenerateShift(ds);
                myCreateShift.scheduleId = editingSShift.myRow.myWeek.mySched.getInsertScheduleId(blankGroupId);
                myCreateShift.createShift(createMasterShifts);
                myQuery.add(myCreateShift.getMyQuery());
            }
        }
        try {
            svParent.getConnection().executeQueryEx(myQuery);
        } catch (Exception e) {
        }
    }

    private void editShift(boolean isMasterShift) {
        RunQueriesEx myQuery = new RunQueriesEx();
        ShiftTypeClass myTempClass = getShiftType(isMasterShift);
        QueryGenerateShift myCreateShift = new QueryGenerateShift(editingShift);
        myCreateShift.startT = (String) myEntryTextField.getValue();
        try {
            myCreateShift.weeklyRotation = Integer.parseInt(weeklyRotationCombo.getSelectedItem().toString());
        } catch (Exception e) {
            myCreateShift.weeklyRotation = 1;
        }
        myCreateShift.myType = myTempClass;
        myCreateShift.endT = (String) myExitTextField.getValue();
        myCreateShift.myBillOptions = getBillBreakInfo();
        myCreateShift.myPayOptions = getPayBreakInfo();
        myCreateShift.rate_code = getRateCode();
        myCreateShift.trainerId = trainerId + "";
        myCreateShift.editShift(isMasterShift);
        myQuery.add(myCreateShift.getMyQuery());
        try {
            svParent.getConnection().executeQueryEx(myQuery);
        } catch (Exception e) {
        }
    }

    private void changeShiftForSingleWeek() {
        if (editingShift == null) {
            createShiftsForCheckBoxesSelected(false);
        } else {
            editShift(false);
        }
        setVisible(false);
    }

    /**
     * Generates Our Shift Type based on what options are clicked in the GUI if
     * schedule is Master then automatically marks it as confirmed...
     */
    private ShiftTypeClass getShiftType(boolean isMaster) {
        ShiftTypeClass myTempClass = new ShiftTypeClass();
        myTempClass.updateVal(ShiftTypeClass.SHIFT_UNCONFIRMED);
        if (cbConfirm != null) {
            if ((cbConfirm.isSelected() && cbConfirm.isVisible()) || isMaster) {
                myTempClass.updateVal(ShiftTypeClass.SHIFT_CONFIRMED);
            }
        }
        if (cbExtraCoverage != null) {
            if (cbExtraCoverage.isSelected()) {
                myTempClass.updateVal(ShiftTypeClass.SHIFT_EXTRA_COVERAGE_SHIFT);
            }
        }
        if (cbTraining != null) {
            if (cbTraining.isSelected() || cbTrainer.isSelected()) {
                myTempClass.updateVal(ShiftTypeClass.SHIFT_TRAINING_SHIFT);
                if (trainerId != 0 && trainerName != null) {
                    myTempClass.setTrainerInformation(trainerId, trainerName);
                }
            } else {
                myTempClass.updateVal(ShiftTypeClass.SHIFT_NON_TRAINING);
            }
        }

        if (cbDoNotBill.isSelected()) {
            myTempClass.updateVal(ShiftTypeClass.SHIFT_NON_BILLABLE);
        } else if (cbBillOT.isSelected()) {
            myTempClass.updateVal(ShiftTypeClass.SHIFT_BILLABLE_OT);
        } else {
            myTempClass.updateVal(ShiftTypeClass.SHIFT_BILLABLE);
        }
        if (cbTrainer.isSelected()) {
            myTempClass.updateVal(ShiftTypeClass.SHIFT_TRAINER_SHIFT);
        }
        if (cbDoNotPay.isSelected()) {
            myTempClass.updateVal(ShiftTypeClass.SHIFT_NON_PAYABLE);
        } else if (cbPayOvertime.isSelected()) {
            myTempClass.updateVal(ShiftTypeClass.SHIFT_PAYABLE_OT);
        } else {
            myTempClass.updateVal(ShiftTypeClass.SHIFT_PAYABLE);
        }
        try {
            myTempClass.setWeeklyRotation(Integer.parseInt(weeklyRotationCombo.getSelectedItem().toString()));
        } catch (Exception e) {
            myTempClass.setWeeklyRotation(1);
        }
        return myTempClass;
    }

    private ShiftOptionsClass getPayBreakInfo() {
        ShiftOptionsClass scb = new ShiftOptionsClass();
        try {
            scb.setBreakLength(Integer.valueOf(efEmpBreakLen.getText()));
        } catch (Exception e) {
            scb.setBreakLength(0);
        }

        try {
            scb.setBreakStart(Integer.parseInt((String) myPayBreakStart.getValue()));
        } catch (Exception e) {
            scb.setBreakLength(0);
        }

        return scb;
    }

    private ShiftOptionsClass getBillBreakInfo() {
        ShiftOptionsClass sca = new ShiftOptionsClass();

        try {
            sca.setBreakLength(Integer.valueOf(efLocBreakLen.getText()));
        } catch (Exception e) {
            sca.setBreakLength(0);
        }

        try {
            sca.setBreakStart(Integer.parseInt((String) myBillBreakStart.getValue()));
        } catch (Exception e) {
            sca.setBreakLength(0);
        }

        return sca;
    }

    private int getRateCode() {
        int rc = 0;

        try {
            rc = Integer.parseInt(lbRateCode.getSelectedRateCodeId());
        } catch (Exception e) {
            rc = 0;
        }

        return rc;
    }

    /**
     * Generates our DShift information used by both save master and save
     * temp... ShiftTypeClass is the shift type info, the int i is used to tell
     * what day of week it is...
     */
    private DShift getShiftInformation(ShiftTypeClass myTempClass, int i) {

        int mydow = (dow[i] - svParent.getBegOfWeek()) % 7;
        if (mydow < 0) {
            mydow = 7 + mydow;
        }

        int dayOfWeek = dow[i] - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }

        String date = svParent.getDateByWeekDay(
                editingSShift.myRow.myWeek.week_no,
                mydow);

        DShift myRetShift = new DShift("0", "0", editingSShift.getClient(),
                editingSShift.getEmployee(),
                Integer.parseInt((String) myEntryTextField.getValue()),
                Integer.parseInt((String) myExitTextField.getValue()),
                dayOfWeek,
                svParent,
                editingSShift.myRow.myWeek.mySched.getInsertScheduleId(),
                false,
                myTempClass.toString(),
                date,
                "2100-10-10",
                "1900-10-10",
                getPayBreakInfo().toString(),
                getBillBreakInfo().toString(),
                getRateCode(),
                this.trainerId + "",
                StaticDateTimeFunctions.convertCalendarToDatabaseFormat(java.util.Calendar.getInstance()), "",
                myTempClass.getWeeklyRotation());
        myRetShift.updateTypeVal(myTempClass);
        return myRetShift;
    }

    private void deleteShifts(boolean deleteFutureShifts) {
        System.out.println("-- NewDShiftEdit Delete Confirmation --");
        if (JOptionPane.showConfirmDialog(Main_Window.parentOfApplication, "Are you sure?", "Confirm Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            System.out.println("-- NewDShiftEdit --");
            QueryGenerateShift myDeleteShift = new QueryGenerateShift(editingShift);
            myDeleteShift.deleteShift(deleteFutureShifts);
            try {
                svParent.getConnection().executeQueryEx(myDeleteShift.getMyQuery());
            } catch (Exception e) {
            }
            setVisible(false);
        }
    }

    /**
     * Builds Employee List only done when you show the trainer card...why? To
     * speed up our load time on this damn slow component.....
     */
    private void showTrainingPanel() {
        CardLayout myLayout = (CardLayout) DateTrainCardPanel.getLayout();
        myLayout.show(DateTrainCardPanel, "trainercard");
        if (trainerId > 0) {
            if (cbTrainer.isSelected()) {
            } else {
            }
        } else {
            if (cbTrainer.isSelected()) {
                TrainingLabel.setText("A Person To Train Has Not Been Selected");
            } else {
                TrainingLabel.setText("A Trainer Has Not Been Selected");
            }
        }
    }

    /**
     * Sets our training info used from our dialog box...
     */
    public void setTrainerInfo(int TrainId, String TrainerName) {
        trainerId = TrainId;
        trainerName = TrainerName;
        if (cbTrainer.isSelected()) {
            TrainingLabel.setText(editingShift.getEmployee().getName() + " is training " + TrainerName);
        } else {
            TrainingLabel.setText(TrainerName + " is training " + editingShift.getEmployee().getName());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        RadioButtonGroup = new javax.swing.ButtonGroup();
        TrainingShiftButtonGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        myTabbedPane = new javax.swing.JTabbedPane();
        pnGeneral = new javax.swing.JPanel();
        shiftTimesPanel = new javax.swing.JPanel();
        rowOnePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        EntryPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        ExitPanel = new javax.swing.JPanel();
        DaysOfWeekPanel = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        RotationPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        weeklyRotationCombo = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        leftColumn = new javax.swing.JPanel();
        cbConfirm = new javax.swing.JCheckBox();
        cbTrainer = new javax.swing.JCheckBox();
        rightColumn = new javax.swing.JPanel();
        cbExtraCoverage = new javax.swing.JCheckBox();
        cbTraining = new javax.swing.JCheckBox();
        pnBill = new javax.swing.JPanel();
        pnPayBill = new javax.swing.JPanel();
        pnRateCode = new javax.swing.JPanel();
        pnSubBill = new javax.swing.JPanel();
        PayOptions = new javax.swing.JPanel();
        cbDoNotPay = new javax.swing.JCheckBox();
        cbPayOvertime = new javax.swing.JCheckBox();
        cbHasPaidBreak = new javax.swing.JCheckBox();
        pnEmpBreakTime = new javax.swing.JPanel();
        pnEmpBillLen = new javax.swing.JPanel();
        lblEmpLen = new javax.swing.JLabel();
        lblEmpLen.putClientProperty(AjaxSwingProperties.COMPONENT_CSS_CLASS, "dShiftLengthLabel");
        efEmpBreakLen = new javax.swing.JTextField();
        lblEmpMin = new javax.swing.JLabel();
        lblEmpMin.putClientProperty(AjaxSwingProperties.COMPONENT_CSS_CLASS, "dShiftMinLabel");
        pnEmpBillStart = new javax.swing.JPanel();
        lblEmpStart = new javax.swing.JLabel();
        pnEmpStart = new javax.swing.JPanel();
        BillOptions = new javax.swing.JPanel();
        cbDoNotBill = new javax.swing.JCheckBox();
        cbBillOT = new javax.swing.JCheckBox();
        cbHasBreakBilled = new javax.swing.JCheckBox();
        pnLocationBreakBill = new javax.swing.JPanel();
        pnLocationBreakLen = new javax.swing.JPanel();
        lblLocLen = new javax.swing.JLabel();
        lblLocLen.putClientProperty(AjaxSwingProperties.COMPONENT_CSS_CLASS, "dShiftlblLocLen");
        efLocBreakLen = new javax.swing.JTextField();
        lblLocMin = new javax.swing.JLabel();
        lblLocMin.putClientProperty(AjaxSwingProperties.COMPONENT_CSS_CLASS, "dShiftlblLocMin");
        pnLocationBreakStart = new javax.swing.JPanel();
        lblLocStart = new javax.swing.JLabel();
        pnLocStart = new javax.swing.JPanel();
        pnRateCode1 = new javax.swing.JPanel();
        HistoryPanel = new javax.swing.JPanel();
        DateTrainCardPanel = new javax.swing.JPanel();
        SpecifyRangePanel = new javax.swing.JPanel();
        ApplyToCurrentWeekRadio = new javax.swing.JRadioButton();
        ApplyToFutureWeekRadio = new javax.swing.JRadioButton();
        DeleteCurrentWeekRadio = new javax.swing.JRadioButton();
        DeleteFutureWeekRadio = new javax.swing.JRadioButton();
        TrainerListPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        TrainingLabel = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        TrainingButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        OkButton = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        DeleteButon = new javax.swing.JButton();

        setTitle("Shift Edit");
        setModal(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(186, 200, 222));
        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3), javax.swing.BorderFactory.createEtchedBorder()));
        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jPanel2.setBackground(new java.awt.Color(222, 224, 238));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        myTabbedPane.setBackground(new java.awt.Color(222, 224, 238));
        myTabbedPane.setMinimumSize(new java.awt.Dimension(279, 0));
        myTabbedPane.setOpaque(true);
        myTabbedPane.setPreferredSize(new java.awt.Dimension(300, 0));

        pnGeneral.setBackground(new java.awt.Color(222, 224, 238));
        pnGeneral.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 5, 2, 5));
        pnGeneral.setMinimumSize(new java.awt.Dimension(12, 0));
        pnGeneral.setPreferredSize(new java.awt.Dimension(12, 80));
        pnGeneral.setLayout(new javax.swing.BoxLayout(pnGeneral, javax.swing.BoxLayout.Y_AXIS));

        shiftTimesPanel.setMaximumSize(new java.awt.Dimension(32767, 35));
        shiftTimesPanel.setMinimumSize(new java.awt.Dimension(10, 35));
        shiftTimesPanel.setOpaque(false);
        shiftTimesPanel.setPreferredSize(new java.awt.Dimension(10, 35));
        shiftTimesPanel.setLayout(new java.awt.GridLayout(1, 0));

        rowOnePanel.setOpaque(false);
        rowOnePanel.setLayout(new javax.swing.BoxLayout(rowOnePanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("Start Time");
        jLabel1.setMaximumSize(new java.awt.Dimension(80, 18));
        jLabel1.setMinimumSize(new java.awt.Dimension(80, 18));
        jLabel1.setPreferredSize(new java.awt.Dimension(80, 18));
        rowOnePanel.add(jLabel1);

        EntryPanel.setMaximumSize(new java.awt.Dimension(2000, 24));
        EntryPanel.setMinimumSize(new java.awt.Dimension(115, 24));
        EntryPanel.setOpaque(false);
        EntryPanel.setPreferredSize(new java.awt.Dimension(115, 24));
        EntryPanel.setLayout(new java.awt.GridLayout(1, 0, 2, 0));
        rowOnePanel.add(EntryPanel);

        jLabel2.setText("End Time");
        jLabel2.setMaximumSize(new java.awt.Dimension(80, 18));
        jLabel2.setMinimumSize(new java.awt.Dimension(80, 18));
        jLabel2.setPreferredSize(new java.awt.Dimension(80, 18));
        rowOnePanel.add(jLabel2);

        ExitPanel.setMaximumSize(new java.awt.Dimension(2000, 24));
        ExitPanel.setMinimumSize(new java.awt.Dimension(115, 24));
        ExitPanel.setOpaque(false);
        ExitPanel.setPreferredSize(new java.awt.Dimension(115, 24));
        ExitPanel.setLayout(new java.awt.GridLayout(1, 0, 2, 0));
        rowOnePanel.add(ExitPanel);

        shiftTimesPanel.add(rowOnePanel);

        pnGeneral.add(shiftTimesPanel);

        DaysOfWeekPanel.setMaximumSize(new java.awt.Dimension(32767, 45));
        DaysOfWeekPanel.setMinimumSize(new java.awt.Dimension(12, 0));
        DaysOfWeekPanel.setOpaque(false);
        DaysOfWeekPanel.setPreferredSize(new java.awt.Dimension(12, 45));
        DaysOfWeekPanel.setLayout(new java.awt.GridLayout(1, 7));
        pnGeneral.add(DaysOfWeekPanel);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Shift Options"));
        jPanel6.setMaximumSize(new java.awt.Dimension(32767, 700));
        jPanel6.setMinimumSize(new java.awt.Dimension(10, 700));
        jPanel6.setOpaque(false);
        jPanel6.setPreferredSize(new java.awt.Dimension(10, 70));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.Y_AXIS));

        RotationPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 2, 0));
        RotationPanel.setMaximumSize(new java.awt.Dimension(1305, 18));
        RotationPanel.setOpaque(false);
        RotationPanel.setLayout(new javax.swing.BoxLayout(RotationPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel3.setText("Rotation");
        jLabel3.setMaximumSize(new java.awt.Dimension(80, 18));
        jLabel3.setMinimumSize(new java.awt.Dimension(80, 18));
        jLabel3.setPreferredSize(new java.awt.Dimension(80, 18));
        RotationPanel.add(jLabel3);

        weeklyRotationCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        weeklyRotationCombo.setMaximumSize(new java.awt.Dimension(55, 22));
        weeklyRotationCombo.setMinimumSize(new java.awt.Dimension(55, 22));
        weeklyRotationCombo.setPreferredSize(new java.awt.Dimension(55, 22));
        RotationPanel.add(weeklyRotationCombo);

        jPanel6.add(RotationPanel);

        jPanel3.setOpaque(false);
        jPanel3.setLayout(new java.awt.GridLayout(1, 2));

        leftColumn.setMaximumSize(new java.awt.Dimension(32767, 23));
        leftColumn.setOpaque(false);
        leftColumn.setLayout(new javax.swing.BoxLayout(leftColumn, javax.swing.BoxLayout.Y_AXIS));

        cbConfirm.setText("Shift Confirmed With Employee");
        cbConfirm.setMaximumSize(new java.awt.Dimension(17100, 23));
        cbConfirm.setMinimumSize(new java.awt.Dimension(1703, 23));
        cbConfirm.setPreferredSize(new java.awt.Dimension(1703, 23));
        leftColumn.add(cbConfirm);

        cbTrainer.setText("Trainer");
        cbTrainer.setMaximumSize(new java.awt.Dimension(1703, 23));
        cbTrainer.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                trainerStateChanged(evt);
            }
        });
        leftColumn.add(cbTrainer);

        jPanel3.add(leftColumn);

        rightColumn.setMaximumSize(new java.awt.Dimension(32767, 23));
        rightColumn.setOpaque(false);
        rightColumn.setLayout(new javax.swing.BoxLayout(rightColumn, javax.swing.BoxLayout.Y_AXIS));

        cbExtraCoverage.setText("Extra Coverage Shift");
        rightColumn.add(cbExtraCoverage);

        cbTraining.setText("Training Shift");
        cbTraining.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TrainingStateChanged(evt);
            }
        });
        rightColumn.add(cbTraining);

        jPanel3.add(rightColumn);

        jPanel6.add(jPanel3);

        pnGeneral.add(jPanel6);

        myTabbedPane.addTab("General", pnGeneral);

        pnBill.setBackground(new java.awt.Color(222, 224, 238));
        pnBill.setEnabled(false);
        pnBill.setMinimumSize(new java.awt.Dimension(262, 0));
        pnBill.setLayout(new java.awt.BorderLayout());

        pnPayBill.setMinimumSize(new java.awt.Dimension(262, 0));
        pnPayBill.setOpaque(false);
        pnPayBill.setLayout(new javax.swing.BoxLayout(pnPayBill, javax.swing.BoxLayout.Y_AXIS));

        pnRateCode.setBorder(javax.swing.BorderFactory.createTitledBorder("Shift Rate"));
        pnRateCode.setMaximumSize(new java.awt.Dimension(2147483647, 75));
        pnRateCode.setMinimumSize(new java.awt.Dimension(0, 75));
        pnRateCode.setOpaque(false);
        pnRateCode.setPreferredSize(new java.awt.Dimension(0, 75));
        pnRateCode.setLayout(new java.awt.BorderLayout());
        pnPayBill.add(pnRateCode);

        pnSubBill.setOpaque(false);
        pnSubBill.setLayout(new javax.swing.BoxLayout(pnSubBill, javax.swing.BoxLayout.LINE_AXIS));

        PayOptions.setBorder(javax.swing.BorderFactory.createTitledBorder("Shift Pay Options"));
        PayOptions.setMaximumSize(new java.awt.Dimension(2147483647, 1220));
        PayOptions.setMinimumSize(new java.awt.Dimension(0, 0));
        PayOptions.setOpaque(false);
        PayOptions.setPreferredSize(new java.awt.Dimension(262, 0));
        PayOptions.setLayout(new javax.swing.BoxLayout(PayOptions, javax.swing.BoxLayout.Y_AXIS));

        cbDoNotPay.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        cbDoNotPay.setText("Do not pay employee.");
        cbDoNotPay.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ClickedDoNotPay(evt);
            }
        });
        PayOptions.add(cbDoNotPay);

        cbPayOvertime.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        cbPayOvertime.setText("Pay employee OT");
        cbPayOvertime.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                cbPayOvertimeClickedDoNotPay(evt);
            }
        });
        PayOptions.add(cbPayOvertime);

        cbHasPaidBreak.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        cbHasPaidBreak.setText("Has unpaid break");
        cbHasPaidBreak.setMaximumSize(new java.awt.Dimension(1009, 23));
        cbHasPaidBreak.setMinimumSize(new java.awt.Dimension(0, 23));
        cbHasPaidBreak.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ChangeUnpaidBreakState(evt);
            }
        });
        PayOptions.add(cbHasPaidBreak);

        pnEmpBreakTime.setMinimumSize(new java.awt.Dimension(131, 0));
        pnEmpBreakTime.setOpaque(false);
        pnEmpBreakTime.setLayout(new javax.swing.BoxLayout(pnEmpBreakTime, javax.swing.BoxLayout.Y_AXIS));

        pnEmpBillLen.setMinimumSize(new java.awt.Dimension(0, 20));
        pnEmpBillLen.setOpaque(false);
        pnEmpBillLen.setPreferredSize(new java.awt.Dimension(40, 20));
        pnEmpBillLen.setLayout(new javax.swing.BoxLayout(pnEmpBillLen, javax.swing.BoxLayout.LINE_AXIS));

        lblEmpLen.setText("Length: ");
        lblEmpLen.setMaximumSize(new java.awt.Dimension(55, 14));
        lblEmpLen.setMinimumSize(new java.awt.Dimension(55, 14));
        lblEmpLen.setPreferredSize(new java.awt.Dimension(55, 14));
        pnEmpBillLen.add(lblEmpLen);

        efEmpBreakLen.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        pnEmpBillLen.add(efEmpBreakLen);

        lblEmpMin.setText("  Min ");
        lblEmpMin.setMaximumSize(new java.awt.Dimension(55, 14));
        lblEmpMin.setMinimumSize(new java.awt.Dimension(55, 14));
        lblEmpMin.setPreferredSize(new java.awt.Dimension(55, 14));
        pnEmpBillLen.add(lblEmpMin);

        pnEmpBreakTime.add(pnEmpBillLen);

        pnEmpBillStart.setMaximumSize(new java.awt.Dimension(2147483647, 27));
        pnEmpBillStart.setMinimumSize(new java.awt.Dimension(0, 23));
        pnEmpBillStart.setOpaque(false);
        pnEmpBillStart.setLayout(new java.awt.BorderLayout());

        lblEmpStart.setText(" Start: ");
        pnEmpBillStart.add(lblEmpStart, java.awt.BorderLayout.WEST);

        pnEmpStart.setMaximumSize(new java.awt.Dimension(32767, 23));
        pnEmpStart.setMinimumSize(new java.awt.Dimension(100, 23));
        pnEmpStart.setOpaque(false);
        pnEmpStart.setPreferredSize(new java.awt.Dimension(100, 23));
        pnEmpStart.setLayout(new java.awt.BorderLayout());
        pnEmpBillStart.add(pnEmpStart, java.awt.BorderLayout.CENTER);

        pnEmpBreakTime.add(pnEmpBillStart);

        PayOptions.add(pnEmpBreakTime);

        pnSubBill.add(PayOptions);

        BillOptions.setBorder(javax.swing.BorderFactory.createTitledBorder("Shift Bill Options"));
        BillOptions.setMaximumSize(new java.awt.Dimension(2147483647, 1220));
        BillOptions.setMinimumSize(new java.awt.Dimension(0, 0));
        BillOptions.setOpaque(false);
        BillOptions.setPreferredSize(new java.awt.Dimension(278, 0));
        BillOptions.setLayout(new javax.swing.BoxLayout(BillOptions, javax.swing.BoxLayout.Y_AXIS));

        cbDoNotBill.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        cbDoNotBill.setText("Do not bill location.");
        cbDoNotBill.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ClickedDoNotBill(evt);
            }
        });
        BillOptions.add(cbDoNotBill);

        cbBillOT.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        cbBillOT.setText("Bill location OT");
        cbBillOT.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                cbBillOTChangeUnbilledBreakState(evt);
            }
        });
        BillOptions.add(cbBillOT);

        cbHasBreakBilled.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12)); // NOI18N
        cbHasBreakBilled.setText("Has unbilled break");
        cbHasBreakBilled.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ChangeUnbilledBreakState(evt);
            }
        });
        BillOptions.add(cbHasBreakBilled);

        pnLocationBreakBill.setMinimumSize(new java.awt.Dimension(131, 0));
        pnLocationBreakBill.setOpaque(false);
        pnLocationBreakBill.setLayout(new javax.swing.BoxLayout(pnLocationBreakBill, javax.swing.BoxLayout.Y_AXIS));

        pnLocationBreakLen.setMinimumSize(new java.awt.Dimension(0, 20));
        pnLocationBreakLen.setOpaque(false);
        pnLocationBreakLen.setLayout(new javax.swing.BoxLayout(pnLocationBreakLen, javax.swing.BoxLayout.LINE_AXIS));

        lblLocLen.setText("Length: ");
        lblLocLen.setMaximumSize(new java.awt.Dimension(55, 14));
        lblLocLen.setMinimumSize(new java.awt.Dimension(55, 14));
        lblLocLen.setPreferredSize(new java.awt.Dimension(55, 14));
        pnLocationBreakLen.add(lblLocLen);

        efLocBreakLen.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        pnLocationBreakLen.add(efLocBreakLen);

        lblLocMin.setText("  Min ");
        lblLocMin.setMaximumSize(new java.awt.Dimension(55, 14));
        lblLocMin.setMinimumSize(new java.awt.Dimension(55, 14));
        lblLocMin.setPreferredSize(new java.awt.Dimension(55, 14));
        pnLocationBreakLen.add(lblLocMin);

        pnLocationBreakBill.add(pnLocationBreakLen);

        pnLocationBreakStart.setMaximumSize(new java.awt.Dimension(2147483647, 27));
        pnLocationBreakStart.setMinimumSize(new java.awt.Dimension(0, 23));
        pnLocationBreakStart.setOpaque(false);
        pnLocationBreakStart.setLayout(new java.awt.BorderLayout());

        lblLocStart.setText(" Start: ");
        pnLocationBreakStart.add(lblLocStart, java.awt.BorderLayout.WEST);

        pnLocStart.setMaximumSize(new java.awt.Dimension(32767, 23));
        pnLocStart.setMinimumSize(new java.awt.Dimension(100, 23));
        pnLocStart.setOpaque(false);
        pnLocStart.setPreferredSize(new java.awt.Dimension(100, 23));
        pnLocStart.setLayout(new java.awt.BorderLayout());
        pnLocationBreakStart.add(pnLocStart, java.awt.BorderLayout.CENTER);

        pnLocationBreakBill.add(pnLocationBreakStart);

        BillOptions.add(pnLocationBreakBill);

        pnSubBill.add(BillOptions);

        pnPayBill.add(pnSubBill);

        pnBill.add(pnPayBill, java.awt.BorderLayout.CENTER);

        pnRateCode1.setOpaque(false);
        pnRateCode1.setLayout(new java.awt.BorderLayout());
        pnBill.add(pnRateCode1, java.awt.BorderLayout.NORTH);

        myTabbedPane.addTab("Bill", pnBill);

        HistoryPanel.setBackground(new java.awt.Color(222, 224, 238));
        HistoryPanel.setLayout(new java.awt.GridLayout(1, 0));
        myTabbedPane.addTab("Shift History", HistoryPanel);

        jPanel2.add(myTabbedPane);

        DateTrainCardPanel.setMaximumSize(new java.awt.Dimension(32767, 660));
        DateTrainCardPanel.setMinimumSize(new java.awt.Dimension(269, 60));
        DateTrainCardPanel.setOpaque(false);
        DateTrainCardPanel.setPreferredSize(new java.awt.Dimension(269, 80));
        DateTrainCardPanel.setLayout(new java.awt.CardLayout());

        SpecifyRangePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 0, 0));
        SpecifyRangePanel.setMaximumSize(new java.awt.Dimension(257, 920));
        SpecifyRangePanel.setOpaque(false);
        SpecifyRangePanel.setLayout(new javax.swing.BoxLayout(SpecifyRangePanel, javax.swing.BoxLayout.Y_AXIS));

        ApplyToCurrentWeekRadio.setSelected(true);
        ApplyToCurrentWeekRadio.setText("Apply Shift Changes To Current Week");
        SpecifyRangePanel.add(ApplyToCurrentWeekRadio);

        ApplyToFutureWeekRadio.setText("Apply Shift Changes To Current And Future Shifts");
        SpecifyRangePanel.add(ApplyToFutureWeekRadio);

        DeleteCurrentWeekRadio.setText("Delete Current Shift");
        SpecifyRangePanel.add(DeleteCurrentWeekRadio);

        DeleteFutureWeekRadio.setText("Delete Current And Future Shifts");
        SpecifyRangePanel.add(DeleteFutureWeekRadio);

        DateTrainCardPanel.add(SpecifyRangePanel, "dateranges");

        TrainerListPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Training Information"));
        TrainerListPanel.setMaximumSize(new java.awt.Dimension(3400, 920));
        TrainerListPanel.setOpaque(false);
        TrainerListPanel.setLayout(new javax.swing.BoxLayout(TrainerListPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel4.setMaximumSize(new java.awt.Dimension(32767, 10));
        jPanel4.setOpaque(false);
        TrainerListPanel.add(jPanel4);

        jPanel7.setMaximumSize(new java.awt.Dimension(32767, 18));
        jPanel7.setOpaque(false);
        jPanel7.setLayout(new java.awt.GridLayout(1, 0));

        TrainingLabel.setText("jLabel3");
        jPanel7.add(TrainingLabel);

        TrainerListPanel.add(jPanel7);

        jPanel9.setOpaque(false);

        TrainingButton.setText("Click to select a trainer");
        TrainingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TrainingButtonActionPerformed(evt);
            }
        });
        jPanel9.add(TrainingButton);

        TrainerListPanel.add(jPanel9);

        DateTrainCardPanel.add(TrainerListPanel, "trainercard");

        jPanel2.add(DateTrainCardPanel);

        jPanel5.setBackground(new java.awt.Color(222, 224, 238));
        jPanel5.setOpaque(false);
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

        OkButton.setText("OK");
        OkButton.setMaximumSize(new java.awt.Dimension(75, 25));
        OkButton.setMinimumSize(new java.awt.Dimension(75, 25));
        OkButton.setPreferredSize(new java.awt.Dimension(75, 25));
        OkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OkButtonActionPerformed(evt);
            }
        });
        jPanel5.add(OkButton);

        jPanel8.setMaximumSize(new java.awt.Dimension(32767, 10));
        jPanel8.setOpaque(false);
        jPanel5.add(jPanel8);

        DeleteButon.setText("Cancel");
        DeleteButon.setMaximumSize(new java.awt.Dimension(75, 25));
        DeleteButon.setMinimumSize(new java.awt.Dimension(75, 25));
        DeleteButon.setPreferredSize(new java.awt.Dimension(75, 25));
        DeleteButon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteButonActionPerformed(evt);
            }
        });
        jPanel5.add(DeleteButon);

        jPanel2.add(jPanel5);

        jPanel1.add(jPanel2);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(490, 443));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void TrainingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TrainingButtonActionPerformed
        TrainingListPanel myListPanel = new TrainingListPanel(null, true, editingSShift, cbTrainer.isSelected(), svParent, this);
        myListPanel.setVisible(true);
    }//GEN-LAST:event_TrainingButtonActionPerformed

    private void TrainingStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_TrainingStateChanged
        CardLayout myLayout = (CardLayout) DateTrainCardPanel.getLayout();
        if (cbTraining.isVisible()) {
            if (cbTraining.isSelected()) {
                TrainingLabel.setText("Training Information");
                TrainingButton.setText("Click to select a trainer");
                cbTrainer.setVisible(false);
                cbTrainer.setSelected(false);
                cbConfirm.setVisible(false);
                showTrainingPanel();
                cbDoNotBill.setSelected(true);
            } else {
                cbTrainer.setVisible(true);
                if (!cbTrainer.isSelected()) {
                    cbDoNotBill.setSelected(false);
                }
                cbConfirm.setVisible(true);
                myLayout.show(DateTrainCardPanel, "dateranges");
            }
            repaint();
        }
    }//GEN-LAST:event_TrainingStateChanged

    private void trainerStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_trainerStateChanged
        CardLayout myLayout = (CardLayout) DateTrainCardPanel.getLayout();
        if (cbTrainer.isVisible()) {
            if (cbTrainer.isSelected()) {
                TrainingLabel.setText("Trainer Information");
                TrainingButton.setText("Click to select a person to train");
                cbTraining.setVisible(false);
                cbTraining.setSelected(false);
                cbConfirm.setVisible(false);
                showTrainingPanel();
                cbDoNotBill.setSelected(true);
            } else {
                cbTraining.setVisible(true);
                if (!cbTraining.isSelected()) {
                    cbDoNotBill.setSelected(false);
                }
                cbConfirm.setVisible(true);
                myLayout.show(DateTrainCardPanel, "dateranges");
            }
            repaint();
        }
    }    private void ClickedDoNotBill(javax.swing.event.ChangeEvent evt) {//GEN-LAST:event_trainerStateChanged
        if (cbDoNotBill.isSelected()) {//GEN-FIRST:event_ClickedDoNotBill
                cbHasBreakBilled.setVisible(false);
                cbHasBreakBilled.setSelected(false);
            } else {
                cbHasBreakBilled.setVisible(true);
            }
    }//GEN-LAST:event_ClickedDoNotBill
    private void ClickedDoNotPay(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ClickedDoNotPay
        if (cbDoNotPay.isSelected()) {
            cbHasPaidBreak.setVisible(false);
            cbHasPaidBreak.setSelected(false);
        } else {
            cbHasPaidBreak.setVisible(true);
        }
    }//GEN-LAST:event_ClickedDoNotPay
    private void ChangeUnbilledBreakState(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ChangeUnbilledBreakState
        if (cbHasBreakBilled.isSelected()) {
            pnLocationBreakBill.setVisible(true);
        } else {
            pnLocationBreakBill.setVisible(false);
        }
        repaint();
    }//GEN-LAST:event_ChangeUnbilledBreakState
    private void ChangeUnpaidBreakState(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ChangeUnpaidBreakState
        if (cbHasPaidBreak.isSelected()) {
            pnEmpBreakTime.setVisible(true);
        } else {
            pnEmpBreakTime.setVisible(false);
        }
        repaint();
    }//GEN-LAST:event_ChangeUnpaidBreakState
    private void cbHasBilledBreakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbHasBilledBreakActionPerformed
    }//GEN-LAST:event_cbHasBilledBreakActionPerformed
    private void OkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OkButtonActionPerformed
        //Action Events are still coming in even though the button is set as 
        //disabled, handling the disable manually.
        if (AjaxSwingManager.isAjaxSwingRunning()) {
            AjaxSwingManager.beginOperation();
        }

        if (this.OkButton.isEnabled()) {
            if (!AjaxSwingManager.isAjaxSwingRunning()) {
                disableOkCancelButtons(true);
            }
            if (!cbHasPaidBreak.isSelected()) {
                efEmpBreakLen.setText("");
            }

            if (!cbHasBreakBilled.isSelected()) {
                efLocBreakLen.setText("");
            }

            if (ApplyToCurrentWeekRadio.isSelected()) {
                changeShiftForSingleWeek();
            } else if (ApplyToFutureWeekRadio.isSelected()) {
                changeFutureShifts();
            } else if (DeleteCurrentWeekRadio.isSelected()) {
                deleteShifts(false);
            } else if (DeleteFutureWeekRadio.isSelected()) {
                deleteShifts(true);
            }
            this.setVisible(false);
            editingSShift.selectThisShift(false);
            editingSShift.setBG();
        }
        if (AjaxSwingManager.isAjaxSwingRunning()) {
            AjaxSwingManager.endOperation();
        }
    }//GEN-LAST:event_OkButtonActionPerformed
    private void DeleteButonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteButonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_DeleteButonActionPerformed

    private void cbPayOvertimeClickedDoNotPay(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_cbPayOvertimeClickedDoNotPay
        // TODO add your handling code here:
    }//GEN-LAST:event_cbPayOvertimeClickedDoNotPay

    private void cbBillOTChangeUnbilledBreakState(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_cbBillOTChangeUnbilledBreakState
        // TODO add your handling code here:
    }//GEN-LAST:event_cbBillOTChangeUnbilledBreakState

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton ApplyToCurrentWeekRadio;
    private javax.swing.JRadioButton ApplyToFutureWeekRadio;
    private javax.swing.JPanel BillOptions;
    private javax.swing.JPanel DateTrainCardPanel;
    private javax.swing.JPanel DaysOfWeekPanel;
    private javax.swing.JButton DeleteButon;
    private javax.swing.JRadioButton DeleteCurrentWeekRadio;
    private javax.swing.JRadioButton DeleteFutureWeekRadio;
    private javax.swing.JPanel EntryPanel;
    private javax.swing.JPanel ExitPanel;
    private javax.swing.JPanel HistoryPanel;
    private javax.swing.JButton OkButton;
    private javax.swing.JPanel PayOptions;
    private javax.swing.ButtonGroup RadioButtonGroup;
    private javax.swing.JPanel RotationPanel;
    private javax.swing.JPanel SpecifyRangePanel;
    private javax.swing.JPanel TrainerListPanel;
    private javax.swing.JButton TrainingButton;
    private javax.swing.JLabel TrainingLabel;
    private javax.swing.ButtonGroup TrainingShiftButtonGroup;
    private javax.swing.JCheckBox cbBillOT;
    private javax.swing.JCheckBox cbConfirm;
    private javax.swing.JCheckBox cbDoNotBill;
    private javax.swing.JCheckBox cbDoNotPay;
    private javax.swing.JCheckBox cbExtraCoverage;
    private javax.swing.JCheckBox cbHasBreakBilled;
    private javax.swing.JCheckBox cbHasPaidBreak;
    private javax.swing.JCheckBox cbPayOvertime;
    private javax.swing.JCheckBox cbTrainer;
    private javax.swing.JCheckBox cbTraining;
    private javax.swing.JTextField efEmpBreakLen;
    private javax.swing.JTextField efLocBreakLen;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel lblEmpLen;
    private javax.swing.JLabel lblEmpMin;
    private javax.swing.JLabel lblEmpStart;
    private javax.swing.JLabel lblLocLen;
    private javax.swing.JLabel lblLocMin;
    private javax.swing.JLabel lblLocStart;
    private javax.swing.JPanel leftColumn;
    private javax.swing.JTabbedPane myTabbedPane;
    private javax.swing.JPanel pnBill;
    private javax.swing.JPanel pnEmpBillLen;
    private javax.swing.JPanel pnEmpBillStart;
    private javax.swing.JPanel pnEmpBreakTime;
    private javax.swing.JPanel pnEmpStart;
    private javax.swing.JPanel pnGeneral;
    private javax.swing.JPanel pnLocStart;
    private javax.swing.JPanel pnLocationBreakBill;
    private javax.swing.JPanel pnLocationBreakLen;
    private javax.swing.JPanel pnLocationBreakStart;
    private javax.swing.JPanel pnPayBill;
    private javax.swing.JPanel pnRateCode;
    private javax.swing.JPanel pnRateCode1;
    private javax.swing.JPanel pnSubBill;
    private javax.swing.JPanel rightColumn;
    private javax.swing.JPanel rowOnePanel;
    private javax.swing.JPanel shiftTimesPanel;
    private javax.swing.JComboBox weeklyRotationCombo;
    // End of variables declaration//GEN-END:variables
}
