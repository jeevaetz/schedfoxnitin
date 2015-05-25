/*
 * GetDataFromSource.java
 *
 * Created on February 21, 2006, 12:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rmischedule.data_import.dataImportClasses;
import java.util.*;
import rmischedule.data_import.dataImportClasses.destinationTables.*;
/**
 *
 * @author Ira Juneau
 */
public abstract class GetDataFromSource {

    private static destTableClass compTable = new destTableClass("control_db", "company");
    private static destTableClass clientTable = new destTableClass("", "client");
    private static destTableClass employeeTable = new destTableClass("", "employee");
    private static destTableClass scheduleTable = new destTableClass("", "schedule");
    private static SourceToDestinationClass companyName = new SourceToDestinationClass(compTable, "company_name");

    public static SourceToDestinationClass clientId = new SourceToDestinationClass(clientTable, "client_id");
    public static SourceToDestinationClass clientName = new SourceToDestinationClass(clientTable, "client_name");
    public static SourceToDestinationClass clientPhone = new SourceToDestinationClass(clientTable, "client_phone");
    public static SourceToDestinationClass clientPhone2 = new SourceToDestinationClass(clientTable, "client_phone2");
    public static SourceToDestinationClass clientFax = new SourceToDestinationClass(clientTable, "client_fax");
    public static SourceToDestinationClass clientAddress = new SourceToDestinationClass(clientTable, "client_address");
    public static SourceToDestinationClass clientAddress2 = new SourceToDestinationClass(clientTable, "client_address2");
    public static SourceToDestinationClass clientCity = new SourceToDestinationClass(clientTable, "client_city");
    public static SourceToDestinationClass clientState = new SourceToDestinationClass(clientTable, "client_state");
    public static SourceToDestinationClass clientZip = new SourceToDestinationClass(clientTable, "client_zip");
    public static SourceToDestinationClass clientManagement = new SourceToDestinationClass(clientTable, "management_id");
    public static SourceToDestinationClass clientStart = new SourceToDestinationClass(clientTable, "client_start_date");
    public static SourceToDestinationClass clientEnd = new SourceToDestinationClass(clientTable, "client_end_date");
    public static SourceToDestinationClass clientDeleted = new SourceToDestinationClass(clientTable, "client_is_deleted");
    public static SourceToDestinationClass clientType = new SourceToDestinationClass(clientTable, "client_type");
    public static SourceToDestinationClass clientUpdated = new SourceToDestinationClass(clientTable, "client_last_updated");
    public static SourceToDestinationClass clientWorksite = new SourceToDestinationClass(clientTable, "client_worksite");
    public static SourceToDestinationClass clientTraining = new SourceToDestinationClass(clientTable, "client_training_time");
    public static SourceToDestinationClass clientBill = new SourceToDestinationClass(clientTable, "client_bill_for_training_bool");
    public static SourceToDestinationClass clientWorksiteOrder = new SourceToDestinationClass(clientTable, "client_worksite_order");
    public static SourceToDestinationClass clientRateCode = new SourceToDestinationClass(clientTable, "rate_code_id");
    public static SourceToDestinationClass clientBreak = new SourceToDestinationClass(clientTable, "client_break");

