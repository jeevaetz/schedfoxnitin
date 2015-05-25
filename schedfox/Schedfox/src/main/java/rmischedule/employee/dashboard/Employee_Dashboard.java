/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Employee_Dashboard.java
 *
 * Created on Oct 14, 2010, 2:44:30 PM
 */
package rmischedule.employee.dashboard;

import com.creamtec.ajaxswing.AjaxSwingManager;
import com.creamtec.ajaxswing.core.ClientAgent;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import javax.swing.ImageIcon;
import rmischedule.components.DynamicFieldPanel;
import rmischedule.components.PicturePanel;
import rmischedule.components.PictureParentInterface;
import rmischedule.data_connection.Connection;
import schedfoxlib.model.Company;
import rmischedule.main.Main_Window;
import rmischedule.schedule.ScheduleDashboardPanel;
import rmischedule.schedule.Schedule_Main_Form;
import rmischedule.schedule.Schedule_View_Panel;
import rmischedule.schedule.components.test.EmployeeTestPanel;
import rmischedule.xadmin.model.DynamicFieldValue;
import rmischeduleserver.control.TimeOffController;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Employee;
import schedfoxlib.model.EmployeeTestScore;
import schedfoxlib.model.TimeOffCalc;
import rmischeduleserver.mysqlconnectivity.queries.employee.get_employee_test_scores_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.get_first_day_worked_query;
import rmischeduleserver.mysqlconnectivity.queries.util.get_dynamic_field_values_for_key_query;

/**
 *
 * @author user
 */
public class Employee_Dashboard extends javax.swing.JPanel implements PictureParentInterface {

    private SimpleDateFormat simpleDateFormat;
    private ScheduleDashboardPanel myParent;
    private Employee selectedEmployee;
    /** Creates new form Employee_Dashboard */
    public Employee_Dashboard(ScheduleDashboardPanel panel) {
        initComponents();
        setEmployeeLoggedIn();
        this.myParent = panel;

        this.simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

        ((PicturePanel)imagePanel).setAllowDelete(false);
        ((PicturePanel)imagePanel).setAllowEdit(false);
        ((PicturePanel)imagePanel).setAllowDownload(false);

        if (!Main_Window.isEmployeeLoggedIn()) {
            vacationPanel.setVisible(false);
            vacationOffPanel.setVisible(false);
        }
    }

