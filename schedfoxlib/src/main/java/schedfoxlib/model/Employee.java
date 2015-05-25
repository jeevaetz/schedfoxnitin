/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.ImageIcon;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.controller.BillingControllerInterface;
import schedfoxlib.controller.CompanyControllerInterface;
import schedfoxlib.controller.EmployeeControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;
import schedfoxlib.model.util.ImageLoader;

/**
 *
 * @author user
 */
public class Employee implements Serializable, Comparable, AddressInterface, UserInterface, EmailContact, PhoneContact, Entity {

    private static final long serialVersionUID = 1L;

    private static final SimpleDateFormat readableFormat = new SimpleDateFormat("MM/dd/yyyy");

    private Date currDate;

    private Integer employeeId;
    //Used as a way of temporarily storing the int from the sequence
    private Integer newEmployeeId;
    private int branchId;
    private String employeeFirstName;
    private String employeeLastName;
    private String employeeMiddleInitial;
    private String employeeSsn;

    private Address addressObj;

    private String employeePhone;
    private String employeePhone2;
    private String employeeCell;
    private String employeePager;
    private String employeeEmail;
    private String employeeEmergency;
    private Date employeeHireDate;
    private Date employeeTermDate;
    private int employeeIsDeleted;
    private Date employeeLastUpdated;
    private int employeeType;
    private String webLogin;
    private String webPw;
    private Date employeeBirthdate;
    private String employeeLogin;
    private String employeePassword;
    private Boolean isLoginAvailable;
    private Integer employeeTypeId;
    private String employeeEmail2;
    private String emailMessaging;
    private Boolean smsMessaging;
    private Integer employeeWorkhrsPweek;
    private Integer accruedVacation;
    private Boolean markInvisible;
    private Integer gender;
    private Integer race;
    private Date employeeDeletedOn;
    private Boolean fullTime;
    private String employeeScanId;
    private Boolean isSubContractor;
    
    private Integer phoneContact;
    private Integer altPhoneContact;
    private Integer cellContact;
    private Integer emailContact;
    private Integer altEmailContact;

    //Lazy loaded objects
    private transient Vector<EmployeeCertification> certifications;
    private transient Branch branch;
    private transient ArrayList<EmployeeDeductions> deductions;

    public Employee(Date currDate) {
        this.currDate = currDate;
        this.addressObj = new Address();
    }

    public Employee(Date currDate, Integer employeeId) {
        this.currDate = currDate;
        this.employeeId = employeeId;
        this.addressObj = new Address();
    }

    public Employee(Date currDate, String employeeSsn) {
        this.currDate = currDate;
        this.employeeSsn = employeeSsn;
        this.addressObj = new Address();
    }

    public Employee(){
        this.addressObj = new Address();
    }


    public Employee(Date currDate, Integer employeeId, int branchId, String employeeFirstName, String employeeLastName, String employeeMiddleInitial, String employeeSsn, String employeeAddress, String employeeAddress2, String employeeCity, String employeeState, String employeeZip, String employeePhone, String employeePhone2, String employeeCell, String employeePager, String employeeEmail, Date employeeHireDate, Date employeeTermDate, int employeeIsDeleted, Date employeeLastUpdated, int employeeType, String webLogin, String webPw, String employeeEmail2, String emailMessaging) {
        this.currDate = currDate;
        this.employeeId = employeeId;
        this.branchId = branchId;
        this.employeeFirstName = employeeFirstName;
        this.employeeLastName = employeeLastName;
        this.employeeMiddleInitial = employeeMiddleInitial;
        this.employeeSsn = employeeSsn;
        this.addressObj = new Address();
        this.addressObj.setAddress1(employeeAddress);
        this.addressObj.setAddress2(employeeAddress2);
        this.addressObj.setCity(employeeCity);
        this.addressObj.setState(employeeState);
        this.addressObj.setZip(employeeZip); 
        this.employeePhone = employeePhone;
        this.employeePhone2 = employeePhone2;
        this.employeeCell = employeeCell;
        this.employeePager = employeePager;
        this.employeeEmail = employeeEmail;
        this.employeeHireDate = employeeHireDate;
        this.employeeTermDate = employeeTermDate;
        this.employeeIsDeleted = employeeIsDeleted;
        this.employeeLastUpdated = employeeLastUpdated;
        this.employeeType = employeeType;
        this.webLogin = webLogin;
        this.webPw = webPw;
        this.employeeEmail2 = employeeEmail2;
        this.emailMessaging = emailMessaging;
    }