    public static SourceToDestinationClass employeeId = new SourceToDestinationClass(employeeTable, "employee_id");
    public static SourceToDestinationClass employeeFName = new SourceToDestinationClass(employeeTable, "employee_first_name");
    public static SourceToDestinationClass employeeLName = new SourceToDestinationClass(employeeTable, "employee_last_name");
    public static SourceToDestinationClass employeeMName = new SourceToDestinationClass(employeeTable, "employee_middle_initial");
    public static SourceToDestinationClass employeeSSN = new SourceToDestinationClass(employeeTable, "employee_ssn");
    public static SourceToDestinationClass employeeAddress = new SourceToDestinationClass(employeeTable, "employee_address");
    public static SourceToDestinationClass employeeAddress2 = new SourceToDestinationClass(employeeTable, "employee_address2");
    public static SourceToDestinationClass employeeCity = new SourceToDestinationClass(employeeTable, "employee_city");
    public static SourceToDestinationClass employeeState = new SourceToDestinationClass(employeeTable, "employee_state");
    public static SourceToDestinationClass employeeZip = new SourceToDestinationClass(employeeTable, "employee_zip");
    public static SourceToDestinationClass employeePhone = new SourceToDestinationClass(employeeTable, "employee_phone");
    public static SourceToDestinationClass employeePhone2 = new SourceToDestinationClass(employeeTable, "employee_phone2");
    public static SourceToDestinationClass employeeCell = new SourceToDestinationClass(employeeTable, "employee_cell");
    public static SourceToDestinationClass employeePager = new SourceToDestinationClass(employeeTable, "employee_pager");
    public static SourceToDestinationClass employeeEMail = new SourceToDestinationClass(employeeTable, "employee_email");
    public static SourceToDestinationClass employeeHire = new SourceToDestinationClass(employeeTable, "employee_hire_date");
    public static SourceToDestinationClass employeeTerm = new SourceToDestinationClass(employeeTable, "employee_term_date");
    public static SourceToDestinationClass employeeDeleted = new SourceToDestinationClass(employeeTable, "employee_is_deleted");
    public static SourceToDestinationClass employeeUpdated = new SourceToDestinationClass(employeeTable, "employee_last_updated");
    public static SourceToDestinationClass employeeType = new SourceToDestinationClass(employeeTable, "employee_type");
    public static SourceToDestinationClass employeeBirthdate = new SourceToDestinationClass(employeeTable, "employee_birthdate");

    public static SourceToDestinationClass scheduleId = new SourceToDestinationClass(scheduleTable, "schedule_id");
    public static SourceToDestinationClass scheduleCID = new SourceToDestinationClass(scheduleTable, "client_id");
    public static SourceToDestinationClass scheduleEID = new SourceToDestinationClass(scheduleTable, "employee_id");
    public static SourceToDestinationClass scheduleOverride = new SourceToDestinationClass(scheduleTable, "schedule_override");
    public static SourceToDestinationClass scheduleDate = new SourceToDestinationClass(scheduleTable, "schedule_date");
    public static SourceToDestinationClass scheduleStart = new SourceToDestinationClass(scheduleTable, "schedule_start");
    public static SourceToDestinationClass scheduleEnd = new SourceToDestinationClass(scheduleTable, "schedule_end");
    public static SourceToDestinationClass scheduleDay = new SourceToDestinationClass(scheduleTable, "schedule_day");
    public static SourceToDestinationClass scheduleWeek = new SourceToDestinationClass(scheduleTable, "schedule_week");
    public static SourceToDestinationClass scheduleType = new SourceToDestinationClass(scheduleTable, "schedule_type");
    public static SourceToDestinationClass scheduleMID = new SourceToDestinationClass(scheduleTable, "schedule_master_id");
    public static SourceToDestinationClass scheduleGroup = new SourceToDestinationClass(scheduleTable, "schedule_group");
    public static SourceToDestinationClass scheduleUpdated = new SourceToDestinationClass(scheduleTable, "schedule_last_updated");
    public static SourceToDestinationClass scheduleDeleted = new SourceToDestinationClass(scheduleTable, "schedule_is_deleted");
    public static SourceToDestinationClass schedulePay = new SourceToDestinationClass(scheduleTable, "schedule_pay_opt");
    public static SourceToDestinationClass scheduleBill = new SourceToDestinationClass(scheduleTable, "schedule_bill_opt");
    public static SourceToDestinationClass scheduleRate = new SourceToDestinationClass(scheduleTable, "rate_code_id");


