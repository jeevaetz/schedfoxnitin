/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TrainingInformationPanel.java
 *
 * Created on Oct 18, 2010, 10:19:26 AM
 */
package rmischedule.training;

import java.awt.Cursor;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import rmischedule.data_connection.Connection;
import rmischedule.employee.dashboard.Employee_Dashboard;
import schedfoxlib.model.Company;
import rmischedule.main.Main_Window;
import schedfoxlib.model.util.FileLoader;
import rmischedule.schedule.PostInstructionPanel;
import rmischedule.schedule.ScheduleDashboardPanel;
import rmischedule.schedule.Schedule_View_Panel;
import rmischeduleserver.control.ScheduleController;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.EmployeeTest;
import schedfoxlib.model.util.ImageLoader;
import rmischeduleserver.mysqlconnectivity.queries.employee.get_employee_test_scores_query;
import schedfoxlib.model.Employee;
import schedfoxlib.model.ScheduleData;
import schedfoxlib.model.ShiftOptionsClass;
import schedfoxlib.model.ShiftTypeClass;

/**
 *
 * @author user
 */
public class TrainingInformationPanel extends javax.swing.JPanel {

    private JFileChooser fchooser;
    private ScheduleDashboardPanel myParent;
    private Schedule_View_Panel svp;

    /**
     * Creates new form TrainingInformationPanel
     */
    public TrainingInformationPanel(ScheduleDashboardPanel panel) {
        initComponents();

        this.myParent = panel;

        this.fchooser = new JFileChooser();
        this.fchooser.setDialogTitle("Select a file to save as post instructions.");
        this.fchooser.setAcceptAllFileFilterUsed(true);
        this.fchooser.setMultiSelectionEnabled(false);

        confirmationPanel.setVisible(false);
        warningLabel.setIcon(Main_Window.Alert_Animated_Icon);

    }

    public void showTrainingInformation(Schedule_View_Panel svp) {

        this.displayFile(svp);
        this.displayTrainingCatalogs(svp);

        this.svp = svp;
        new CheckConfirmationThread().start();
    }

    public String getPageText() {
        return "Training Information";
    }

    private ArrayList<EmployeeTest> getTestsForClient(int client_id, Schedule_View_Panel svp) {
        ArrayList<EmployeeTest> retVal = new ArrayList<EmployeeTest>();
//        get_training_for_client_query getTrainingQuery = new get_training_for_client_query();
        get_employee_test_scores_query getTrainingQuery = new get_employee_test_scores_query();
        Integer userId = Integer.parseInt(Main_Window.parentOfApplication.myUser.getUserId());
        getTrainingQuery.setPreparedStatement(new Object[]{userId, client_id, client_id});
        Connection myConn = svp.getConnection();
        Record_Set rst = myConn.executeQuery(getTrainingQuery);
        for (int r = 0; r < rst.length(); r++) {
            EmployeeTest empTest = new EmployeeTest(rst);
            retVal.add(empTest);
            rst.moveNext();
        }
        return retVal;
    }

