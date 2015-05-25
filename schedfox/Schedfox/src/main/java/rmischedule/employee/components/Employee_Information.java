/*
 * Employee_Information.java
 *
 * Created on July 22, 2004, 12:12 PM
 *
 */
/**
 * Modifications: Jeffrey Davis renamed tab from "Information" to "Info" on
 * 06/16/2010 per Jim's request
 */
package rmischedule.employee.components;

import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischedule.utility.TextField;
import java.io.File;
import rmischedule.main.Main_Window;
import rmischedule.components.*;
import rmischedule.components.jcalendar.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import rmischedule.components.graphicalcomponents.*;
import javax.swing.*;
import javax.swing.event.*;
import rmischedule.components.other_functions;
import schedfoxlib.model.util.Record_Set;
import rmischedule.employee.*;
import rmischedule.security.*;
import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.mysqlconnectivity.queries.employee.*;
import rmischedule.employee.data_components.EmployeeType;
import schedfoxlib.model.Company;
import schedfoxlib.model.util.ImageLoader;
import rmischedule.options.optiontypeclasses.StringOptionClass;
import rmischedule.utility.PicturePanelUtilities;
import rmischeduleserver.control.EmployeeController;
import schedfoxlib.model.Employee;
import rmischeduleserver.mysqlconnectivity.queries.util.GenericQuery;
import rmischeduleserver.mysqlconnectivity.queries.util.get_dynamic_field_values_for_key_query;

/**
 *
 * @author jason.allen Employee type = 0 is regular employee, type = 1 is
 * supervisor employee.
 *
 */
public class Employee_Information extends GenericEditSubForm implements PictureParentInterface {

    private xEmployeeEdit mainForm;
    private JCalendarComboBox calHireDate;
    private JCalendarComboBox calTermDate;
    private Vector<JTextField> myComponents;
    private Vector<JTextField> requiredFields;
    private Hashtable<EmployeeType, JRadioButton> employeeTypeHash;
    private int RecordSetNumber;
    private loadStatsThread myThread;
    private DateFormat myDateFormat;
    private JFileChooser fchooser;
    private PicturePanel employeePicturePanel;
    private Employee employeeObj;
    private String type = new String();
    private GenderModel genderModel = new GenderModel();
    private RaceModel raceModel = new RaceModel();

    /**
     * Creates new form Employee_Information
     */
    public Employee_Information(xEmployeeEdit main) {
        myDateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        employeeTypeHash = new Hashtable<EmployeeType, JRadioButton>();
        employeePicturePanel = new PicturePanel(this);
        myDateFormat.setLenient(false);

        mainForm = main;
        RecordSetNumber = 0;
        initComponents();
        myThread = new loadStatsThread();
        myThread.start();
        requiredFields = new Vector();
        /*
         * add date fields
         */
        calHireDate = new JCalendarComboBox();
        calTermDate = new JCalendarComboBox();

        if (Main_Window.parentOfApplication.loginType.equalsIgnoreCase("NewEmployee")) {
            calHireDate.setEnabled(false);
            calHireDate.setVisible(false);
        } else {
            calHireDate.setCalendar(Calendar.getInstance());
        }

        calTermDate.setEnabled(false);
        calTermDate.setVisible(false);

        jpHireDate.setBorder(BorderFactory.createEmptyBorder());
        jpTermDate.setBorder(BorderFactory.createEmptyBorder());

        jpHireDate.add(calHireDate, BorderLayout.CENTER);
        jpTermDate.add(calTermDate, BorderLayout.CENTER);
        //pbNew.setVisible(false);

        allowAllChk.setIcon(Main_Window.Client_Email_AddressBook_Finished_16x16_px);
        allowSomeChk.setIcon(Main_Window.Ok_Yellow);
        allowNoneLabel.setIcon(Main_Window.Problemsolver_Email_Dialog_Exit_16x16_px);

        myComponents = new Vector();
        myComponents.add(efAddress_1);
        myComponents.add(efAddress_2);
        myComponents.add(efCity);
        myComponents.add(efEMail);
        myComponents.add(efFirstName);
        myComponents.add(efLastName);
        myComponents.add(efMiddleName);

        myComponents.add(efPhone_1);
        myComponents.add(efCellPhone);
        myComponents.add(efSSN);

        myComponents.add(efState);
        myComponents.add(efZip);

        //Set up what fields are required...
        requiredFields.add(efFirstName);
        requiredFields.add(efLastName);
        requiredFields.add(efSSN);
        requiredFields.add(dob);

        returnColor();

        this.imagePanel.add(employeePicturePanel);
        this.fchooser = new JFileChooser();
        this.fchooser.setDialogTitle("Select an image for this employee");
        this.fchooser.setAcceptAllFileFilterUsed(false);
        this.fchooser.setMultiSelectionEnabled(false);
        this.fchooser.setAccessory(new ImagePreview(this.fchooser));
        this.fchooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                if (f.getName().toLowerCase().endsWith(".jpeg") || f.getName().toLowerCase().endsWith(".jpg")) {
                    return true;
                }
                return false;
            }