    public GetDataFromSource(String sourceLocation) {
        initializeSource(sourceLocation);

        /**
         * Set client info
         */
        clientAddress.setSource(getClientAddress());
        clientAddress2.setSource(getClientAddress2());
        clientBill.setSource(getClientBill());
        clientBreak.setSource(getClientBreak());
        clientCity.setSource(getClientCity());
        clientDeleted.setSource(getClientDeleted());
        clientEnd.setSource(getClientEnd());
        clientFax.setSource(getClientFax());
        clientId.setSource(getClientId());
        clientManagement.setSource(getClientManagement());
        clientName.setSource(getClientName());
        clientPhone.setSource(getClientPhone());
        clientPhone2.setSource(getClientPhone2());
        clientRateCode.setSource(getClientRateCode());
        clientStart.setSource(getClientStart());
        clientState.setSource(getClientState());
        clientTraining.setSource(getClientTraining());
        clientType.setSource(getClientType());
        clientUpdated.setSource(getClientUpdated());
        clientWorksite.setSource(getClientWorksite());
        clientWorksiteOrder.setSource(getClientWorksiteOrder());
        clientZip.setSource(getClientZip());
        //companyName.setSource(getCompanyName(), clientFields);
        /**
         * End Client Info
         */

        /**
         * Start Employee Info
         */
        employeeAddress.setSource(getEmployeeAddress());
        employeeAddress2.setSource(getEmployeeAddress2());
        employeeBirthdate.setSource(getEmployeeBirthDate());
        employeeCell.setSource(getEmployeeCell());
        employeeCity.setSource(getEmployeeCity());
        employeeDeleted.setSource(getEmployeeDeleted());
        employeeEMail.setSource(getEmployeeEMail());
        employeeFName.setSource(getEmployeeFName());
        employeeHire.setSource(getEmployeeHire());
        employeeId.setSource(getEmployeeId());
        employeeLName.setSource(getEmployeeLName());
        employeeMName.setSource(getEmployeeMName());
        employeePager.setSource(getEmployeePager());
        employeePhone.setSource(getEmployeePhone());
        employeePhone2.setSource(getEmployeePhone2());
        employeeSSN.setSource(getEmployeeSSN());
        employeeState.setSource(getEmployeeState());
        employeeTerm.setSource(getEmployeeTerm());
        employeeType.setSource(getEmployeeType());
        employeeUpdated.setSource(getEmployeeUpdated());
        employeeZip.setSource(getEmployeeZip());
        /**
         * End Employee Info
         */

        /**
         * Start Schedule Info
         */
        scheduleBill.setSource(getScheduleBill());
        scheduleCID.setSource(getScheduleCID());
        scheduleDate.setSource(getScheduleDate());
        scheduleDay.setSource(getScheduleDay());
        scheduleDeleted.setSource(getScheduleDeleted());
        scheduleEID.setSource(getScheduleEID());
        scheduleEnd.setSource(getScheduleEnd());
        scheduleGroup.setSource(getScheduleGroup());
        scheduleId.setSource(getScheduleId());
        scheduleMID.setSource(getScheduleMID());
        scheduleOverride.setSource(getScheduleOverride());
        schedulePay.setSource(getSchedulePay());
        scheduleRate.setSource(getScheduleRate());
        scheduleStart.setSource(getScheduleStart());
        scheduleType.setSource(getScheduleType());
        scheduleUpdated.setSource(getScheduleUpdated());
        scheduleWeek.setSource(getScheduleWeek());
        /**
         * End Schedule Info
         */
    }

    protected SourceClass getCompanyName() {
        return new SourceClass();
    }

    protected SourceClass getClientId() {
        return new SourceClass();
    }

    protected SourceClass getClientManagement() {
        return new SourceClass();
    }

    protected SourceClass getClientWorksiteOrder() {
        return new SourceClass();
    }

    protected SourceClass getClientName() {
        return new SourceClass();
    }

    protected SourceClass getClientFax() {
        return new SourceClass();
    }

    protected SourceClass getClientPhone() {
        return new SourceClass();
    }

    protected SourceClass getClientPhone2() {
        return new SourceClass();
    }

    protected SourceClass getClientRateCode() {
        return new SourceClass();
    }

    protected SourceClass getClientBill() {
        return new SourceClass();
    }

    protected SourceClass getClientBreak() {
        return new SourceClass();
    }

    protected SourceClass getClientStart() {
        return new SourceClass();
    }

