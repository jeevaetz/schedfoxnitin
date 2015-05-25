/*
 * xEmployeeEdit.java
 *
 * Created on September 15, 2005, 6:41 AM
 *
 * Copyright: SchedFox 2005
 */
/**
 * Modifications: Jeffrey Davis removed Assigned stores tab, Branch tab on
 * 06/16/2010 per Jim's request. Underlying code remains in tact, Branch panel
 * remains for internal code, tab simple removed
 */
package rmischedule.employee;

import schedfoxlib.model.util.Record_Set;
import rmischedule.employee.components.xavailability.xMainAvailabilityPanel;
import rmischedule.components.graphicalcomponents.*;
import rmischedule.components.*;
import rmischedule.employee.components.*;
import rmischedule.employee.searchEmployee.*;
import rmischedule.data_connection.*;
import rmischedule.security.*;
import rmischedule.main.*;
import javax.swing.*;
import java.util.Calendar;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import rmischedule.xadmin.CompanyLoginInformation;
import rmischeduleserver.control.ClientController;
import rmischeduleserver.control.ScheduleController;
import schedfoxlib.model.CompanyInformationObj;
import schedfoxlib.model.Employee;
import rmischeduleserver.mysqlconnectivity.queries.employee.*;
import schedfoxlib.model.Client;

/**
 *
 * @author Ira Juneau
 */
public class xEmployeeEdit extends GenericTabbedEditForm implements CompanyBranchMenuInterface {

    private Connection myConnection;
    public String cpny;
    public String branch;
    public String deleted;
    private Employee_Branch branchPanel;
    public xEmployeeEdit thisObject;
    private String employee;
    public boolean empSelfAdd = false;
    public boolean empApproval = false;
    private int empType = 0;

    /**
     * Creates a new instance of xEmployeeEdit
     */
    public xEmployeeEdit(String loginType) {
        this();
        if (loginType.equalsIgnoreCase("NewEmployee")) {
            super.hideEmpList();
            empSelfAdd = true;
        }
        if (loginType.equalsIgnoreCase("EmpApproval")) {
            super.showApprovalButton();
            empType = 2;
            empApproval = true;
        }
    }

    public xEmployeeEdit() {
        myConnection = new Connection();
        thisObject = this;
        deleted = "0";
    }