    public Employee(Date currDate, Record_Set rst) {
        this.currDate = currDate;
        this.addressObj = new Address();
        try {
            this.branchId = rst.getInt("branch_id");
        } catch (Exception e) {}
        if (rst.hasColumn("employee_id")) {
            this.employeeId = rst.getInt("employee_id");
        } else {
            this.employeeId = rst.getInt("id");
        }
        try {
            if (rst.hasColumn("employee_birthdate")) {
                this.employeeBirthdate = rst.getDate("employee_birthdate");
            } else {
                this.employeeBirthdate = rst.getDate("bdate");
            }
        } catch (Exception exe) {}
        if (rst.hasColumn("employee_first_name")) {
            this.employeeFirstName = rst.getString("employee_first_name");
        } else {
            this.employeeFirstName = rst.getString("firstname");
        }
        if (rst.hasColumn("employee_last_name")) {
            this.employeeLastName = rst.getString("employee_last_name");
        } else {
            this.employeeLastName = rst.getString("lastname");
        }
        if (rst.hasColumn("name")) {
            String[] nameSplit = rst.getString("name").split(",");
            this.employeeLastName = nameSplit[0];
            this.employeeFirstName = nameSplit[1];
        }
        if (rst.hasColumn("employee_middle_initial")) {
            this.employeeMiddleInitial = rst.getString("employee_middle_initial");
        } else {
            this.employeeMiddleInitial = rst.getString("middlename");
        }
        if (rst.hasColumn("employee_address")) {
            this.addressObj.setAddress1(rst.getString("employee_address"));
        } else {
            this.addressObj.setAddress1(rst.getString("address"));
        }
        if (rst.hasColumn("employee_address2")) {
            this.addressObj.setAddress2(rst.getString("employee_address2"));
        } else {
            this.addressObj.setAddress2(rst.getString("address2"));
        }
        if (rst.hasColumn("employee_phone")) {
            this.employeePhone = rst.getString("employee_phone");
        } else {
            this.employeePhone = rst.getString("phone");
        }
        if (rst.hasColumn("employee_cell")) {
            this.employeeCell = rst.getString("employee_cell");
        } else {
            this.employeeCell = rst.getString("cell");
        }
        if (rst.hasColumn("employee_phone2")) {
            this.employeePhone2 = rst.getString("employee_phone2");
        } else {
            this.employeePhone2 = rst.getString("phone2");
        }
        if (rst.hasColumn("employee_city")) {
            this.addressObj.setCity(rst.getString("employee_city"));
        } else {
            this.addressObj.setCity(rst.getString("city"));
        }
        if (rst.hasColumn("employee_state")) {
            this.addressObj.setState(rst.getString("employee_state"));
        } else {
            this.addressObj.setState(rst.getString("state"));
        }
        if (rst.hasColumn("employee_zip")) {
            this.addressObj.setZip(rst.getString("employee_zip"));
        } else {
            this.addressObj.setZip(rst.getString("zip"));
        }
        if (rst.hasColumn("ssn")) {
            this.employeeSsn = rst.getString("ssn");
        } else if (rst.hasColumn("employee_ssn")) {
            this.employeeSsn = rst.getString("employee_ssn");
        }
        if (rst.hasColumn("email")) {
            this.employeeEmail = rst.getString("email");
        } else if (rst.hasColumn("employee_email")) {
            this.employeeEmail = rst.getString("employee_email");
        }
        if (rst.hasColumn("email2")) {
            this.employeeEmail2 = rst.getString("email2");
        }
        if (rst.hasColumn("smsmessaging")) {
            this.smsMessaging = rst.getBoolean("smsmessaging");
        }
        if (rst.hasColumn("emessaging")) {
            this.emailMessaging = rst.getString("emessaging");
        }
        if (rst.hasColumn("employee_deleted_on")) {
            try {
                this.employeeDeletedOn = rst.getTimestamp("employee_deleted_on");
            } catch (Exception exe) {}
        }
        if (rst.hasColumn("employee_hire_date")) {
            this.employeeHireDate = rst.getDate("employee_hire_date");
        } else {
            try {
                this.employeeHireDate = rst.getDate("hire");
            } catch (Exception e) {
                this.employeeHireDate = new Date();
            }
        }
        if (rst.hasColumn("employee_term_date")) {
            this.employeeTermDate = rst.getDate("employee_term_date");
        } else {
            try {
                this.employeeTermDate = rst.getDate("term");
            } catch (Exception e) {
                this.employeeTermDate = new Date();
            }
        }
        if (rst.hasColumn("deleted")) {
            this.employeeIsDeleted = rst.getInt("deleted");
        } else if (rst.hasColumn("del")) {
            this.employeeIsDeleted = rst.getInt("del");
        } else {
            this.employeeIsDeleted = rst.getInt("employee_is_deleted");
        }
        try {
            if (rst.hasColumn("mark_invisible")) {
                this.markInvisible = rst.getBoolean("mark_invisible");
            } else {                                 
                this.markInvisible = rst.getBoolean("isinvisible");
            }
        } catch (Exception e) {}

        try {
            this.gender = rst.getInt("gender");
        } catch (Exception e) {}
        try {
            this.race = rst.getInt("race");
        } catch (Exception e) {}
        
        try {
            this.phoneContact = rst.getInt("phone_contact");
        } catch (Exception e) {}
        try {
            if (rst.hasColumn("alt_phone_contact")) {
                this.altPhoneContact = rst.getInt("alt_phone_contact");
            } else {
                this.altPhoneContact = rst.getInt("phone2_contact");
            }
        } catch (Exception e) {}
        try {
            this.cellContact = rst.getInt("cell_contact");
        } catch (Exception e) {}
        try {
            this.emailContact = rst.getInt("email_contact");
        } catch (Exception e) {}
        try {
            this.employeeEmergency = rst.getString("employee_emergency");
        } catch (Exception e) {}
        try {
            this.altEmailContact = rst.getInt("alt_email_contact");
        } catch (Exception e) {}
        try {
            this.fullTime = rst.getBoolean("full_time");
        } catch (Exception e) {}
        try {
            this.employeeScanId = rst.getString("employee_scan_id");
        } catch (Exception e) {}
        try {
            this.employeeEmergency = rst.getString("employee_emergency");
        } catch (Exception e) {}
        try {
            this.isSubContractor = rst.getBoolean("is_sub_contractor");
        } catch (Exception e) {}
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getEmployeeFirstName() {
        if (employeeFirstName == null) {
            return "";
        }
        return employeeFirstName.trim();
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        if (employeeFirstName != null && employeeFirstName.length() > 100) {
            employeeFirstName = employeeFirstName.substring(0, 99);
        }
        this.employeeFirstName = employeeFirstName;
    }

    public String getEmployeeLastName() {
        if (employeeLastName == null) {
            return "";
        }
        return employeeLastName.trim();
    }

    public void setEmployeeLastName(String employeeLastName) {
        if (employeeLastName != null && employeeLastName.length() > 100) {
            employeeLastName = employeeLastName.substring(0, 99);
        }
        this.employeeLastName = employeeLastName;
    }

    public String getEmployeeFullName() {
        return this.getEmployeeFirstName() + " " + this.getEmployeeLastName();
    }

    public String getEmployeeFullNameReversed() {
        if ((this.getEmployeeFirstName() + this.getEmployeeLastName()).length() == 0) {
            return "";
        }
        return this.getEmployeeLastName() + ", " + this.getEmployeeFirstName();
    }

    public String getEmployeeMiddleInitial() {
        if (employeeMiddleInitial == null) {
            return "";
        }
        return employeeMiddleInitial;
    }

    public void setEmployeeMiddleInitial(String employeeMiddleInitial) {
        if (employeeMiddleInitial != null && employeeMiddleInitial.length() > 100) {
            employeeMiddleInitial = employeeMiddleInitial.substring(0, 99);
        }
        this.employeeMiddleInitial = employeeMiddleInitial;
    }

    public String getEmployeeSsn() {
        if (employeeSsn == null) {
            return "";
        }
        return employeeSsn;
    }

    public void setEmployeeSsn(String employeeSsn) {
        if (employeeSsn != null) {
            employeeSsn = employeeSsn.replaceAll("\\D", "");
            if (employeeSsn.length() > 9) {
                employeeSsn = employeeSsn.substring(0, 9);
            }
        }
        this.employeeSsn = employeeSsn;
    }

    public String getEmployeeAddress() {
        if (this.addressObj == null) {
            return "";
        }
        return this.addressObj.getAddress1();
    }

    public void setEmployeeAddress(String employeeAddress) {
        if (employeeAddress != null && employeeAddress.length() > 50) {
            employeeAddress = employeeAddress.substring(0, 49);
        }
        if (addressObj == null) {
            addressObj = new Address();
        }
        this.addressObj.setAddress1(employeeAddress);
    }

    public String getEmployeeAddress2() {
        if (addressObj == null) {
            return "";
        }
        return this.addressObj.getAddress2();
    }

    public void setEmployeeAddress2(String employeeAddress2) {
        if (employeeAddress2 != null && employeeAddress2.length() > 50) {
            employeeAddress2 = employeeAddress2.substring(0, 49);
        }
        if (addressObj == null) {
            addressObj = new Address();
        }
        this.addressObj.setAddress2(employeeAddress2);
    }

    public String getEmployeeCity() {
        if (addressObj == null) {
            return "";
        }
        return this.addressObj.getCity();
    }

    public void setEmployeeCity(String employeeCity) {
        if (employeeCity != null && employeeCity.length() > 50) {
            employeeCity = employeeCity.substring(0, 49);
        }
        if (addressObj == null) {
            addressObj = new Address();
        }
        this.addressObj.setCity(employeeCity);
    }

    public String getEmployeeState() {
        if (addressObj == null) {
            return "";
        }
        return this.addressObj.getState();
    }

    public void setEmployeeState(String employeeState) {
        if (employeeState != null && employeeState.length() > 2) {
            employeeState = employeeState.substring(0, 2);
        }
        if (employeeState != null) {
            employeeState = employeeState.toUpperCase();
        }
        if (addressObj == null) {
            addressObj = new Address();
        }
        this.addressObj.setState(employeeState);
    }

    public String getEmployeeZip() {
        if (addressObj == null) {
            return "";
        }
        return this.addressObj.getZip();
    }

    public void setEmployeeZip(String employeeZip) {
        if (employeeZip != null) {
            employeeZip = employeeZip.replaceAll("\\D", "");
        }
        if (employeeZip != null && employeeZip.length() > 20) {
            employeeZip = employeeZip.substring(0, 19);
        }
        if (addressObj == null) {
            addressObj = new Address();
        }
        this.addressObj.setZip(employeeZip);
    }

    public String getEmployeePhone() {
        if (employeePhone == null) {
            return "";
        }
        return employeePhone;
    }

    public void setEmployeePhone(String employeePhone) {
        if (employeePhone != null && employeePhone.length() > 20) {
            employeePhone = employeePhone.substring(0, 19);
        }
        if (employeePhone != null) {
            employeePhone = employeePhone.replaceAll("\\D", "");
            if (employeePhone.length() >= 10) {
                employeePhone = employeePhone.substring(0, 3) + "-" + employeePhone.substring(3, 6) + "-" + employeePhone.substring(6, 10);
            }
        }
        this.employeePhone = employeePhone;
    }

    public String getEmployeePhone2() {
        if (employeePhone2 == null) {
            return "";
        }
        return employeePhone2;
    }

    public void setEmployeePhone2(String employeePhone2) {
        if (employeePhone2 != null && employeePhone2.length() > 20) {
            employeePhone2 = employeePhone2.substring(0, 19);
        }
        if (employeePhone2 != null) {
            employeePhone2 = employeePhone2.replaceAll("\\D", "");
            if (employeePhone2.length() >= 10) {
                employeePhone2 = employeePhone2.substring(0, 3) + "-" + employeePhone2.substring(3, 6) + "-" + employeePhone2.substring(6, 10);
            }
        }
        this.employeePhone2 = employeePhone2;
    }

    public String getEmployeeCell() {
        if (employeeCell == null) {
            return "";
        }
        return employeeCell;
    }

    public void setEmployeeCell(String employeeCell) {
        if (employeeCell != null && employeeCell.length() > 20) {
            employeeCell = employeeCell.substring(0, 19);
        }
        if (employeeCell != null) {
            employeeCell = employeeCell.replaceAll("\\D", "");
            if (employeeCell.length() >= 10) {
                employeeCell = employeeCell.substring(0, 3) + "-" + employeeCell.substring(3, 6) + "-" + employeeCell.substring(6, 10);
            }
        }
        this.employeeCell = employeeCell;
    }

    public String getEmployeePager() {
        if (employeePager == null) {
            return "";
        }
        return employeePager;
    }

    public void setEmployeePager(String employeePager) {
        if (employeePager != null && employeePager.length() > 20) {
            employeePager = employeePager.substring(0, 19);
        }
        if (employeePager != null) {
            employeePager = employeePager.replaceAll("\\D", "");
            if (employeePager.length() >= 10) {
                employeePager = employeePager.substring(0, 3) + "-" + employeePager.substring(3, 6) + "-" + employeePager.substring(6, 10);
            }
        }
        this.employeePager = employeePager;
    }

    public String getEmployeeEmail() {
        if (employeeEmail == null) {
            return "";
        }
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        if (employeeEmail != null && employeeEmail.length() > 50) {
            employeeEmail = employeeEmail.substring(0, 49);
        }
        this.employeeEmail = employeeEmail;
    }

    public Date getEmployeeHireDate() {
        if (employeeHireDate == null) {
            employeeHireDate = new Date("01/01/2000");
        }
        return employeeHireDate;
    }

    public void setEmployeeHireDate(Date employeeHireDate) {
        this.employeeHireDate = employeeHireDate;
    }

    public String getEmployeeHireDateStr() {
        try {
            return readableFormat.format(employeeHireDate);
        } catch (Exception e) {
            return "";
        }
    }

    public String getEmployeeTermDateStr() {
        try {
            return readableFormat.format(employeeTermDate);
        } catch (Exception e) {
            return "";
        }
    }

    public Date getEmployeeTermDate() {
        if (employeeTermDate == null) {
            SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
            try {
                employeeTermDate = myFormat.parse("12/31/2100");
            } catch (Exception e) {
                employeeTermDate = new Date();
            }
        }
        return employeeTermDate;
    }

    public void setEmployeeTermDate(Date employeeTermDate) {
        this.employeeTermDate = employeeTermDate;
    }

    public void setDeleted(boolean deleted) {
        if (deleted) {
            employeeIsDeleted = 1;
        } else {
            employeeIsDeleted = 0;
        }
    }
    
    public boolean isDeleted() {
        try {
            return employeeIsDeleted == 1 ? true : false;
        } catch (Exception exe) {
            return false;
        }
    }
    
    public int getEmployeeIsDeleted() {
        return employeeIsDeleted;
    }

    public void setEmployeeIsDeleted(String employeeIsDeleted) {
        int isDel = 0;
        if (employeeIsDeleted.equalsIgnoreCase("true") ||
                employeeIsDeleted.equalsIgnoreCase("yes") ||
                employeeIsDeleted.equalsIgnoreCase("1")) {
            isDel = 1;
        }
        this.employeeIsDeleted = isDel;
    }

    public void setEmployeeIsDeletedShort(int employeeIsDeleted) {
        this.employeeIsDeleted = employeeIsDeleted;
    }

    public Date getEmployeeLastUpdated() {
        if (employeeLastUpdated == null) {
            employeeLastUpdated = this.currDate;
        }
        return employeeLastUpdated;
    }

    public void setEmployeeLastUpdated(Date employeeLastUpdated) {
        this.employeeLastUpdated = employeeLastUpdated;
    }

    public int getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(int employeeType) {
        this.employeeType = employeeType;
    }

    public String getWebLogin() {
        if (webLogin == null) {
            return "";
        }
        return webLogin;
    }

    public void setWebLogin(String webLogin) {
        this.webLogin = webLogin;
    }

    public String getWebPw() {
        if (webPw == null) {
            return "";
        }
        return webPw;
    }

    public void setWebPw(String webPw) {
        this.webPw = webPw;
    }

    public Date getEmployeeBirthdate() {
        if (employeeBirthdate == null) {
            employeeBirthdate = this.currDate;
        }
        return employeeBirthdate;
    }

    public void setEmployeeBirthdate(Date employeeBirthdate) {
        this.employeeBirthdate = employeeBirthdate;
    }

    public String getEmployeeLogin() {
        if (employeeLogin == null) {
            return "";
        }
        return employeeLogin;
    }

    public void setEmployeeLogin(String employeeLogin) {
        this.employeeLogin = employeeLogin;
    }

    public String getEmployeePassword() {
        if (employeePassword == null) {
            return "";
        }
        return employeePassword;
    }

    public void setEmployeePassword(String employeePassword) {
        this.employeePassword = employeePassword;
    }

    public Boolean getIsLoginAvailable() {
        return isLoginAvailable;
    }

    public void setIsLoginAvailable(Boolean isLoginAvailable) {
        this.isLoginAvailable = isLoginAvailable;
    }

    public void setIsLoginAvailableShort(String isLoginAvailable) {
        boolean isAvail = false;
        if (isLoginAvailable.equalsIgnoreCase("true") ||
                isLoginAvailable.equalsIgnoreCase("yes") ||
                isLoginAvailable.equalsIgnoreCase("1")) {
            isAvail = true;
        }
        this.isLoginAvailable = isAvail;
    }

    public Integer getEmployeeTypeId() {
        return employeeTypeId;
    }

    public void setEmployeeTypeId(Integer employeeTypeId) {
        this.employeeTypeId = employeeTypeId;
    }

    public String getEmployeeEmail2() {
        if (employeeEmail2 == null) {
            return "";
        }
        return employeeEmail2;
    }

    public void setEmployeeEmail2(String employeeEmail2) {
        this.employeeEmail2 = employeeEmail2;
    }

    public String getEmailMessaging() {
        if (emailMessaging == null) {
            return "";
        }
        return emailMessaging;
    }

    public void setEmailMessaging(String emailMessaging) {
        this.emailMessaging = emailMessaging;
    }

    public Boolean getSmsMessaging() {
        return smsMessaging;
    }

    public void setSmsMessaging(String smsMessaging) {
        boolean sms = true;
        if (smsMessaging.equalsIgnoreCase("true") ||
                smsMessaging.equalsIgnoreCase("yes") ||
                smsMessaging.equalsIgnoreCase("1")) {
            sms = false;
        }
        this.smsMessaging = sms;
    }

    public void setSmsMessagingBoolean(Boolean smsMessaging) {
        this.smsMessaging = smsMessaging;
    }

    public Integer getEmployeeWorkhrsPweek() {
        return employeeWorkhrsPweek;
    }

    public void setEmployeeWorkhrsPweek(Integer employeeWorkhrsPweek) {
        this.employeeWorkhrsPweek = employeeWorkhrsPweek;
    }

    public Integer getAccruedVacation() {
        return accruedVacation;
    }

    public void setAccruedVacation(Integer accruedVacation) {
        this.accruedVacation = accruedVacation;
    }

    /**
     * Gets the certifications.
     * @return
     */
    public Vector<EmployeeCertification> getCertifications(String companyId) {
        if (this.certifications == null) {
            try {
                EmployeeControllerInterface employeeController = ControllerRegistryAbstract.getEmployeeController(companyId);
                this.certifications = employeeController.getEmployeeCertificationsForEmployee(this.getEmployeeId());
            } catch (Exception exe) {
                this.certifications = new Vector<EmployeeCertification>();
            }
        }
        return this.certifications;
    }

    public ArrayList<EmployeeDeductions> getDeductions(String companyId) {
        if (this.deductions == null) {
            try {
                BillingControllerInterface billingController = ControllerRegistryAbstract.getBillingController(companyId);
                this.deductions = billingController.getEmployeeDeductions(this.employeeId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.deductions;
    }

    public Branch getBranch(String companyId) {
        if (this.branch == null) {
            CompanyControllerInterface compController = ControllerRegistryAbstract.getCompanyController(companyId);
            try {
                this.branch = compController.getBranchById(this.branchId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.branch;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeId != null ? employeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (object instanceof String) {
            return (this.getEmployeeId() + "").equals(object);
        }
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getFullName();
    }

    /**
     * @return the newEmployeeId
     */
    public Integer getNewEmployeeId() {
        return newEmployeeId;
    }

    /**
     * @param newEmployeeId the newEmployeeId to set
     */
    public void setNewEmployeeId(Integer newEmployeeId) {
        this.newEmployeeId = newEmployeeId;
    }

    public String getEmployeeImageUrl(String currentCompanyDB) throws Exception {
        return ImageLoader.getImageURL(this.getEmployeeId() + ".jpg", currentCompanyDB, "employee");
    }
    
    public String getEmployeeImageUrl() throws Exception {
        return this.getEmployeeImageUrl("champion_db");
    }
    
    /**
     * Loads the employee image, this actually does make a call to the server so
     * use it sparingly and use local variables where called from to limit back
     * and forth.
     * @param currentCompanyDB The schema name of the company the employee is
     * associated with.
     * @return ImageIcon of the employe image
     * @throws Exception If there is a problem connecting to server
     */
    public ImageIcon getEmployeeImage(String currentCompanyDB) throws Exception {
        return ImageLoader.getImage(this.getEmployeeId() + ".jpg", currentCompanyDB, "employee");
    }

    @Override
    public String getAddress1() {
        return this.getEmployeeAddress();
    }
    
    @Override
    public String getAddress2() {
        return this.getEmployeeAddress2();
    }

    @Override
    public String getCity() {
        return this.getEmployeeCity();
    }

    @Override
    public String getState() {
        return this.getEmployeeState();
    }

    @Override
    public String getZip() {
        return this.getEmployeeZip();
    }

    @Override
    public boolean isValid() {
        boolean retVal = true;
        if (this.getZip() == null) {
            retVal = false;
        } else {
            String testString = this.getZip().replaceAll("\\D+", "");
            if (testString.trim().length() == 0) {
                retVal = false;
            }
        }
        return retVal;
    }

    @Override
    public Integer getId() {
        return this.employeeId;
    }

    @Override
    public String getEmailAddress() {
        return this.getEmployeeEmail();
    }

    @Override
    public String getFullName() {
        return this.getEmployeeFirstName() + " " + this.getEmployeeLastName();
    }

    @Override
    public int getPrimaryId() {
        return this.getEmployeeId() == null ? 0 : this.getEmployeeId();
    }

    @Override
    public String getType() {
        return "Employee";
    }

    @Override
    public String getPhoneNumber() {
        return this.getEmployeePhone();
    }

    /**
     * @return the markInvisible
     */
    public Boolean getMarkInvisible() {
        return markInvisible;
    }

    /**
     * @param markInvisible the markInvisible to set
     */
    public void setMarkInvisible(Boolean markInvisible) {
        this.markInvisible = markInvisible;
    }

    /**
     * @return the gender
     */
    public Integer getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(Integer gender) {
        this.gender = gender;
    }

    /**
     * @return the race
     */
    public Integer getRace() {
        return race;
    }

    /**
     * @param race the race to set
     */
    public void setRace(Integer race) {
        this.race = race;
    }

    public Address getAddressObj() {
        return this.addressObj;
    }
    
    public void setAddress1(String address1) {
        if (this.addressObj == null) {
            this.addressObj = new Address();
        }
        this.addressObj.setAddress1(address1);
    }
    
    public void setAddress2(String address2) {
        if (this.addressObj == null) {
            this.addressObj = new Address();
        }
        this.addressObj.setAddress2(address2);
    }
    
    public void setCity(String city) {
        if (this.addressObj == null) {
            this.addressObj = new Address();
        }
        this.addressObj.setCity(city);
    }
    
    public void setState(String state) {
        if (this.addressObj == null) {
            this.addressObj = new Address();
        }
        this.addressObj.setState(state);
    }
    
    public void setZip(String zip) {
        if (this.addressObj == null) {
            this.addressObj = new Address();
        }
        this.addressObj.setZip(zip);
    }
    
    public void setAddressObj(Address address) {
        this.addressObj = address;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Employee) {
            this.getEmployeeLastName().compareToIgnoreCase(((Employee)o).getEmployeeLastName());
        }
        return 1;
    }

    /**
     * @return the phoneContact
     */
    public Integer getPhoneContact() {
        return phoneContact;
    }

    /**
     * @param phoneContact the phoneContact to set
     */
    public void setPhoneContact(Integer phoneContact) {
        this.phoneContact = phoneContact;
    }

    /**
     * @return the altPhoneContact
     */
    public Integer getAltPhoneContact() {
        return altPhoneContact;
    }

    /**
     * @param altPhoneContact the altPhoneContact to set
     */
    public void setAltPhoneContact(Integer altPhoneContact) {
        this.altPhoneContact = altPhoneContact;
    }

    /**
     * @return the cellContact
     */
    public Integer getCellContact() {
        return cellContact;
    }

    /**
     * @param cellContact the cellContact to set
     */
    public void setCellContact(Integer cellContact) {
        this.cellContact = cellContact;
    }

    /**
     * @return the emailContact
     */
    public Integer getEmailContact() {
        return emailContact;
    }

    /**
     * @param emailContact the emailContact to set
     */
    public void setEmailContact(Integer emailContact) {
        this.emailContact = emailContact;
    }

    /**
     * @return the altEmailContact
     */
    public Integer getAltEmailContact() {
        return altEmailContact;
    }

    /**
     * @param altEmailContact the altEmailContact to set
     */
    public void setAltEmailContact(Integer altEmailContact) {
        this.altEmailContact = altEmailContact;
    }

    @Override
    public String getName() {
        return this.getFullName();
    }

    /**
     * @return the employee_deleted_on
     */
    public Date getEmployeeDeletedOn() {
        return employeeDeletedOn;
    }

    /**
     * @param employee_deleted_on the employee_deleted_on to set
     */
    public void setEmployeeDeletedOn(Date employee_deleted_on) {
        this.employeeDeletedOn = employee_deleted_on;
    }

    /**
     * @return the fullTime
     */
    public Boolean getFullTime() {
        return fullTime;
    }

    /**
     * @param fullTime the fullTime to set
     */
    public void setFullTime(Boolean fullTime) {
        this.fullTime = fullTime;
    }

    /**
     * @return the employeeScanId
     */
    public String getEmployeeScanId() {
        return employeeScanId;
    }

    /**
     * @param employeeScanId the employeeScanId to set
     */
    public void setEmployeeScanId(String employeeScanId) {
        this.employeeScanId = employeeScanId;
    }

    /**
     * @return the employeeEmergency
     */
    public String getEmployeeEmergency() {
        return employeeEmergency;
    }

    /**
     * @param employeeEmergency the employeeEmergency to set
     */
    public void setEmployeeEmergency(String employeeEmergency) {
        this.employeeEmergency = employeeEmergency;
    }

    /**
     * @return the isSubContractor
     */
    public Boolean getIsSubContractor() {
        return isSubContractor;
    }

    /**
     * @param isSubContractor the isSubContractor to set
     */
    public void setIsSubContractor(Boolean isSubContractor) {
        this.isSubContractor = isSubContractor;
    }
}