    public void setEmployee(Employee emp, Schedule_View_Panel svp) {
        selectedEmployee = emp;
        try {
            if (AjaxSwingManager.isAjaxSwingRunning()) {
                AjaxSwingManager.beginOperation();
            }
        } catch (Exception exe) {
        }
        
        Calendar currDate = Calendar.getInstance();
        Random random = new Random();
        int pick = random.nextInt(2000);
        currDate.add(Calendar.DAY_OF_YEAR, -pick);

        nameLabel.setText(emp.getEmployeeFirstName() + " " + emp.getEmployeeLastName());

        ImageIcon empIcon = null;
        try {
            Company currComp = Main_Window.parentOfApplication.getCompanyById(svp.getCompany());
            empIcon = emp.getEmployeeImage(currComp.getDB());
        } catch (Exception e) {
        }
        if (empIcon != null) {
            ((PicturePanel)imagePanel).setImage(empIcon.getImage());
        } else {
            ((PicturePanel)imagePanel).setImage(null);
        }

        try {
            TimeOffController timeController =
                TimeOffController.getInstance(svp.getCompany());

            HashMap<Integer, Integer> integers = timeController.getAdjustments(emp.getEmployeeId());

            double sickDaysOff =
                    timeController.getCurrentCalculationsForEmployee(emp.getEmployeeId(), 3);
            Integer sickDaysAdjust = integers.get(3);
            if (sickDaysAdjust != null) {
                sickDaysOff = sickDaysOff - sickDaysAdjust;
            }
            if (sickDaysOff < 0) {
                sickDaysOff = 0;
            }
            availVacationOffLabel.setText(sickDaysOff + "");

            ArrayList<TimeOffCalc> vacationCalcs =
                    timeController.getCalculationsForEmployee(emp.getEmployeeId(), 2);
            double daysOff = 0;
            double daysTaken = 0;
            for (int p = 0; p < vacationCalcs.size(); p++) {
                if (!vacationCalcs.get(p).isIsExpired()) {
                    double adjust = (double)vacationCalcs.get(p).getDaysAccrued() - vacationCalcs.get(p).getDaysTaken().doubleValue();
                    daysOff += adjust;
                    daysTaken += vacationCalcs.get(p).getDaysTaken().doubleValue();
                }
            }
            availVacationOffLabel.setText(daysTaken + "");
            availVacationDaysLabel.setText(daysOff + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        get_dynamic_field_values_for_key_query myQuery = new get_dynamic_field_values_for_key_query();
        try {
            myQuery.update(emp.getEmployeeId(), 0, true, false);
        } catch (Exception e) {
        }

        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        Record_Set rs = svp.getConnection().executeQuery(myQuery);
        this.additionalDataPanel.removeAll();
        for (int r = 0; r < rs.length(); r++) {
            DynamicFieldValue fieldValue = new DynamicFieldValue(rs, emp.getEmployeeId());
            try {
                myFormat.parse(fieldValue.getDynamic_field_value());
            } catch (Exception e) {
                //If the date is missing or invalid we assume the date is the hire date!
                fieldValue.setDynamic_field_value(myFormat.format(emp.getEmployeeHireDate()));
            }

            DynamicFieldPanel myPanel = new DynamicFieldPanel(fieldValue, false);

            myPanel.setEnabled(false);
            this.additionalDataPanel.add(myPanel);

            rs.moveNext();
        }

        try {
            get_first_day_worked_query myFirstDayQuery = new get_first_day_worked_query();
            myFirstDayQuery.setPreparedStatement(new Object[]{
                        emp.getEmployeeId(), Integer.parseInt(Main_Window.parentOfApplication.myUser.getUserId()),
                        emp.getEmployeeId(), Integer.parseInt(Main_Window.parentOfApplication.myUser.getUserId())});
            Connection myConn = svp.getConnection();
            Record_Set rst = myConn.executeQuery(myFirstDayQuery);
            Date minDate = new Date();
            for (int r = 0; r < rst.length(); r++) {
                try {
                    if (minDate.compareTo(rst.getDate("mindate")) >= 0) {
                        minDate = rst.getDate("mindate");
                    }
                } catch (Exception e) {
                    System.out.println("Bad Date: " + rst.getString("mindate"));
                }
                rst.moveNext();
            }
            onsiteText.setText(simpleDateFormat.format(minDate));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (AjaxSwingManager.isAjaxSwingRunning()) {
                AjaxSwingManager.endOperation();
                ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(this, true);
                //ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(this.jPanel2, true);
                //ClientAgent.getCurrentInstance().setUpdateBrowser(true);
            }
        } catch (Exception e) {
        }


    }
    private void setEmployeeLoggedIn(){
        if(Main_Window.parentOfApplication.isEmployeeLoggedIn())
            editProfileButton.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(ActionEvent e) {
                  try {
                  Main_Window.getEmployeeEditWindow().setInformation(Schedule_Main_Form.companyId,
                          Schedule_Main_Form.branchId, String.valueOf(selectedEmployee.getEmployeeId()));
                    Main_Window.getEmployeeEditWindow().setVisible(true);
                    Main_Window.getEmployeeEditWindow().setSelected( String.valueOf(selectedEmployee.getEmployeeId()));
                  } catch (Exception exe) {

                  } finally {
                      if (AjaxSwingManager.isAjaxSwingRunning()) {
                        AjaxSwingManager.endOperation();
                      }
                  }
                }
            });
        else
            editProfileButton.setVisible(false);

    }
    /**
     * Load Employee Test Scores, loads all of them from the provided employee id.
     * @param emp
     * @param svp
     */
    public void loadEmployeeTestScores(Schedule_View_Panel svp) {
        get_employee_test_scores_query scoresQuery = new get_employee_test_scores_query();
        scoresQuery.setPreparedStatement(new Object[]{selectedEmployee.getEmployeeId(),
                    myParent.getCurrentClientId(), myParent.getCurrentClientId()});
        Connection myConn = svp.getConnection();

        Record_Set rst = myConn.executeQuery(scoresQuery);
        ArrayList<EmployeeTestScore> scores = new ArrayList<EmployeeTestScore>();
        trainingPanel.removeAll();
        trainingPanel.add(new EmployeeTestPanel());


        for (int r = 0; r
                < rst.length(); r++) {
            EmployeeTestScore score = new EmployeeTestScore(rst);
            rst.moveNext();
            scores.add(score);

            trainingPanel.add(new EmployeeTestPanel(score, svp));


        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        namePanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        editProfileButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        containerPanel = new javax.swing.JPanel();
        imagePanel = new PicturePanel(this);
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        onsiteText = new javax.swing.JLabel();
        additionalDataPanel = new javax.swing.JPanel();
        vacationOffPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        availVacationOffLabel = new javax.swing.JLabel();
        vacationPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        availVacationDaysLabel = new javax.swing.JLabel();
        bottomPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        trainingPanel = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Employee Information"));
        setMaximumSize(new java.awt.Dimension(400, 2147483647));
        setMinimumSize(new java.awt.Dimension(370, 485));
        setPreferredSize(new java.awt.Dimension(400, 485));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanel4.setMinimumSize(new java.awt.Dimension(100, 440));
        jPanel4.setPreferredSize(new java.awt.Dimension(100, 470));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        namePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 8, 1, 1));
        namePanel.setMaximumSize(new java.awt.Dimension(32767, 40));
        namePanel.setMinimumSize(new java.awt.Dimension(100, 40));
        namePanel.setLayout(new javax.swing.BoxLayout(namePanel, javax.swing.BoxLayout.LINE_AXIS));