    private void setupTabs() {
        //Get our view options so we know what tabs this company should see.
        Hashtable<String, Vector<CompanyInformationObj>> companyViewOptions =
                Main_Window.parentOfApplication.getCompanyViewOptions(cpny);

        super.clearSubForms();

        super.addSubForm(new Employee_Information(this));

        if (empSelfAdd == false && empApproval == false) {
            try {
                if (Main_Window.parentOfApplication.isUserAMemberOfGroups(this.getConnection(), "Payroll")  && !cpny.equals("1")) {
                    super.addSubForm(new Employee_Billing(this));
                }
            } catch (Exception exe) {}
        
        }

        super.addSubForm(new xMainAvailabilityPanel(this));
        if (empSelfAdd == false && empApproval == false) {
            if (!Main_Window.isEmployeeLoggedIn()) {
                //super.addSubForm(new EmployeePictures(this));
                super.addSubForm(new Employee_Files(this));
            }
        }
        if (empSelfAdd == false && empApproval == false) {
            try {
                CompanyInformationObj certObj =
                        Main_Window.parentOfApplication.getCompanyInformation(companyViewOptions, CompanyLoginInformation.EMP_VIEW_CERTIFICATIONS);

                if (!certObj.getOption_value().equalsIgnoreCase("false")) {
                    super.addSubForm(new Employee_Certifications(this));
                }

                CompanyInformationObj dynamicObj =
                        Main_Window.parentOfApplication.getCompanyInformation(companyViewOptions, CompanyLoginInformation.EMP_DYNAMIC_FIELDS);
                if (!dynamicObj.getOption_value().equalsIgnoreCase("false")) {
                    super.addSubForm(new EmployeeDynamicForm(this));
                }

                super.addSubForm(new xEmployee_Notes(this));
                
                CompanyInformationObj stateObj =
                        Main_Window.parentOfApplication.getCompanyInformation(companyViewOptions, CompanyLoginInformation.EMP_STATE);
                if (!stateObj.getOption_value().equalsIgnoreCase("false")) {
                    super.addSubForm(new Employee_State_Cert(this));
                }

                CompanyInformationObj bannedObj =
                        Main_Window.parentOfApplication.getCompanyInformation(companyViewOptions, CompanyLoginInformation.EMP_VIEW_BANNED);
                if (!bannedObj.getOption_value().equalsIgnoreCase("false")) {
                    super.addSubForm(new Employee_Restrictions());
                }

                CompanyInformationObj trainedObj =
                        Main_Window.parentOfApplication.getCompanyInformation(companyViewOptions, CompanyLoginInformation.EMP_TRAINED);
                if (!trainedObj.getOption_value().equalsIgnoreCase("false")) {
                    super.addSubForm(new Employee_Trained());
                }

                CompanyInformationObj branchObj =
                        Main_Window.parentOfApplication.getCompanyInformation(companyViewOptions, CompanyLoginInformation.EMP_VIEW_BRANCH);
                if (!branchObj.getOption_value().equalsIgnoreCase("false")) {
                    this.branchPanel = new Employee_Branch(this);
                    super.addSubForm(this.branchPanel);
                }
                
                if (Main_Window.parentOfApplication.isUserAMemberOfGroups(this.getConnection(), "ADMIN", "Payroll", "District Manager", "Personnel Manager", "Scheduling Manager") && this.getConnection().myCompany.equals("2")) {
                    super.addSubForm(new Employee_Events(this));
                }

                if (Main_Window.parentOfApplication.isUserAMemberOfGroups(this.getConnection(), "ADMIN", "Payroll")) {
                    super.addSubForm(new Employee_Export(this));
                } else {
                    super.addSubForm(new Employee_Export(this), false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        if (empSelfAdd == false && empApproval == false) {
            CompanyInformationObj loginObj =
                    Main_Window.parentOfApplication.getCompanyInformation(companyViewOptions, CompanyLoginInformation.EMP_LOGIN);
            if (loginObj.getOption_value().equalsIgnoreCase("true")) {
                super.addSubForm(new EmployeeLoginPanel(this));
            }
        }
        if (empApproval == true) {
            //super.addSubForm(new Employee_Forms(this));
        }
    }
    
    public boolean shouldView() {
        Employee emp = (Employee)super.getSelectedObject();
        //if (emp.get)
        return false;
    }

    @Override
    protected boolean hasAddData() {
        return !Main_Window.isEmployeeLoggedIn();
    }

    @Override
    protected boolean hasDelete() {
        return !Main_Window.isEmployeeLoggedIn();
    }

    public String getBranchFromChild() {
        if (this.branchPanel != null && !this.branchPanel.getBranch().equals("-1")) {
            return this.branchPanel.getBranch();
        } else {
            return this.branch;
        }
    }

    public void setInformation(String loginType, String co, String br, String employee) {
        setInformation(co, br, employee);
    }

    public void setInformation(String co, String br, String employee) {
        myConnection.setCompany(co);
        myConnection.setBranch(br);
        cpny = co;
        branch = br;
        this.employee = employee;
        setupTabs();
        getData();
    }

    @Override
    public void setVisible(boolean val) {
        if (Main_Window.parentOfApplication.loginType.equalsIgnoreCase("NewEmployee")) {
            super.setVisible(val);
            return;
        }

        if (Main_Window.parentOfApplication.checkSecurity(security_detail.MODULES.EMPLOYEE_EDIT) || Main_Window.isEmployeeLoggedIn()) {
            super.setVisible(val);
//            if (AjaxSwingManager.isAjaxSwingRunning()) {
//                ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(thisObject, true);
//            }
        } else if (!val) {
            super.setVisible(val);
        }
    }

    public void addMyMenu(JMenuBar myMenu) {
        if (!Main_Window.parentOfApplication.loginType.equalsIgnoreCase("NewEmployee")
                && !Main_Window.isEmployeeLoggedIn()) {
            JMenu mainMenu = new JMenu("Select Branch/Company");
            JMenu searchMenu = new JMenu("Search Employees");
            JMenuItem searchBySSN = new JMenuItem("Search Employees");
            searchBySSN.addActionListener(new SSNSearchListener());
            searchMenu.add(searchBySSN);
            Main_Window.parentOfApplication.setUpMenuOfCompsAndBranches(mainMenu, this, "");
            myMenu.add(mainMenu);
            myMenu.add(searchMenu);
        }
    }

    public Object createObjectForList(Record_Set rs) {
        return new Employee(new Date(), rs);
    }

    public String getDisplayNameForObject(Object input) {
        Employee emp = (Employee) input;
        return emp.getEmployeeLastName() + ", " + emp.getEmployeeFirstName();
    }

    public String getWindowTitle() {
        try {
            if (currentSelectedObject == null) {
                return "Adding New Employee For " + Main_Window.parentOfApplication.getCompanyNameById(cpny) + " in " + Main_Window.parentOfApplication.getBranchNameById(branch);
            }
            return "Editing Employee - " + getDisplayNameForObject(currentSelectedObject) + " (" + Main_Window.parentOfApplication.getCompanyNameById(cpny) + ", " + Main_Window.parentOfApplication.getBranchNameById(branch) + ")";
        } catch (Exception e) {
        }
        return "";
    }

    @Override
    protected String getUndeleteString() {
        return "REHIRE";
    }

    public void terminateEmployee(Calendar c) {
        employee_info_query myInfoQuery = new employee_info_query();
        employee_delete_query myDeleteQuery = new employee_delete_query();
        Record_Set rs = new Record_Set();
        getConnection().prepQuery(myInfoQuery);
        myInfoQuery.update(getMyIdForSave(), "");
        getConnection().prepQuery(myDeleteQuery);
        try {
            rs = getConnection().executeQuery(myInfoQuery);
            myDeleteQuery.update(rs.getString("deleted"), getMyIdForSave(), other_functions.cal2Str(c));
            getConnection().executeUpdate(myDeleteQuery);
        } catch (Exception e) {
            return;
        }
        getData();
        clearData();
    }

    public void deleteData() {
        if (!selectedIsMarkedDeleted()) {
            ScheduleController scheduleController = new ScheduleController(this.cpny);
            ArrayList<Integer> clientsWorkedAt = scheduleController.getClientsWorkedAtLastXWeeks(((Employee) currentSelectedObject).getEmployeeId(), 1, 5);
            if (clientsWorkedAt != null && clientsWorkedAt.size() > 0) {
                StringBuilder msg = new StringBuilder();
                msg.append("Employee has worked at the following locations for an extended period of time \r\n");
                try {
                    for (int i = 0; i < clientsWorkedAt.size(); i++) {
                        ClientController clientController = ClientController.getInstance(cpny);
                        Client currClient = clientController.getClientById(clientsWorkedAt.get(i));
                        msg.append("   ").append(currClient.getClientName()).append("\r\n");
                    }
                } catch (Exception exe) {
                }
                msg.append("Please fill out a Personnel Change Form to notify the client(s) of the change.");
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication, msg.toString(), "Notification", JOptionPane.OK_OPTION);
            }

            Employee_Termination_Confirmation etc = new Employee_Termination_Confirmation(Main_Window.parentOfApplication, true, this);
            etc.setVisible(true);
        } else {
            this.terminateEmployee(Calendar.getInstance());
        }

    }

    @Override
    protected String getDeletedString() {
        return "TERMINATE";
    }

    public void getData() {
        employee_list_query myListQuery = new employee_list_query();
        Record_Set rs = new Record_Set();
        boolean canSeeOtherEmployees = false;
        if (security_detail.doesEmployeeHaveAccess(security_detail.EMPLOYEE_SEC.VIEW_OTHER_EMPS.getVal())) {
            //canSeeOtherEmployees = true;
        }

        try {
            myListQuery.update("", deleted, this.employee, canSeeOtherEmployees, empType);
            getConnection().prepQuery(myListQuery);
            rs = myConnection.executeQuery(myListQuery);
        } catch (NullPointerException npe) {
            //Eat this error, just means connection not set up yet
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.populateList(rs, "deleted", "1");
    }

    private JMenuItem addMenuItem(JMenu m, String t, ActionListener a) {
        JMenuItem mi = new JMenuItem(t);
        mi.addActionListener(a);
        m.add(mi);
        return mi;
    }

    public void setDeleted(boolean isPressed) {
        if (isPressed) {
            deleted = "1";
        } else {
            deleted = "0";
        }
    }

    @Override
    public void showDeleted(boolean isPressed) {
        if (isPressed) {
            deleted = "1";
        } else {
            deleted = "0";
        }
        getData();
    }

    public Connection getConnection() {
        return myConnection;
    }

    public String getMyIdForSave() {
        if (currentSelectedObject == null) {
            return "(SELECT COALESCE(MAX(employee_id) + 1, 1) From employee)";
        }
        return ((schedfoxlib.model.Employee) currentSelectedObject).getEmployeeId().toString();
    }

    public void exitForm() {
        setVisible(false);
        clearData();
    }

    @Override
    public boolean getToggleDeleted() {
        return true;
    }

    @Override
    protected ImageIcon getDeletedUpIcon() {
        return Main_Window.Viewing_Active_Employees;
    }

    @Override
    protected ImageIcon getDeletedDownIcon() {
        return Main_Window.Viewing_All_Employees;
    }

    public void clickedMenu(String action, String companyName, String branchName, String companyId, String branchId) {
        myConnection.setBranch(branchId);
        myConnection.setCompany(companyId);
        branch = branchId;
        cpny = companyId;
        getData();
        clearData();
    }

    private class SSNSearchListener implements ActionListener {

        public SSNSearchListener() {
        }

        public void actionPerformed(ActionEvent e) {
            EnterSSNSearch mySearch = new EnterSSNSearch(Main_Window.parentOfApplication, true, thisObject.getConnection(), thisObject);
            mySearch.setVisible(true);
        }
    }
}
