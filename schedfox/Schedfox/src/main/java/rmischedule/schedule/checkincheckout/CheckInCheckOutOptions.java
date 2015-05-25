/*
 * CheckInCheckOutOptions.java
 *
 * Created on February 16, 2006, 10:54 AM
 */

package rmischedule.schedule.checkincheckout;

import rmischeduleserver.util.StaticDateTimeFunctions;
import schedfoxlib.model.util.Record_Set;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import rmischedule.security.security_detail;

import rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in.*;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.notes.*;
import rmischeduleserver.mysqlconnectivity.queries.client.*;
import rmischeduleserver.mysqlconnectivity.queries.employee.*;
import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.data_connection_types.*;

import rmischedule.data_connection.*;
import rmischedule.components.*;
import rmischedule.main.*;
import schedfoxlib.model.AssembleCheckinScheduleType;
/**
 *
 * @author  Ira Juneau
 */
public class CheckInCheckOutOptions extends javax.swing.JPanel {
    
    private IndividualCheckInCheckOutPanel currentPanel;
    private String originalStartTime;
    private Connection myConn;
     
    /** Creates new form CheckInCheckOutOptions */
    public CheckInCheckOutOptions() {
        initComponents();
        mainPanel.setBackground(new Color(255,255,255,195));
        originalStartTime = "";
        myConn = new Connection();
    }
    