        nameLabel.setFont(new java.awt.Font("Tahoma", 1, 18));
        nameLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        nameLabel.setMaximumSize(new java.awt.Dimension(500000, 35));
        nameLabel.setMinimumSize(new java.awt.Dimension(100, 35));
        nameLabel.setPreferredSize(new java.awt.Dimension(100, 35));
        namePanel.add(nameLabel);

        editProfileButton.setText("Edit Profile");
        editProfileButton.setMaximumSize(new java.awt.Dimension(110, 20));
        editProfileButton.setMinimumSize(new java.awt.Dimension(90, 20));
        editProfileButton.setPreferredSize(new java.awt.Dimension(110, 20));
        namePanel.add(editProfileButton);

        jButton1.setText("Logout");
        jButton1.setMaximumSize(new java.awt.Dimension(66, 20));
        jButton1.setMinimumSize(new java.awt.Dimension(66, 20));
        jButton1.setPreferredSize(new java.awt.Dimension(66, 20));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        namePanel.add(jButton1);

        jPanel4.add(namePanel);

        containerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        containerPanel.setMaximumSize(new java.awt.Dimension(40000, 160));
        containerPanel.setMinimumSize(new java.awt.Dimension(0, 160));
        containerPanel.setPreferredSize(new java.awt.Dimension(576, 160));
        containerPanel.setLayout(new javax.swing.BoxLayout(containerPanel, javax.swing.BoxLayout.X_AXIS));
        containerPanel.add(imagePanel);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        jPanel1.setMaximumSize(new java.awt.Dimension(10000, 1000));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jPanel3.setMaximumSize(new java.awt.Dimension(500, 500));
        jPanel3.setMinimumSize(new java.awt.Dimension(146, 26));
        jPanel3.setPreferredSize(new java.awt.Dimension(146, 26));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.X_AXIS));

        jLabel4.setText("Onsite Training");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel4.setMaximumSize(new java.awt.Dimension(140, 30));
        jLabel4.setMinimumSize(new java.awt.Dimension(140, 14));
        jLabel4.setPreferredSize(new java.awt.Dimension(140, 14));
        jPanel3.add(jLabel4);

        onsiteText.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        onsiteText.setMaximumSize(new java.awt.Dimension(100, 30));
        jPanel3.add(onsiteText);

        jPanel1.add(jPanel3);

        additionalDataPanel.setLayout(new java.awt.GridLayout(0, 1));
        jPanel1.add(additionalDataPanel);

        containerPanel.add(jPanel1);

        jPanel4.add(containerPanel);

        vacationOffPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 30, 0, 0));
        vacationOffPanel.setMaximumSize(new java.awt.Dimension(32767, 30));
        vacationOffPanel.setMinimumSize(new java.awt.Dimension(10, 30));
        vacationOffPanel.setPreferredSize(new java.awt.Dimension(100, 30));
        vacationOffPanel.setLayout(new javax.swing.BoxLayout(vacationOffPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel2.setFont(new java.awt.Font("sansserif", 1, 12));
        jLabel2.setText("Vacation Days Used");
        jLabel2.setMaximumSize(new java.awt.Dimension(160, 16));
        jLabel2.setMinimumSize(new java.awt.Dimension(160, 16));
        jLabel2.setPreferredSize(new java.awt.Dimension(160, 16));
        vacationOffPanel.add(jLabel2);

        availVacationOffLabel.setFont(new java.awt.Font("sansserif", 1, 12));
        availVacationOffLabel.setMaximumSize(new java.awt.Dimension(50, 16));
        availVacationOffLabel.setMinimumSize(new java.awt.Dimension(50, 16));
        availVacationOffLabel.setPreferredSize(new java.awt.Dimension(50, 16));
        vacationOffPanel.add(availVacationOffLabel);

        jPanel4.add(vacationOffPanel);

        vacationPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 30, 0, 0));
        vacationPanel.setMaximumSize(new java.awt.Dimension(32767, 30));
        vacationPanel.setMinimumSize(new java.awt.Dimension(10, 30));
        vacationPanel.setPreferredSize(new java.awt.Dimension(100, 30));
        vacationPanel.setLayout(new javax.swing.BoxLayout(vacationPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 12));
        jLabel1.setText("Available Vacation Days");
        jLabel1.setMaximumSize(new java.awt.Dimension(160, 16));
        jLabel1.setMinimumSize(new java.awt.Dimension(160, 16));
        jLabel1.setPreferredSize(new java.awt.Dimension(160, 16));
        vacationPanel.add(jLabel1);

        availVacationDaysLabel.setFont(new java.awt.Font("sansserif", 1, 12));
        availVacationDaysLabel.setMaximumSize(new java.awt.Dimension(50, 16));
        availVacationDaysLabel.setMinimumSize(new java.awt.Dimension(50, 16));
        availVacationDaysLabel.setPreferredSize(new java.awt.Dimension(50, 16));
        vacationPanel.add(availVacationDaysLabel);

        jPanel4.add(vacationPanel);

        bottomPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Employees Test Scores"));
        bottomPanel.setLayout(new java.awt.GridLayout(1, 0));

        trainingPanel.setLayout(new javax.swing.BoxLayout(trainingPanel, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(trainingPanel);

        bottomPanel.add(jScrollPane1);

        jPanel4.add(bottomPanel);

        add(jPanel4);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel additionalDataPanel;
    private javax.swing.JLabel availVacationDaysLabel;
    private javax.swing.JLabel availVacationOffLabel;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JPanel containerPanel;
    private javax.swing.JButton editProfileButton;
    private javax.swing.JPanel imagePanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JPanel namePanel;
    private javax.swing.JLabel onsiteText;
    private javax.swing.JPanel trainingPanel;
    private javax.swing.JPanel vacationOffPanel;
    private javax.swing.JPanel vacationPanel;
    // End of variables declaration//GEN-END:variables

    public void editImage(PicturePanel panel) {
        
    }

    public void addImage(PicturePanel panel) {
        
    }

    public void deleteImage(PicturePanel panel) {
        
    }

    public void downloadImage(PicturePanel panel) {
        
    }
}