            public String getDescription() {
                return "Image (JPEG, JPG)";
            }
        });

        //Shut off access via code for employee login, move to db later.
        if (Main_Window.isEmployeeLoggedIn()) {
            efSSN.setEditable(false);
            efFirstName.setEditable(false);
            efLastName.setEditable(false);
            efMiddleName.setEditable(false);
            calHireDate.setEnabled(false);
            calTermDate.setEnabled(false);
            dob.setEditable(false);

            socialLabel.setVisible(false);
            efSSN.setVisible(false);

            markInvisibleChk.setVisible(false);
        }

        if (main.empSelfAdd) {
            type = "2";
        } else {
            type = "0";
        }

        //this.additionalDataPanel.setVisible(false);
    }

    public void doOnClear() {

        try {
            if (((StringOptionClass) Main_Window.newOptions.getOptionByName("lssnlabel")).read() != null) {
                socialLabel.setText(((StringOptionClass) Main_Window.newOptions.getOptionByName("lssnlabel")).read().toString());
            }
        } catch (Exception e) {
        }
        returnColor();

        this.homeContact.setIcon(Main_Window.Client_Email_AddressBook_Finished_16x16_px);
        this.cellContact.setIcon(Main_Window.Client_Email_AddressBook_Finished_16x16_px);
        this.altPhoneContact.setIcon(Main_Window.Client_Email_AddressBook_Finished_16x16_px);
        this.emailContact.setIcon(Main_Window.Client_Email_AddressBook_Finished_16x16_px);
        this.altEmailContact.setIcon(Main_Window.Client_Email_AddressBook_Finished_16x16_px);

        genderBox.setSelectedItem("Not Set");
        raceBox.setSelectedItem("Not Set");

        fullTimeCombo.setSelectedIndex(0);
        calHireDate.setCalendar(Calendar.getInstance());
        calTermDate.setEnabled(false);
        calTermDate.setVisible(false);
        markInvisibleChk.setSelected(false);
        efSSN.setText("");
        dob.setValue("");
        if (efPhone_1 instanceof JFormattedTextField) {
            ((JFormattedTextField) efPhone_1).setValue("");
        } else {
            efPhone_1.setText("");
        }
        if (efCellPhone instanceof JFormattedTextField) {
            ((JFormattedTextField) efCellPhone).setValue("");
        } else {
            efCellPhone.setText("");
        }
        if (efAltPhone instanceof JFormattedTextField) {
            ((JFormattedTextField) efAltPhone).setValue("");
        } else {
            efAltPhone.setText("");
        }
        if (efAlt_Emergency instanceof JFormattedTextField) {
            ((JFormattedTextField) efAlt_Emergency).setValue("");
        } else {
            efAlt_Emergency.setText("");
        }
        targetHoursTxt.setText("");
        vacationHoursTxt.setText("");
        checkFieldsAndSetCancelButton();
    }

    @Override
    public String checkData() {
        String socialSecurity = efSSN.getText();
        Record_Set checkrs = new Record_Set();
        employee_check_if_ssn_exists_query myCheckQuery = new employee_check_if_ssn_exists_query();
        if (socialSecurity.contains("-") || socialSecurity.contains(" ")) {
            StringTokenizer ssn = new StringTokenizer(socialSecurity, " -");
            socialSecurity = "";
            while (ssn.hasMoreTokens()) {
                socialSecurity = socialSecurity + ssn.nextToken();
            }
            if (socialSecurity.contains("_")) {
                return "A valid social security number must be entered!";
            }
        }
        if (!checkFields()) {
            return "A required field or fields are blank!";
        }
        myCheckQuery.update(socialSecurity);
        try {
            checkrs = (mainForm.getConnection().executeQuery(myCheckQuery));
            if (checkrs.length() > 0 && myparent.getSelectedObject() == null) {
                return "Social Security has already been entered for employee " + checkrs.getString("fname") + " " + checkrs.getString("lname");
            }
        } catch (Exception e) {
        }
        try {
            SimpleDateFormat birthFormat = new SimpleDateFormat("MM/dd/yyyy");
            birthFormat.parse(dob.getText());
        } catch (Exception e) {
            return "You must enter a valid birthdate!";
        }
        try {
            if (targetHoursTxt.getText().trim().length() > 0) {
                Integer.parseInt(targetHoursTxt.getText());
            }
        } catch (Exception e) {
            return "The data for target hours worked is invalid, please specify a number!";
        }
        try {
            if (vacationHoursTxt.getText().trim().length() > 0) {
                Integer.parseInt(vacationHoursTxt.getText());
            }
        } catch (Exception e) {
            return "The data for vacation is invalid, please specify a number!";
        }
        return null;
    }

    private String parseOutSocialSecurity() {
        String socialSecurity = efSSN.getText();
        StringTokenizer ssn = new StringTokenizer(socialSecurity, " -");
        socialSecurity = "";
        while (ssn.hasMoreTokens()) {
            socialSecurity = socialSecurity + ssn.nextToken();
        }
        return socialSecurity;
    }

    public GeneralQueryFormat getSaveQuery(boolean val) {
        Date termdate = null;
        Date hiredate = null;
        boolean isDeleted = false;
        if (calTermDate.isVisible()) {
            termdate = calTermDate.getCalendar().getTime();
            isDeleted = true;
        }
        if (calHireDate.isVisible()) {
            hiredate = calHireDate.getCalendar().getTime();
        }
        String socialSecurity = parseOutSocialSecurity();
        Integer vacationHours = new Integer(0);
        Integer targetHours = new Integer(40);
        try {
            vacationHours = Integer.parseInt(vacationHoursTxt.getText());
        } catch (Exception e) {
        }
        try {
            targetHours = Integer.parseInt(targetHoursTxt.getText());
        } catch (Exception e) {
        }

        Employee currentEmployee = (Employee) myparent.getSelectedObject();
        if (currentEmployee == null) {
            currentEmployee = new Employee();
        }
        try {
            currentEmployee.setBranchId(Integer.parseInt(((xEmployeeEdit) myparent).getBranchFromChild()));
        } catch (Exception e) {
            currentEmployee.setBranchId(Integer.parseInt(myparent.getConnection().myBranch));
        }
        currentEmployee.setEmployeeTermDate(termdate);
        currentEmployee.setEmployeeHireDate(hiredate);
        if (!socialSecurity.contains("X")) {
            currentEmployee.setEmployeeSsn(socialSecurity);
        }
        currentEmployee.setEmployeePhone(efPhone_1.getText().trim());
        currentEmployee.setEmployeeCell(efCellPhone.getText().trim());
        currentEmployee.setEmployeePhone2(efAltPhone.getText().trim());
        currentEmployee.setEmployeeEmergency(efAlt_Emergency.getText().trim());
        currentEmployee.setFullTime(fullTimeCombo.getSelectedIndex() == 0);
        try {
            SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
            currentEmployee.setEmployeeBirthdate(myFormat.parse(dob.getText()));
        } catch (Exception e) {
        }
        currentEmployee.setSmsMessagingBoolean(eFSMS.getSelectedIndex() == 1);
        currentEmployee.setEmailMessaging("None");
        currentEmployee.setEmployeeFirstName(efFirstName.getText().trim());
        currentEmployee.setEmployeeLastName(efLastName.getText().trim());
        currentEmployee.setEmployeeMiddleInitial(efMiddleName.getText().trim());
        currentEmployee.setEmployeeAddress(efAddress_1.getText().trim());
        currentEmployee.setEmployeeAddress2(efAddress_2.getText().trim());
        currentEmployee.setEmployeeCity(efCity.getText().trim());
        currentEmployee.setEmployeeState(efState.getText().trim());
        currentEmployee.setEmployeeZip(efZip.getText().trim());
        currentEmployee.setEmployeeEmail(efEMail.getText());
        currentEmployee.setEmployeeEmail2(efAlt_Email.getText());
        currentEmployee.setEmployeeType(Integer.parseInt(type));
        currentEmployee.setEmployeeWorkhrsPweek(targetHours);
        currentEmployee.setAccruedVacation(vacationHours);
        try {
            currentEmployee.setGender(genderModel.getSelected());
            currentEmployee.setRace(raceModel.getSelected());
        } catch (Exception e) {
        }
        currentEmployee.setEmployeeIsDeletedShort(isDeleted ? 1 : 0);
        currentEmployee.setMarkInvisible(markInvisibleChk.isSelected());

        if (homeContact.getIcon().equals(Main_Window.Client_Email_AddressBook_Finished_16x16_px)) {
            currentEmployee.setPhoneContact(1);
        } else if (homeContact.getIcon().equals(Main_Window.Ok_Yellow)) {
            currentEmployee.setPhoneContact(2);
        } else {
            currentEmployee.setPhoneContact(0);
        }
        if (cellContact.getIcon().equals(Main_Window.Client_Email_AddressBook_Finished_16x16_px)) {
            currentEmployee.setCellContact(1);
        } else if (cellContact.getIcon().equals(Main_Window.Ok_Yellow)) {
            currentEmployee.setCellContact(2);
        } else {
            currentEmployee.setCellContact(0);
        }
        if (altPhoneContact.getIcon().equals(Main_Window.Client_Email_AddressBook_Finished_16x16_px)) {
            currentEmployee.setAltPhoneContact(1);
        } else if (altPhoneContact.getIcon().equals(Main_Window.Ok_Yellow)) {
            currentEmployee.setAltPhoneContact(2);
        } else {
            currentEmployee.setAltPhoneContact(0);
        }
        if (emailContact.getIcon().equals(Main_Window.Client_Email_AddressBook_Finished_16x16_px)) {
            currentEmployee.setEmailContact(1);
        } else if (emailContact.getIcon().equals(Main_Window.Ok_Yellow)) {
            currentEmployee.setEmailContact(2);
        } else {
            currentEmployee.setEmailContact(0);
        }
        if (altEmailContact.getIcon().equals(Main_Window.Client_Email_AddressBook_Finished_16x16_px)) {
            currentEmployee.setAltEmailContact(1);
        } else if (altEmailContact.getIcon().equals(Main_Window.Ok_Yellow)) {
            currentEmployee.setAltEmailContact(2);
        } else {
            currentEmployee.setAltEmailContact(0);
        }

        try {
            EmployeeController empController = EmployeeController.getInstance(myparent.getConnection().myCompany);
            empController.saveEmployee(currentEmployee);
            myparent.setCurrentSelectedObjectDirectly(currentEmployee);

            employee_type_save_query empTypeQuery = new employee_type_save_query();
            Enumeration<EmployeeType> keys = this.employeeTypeHash.keys();
            ArrayList<Integer> selectedTypes = new ArrayList<Integer>();
            while (keys.hasMoreElements()) {
                EmployeeType empType = keys.nextElement();
                JRadioButton jChk = this.employeeTypeHash.get(empType);
                if (jChk.isSelected()) {
                    selectedTypes.add(empType.getEmployeeTypeId());
                }
            }
            try {
                empTypeQuery.update(Integer.parseInt(myparent.getMyIdForSave()), selectedTypes);
                myparent.getConnection().executeUpdate(empTypeQuery);
            } catch (Exception e) {
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Problem saving Employee!");
            return null;
        }

        return new GenericQuery("Select NOW();");
    }

    public boolean shouldDisplayAdditionalDataInTab() {
        get_dynamic_field_values_for_key_query myQuery = new get_dynamic_field_values_for_key_query();
        try {
            myQuery.update(Integer.parseInt(myparent.getMyIdForSave()), 1, false, false);
        } catch (Exception e) {
        }
        Record_Set rst = this.myparent.getConnection().executeQuery(myQuery);
        if (rst.length() > 0 && rst.length() < 20) {
            return true;
        }
        return false;
    }

    public GeneralQueryFormat getQuery(boolean val) {
        employee_list_types_query employeeListType = new employee_list_types_query();
        grab_employee_target_hours_query emp_hours_query = new grab_employee_target_hours_query();
        RunQueriesEx myCompleteQuery = new RunQueriesEx();
        emp_hours_query.update(myparent.getMyIdForSave());
        try {
            employeeListType.update(Integer.parseInt(myparent.getMyIdForSave()));
        } catch (Exception e) {
        }

        try {
            EmployeeController employeeController = EmployeeController.getInstance(super.myparent.getConnection().myCompany);
            employeeObj = employeeController.getEmployeeById(Integer.parseInt(myparent.getMyIdForSave()));
            loadEmployeeData();
        } catch (Exception exe) {
            exe.printStackTrace();
        }

        myCompleteQuery.add(employeeListType);
        myCompleteQuery.add(emp_hours_query);
        return myCompleteQuery;
    }

    private void loadEmployeeData() {
        efFirstName.setText(employeeObj.getEmployeeFirstName());
        efLastName.setText(employeeObj.getEmployeeLastName());
        efMiddleName.setText(employeeObj.getEmployeeMiddleInitial());
        efAddress_1.setText(employeeObj.getEmployeeAddress());
        efAddress_2.setText(employeeObj.getEmployeeAddress2());
        efPhone_1.setText(employeeObj.getEmployeePhone());
        efCellPhone.setText(employeeObj.getEmployeeCell());
        efAltPhone.setText(employeeObj.getEmployeePhone2());
        efCity.setText(employeeObj.getEmployeeCity());
        efState.setText(employeeObj.getEmployeeState());
        efZip.setText(employeeObj.getEmployeeZip());

        if (Main_Window.parentOfApplication.getUser().getCanViewSsn()) {
            efSSN.setText(employeeObj.getEmployeeSsn());
        } else {
            try {
                efSSN.setText("XXX-XX-" + employeeObj.getEmployeeSsn().substring(employeeObj.getEmployeeSsn().length() - 4));
            } catch (Exception exe) {
            }
        }
        efEMail.setText(employeeObj.getEmployeeEmail());
        efAlt_Email.setText(employeeObj.getEmployeeEmail2());
        try {
            efAlt_Emergency.setText(employeeObj.getEmployeeEmergency());
        } catch (Exception exe) {
            efAlt_Emergency.setText("");
        }
        try {
            fullTimeCombo.setSelectedIndex(employeeObj.getFullTime() ? 0 : 1);
        } catch (Exception exe) {
        }
        try {
            markInvisibleChk.setSelected(employeeObj.getMarkInvisible());
        } catch (Exception e) {
        }

        if (employeeObj.getSmsMessaging() != null && employeeObj.getSmsMessaging().booleanValue()) {
            eFSMS.setSelectedIndex(1);
        } else {
            eFSMS.setSelectedIndex(0);
        }

        genderModel.setItem(employeeObj.getGender());
        raceModel.setItem(employeeObj.getRace());

        if (employeeObj.getEmailMessaging().matches("Primary")) {
            eFEmail.setSelectedIndex(1);
        } else if (employeeObj.getEmailMessaging().matches("Alternate")) {
            eFEmail.setSelectedIndex(2);
        } else {
            eFEmail.setSelectedIndex(0);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        if (employeeObj.getEmployeeBirthdate() == null
                || formatter.format(employeeObj.getEmployeeBirthdate()).equals("01/01/1000")) {
            dob.setText("");
        } else {
            dob.setText(formatter.format(employeeObj.getEmployeeBirthdate()));
        }

        try {
            Calendar myCal = Calendar.getInstance();
            if (!(employeeObj.getEmployeeHireDateStr()).equals("10/10/2100")) {
                myCal.setTime(employeeObj.getEmployeeHireDate());
            }
            calHireDate.setCalendar(myCal);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (employeeObj.getEmployeeTermDate() == null
                || formatter.format(employeeObj.getEmployeeTermDate()).equals("1000-10-10")
                || formatter.format(employeeObj.getEmployeeTermDate()).equals("0000-00-00")) {
            calTermDate.setVisible(false);
        } else {
            calTermDate.setEnabled(true);
            calTermDate.setVisible(true);
            try {
                Calendar myCal = Calendar.getInstance();
                myCal.setTime(employeeObj.getEmployeeTermDate());
                calTermDate.setCalendar(myCal);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (employeeObj.getEmployeeIsDeleted() != 1) {
            calTermDate.setVisible(false);
        }

        try {
            if (employeeObj.getPhoneContact().equals(1)) {
                this.homeContact.setIcon(Main_Window.Client_Email_AddressBook_Finished_16x16_px);
            } else if (employeeObj.getPhoneContact().equals(2)) {
                this.homeContact.setIcon(Main_Window.Ok_Yellow);
            } else {
                this.homeContact.setIcon(Main_Window.Problemsolver_Email_Dialog_Exit_16x16_px);
            }
        } catch (Exception exe) {
            this.homeContact.setIcon(Main_Window.Client_Email_AddressBook_Finished_16x16_px);
        }
        try {
            if (employeeObj.getCellContact().equals(1)) {
                this.cellContact.setIcon(Main_Window.Client_Email_AddressBook_Finished_16x16_px);
            } else if (employeeObj.getCellContact().equals(2)) {
                this.cellContact.setIcon(Main_Window.Ok_Yellow);
            } else {
                this.cellContact.setIcon(Main_Window.Problemsolver_Email_Dialog_Exit_16x16_px);
            }
        } catch (Exception exe) {
            this.cellContact.setIcon(Main_Window.Client_Email_AddressBook_Finished_16x16_px);
        }
        try {
            if (employeeObj.getAltPhoneContact().equals(1)) {
                this.altPhoneContact.setIcon(Main_Window.Client_Email_AddressBook_Finished_16x16_px);
            } else if (employeeObj.getPhoneContact().equals(2)) {
                this.altPhoneContact.setIcon(Main_Window.Ok_Yellow);
            } else {
                this.altPhoneContact.setIcon(Main_Window.Problemsolver_Email_Dialog_Exit_16x16_px);
            }
        } catch (Exception exe) {
            this.altPhoneContact.setIcon(Main_Window.Client_Email_AddressBook_Finished_16x16_px);
        }
        try {
            if (employeeObj.getEmailContact().equals(1)) {
                this.emailContact.setIcon(Main_Window.Client_Email_AddressBook_Finished_16x16_px);
            } else if (employeeObj.getPhoneContact().equals(2)) {
                this.emailContact.setIcon(Main_Window.Ok_Yellow);
            } else {
                this.emailContact.setIcon(Main_Window.Problemsolver_Email_Dialog_Exit_16x16_px);
            }
        } catch (Exception exe) {
            this.emailContact.setIcon(Main_Window.Client_Email_AddressBook_Finished_16x16_px);
        }
        try {
            if (employeeObj.getAltEmailContact().equals(1)) {
                this.altEmailContact.setIcon(Main_Window.Client_Email_AddressBook_Finished_16x16_px);
            } else if (employeeObj.getPhoneContact().equals(2)) {
                this.altEmailContact.setIcon(Main_Window.Ok_Yellow);
            } else {
                this.altEmailContact.setIcon(Main_Window.Problemsolver_Email_Dialog_Exit_16x16_px);
            }
        } catch (Exception exe) {
            this.altEmailContact.setIcon(Main_Window.Client_Email_AddressBook_Finished_16x16_px);
        }

        myThread.setEmployeeToLoad(myparent.getMyIdForSave());
        this.loadEmployeeImage();
        checkFieldsAndSetCancelButton();
    }

    public void loadData(Record_Set rs) {
        if (RecordSetNumber == 0) {
            checkBoxHolderPanel.removeAll();
            employeeTypeHash.clear();
            ButtonGroup buttonGroup = new ButtonGroup();
            if (rs.length() > 0) {
                employeeOptions.setVisible(true);
                do {
                    EmployeeType employeeType = new EmployeeType();
                    employeeType.setEmployeeTypeId(rs.getInt("employee_type_id"));
                    employeeType.setEmployeeType(rs.getString("employee_type"));

                    JRadioButton tempEmpTypeChk = new JRadioButton(employeeType.getEmployeeType());
                    if (rs.getString("employee_type_sel").equals("t")) {
                        tempEmpTypeChk.setSelected(true);
                    }
                    if (Main_Window.parentOfApplication.loginType.equalsIgnoreCase("employee")) {
                        tempEmpTypeChk.setEnabled(false);
                    }
                    checkBoxHolderPanel.add(tempEmpTypeChk);
                    buttonGroup.add(tempEmpTypeChk);
                    employeeTypeHash.put(employeeType, tempEmpTypeChk);
                } while (rs.moveNext());
                empTypesScrollPane.validate();
            } else {
                employeeOptions.setVisible(false);
            }
        } else if (rs.length() > 0 && RecordSetNumber == 1) {
            targetHoursTxt.setText(rs.getString("employee_workhrs_pweek"));
            vacationHoursTxt.setText(rs.getString("accrued_vacation"));
        }

    }

    /**
     * Threaded loading of the employee image.
     */
    private void loadEmployeeImage() {
        Runnable fetchImage = new Runnable() {

            public void run() {
                try {
                    String currentCompanyId = myparent.getConnection().myCompany;
                    Company comp = Main_Window.getCompanyById(currentCompanyId);
                    final ImageIcon newIcon = employeeObj.getEmployeeImage(comp.getDB());
                    Runnable setImageRunnable = new Runnable() {

                        public void run() {
                            if (newIcon != null) {
                                employeePicturePanel.setImage(newIcon.getImage());
                            } else {
                                employeePicturePanel.setImage(null);
                            }
                        }
                    };
                    SwingUtilities.invokeAndWait(setImageRunnable);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Runnable clearImageRunnable = new Runnable() {

                        public void run() {
                            employeePicturePanel.setImage(null);
                        }
                    };
                    try {
                        SwingUtilities.invokeAndWait(clearImageRunnable);
                    } catch (Exception e) {
                    }
                }
            }
        };
        new Thread(fetchImage).start();
    }

    public String getMyTabTitle() {
        return "Info";
    }

    private void returnColor() {
        for (int i = 0; i < requiredFields.size(); i++) {
            requiredFields.get(i).setBackground(new Color(255, 255, 204));
        }
    }

    public void terminateEmployee(Calendar c) {

        employee_info_query myInfoQuery = new employee_info_query();
        employee_delete_query myDeleteQuery = new employee_delete_query();

        mainForm.getConnection().prepQuery(myInfoQuery);
        myInfoQuery.update(myparent.getMyIdForSave(), "");
        mainForm.getConnection().prepQuery(myDeleteQuery);
        try {
            Record_Set rs = mainForm.getConnection().executeQuery(myInfoQuery);
            myDeleteQuery.update(rs.getString("deleted"), myparent.getMyIdForSave(), other_functions.cal2Str(c));
            mainForm.getConnection().executeUpdate(myDeleteQuery);
        } catch (Exception e) {
            return;
        }

        clearData();
        mainForm.clearData();
    }

    public void terminateEmployee() {
    }

    public JPanel getMyForm() {
        return this;
    }

    public boolean needsMoreRecordSets() {
        RecordSetNumber++;
        if (RecordSetNumber <= 1) {
            return true;
        }
        RecordSetNumber = 0;
        return false;
    }

    public boolean checkFields() {
        boolean good = true;
        for (int i = 0; i < requiredFields.size(); i++) {
            if (!checkField(requiredFields.get(i))) {
                good = false;
            }
        }
        validate();
        return good;
    }

    public boolean checkField(JTextField jtf) {

        if (jtf.getText().trim().length() < 1) {
            try {
                jtf.requestFocus();
            } catch (Exception e) {
            }
            jtf.setBackground(Main_Window.redInputColor);
            return false;
        }

        return true;
    }

    /**
     * Checks all of the fields to see if they are null or not, reason why it
     * checks for 4 is that the social security field when trimmed returns the
     * string "- -" because of mask so that is actually a blank value...kinda
     * lame yes
     */
    public void checkFieldsAndSetCancelButton() {
        for (int i = 0; i < myComponents.size(); i++) {
            myComponents.get(i).getText().trim().length();
        }
    }

    public void editImage(PicturePanel panel) {
        if (fchooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                System.out.println("editImage method run.");
                Image image = ImageIO.read(fchooser.getSelectedFile());
                Image scaledImage = image.getScaledInstance(-1, this.employeePicturePanel.getHeight(), Image.SCALE_SMOOTH);
                if (scaledImage.getWidth(null) > this.employeePicturePanel.getWidth()) {
                    scaledImage = image.getScaledInstance(this.employeePicturePanel.getWidth(), -1, Image.SCALE_SMOOTH);
                }

                String empId = this.myparent.getMyIdForSave();
                try {
                    Integer.parseInt(empId);
                    ImageIcon newIcon = new ImageIcon(scaledImage);
                    ImageIcon nonScaledIcon = new ImageIcon(image);

                    String currentCompanyId = myparent.getConnection().myCompany;
                    Company comp = Main_Window.parentOfApplication.getCompanyById(currentCompanyId);

                    ImageLoader.saveImage(empId + ".jpg", comp.getDB(), "employee", newIcon);

                    ImageLoader.saveAdditionalImages(comp.getDB(), "additional_images",
                            "myImages", ".jpg", "_", empId, nonScaledIcon);

                    this.employeePicturePanel.setImage(newIcon.getImage());

                } catch (Exception ex) {
                    if (JOptionPane.showConfirmDialog(this, "You must save this employee before adding a picture.  Save Now?",
                            "Save Employee?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        this.myparent.saveData();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void addImage(PicturePanel panel) {
    }

    public void deleteImage(PicturePanel panel) {
        int result = JOptionPane.showConfirmDialog(Main_Window.parentOfApplication,
                "Do you want to delete this employees image?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            String empId = this.myparent.getMyIdForSave();
            String currentCompanyId = myparent.getConnection().myCompany;
            Company comp = Main_Window.parentOfApplication.getCompanyById(currentCompanyId);
            ImageLoader.removeImage(empId + ".jpg", comp.getDB(), "employee", "", "", "");
            loadEmployeeImage();
        }
    }

    public void downloadImage(PicturePanel panel) {
        PicturePanelUtilities download = new PicturePanelUtilities.PicturePanelBuilder().image(panel.getImage()).operation(PicturePanelUtilities.Operation.DOWNLOAD).build();
        download.start();
    }

    public boolean userHasAccess() {
        if (Main_Window.parentOfApplication.loginType.equalsIgnoreCase("NewEmployee")) {
            return (true);
        }

        return Main_Window.parentOfApplication.checkSecurity(security_detail.MODULES.EMPLOYEE_EDIT)
                || Main_Window.isEmployeeLoggedIn();
    }

    /**
     * Class used to load employees employment statistics rather than bog
     * everything down
     */
    private class loadStatsThread extends Thread {

        private String nextEmployeeToLoad;

        public loadStatsThread() {
            nextEmployeeToLoad = "";
        }

        public void setEmployeeToLoad(String id) {
            nextEmployeeToLoad = id;
        }

        public void run() {
            while (true) {
                if (nextEmployeeToLoad.trim().length() > 0) {
                    Calendar myCal = Calendar.getInstance();
                    myCal.add(Calendar.DAY_OF_YEAR, -1);
                    employee_stats_query myStats = new employee_stats_query();
                    myStats.update(nextEmployeeToLoad, StaticDateTimeFunctions.convertCalendarToDatabaseFormat(myCal));
                    myparent.getConnection().prepQuery(myStats);
                    RunQueriesEx myQuery = new RunQueriesEx();
                    ArrayList myData = new ArrayList();

                    myQuery.add(myStats);
                    myparent.getConnection().prepQuery(myQuery);
                    try {
                        myData = myparent.getConnection().executeQueryEx(myQuery);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Record_Set rs = ((Record_Set) myData.get(0));
                    employeeIdLabel.setText(nextEmployeeToLoad + "");
                    nextEmployeeToLoad = "";
                    try {
                        FirstDayWorked.setText(rs.getString("first_date"));
                    } catch (Exception e) {
                        FirstDayWorked.setText("Has Not Worked");
                    }
                    try {
                        LastDayWorked.setText(rs.getString("last_date"));
                    } catch (Exception e) {
                        LastDayWorked.setText("Has Not Worked");
                    }
                    try {
                        TotalHoursWorked.setText((Integer.parseInt(rs.getString("totalminutes")) / 60) + "");
                    } catch (Exception e) {
                        TotalHoursWorked.setText("Has Not Worked");
                    }
                    try {
                        AverageHours.setText((Integer.parseInt(rs.getString("average")) / 60) + "");
                    } catch (Exception e) {
                        AverageHours.setText("Has Not Worked");
                    }
                } else {
                    try {
                        sleep(100);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox2 = new javax.swing.JComboBox();
        eFEmail = new javax.swing.JComboBox();
        eFSMS = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        targetHoursTxt = new javax.swing.JTextField();
        vacationHoursTxt = new javax.swing.JTextField();
        jpEmployeeInfo = new javax.swing.JPanel();
        MainPanel1 = new javax.swing.JPanel();
        leftPanel = new javax.swing.JPanel();
        GeneralInformationPanel = new javax.swing.JPanel();
        generalInfoPanel = new javax.swing.JPanel();
        firstRowPanel = new javax.swing.JPanel();
        lblLastName = new javax.swing.JLabel();
        efLastName = new javax.swing.JTextField();
        lblMiddleName = new javax.swing.JLabel();
        efMiddleName = new javax.swing.JTextField();
        lblFirstName = new javax.swing.JLabel();
        efFirstName = new javax.swing.JTextField();
        secondRowPanel = new javax.swing.JPanel();
        socialLabel = new javax.swing.JLabel();
        efSSN = TextField.getSSNNewTextField();
        jLabel18 = new javax.swing.JLabel();
        try {     javax.swing.text.MaskFormatter myDOBFormatter = new javax.swing.text.MaskFormatter("##/##/####");myDOBFormatter.setPlaceholderCharacter('_');
            dob = new JFormattedTextField(myDOBFormatter);
        } catch (Exception e) {}
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        genderBox = new javax.swing.JComboBox();
        raceBox = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        ContactInfoPanel = new javax.swing.JPanel();
        AddressPanel = new javax.swing.JPanel();
        AddressStreetPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        efAddress_1 = new javax.swing.JTextField();
        AddressStreetPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        efAddress_2 = new javax.swing.JTextField();
        CityStatePanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        try {
            efCity = new javax.swing.JFormattedTextField();
        } catch (Exception e) {}
        jLabel3 = new javax.swing.JLabel();
        efState = TextField.getStateTextField()  ;
        jLabel4 = new javax.swing.JLabel();
        efZip = TextField.getZipTextField() ;
        jPanel4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        efEMail = new javax.swing.JTextField();
        booleanContactNamePanel2 = new javax.swing.JPanel();
        emailContact = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        legendPanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        booleanContactNamePanel3 = new javax.swing.JPanel();
        allowAllChk = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        booleanContactNamePanel4 = new javax.swing.JPanel();
        allowSomeChk = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        booleanContactNamePanel5 = new javax.swing.JPanel();
        allowNoneLabel = new javax.swing.JLabel();
        PhonePanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        efPhone_1 =  TextField.getPhoneTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        efCellPhone =  TextField.getPhoneTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        efAltPhone =  TextField.getPhoneTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        efAlt_Email = new javax.swing.JFormattedTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        efAlt_Emergency = new javax.swing.JFormattedTextField();
        booleanContactPanel = new javax.swing.JPanel();
        booleanContactNamePanel = new javax.swing.JPanel();
        homeContact = new javax.swing.JLabel();
        booleanContactEmailPanel = new javax.swing.JPanel();
        cellContact = new javax.swing.JLabel();
        booleanContactSMSPanel = new javax.swing.JPanel();
        altPhoneContact = new javax.swing.JLabel();
        booleanContactNamePanel1 = new javax.swing.JPanel();
        altEmailContact = new javax.swing.JLabel();
        blankSpace = new javax.swing.JPanel();
        rightPanel = new javax.swing.JPanel();
        rightSidePanel = new javax.swing.JPanel();
        imagePanel = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        employeeIdLabel = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        fullTimeCombo = new javax.swing.JComboBox();
        jPanel14 = new javax.swing.JPanel();
        bottomPanel = new javax.swing.JPanel();
        leftColumn = new javax.swing.JPanel();
        DatesEmployedPanel = new javax.swing.JPanel();
        jpHireDateContainer = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jpHireDate = new javax.swing.JPanel();
        jpTermDateContainer = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jpTermDate = new javax.swing.JPanel();
        employeeOptions = new javax.swing.JPanel();
        empTypesScrollPane = new javax.swing.JScrollPane(checkBoxHolderPanel);
        checkBoxHolderPanel = new javax.swing.JPanel();
        rightColumn = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        markInvisibleChk = new javax.swing.JCheckBox();
        jPanel20 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        FirstDayWorked = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        LastDayWorked = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        AverageHours = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        TotalHoursWorked = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        eFEmail.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Primary", "Alternate" }));
        eFEmail.setMaximumSize(new java.awt.Dimension(80, 10));
        eFEmail.setMinimumSize(new java.awt.Dimension(80, 10));
        eFEmail.setPreferredSize(new java.awt.Dimension(80, 10));

        eFSMS.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No", "Yes" }));
        eFSMS.setMaximumSize(new java.awt.Dimension(80, 10));
        eFSMS.setMinimumSize(new java.awt.Dimension(80, 10));
        eFSMS.setPreferredSize(new java.awt.Dimension(80, 10));

        jLabel24.setText("Vacation Hours Available");
        jLabel24.setMaximumSize(new java.awt.Dimension(220, 18));

        jLabel23.setText("Target Hours Per Period");
        jLabel23.setMaximumSize(new java.awt.Dimension(220, 18));

        targetHoursTxt.setMaximumSize(new java.awt.Dimension(70, 2147483647));

        vacationHoursTxt.setMaximumSize(new java.awt.Dimension(70, 2147483647));

        setBackground(new java.awt.Color(186, 186, 222));
        setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3), javax.swing.BorderFactory.createEtchedBorder()));
        setLayout(new java.awt.BorderLayout());

        jpEmployeeInfo.setMinimumSize(new java.awt.Dimension(0, 300));
        jpEmployeeInfo.setPreferredSize(new java.awt.Dimension(100, 360));
        jpEmployeeInfo.setLayout(new javax.swing.BoxLayout(jpEmployeeInfo, javax.swing.BoxLayout.Y_AXIS));

        MainPanel1.setMinimumSize(new java.awt.Dimension(582, 235));
        MainPanel1.setPreferredSize(new java.awt.Dimension(722, 235));
        MainPanel1.setLayout(new javax.swing.BoxLayout(MainPanel1, javax.swing.BoxLayout.LINE_AXIS));

        leftPanel.setMaximumSize(new java.awt.Dimension(2147483647, 2000));
        leftPanel.setMinimumSize(new java.awt.Dimension(432, 220));
        leftPanel.setPreferredSize(new java.awt.Dimension(572, 220));
        leftPanel.setLayout(new javax.swing.BoxLayout(leftPanel, javax.swing.BoxLayout.Y_AXIS));

        GeneralInformationPanel.setMaximumSize(new java.awt.Dimension(35055, 85));
        GeneralInformationPanel.setMinimumSize(new java.awt.Dimension(432, 85));
        GeneralInformationPanel.setPreferredSize(new java.awt.Dimension(572, 85));
        GeneralInformationPanel.setLayout(new javax.swing.BoxLayout(GeneralInformationPanel, javax.swing.BoxLayout.X_AXIS));

        generalInfoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "General Information"));
        generalInfoPanel.setMinimumSize(new java.awt.Dimension(162, 78));
        generalInfoPanel.setPreferredSize(new java.awt.Dimension(162, 78));
        generalInfoPanel.setLayout(new javax.swing.BoxLayout(generalInfoPanel, javax.swing.BoxLayout.Y_AXIS));

        firstRowPanel.setMaximumSize(new java.awt.Dimension(200000, 28));
        firstRowPanel.setMinimumSize(new java.awt.Dimension(150, 28));
        firstRowPanel.setPreferredSize(new java.awt.Dimension(150, 28));
        firstRowPanel.setLayout(new javax.swing.BoxLayout(firstRowPanel, javax.swing.BoxLayout.X_AXIS));

        lblLastName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblLastName.setText("Last Name");
        lblLastName.setMaximumSize(new java.awt.Dimension(70, 19));
        lblLastName.setMinimumSize(new java.awt.Dimension(70, 18));
        lblLastName.setPreferredSize(new java.awt.Dimension(70, 18));
        firstRowPanel.add(lblLastName);

        efLastName.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        efLastName.setMaximumSize(new java.awt.Dimension(100, 22));
        efLastName.setMinimumSize(new java.awt.Dimension(100, 22));
        efLastName.setNextFocusableComponent(efMiddleName);
        efLastName.setPreferredSize(new java.awt.Dimension(100, 22));
        efLastName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                efLastNameKeyTyped(evt);
            }
        });
        firstRowPanel.add(efLastName);

        lblMiddleName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblMiddleName.setText("M I");
        lblMiddleName.setMaximumSize(new java.awt.Dimension(30, 14));
        lblMiddleName.setMinimumSize(new java.awt.Dimension(30, 14));
        lblMiddleName.setPreferredSize(new java.awt.Dimension(30, 14));
        firstRowPanel.add(lblMiddleName);

        efMiddleName.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        efMiddleName.setMaximumSize(new java.awt.Dimension(25, 22));
        efMiddleName.setMinimumSize(new java.awt.Dimension(25, 22));
        efMiddleName.setNextFocusableComponent(efFirstName);
        efMiddleName.setPreferredSize(new java.awt.Dimension(25, 22));
        efMiddleName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                efMiddleNameKeyTyped(evt);
            }
        });
        firstRowPanel.add(efMiddleName);

        lblFirstName.setText("First Name");
        lblFirstName.setMaximumSize(new java.awt.Dimension(70, 19));
        lblFirstName.setMinimumSize(new java.awt.Dimension(70, 18));
        lblFirstName.setPreferredSize(new java.awt.Dimension(70, 18));
        firstRowPanel.add(lblFirstName);

        efFirstName.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        efFirstName.setMaximumSize(new java.awt.Dimension(100, 22));
        efFirstName.setMinimumSize(new java.awt.Dimension(100, 22));
        efFirstName.setNextFocusableComponent(efSSN);
        efFirstName.setPreferredSize(new java.awt.Dimension(100, 22));
        efFirstName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                efFirstNameKeyTyped(evt);
            }
        });
        firstRowPanel.add(efFirstName);

        generalInfoPanel.add(firstRowPanel);

        secondRowPanel.setMaximumSize(new java.awt.Dimension(20000, 26));
        secondRowPanel.setMinimumSize(new java.awt.Dimension(150, 26));
        secondRowPanel.setPreferredSize(new java.awt.Dimension(150, 26));
        secondRowPanel.setLayout(new javax.swing.BoxLayout(secondRowPanel, javax.swing.BoxLayout.LINE_AXIS));

        socialLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        socialLabel.setText("SS#");
        socialLabel.setMaximumSize(new java.awt.Dimension(70, 19));
        socialLabel.setMinimumSize(new java.awt.Dimension(70, 18));
        socialLabel.setPreferredSize(new java.awt.Dimension(70, 18));
        secondRowPanel.add(socialLabel);

        efSSN.setMaximumSize(new java.awt.Dimension(100, 22));
        efSSN.setMinimumSize(new java.awt.Dimension(100, 22));
        efSSN.setNextFocusableComponent(dob);
        efSSN.setPreferredSize(new java.awt.Dimension(100, 22));
        efSSN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                efSSNActionPerformed(evt);
            }
        });
        secondRowPanel.add(efSSN);

        jLabel18.setText("DOB");
        jLabel18.setMaximumSize(new java.awt.Dimension(30, 14));
        jLabel18.setMinimumSize(new java.awt.Dimension(30, 14));
        jLabel18.setPreferredSize(new java.awt.Dimension(30, 14));
        secondRowPanel.add(jLabel18);

        dob.setMaximumSize(new java.awt.Dimension(100, 22));
        dob.setMinimumSize(new java.awt.Dimension(100, 22));
        dob.setNextFocusableComponent(efEMail);
        dob.setPreferredSize(new java.awt.Dimension(100, 22));
        dob.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dobActionPerformed(evt);
            }
        });
        dob.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                dobFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                dobFocusLost(evt);
            }
        });
        secondRowPanel.add(dob);
        secondRowPanel.add(jPanel6);

        generalInfoPanel.add(secondRowPanel);

        GeneralInformationPanel.add(generalInfoPanel);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Demographics"));
        jPanel3.setMaximumSize(new java.awt.Dimension(120, 65564));
        jPanel3.setMinimumSize(new java.awt.Dimension(115, 82));
        jPanel3.setPreferredSize(new java.awt.Dimension(115, 100));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));

        genderBox.setModel(genderModel);
        jPanel3.add(genderBox);

        raceBox.setModel(raceModel);
        jPanel3.add(raceBox);

        GeneralInformationPanel.add(jPanel3);

        leftPanel.add(GeneralInformationPanel);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Contact Information"));
        jPanel5.setMaximumSize(new java.awt.Dimension(2147483647, 200));
        jPanel5.setMinimumSize(new java.awt.Dimension(172, 160));
        jPanel5.setPreferredSize(new java.awt.Dimension(172, 160));
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.Y_AXIS));

        ContactInfoPanel.setMaximumSize(new java.awt.Dimension(2147483647, 140));
        ContactInfoPanel.setMinimumSize(new java.awt.Dimension(160, 110));
        ContactInfoPanel.setPreferredSize(new java.awt.Dimension(160, 120));
        ContactInfoPanel.setLayout(new javax.swing.BoxLayout(ContactInfoPanel, javax.swing.BoxLayout.LINE_AXIS));

        AddressPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 8));
        AddressPanel.setMaximumSize(new java.awt.Dimension(300, 120));
        AddressPanel.setMinimumSize(new java.awt.Dimension(300, 120));
        AddressPanel.setPreferredSize(new java.awt.Dimension(300, 120));
        AddressPanel.setLayout(new java.awt.GridLayout(0, 1));

        AddressStreetPanel.setMaximumSize(new java.awt.Dimension(225, 225));
        AddressStreetPanel.setMinimumSize(new java.awt.Dimension(225, 19));
        AddressStreetPanel.setPreferredSize(new java.awt.Dimension(225, 19));
        AddressStreetPanel.setLayout(new javax.swing.BoxLayout(AddressStreetPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("Street 1");
        jLabel1.setMaximumSize(new java.awt.Dimension(50, 19));
        jLabel1.setMinimumSize(new java.awt.Dimension(50, 19));
        jLabel1.setPreferredSize(new java.awt.Dimension(50, 19));
        AddressStreetPanel.add(jLabel1);

        efAddress_1.setMaximumSize(new java.awt.Dimension(2000, 22));
        efAddress_1.setMinimumSize(new java.awt.Dimension(200, 22));
        efAddress_1.setPreferredSize(new java.awt.Dimension(200, 22));
        AddressStreetPanel.add(efAddress_1);

        AddressPanel.add(AddressStreetPanel);

        AddressStreetPanel2.setMaximumSize(new java.awt.Dimension(225, 225));
        AddressStreetPanel2.setMinimumSize(new java.awt.Dimension(225, 19));
        AddressStreetPanel2.setPreferredSize(new java.awt.Dimension(225, 19));
        AddressStreetPanel2.setLayout(new javax.swing.BoxLayout(AddressStreetPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jLabel8.setText("Street 2");
        jLabel8.setMaximumSize(new java.awt.Dimension(50, 19));
        jLabel8.setMinimumSize(new java.awt.Dimension(50, 19));
        jLabel8.setPreferredSize(new java.awt.Dimension(50, 19));
        AddressStreetPanel2.add(jLabel8);

        efAddress_2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        efAddress_2.setMaximumSize(new java.awt.Dimension(2000, 22));
        efAddress_2.setMinimumSize(new java.awt.Dimension(200, 22));
        efAddress_2.setNextFocusableComponent(efCity);
        efAddress_2.setPreferredSize(new java.awt.Dimension(200, 22));
        efAddress_2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                efAddress_2KeyTyped(evt);
            }
        });
        AddressStreetPanel2.add(efAddress_2);

        AddressPanel.add(AddressStreetPanel2);

        CityStatePanel.setMaximumSize(new java.awt.Dimension(225, 225));
        CityStatePanel.setMinimumSize(new java.awt.Dimension(225, 19));
        CityStatePanel.setPreferredSize(new java.awt.Dimension(225, 19));
        CityStatePanel.setLayout(new javax.swing.BoxLayout(CityStatePanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel2.setText("City");
        jLabel2.setMaximumSize(new java.awt.Dimension(50, 19));
        jLabel2.setMinimumSize(new java.awt.Dimension(50, 19));
        jLabel2.setPreferredSize(new java.awt.Dimension(50, 19));
        CityStatePanel.add(jLabel2);

        efCity.setMaximumSize(new java.awt.Dimension(120, 22));
        efCity.setMinimumSize(new java.awt.Dimension(70, 22));
        efCity.setPreferredSize(new java.awt.Dimension(88, 22));
        CityStatePanel.add(efCity);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("State");
        CityStatePanel.add(jLabel3);

        efState.setMaximumSize(new java.awt.Dimension(35, 22));
        efState.setMinimumSize(new java.awt.Dimension(30, 22));
        efState.setPreferredSize(new java.awt.Dimension(30, 22));
        CityStatePanel.add(efState);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Zip");
        CityStatePanel.add(jLabel4);

        efZip.setMaximumSize(new java.awt.Dimension(50, 22));
        efZip.setMinimumSize(new java.awt.Dimension(50, 22));
        efZip.setNextFocusableComponent(efPhone_1);
        efZip.setPreferredSize(new java.awt.Dimension(50, 22));
        CityStatePanel.add(efZip);

        AddressPanel.add(CityStatePanel);

        jPanel4.setMaximumSize(new java.awt.Dimension(225, 225));
        jPanel4.setMinimumSize(new java.awt.Dimension(225, 19));
        jPanel4.setPreferredSize(new java.awt.Dimension(225, 19));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("E-Mail");
        jLabel13.setMaximumSize(new java.awt.Dimension(50, 19));
        jLabel13.setMinimumSize(new java.awt.Dimension(50, 19));
        jLabel13.setOpaque(true);
        jLabel13.setPreferredSize(new java.awt.Dimension(50, 19));
        jPanel4.add(jLabel13);

        efEMail.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        efEMail.setMaximumSize(new java.awt.Dimension(2000, 22));
        efEMail.setMinimumSize(new java.awt.Dimension(60, 22));
        efEMail.setNextFocusableComponent(efAddress_1);
        efEMail.setPreferredSize(new java.awt.Dimension(150, 22));
        efEMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                efEMailActionPerformed(evt);
            }
        });
        efEMail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                efEMailKeyTyped(evt);
            }
        });
        jPanel4.add(efEMail);

        booleanContactNamePanel2.setMaximumSize(new java.awt.Dimension(23, 23));
        booleanContactNamePanel2.setMinimumSize(new java.awt.Dimension(23, 23));
        booleanContactNamePanel2.setPreferredSize(new java.awt.Dimension(23, 23));
        booleanContactNamePanel2.setLayout(new java.awt.GridLayout(1, 0));

        emailContact.setToolTipText("<html>\nToggles between different communication levels for Schedfox. <br/><br/>\n<ul>\n<li>Green checkboxes indicate the employee can be contacted with either our automated systems <br/>\n(sending schedules at each week etc...) or a manual contact, through the messaging menu. </li>\n<li>Yellow icons indicate that the employee will not get any schedule infomation each week, or any <br/>\nother automated contact, but a manual messaging action (to fill a shift etc...) will still work. </li>\n<li>Red icons mean customer will not be contacted with ANY system.</li>\n<ul>\n</html>");
        emailContact.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                emailContactMousePressed(evt);
            }
        });
        booleanContactNamePanel2.add(emailContact);

        jPanel4.add(booleanContactNamePanel2);

        AddressPanel.add(jPanel4);

        jPanel15.setLayout(new javax.swing.BoxLayout(jPanel15, javax.swing.BoxLayout.LINE_AXIS));

        legendPanel.setMaximumSize(new java.awt.Dimension(32767, 20));
        legendPanel.setMinimumSize(new java.awt.Dimension(10, 20));
        legendPanel.setPreferredSize(new java.awt.Dimension(100, 20));
        legendPanel.setLayout(new javax.swing.BoxLayout(legendPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel9.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabel9.setText("All contact");
        legendPanel.add(jLabel9);

        booleanContactNamePanel3.setMaximumSize(new java.awt.Dimension(23, 23));
        booleanContactNamePanel3.setMinimumSize(new java.awt.Dimension(23, 23));
        booleanContactNamePanel3.setPreferredSize(new java.awt.Dimension(23, 23));
        booleanContactNamePanel3.setLayout(new java.awt.GridLayout(1, 0));

        allowAllChk.setToolTipText("Allow Contact?");
        allowAllChk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                allowAllChkMousePressed(evt);
            }
        });
        booleanContactNamePanel3.add(allowAllChk);

        legendPanel.add(booleanContactNamePanel3);
        legendPanel.add(jPanel16);

        jLabel19.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabel19.setText("Only manual contact");
        legendPanel.add(jLabel19);

        booleanContactNamePanel4.setMaximumSize(new java.awt.Dimension(23, 23));
        booleanContactNamePanel4.setMinimumSize(new java.awt.Dimension(23, 23));
        booleanContactNamePanel4.setPreferredSize(new java.awt.Dimension(23, 23));
        booleanContactNamePanel4.setLayout(new java.awt.GridLayout(1, 0));

        allowSomeChk.setToolTipText("Allow Contact?");
        allowSomeChk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                allowSomeChkMousePressed(evt);
            }
        });
        booleanContactNamePanel4.add(allowSomeChk);

        legendPanel.add(booleanContactNamePanel4);
        legendPanel.add(jPanel17);

        jLabel20.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabel20.setText("No Contact");
        legendPanel.add(jLabel20);

        booleanContactNamePanel5.setMaximumSize(new java.awt.Dimension(23, 23));
        booleanContactNamePanel5.setMinimumSize(new java.awt.Dimension(23, 23));
        booleanContactNamePanel5.setPreferredSize(new java.awt.Dimension(23, 23));
        booleanContactNamePanel5.setLayout(new java.awt.GridLayout(1, 0));

        allowNoneLabel.setToolTipText("Allow Contact?");
        allowNoneLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                allowNoneLabelMousePressed(evt);
            }
        });
        booleanContactNamePanel5.add(allowNoneLabel);

        legendPanel.add(booleanContactNamePanel5);

        jPanel15.add(legendPanel);

        AddressPanel.add(jPanel15);

        ContactInfoPanel.add(AddressPanel);

        PhonePanel.setMaximumSize(new java.awt.Dimension(15500, 900));
        PhonePanel.setMinimumSize(new java.awt.Dimension(155, 76));
        PhonePanel.setPreferredSize(new java.awt.Dimension(155, 76));
        PhonePanel.setLayout(new java.awt.GridLayout(5, 1));

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("Home");
        jLabel12.setMaximumSize(new java.awt.Dimension(60, 19));
        jLabel12.setMinimumSize(new java.awt.Dimension(60, 19));
        jLabel12.setPreferredSize(new java.awt.Dimension(60, 19));
        jLabel12.setVerifyInputWhenFocusTarget(false);
        jPanel1.add(jLabel12);

        efPhone_1.setMaximumSize(new java.awt.Dimension(180, 22));
        efPhone_1.setMinimumSize(new java.awt.Dimension(85, 22));
        efPhone_1.setPreferredSize(new java.awt.Dimension(105, 22));
        efPhone_1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                efPhone_1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                efPhone_1FocusLost(evt);
            }
        });
        efPhone_1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                efPhone_1KeyTyped(evt);
            }
        });
        jPanel1.add(efPhone_1);

        PhonePanel.add(jPanel1);

        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.LINE_AXIS));

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("Cell");
        jLabel14.setMaximumSize(new java.awt.Dimension(60, 19));
        jLabel14.setMinimumSize(new java.awt.Dimension(60, 19));
        jLabel14.setPreferredSize(new java.awt.Dimension(60, 19));
        jLabel14.setVerifyInputWhenFocusTarget(false);
        jPanel8.add(jLabel14);

        efCellPhone.setMaximumSize(new java.awt.Dimension(180, 22));
        efCellPhone.setMinimumSize(new java.awt.Dimension(85, 22));
        efCellPhone.setPreferredSize(new java.awt.Dimension(105, 22));
        efCellPhone.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                efCellPhoneFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                efCellPhoneFocusLost(evt);
            }
        });
        efCellPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                efCellPhoneKeyTyped(evt);
            }
        });
        jPanel8.add(efCellPhone);

        PhonePanel.add(jPanel8);

        jPanel2.setMaximumSize(new java.awt.Dimension(75, 19));
        jPanel2.setMinimumSize(new java.awt.Dimension(75, 19));
        jPanel2.setPreferredSize(new java.awt.Dimension(75, 19));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("Alt #");
        jLabel11.setMaximumSize(new java.awt.Dimension(60, 19));
        jLabel11.setMinimumSize(new java.awt.Dimension(60, 19));
        jLabel11.setPreferredSize(new java.awt.Dimension(60, 19));
        jLabel11.setVerifyInputWhenFocusTarget(false);
        jPanel2.add(jLabel11);

        efAltPhone.setMaximumSize(new java.awt.Dimension(180, 22));
        efAltPhone.setMinimumSize(new java.awt.Dimension(85, 22));
        efAltPhone.setPreferredSize(new java.awt.Dimension(105, 22));
        efAltPhone.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                efAltPhoneFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                efAltPhoneFocusLost(evt);
            }
        });
        efAltPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                efAltPhoneKeyTyped(evt);
            }
        });
        jPanel2.add(efAltPhone);

        PhonePanel.add(jPanel2);

        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.LINE_AXIS));

        jLabel10.setText("Alt Email");
        jLabel10.setMaximumSize(new java.awt.Dimension(60, 19));
        jLabel10.setMinimumSize(new java.awt.Dimension(60, 19));
        jLabel10.setPreferredSize(new java.awt.Dimension(60, 19));
        jLabel10.setVerifyInputWhenFocusTarget(false);
        jPanel7.add(jLabel10);

        efAlt_Email.setMaximumSize(new java.awt.Dimension(180, 22));
        efAlt_Email.setMinimumSize(new java.awt.Dimension(85, 22));
        efAlt_Email.setPreferredSize(new java.awt.Dimension(105, 22));
        jPanel7.add(efAlt_Email);

        PhonePanel.add(jPanel7);

        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.LINE_AXIS));

        jLabel25.setText("Emrgcy");
        jLabel25.setMaximumSize(new java.awt.Dimension(60, 19));
        jLabel25.setMinimumSize(new java.awt.Dimension(60, 19));
        jLabel25.setPreferredSize(new java.awt.Dimension(60, 19));
        jLabel25.setVerifyInputWhenFocusTarget(false);
        jPanel11.add(jLabel25);

        efAlt_Emergency.setMaximumSize(new java.awt.Dimension(180, 22));
        efAlt_Emergency.setMinimumSize(new java.awt.Dimension(85, 22));
        efAlt_Emergency.setPreferredSize(new java.awt.Dimension(105, 22));
        jPanel11.add(efAlt_Emergency);

        PhonePanel.add(jPanel11);

        ContactInfoPanel.add(PhonePanel);

        booleanContactPanel.setMaximumSize(new java.awt.Dimension(20, 32767));
        booleanContactPanel.setMinimumSize(new java.awt.Dimension(20, 92));
        booleanContactPanel.setPreferredSize(new java.awt.Dimension(20, 92));
        booleanContactPanel.setLayout(new java.awt.GridLayout(5, 1));

        booleanContactNamePanel.setMaximumSize(new java.awt.Dimension(60, 23));
        booleanContactNamePanel.setMinimumSize(new java.awt.Dimension(40, 23));
        booleanContactNamePanel.setPreferredSize(new java.awt.Dimension(40, 23));
        booleanContactNamePanel.setLayout(new java.awt.GridLayout(1, 0));

        homeContact.setToolTipText("<html>\nToggles between different communication levels for Schedfox. <br/><br/>\n<ul>\n<li>Green checkboxes indicate the employee can be contacted with either our automated systems <br/>\n(sending schedules at each week etc...) or a manual contact, through the messaging menu. </li>\n<li>Yellow icons indicate that the employee will not get any schedule infomation each week, or any <br/>\nother automated contact, but a manual messaging action (to fill a shift etc...) will still work. </li>\n<li>Red icons mean customer will not be contacted with ANY system.</li>\n<ul>\n</html>");
        homeContact.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                homeContactMousePressed(evt);
            }
        });
        booleanContactNamePanel.add(homeContact);

        booleanContactPanel.add(booleanContactNamePanel);

        booleanContactEmailPanel.setMaximumSize(new java.awt.Dimension(95, 20));
        booleanContactEmailPanel.setLayout(new java.awt.GridLayout(1, 0));

        cellContact.setToolTipText("<html>\nToggles between different communication levels for Schedfox. <br/><br/>\n<ul>\n<li>Green checkboxes indicate the employee can be contacted with either our automated systems <br/>\n(sending schedules at each week etc...) or a manual contact, through the messaging menu. </li>\n<li>Yellow icons indicate that the employee will not get any schedule infomation each week, or any <br/>\nother automated contact, but a manual messaging action (to fill a shift etc...) will still work. </li>\n<li>Red icons mean customer will not be contacted with ANY system.</li>\n<ul>\n</html>");
        cellContact.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cellContactMousePressed(evt);
            }
        });
        booleanContactEmailPanel.add(cellContact);

        booleanContactPanel.add(booleanContactEmailPanel);

        booleanContactSMSPanel.setMaximumSize(new java.awt.Dimension(95, 20));
        booleanContactSMSPanel.setMinimumSize(new java.awt.Dimension(95, 20));
        booleanContactSMSPanel.setPreferredSize(new java.awt.Dimension(95, 20));
        booleanContactSMSPanel.setLayout(new java.awt.GridLayout(1, 0));

        altPhoneContact.setToolTipText("<html>\nToggles between different communication levels for Schedfox. <br/><br/>\n<ul>\n<li>Green checkboxes indicate the employee can be contacted with either our automated systems <br/>\n(sending schedules at each week etc...) or a manual contact, through the messaging menu. </li>\n<li>Yellow icons indicate that the employee will not get any schedule infomation each week, or any <br/>\nother automated contact, but a manual messaging action (to fill a shift etc...) will still work. </li>\n<li>Red icons mean customer will not be contacted with ANY system.</li>\n<ul>\n</html>");
        altPhoneContact.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                altPhoneContactMousePressed(evt);
            }
        });
        booleanContactSMSPanel.add(altPhoneContact);

        booleanContactPanel.add(booleanContactSMSPanel);

        booleanContactNamePanel1.setMaximumSize(new java.awt.Dimension(60, 23));
        booleanContactNamePanel1.setMinimumSize(new java.awt.Dimension(40, 23));
        booleanContactNamePanel1.setPreferredSize(new java.awt.Dimension(40, 23));
        booleanContactNamePanel1.setLayout(new java.awt.GridLayout(1, 0));

        altEmailContact.setToolTipText("<html>\nToggles between different communication levels for Schedfox. <br/><br/>\n<ul>\n<li>Green checkboxes indicate the employee can be contacted with either our automated systems <br/>\n(sending schedules at each week etc...) or a manual contact, through the messaging menu. </li>\n<li>Yellow icons indicate that the employee will not get any schedule infomation each week, or any <br/>\nother automated contact, but a manual messaging action (to fill a shift etc...) will still work. </li>\n<li>Red icons mean customer will not be contacted with ANY system.</li>\n<ul>\n</html>");
        altEmailContact.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                altEmailContactMousePressed(evt);
            }
        });
        booleanContactNamePanel1.add(altEmailContact);

        booleanContactPanel.add(booleanContactNamePanel1);
        booleanContactPanel.add(blankSpace);

        ContactInfoPanel.add(booleanContactPanel);

        jPanel5.add(ContactInfoPanel);

        leftPanel.add(jPanel5);

        MainPanel1.add(leftPanel);

        rightPanel.setMaximumSize(new java.awt.Dimension(150, 20000));
        rightPanel.setMinimumSize(new java.awt.Dimension(150, 130));
        rightPanel.setPreferredSize(new java.awt.Dimension(150, 130));
        rightPanel.setLayout(new java.awt.GridLayout(1, 0));

        rightSidePanel.setLayout(new javax.swing.BoxLayout(rightSidePanel, javax.swing.BoxLayout.Y_AXIS));

        imagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Employee Image"));
        imagePanel.setMaximumSize(new java.awt.Dimension(12000, 150));
        imagePanel.setMinimumSize(new java.awt.Dimension(12, 150));
        imagePanel.setPreferredSize(new java.awt.Dimension(12, 150));
        imagePanel.setLayout(new javax.swing.BoxLayout(imagePanel, javax.swing.BoxLayout.LINE_AXIS));
        rightSidePanel.add(imagePanel);

        jPanel12.setLayout(new javax.swing.BoxLayout(jPanel12, javax.swing.BoxLayout.LINE_AXIS));

        jLabel16.setText("Id Num:");
        jLabel16.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 3, 0, 0));
        jPanel12.add(jLabel16);

        employeeIdLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        employeeIdLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        employeeIdLabel.setMaximumSize(new java.awt.Dimension(2000, 22));
        jPanel12.add(employeeIdLabel);

        rightSidePanel.add(jPanel12);
        rightSidePanel.add(jPanel13);

        fullTimeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Fulltime", "Parttime" }));
        fullTimeCombo.setMaximumSize(new java.awt.Dimension(32767, 26));
        rightSidePanel.add(fullTimeCombo);
        rightSidePanel.add(jPanel14);

        rightPanel.add(rightSidePanel);

        MainPanel1.add(rightPanel);

        jpEmployeeInfo.add(MainPanel1);

        bottomPanel.setMinimumSize(new java.awt.Dimension(600, 100));
        bottomPanel.setPreferredSize(new java.awt.Dimension(600, 100));
        bottomPanel.setLayout(new java.awt.GridLayout(1, 0));

        leftColumn.setMaximumSize(new java.awt.Dimension(300, 32827));
        leftColumn.setLayout(new javax.swing.BoxLayout(leftColumn, javax.swing.BoxLayout.Y_AXIS));

        DatesEmployedPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        DatesEmployedPanel.setMaximumSize(new java.awt.Dimension(600, 600));
        DatesEmployedPanel.setMinimumSize(new java.awt.Dimension(100, 50));
        DatesEmployedPanel.setPreferredSize(new java.awt.Dimension(100, 60));
        DatesEmployedPanel.setLayout(new java.awt.GridLayout(1, 2));

        jpHireDateContainer.setMaximumSize(new java.awt.Dimension(150, 44));
        jpHireDateContainer.setLayout(new javax.swing.BoxLayout(jpHireDateContainer, javax.swing.BoxLayout.Y_AXIS));

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Hire Date");
        jLabel5.setAlignmentY(0.0F);
        jLabel5.setMaximumSize(new java.awt.Dimension(4800, 14));
        jpHireDateContainer.add(jLabel5);

        jpHireDate.setAlignmentX(0.0F);
        jpHireDate.setAlignmentY(0.0F);
        jpHireDate.setMaximumSize(new java.awt.Dimension(32767, 30));
        jpHireDate.setMinimumSize(new java.awt.Dimension(10, 20));
        jpHireDate.setNextFocusableComponent(jpTermDate);
        jpHireDate.setPreferredSize(new java.awt.Dimension(10, 20));
        jpHireDate.setLayout(new java.awt.GridLayout(1, 0));
        jpHireDateContainer.add(jpHireDate);

        DatesEmployedPanel.add(jpHireDateContainer);

        jpTermDateContainer.setMaximumSize(new java.awt.Dimension(150, 44));
        jpTermDateContainer.setLayout(new javax.swing.BoxLayout(jpTermDateContainer, javax.swing.BoxLayout.Y_AXIS));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Term Date");
        jLabel6.setAlignmentY(0.0F);
        jLabel6.setMaximumSize(new java.awt.Dimension(5300, 14));
        jpTermDateContainer.add(jLabel6);

        jpTermDate.setAlignmentX(0.0F);
        jpTermDate.setAlignmentY(0.0F);
        jpTermDate.setMaximumSize(new java.awt.Dimension(32767, 30));
        jpTermDate.setMinimumSize(new java.awt.Dimension(10, 20));
        jpTermDate.setPreferredSize(new java.awt.Dimension(10, 20));
        jpTermDate.setLayout(new java.awt.GridLayout(1, 0));
        jpTermDateContainer.add(jpTermDate);

        DatesEmployedPanel.add(jpTermDateContainer);

        leftColumn.add(DatesEmployedPanel);

        employeeOptions.setMaximumSize(new java.awt.Dimension(600, 100));
        employeeOptions.setMinimumSize(new java.awt.Dimension(300, 60));
        employeeOptions.setPreferredSize(new java.awt.Dimension(300, 100));
        employeeOptions.setLayout(new java.awt.GridLayout(1, 0));

        empTypesScrollPane.setBorder(null);
        empTypesScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        empTypesScrollPane.setMaximumSize(new java.awt.Dimension(290, 125));
        empTypesScrollPane.setMinimumSize(new java.awt.Dimension(290, 50));
        empTypesScrollPane.setPreferredSize(new java.awt.Dimension(290, 50));

        checkBoxHolderPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 10, 5));
        checkBoxHolderPanel.setMaximumSize(new java.awt.Dimension(280, 125));
        checkBoxHolderPanel.setMinimumSize(new java.awt.Dimension(280, 40));
        checkBoxHolderPanel.setPreferredSize(new java.awt.Dimension(280, 40));
        checkBoxHolderPanel.setLayout(new java.awt.GridLayout(0, 3));
        empTypesScrollPane.setViewportView(checkBoxHolderPanel);

        employeeOptions.add(empTypesScrollPane);

        leftColumn.add(employeeOptions);

        bottomPanel.add(leftColumn);

        rightColumn.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
        rightColumn.setLayout(new java.awt.GridLayout(1, 0));

        jPanel19.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jPanel19.setLayout(new javax.swing.BoxLayout(jPanel19, javax.swing.BoxLayout.Y_AXIS));

        jPanel28.setMaximumSize(new java.awt.Dimension(32767, 22));
        jPanel28.setMinimumSize(new java.awt.Dimension(10, 22));
        jPanel28.setPreferredSize(new java.awt.Dimension(10, 22));
        jPanel28.setLayout(new javax.swing.BoxLayout(jPanel28, javax.swing.BoxLayout.LINE_AXIS));

        markInvisibleChk.setText("Make Employee Invisible on Schedule");
        markInvisibleChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                markInvisibleChkActionPerformed(evt);
            }
        });
        jPanel28.add(markInvisibleChk);

        jPanel19.add(jPanel28);

        jPanel20.setMaximumSize(new java.awt.Dimension(32767, 22));
        jPanel20.setMinimumSize(new java.awt.Dimension(10, 22));
        jPanel20.setPreferredSize(new java.awt.Dimension(10, 22));
        jPanel20.setLayout(new javax.swing.BoxLayout(jPanel20, javax.swing.BoxLayout.LINE_AXIS));

        jLabel15.setText("First Day Worked");
        jPanel20.add(jLabel15);

        FirstDayWorked.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        FirstDayWorked.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        FirstDayWorked.setMaximumSize(new java.awt.Dimension(2000, 22));
        jPanel20.add(FirstDayWorked);

        jPanel19.add(jPanel20);

        jPanel22.setMaximumSize(new java.awt.Dimension(32767, 22));
        jPanel22.setMinimumSize(new java.awt.Dimension(10, 22));
        jPanel22.setPreferredSize(new java.awt.Dimension(10, 22));
        jPanel22.setLayout(new javax.swing.BoxLayout(jPanel22, javax.swing.BoxLayout.LINE_AXIS));

        jLabel17.setText("Last Day Worked");
        jPanel22.add(jLabel17);

        LastDayWorked.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        LastDayWorked.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        LastDayWorked.setMaximumSize(new java.awt.Dimension(2000, 22));
        jPanel22.add(LastDayWorked);

        jPanel19.add(jPanel22);

        jPanel29.setMaximumSize(new java.awt.Dimension(32767, 22));
        jPanel29.setMinimumSize(new java.awt.Dimension(10, 22));
        jPanel29.setPreferredSize(new java.awt.Dimension(10, 22));
        jPanel29.setLayout(new javax.swing.BoxLayout(jPanel29, javax.swing.BoxLayout.LINE_AXIS));

        jLabel21.setText("Average Hours");
        jPanel29.add(jLabel21);

        AverageHours.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        AverageHours.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        AverageHours.setMaximumSize(new java.awt.Dimension(2000, 22));
        jPanel29.add(AverageHours);

        jPanel19.add(jPanel29);

        jPanel30.setMaximumSize(new java.awt.Dimension(32767, 22));
        jPanel30.setMinimumSize(new java.awt.Dimension(10, 22));
        jPanel30.setPreferredSize(new java.awt.Dimension(10, 22));
        jPanel30.setLayout(new javax.swing.BoxLayout(jPanel30, javax.swing.BoxLayout.LINE_AXIS));

        jLabel22.setText("Total Hours");
        jPanel30.add(jLabel22);

        TotalHoursWorked.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        TotalHoursWorked.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        TotalHoursWorked.setMaximumSize(new java.awt.Dimension(2000, 22));
        jPanel30.add(TotalHoursWorked);

        jPanel19.add(jPanel30);

        rightColumn.add(jPanel19);

        bottomPanel.add(rightColumn);

        jpEmployeeInfo.add(bottomPanel);

        jPanel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 4, 0, 4));
        jPanel9.setMaximumSize(new java.awt.Dimension(32767, 19));
        jPanel9.setLayout(new java.awt.GridLayout(1, 0));

        jLabel7.setBackground(new java.awt.Color(255, 255, 204));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Highlighted fields are required.");
        jLabel7.setOpaque(true);
        jPanel9.add(jLabel7);

        jpEmployeeInfo.add(jPanel9);

        add(jpEmployeeInfo, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void dobFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dobFocusGained
        dob.setCaretPosition(0);
    }//GEN-LAST:event_dobFocusGained

    private void dobFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dobFocusLost
        try {

            if (myDateFormat.parse(dob.getText()) == null) {
                dob.setText("");
            }
        } catch (ParseException ex) {
            dob.setText("");
        }
    }//GEN-LAST:event_dobFocusLost

    private void efAddress_2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_efAddress_2KeyTyped
        other_functions.maxlength(efAddress_2, 50);
    }//GEN-LAST:event_efAddress_2KeyTyped

    private void efEMailKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_efEMailKeyTyped
        other_functions.maxlength(efEMail, 50);
    }//GEN-LAST:event_efEMailKeyTyped

    private void efFirstNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_efFirstNameKeyTyped
        other_functions.maxlength(efFirstName, 100);
    }//GEN-LAST:event_efFirstNameKeyTyped

    private void efMiddleNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_efMiddleNameKeyTyped
        other_functions.maxlength(efMiddleName, 100);
    }//GEN-LAST:event_efMiddleNameKeyTyped

    private void efLastNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_efLastNameKeyTyped
        other_functions.maxlength(efLastName, 100);
    }//GEN-LAST:event_efLastNameKeyTyped

    private void efEMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_efEMailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_efEMailActionPerformed

    private void dobActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dobActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dobActionPerformed

    private void efSSNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_efSSNActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_efSSNActionPerformed

    private void efAltPhoneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_efAltPhoneKeyTyped
    }//GEN-LAST:event_efAltPhoneKeyTyped

    private void efAltPhoneFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_efAltPhoneFocusLost
        if (efAltPhone instanceof JFormattedTextField) {
            return;
        }

        efAltPhone.setText(TextField.editPhoneField(efAltPhone.getText()));

    }//GEN-LAST:event_efAltPhoneFocusLost

    private void efPhone_1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_efPhone_1FocusLost
        if (efPhone_1 instanceof JFormattedTextField) {
            return;
        }

        efPhone_1.setText(TextField.editPhoneField(efPhone_1.getText()));
    }//GEN-LAST:event_efPhone_1FocusLost

    private void efCellPhoneFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_efCellPhoneFocusLost
        if (efCellPhone instanceof JFormattedTextField) {
            return;
        }

        efCellPhone.setText(TextField.editPhoneField(efCellPhone.getText()));
    }//GEN-LAST:event_efCellPhoneFocusLost

    private void efAltPhoneFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_efAltPhoneFocusGained
    }//GEN-LAST:event_efAltPhoneFocusGained

    private void efCellPhoneFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_efCellPhoneFocusGained
    }//GEN-LAST:event_efCellPhoneFocusGained

    private void efPhone_1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_efPhone_1FocusGained
    }//GEN-LAST:event_efPhone_1FocusGained

    private void efPhone_1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_efPhone_1KeyTyped
    }//GEN-LAST:event_efPhone_1KeyTyped

    private void efCellPhoneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_efCellPhoneKeyTyped
    }//GEN-LAST:event_efCellPhoneKeyTyped

    private void markInvisibleChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_markInvisibleChkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_markInvisibleChkActionPerformed

    private void homeContactMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeContactMousePressed
        if (homeContact.getIcon().equals(Main_Window.Client_Email_AddressBook_Finished_16x16_px)) {
            homeContact.setIcon(Main_Window.Ok_Yellow);
        } else if (homeContact.getIcon().equals(Main_Window.Ok_Yellow)) {
            homeContact.setIcon(Main_Window.Problemsolver_Email_Dialog_Exit_16x16_px);
        } else {
            homeContact.setIcon(Main_Window.Client_Email_AddressBook_Finished_16x16_px);
        }
    }//GEN-LAST:event_homeContactMousePressed

    private void cellContactMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cellContactMousePressed
        if (cellContact.getIcon().equals(Main_Window.Client_Email_AddressBook_Finished_16x16_px)) {
            cellContact.setIcon(Main_Window.Ok_Yellow);
        } else if (cellContact.getIcon().equals(Main_Window.Ok_Yellow)) {
            cellContact.setIcon(Main_Window.Problemsolver_Email_Dialog_Exit_16x16_px);
        } else {
            cellContact.setIcon(Main_Window.Client_Email_AddressBook_Finished_16x16_px);
        }
    }//GEN-LAST:event_cellContactMousePressed

    private void altPhoneContactMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_altPhoneContactMousePressed
        if (altPhoneContact.getIcon().equals(Main_Window.Client_Email_AddressBook_Finished_16x16_px)) {
            altPhoneContact.setIcon(Main_Window.Ok_Yellow);
        } else if (altPhoneContact.getIcon().equals(Main_Window.Ok_Yellow)) {
            altPhoneContact.setIcon(Main_Window.Problemsolver_Email_Dialog_Exit_16x16_px);
        } else {
            altPhoneContact.setIcon(Main_Window.Client_Email_AddressBook_Finished_16x16_px);
        }
    }//GEN-LAST:event_altPhoneContactMousePressed

    private void altEmailContactMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_altEmailContactMousePressed
        if (altEmailContact.getIcon().equals(Main_Window.Client_Email_AddressBook_Finished_16x16_px)) {
            altEmailContact.setIcon(Main_Window.Ok_Yellow);
        } else if (altEmailContact.getIcon().equals(Main_Window.Ok_Yellow)) {
            altEmailContact.setIcon(Main_Window.Problemsolver_Email_Dialog_Exit_16x16_px);
        } else {
            altEmailContact.setIcon(Main_Window.Client_Email_AddressBook_Finished_16x16_px);
        }
    }//GEN-LAST:event_altEmailContactMousePressed

    private void emailContactMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_emailContactMousePressed
        if (emailContact.getIcon().equals(Main_Window.Client_Email_AddressBook_Finished_16x16_px)) {
            emailContact.setIcon(Main_Window.Ok_Yellow);
        } else if (emailContact.getIcon().equals(Main_Window.Ok_Yellow)) {
            emailContact.setIcon(Main_Window.Problemsolver_Email_Dialog_Exit_16x16_px);
        } else {
            emailContact.setIcon(Main_Window.Client_Email_AddressBook_Finished_16x16_px);
        }
    }//GEN-LAST:event_emailContactMousePressed

    private void allowSomeChkMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_allowSomeChkMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_allowSomeChkMousePressed

    private void allowNoneLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_allowNoneLabelMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_allowNoneLabelMousePressed

    private void allowAllChkMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_allowAllChkMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_allowAllChkMousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AddressPanel;
    private javax.swing.JPanel AddressStreetPanel;
    private javax.swing.JPanel AddressStreetPanel2;
    private javax.swing.JLabel AverageHours;
    private javax.swing.JPanel CityStatePanel;
    private javax.swing.JPanel ContactInfoPanel;
    private javax.swing.JPanel DatesEmployedPanel;
    private javax.swing.JLabel FirstDayWorked;
    private javax.swing.JPanel GeneralInformationPanel;
    private javax.swing.JLabel LastDayWorked;
    private javax.swing.JPanel MainPanel1;
    private javax.swing.JPanel PhonePanel;
    private javax.swing.JLabel TotalHoursWorked;
    private javax.swing.JLabel allowAllChk;
    private javax.swing.JLabel allowNoneLabel;
    private javax.swing.JLabel allowSomeChk;
    private javax.swing.JLabel altEmailContact;
    private javax.swing.JLabel altPhoneContact;
    private javax.swing.JPanel blankSpace;
    private javax.swing.JPanel booleanContactEmailPanel;
    private javax.swing.JPanel booleanContactNamePanel;
    private javax.swing.JPanel booleanContactNamePanel1;
    private javax.swing.JPanel booleanContactNamePanel2;
    private javax.swing.JPanel booleanContactNamePanel3;
    private javax.swing.JPanel booleanContactNamePanel4;
    private javax.swing.JPanel booleanContactNamePanel5;
    private javax.swing.JPanel booleanContactPanel;
    private javax.swing.JPanel booleanContactSMSPanel;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JLabel cellContact;
    private javax.swing.JPanel checkBoxHolderPanel;
    private javax.swing.JFormattedTextField dob;
    private javax.swing.JComboBox eFEmail;
    private javax.swing.JComboBox eFSMS;
    private javax.swing.JTextField efAddress_1;
    private javax.swing.JTextField efAddress_2;
    private javax.swing.JTextField efAltPhone;
    private javax.swing.JFormattedTextField efAlt_Email;
    private javax.swing.JFormattedTextField efAlt_Emergency;
    private javax.swing.JTextField efCellPhone;
    private javax.swing.JFormattedTextField efCity;
    private javax.swing.JTextField efEMail;
    private javax.swing.JTextField efFirstName;
    private javax.swing.JTextField efLastName;
    private javax.swing.JTextField efMiddleName;
    private javax.swing.JTextField efPhone_1;
    private javax.swing.JTextField efSSN;
    private javax.swing.JTextField efState;
    private javax.swing.JFormattedTextField efZip;
    private javax.swing.JLabel emailContact;
    private javax.swing.JScrollPane empTypesScrollPane;
    private javax.swing.JLabel employeeIdLabel;
    private javax.swing.JPanel employeeOptions;
    private javax.swing.JPanel firstRowPanel;
    private javax.swing.JComboBox fullTimeCombo;
    private javax.swing.JComboBox genderBox;
    private javax.swing.JPanel generalInfoPanel;
    private javax.swing.JLabel homeContact;
    private javax.swing.JPanel imagePanel;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jpEmployeeInfo;
    private javax.swing.JPanel jpHireDate;
    private javax.swing.JPanel jpHireDateContainer;
    private javax.swing.JPanel jpTermDate;
    private javax.swing.JPanel jpTermDateContainer;
    private javax.swing.JLabel lblFirstName;
    private javax.swing.JLabel lblLastName;
    private javax.swing.JLabel lblMiddleName;
    private javax.swing.JPanel leftColumn;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel legendPanel;
    private javax.swing.JCheckBox markInvisibleChk;
    private javax.swing.JComboBox raceBox;
    private javax.swing.JPanel rightColumn;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JPanel rightSidePanel;
    private javax.swing.JPanel secondRowPanel;
    private javax.swing.JLabel socialLabel;
    private javax.swing.JTextField targetHoursTxt;
    private javax.swing.JTextField vacationHoursTxt;
    // End of variables declaration//GEN-END:variables
}