    /**
     * Displays Data for selected panel
     */
    public void showData(IndividualCheckInCheckOutPanel myPanel) {
        populateText(myPanel);
        AssembleCheckinScheduleType checkinType = myPanel.getCheckinType();
        myConn.setCompany(checkinType.getCompanyId());
        
        if (currentPanel != null) {
            currentPanel.setBorderToSelected(false);
        }
        
        currentPanel = myPanel;
        currentPanel.setBorderToSelected(true);
        colorPanel.setBackground(myPanel.getBackground());
        setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, myPanel.getBackground())));
        
        if (myPanel.getMyParent() == CheckInCheckOutDataObject.myCheckOutPanel) {
            TitleLabel.setText("Check Out");
            jButton1.setText("Check Out");
            CheckInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Check Out Information"));
            jLabel14.setText("Exit Time ");
        } else {
            TitleLabel.setText("Check In");
            jButton1.setText("Check In");
            CheckInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Check In Information"));
            jLabel14.setText("Entry Time ");
        }
        
        originalStartTime = myPanel.StartLabel.getText();
        
        if (Main_Window.parentOfApplication.checkSecurity(security_detail.MODULES.ADMIN_GROUP)      ||
            Main_Window.parentOfApplication.checkSecurity(security_detail.MODULES.ADMIN_USER)       ||
            Main_Window.parentOfApplication.checkSecurity(security_detail.MODULES.SCHEDULING_EDIT)  ||
            Main_Window.parentOfApplication.checkSecurity(security_detail.MODULES.SCHEDULING_SHIFT_EDIT)) {
          this.CheckInfo.setVisible(true);  
        } else {
          this.CheckInfo.setVisible(false);              
        }
        
        setVisible(true);
    }
    
    private void populateText(IndividualCheckInCheckOutPanel myPanel) {
        
        AssembleCheckinScheduleType checkinSchedType = myPanel.getCheckinType();
        EmployeeName.setText(checkinSchedType.getEmp_first_name() + " " + checkinSchedType.getEmp_last_name());
        EmployeeID.setText(checkinSchedType.getEid().toString());
        EmployeePhone.setText(checkinSchedType.getEmp_phone());
        EmployeeCell.setText(checkinSchedType.getEmp_cell());
        EmployeeAddress.setText(checkinSchedType.getEmp_address());
        EmployeeAddress2.setText(checkinSchedType.getEmp_city() + ", " + checkinSchedType.getEmp_state() + " " + checkinSchedType.getEmp_zip());
        
        ClientName.setText(checkinSchedType.getClient_name());
        ClientPhone.setText(checkinSchedType.getClient_phone());
        ClientPhone2.setText(checkinSchedType.getClient_phone2());
        ClientAddress.setText(checkinSchedType.getClient_address());
        ClientAddress2.setText(checkinSchedType.getClient_city() + ", " + checkinSchedType.getClient_state() + " " + checkinSchedType.getClient_zip());
        
        companyLabel.setText(Main_Window.parentOfApplication.getCompanyNameById(checkinSchedType.getCompanyId()));
        
        TimeField.setText(myPanel.StartLabel.getText());
        UknownEntryTime.setSelected(false);
        Notes.setText("");
    }
    
    /**
     * Method to check in, save notes, change times...whateva!
     */
    public void checkInCheckOut() {
        AssembleCheckinScheduleType checkinType = currentPanel.getCheckinType();
        String shiftId = currentPanel.getShiftId();
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = myFormat.format(checkinType.getDate());

        String emp = currentPanel.getEmployeeId();
        if (!originalStartTime.trim().equals(TimeField.getText().trim())) {
            String oldShiftId = shiftId;
            shiftId = modifyShiftTime();
            if (shiftId == null) {
                return;
            } else {
                Main_Window.ciw.updateHashTableWithShiftId(oldShiftId, shiftId, currentPanel);
            }
        }
        if (Notes.getText().trim().length() > 0) {
            saveNoteForShift(shiftId);
        }
        if (currentPanel.getMyParent() == CheckInCheckOutDataObject.myCheckOutPanel) {
            saveCheckOut(shiftId);
        } else {
            saveCheckIn(shiftId, date, checkinType.getStart_time() + "", checkinType.getEnd_time() + "", emp);
        }
        setVisible(false);
    }
    
    /**
     * Used to modify the entry time for a given shift if it is different than given....
     */
    private String modifyShiftTime() {
        Record_Set newCheckInId = new Record_Set();
        update_check_in_out_time_query myQuery = new update_check_in_out_time_query();
        RunQueriesEx myTotalQuery = new RunQueriesEx();
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            boolean isCheckIn = true;
            if (currentPanel.getMyParent() == CheckInCheckOutDataObject.myCheckOutPanel) {
                isCheckIn = false;
            }
            myQuery.update(StaticDateTimeFunctions.fromTextToTime(TimeField.getText()), 
                    currentPanel.getShiftId(), isCheckIn, myFormat.format(currentPanel.getCheckinType().getDate()), Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));
            myConn.prepQuery(myQuery);
            myConn.setCompany(currentPanel.getCheckinType().getCompanyId());
            myTotalQuery.setCompany(currentPanel.getCheckinType().getCompanyId());
            myQuery.setCompany(currentPanel.getCheckinType().getCompanyId());
            myConn.setBranch(currentPanel.getCheckinType().getBranch_id() + "");
            myTotalQuery.add(myQuery);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Time Entered!");
            return null;
        }
        try {
            ArrayList al = myConn.executeQueryEx(myTotalQuery);
            newCheckInId = (Record_Set)al.get(0);
            newCheckInId.decompressData();
        } catch (Exception e) {}
        return newCheckInId.getString("sid");
    }
    
    /**
     * Saves checkin, of note, will save start and end time before it is fiddled with so later we 
     * can see if it has been modified....
     */
    private void saveCheckIn(String shiftId, String date, String stime, String etime, String emp) {
        save_check_in_query myCheckInQuery = new save_check_in_query();
        myCheckInQuery.update(date, shiftId, etime, stime, emp, Main_Window.parentOfApplication.getUser().getUserId(), System.currentTimeMillis() + "");
        try {
            myConn.prepQuery(myCheckInQuery);
            myConn.executeUpdate(myCheckInQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Marks given shift as checked out...
     */
    private void saveCheckOut(String sid) {
        save_check_out_query myCheckOutQuery = new save_check_out_query();
        myCheckOutQuery.update(sid, System.currentTimeMillis() + "");
        try {
            myConn.prepQuery(myCheckOutQuery);
            myConn.executeUpdate(myCheckOutQuery);
        } catch (Exception e) { }
    }
    
    /**
     * Saves notes if needed
     */
    private void saveNoteForShift(String shiftId) {
        save_schedule_note_query myNoteSave = new save_schedule_note_query();
        client_note_update_query myCliNoteSave = new client_note_update_query(currentPanel.getClientId());
        employee_note_update myEmpNoteSave = new employee_note_update(Integer.parseInt(currentPanel.getEmployeeId()));
        String noteType = "-2";
        if (currentPanel.getMyParent() == CheckInCheckOutDataObject.myCheckOutPanel) {
            noteType = "-3";
        }
        myNoteSave.update(shiftId, Notes.getText(), noteType);
        myCliNoteSave.update("0", noteType, Notes.getText(), Main_Window.parentOfApplication.getUser().getUserId(), StaticDateTimeFunctions.convertCalendarToDatabaseFormat(Calendar.getInstance()));;
        myEmpNoteSave.update(0, Integer.parseInt(noteType), Notes.getText(), Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()), StaticDateTimeFunctions.convertCalendarToDatabaseFormat(Calendar.getInstance()));;
        try {
            myConn.prepQuery(myNoteSave);
            myConn.prepQuery(myCliNoteSave);
            myConn.prepQuery(myEmpNoteSave);
            myConn.executeQuery(myNoteSave);
            myConn.executeQuery(myCliNoteSave);
            myConn.executeQuery(myEmpNoteSave);
        } catch (Exception e) {}
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        colorPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        TitleLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        companyLabel = new javax.swing.JLabel();
        EmpInfo = new javax.swing.JPanel();
        NamePanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        EmployeeName = new javax.swing.JLabel();
        EmployeeIDPanel = new javax.swing.JPanel();
        EmployeeIDLabel = new javax.swing.JLabel();
        EmployeeID = new javax.swing.JLabel();
        PhonePanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        EmployeePhone = new javax.swing.JLabel();
        CellPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        EmployeeCell = new javax.swing.JLabel();
        AddressPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        EmployeeAddress = new javax.swing.JLabel();
        Address2Panel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        EmployeeAddress2 = new javax.swing.JLabel();
        CliInfo = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        ClientName = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        ClientPhone = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        ClientPhone2 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        ClientAddress = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        ClientAddress2 = new javax.swing.JLabel();
        CheckInfo = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        TimeField = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        UknownEntryTime = new javax.swing.JCheckBox();
        ShiftNote = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Notes = new javax.swing.JTextArea();
        jPanel11 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(0, 0, 0))));
        setMaximumSize(new java.awt.Dimension(253, 566));
        setMinimumSize(new java.awt.Dimension(253, 566));
        setPreferredSize(new java.awt.Dimension(253, 566));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        colorPanel.setBackground(new java.awt.Color(160, 184, 238));
        colorPanel.setMaximumSize(new java.awt.Dimension(32767, 22));
        colorPanel.setMinimumSize(new java.awt.Dimension(10, 22));
        colorPanel.setPreferredSize(new java.awt.Dimension(100, 22));
        colorPanel.setLayout(new javax.swing.BoxLayout(colorPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText(" ");
        colorPanel.add(jLabel1);

        TitleLabel.setMaximumSize(new java.awt.Dimension(30004, 14));
        colorPanel.add(TitleLabel);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("X");
        jLabel2.setMaximumSize(new java.awt.Dimension(14, 14));
        jLabel2.setMinimumSize(new java.awt.Dimension(14, 14));
        jLabel2.setPreferredSize(new java.awt.Dimension(14, 14));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                setNonVisible(evt);
            }
        });
        colorPanel.add(jLabel2);

        add(colorPanel);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setLayout(new java.awt.GridLayout(1, 0));

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jPanel3.setOpaque(false);
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 22));
        jPanel1.setMinimumSize(new java.awt.Dimension(41, 22));
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(242, 22));
        jPanel1.setLayout(new java.awt.GridLayout());

        companyLabel.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        companyLabel.setMaximumSize(new java.awt.Dimension(40000, 20));
        jPanel1.add(companyLabel);

        jPanel3.add(jPanel1);

        EmpInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Employee Information"));
        EmpInfo.setMaximumSize(new java.awt.Dimension(32779, 150));
        EmpInfo.setMinimumSize(new java.awt.Dimension(22, 130));
        EmpInfo.setOpaque(false);
        EmpInfo.setPreferredSize(new java.awt.Dimension(112, 130));
        EmpInfo.setLayout(new javax.swing.BoxLayout(EmpInfo, javax.swing.BoxLayout.Y_AXIS));

        NamePanel.setMaximumSize(new java.awt.Dimension(32767, 14));
        NamePanel.setMinimumSize(new java.awt.Dimension(10, 14));
        NamePanel.setOpaque(false);
        NamePanel.setPreferredSize(new java.awt.Dimension(100, 14));
        NamePanel.setLayout(new javax.swing.BoxLayout(NamePanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel3.setText("Name ");
        jLabel3.setMaximumSize(new java.awt.Dimension(70, 14));
        jLabel3.setMinimumSize(new java.awt.Dimension(70, 14));
        jLabel3.setPreferredSize(new java.awt.Dimension(70, 14));
        NamePanel.add(jLabel3);

        EmployeeName.setFont(new java.awt.Font("Tahoma", 1, 10));
        EmployeeName.setMaximumSize(new java.awt.Dimension(2000, 14));
        EmployeeName.setMinimumSize(new java.awt.Dimension(0, 14));
        EmployeeName.setPreferredSize(new java.awt.Dimension(200, 14));
        NamePanel.add(EmployeeName);

        EmpInfo.add(NamePanel);

        EmployeeIDPanel.setMaximumSize(new java.awt.Dimension(32767, 14));
        EmployeeIDPanel.setMinimumSize(new java.awt.Dimension(10, 14));
        EmployeeIDPanel.setOpaque(false);
        EmployeeIDPanel.setPreferredSize(new java.awt.Dimension(100, 14));
        EmployeeIDPanel.setLayout(new javax.swing.BoxLayout(EmployeeIDPanel, javax.swing.BoxLayout.LINE_AXIS));

        EmployeeIDLabel.setFont(new java.awt.Font("Tahoma", 0, 10));
        EmployeeIDLabel.setText("Employee ID");
        EmployeeIDLabel.setMaximumSize(new java.awt.Dimension(70, 14));
        EmployeeIDLabel.setMinimumSize(new java.awt.Dimension(70, 14));
        EmployeeIDLabel.setPreferredSize(new java.awt.Dimension(70, 14));
        EmployeeIDPanel.add(EmployeeIDLabel);

        EmployeeID.setFont(new java.awt.Font("Tahoma", 1, 10));
        EmployeeID.setMaximumSize(new java.awt.Dimension(2000, 14));
        EmployeeID.setMinimumSize(new java.awt.Dimension(0, 14));
        EmployeeID.setPreferredSize(new java.awt.Dimension(200, 14));
        EmployeeIDPanel.add(EmployeeID);

        EmpInfo.add(EmployeeIDPanel);

        PhonePanel.setMaximumSize(new java.awt.Dimension(32767, 14));
        PhonePanel.setMinimumSize(new java.awt.Dimension(10, 14));
        PhonePanel.setOpaque(false);
        PhonePanel.setPreferredSize(new java.awt.Dimension(100, 14));
        PhonePanel.setLayout(new javax.swing.BoxLayout(PhonePanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel5.setText("Phone ");
        jLabel5.setMaximumSize(new java.awt.Dimension(70, 14));
        jLabel5.setMinimumSize(new java.awt.Dimension(70, 14));
        jLabel5.setPreferredSize(new java.awt.Dimension(70, 14));
        PhonePanel.add(jLabel5);

        EmployeePhone.setFont(new java.awt.Font("Tahoma", 1, 10));
        EmployeePhone.setMaximumSize(new java.awt.Dimension(2000, 14));
        EmployeePhone.setMinimumSize(new java.awt.Dimension(0, 14));
        EmployeePhone.setPreferredSize(new java.awt.Dimension(200, 14));
        PhonePanel.add(EmployeePhone);

        EmpInfo.add(PhonePanel);

        CellPanel.setMaximumSize(new java.awt.Dimension(32767, 14));
        CellPanel.setMinimumSize(new java.awt.Dimension(10, 14));
        CellPanel.setOpaque(false);
        CellPanel.setPreferredSize(new java.awt.Dimension(100, 14));
        CellPanel.setLayout(new javax.swing.BoxLayout(CellPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel6.setText("Cell ");
        jLabel6.setMaximumSize(new java.awt.Dimension(70, 14));
        jLabel6.setMinimumSize(new java.awt.Dimension(70, 14));
        jLabel6.setPreferredSize(new java.awt.Dimension(70, 14));
        CellPanel.add(jLabel6);

        EmployeeCell.setFont(new java.awt.Font("Tahoma", 1, 10));
        EmployeeCell.setMaximumSize(new java.awt.Dimension(2000, 14));
        EmployeeCell.setMinimumSize(new java.awt.Dimension(0, 14));
        EmployeeCell.setPreferredSize(new java.awt.Dimension(200, 14));
        CellPanel.add(EmployeeCell);

        EmpInfo.add(CellPanel);

        AddressPanel.setMaximumSize(new java.awt.Dimension(32767, 14));
        AddressPanel.setMinimumSize(new java.awt.Dimension(10, 14));
        AddressPanel.setOpaque(false);
        AddressPanel.setPreferredSize(new java.awt.Dimension(100, 14));
        AddressPanel.setLayout(new javax.swing.BoxLayout(AddressPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel7.setText("Address 1 ");
        jLabel7.setMaximumSize(new java.awt.Dimension(70, 14));
        jLabel7.setMinimumSize(new java.awt.Dimension(70, 14));
        jLabel7.setPreferredSize(new java.awt.Dimension(70, 14));
        AddressPanel.add(jLabel7);

        EmployeeAddress.setFont(new java.awt.Font("Tahoma", 1, 10));
        EmployeeAddress.setMaximumSize(new java.awt.Dimension(2000, 14));
        EmployeeAddress.setMinimumSize(new java.awt.Dimension(0, 14));
        EmployeeAddress.setPreferredSize(new java.awt.Dimension(200, 14));
        AddressPanel.add(EmployeeAddress);

        EmpInfo.add(AddressPanel);

        Address2Panel.setMaximumSize(new java.awt.Dimension(32767, 14));
        Address2Panel.setMinimumSize(new java.awt.Dimension(10, 14));
        Address2Panel.setOpaque(false);
        Address2Panel.setPreferredSize(new java.awt.Dimension(100, 14));
        Address2Panel.setLayout(new javax.swing.BoxLayout(Address2Panel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel8.setText("Address 2 ");
        jLabel8.setMaximumSize(new java.awt.Dimension(70, 14));
        jLabel8.setMinimumSize(new java.awt.Dimension(70, 14));
        jLabel8.setPreferredSize(new java.awt.Dimension(70, 14));
        Address2Panel.add(jLabel8);

        EmployeeAddress2.setFont(new java.awt.Font("Tahoma", 1, 10));
        EmployeeAddress2.setMaximumSize(new java.awt.Dimension(2000, 14));
        EmployeeAddress2.setMinimumSize(new java.awt.Dimension(0, 14));
        EmployeeAddress2.setPreferredSize(new java.awt.Dimension(200, 14));
        Address2Panel.add(EmployeeAddress2);

        EmpInfo.add(Address2Panel);

        jPanel3.add(EmpInfo);

        CliInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Client Information"));
        CliInfo.setMaximumSize(new java.awt.Dimension(32779, 115));
        CliInfo.setMinimumSize(new java.awt.Dimension(22, 115));
        CliInfo.setOpaque(false);
        CliInfo.setPreferredSize(new java.awt.Dimension(112, 115));
        CliInfo.setLayout(new javax.swing.BoxLayout(CliInfo, javax.swing.BoxLayout.Y_AXIS));

        jPanel5.setMaximumSize(new java.awt.Dimension(32767, 14));
        jPanel5.setMinimumSize(new java.awt.Dimension(10, 14));
        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(100, 14));
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel9.setText("Name");
        jLabel9.setMaximumSize(new java.awt.Dimension(70, 14));
        jLabel9.setMinimumSize(new java.awt.Dimension(70, 14));
        jLabel9.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel5.add(jLabel9);

        ClientName.setFont(new java.awt.Font("Tahoma", 1, 10));
        ClientName.setMaximumSize(new java.awt.Dimension(4500, 14));
        ClientName.setMinimumSize(new java.awt.Dimension(45, 14));
        ClientName.setPreferredSize(new java.awt.Dimension(45, 14));
        jPanel5.add(ClientName);

        CliInfo.add(jPanel5);

        jPanel6.setMaximumSize(new java.awt.Dimension(32767, 14));
        jPanel6.setMinimumSize(new java.awt.Dimension(10, 14));
        jPanel6.setOpaque(false);
        jPanel6.setPreferredSize(new java.awt.Dimension(100, 14));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel10.setText("Phone 1");
        jLabel10.setMaximumSize(new java.awt.Dimension(70, 14));
        jLabel10.setMinimumSize(new java.awt.Dimension(70, 14));
        jLabel10.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel6.add(jLabel10);

        ClientPhone.setFont(new java.awt.Font("Tahoma", 1, 10));
        ClientPhone.setMaximumSize(new java.awt.Dimension(4500, 14));
        ClientPhone.setMinimumSize(new java.awt.Dimension(45, 14));
        ClientPhone.setPreferredSize(new java.awt.Dimension(45, 14));
        jPanel6.add(ClientPhone);

        CliInfo.add(jPanel6);

        jPanel7.setMaximumSize(new java.awt.Dimension(32767, 14));
        jPanel7.setMinimumSize(new java.awt.Dimension(10, 14));
        jPanel7.setOpaque(false);
        jPanel7.setPreferredSize(new java.awt.Dimension(100, 14));
        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.LINE_AXIS));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel11.setText("Phone 2");
        jLabel11.setMaximumSize(new java.awt.Dimension(70, 14));
        jLabel11.setMinimumSize(new java.awt.Dimension(70, 14));
        jLabel11.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel7.add(jLabel11);

        ClientPhone2.setFont(new java.awt.Font("Tahoma", 1, 10));
        ClientPhone2.setMaximumSize(new java.awt.Dimension(4500, 14));
        ClientPhone2.setMinimumSize(new java.awt.Dimension(45, 14));
        ClientPhone2.setPreferredSize(new java.awt.Dimension(45, 14));
        jPanel7.add(ClientPhone2);

        CliInfo.add(jPanel7);

        jPanel8.setMaximumSize(new java.awt.Dimension(32767, 14));
        jPanel8.setMinimumSize(new java.awt.Dimension(10, 14));
        jPanel8.setOpaque(false);
        jPanel8.setPreferredSize(new java.awt.Dimension(100, 14));
        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.LINE_AXIS));

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel12.setText("Address 1");
        jLabel12.setMaximumSize(new java.awt.Dimension(70, 14));
        jLabel12.setMinimumSize(new java.awt.Dimension(70, 14));
        jLabel12.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel8.add(jLabel12);

        ClientAddress.setFont(new java.awt.Font("Tahoma", 1, 10));
        ClientAddress.setMaximumSize(new java.awt.Dimension(4500, 14));
        ClientAddress.setMinimumSize(new java.awt.Dimension(45, 14));
        ClientAddress.setPreferredSize(new java.awt.Dimension(45, 14));
        jPanel8.add(ClientAddress);

        CliInfo.add(jPanel8);

        jPanel9.setMaximumSize(new java.awt.Dimension(32767, 14));
        jPanel9.setMinimumSize(new java.awt.Dimension(10, 14));
        jPanel9.setOpaque(false);
        jPanel9.setPreferredSize(new java.awt.Dimension(100, 14));
        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.LINE_AXIS));

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel13.setText("Address 2");
        jLabel13.setMaximumSize(new java.awt.Dimension(70, 14));
        jLabel13.setMinimumSize(new java.awt.Dimension(70, 14));
        jLabel13.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel9.add(jLabel13);

        ClientAddress2.setFont(new java.awt.Font("Tahoma", 1, 10));
        ClientAddress2.setMaximumSize(new java.awt.Dimension(4500, 14));
        ClientAddress2.setMinimumSize(new java.awt.Dimension(45, 14));
        ClientAddress2.setPreferredSize(new java.awt.Dimension(45, 14));
        jPanel9.add(ClientAddress2);

        CliInfo.add(jPanel9);

        jPanel3.add(CliInfo);

        CheckInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Check In Information"));
        CheckInfo.setMaximumSize(new java.awt.Dimension(32767, 90));
        CheckInfo.setMinimumSize(new java.awt.Dimension(22, 90));
        CheckInfo.setOpaque(false);
        CheckInfo.setPreferredSize(new java.awt.Dimension(100, 90));
        CheckInfo.setLayout(new java.awt.GridLayout(1, 0));

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        jPanel4.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel4.setMinimumSize(new java.awt.Dimension(10, 25));
        jPanel4.setOpaque(false);
        jPanel4.setPreferredSize(new java.awt.Dimension(100, 25));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        jLabel14.setText("Entry Time ");
        jLabel14.setMaximumSize(new java.awt.Dimension(7000, 14));
        jLabel14.setMinimumSize(new java.awt.Dimension(70, 14));
        jLabel14.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel4.add(jLabel14);

        TimeField.setBackground(new java.awt.Color(253, 254, 212));
        TimeField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        TimeField.setMaximumSize(new java.awt.Dimension(60, 24));
        TimeField.setMinimumSize(new java.awt.Dimension(60, 24));
        TimeField.setPreferredSize(new java.awt.Dimension(60, 24));
        jPanel4.add(TimeField);

        jPanel2.add(jPanel4);

        jPanel10.setMaximumSize(new java.awt.Dimension(32767, 24));
        jPanel10.setMinimumSize(new java.awt.Dimension(10, 24));
        jPanel10.setOpaque(false);
        jPanel10.setPreferredSize(new java.awt.Dimension(100, 24));
        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.LINE_AXIS));

        UknownEntryTime.setText("Unknown Entry Time");
        UknownEntryTime.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        UknownEntryTime.setMaximumSize(new java.awt.Dimension(7300, 15));
        jPanel10.add(UknownEntryTime);

        jPanel2.add(jPanel10);

        CheckInfo.add(jPanel2);

        jPanel3.add(CheckInfo);

        ShiftNote.setBorder(javax.swing.BorderFactory.createTitledBorder("Add Check In/Out Note"));
        ShiftNote.setMaximumSize(new java.awt.Dimension(32767, 100));
        ShiftNote.setOpaque(false);
        ShiftNote.setPreferredSize(new java.awt.Dimension(22, 100));

        jScrollPane1.setWheelScrollingEnabled(false);

        Notes.setColumns(10);
        Notes.setRows(3);
        jScrollPane1.setViewportView(Notes);

        org.jdesktop.layout.GroupLayout ShiftNoteLayout = new org.jdesktop.layout.GroupLayout(ShiftNote);
        ShiftNote.setLayout(ShiftNoteLayout);
        ShiftNoteLayout.setHorizontalGroup(
            ShiftNoteLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
        );
        ShiftNoteLayout.setVerticalGroup(
            ShiftNoteLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
        );

        jPanel3.add(ShiftNote);

        jPanel11.setMinimumSize(new java.awt.Dimension(10, 0));
        jPanel11.setOpaque(false);
        jPanel11.setLayout(new java.awt.BorderLayout());

        jButton1.setText("Check In");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveCheckInOut(evt);
            }
        });
        jPanel11.add(jButton1, java.awt.BorderLayout.NORTH);

        jPanel3.add(jPanel11);

        mainPanel.add(jPanel3);

        add(mainPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
    }//GEN-LAST:event_formMouseClicked

    private void saveCheckInOut(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveCheckInOut
        checkInCheckOut();
        currentPanel.setBorderToSelected(false);
    }//GEN-LAST:event_saveCheckInOut

    private void setNonVisible(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_setNonVisible
        setVisible(false);
    }//GEN-LAST:event_setNonVisible
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Address2Panel;
    private javax.swing.JPanel AddressPanel;
    private javax.swing.JPanel CellPanel;
    private javax.swing.JPanel CheckInfo;
    private javax.swing.JPanel CliInfo;
    private javax.swing.JLabel ClientAddress;
    private javax.swing.JLabel ClientAddress2;
    private javax.swing.JLabel ClientName;
    private javax.swing.JLabel ClientPhone;
    private javax.swing.JLabel ClientPhone2;
    private javax.swing.JPanel EmpInfo;
    private javax.swing.JLabel EmployeeAddress;
    private javax.swing.JLabel EmployeeAddress2;
    private javax.swing.JLabel EmployeeCell;
    private javax.swing.JLabel EmployeeID;
    private javax.swing.JLabel EmployeeIDLabel;
    private javax.swing.JPanel EmployeeIDPanel;
    private javax.swing.JLabel EmployeeName;
    private javax.swing.JLabel EmployeePhone;
    private javax.swing.JPanel NamePanel;
    private javax.swing.JTextArea Notes;
    private javax.swing.JPanel PhonePanel;
    private javax.swing.JPanel ShiftNote;
    private javax.swing.JTextField TimeField;
    private javax.swing.JLabel TitleLabel;
    private javax.swing.JCheckBox UknownEntryTime;
    private javax.swing.JPanel colorPanel;
    private javax.swing.JLabel companyLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables
    
}