    protected SourceClass getClientEnd() {
        return new SourceClass();
    }

    protected SourceClass getClientDeleted() {
        return new SourceClass();
    }

    protected SourceClass getClientAddress() {
        return new SourceClass();
    }

    protected SourceClass getClientAddress2() {
        return new SourceClass();
    }

    protected SourceClass getClientCity() {
        return new SourceClass();
    }

    protected SourceClass getClientState() {
        return new SourceClass();
    }

    protected SourceClass getClientTraining() {
        return new SourceClass();
    }

    protected SourceClass getClientType() {
        return new SourceClass();
    }

    protected SourceClass getClientUpdated() {
        return new SourceClass();
    }

    protected SourceClass getClientWorksite() {
        return new SourceClass();
    }

    protected SourceClass getClientZip() {
        return new SourceClass();
    }


    protected SourceClass getEmployeeId() {
        return new SourceClass();
    }

    protected SourceClass getEmployeeAddress() {
        return new SourceClass();
    }

    protected SourceClass getEmployeeAddress2() {
        return new SourceClass();
    }

    protected SourceClass getEmployeeBirthDate() {
        return new SourceClass();
    }

    protected SourceClass getEmployeeCell() {
        return new SourceClass();
    }

    protected SourceClass getEmployeeCity() {
        return new SourceClass();
    }

    protected SourceClass getEmployeeDeleted() {
        return new SourceClass();
    }

    protected SourceClass getEmployeeEMail() {
        return new SourceClass();
    }

    protected SourceClass getEmployeeFName() {
        return new SourceClass();
    }

    protected SourceClass getEmployeeHire() {
        return new SourceClass();
    }

    protected SourceClass getEmployeeLName() {
        return new SourceClass();
    }

    protected SourceClass getEmployeeMName() {
        return new SourceClass();
    }

    protected SourceClass getEmployeePager() {
        return new SourceClass();
    }

    protected SourceClass getEmployeePhone() {
        return new SourceClass();
    }

    protected SourceClass getEmployeePhone2() {
        return new SourceClass();
    }

    protected SourceClass getEmployeeSSN() {
        return new SourceClass();
    }

    protected SourceClass getEmployeeState() {
        return new SourceClass();
    }

    protected SourceClass getEmployeeTerm() {
        return new SourceClass();
    }

    protected SourceClass getEmployeeType() {
        return new SourceClass();
    }

    protected SourceClass getEmployeeUpdated() {
        return new SourceClass();
    }

    protected SourceClass getEmployeeZip() {
        return new SourceClass();
    }

    protected SourceClass getScheduleBill() {
        return new SourceClass();
    }

    protected SourceClass getScheduleCID() {
        return new SourceClass();
    }

    protected SourceClass getScheduleDate() {
        return new SourceClass();
    }

    protected SourceClass getScheduleDay() {
        return new SourceClass();
    }

    protected SourceClass getScheduleDeleted() {
        return new SourceClass();
    }

    protected SourceClass getScheduleEID() {
        return new SourceClass();
    }

    protected SourceClass getScheduleEnd() {
        return new SourceClass();
    }

    protected SourceClass getScheduleGroup() {
        return new SourceClass();
    }

    protected SourceClass getScheduleId() {
        return new SourceClass();
    }

    protected SourceClass getScheduleMID() {
        return new SourceClass();
    }

    protected SourceClass getScheduleOverride() {
        return new SourceClass();
    }

    protected SourceClass getSchedulePay() {
        return new SourceClass();
    }

    protected SourceClass getScheduleRate() {
        return new SourceClass();
    }

    protected SourceClass getScheduleStart() {
        return new SourceClass();
    }

    protected SourceClass getScheduleType() {
        return new SourceClass();
    }

    protected SourceClass getScheduleUpdated() {
        return new SourceClass();
    }

    protected SourceClass getScheduleWeek() {
        return new SourceClass();
    }

    public abstract void initializeSource(String sourceLocation);

    public abstract TableClass getEmployeeTable();
    public abstract TableClass getScheduleTable();
    public abstract TableClass getClientTable();



}