    public void displayTrainingCatalogs(Schedule_View_Panel svp) {
        try {
            int clientId = this.myParent.getCurrentClientId();
            ArrayList<EmployeeTest> employeeTests = this.getTestsForClient(clientId, svp);
            testingContainerPanel.removeAll();
            for (int c = 0; c < employeeTests.size(); c++) {
                testingContainerPanel.add(new CatalogDisplayPanel(employeeTests.get(c), clientId));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "ERROR : " + ex);
            Logger.getLogger(Employee_Dashboard.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    private void displayProblem() {
        JOptionPane.showMessageDialog(null, "Client has not been set up for this "
                + "functionality yet, please finalize client!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void addFile(Schedule_View_Panel svp) {
        if (fchooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                //  set cursor
                Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
                setCursor(hourglassCursor);

                //  check for collisions
                if (!this.saveHasNamingCollision(fchooser.getSelectedFile(), svp)) {
                    this.saveFileToServer(fchooser.getSelectedFile(), svp);
                } else {
                    // ask user if they wish to overwrite
                    int response = JOptionPane.showConfirmDialog(this, "A file with "
                            + "the same name has been detected on the server.  Do you "
                            + "wish to overwrite?", "Overwrite existing file?", JOptionPane.YES_NO_OPTION);
                    if (response == 0) {
                        //  overwrite file
                        this.saveFileToServer(fchooser.getSelectedFile(), svp);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "There was an error saving your file.",
                        "FileServer Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                //  reset cursor
                Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                setCursor(normalCursor);
            }   // finally
        }   //  if
    }   //  method

    private void displayFile(Schedule_View_Panel svp) {
        Company comp = Main_Window.parentOfApplication.getCompanyById(svp.getCompany());

        //  load files
        try {
            ArrayList<String> filesFromServer = FileLoader.getFileNames(comp.getDB(),
                    this.myParent.getCurrentClientId() + "", "location_additional_files");
            postInstrPanel.removeAll();
            for (int f = 0; f < filesFromServer.size(); f++) {
                postInstrPanel.add(new PostInstructionPanel(f + 1, filesFromServer.get(f)));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "No post instructions!",
                    "No post instructions!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    private void saveFileToServer(File fileToSave, Schedule_View_Panel svp) {
        Company comp = Main_Window.parentOfApplication.getCompanyById(svp.getCompany());

        try {
            ImageLoader.saveFile("post_instructions_" + this.myParent.getCurrentClientId(), comp.getDB(),
                    "general", fileToSave);
            JOptionPane.showMessageDialog(this, "The file was saved successfully to the server.",
                    "Save file to server", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error:  file was not saved to server.",
                    "Save file to server", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean saveHasNamingCollision(File argFile, Schedule_View_Panel svp) {
        boolean hasCollision = false;

        Company comp = Main_Window.parentOfApplication.getCompanyById(svp.getCompany());

        //  get list of file names from server
        ArrayList<String> fileLocations = new ArrayList<String>();
        fileLocations = FileLoader.getFileNames(comp.getDB(),
                "post_instructions_" + this.myParent.getCurrentClientId(), "general");
        String argFileName = argFile.getName();

        // check file names from server against local filename
        for (int idx = 0; idx < fileLocations.size(); idx++) {
            String fileNameFromServer = fileLocations.get(idx);
            if (argFileName.matches(fileNameFromServer)) {
                hasCollision = true;
            }
        }

        return hasCollision;
    }

    private class CheckConfirmationThread extends Thread {

        @Override
        public void run() {
            String userId = Main_Window.parentOfApplication.myUser.getUserId();
            ScheduleController scheduleController = new ScheduleController(svp.getCompany());
            Employee tempEmployee = new Employee();

            tempEmployee.setEmployeeId(Integer.parseInt(userId));

            Calendar startCal = svp.getBegWeek();
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(startCal.getTime());
            endCal.add(Calendar.DAY_OF_MONTH, 6);

            try {
                ArrayList<ScheduleData> schedules = scheduleController.getSchedule(svp.getBegWeek().getTime(), endCal.getTime(), tempEmployee);
                boolean isConfirmedForWeek = true;
                for (int s = 0; s < schedules.size(); s++) {
                    if (!schedules.get(s).isShiftType(ShiftTypeClass.SHIFT_RECONCILED)) {
                        isConfirmedForWeek = false;
                    }
                }
                final boolean showPanel = !isConfirmedForWeek;
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        confirmationPanel.setVisible(showPanel);
                    }
                });
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        confirmationPanel = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        warningLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        confirmBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        postInstrPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        testingPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        testingContainerPanel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(410, 32767));
        setMinimumSize(new java.awt.Dimension(410, 120));
        setPreferredSize(new java.awt.Dimension(410, 485));
        setLayout(new java.awt.GridLayout(1, 0));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Instructions"));
        jPanel1.setMinimumSize(new java.awt.Dimension(185, 470));
        jPanel1.setPreferredSize(new java.awt.Dimension(172, 470));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        confirmationPanel.setMaximumSize(new java.awt.Dimension(32767, 70));
        confirmationPanel.setMinimumSize(new java.awt.Dimension(182, 70));
        confirmationPanel.setPreferredSize(new java.awt.Dimension(182, 70));
        confirmationPanel.setLayout(new java.awt.GridLayout(2, 0));

        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.LINE_AXIS));

        warningLabel.setMaximumSize(new java.awt.Dimension(32, 32));
        warningLabel.setMinimumSize(new java.awt.Dimension(32, 32));
        warningLabel.setPreferredSize(new java.awt.Dimension(32, 32));
        jPanel8.add(warningLabel);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 0, 0));
        jLabel6.setText("SCHEDULE CONFIRMATION");
        jLabel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 0, 0));
        jLabel6.setMaximumSize(new java.awt.Dimension(5000, 30));
        jPanel8.add(jLabel6);

        confirmationPanel.add(jPanel8);

        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.LINE_AXIS));

        jLabel2.setForeground(new java.awt.Color(204, 0, 0));
        jLabel2.setText("Last weeks schedule is not confirmed, please do so.");
        jPanel9.add(jLabel2);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );

        jPanel9.add(jPanel10);

        confirmBtn.setText("Confirm ");
        confirmBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmBtnActionPerformed(evt);
            }
        });
        jPanel9.add(confirmBtn);

        confirmationPanel.add(jPanel9);

        jPanel1.add(confirmationPanel);

        jPanel3.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel3.setMinimumSize(new java.awt.Dimension(195, 30));
        jPanel3.setPreferredSize(new java.awt.Dimension(100, 30));
        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("POST INSTRUCTIONS");
        jLabel1.setMaximumSize(new java.awt.Dimension(5000, 26));
        jPanel3.add(jLabel1);

        jPanel1.add(jPanel3);

        postInstrPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        postInstrPanel.setMaximumSize(new java.awt.Dimension(100000, 20000));
        postInstrPanel.setLayout(new javax.swing.BoxLayout(postInstrPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 10));
        jPanel2.setPreferredSize(new java.awt.Dimension(100, 10));
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jPanel5.setMaximumSize(new java.awt.Dimension(32767, 10));
        jPanel5.setPreferredSize(new java.awt.Dimension(100, 10));
        jPanel2.add(jPanel5);

        postInstrPanel.add(jPanel2);

        jPanel1.add(postInstrPanel);

        jPanel4.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel4.setMinimumSize(new java.awt.Dimension(182, 30));
        jPanel4.setPreferredSize(new java.awt.Dimension(182, 30));
        jPanel4.setLayout(new java.awt.GridLayout(1, 0));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("EMPLOYEE TESTING");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 0, 0));
        jLabel3.setMaximumSize(new java.awt.Dimension(5000, 30));
        jPanel4.add(jLabel3);

        jPanel1.add(jPanel4);

        testingPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        testingPanel.setMaximumSize(new java.awt.Dimension(30000, 300000));
        testingPanel.setLayout(new javax.swing.BoxLayout(testingPanel, javax.swing.BoxLayout.Y_AXIS));

        testingContainerPanel.setMaximumSize(new java.awt.Dimension(40000, 40000));
        testingContainerPanel.setLayout(new javax.swing.BoxLayout(testingContainerPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel7.setMaximumSize(new java.awt.Dimension(32767, 2));
        jPanel7.setMinimumSize(new java.awt.Dimension(10, 2));
        jPanel7.setPreferredSize(new java.awt.Dimension(100, 2));
        testingContainerPanel.add(jPanel7);

        jScrollPane1.setViewportView(testingContainerPanel);

        testingPanel.add(jScrollPane1);

        jPanel6.setMaximumSize(new java.awt.Dimension(32767, 40));
        jPanel6.setPreferredSize(new java.awt.Dimension(100, 40));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("*Test results will not be available until the day after ");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel4.setMinimumSize(new java.awt.Dimension(200, 13));
        jLabel4.setPreferredSize(new java.awt.Dimension(250, 15));

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("test is completed.");
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel5.setMinimumSize(new java.awt.Dimension(200, 13));
        jLabel5.setPreferredSize(new java.awt.Dimension(250, 15));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        testingPanel.add(jPanel6);

        jPanel1.add(testingPanel);

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents

    private void confirmBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmBtnActionPerformed
        int response = JOptionPane.showConfirmDialog(Main_Window.parentOfApplication, "This will confirm that the hours shown on the schedule below for\r\n"
                + "last week are accurate and your paycheck will reflect the hours. \r\n\r\nPlease check the hours shown below for the last week \r\n"
                + "and if they are accurate, click 'Yes' otherwise click 'No' \r\nand contact our office with any corrections to your times.",
                "Confirm Last Week?", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            String userId = Main_Window.parentOfApplication.myUser.getUserId();
            ScheduleController scheduleController = new ScheduleController(svp.getCompany());
            Employee tempEmployee = new Employee();

            tempEmployee.setEmployeeId(Integer.parseInt(userId));

            Calendar startCal = svp.getBegWeek();
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(startCal.getTime());
            endCal.add(Calendar.DAY_OF_MONTH, 6);

            try {
                ArrayList<ScheduleData> schedules = scheduleController.getSchedule(svp.getBegWeek().getTime(), endCal.getTime(), tempEmployee);
                for (int s = 0; s < schedules.size(); s++) {
                    if (!schedules.get(s).isShiftType(ShiftTypeClass.SHIFT_RECONCILED)) {
                        ScheduleData scheduleData = schedules.get(s);
                        ShiftTypeClass shiftType = new ShiftTypeClass(scheduleData.getType() + "");
                        shiftType.setVal(ShiftTypeClass.SHIFT_RECONCILED);
                        scheduleData.setType(shiftType.getVal());
                        ShiftOptionsClass payOpt = new ShiftOptionsClass();
                        payOpt.parse(scheduleData.getPayOpt());

                        ShiftOptionsClass billOpt = new ShiftOptionsClass();
                        billOpt.parse(scheduleData.getBillOpt());

                        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            if (scheduleData.isMaster()) {
                                scheduleController.deletePermShiftForOneWeek(scheduleData.getScheduleId(), scheduleData.getEmployeeId() + "", scheduleData.getClientId() + "", "0");
                                scheduleController.createTempShift(scheduleData.getEmployeeId() + "", scheduleData.getClientId() + "", "0",
                                        dbFormat.format(scheduleData.getDate()), scheduleData.getStartTime() + "", scheduleData.getEndTime() + "",
                                        scheduleData.getDayOfWeek(), shiftType, scheduleData.getGp() + "", scheduleData.getScheduleMasterId() + "",
                                        payOpt, billOpt, scheduleData.getRateCodeId());
                            } else {
                                scheduleController.editTempShift(scheduleData.getEmployeeId() + "", scheduleData.getClientId() + "",
                                        scheduleData.getScheduleId(), dbFormat.format(scheduleData.getDate()), scheduleData.getStartTime() + "", scheduleData.getEndTime() + "",
                                        scheduleData.getDayOfWeek() + "", shiftType, scheduleData.getGp() + "", scheduleData.getScheduleMasterId() + "",
                                        scheduleData.getIsDeleted() + "", scheduleData.getScheduleId(), payOpt, billOpt, scheduleData.getRateCodeId(),
                                        userId);
                            }
                        } catch (Exception exe) {
                            exe.printStackTrace();
                        }
                    }

                }

                confirmationPanel.setVisible(false);
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Your schedule is confirmed, thank you!");
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
    }//GEN-LAST:event_confirmBtnActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton confirmBtn;
    private javax.swing.JPanel confirmationPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel postInstrPanel;
    private javax.swing.JPanel testingContainerPanel;
    private javax.swing.JPanel testingPanel;
    private javax.swing.JLabel warningLabel;
    // End of variables declaration//GEN-END:variables
}
