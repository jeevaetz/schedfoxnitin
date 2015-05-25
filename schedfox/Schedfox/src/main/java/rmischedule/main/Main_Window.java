/*
 * Main_Window.java
 *
 * Created on February 3, 2004, 8:22 AM
 */
package rmischedule.main;

import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischeduleserver.util.xprint.xPrintData;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Company;
import rmischedule.admin.newuser_alert.NewUser_Alert_Screen;
import com.creamtec.ajaxswing.AjaxSwingManager;
import com.creamtec.ajaxswing.core.ClientAgent;
import com.creamtec.ajaxswing.gui.AjaxSwingToolkit;
import com.inet.jortho.SpellChecker;
import java.util.logging.Level;
import java.util.logging.Logger;
import rmischedule.schedule.*;
import rmischedule.components.*;
import rmischedule.xprint.templates.genericreportcomponents.*;
import rmischedule.xprint.data.*;
import rmischedule.options.*;
import rmischedule.security.*;
import rmischedule.options.optionsgraphical.*;
import rmischedule.xprint.components.*;
import rmischedule.admin.*;
import rmischedule.schedfox.invoicing.*;
import rmischeduleserver.*;
import rmischeduleserver.mysqlconnectivity.queries.util.*;
import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.data_connection_types.*;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.*;
import rmischeduleserver.mysqlconnectivity.queries.reports.*;
import rmischeduleserver.mysqlconnectivity.queries.employee.*;
import rmischeduleserver.mysqlconnectivity.queries.client.*;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.*;
import java.net.*;
import java.text.*;
import java.rmi.Naming;
import java.sql.PreparedStatement;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import rmischedule.employee.*;
import rmischedule.client.*;
import rmischedule.xadmin.*;
import rmischedule.schedule.checkincheckout.CheckInButton;
import rmischedule.schedule.print.employeereports.*;
import rmischedule.schedule.print.phonelistreports.phonelistreport;
import rmischedule.data_connection.*;
import rmischedule.employee.security.EmployeeSecurityGroups;
import rmischedule.ireports.viewer.IReportViewer;
import rmischedule.login.*;
import rmischedule.messaging.components.MessagingFiltersWindow;
import rmischedule.messaging.xMessagingEdit;
import rmischedule.messagingboard.GenericMessageBoard;
import rmischedule.misc.*;
import rmischedule.schedule.components.DShift;
import rmischedule.EmailEmployeeSchedule.emailScheduleReports;
import rmischedule.admin.db_operations.Database_Operations;
import rmischedule.analytics.HistoricalHoursFrame;
import rmischedule.analytics.ProfitAnalysisFrame;
import rmischedule.client.constant_contact.CheckConstantContactThread;
import rmischedule.client.constant_contact.ConstantContact;
import rmischedule.client.login.ClientLogin;
import rmischedule.client.rate_increases.RateIncreaseFrame;
import rmischedule.client.search_client.AllClientSearch;
import rmischedule.commissions.SetupCommissionsDialog;
import rmischedule.employee.login.EmployeeLogin;
import rmischedule.employee.searchEmployee.EnterSSNSearch;
import rmischedule.equipment.EquipmentReturnWindow;
import rmischedule.equipment.ManageEquipmentDialog;
import rmischedule.event_log.EventLoggerInternalFrame;
import rmischedule.event_log.EventTypeContactDialog;
import rmischedule.importexportData.ExportDisplayOption;
import rmischedule.invoicing.InvoiceClientManagement;
import rmischedule.ireports.classes.PrintActiveEmps;
import rmischedule.ireports.classes.PrintClientBreakdown;
import rmischedule.ireports.classes.PrintCommission;
import rmischedule.ireports.classes.PrintCoporateCommUsageReport;
import rmischedule.ireports.classes.PrintDemographics;
import rmischedule.ireports.classes.PrintEmpCountReport;
import rmischedule.ireports.classes.PrintEmployeeReport;
import rmischedule.ireports.classes.PrintOverTime;
import rmischedule.ireports.classes.PrintOverUnder;
import rmischedule.login.SplashScreen;
import rmischeduleserver.control.model.ClientDBMapper;
import rmischeduleserver.control.model.ClientRateCodesDBMapper;
import rmischedule.main.CompanyBranding.LoginType;
import rmischedule.mapping.MappingFrame;
import rmischedule.messaging.client_email.views.Client_Email_Form;
import rmischedule.personnel.PersonnelChangeForm;
import rmischedule.print.DeductionReportFrame;
import rmischedule.print.ShowCertifications;
import rmischedule.print.ShowEmployeeTypes;
import rmischedule.problemsolver.SearchProblemSolverDialog;
import rmischedule.reports.email.controllers.EmailReportController;
import rmischedule.schedule.checkincheckout.process.LateCheckInProcessThread;
import rmischedule.timeoff.SetupTimeOffIntervalDialog;
import rmischeduleserver.control.*;
import rmischeduleserver.control.model.EmployeeDBMapper;
import schedfoxlib.model.CompanyInformationObj;
import rmischeduleserver.data_connection_types.SocketCommandStructure;
import schedfoxlib.model.Branch;
import schedfoxlib.model.Certification;
import schedfoxlib.model.Group;
import rmischeduleserver.mysqlconnectivity.queries.admin.get_company_view_options_query;
import rmischeduleserver.mysqlconnectivity.queries.user.get_user_groups_query;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;

/**
 * This file is pretty ugly. There's a lot of miscellaneous junk scattered
 * throughout... Lots of static/global variables, and quite a bit of the report
 * stuff is in here as well for some reason. --Shawn
 *
 * @author jason.allen
 */
/**
 * *****************************************************************************
 * THROUGHOUT THIS PROGRAM THE FOLLOWING WILL HOLD TRUE 1 = Monday... to ... 7 =
 * Sunday
 *
 ******************************************************************************
 */
public class Main_Window extends JFrame implements ActionListener, CompanyBranchMenuInterface, CompanyMenuInterface {

    private static boolean useLocalConnection = true;
    public static int mySocketThreadNumber;
    public static Main_Window parentOfApplication;
    public static CompanyBranding compBranding;
    public static Schedule_Main_Form myScheduleForm;
    private static xClientEdit clientEditWindow;
    private static xEmployeeEdit employeeEditWindow;
    private static SchedFoxCustForm pricingForm;
    private static SchedFoxInvoices invoiceForm;
    public static optionWindow myOptionsWindow;
    public static security_detail mySecurity;
    public static xGroups groupEditWindow;
    private static Connected_Users userListWindow;
    private static EmployeeSecurityGroups employeeSecurityGroup;
    private static xUsersForm userEditWindow;
    public static xManagementForm manageEditWindow;
    public static rmischedule.schedule.components.openshifts.OpenShiftAlert myOpenShiftAlertWindow;
    private static RMIScheduleServerImpl myStaticServer;
    public static xMessagingEdit messagingEditWindow;
    public static EquipmentReturnWindow equipmentReturnWindow;
    private static Connected_Users connectedUsersWindow;
    public static MessagingFiltersWindow sortParametersWindow;
    private static NewUser_Alert_Screen emailAlertWindow;
    public static Employee_View_Additional_Fields_Files additionalFieldsFilesWindow;
    private static Database_Operations dbOperationsForm;
    private static Client_Email_Form clientEmailForm;
    private ArrayList<Integer> accessibleClients;
    /**
     * Avail Shit!
     */
    public final static int AVAILABLE = 0;
    public final static int NON_AVAILABLE = 1;
    public final static int NON_AVAILABLE_VACATION = 2;
    public final static int NON_AVAILABLE_PERSONAL = 3;
    public final static int NON_AVAILABLE_SICK = 4;
    public final static int NON_AVAILABLE_MILITARY = 5;
    public final static int NON_AVAILABLE_HALF_VACATION = 5;
    public final static int NON_AVAILABLE_HALF_PERSONAL = 6;
    /**
     * 6 hour buffer till someone is avail *
     */
    public final static int AMOUNT_TO_BUFFER_AVAILABILITY = 360;
    public static Integer AMOUNT_TO_CHECKIN_BUFFER = 320;
    /**
     * Rate code screen.
     */
    public static rmischedule.rate_codes.rate_code_edit rateCodeEditWindow;
    public static String LOCATION_OF_HELP_FILES = "http://www.schedfox.com/support.html";
    public static String LOCATION_OF_TEXTING_CHANGE = "http://www.schedfox.com/employee/MainPage3.php";
    public static String LOCATION_OF_PERSONELL_CHANGE = "http://www.schedfox.com/employee/employee_change.php";
    public static String LOCATION_OF_CORP_COMM = "http://www.schedfox.com/employee/corporate_communicator.php";
    public static OptionsDataClass newOptions;
    private static JOptionPane myConfirmPermChange = new JOptionPane("Make change permanent?", JOptionPane.YES_NO_OPTION);
    public static final Font myShiftTotalFont = new Font("Dialog", Font.BOLD, 12);
    public static ImageIcon Yellow_16_Icon;
    public static ImageIcon Green_16_Icon;
    public static ImageIcon Red_16_Icon;
    public static ImageIcon Load_Image;
    public static ImageIcon Red_Bullet_Icon;
    public static ImageIcon Green_Bullet_Icon;
    public static ImageIcon Yellow_Bullet_Icon;
    public static ImageIcon Blue_Bullet_Icon;
    public static ImageIcon Devil_Icon;
    public static ImageIcon House_Icon;
    public static ImageIcon Mobile_Phone_Icon;
    public static ImageIcon Stop_Watch_Icon;
    public static ImageIcon Yellow_Warning_Icon;
    public static ImageIcon Blue_Warning_Icon;
    public static ImageIcon Red_Warning_Icon;
    public static ImageIcon Blue_Information_Icon;
    public static ImageIcon Alert_Animated_Icon;
    public static ImageIcon Shift_Training_Icon;
    public static ImageIcon Calendar_Up_Icon;
    public static ImageIcon Calendar_Down_Icon;
    public static ImageIcon Index_Icon;
    public static ImageIcon Large_Error_Icon;
    public static ImageIcon Medium_Error_Icon;
    public static ImageIcon Lock_Time_Icon;
    public static ImageIcon Id_Cards_Icon;
    public static ImageIcon Note_Icon;
    public static ImageIcon Shield_Icon;
    public static ImageIcon User3_Icon;
    public static ImageIcon Reconcile_Icon;
    public static ImageIcon Mail_Schedule_Icon;
    public static ImageIcon Lock_Icon;
    public static ImageIcon Books_Icon;
    public static ImageIcon All_Users_Icon;
    public static ImageIcon Active_Users_Icon;
    public static ImageIcon Note_Edit_Icon;
    public static ImageIcon Note_Pinned_Icon;
    public static ImageIcon Close_Window_Icon;
    public static ImageIcon Selection_Delete_Icon;
    public static ImageIcon Find_Icon;
    public static ImageIcon Unfaded_Warning_Large_Icon;
    public static ImageIcon Warning_Large_Icon;
    public static ImageIcon Mobile_Phone_3_Icon;
    public static ImageIcon Printer_View_Icon;
    public static ImageIcon Printer_Icon;
    public static ImageIcon Ok_Icon;
    public static ImageIcon Navigate_Left_Icon;
    public static ImageIcon Navigate_Right_Icon;
    public static ImageIcon Navigate_Left_2_Icon;
    public static ImageIcon Navigate_Right_2_Icon;
    public static ImageIcon Component_Icon_Green;
    public static ImageIcon Green_Pin_Icon;
    public static ImageIcon Yellow_Pin_Icon;
    public static ImageIcon Red_Pin_Icon;
    public static ImageIcon Blue_Pin_Icon;
    public static ImageIcon Grey_Pin_Icon;
    public static ImageIcon Time_Sheet_Icon;
    public static ImageIcon Time_Sheet_Over_Icon;
    public static ImageIcon Time_Sheet_Check_Icon;
    public static ImageIcon myBackground;
    public static ImageIcon Close_Notes_Icon;
    public static ImageIcon Shift_Training_No_Trainer_Icon;
    public static ImageIcon Schedule_No_Training_Icon;
    public static ImageIcon Employee_Print_Icon;
    public static ImageIcon House_Meeting_Icon;
    public static ImageIcon Wrench_Icon;
    public static ImageIcon Shift_Viewed_Icon;
    public static ImageIcon Warning32x32;
    public static ImageIcon RecycleRefreshIcon;
    public static ImageIcon CheckInPrinterIcon;
    public static ImageIcon HelpIcon;
    public static ImageIcon FilterUsers;
    public static ImageIcon ClockIcon;
    public static ImageIcon ReplaceIcon;
    public static ImageIcon ViewOpenShiftsIcon;
    public static ImageIcon ViewAllShiftsIcon;
    public static ImageIcon PrintForToolbarIcon;
    public static ImageIcon AvailabilityIcon;
    public static ImageIcon SelectAllIcon;
    public static ImageIcon DeselectAllIcon;
    public static ImageIcon EmployeeListIcon;
    public static ImageIcon ConsolidateIcon;
    public static ImageIcon ShiftAlertIcon;
    public static ImageIcon ViewOpenShifts;
    public static ImageIcon ViewAllShifts;
    public static ImageIcon CheckInButtonIcon;
    public static ImageIcon Deleted_Shift_Icon;
    public static ImageIcon Find_Next_Icon;
    public static ImageIcon Find_Previous_Icon;
    public static ImageIcon Viewing_All_Employees;
    public static ImageIcon Viewing_Active_Employees;
    public static ImageIcon Viewing_All_Clients;
    public static ImageIcon Viewing_Active_Clients;
    public static ImageIcon Save_Notes_Icon;
    public static ImageIcon Edit_Notes_Icon;
    public static ImageIcon Connected_Icon;
    public static ImageIcon Connected_Full_Icon;
    public static ImageIcon Disconnected_Icon;
    public static ImageIcon Disconnected_Full_Icon;
    public static ImageIcon Save_Data_24x24;
    public static ImageIcon Add_Data_24x24;
    public static ImageIcon Delete_Data_24x24;
    public static ImageIcon Help_32x32_Icon;
    public static ImageIcon Sic_Icon;
    public static ImageIcon Sic_Half_Icon;
    public static ImageIcon Sic_Half_Icon_Faded;
    public static ImageIcon Vac_Icon;
    public static ImageIcon Vac_Half_Icon;
    public static ImageIcon Vac_Half_Icon_Faded;
    public static ImageIcon Na_Icon;
    public static ImageIcon Personal_Icon;
    public static ImageIcon Personal_Half_Icon;
    public static ImageIcon Personal_Half_Icon_Faded;
    public static ImageIcon Garbage_Full_16x16;
    public static ImageIcon Find_Next32x32;
    public static ImageIcon Military_Icon;
    public static ImageIcon Generic_NA_Icon;
    public static ImageIcon MapIcon;
    public static ImageIcon Personal_Icon_Faded;
    public static ImageIcon Ok_Yellow;
    public static ImageIcon Sic_Icon_Faded;
    public static ImageIcon Vac_Icon_Faded;
    public static ImageIcon Na_Icon_Faded;
    public static ImageIcon Military_Icon_Faded;
    public static ImageIcon Generic_NA_Icon_Faded;
    public static ImageIcon Exit_Form_Icon;
    public static ImageIcon Arrow_Down_Icon;
    public static ImageIcon Arrow_Up_Icon;
    public static ImageIcon Zoom_In_Shift_Icon;
    public static ImageIcon Zoom_Out_Shift_Icon;
    public static ImageIcon NavDown16x16_Icon;
    public static ImageIcon NavUp16x16_Icon;
    public static ImageIcon BulletYellow16x16Icon;
    public static ImageIcon BulletGreen16x16Icon;
    public static ImageIcon BulletRed16x16Icon;
    public static ImageIcon LetterHead;
    public static ImageIcon YellowPin24x24;
    public static ImageIcon Note16x16;
    public static ImageIcon First16x16;
    public static ImageIcon Prev16x16;
    public static ImageIcon Next16x16;
    public static ImageIcon Last16x16;
    public static ImageIcon ClientLogin;
    public static ImageIcon Emp_Approval_Icon;
    public static ImageIcon Save_Message_Icon_36x36;
    public static ImageIcon Send_Message_Icon_36x36;
    public static ImageIcon Reset_Message_Icon_36x36;
    public static ImageIcon Exit_Message_Icon_36x36;
    public static ImageIcon Employee_Message_Icon_227x23;
    public static ImageIcon New_User_Icon_Aero_32x32px;
    public static ImageIcon Save_User_Icon_Aero_32x32px;
    public static ImageIcon Save_User_Icon_24x24px;
    public static ImageIcon Remove_User_Icon_Aero_32x32px;
    public static ImageIcon Exit_Icon_Aero_32x32px;
    public static ImageIcon New_User_Alert_Panel;
    public static ImageIcon WindowsEdit24x24;
    public static ImageIcon Delete24x24;
    public static ImageIcon Save_Schema_16x16_px;
    public static ImageIcon Remove_Schema_16x16_px;
    public static ImageIcon Restore_Schema_16x16_px;
    public static ImageIcon Active_Schema_16x16_px;
    public static ImageIcon Inactive_Schema_16x16_px;
    public static ImageIcon Download_Image_24x24_px;
    public static ImageIcon starIcon;
    public static ImageIcon disabledStarIcon;
    public static ImageIcon starIcon_24;
    public static ImageIcon disabledStarIcon_24;
    public static ImageIcon Edit_Template_Diag_Save_16x16_px;
    public static ImageIcon Edit_Template_Diag_Exit_16x16_px;
    public static ImageIcon Edit_Template_Diag_Delete_16x16_px;
    public static ImageIcon Problemsolver_Email_Dialog_Send_16x16_px;
    public static ImageIcon Problemsolver_Email_Dialog_Exit_16x16_px;
    public static ImageIcon Client_Email_AddressBook_Add_16x16_px;
    public static ImageIcon Client_Email_AddressBook_Finished_16x16_px;
    public static ImageIcon SchedFoxIcon;
    public static ImageIcon EmailReport_RunReport_16x16px;
    public static ImageIcon E24x24EMAILSEND;
    public static Color redInputColor = new Color(255, 191, 191);
    public static final String CLIENT_EDIT_ACTION = "ClientEdit";
    public static final String CLIENT_SEARCH = "ClientSearch";
    public static final String CLIENT_CALL_QUEUE = "ClientCallQueue";
    public static final String PROBLEM_SEARCH = "ProblemSearch";
    public static final String CLIENT_RATE_INCREASE = "ClientRateIncrease";
    public static final String EMPLOYEE_EDIT_ACTION = "EmployeeEdit";
    public static final String EMPLOYEE_APPROVAL_ACTION = "EmployeeApproval";
    public static final String EMPLOYEE_SEARCH_ACTION = "EmployeeSearch";
    public static final String SCHEDULE_EDIT_ACTION = "ScheduleEdit";
    public static final String PRINT_PHONE_REPORT = "PhoneReport";
    public static final String EMAIL_SCHEDULE = "EmailSchedule";
    public static final String HEALTHCARE_OPTIONS = "HealthCareOption";
    public static final String PRINT_TRAIN_REPORT = "TrainReport";
    public static final String PRINT_CONTRACT_RENEWAL_REPORT = "ContractRenewalReport";
    public static final String PRINT_LOCATION_REPORT = "LocationReport";
    public static final String PRINT_UNCON_REPORT = "UnconReport";
    public static final String PRINT_OPEN_REPORT = "OpenReport";
    public static final String PRINT_EMP_REPORT = "EmployeeReport";
    public static final String PRINT_DEDUCTION_REPORT = "EmployeeDeductionREport";
    public static final String PRINT_OVER_UNDER_REPORT = "PrintOverUnder";
    public static final String PRINT_TIME_OFF_REPORT = "PrintTimeOffReport";
    public static final String PRINT_ACTIVE_EMPS_REPORT = "PrintActiveEmps";
    public static final String PRINT_EMP_TERMINATION_REPORT = "PrintTerminationReport";
    public static final String PRINT_HIRED_EMP = "PrintHiredEmp";
    public static final String PRINT_CERTIFICAION_EXP_REPORT = "PrintCertExpReport";
    public static final String PRINT_CERTIFICAION_MISSING_REPORT = "PrintCertMissingReport";
    public static final String PRINT_CALLING_QUEUE = "PrintCallingQueue";
    public static final String PRINT_SALES_REPORT = "PrintSalesReport";
    public static final String PRINT_CORP_CALLING_QUEUE = "PrintCorpCallingQueue";
    public static final String PRINT_CORP_SUMMARY_REPORT = "PrintCorpSummaryReport";
    public static final String PRINT_CC_TABLE_REPORT = "PrintCCTableReport";
    public static final String PRINT_DEMOGRAPHICS = "PrintDemographics";
    public static final String PRINT_TERMINATED_EMPS = "PrintTerminatedEmployees";
    public static final String PRINT_CROSS_EMPS = "PrintCrossEmps";
    public static final String PRINT_PERSONNEL_REPORT = "PrintPersonnelReport";
    public static final String PRINT_EMPLOYEE_HIRED_REPORT = "PrintHiredEmployeeReport";
    public static final String PRINT_FAILED_COMMUNICATION = "PrintFailedCommunication";
    public static final String PRINT_EMPLOYEE_TYPE_REPORT = "PrintEmployeeTypeReport";
    public static final String PRINT_EMP_ROLLCALL_REPORT = "PrintRollCall";
    public static final String PRINT_TOTALS_BY_LOCATION_REPORT = "PrintTotalsLocation";
    public static final String PRINT_EXTENDED_EMP_ROLLCALL_REPORT = "PrintExtendedEmps";
    public static final String PRINT_OVERTIME_REPORT = "PrintOvertimeReport";
    public static final String PRINT_CHECKIN_REPORT = "PrintCheckInReport";
    public static final String PRINT_CLIENT_PAYROLL = "PrintClientPayroll";
    public static final String PRINT_OVERTIME_SUMMARY_REPORT = "PrintOverTimeReport";
    public static final String PRINT_CENSUS_REPORT = "PrintCensusReport";
    public static final String PRINT_BRANCH_PAYROLL_REPORT = "PrintBranchPayroll";
    public static final String CLIENT_BREAKDOWN_REPORT = "ClientBreakdownReport";
    public static final String ACTIVE_EMP_COUNT_REPORT = "PrintEmpCountReport";
    public static final String PRINT_RATING_REPORT = "PrintRatingReport";
    public static final String CORPORATE_COMM_USAGE_REPORT = "CorporateCommUsageReport";
    public static final String PRINT_CERT_REPORT = "PrintCertReport";
    public static final String PRINT_CORP_REPORT = "PrintCorpReport";
    public static final String PRINT_LOG_REPORT = "PrintLogReport";
    public static final String NEW_EMPLOYEE_LOW_ALERT = "NewEmployeeAlert";
    public static final String EXPORT_EMPLOYEE_SCHEDULE = "ExportEmployeeSchedules";
    public static final String MESSAGING_ACTION = "Messaging";
    public static final String PERSONNEL_FORM = "PersonnelForm";
    public static final String EXPORT_GOVT_DATA = "ExportGovtData";
    public static final String HOUR_BREAKDOWN_REPORT = "HourBreakdown";
    public static final String PRINT_COMMISSION_REPORT = "Print_Comm_Report";
    public static final String ALL_EMPLOYEES_MESSAGING_ACTION = "MessageAllEmployees";
    public static final String ADD_EVENT = "AddEvent";
    public static final String IMPORT_EMPLOYEE_DATA = "Import Emp Data";
    public static final String EXPORT_EMPLOYEE_DATA = "Export Emp Data";
    public static final String IMPORT_CLIENT_DATA = "Import Cli Data";
    public static final String EXPORT_CLIENT_DATA = "Export Cli Data";
    public static final String RETURN_EMP_EQUIPMENT = "Return Emp Equipment";
    public static final String EXPORT_CLIENT_RATECODES_DATA = "Export Rate Cli Data";
    public static final String NOTE_TYPE_REPORT = "Note type report";
    public static final String BILLING_INFO = "Billling Info";
    public static final String COMPANY_EDIT = "Company Information";
    public static final String ANALYTICS_HISTORICAL_HOURLY = "Historical Hourly";
    public static final String ANALYTICS_PROFIT_ANALYSIS = "Profit Analysis";
    public static Font client_font = new Font("Tahoma", Font.BOLD, 16);
    // public static Font shift_font = new Font("Calibri",  Font.PLAIN, new Double(13.0).intValue());//current
    public static Font shift_font = new Font("Verdana", Font.BOLD, new Double(11.0).intValue());
    public static Font time_font = new Font("Dialog", Font.PLAIN, new Double(11.0).intValue());
    public static Font shift_totals_font = new Font("Verdana", Font.BOLD, new Double(11.0).intValue());
    public static Font employee_font = new Font("Dialog", Font.BOLD, 13);
    public static Font employee_info_font = new Font("Arial", Font.BOLD, new Double(10.0).intValue());
    public static Font header_font = AjaxSwingManager.isAjaxSwingRunning() ? new Font(null, Font.PLAIN, 20) : new Font("Tahoma", Font.BOLD, 13);//mon tue:Days
    public static Font date_font = new Font(AjaxSwingManager.isAjaxSwingRunning() ? null : "Calibri", Font.PLAIN, 13);//dates
    public static Font totals_font = new Font(null, Font.BOLD, 10);//total font
    private static GenericMessageBoard messageBoard;
    private JMenu scheduleAddEdit;

    public static Vector<Company> getActiveListOfCompanies() {
        return Main_Window.parentOfApplication.ActiveListOfCompanies;
    }
    public String myManagementDatabase;
    public myDesktopPane desktop;
    private String base;
    private String user;
    public rmischedule.security.User myUser;
    private rmischedule.login.Login_Frame loginFrame;
    public rmischedule.login.Login_Wait lw;
    public rmischedule.components.Options options;
    private Jar_Images ji;
    private int bufferValue;
    private String myManagementId;
    public static rmischedule.login.SplashScreen mySplash;
    public static rmischedule.schedule.checkincheckout.CheckInCheckOutWindow ciw;
    private CheckInButton cib;
    private boolean checkInAlert;
    private static int loadProgress;
    private static String loadText;
    public static xSocketConnection mySocketConn = null;
//    public static final Color myBackgroundColor = new Color(150,56,22);
    public static final Color myBackgroundColor = new Color(0, 9, 234);
//    public static final Color myBackgroundColor = new Color(40,40,90);    
    public static boolean isClosing = false;
    public static String[] lastColumnsForGenericReport;
    //Accessible Company and Branch Information
    private Vector companyId;
    private Vector companyName;
    private Vector branchId;
    private Vector branchName;
    private static Vector<Company> ActiveListOfCompanies;
    private JMenu windowMenu;
    private ComponentListener frameHideShowListener;
    private InternalFrameListener frameCloseListener;
    private String[] argument1;
    //IMPORTANT USED TO QUEUE UP ALL QUERY HEARTBEAT INFORMATION SO IT CAN BE RESENT IF NEEDED....
    private static ClientConnection mySavedQueryInformation;
    private static Object terminateLock = new Object();
    public static int myBasicSizeInteger = 5;
    public static String gqfId = "none";
    private int THREAD_COUNT = 25;
    private final ThreadPoolExecutor pool
            = new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT, 120, TimeUnit.SECONDS, new LinkedBlockingQueue());
    public String loginType;
    public String companyDB;
    public Boolean showRateCodes;
    /*  additions by Jeffrey Davis on 04/21/2011 for EmailReportSystem  */
    private EmailReportController emailReportController = EmailReportController.getInstance();
    /*  additions by Jeffrey Davis on 04/21/2011 for EmailReportSystem complete */

    public static void main(String args[]) {
        final String[] arguments = args;

        Main_Window main = new Main_Window(arguments);
        if (AjaxSwingManager.isAjaxSwingRunning()) {
            main.setVisible(true);
        }
        SplashScreen splash = new SplashScreen(main);
        splash.startMe();
        splash.setVisible(true);

    }

    public Main_Window(String args[]) {
        parentOfApplication = this;
        mySocketThreadNumber = 0;

        loadText = new String("Initializing SchedFox");

        ControllerRegistryAbstract.setBillingController(BillingController.class);
        ControllerRegistryAbstract.setClientContractController(ClientContractController.class);
        ControllerRegistryAbstract.setClientController(ClientController.class);
        ControllerRegistryAbstract.setCompanyController(CompanyController.class);
        ControllerRegistryAbstract.setEmployeeController(EmployeeController.class);
        ControllerRegistryAbstract.setGenericController(GenericController.class);
        ControllerRegistryAbstract.setProblemSolverInterface(ProblemSolverController.class);
        ControllerRegistryAbstract.setTimeOffController(TimeOffController.class);
        ControllerRegistryAbstract.setUserController(UserController.class);
        ControllerRegistryAbstract.setIncidentReportInterface(IncidentReportController.class);
        ControllerRegistryAbstract.setHealthInterface(HealthCareController.class);
        ControllerRegistryAbstract.setOfficerDailyReportInterface(OfficerDailyReportController.class);

        try {
            if (AjaxSwingManager.isAjaxSwingRunning()) {
                this.setUndecorated(true);
            } else {
                for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            }
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }

        loadProgress = 0;
        compBranding = new CompanyBranding();

        loginType = "User";
        String clientUrl = "";
        if (AjaxSwingManager.isAjaxSwingRunning()) {
            try {
                if (ClientAgent.getCurrentInstance().getRequestData().getParams().get("company") != null
                        && ClientAgent.getCurrentInstance().getRequestData().getParams().get("company").toString().trim().length() > 0) {
                    String companyDB = (String) ClientAgent.getCurrentInstance().getRequestData().getParams().get("company");
                    loginType = (String) ClientAgent.getCurrentInstance().getRequestData().getParams().get("login_type");
                    try {
                        clientUrl = (String) ClientAgent.getCurrentInstance().getRequestData().getParams().get("clientUrl");
                    } catch (Exception e) {
                    }
                    compBranding.loadUpCompanyInfo(companyDB, clientUrl);
                }
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        } else {
            try {
                companyDB = args[0];
                loginType = args[1];
                try {
                    clientUrl = args[2];
                } catch (Exception e) {
                }
                compBranding.loadUpCompanyInfo(companyDB, clientUrl);
            } catch (Exception e) {
            }
        }

        compBranding.setLoginInfo(loginType);

        desktop = new myDesktopPane(this);
        desktop.setLayout(null);
        getContentPane().add(desktop);

        setSize(new Dimension(700, 450));
        setExtendedState(MAXIMIZED_BOTH);
        checkInAlert = false;
        desktop.setBackground(myBackgroundColor);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                Main_Window.terminateSchedFox();
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }
        });

        /*
         * Some event listeners and other junk to help manage our Windows drop down menu
         */
        this.windowMenu = new JMenu("Windows");

        this.desktop.addContainerListener(new ContainerListener() {
            public void componentAdded(ContainerEvent e) {
                desktopComponentAdded(e);
            }

            public void componentRemoved(ContainerEvent e) {
            }
        });

        this.frameHideShowListener = new ComponentListener() {
            public void componentHidden(ComponentEvent e) {
                internalFrameHidden(e);
            }

            public void componentShown(ComponentEvent e) {
                internalFrameShown(e);
            }

            public void componentMoved(ComponentEvent e) {
            }

            public void componentResized(ComponentEvent e) {
            }
        };

        this.frameCloseListener = new InternalFrameListener() {
            public void internalFrameClosed(InternalFrameEvent e) {
                internalFrameDisposed(e);
            }

            public void internalFrameActivated(InternalFrameEvent e) {
            }

            public void internalFrameClosing(InternalFrameEvent e) {
            }

            public void internalFrameDeactivated(InternalFrameEvent e) {
            }

            public void internalFrameDeiconified(InternalFrameEvent e) {
            }

            public void internalFrameIconified(InternalFrameEvent e) {
            }

            public void internalFrameOpened(InternalFrameEvent e) {
            }
        };

        try {
            ji = new Jar_Images(this);
            myBackground = loadSingleImage("images/background.png", .9, false);
        } catch (Exception e) {
        }
        argument1 = args;
        myManagementDatabase = "SchedData";

        Thread loadThread = new Thread() {
            public void run() {
                if (!AjaxSwingManager.isAjaxSwingRunning()) {
                    while (mySplash == null || !mySplash.isVisible()) {
                        try {
                            Thread.sleep(50);
                        } catch (Exception ex) {
                        }
                    }
                }
                Main_Window.getServer();
                loadAllImages();
                loadDictionariesForSpellChecker();
                finishLoading();
                if (loginType.equalsIgnoreCase("NewEmployee")) {
                    newEmployeeAdd(compBranding.getCompany().getId(), loginType);
                }
            }
        };
        if (!AjaxSwingManager.isAjaxSwingRunning()) {
            loadThread.start();
        } else {
            loadThread.run();
        }

    }

    public void newEmployeeAdd(String Company, String loginType) {

        try {
            Main_Window.setWaitCursor(true);
            getEmployeeEditWindow(loginType).setInformation(loginType, Company, "0", null);
            getEmployeeEditWindow().setVisible(true);
            getEmployeeEditWindow().setIcon(false);
            getEmployeeEditWindow().setSelected(true);
        } catch (Exception ex) {
        } finally {
            Main_Window.setWaitCursor(false);
        }

    }

    public void finishLoading() {

        myManagementId = new String();
        myScheduleForm = new Schedule_Main_Form();
        desktop.add(myScheduleForm);
        loadProgress += 10;
        Thread.yield();
        Connection myConn = new Connection();
        StaticDateTimeFunctions.setClientServerTimeZone(myConn.getServerTimeZone(), myConn.getServerTimeMillis());
        myOpenShiftAlertWindow = new rmischedule.schedule.components.openshifts.OpenShiftAlert();
        getAccessibleBranches();
        getAccessibleCompanies();
        desktop.setOpaque(true);
        loadProgress += 10;

        LateCheckInProcessThread thread = new LateCheckInProcessThread();
        thread.start();
        
        CheckConstantContactThread constantThread = new CheckConstantContactThread();
        constantThread.start();
    }

    public ThreadPoolExecutor getThreadPool() {
        return this.pool;
    }
    
    public int getNumberOfDaysForDM(String companyId) {
        try {
            Hashtable<String, Vector<CompanyInformationObj>> companyViewOptions =
                Main_Window.parentOfApplication.getCompanyViewOptions(companyId);
            return Integer.parseInt(Main_Window.parentOfApplication.getCompanyInformation(companyViewOptions, CompanyLoginInformation.CALL_QUEUE_DM_ROTATION).getOption_value());
        } catch (Exception e) {
            return 7;
        }
    }

    public int getNumberOfDaysForCorporateUser(String companyId) {
        try {
            Hashtable<String, Vector<CompanyInformationObj>> companyViewOptions =
                Main_Window.parentOfApplication.getCompanyViewOptions(companyId);
            return Integer.parseInt(Main_Window.parentOfApplication.getCompanyInformation(companyViewOptions, CompanyLoginInformation.CALL_QUEUE_CORP_ROTATION).getOption_value());
        } catch (Exception e) {
            return 21;
        }
    }

    /**
     * Anytime you want to close the program you should use one of these two
     * methods. This prevents multiple error dialogs from popping up when
     * something goes wrong.
     */
    public static void terminateSchedFox() {
        synchronized (Main_Window.terminateLock) {
            System.exit(1);
        }
    }

    public static void terminateSchedFox(String message, String title) {
        synchronized (Main_Window.terminateLock) {
            Main_Window.isClosing = true;
            Thread[] allThreads = new Thread[Thread.activeCount() * 2];
            Thread thisThread = Thread.currentThread();
            thisThread.setPriority(Thread.MAX_PRIORITY);
            int numThreads = Thread.enumerate(allThreads);

            for (int i = 0; i < numThreads && i < allThreads.length; i++) {
                if (allThreads[i].getId() != thisThread.getId()) {
                    allThreads[i].setPriority(Thread.MIN_PRIORITY);
                    allThreads[i].yield();
                }
            }

            while (!Main_Window.parentOfApplication.isVisible()) {
                try {
                    Main_Window.parentOfApplication.setVisible(true);
                } catch (Exception ex) {
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }

            if (Main_Window.parentOfApplication.mySocketConn != null) {
                Main_Window.parentOfApplication.mySocketConn.dispose();
            }

            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, message, title, JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public int getBasicSize() {
        return myBasicSizeInteger;
    }

    /**
     * Threads off the loading of the spell checker dictionary!
     */
    public void loadDictionariesForSpellChecker() {
        try {
            new Thread(new Runnable() {
                public void run() {
                    URL url = this.getClass().getResource("/rmischedule/dictionary_files/");
                    SpellChecker.registerDictionaries(url, "en", "en");
                }
            }).start();
        } catch (Exception e) {
            System.out.println("PROBLEM LOADING SPELL CHECKING DICTIONARY!");
        }
    }

    public boolean is12HourFormat() {
        try {
            if (Main_Window.isEmployeeLoggedIn()) {
                return false;
            }
            return ((Boolean) newOptions.getOptionByName(newOptions.is12Hour).read()).booleanValue();
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Returns how many weeks in past this user has flagged as loading by
     * default
     */
    public int getNumberOfWeeksToLoadInPast(String companyId) {
        try {
            if (Main_Window.isEmployeeLoggedIn()) {
                Hashtable<String, Vector<CompanyInformationObj>> companyViewOptions
                        = Main_Window.parentOfApplication.getCompanyViewOptions(companyId);
                CompanyInformationObj dynamicObj
                        = Main_Window.parentOfApplication.getCompanyInformation(companyViewOptions, CompanyLoginInformation.WEEKS_IN_PAST_TO_LOAD_FOR_EMP);
                return Integer.parseInt(dynamicObj.getOption_value());
            } else {
                return ((Integer) newOptions.getOptionByName(newOptions.numweeksinpast).read()).intValue();
            }
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * Shows the rate codes.
     *
     * @return
     */
    public Boolean getShowRateCodes() {
        if (showRateCodes == null) {
            showRateCodes = ((Boolean) newOptions.getOptionByName(newOptions.showratecodes).read()).booleanValue();
        }
        return showRateCodes;
    }

    /**
     * Returns how many weeks in future this user has flagged as loading by
     * default
     */
    public int getNumberOfWeeksToLoadInFuture(String companyId) {
        try {
            if (Main_Window.isEmployeeLoggedIn()) {
                Hashtable<String, Vector<CompanyInformationObj>> companyViewOptions
                        = Main_Window.parentOfApplication.getCompanyViewOptions(companyId);
                CompanyInformationObj dynamicObj
                        = Main_Window.parentOfApplication.getCompanyInformation(companyViewOptions, CompanyLoginInformation.DAY_OF_WEEK_WHERE_NEXT_WEEK_VISIBLE);
                Connection myConn = new Connection();
                Calendar getDate = Calendar.getInstance();
                getDate.setTimeInMillis(myConn.getServerTimeMillis());
                if (getDate.get(Calendar.DAY_OF_WEEK) > Integer.parseInt(dynamicObj.getOption_value())) {
                    return 1;
                } else if (getDate.get(Calendar.DAY_OF_WEEK) == Integer.parseInt(dynamicObj.getOption_value())) {
                    CompanyInformationObj hourObj
                            = Main_Window.parentOfApplication.getCompanyInformation(companyViewOptions, CompanyLoginInformation.TIME_DAY_OF_WEEK_WHERE_NEXT_WEEK_VISIBLE);
                    if (getDate.get(Calendar.HOUR_OF_DAY) >= Integer.parseInt(hourObj.getOption_value())) {
                        return 1;
                    }
                }
                return 0;
            } else {
                return ((Integer) newOptions.getOptionByName(newOptions.numweeksinfuture).read()).intValue();
            }
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * Returns the number of hours (From options) needed to not mark shifts as
     * conflicted and avail runs off of
     */
    public int getNumberOfHoursBetweenShifts() {
        try {
            return ((Integer) newOptions.getOptionByName(newOptions.hoursforConflict).read()).intValue() * 60;
        } catch (Exception e) {
            return 4;
        }
    }

    /**
     * Mark uncon shifts yellow
     */
    public boolean markUnconShifts() {
        try {
            return ((Boolean) newOptions.getOptionByName(newOptions.markUncon).read()).booleanValue();
        } catch (Exception e) {
            return false;
        }
    }

    public int getNumberOfDaysNewEmployee() {
        try {
            return ((Integer) newOptions.getOptionByName(newOptions.numHoursNewEmp).read());
        } catch (Exception e) {
            return 1;
        }
    }

    public boolean showTrainingForClients() {
        return ((Boolean) newOptions.getOptionByName(newOptions.showTrain).read());
    }

    public boolean showRatesForClients() {
        return ((Boolean) newOptions.getOptionByName(newOptions.showRate).read());
    }

    public boolean showManagementForClients() {
        return ((Boolean) newOptions.getOptionByName(newOptions.showManagement).read());
    }

    /**
     * Should only be used by our splash screen to indicate what schedfox is
     * currently doing...
     */
    public String getTextForLoadingProgress() {
        return loadText;
    }

    /**
     * Should only be used by our splash screen to indicate how far along our
     * load is going returns int 1 - 100 depending on how load is going...
     */
    public int getLoadProgress() {
        return loadProgress;
    }

    public void loadAllImages() {
        loadText = new String("Loading Images");
        Load_Image = loadSingleImage("login.jpg");
        AjaxSwingToolkit.registerIcon(Load_Image, "rmischedule/images/login.jpg");

        ClientLogin = loadSingleImage("clientlogin.png");
        AjaxSwingToolkit.registerIcon(ClientLogin, "rmischedule/images/clientlogin.png");

        Red_Bullet_Icon = loadSingleImage("bullet_ball_glass_red.png");
        AjaxSwingToolkit.registerIcon(Red_Bullet_Icon, "rmischedule/images/bullet_ball_glass_red.png");

        Green_Bullet_Icon = loadSingleImage("bullet_ball_glass_green.png");
        AjaxSwingToolkit.registerIcon(Green_Bullet_Icon, "rmischedule/images/bullet_ball_glass_green.png");

        Blue_Bullet_Icon = loadSingleImage("bullet_ball_glass_blue.png");
        AjaxSwingToolkit.registerIcon(Green_Bullet_Icon, "rmischedule/images/bullet_ball_glass_green.png");

        Large_Error_Icon = loadSingleImage("2005-stop.png");
        AjaxSwingToolkit.registerIcon(Large_Error_Icon, "rmischedule/images/2005-stop.png");

        Medium_Error_Icon = loadSingleImage("24x242005-stop.png");
        AjaxSwingToolkit.registerIcon(Medium_Error_Icon, "rmischedule/images/24x242005-stop.png");

        Yellow_Bullet_Icon = loadSingleImage("bullet_ball_glass_yellow.png");
        AjaxSwingToolkit.registerIcon(Yellow_Bullet_Icon, "rmischedule/images/bullet_ball_glass_yellow.png");

        Time_Sheet_Icon = loadSingleImage("help2.png");
        AjaxSwingToolkit.registerIcon(Time_Sheet_Icon, "rmischedule/images/help2.png");

        Time_Sheet_Over_Icon = loadSingleImage("help.png");
        AjaxSwingToolkit.registerIcon(Time_Sheet_Over_Icon, "rmischedule/images/help.png");

        Time_Sheet_Check_Icon = loadSingleImage("check224x24.png");
        AjaxSwingToolkit.registerIcon(Time_Sheet_Check_Icon, "rmischedule/images/check224x24.png");

        Emp_Approval_Icon = loadSingleImage("check224x24.png");
        AjaxSwingToolkit.registerIcon(Time_Sheet_Check_Icon, "rmischedule/images/check224x24.png");

        Grey_Pin_Icon = loadSingleImage("pin_grey.png");
        AjaxSwingToolkit.registerIcon(Grey_Pin_Icon, "rmischedule/images/pin_grey.png");

        Blue_Pin_Icon = loadSingleImage("pin_blue.png");
        AjaxSwingToolkit.registerIcon(Blue_Pin_Icon, "rmischedule/images/pin_blue.png");

        Save_Data_24x24 = loadSingleImage("disks.png");
        AjaxSwingToolkit.registerIcon(Save_Data_24x24, "rmischedule/images/disks.png");

        Add_Data_24x24 = loadSingleImage("add.png");
        AjaxSwingToolkit.registerIcon(Add_Data_24x24, "rmischedule/images/add.png");

        Arrow_Down_Icon = loadSingleImage("arrow_down_blue.png");
        AjaxSwingToolkit.registerIcon(Arrow_Down_Icon, "rmischedule/images/arrow_down_blue.png");

        Arrow_Up_Icon = loadSingleImage("arrow_up_blue.png");
        AjaxSwingToolkit.registerIcon(Arrow_Up_Icon, "rmischedule/images/arrow_up_blue.png");

        Zoom_In_Shift_Icon = loadSingleImage("zoom_out_shift.png");
        AjaxSwingToolkit.registerIcon(Zoom_In_Shift_Icon, "rmischedule/images/zoom_out_shift.png");

        Zoom_Out_Shift_Icon = loadSingleImage("zoom_in_shift.png");
        AjaxSwingToolkit.registerIcon(Zoom_Out_Shift_Icon, "rmischedule/images/zoom_in_shift.png");

        loadProgress += 5;
        Thread.yield();
        LetterHead = loadSingleImage("letterhead.jpg");
        AjaxSwingToolkit.registerIcon(LetterHead, "rmischedule/images/letterhead.jpg");

        Green_Pin_Icon = loadSingleImage("pin_green.png");
        AjaxSwingToolkit.registerIcon(Green_Pin_Icon, "rmischedule/images/pin_green.png");

        Yellow_Pin_Icon = loadSingleImage("pin_yellow.png");
        AjaxSwingToolkit.registerIcon(Yellow_Pin_Icon, "rmischedule/images/pin_yellow.png");

        Warning32x32 = loadSingleImage("warning32x32.png");
        AjaxSwingToolkit.registerIcon(Warning32x32, "rmischedule/images/warning32x32.png");

        Red_Pin_Icon = loadSingleImage("pin_red.png");
        AjaxSwingToolkit.registerIcon(Red_Pin_Icon, "rmischedule/images/pin_red.png");

        Devil_Icon = loadSingleImage("user3.png");
        AjaxSwingToolkit.registerIcon(Devil_Icon, "rmischedule/images/user3.png");

        House_Icon = loadSingleImage("house.png");
        AjaxSwingToolkit.registerIcon(House_Icon, "rmischedule/images/house.png");

        Reconcile_Icon = loadSingleImage("calendar_preferences.png");
        AjaxSwingToolkit.registerIcon(Reconcile_Icon, "rmischedule/images/calendar_preferences.png");

        Mobile_Phone_Icon = loadSingleImage("mobilephone1.png");
        AjaxSwingToolkit.registerIcon(Mobile_Phone_Icon, "rmischedule/images/mobilephone1.png");

        Stop_Watch_Icon = loadSingleImage("stopwatch.png");
        AjaxSwingToolkit.registerIcon(Stop_Watch_Icon, "rmischedule/images/stopwatch.png");

        Yellow_Warning_Icon = loadSingleImage("warningyellow.png");
        AjaxSwingToolkit.registerIcon(Yellow_Warning_Icon, "rmischedule/images/warningyellow.png");

        Blue_Warning_Icon = loadSingleImage("warningblue.png");
        AjaxSwingToolkit.registerIcon(Blue_Warning_Icon, "rmischedule/images/warningblue.png");

        Red_Warning_Icon = loadSingleImage("warningred.png");
        AjaxSwingToolkit.registerIcon(Red_Warning_Icon, "rmischedule/images/warningred.png");

        Blue_Information_Icon = loadSingleImage("information.png");
        AjaxSwingToolkit.registerIcon(Blue_Information_Icon, "rmischedule/images/information.png");

        Alert_Animated_Icon = loadSingleImage("alert_animated.gif");
        AjaxSwingToolkit.registerIcon(Alert_Animated_Icon, "rmischedule/alert_animated.gif");

        Calendar_Up_Icon = loadSingleImage("calendar_up.png");
        AjaxSwingToolkit.registerIcon(Calendar_Up_Icon, "rmischedule/images/calendar_up.png");

        Close_Window_Icon = loadSingleImage("CloseWindow.png");
        AjaxSwingToolkit.registerIcon(Close_Window_Icon, "rmischedule/images/CloseWindow.png");

        YellowPin24x24 = loadSingleImage("pin_yellow24x24.png");
        AjaxSwingToolkit.registerIcon(YellowPin24x24, "rmischedule/images/pin_yellow24x24.png");

        Note16x16 = loadSingleImage("document_pinned.png");
        AjaxSwingToolkit.registerIcon(Note16x16, "rmischedule/images/document_pinned.png");

        loadProgress += 5;
        Thread.yield();
        Calendar_Down_Icon = loadSingleImage("calendar_down.png");
        AjaxSwingToolkit.registerIcon(Calendar_Down_Icon, "rmischedule/images/calendar_down.png");

        CheckInButtonIcon = loadSingleImage("checkin.png");
        AjaxSwingToolkit.registerIcon(CheckInButtonIcon, "rmischedule/images/checkin.png");

        Index_Icon = loadSingleImage("index.png");
        AjaxSwingToolkit.registerIcon(Index_Icon, "rmischedule/images/index.png");

        Id_Cards_Icon = loadSingleImage("id_cards.png");
        AjaxSwingToolkit.registerIcon(Id_Cards_Icon, "rmischedule/images/id_cards.png");

        Note_Icon = loadSingleImage("note.png");
        AjaxSwingToolkit.registerIcon(Note_Icon, "rmischedule/images/note.png");

        Shield_Icon = loadSingleImage("shield.png");
        AjaxSwingToolkit.registerIcon(Shield_Icon, "rmischedule/images/shield.png");

        User3_Icon = loadSingleImage("user3.png");
        AjaxSwingToolkit.registerIcon(User3_Icon, "rmischedule/images/user3.png");

        Lock_Icon = loadSingleImage("lock.png");
        AjaxSwingToolkit.registerIcon(Lock_Icon, "rmischedule/images/lock.png");

        Books_Icon = loadSingleImage("books.png");
        AjaxSwingToolkit.registerIcon(Books_Icon, "rmischedule/images/books.png");

        Note_Edit_Icon = loadSingleImage("note_edit.png");
        AjaxSwingToolkit.registerIcon(Note_Edit_Icon, "rmischedule/images/note_edit.png");

        Note_Pinned_Icon = loadSingleImage("note_pinned.png");
        AjaxSwingToolkit.registerIcon(Note_Pinned_Icon, "rmischedule/images/note_pinned.png");

        Exit_Form_Icon = loadSingleImage("delete224x24.png");
        AjaxSwingToolkit.registerIcon(Exit_Form_Icon, "rmischedule/images/delete224x24.png");

        Save_Notes_Icon = loadSingleImage("disk_green.png");
        AjaxSwingToolkit.registerIcon(Save_Notes_Icon, "rmischedule/images/disk_green.png");

        Edit_Notes_Icon = loadSingleImage("document_dirty.png");
        AjaxSwingToolkit.registerIcon(Edit_Notes_Icon, "rmischedule/images/document_dirty.png");

        Close_Notes_Icon = loadSingleImage("closeNotes.png");
        AjaxSwingToolkit.registerIcon(Close_Notes_Icon, "rmischedule/images/closeNotes.png");

        Garbage_Full_16x16 = loadSingleImage("garbage_full.png");
        AjaxSwingToolkit.registerIcon(Garbage_Full_16x16, "rmischedule/images/garbage_full.png");

        BulletRed16x16Icon = loadSingleImage("bullet_ball_red.png");
        AjaxSwingToolkit.registerIcon(BulletRed16x16Icon, "rmischedule/images/bullet_ball_red.png");

        BulletGreen16x16Icon = loadSingleImage("bullet_ball_green.png");
        AjaxSwingToolkit.registerIcon(BulletGreen16x16Icon, "rmischedule/images/bullet_ball_green.png");

        BulletYellow16x16Icon = loadSingleImage("bullet_ball_yellow.png");
        AjaxSwingToolkit.registerIcon(BulletYellow16x16Icon, "rmischedule/images/bullet_ball_yellow.png");

        loadProgress += 5;
        Thread.yield();
        Selection_Delete_Icon = loadSingleImage("selection_delete.png");
        AjaxSwingToolkit.registerIcon(Selection_Delete_Icon, "rmischedule/images/selection_delete.png");

        Find_Icon = loadSingleImage("find.png");
        AjaxSwingToolkit.registerIcon(Find_Icon, "rmischedule/images/find.png");

        Navigate_Left_Icon = loadSingleImage("navigate_left.png");
        AjaxSwingToolkit.registerIcon(Navigate_Left_Icon, "rmischedule/images/navigate_left.png");

        Navigate_Left_2_Icon = loadSingleImage("navigate_left2.png");
        AjaxSwingToolkit.registerIcon(Navigate_Left_2_Icon, "rmischedule/images/navigate_left2.png");

        Navigate_Right_Icon = loadSingleImage("navigate_right.png");
        AjaxSwingToolkit.registerIcon(Navigate_Right_Icon, "rmischedule/images/navigate_right.png");

        Navigate_Right_2_Icon = loadSingleImage("navigate_right2.png");
        AjaxSwingToolkit.registerIcon(Navigate_Right_2_Icon, "rmischedule/images/navigate_right2.png");

        Active_Users_Icon = loadSingleImage("active_users.png");
        AjaxSwingToolkit.registerIcon(Active_Users_Icon, "rmischedule/images/active_users.png");

        All_Users_Icon = loadSingleImage("all_users.png");
        AjaxSwingToolkit.registerIcon(All_Users_Icon, "rmischedule/images/all_users.png");

        Shift_Training_No_Trainer_Icon = loadSingleImage("user1_information.png");
        AjaxSwingToolkit.registerIcon(Shift_Training_No_Trainer_Icon, "rmischedule/images/user1_information.png");

        Schedule_No_Training_Icon = loadSingleImage("certificate_information.png");
        AjaxSwingToolkit.registerIcon(Schedule_No_Training_Icon, "rmischedule/images/certificate_information.png");

        Wrench_Icon = loadSingleImage("wrench.png");
        AjaxSwingToolkit.registerIcon(Wrench_Icon, "rmischedule/images/wrench.png");

        Shift_Viewed_Icon = loadSingleImage("eye.png");
        AjaxSwingToolkit.registerIcon(Shift_Viewed_Icon, "rmischedule/images/eye.png");

        RecycleRefreshIcon = loadSingleImage("recycle.png");
        AjaxSwingToolkit.registerIcon(RecycleRefreshIcon, "rmischedule/images/recycle.png");

        ReplaceIcon = loadSingleImage("replace.png");
        AjaxSwingToolkit.registerIcon(ReplaceIcon, "rmischedule/images/replace.png");

        ViewOpenShiftsIcon = loadSingleImage("zoom_out.png");
        AjaxSwingToolkit.registerIcon(ViewOpenShiftsIcon, "rmischedule/images/zoom_out.png");

        Mail_Schedule_Icon = loadSingleImage("mail_write.png");
        AjaxSwingToolkit.registerIcon(Mail_Schedule_Icon, "rmischedule/images/mail_write.png");

        Find_Next32x32 = loadSingleImage("find_next.png");
        AjaxSwingToolkit.registerIcon(Find_Next32x32, "rmischedule/images/find_next.png");

        loadProgress += 5;
        Thread.yield();
        ViewAllShiftsIcon = loadSingleImage("zoom_in.png");
        AjaxSwingToolkit.registerIcon(ViewAllShiftsIcon, "rmischedule/images/zoom_in.png");

        CheckInPrinterIcon = loadSingleImage("CheckInPrinter.png");
        AjaxSwingToolkit.registerIcon(CheckInPrinterIcon, "rmischedule/images/CheckInPrinter.png");

        HelpIcon = loadSingleImage("help224x24.png");
        AjaxSwingToolkit.registerIcon(HelpIcon, "rmischedule/images/help224x24.png");

        Yellow_16_Icon = loadSingleImage("point_yellow_16_16.png");
        AjaxSwingToolkit.registerIcon(Yellow_16_Icon, "rmischedule/images/point_yellow_16_16.png");

        Green_16_Icon = loadSingleImage("point_green_16_16.png");
        AjaxSwingToolkit.registerIcon(Yellow_16_Icon, "rmischedule/images/point_green_16_16.png");

        Red_16_Icon = loadSingleImage("point_red_16_16.png");
        AjaxSwingToolkit.registerIcon(Yellow_16_Icon, "rmischedule/images/point_red_16_16.png");

        ClockIcon = loadSingleImage("clock24x24.png");
        AjaxSwingToolkit.registerIcon(ClockIcon, "rmischedule/images/clock24x24.png");

        PrintForToolbarIcon = loadSingleImage("ToolBarPrint.png");
        AjaxSwingToolkit.registerIcon(PrintForToolbarIcon, "rmischedule/images/ToolBarPrint.png");

        AvailabilityIcon = loadSingleImage("user2.png");
        AjaxSwingToolkit.registerIcon(AvailabilityIcon, "rmischedule/images/user2.png");

        SelectAllIcon = loadSingleImage("SelectAll.PNG");
        AjaxSwingToolkit.registerIcon(SelectAllIcon, "rmischedule/images/SelectAll.PNG");

        DeselectAllIcon = loadSingleImage("DeselectAll.PNG");
        AjaxSwingToolkit.registerIcon(SelectAllIcon, "rmischedule/images/DeselectAll.PNG");

        EmployeeListIcon = loadSingleImage("user1_find.png");
        AjaxSwingToolkit.registerIcon(EmployeeListIcon, "rmischedule/images/user1_find.png");

        ConsolidateIcon = loadSingleImage("copy.png");
        AjaxSwingToolkit.registerIcon(ConsolidateIcon, "rmischedule/images/copy.png");

        ShiftAlertIcon = loadSingleImage("warningreal32.png");
        AjaxSwingToolkit.registerIcon(ShiftAlertIcon, "rmischedule/images/warningreal32.png");

        ViewOpenShifts = loadSingleImage("openshift.png");
        AjaxSwingToolkit.registerIcon(ViewOpenShifts, "rmischedule/images/openshift.png");

        FilterUsers = loadSingleImage("user_search.png");
        AjaxSwingToolkit.registerIcon(FilterUsers, "rmischedule/images/user_search.png");

        ViewAllShifts = loadSingleImage("allshifts.png");
        AjaxSwingToolkit.registerIcon(ViewAllShifts, "rmischedule/images/allshifts.png");

        Find_Next_Icon = loadSingleImage("viewallemp.png");
        AjaxSwingToolkit.registerIcon(Find_Next_Icon, "rmischedule/images/viewallemp.png");

        NavDown16x16_Icon = loadSingleImage("navigate_down.png");
        AjaxSwingToolkit.registerIcon(NavDown16x16_Icon, "rmischedule/images/navigate_down.png");

        NavUp16x16_Icon = loadSingleImage("navigate_up.png");
        AjaxSwingToolkit.registerIcon(NavUp16x16_Icon, "rmischedule/images/navigate_up.png");

        loadProgress += 5;
        Thread.yield();
        Find_Previous_Icon = loadSingleImage("viewactiveemp.png");
        AjaxSwingToolkit.registerIcon(Find_Previous_Icon, "rmischedule/images/viewactiveemp.png");

        Viewing_All_Employees = loadSingleImage("viewallnew.png");
        AjaxSwingToolkit.registerIcon(Viewing_All_Employees, "rmischedule/images/viewallnew.png");

        Viewing_Active_Employees = loadSingleImage("viewactivenew.png");
        AjaxSwingToolkit.registerIcon(Viewing_Active_Employees, "rmischedule/images/viewactivenew.png");

        Viewing_All_Clients = loadSingleImage("viewallclients.png");
        AjaxSwingToolkit.registerIcon(Viewing_All_Clients, "rmischedule/images/viewallclients.png");

        Viewing_Active_Clients = loadSingleImage("viewactiveclients.png");
        AjaxSwingToolkit.registerIcon(Viewing_Active_Clients, "rmischedule/images/viewactiveclients.png");

        Connected_Icon = loadSingleImage("green.png");
        AjaxSwingToolkit.registerIcon(Connected_Icon, "rmischedule/images/green.png");

        Connected_Full_Icon = loadSingleImage("green_full.png");
        AjaxSwingToolkit.registerIcon(Connected_Full_Icon, "rmischedule/images/green_full.png");

        Disconnected_Icon = loadSingleImage("red.png");
        AjaxSwingToolkit.registerIcon(Disconnected_Icon, "rmischedule/images/red.png");

        Disconnected_Full_Icon = loadSingleImage("red_full.png");
        AjaxSwingToolkit.registerIcon(Disconnected_Full_Icon, "rmischedule/images/red_full.png");

        Help_32x32_Icon = loadSingleImage("help32x32.png");
        AjaxSwingToolkit.registerIcon(Help_32x32_Icon, "rmischedule/images/help32x32.png");

        Delete_Data_24x24 = loadSingleImage("garbage.png");
        AjaxSwingToolkit.registerIcon(Delete_Data_24x24, "rmischedule/images/garbage.png");

        Unfaded_Warning_Large_Icon = loadSingleImage("warning.png");
        AjaxSwingToolkit.registerIcon(Unfaded_Warning_Large_Icon, "rmischedule/images/warning.png");

        First16x16 = loadSingleImage("navigate_left216x16.png");
        AjaxSwingToolkit.registerIcon(First16x16, "rmischedule/images/navigate_left216x16.png");

        Prev16x16 = loadSingleImage("navigate_left16x16.png");
        AjaxSwingToolkit.registerIcon(Prev16x16, "rmischedule/images/navigate_left16x16.png");

        Next16x16 = loadSingleImage("navigate_right16x16.png");
        AjaxSwingToolkit.registerIcon(Next16x16, "rmischedule/images/navigate_right16x16.png");

        Last16x16 = loadSingleImage("navigate_right216x16.png");
        AjaxSwingToolkit.registerIcon(Last16x16, "rmischedule/images/navigate_right216x16.png");

        //  addition by Jeffrey Davis on 05/26/2010 for messaging system
        Save_Message_Icon_36x36 = loadSingleImage("36px-Gnome-document-save.png");
        AjaxSwingToolkit.registerIcon(Save_Message_Icon_36x36, "rmischedule/images/36px-Gnome-document-save.png");

        Send_Message_Icon_36x36 = loadSingleImage("36px-Gnome-document-send.png");
        AjaxSwingToolkit.registerIcon(Save_Message_Icon_36x36, "rmischedule/images/36px-Gnome-document-send.png");

        Reset_Message_Icon_36x36 = loadSingleImage("36px-Gnome-reset.png");
        AjaxSwingToolkit.registerIcon(Reset_Message_Icon_36x36, "rmischedule/images/36px-Gnome-reset.png");

        Exit_Message_Icon_36x36 = loadSingleImage("36px-Gnome-process-stop.png");
        AjaxSwingToolkit.registerIcon(Save_Message_Icon_36x36, "rmischedule/images/36px-Gnome-process-stop.png");

        Employee_Message_Icon_227x23 = loadSingleImage("227x23-employee-list-icon.png");
        AjaxSwingToolkit.registerIcon(Save_Message_Icon_36x36, "rmischedule/images/227x23-employee-list-icon.png");
        //  addition by Jeffrey Davis on 05/26/2010 for messaging system

        //  addition by Jeffrey Davis on 08/03/2010 for New User Alert Screen
        New_User_Icon_Aero_32x32px = loadSingleImage("32x32px-Aero-newuser.png");
        AjaxSwingToolkit.registerIcon(New_User_Icon_Aero_32x32px, "rmischedule/images/32x32px-Aero-newuser.png");

        Save_User_Icon_Aero_32x32px = loadSingleImage("32x32px-Aero-save.png");
        AjaxSwingToolkit.registerIcon(Save_User_Icon_Aero_32x32px, "rmischedule/images/32x32px-Aero-save.png");

        Save_User_Icon_24x24px = loadSingleImage("24x24save.png");
        AjaxSwingToolkit.registerIcon(Save_User_Icon_24x24px, "rmischedule/images/24x24save.png");

        Remove_User_Icon_Aero_32x32px = loadSingleImage("32x32px-Aero-DB-delete.png");
        AjaxSwingToolkit.registerIcon(Remove_User_Icon_Aero_32x32px, "rmischedule/images/32x32px-Aero-DB-delete.png");

        Exit_Icon_Aero_32x32px = loadSingleImage("32x32px-Aero-cancel.png");
        AjaxSwingToolkit.registerIcon(Exit_Icon_Aero_32x32px, "rmischedule/images/32x32px-Aero-cancel.png");

        New_User_Alert_Panel = loadSingleImage("214x41px-Alert-header-custom.png");
        AjaxSwingToolkit.registerIcon(New_User_Alert_Panel, "rmischedule/images/214x41px-Alert-header-custom.png");
        //  additions by Jeffrey Davis on 08/03/2010 complete

        /*  Additions by Jeffrey Davis on 01/24/2011 for Database Operations    */
        Save_Schema_16x16_px = loadSingleImage("4007-database_export_16x16.png");
        AjaxSwingToolkit.registerIcon(Save_Schema_16x16_px, "rmischedule/images/4007-database_export_16x16.png");

        Remove_Schema_16x16_px = loadSingleImage("4002-database_delete_16x16.png");
        AjaxSwingToolkit.registerIcon(Remove_Schema_16x16_px, "rmischedule/images/4002-database_delete_16x16.png");

        Restore_Schema_16x16_px = loadSingleImage("4006-database_import_16x16.png");
        AjaxSwingToolkit.registerIcon(Restore_Schema_16x16_px, "rmischedule/images/4006-database_import_16x16.png");

        Active_Schema_16x16_px = loadSingleImage("2002-plus_16x16.png");
        AjaxSwingToolkit.registerIcon(Active_Schema_16x16_px, "rmischedule/images/2002-plus_16x16.png");

        Inactive_Schema_16x16_px = loadSingleImage("2003-minus_16x16.png");
        AjaxSwingToolkit.registerIcon(Inactive_Schema_16x16_px, "rmischedule/images/2003-minus_16x16.png");
        /*  Additions by Jeffrey Davis on 01/24/2011 complete */

        /*  Additions by Jeffrey Davis on 01/27/2011 for downloading images via Picture Panel   */
        Download_Image_24x24_px = loadSingleImage("5002-download_24x24_px.png");
        AjaxSwingToolkit.registerIcon(Download_Image_24x24_px, "rmischedule/images/5002-download_24x24_px.png");
        /*  Additions by Jeffrey Davis on 01/27/2011 complete   */

        disabledStarIcon = loadSingleImage("star_disabled.png");
        AjaxSwingToolkit.registerIcon(disabledStarIcon, "star_disabled.png");

        starIcon = loadSingleImage("star.png");
        AjaxSwingToolkit.registerIcon(starIcon, "star.png");
        starIcon_24 = loadSingleImage("star_24.png");
        disabledStarIcon_24 = loadSingleImage("star_disabled_24.png");

        /*  Additions by Jeffrey Davis on 02/01/2011 for EditTemplateDiag   */
        Edit_Template_Diag_Save_16x16_px = loadSingleImage("0100-save_16x16_px.png");
        AjaxSwingToolkit.registerIcon(Edit_Template_Diag_Save_16x16_px, "rmischedule/images/0100-save_16x16_px.png");

        Edit_Template_Diag_Exit_16x16_px = loadSingleImage("2001-cancel_16x16_px.png");
        AjaxSwingToolkit.registerIcon(Edit_Template_Diag_Exit_16x16_px, "rmischedule/images/2001-cancel_16x16_px.png");

        Edit_Template_Diag_Delete_16x16_px = loadSingleImage("0036-doc_delete_16x16.png");
        AjaxSwingToolkit.registerIcon(Edit_Template_Diag_Delete_16x16_px, "rmischedule/images/0036-doc_delete_16x16.png");
        /*  Additions by Jeffrey Davis on 02/01/2011 for EditTemplateDiag complete  */

        /*  Additions by Jeffrey Davis on 02/10/2011 for ProblemsolverEmailDialog   */
        Problemsolver_Email_Dialog_Send_16x16_px = loadSingleImage("0152-email_send_16x16.png");
        AjaxSwingToolkit.registerIcon(Problemsolver_Email_Dialog_Send_16x16_px, "rmischedule/images/0152-email_send_16x16.png");

        Problemsolver_Email_Dialog_Exit_16x16_px = loadSingleImage("2001-cancel_16x16.png");
        AjaxSwingToolkit.registerIcon(Problemsolver_Email_Dialog_Exit_16x16_px, "rmischedule/images/2001-cancel_16x16.png");
        /*  Additions by Jeffrey Davis on 02/10/2011 for ProblemsolverEmailDiagog complete  */

        /*  Additions by Jeffrey Davis on 03/29/2011 for Client_Email_AddressBook   */
        Client_Email_AddressBook_Add_16x16_px = loadSingleImage("2002-address-book-add_16x16.png");
        AjaxSwingToolkit.registerIcon(Client_Email_AddressBook_Add_16x16_px, "rmischedule/images/2002-address-book-add_16x16.png");

        Client_Email_AddressBook_Finished_16x16_px = loadSingleImage("2000-address-book-finished_16x16.png");
        AjaxSwingToolkit.registerIcon(Client_Email_AddressBook_Finished_16x16_px, "rmischedule/images/2000-address-book-finished_16x16.png");
        /*  Additions by Jeffrey Davis on 03/29/2011 for Client_Email_AddressBook complete  */

        /*  Additions by Jeffrey Davis on 04/21/2011 for EmailReportSystem  */
        EmailReport_RunReport_16x16px = loadSingleImage("0005-doc-16x16.png");
        AjaxSwingToolkit.registerIcon(EmailReport_RunReport_16x16px, "rmischedule/images/0005-doc-16x16.png");
        /*  Additions by Jeffrey Davis on 04/21/2011 for EmailReportSystem complete */

        E24x24EMAILSEND = loadSingleImage("24x24email_send.png");
        AjaxSwingToolkit.registerIcon(E24x24EMAILSEND, "rmischedule/images/24x24email_send.png");

        WindowsEdit24x24 = loadSingleImage("24x24windows_edit.png");
        AjaxSwingToolkit.registerIcon(WindowsEdit24x24, "rmischedule/images/24x24windows_edit.png");

        Delete24x24 = loadSingleImage("24x24recyclebin_full.png");
        AjaxSwingToolkit.registerIcon(Delete24x24, "rmischedule/images/24x24recyclebin_full.png");

        /* Added by Derrick Albers - Frame icon
         */
        SchedFoxIcon = loadSingleImage("schedIcon.png");
        AjaxSwingToolkit.registerIcon(SchedFoxIcon, "rmischedule/images/schedIcon.png");
        if (SchedFoxIcon != null) {
            this.setIconImage(SchedFoxIcon.getImage());
        }
        loadProgress += 5;
        Thread.yield();
        //Faded Images//
        Sic_Icon = loadSingleImage("sick.png");
        AjaxSwingToolkit.registerIcon(Sic_Icon, "rmischedule/images/sick.png");

        Sic_Half_Icon = loadSingleImage("sick_half.png");
        AjaxSwingToolkit.registerIcon(Sic_Half_Icon, "rmischedule/images/sick_half.png");

        Vac_Icon = loadSingleImage("vac.png");
        AjaxSwingToolkit.registerIcon(Vac_Icon, "rmischedule/images/vac.png");

        Vac_Half_Icon = loadSingleImage("vac_half.png");
        AjaxSwingToolkit.registerIcon(Vac_Half_Icon, "rmischedule/images/vac_half.png");

        Vac_Half_Icon_Faded = loadSingleImage("vac_half.png", .3);
        AjaxSwingToolkit.registerIcon(Vac_Half_Icon_Faded, "rmischedule/images/vac_half.png");

        Na_Icon = loadSingleImage("user1_preferences.png");
        AjaxSwingToolkit.registerIcon(Na_Icon, "rmischedule/images/user1_preferences.png");

        Generic_NA_Icon = loadSingleImage("naslash.png");
        AjaxSwingToolkit.registerIcon(Generic_NA_Icon, "rmischedule/images/naslash.png");

        MapIcon = loadSingleImage("doc_map_edit.png");
        AjaxSwingToolkit.registerIcon(MapIcon, "rmischedule/images/doc_map_edit.png");

        Personal_Icon = loadSingleImage("personal.png");
        AjaxSwingToolkit.registerIcon(Personal_Icon, "rmischedule/images/personal.png");

        Personal_Half_Icon = loadSingleImage("personal_half.png");
        AjaxSwingToolkit.registerIcon(Personal_Half_Icon, "rmischedule/images/personal_half.png");

        Personal_Half_Icon_Faded = loadSingleImage("personal_half.png", .3);
        AjaxSwingToolkit.registerIcon(Personal_Half_Icon_Faded, "rmischedule/images/personal_half.png");

        Military_Icon = loadSingleImage("military.png");
        AjaxSwingToolkit.registerIcon(Military_Icon, "rmischedule/images/military.png");

        Sic_Icon_Faded = loadSingleImage("sick.png", .3);
        AjaxSwingToolkit.registerIcon(Sic_Icon_Faded, "rmischedule/images/sick.png");

        Sic_Half_Icon_Faded = loadSingleImage("sick_half.png", .3);
        AjaxSwingToolkit.registerIcon(Sic_Half_Icon_Faded, "rmischedule/images/sick_half.png");

        Vac_Icon_Faded = loadSingleImage("vac.png", .3);
        AjaxSwingToolkit.registerIcon(Vac_Icon_Faded, "rmischedule/images/vac.png");

        Na_Icon_Faded = loadSingleImage("na.png", .3);
        AjaxSwingToolkit.registerIcon(Na_Icon_Faded, "rmischedule/images/na.png");

        Generic_NA_Icon_Faded = loadSingleImage("naslash.png", .3);
        AjaxSwingToolkit.registerIcon(Generic_NA_Icon_Faded, "rmischedule/images/naslash.png");

        Military_Icon_Faded = loadSingleImage("military.png", .3);
        AjaxSwingToolkit.registerIcon(Military_Icon_Faded, "rmischedule/images/military.png");

        Ok_Yellow = loadSingleImage("2000-ok-yellow.png");
        AjaxSwingToolkit.registerIcon(Ok_Yellow, "rmischedule/images/2000-ok-yellow.png");

        Personal_Icon_Faded = loadSingleImage("personal.png", .3);
        AjaxSwingToolkit.registerIcon(Personal_Icon_Faded, "rmischedule/images/personal.png");

        Deleted_Shift_Icon = loadSingleImage("Delete_Shift.png", .4);
        AjaxSwingToolkit.registerIcon(Deleted_Shift_Icon, "rmischedule/images/Delete_Shift.png");

        Warning_Large_Icon = loadSingleImage("warning.png", .4);
        AjaxSwingToolkit.registerIcon(Warning_Large_Icon, "rmischedule/images/warning.png");

        Employee_Print_Icon = loadSingleImage("printer.png", .6);
        AjaxSwingToolkit.registerIcon(Employee_Print_Icon, "rmischedule/images/printer.png");

        Mobile_Phone_3_Icon = loadSingleImage("mobilephone3.png", .4);
        AjaxSwingToolkit.registerIcon(Mobile_Phone_3_Icon, "rmischedule/images/mobilephone3.png");

        Lock_Time_Icon = loadSingleImage("lock_time.png", .4);
        AjaxSwingToolkit.registerIcon(Lock_Time_Icon, "rmischedule/images/lock_time.png");

        House_Meeting_Icon = loadSingleImage("home.png", .4);
        AjaxSwingToolkit.registerIcon(House_Meeting_Icon, "rmischedule/images/home.png");

        Shift_Training_Icon = loadSingleImage("users3_preferences.png", .4);
        AjaxSwingToolkit.registerIcon(Shift_Training_Icon, "rmischedule/images/users3_preferences.png");

        Printer_View_Icon = loadSingleImage("printer_view.png", .6);
        AjaxSwingToolkit.registerIcon(Printer_View_Icon, "rmischedule/images/printer_view.png");

        Ok_Icon = loadSingleImage("ok.png", .9);
        AjaxSwingToolkit.registerIcon(Ok_Icon, "rmischedule/images/ok.png");

        Printer_Icon = loadSingleImage("printer.png", .9);
        AjaxSwingToolkit.registerIcon(Printer_Icon, "rmischedule/images/printer.png");

        Component_Icon_Green = loadSingleImage("component_green.png", .7);
        AjaxSwingToolkit.registerIcon(Component_Icon_Green, "rmischedule/images/component_green.png");

        loadProgress += 10;
        loadText = new String("Finalizing Connection With Server");
        Thread.yield();

    }

    public ImageIcon loadSingleImage(String ImageName) {
        try {
            return new ImageIcon(ji.getImageFromJAR("images/" + ImageName));
        } catch (Exception e) {
            System.out.println("Error Loading " + ImageName + " Please Make Certain File Exists And Is Correct Format");
            return null;
        }
    }

    public ImageIcon loadSingleImage(String ImageName, double amountToFade) {
        return loadSingleImage(ImageName, amountToFade, true);
    }

    public ImageIcon loadSingleImage(String ImageName, double amountToFade, boolean loadFromImages) {
        String path = "";
        if (loadFromImages) {
            path = "images/";
        }
        try {
            BufferedImage newImage = ji.getBufferedImageFromJAR(path + ImageName);
            return new ImageIcon(fadeImage(newImage, amountToFade));
        } catch (Exception e) {
            System.out.println("Error Loading and Fading " + ImageName + " Please Make Certain File Exists And Is Correct Format");
            return null;
        }
    }

    public void setManagementId(String id) {
        myManagementId = id;
    }

    public String getManagementId() {
        return myManagementId;
    }

    /**
     * Is Current User a Root User?
     */
    public boolean isRootUser() {
        if (myManagementId.equals("0")) {
            return true;
        }
        return false;
    }

    public BufferedImage fadeImage(BufferedImage input, double floatAmount) {
        int jjh = -1;
        int jjw = -1;
        while (jjw < 0 || jjh < 0) {
            jjh = input.getHeight(this);
            jjw = input.getWidth(this);
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
        }
        BufferedImage bi = new BufferedImage(jjw, jjh, BufferedImage.TYPE_INT_ARGB);
        Graphics2D biContext = bi.createGraphics();
        biContext.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) floatAmount));
        biContext.drawImage(input, 0, 0, null);
        return bi;
    }

    public void continueLoad() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                try {
                    if (mySocketConn != null) {
                        mySocketConn.dispose();
                    }
                    dispose();
                    System.exit(0);
                } catch (java.security.AccessControlException e) {
                    dispose();
                }
            }
        });

        options = new rmischedule.components.Options(this);
        options.updateValues();
        setVisible(true);

        if (compBranding.getLoginType().equals(LoginType.USER)) {
            Login_Frame lf = new Login_Frame(this, argument1);
            this.desktop.add(lf);
            lf.setVisible(true);
        } else if (compBranding.getLoginType().equals(LoginType.CLIENT)) {
            ClientLogin clientLogin = new ClientLogin(compBranding.getCompany().getDB());
            Main_Window.parentOfApplication.desktop.add(clientLogin);
            clientLogin.setVisible(true);
        } else if (!compBranding.getLoginType().equals(LoginType.NEWEMPLOYEE)) {
            EmployeeLogin employeeLogin = new EmployeeLogin(compBranding.getCompany().getDB());
            Main_Window.parentOfApplication.desktop.add(employeeLogin);
            employeeLogin.setVisible(true);
        }
    }

    /**
     * Loads the company view object information for the specified company_id.
     *
     * @param company_id
     * @return
     */
    public Hashtable<String, Vector<CompanyInformationObj>> getCompanyViewOptions(String company_id) {
        Hashtable<String, Vector<CompanyInformationObj>> retVal
                = new Hashtable<String, Vector<CompanyInformationObj>>();
        try {
            get_company_view_options_query viewOptionsQuery = new get_company_view_options_query();
            viewOptionsQuery.update(Integer.parseInt(company_id));
            Record_Set rs = (new Connection()).executeQuery(viewOptionsQuery);
            for (int r = 0; r < rs.length(); r++) {
                if (retVal.get(rs.getString("company_view_option_type")) == null) {
                    retVal.put(rs.getString("company_view_option_type"), new Vector<CompanyInformationObj>());
                }
                Vector<CompanyInformationObj> currentOptions = retVal.get(rs.getString("company_view_option_type"));
                CompanyInformationObj obj = new CompanyInformationObj(rs);
                try {
                    obj.setCompany_id(Integer.parseInt(company_id));
                } catch (Exception e) {
                }
                currentOptions.add(obj);
                rs.moveNext();
            }
        } catch (Exception e) {
        }
        return retVal;
    }

    /**
     * Gets the company wide view setting, for employee window and client window
     * etc...
     *
     * @param companyInfo
     * @param value
     * @return
     */
    public CompanyInformationObj getCompanyInformation(Hashtable<String, Vector<CompanyInformationObj>> companyInfo, String value) {
        Iterator<String> keys = companyInfo.keySet().iterator();
        CompanyInformationObj retVal = new CompanyInformationObj();
        retVal.setOption_value("true");
        while (keys.hasNext()) {
            Vector<CompanyInformationObj> currObjVect = companyInfo.get(keys.next());
            for (int c = 0; c < currObjVect.size(); c++) {
                CompanyInformationObj currObj = currObjVect.get(c);
                if (currObj.getOption_key().equalsIgnoreCase(value)) {
                    retVal = currObj;
                }
            }
        }
        return retVal;
    }

    public void authenticateUser(String userName, String password) {
        /**
         * I am working on the actual logic behind this method for right now use
         * this method to set your variables, later I will actually provide
         * queries...pretty tricky right now until then just set the employees
         * id, database and branch_id manually.
         */
        validate_employee_query myValidateQuery = new validate_employee_query();
        RunQueriesEx myCompQuery = new RunQueriesEx();
        if (userName == null) {
            userName = "mylogin";
        }
        if (password == null) {
            password = "mypw";
        }
        myValidateQuery.update(userName, password);
        myCompQuery.add(myValidateQuery);
        Record_Set rs = new Record_Set();
        try {
            ArrayList myList = new ArrayList();
            myList = new Connection().executeQueryEx(myCompQuery);
            rs = (Record_Set) myList.get(0);
            java.util.StringTokenizer myIdComp = new java.util.StringTokenizer(rs.getString("validate_employee"), "-");

            userName = rs.getString("validate_employee");
        } catch (Exception e) {
            StringBuilder myString = new StringBuilder();
            StackTraceElement[] myComString = e.getStackTrace();
            myString.append("ERROR! " + e.toString() + "\n");
            for (int i = 0; i < myComString.length; i++) {
                myString.append(myComString[i].toString() + "\n");
            }
        }
    }

    public Jar_Images getJarImage() {
        return ji;
    }

    public String getBase() {
        return base;
    }

    public void addMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu logoutMenu = new JMenu("Logout");
        JMenu adminMenu = new JMenu("Admin");
        JMenu clientMenu = new JMenu("Locations");
        JMenu clientSubMenu = new JMenu("Add/Edit Locations");
        JMenu employeeMenu = new JMenu("Employees");
        JMenu eventsMenu = new JMenu("Events");
        JMenu equipmentMenu = new JMenu("Uniform Returns");
        JMenu employeeSubMenu = new JMenu("Add/Edit Employees");
        JMenu employeeAppSubMenu = new JMenu("Pending Employee Approval");
        JMenu scheduleMenu = new JMenu("Schedule");
        scheduleAddEdit = new JMenu("Schedule");
        JMenu settingsMenu = new JMenu("Settings");
        JMenu miscMenu = new JMenu("Misc");
        JMenu messagingMenu = new JMenu("Messaging");

        JMenu rateCodeMenu = new JMenu("Rate Code");
        JMenu empSecurityGroupsMenu = new JMenu("Employee Security Groups");
        JMenu noteTypeMenu = new JMenu("Edit Note Types");
        JMenu contactTypeMenu = new JMenu("Edit Client Contact Types");
        JMenu certificationsMenu = new JMenu("Edit Certifications");
        JMenu printMenu = new JMenu("Reports");
        JMenu analyticsMenu = new JMenu("Analytics");
        JMenu helpMenu = new JMenu("Help");
        windowMenu = new JMenu("Windows");

        addMenuItem(logoutMenu, "Logout", new LogoutAction(this));
        menuBar.add(logoutMenu);

        if (checkSecurity(security_detail.MODULES.ADMIN_USER)) {
            JMenuItem companyEditMenuItem = new JMenuItem("Company Information");
            companyEditMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        setWaitCursor(true);
                        clickedMenu(Main_Window.COMPANY_EDIT, null);
                    } catch (Exception ex) {
                    } finally {
                        setWaitCursor(false);
                    }
                }
            });
            adminMenu.add(companyEditMenuItem);
        }

        if (checkSecurity(security_detail.MODULES.ADMIN_GROUP) && isRootUser()) {
            addMenuItem(adminMenu, "Groups", new AdminGroupAction(this));
        }

        if (checkSecurity(security_detail.MODULES.ADMIN_USER)) {
            addMenuItem(adminMenu, "Users", new AdminUserAction(this));
        }

        if (isRootUser()) {
            addMenuItem(adminMenu, "* Client Email", new ClientEmailAction(this));
            addMenuItem(adminMenu, "* Connected Users", new AdminConnectedAction(this));
            //Don't do this often but this one needs to be VERY restricted.
            if (Main_Window.parentOfApplication.getUser().getLogin().equals("jc")
                    || Main_Window.parentOfApplication.getUser().getLogin().equals("irajuneau")) {
                addMenuItem(adminMenu, "* Commission Cap", new AdminCommissionCap(this));
            }
            addMenuItem(adminMenu, "* Database Operations", new AdminDatabaseOperationsAction(this));
            addMenuItem(adminMenu, "* Invoicing", new InvoicingAction());
            addMenuItem(adminMenu, "* Mapping", new AdminMapAction());
            addMenuItem(adminMenu, "* Management", new AdminManagementAction(this));
            addMenuItem(adminMenu, "* New User Alerts", new AdminNewUserEmailAlertsAction(this));
        }

        if (adminMenu.getMenuComponentCount() > 0) {
            menuBar.add(adminMenu);

        }

        if (checkSecurity(security_detail.MODULES.CLIENT_INFORMATION)) {
            menuBar.add(clientMenu);
            setUpMenuOfCompsAndBranches(clientSubMenu, this, CLIENT_EDIT_ACTION);

            JMenu callQueue = new JMenu("Client Call Log - CC's");
            this.setUpMenuOfComps(callQueue, this, CLIENT_CALL_QUEUE);

            JMenu problems = new JMenu("Corporate Communicator Search");
            this.setUpMenuOfComps(problems, this, PROBLEM_SEARCH);

            JMenu importDataMenuItem = new JMenu("Import Data");
            this.setUpMenuOfCompsAndBranches(importDataMenuItem, this, IMPORT_CLIENT_DATA);

            JMenu exportDataMenuItem = new JMenu("Export Client Data");
            this.setUpMenuOfCompsAndBranches(exportDataMenuItem, this, EXPORT_CLIENT_DATA);

            JMenu exportClientRateMenuItem = new JMenu("Export Client Rate Data");
            this.setUpMenuOfCompsAndBranches(exportClientRateMenuItem, this, EXPORT_CLIENT_RATECODES_DATA);

            clientMenu.add(clientSubMenu);

            JMenu searchClients = new JMenu("Search Clients");
            this.setUpMenuOfComps(searchClients, this, CLIENT_SEARCH);
            clientMenu.add(searchClients);

            clientMenu.add(callQueue);
            if (Main_Window.parentOfApplication.isUserAMemberOfGroups(new Connection(), "Corporate User")) {
                clientMenu.add(problems);
            }

            if (Main_Window.parentOfApplication.isUserAMemberOfGroups(new Connection(), "ADMIN", "Corporate User")) {
                JMenu rateIncrease = new JMenu("Rate Increase");
                this.setUpMenuOfComps(rateIncrease, this, CLIENT_RATE_INCREASE);
                clientMenu.add(rateIncrease);
            }

            clientMenu.add(new JSeparator());
            clientMenu.add(importDataMenuItem);
            clientMenu.add(exportDataMenuItem);
            clientMenu.add(exportClientRateMenuItem);
        }

        if (checkSecurity(security_detail.MODULES.EMPLOYEE_EDIT)) {
            menuBar.add(employeeMenu);
            JMenu importDataMenuItem = new JMenu("Import Data");
            this.setUpMenuOfCompsAndBranches(importDataMenuItem, this, IMPORT_EMPLOYEE_DATA);

            JMenu exportDataMenuItem = new JMenu("Export Data");
            this.setUpMenuOfCompsAndBranches(exportDataMenuItem, this, EXPORT_EMPLOYEE_DATA);

            employeeMenu.add(employeeSubMenu);
            employeeMenu.add(employeeAppSubMenu);
            addMenuItem(employeeMenu, "Search Employees", new SearchEmployeeAction(this));
            employeeMenu.add(new JSeparator());
            employeeMenu.add(importDataMenuItem);
            employeeMenu.add(exportDataMenuItem);
        }

        if (Main_Window.parentOfApplication.isUserAMemberOfGroups(new Connection(), "ADMIN", "Corporate User", "Payroll", "Dispatcher", "Personnel Manager", "Scheduling Manager", "Upper management", "District Manager")) {
            menuBar.add(eventsMenu);

            JMenu addEventMenu = new JMenu("Event Manager");
            this.setUpMenuOfComps(addEventMenu, this, ADD_EVENT);
            eventsMenu.add(addEventMenu);
        }

        //Good ol equipment menu
        JMenu returnEquipmentMenu = new JMenu("Return Employee Uniforms");
        this.setUpMenuOfComps(returnEquipmentMenu, this, RETURN_EMP_EQUIPMENT);
        equipmentMenu.add(returnEquipmentMenu);
        menuBar.add(equipmentMenu);

        if (checkSecurity(security_detail.MODULES.SCHEDULING_EDIT) || checkSecurity(security_detail.MODULES.SCHEDULING_SHIFT_EDIT)) {
            scheduleMenu.add(scheduleAddEdit);
        }

        //  menuBar.add(sTexting1);
        setUpMenuOfCompsAndBranches(scheduleAddEdit, this, SCHEDULE_EDIT_ACTION);
        setUpMenuOfCompsAndBranches(employeeSubMenu, this, EMPLOYEE_EDIT_ACTION);
        setUpMenuOfCompsAndBranches(employeeAppSubMenu, this, EMPLOYEE_APPROVAL_ACTION);

        menuBar.add(scheduleMenu);

        addMenuItem(settingsMenu, "Options", new Main_Window.OptionAction(this));
        addMenuItem(settingsMenu, "View/Edit Your Account", new Main_Window.EditAccountAction());

        menuBar.add(settingsMenu);

        setUpRateCode(rateCodeMenu);
        if (checkSecurity(security_detail.MODULES.RATE_CODE)) {
            miscMenu.add(rateCodeMenu);
        }

        setupEmployeeSecurity(empSecurityGroupsMenu);
        if (checkSecurity(security_detail.MODULES.ADMIN_USER)) {
            miscMenu.add(empSecurityGroupsMenu);

            JMenu healthCareOptions = new JMenu("Health Care Option");
            setUpMenuOfComps(healthCareOptions, this, HEALTHCARE_OPTIONS);
            miscMenu.add(healthCareOptions);
        }

        if (checkSecurity(security_detail.MODULES.ADMIN_GROUP) || checkSecurity(security_detail.MODULES.ADMIN_USER)) {
            for (int i = 0; i < ActiveListOfCompanies.size(); i++) {
                JMenuItem myNoteTypeMenu = new JMenuItem(ActiveListOfCompanies.get(i).getName(), null);
                noteTypeMenu.add(myNoteTypeMenu);
                myNoteTypeMenu.addActionListener(new NoteTypeEditAction(ActiveListOfCompanies.get(i).getId()));
            }
        }

        if (checkSecurity(security_detail.MODULES.ADMIN_GROUP) || checkSecurity(security_detail.MODULES.ADMIN_USER)) {
            for (int i = 0; i < ActiveListOfCompanies.size(); i++) {
                JMenuItem myContactTypeSubMenu = new JMenuItem(ActiveListOfCompanies.get(i).getName(), null);
                contactTypeMenu.add(myContactTypeSubMenu);
                myContactTypeSubMenu.addActionListener(new ClientContactTypeEditAction(ActiveListOfCompanies.get(i).getId()));
            }
        }

        if (checkSecurity(security_detail.MODULES.ADMIN_GROUP) || checkSecurity(security_detail.MODULES.ADMIN_USER)) {
            for (int i = 0; i < ActiveListOfCompanies.size(); i++) {
                JMenuItem myCertificationsMenu = new JMenuItem(ActiveListOfCompanies.get(i).getName(), null);
                certificationsMenu.add(myCertificationsMenu);
                myCertificationsMenu.addActionListener(new CertificationEditAction(ActiveListOfCompanies.get(i).getId()));
            }
        }

        if (checkSecurity(security_detail.MODULES.ADMIN_GROUP) || checkSecurity(security_detail.MODULES.ADMIN_USER)) {
            JMenu timeOffAccrualMenu = new JMenu("Edit Time Off Series");
            for (int i = 0; i < ActiveListOfCompanies.size(); i++) {
                JMenuItem myTimeOffAccrualMenu = new JMenuItem(ActiveListOfCompanies.get(i).getName(), null);
                timeOffAccrualMenu.add(myTimeOffAccrualMenu);
                myTimeOffAccrualMenu.addActionListener(new AccrualEditAction(ActiveListOfCompanies.get(i).getId()));
            }
            miscMenu.add(timeOffAccrualMenu);

        }

        if (checkSecurity(security_detail.MODULES.ADMIN_GROUP) || checkSecurity(security_detail.MODULES.ADMIN_USER)) {
            JMenu commissionsMenu = new JMenu("Edit Commissions");
            for (int i = 0; i < ActiveListOfCompanies.size(); i++) {
                JMenuItem myCommissionsMenu = new JMenuItem(ActiveListOfCompanies.get(i).getName(), null);
                commissionsMenu.add(myCommissionsMenu);
                myCommissionsMenu.addActionListener(new CommissionsEditAction(ActiveListOfCompanies.get(i).getId()));
            }
            miscMenu.add(commissionsMenu);

        }
        if (checkSecurity(security_detail.MODULES.ADMIN_GROUP) || checkSecurity(security_detail.MODULES.ADMIN_USER)) {
            JMenu editEquipmentMenu = new JMenu("Edit Equipment");
            for (int i = 0; i < ActiveListOfCompanies.size(); i++) {
                JMenuItem myCommissionsMenu = new JMenuItem(ActiveListOfCompanies.get(i).getName(), null);
                editEquipmentMenu.add(myCommissionsMenu);
                myCommissionsMenu.addActionListener(new EquipmentEditAction(ActiveListOfCompanies.get(i).getId()));
            }
            miscMenu.add(editEquipmentMenu);
        }
        if (checkSecurity(security_detail.MODULES.ADMIN_GROUP) || checkSecurity(security_detail.MODULES.ADMIN_USER)) {
            JMenu editEventTypeContactMenu = new JMenu("Edit Event Type Contacts");
            for (int i = 0; i < ActiveListOfCompanies.size(); i++) {
                JMenuItem myContactTypeMenu = new JMenuItem(ActiveListOfCompanies.get(i).getName(), null);
                editEventTypeContactMenu.add(myContactTypeMenu);
                myContactTypeMenu.addActionListener(new ContactTypeContactAction(ActiveListOfCompanies.get(i).getId()));
            }
            miscMenu.add(editEventTypeContactMenu);
        }
        miscMenu.add(noteTypeMenu);
        miscMenu.add(contactTypeMenu);
        miscMenu.add(certificationsMenu);

        if (checkSecurity(security_detail.MODULES.ADMIN_GROUP) || checkSecurity(security_detail.MODULES.ADMIN_USER)) {
            SortableMenu employeeScheduleExportMenu = new SortableMenu("Employee Schedule Export");
            setUpMenuOfCompsAndBranches(employeeScheduleExportMenu, this, this.EXPORT_EMPLOYEE_SCHEDULE);
            miscMenu.add(employeeScheduleExportMenu);
        }

        if (checkSecurity(security_detail.MODULES.ADMIN_GROUP) || checkSecurity(security_detail.MODULES.ADMIN_USER)) {
            miscMenu.add(new JSeparator());
            addMenuItem(miscMenu, "* Import Schedule", new Main_Window.UskedImport(this));
            if (checkSecurity(security_detail.MODULES.EMPLOYEE_EXPORT) || checkSecurity(security_detail.MODULES.CLIENT_EXPORT)) {
                addMenuItem(miscMenu, "* Export Schedule", new Main_Window.UskedExport(this));
            }

            JMenu exportDataParent = new JMenu("* Export Government Data");
            setUpMenuOfComps(exportDataParent, this, Main_Window.EXPORT_GOVT_DATA);
            miscMenu.add(exportDataParent);
        }

        if (Main_Window.parentOfApplication.getManagementId().equals("0") || Main_Window.parentOfApplication.getManagementId().equals("1")) {
            JMenu sForms = new JMenu("Forms");
            menuBar.add(sForms);

            JMenu personnelForm = new JMenu("Personnel Change");
            setUpMenuOfCompsAndBranches(personnelForm, this, Main_Window.PERSONNEL_FORM);
            sForms.add(personnelForm);
        }
        menuBar.add(miscMenu);

        /**
         * Messaging Menu modification by Jeffrey N. Davis Original Code
         * Commented out and noted
         */
        if (Main_Window.parentOfApplication.isUserAMemberOfGroups(new Connection(), "ADMIN", "Corporate User")) {
            JMenu allEmployeesMenu = new JMenu("Company Wide");
            setUpMenuOfComps(allEmployeesMenu, this, ALL_EMPLOYEES_MESSAGING_ACTION);
            messagingMenu.add(allEmployeesMenu);
        }

        if (Main_Window.parentOfApplication.isUserAMemberOfGroups(new Connection(), "ADMIN", "Corporate User", "Scheduling Manager")) {
            JMenu branchEmployeesMenu = new JMenu("Branch Wide");
            setUpMenuOfCompsAndBranches(branchEmployeesMenu, this, MESSAGING_ACTION);
            messagingMenu.add(branchEmployeesMenu);
        }

        //adds messageboard
        //  JMenuItem msgBoardItem = new JMenuItem("Internal Email");
        ActionListener pullUpMsgBoard = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    Main_Window.getMessageBoard().setInformation(myUser);
                    Main_Window.getMessageBoard().setVisible(true);
                    Main_Window.getMessageBoard().setMaximum(true);
                } catch (PropertyVetoException ex) {
                    Logger.getLogger(Main_Window.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        if (checkSecurity(security_detail.MODULES.MESSAGING)
                && Main_Window.parentOfApplication.isUserAMemberOfGroups(new Connection(), "ADMIN", "Corporate User", "District Manager", "Scheduling Manager")) {
            menuBar.add(messagingMenu);
        }

        Vector<SortableMenu> myVector = new Vector();

        //add a schedule emailer
        //sortableMenu emailReports = new SortableMenu("Schedule Emailer");
        //setUpMenuOfCompsAndBranches(emailReports, this, this.EMAIL_SCHEDULE);
        // myVector.add(emailReports);
        SortableMenu phoneListReportMenu = new SortableMenu("Phone List Reports");
        setUpMenuOfCompsAndBranches(phoneListReportMenu, this, this.PRINT_PHONE_REPORT);
        myVector.add(phoneListReportMenu);

        SortableMenu trainingReportMenu = new SortableMenu("Training Reports");
        setUpMenuOfCompsAndBranches(trainingReportMenu, this, this.PRINT_TRAIN_REPORT);
        myVector.add(trainingReportMenu);

        SortableMenu crossEmployeesMenu = new SortableMenu("Cross Branch Employees");
        setUpMenuOfComps(crossEmployeesMenu, this, PRINT_CROSS_EMPS);
        myVector.add(crossEmployeesMenu);

        SortableMenu corporateMenu = new SortableMenu("Corporate Communicator Usage Report");
        setUpMenuOfComps(corporateMenu, this, this.PRINT_CORP_REPORT);
        myVector.add(corporateMenu);
        
        SortableMenu logMenu = new SortableMenu("Call Log Report");
        setUpMenuOfComps(logMenu, this, this.PRINT_LOG_REPORT);
        myVector.add(logMenu);

        SortableMenu contractRenewalReportMenu = new SortableMenu("Contract Renewal Reports");
        setUpMenuOfComps(contractRenewalReportMenu, this, this.PRINT_CONTRACT_RENEWAL_REPORT);
        myVector.add(contractRenewalReportMenu);

        SortableMenu LocationSchedules = new SortableMenu("Location Schedules");
        setUpMenuOfCompsAndBranches(LocationSchedules, this, this.PRINT_LOCATION_REPORT);
        myVector.add(LocationSchedules);

        SortableMenu TotalsForLocations = new SortableMenu("Total Hours by Location Report");
        setUpMenuOfCompsAndBranches(TotalsForLocations, this, this.PRINT_TOTALS_BY_LOCATION_REPORT);
        //myVector.add(TotalsForLocations);

        SortableMenu UnconfirmedSchedules = new SortableMenu("Unconfirmed Schedules Reports");
        setUpMenuOfCompsAndBranches(UnconfirmedSchedules, this, this.PRINT_UNCON_REPORT);
        myVector.add(UnconfirmedSchedules);

        SortableMenu noteTypeReport = new SortableMenu("Note Type Report");
        this.setUpMenuOfCompsAndBranches(noteTypeReport, this, NOTE_TYPE_REPORT);
        myVector.add(noteTypeReport);

        SortableMenu OpenSchedules = new SortableMenu("Open Shift Reports");
        setUpMenuOfCompsAndBranches(OpenSchedules, this, this.PRINT_OPEN_REPORT);
        myVector.add(OpenSchedules);

        SortableMenu EmployeeSchedules = new SortableMenu("Employee Schedule Reports");
        setUpMenuOfCompsAndBranches(EmployeeSchedules, this, this.PRINT_EMP_REPORT);
        myVector.add(EmployeeSchedules);

        SortableMenu deductionReport = new SortableMenu("Employee Oustanding Deduction Reports");
        setUpMenuOfComps(deductionReport, this, this.PRINT_DEDUCTION_REPORT);
        myVector.add(deductionReport);

        SortableMenu OverUnderReport = new SortableMenu("Over/Under Reports");
        setUpMenuOfCompsAndBranches(OverUnderReport, this, this.PRINT_OVER_UNDER_REPORT);
        myVector.add(OverUnderReport);

        SortableMenu Commissionreport = new SortableMenu("Commission Report");
        setUpMenuOfComps(Commissionreport, this, this.PRINT_COMMISSION_REPORT);
        myVector.add(Commissionreport);

        SortableMenu timeOffReport = new SortableMenu("Time Off Reports");
        setUpMenuOfCompsAndBranches(timeOffReport, this, this.PRINT_TIME_OFF_REPORT);
        myVector.add(timeOffReport);

        SortableMenu PrintActiveEmployeesReport = new SortableMenu("Active Employees Reports");
        setUpMenuOfCompsAndBranches(PrintActiveEmployeesReport, this, this.PRINT_ACTIVE_EMPS_REPORT, true);
        myVector.add(PrintActiveEmployeesReport);

        if (checkSecurity(security_detail.MODULES.ADMIN_USER)) {
            SortableMenu PrintTermReport = new SortableMenu("Recently Hired Employee");
            setUpMenuOfComps(PrintTermReport, this, this.PRINT_HIRED_EMP);
            myVector.add(PrintTermReport);
        }

        SortableMenu PrintTermReport = new SortableMenu("Termination Deduction Reports");
        setUpMenuOfComps(PrintTermReport, this, this.PRINT_EMP_TERMINATION_REPORT);
        myVector.add(PrintTermReport);

        SortableMenu PrintCertificationExpReport = new SortableMenu("Certification Expiration Reports");
        setUpMenuOfCompsAndBranches(PrintCertificationExpReport, this, this.PRINT_CERTIFICAION_EXP_REPORT);
        myVector.add(PrintCertificationExpReport);

        SortableMenu PrintCertificationMissingReport = new SortableMenu("Certification Missing Reports");
        setUpMenuOfCompsAndBranches(PrintCertificationMissingReport, this, this.PRINT_CERTIFICAION_MISSING_REPORT);
        myVector.add(PrintCertificationMissingReport);

        SortableMenu PrintDemographicsReport = new SortableMenu("Demographics Report");
        setUpMenuOfCompsAndBranches(PrintDemographicsReport, this, this.PRINT_DEMOGRAPHICS, true);
        myVector.add(PrintDemographicsReport);

        //One for corporate users as well.
        SortableMenu printCallingQueue = new SortableMenu("Client Call Log (DM)");
        setUpMenuOfComps(printCallingQueue, this, this.PRINT_CALLING_QUEUE);
        myVector.add(printCallingQueue);

        if (Main_Window.parentOfApplication.isUserAMemberOfGroups(new Connection(), "Sales Manager")) {
            SortableMenu salesReport = new SortableMenu("Sales Avg Hours Report");
            setUpMenuOfComps(salesReport, this, PRINT_SALES_REPORT);
            myVector.add(salesReport);
        }

        SortableMenu printCallingCorpQueue = new SortableMenu("Client Call Log (Corp)");
        setUpMenuOfComps(printCallingCorpQueue, this, this.PRINT_CORP_CALLING_QUEUE);
        myVector.add(printCallingCorpQueue);

        SortableMenu printCCTableMenu = new SortableMenu("Corporate Communicator Table Report");
        setUpMenuOfComps(printCCTableMenu, this, this.PRINT_CC_TABLE_REPORT);
        myVector.add(printCCTableMenu);

        //sortableMenu printCorpSummary = new SortableMenu("Corporate Communicator Usage");
        //setUpMenuOfComps(printCorpSummary, this, this.PRINT_CORP_SUMMARY_REPORT);
        //myVector.add(printCorpSummary);
        SortableMenu PrintTerminatedEmployeesReport = new SortableMenu("Terminated Employee Reports");
        setUpMenuOfCompsAndBranches(PrintTerminatedEmployeesReport, this, this.PRINT_TERMINATED_EMPS);
        myVector.add(PrintTerminatedEmployeesReport);

        SortableMenu PrintEmployeeTypeMissingReport = new SortableMenu("Get Employees by Type");
        setUpMenuOfCompsAndBranches(PrintEmployeeTypeMissingReport, this, this.PRINT_EMPLOYEE_TYPE_REPORT);
        myVector.add(PrintEmployeeTypeMissingReport);

        SortableMenu PrintRollCallEmployees = new SortableMenu("Employee Rollcall Report");
        setUpMenuOfCompsAndBranches(PrintRollCallEmployees, this, this.PRINT_EMP_ROLLCALL_REPORT);
        myVector.add(PrintRollCallEmployees);

        SortableMenu PrintOvertimeReport = new SortableMenu("Overtime Report");
        setUpMenuOfCompsAndBranches(PrintOvertimeReport, this, this.PRINT_OVERTIME_REPORT);
        myVector.add(PrintOvertimeReport);

        SortableMenu PrintExtendedEmployees = new SortableMenu("Employee Schedule Reports For Multiple Weeks");
        setUpMenuOfCompsAndBranches(PrintExtendedEmployees, this, this.PRINT_EXTENDED_EMP_ROLLCALL_REPORT);
        myVector.add(PrintExtendedEmployees);

//        SortableMenu PrintCertReport = new SortableMenu("Certification Expiration Report");
//        setUpMenuOfCompsAndBranches(PrintCertReport, this, this.PRINT_CERT_REPORT);
//        myVector.add(PrintCertReport);
        SortableMenu PrintCheckIn = new SortableMenu("Checkin Reports");
        setUpMenuOfComps(PrintCheckIn, this, this.PRINT_CHECKIN_REPORT);
        myVector.add(PrintCheckIn);

        SortableMenu clientRatingReports = new SortableMenu("Client Rating Reports");
        setUpMenuOfComps(clientRatingReports, this, this.PRINT_RATING_REPORT);
        myVector.add(clientRatingReports);

        SortableMenu empCountReport = new SortableMenu("Active Employee Count");
        setUpMenuOfComps(empCountReport, this, this.ACTIVE_EMP_COUNT_REPORT);
        myVector.add(empCountReport);

        SortableMenu newEmpAlertReport = new SortableMenu("New Employee Low Hours");
        setUpMenuOfComps(newEmpAlertReport, this, this.NEW_EMPLOYEE_LOW_ALERT);
        myVector.add(newEmpAlertReport);

//        SortableMenu corpCountReport = new SortableMenu("Corporate Communicator Usage Report");
//        setUpMenuOfComps(corpCountReport, this, this.CORPORATE_COMM_USAGE_REPORT);
//        myVector.add(corpCountReport);
        SortableMenu personnelReport = new SortableMenu("Personnel Change Report");
        setUpMenuOfComps(personnelReport, this, this.PRINT_PERSONNEL_REPORT);
        myVector.add(personnelReport);

        SortableMenu employeeHireReport = new SortableMenu("New Employee Report");
        setUpMenuOfComps(employeeHireReport, this, PRINT_EMPLOYEE_HIRED_REPORT);
        myVector.add(employeeHireReport);

        SortableMenu faledCommReport = new SortableMenu("Issues with SMS / Emails");
        setUpMenuOfComps(faledCommReport, this, PRINT_FAILED_COMMUNICATION);
        myVector.add(faledCommReport);

        /*  additions by Jeffrey Davis on 04/21/2011 for EmailReportin  */
        SortableMenu emailReportMenu = new SortableMenu("Email Address Reports");
        JMenuItem inactiveClientEmalReportItem = new JMenuItem("Inactive Client");
        inactiveClientEmalReportItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //emailReportController.initInactiveClientEmailSystem(ActiveListOfCompanies);
            }
        });
        JMenuItem invalidClientEmailReportItem = new JMenuItem("Invalid Client");
        invalidClientEmailReportItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                emailReportController.initInvalidClientEmailSystem(ActiveListOfCompanies);
            }
        });
        JMenuItem employeeEmailReportItem = new JMenuItem("Invalid Employee");
        employeeEmailReportItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                emailReportController.initEmployeeEmailSystem(ActiveListOfCompanies);
            }
        });
        emailReportMenu.add(inactiveClientEmalReportItem);
        emailReportMenu.add(invalidClientEmailReportItem);
        emailReportMenu.add(employeeEmailReportItem);
        myVector.add(emailReportMenu);
        /*  additions by Jeffrey Davis on 04/21/2011 for EmailReporint complete */

        Collections.sort(myVector);
        for (int i = 0; i < myVector.size(); i++) {
            printMenu.add((SortableMenu) myVector.get(i));
        }

        JMenuItem helpMenuItem = new JMenuItem("SchedFox Help");
        helpMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rmischedule.schedule.help.BareBonesBrowserLaunch.openURL(Main_Window.LOCATION_OF_HELP_FILES);
            }
        });
        helpMenu.add(helpMenuItem);
        helpMenu.add(helpMenuItem);
        menuBar.add(printMenu);

        if (checkSecurity(security_detail.MODULES.ADMIN_USER)) {
            menuBar.add(analyticsMenu);

            JMenu exportDataMenuItem = new JMenu("Weekly Hour Totals");
            this.setUpMenuOfComps(exportDataMenuItem, this, ANALYTICS_HISTORICAL_HOURLY);
            analyticsMenu.add(exportDataMenuItem);

            JMenu costDataMenuItem = new JMenu("Profit Analysis");
            this.setUpMenuOfComps(costDataMenuItem, this, ANALYTICS_PROFIT_ANALYSIS);
            analyticsMenu.add(costDataMenuItem);

            analyticsMenu.add(new JSeparator());

            JMenu branchMenuItem = new JMenu("Branch Report");
            this.setUpMenuOfComps(branchMenuItem, this, PRINT_BRANCH_PAYROLL_REPORT);
            analyticsMenu.add(branchMenuItem);

            JMenu hourBreakDownItem = new JMenu("Hour BreakDown Report");
            this.setUpMenuOfComps(hourBreakDownItem, this, HOUR_BREAKDOWN_REPORT);
            analyticsMenu.add(hourBreakDownItem);

            JMenu branchSummaryReport = new JMenu("Branch Overtime Summary Report");
            this.setUpMenuOfComps(branchSummaryReport, this, PRINT_OVERTIME_SUMMARY_REPORT);
            analyticsMenu.add(branchSummaryReport);

            JMenu censusMenuItem = new JMenu("Census Report");
            this.setUpMenuOfComps(censusMenuItem, this, PRINT_CENSUS_REPORT);
            analyticsMenu.add(censusMenuItem);

            JMenu clientbreakdownMenu = new JMenu("Client Breakdown Reports");
            setUpMenuOfCompsAndBranches(clientbreakdownMenu, this, this.CLIENT_BREAKDOWN_REPORT, true);
            analyticsMenu.add(clientbreakdownMenu);

            JMenu payrollMenuItem = new JMenu("Profit Report");
            this.setUpMenuOfCompsAndBranches(payrollMenuItem, this, PRINT_CLIENT_PAYROLL);
            analyticsMenu.add(payrollMenuItem);
        }

        menuBar.add(helpMenu);

        //Our Windows menu--Disable it if there's nothing in it
        menuBar.add(windowMenu);
        if (this.windowMenu.getMenuComponentCount() == 0) {
            this.windowMenu.setEnabled(false);
        }

        menuBar.add(setUpMemoryAndCheckInButtons());

        if (Main_Window.compBranding.getLoginType().equals(LoginType.USER)) {
            setJMenuBar(menuBar);
            addCheckInWindow();
        } else {
            //EmployeeDashboard empDash = new EmployeeDashboard();

            if (Main_Window.compBranding.getLoginType().equals(LoginType.CLIENT)) {
                addClientLoginChangeMenu();
            }

            this.viewEmployeeSchedule();

        }

        //  added by Jeffrey Davis on 06/10/2010 to handle user's connected
        User_Connected updateUserConnected = new User_Connected(getUser());
        updateUserConnected.start();

    }

    /**
     * Small method to load the employee Schedule
     */
    private void addClientLoginChangeMenu() {
        JMenu clientOptions = new JMenu("Client Options");
        JMenuItem changeUser = new JMenuItem("Change UserID");
        changeUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                ClientLogin clientLogin = new ClientLogin(compBranding.getCompany().getDB(), true);
                Main_Window.parentOfApplication.desktop.add(clientLogin);
                clientLogin.setVisible(true);
            }
        });
        clientOptions.add(changeUser);
        JMenuBar clientMenuBar = new JMenuBar();
        clientMenuBar.add(clientOptions);
        setJMenuBar(clientMenuBar);

    }

    public void viewEmployeeSchedule() {
        User user = Main_Window.parentOfApplication.getUser();
        ArrayList<Company> companies = this.myUser.getCompanies();
        ArrayList<Branch> branches = this.myUser.getBranches();

        Main_Window.myScheduleForm.checkForAndLoadSchedule(branches.get(0).getBranchId() + "", companies.get(0).getId() + "");
        try {
            Main_Window.myScheduleForm.setIcon(false);
        } catch (Exception ex) {
        }
    }


    /* ****START OF WINDOW MENU JUNK**** */
    //function that gets called by our ComponentEventListener when a frame is hidden
    private void internalFrameHidden(ComponentEvent e) {
        //if the component was a JInternalFrame, remove it from our Windows menu
        if (e.getComponent() instanceof JInternalFrame) {
            JInternalFrame newFrame = (JInternalFrame) e.getComponent();
            removeWindowFromMenu(newFrame);
        }
    }

    //function that gets called by our ComponentEventListener when a frame is shown
    private void internalFrameShown(ComponentEvent e) {
        //if the component was a JInternalFrame, add it to our Windows menu
        if (e.getComponent() instanceof JInternalFrame) {
            JInternalFrame newFrame = (JInternalFrame) e.getComponent();
            addWindowToMenu(newFrame);
        }
    }

    //function that gets called by our InternalFrameListener when a frame is closed
    private void internalFrameDisposed(InternalFrameEvent e) {
        removeWindowFromMenu(e.getInternalFrame());
    }

    //function that gets called by our ContainerListener when a component gets added to our desktop
    private void desktopComponentAdded(ContainerEvent e) {
        //if the added component was a JInternalFrame, add the appropriate event listeners to it
        if (e.getChild() instanceof JInternalFrame) {
            JInternalFrame frame = (JInternalFrame) e.getChild();
            ComponentListener[] listenerList = frame.getComponentListeners();

            //check to see if this listener has already been added--this is needed because when a frame
            //gets iconified, it gets removed from the desktop, and added again when deiconified
            for (int i = 0; i < listenerList.length; i++) {
                if (listenerList[i].equals(this.frameHideShowListener)) {
                    return;
                }
            }
            frame.addComponentListener(this.frameHideShowListener);
            frame.addInternalFrameListener(this.frameCloseListener);
        }
    }

    //function to handle clicks on a WindowMenuItem
    private void windowMenuItemClicked(ActionEvent e) {
        try {
            WindowMenuItem clicked = (WindowMenuItem) e.getSource();
            JInternalFrame frame = clicked.getFrame();
            frame.setIcon(false);
            frame.setSelected(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //search through our Windows menu to see if an item already exists for the given frame
    //returns the WindowMenuItem associated with this frame, or null if one wasn't found
    private WindowMenuItem getWindowMenuItemForFrame(JInternalFrame frame) {
        WindowMenuItem current;
        for (int i = 0; i < this.windowMenu.getMenuComponentCount(); i++) {
            current = (WindowMenuItem) this.windowMenu.getMenuComponent(i);
            if (current.getFrame().equals(frame)) {
                return current;
            }
        }
        return null;
    }

    //try to add this frame to our Windows menu
    private void addWindowToMenu(JInternalFrame frame) {
        try {
            //if an menu item for this frame doesn't already exist, add it
            if (getWindowMenuItemForFrame(frame) == null) {
                WindowMenuItem newMenuItem = new WindowMenuItem(frame);
                newMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        windowMenuItemClicked(e);
                    }
                });
                windowMenu.add(newMenuItem);
                windowMenu.setEnabled(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //try to remove this frame from our Windows menu
    private void removeWindowFromMenu(JInternalFrame frame) {
        try {
            //if a menu item for this frame does exist, remove it
            WindowMenuItem remove = getWindowMenuItemForFrame(frame);
            if (remove != null) {
                windowMenu.remove(remove);
                if (windowMenu.getMenuComponentCount() == 0) {
                    windowMenu.setEnabled(false);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Dumb little class... just a JMenuItem with a reference to a JInternalFrame in it
    private class WindowMenuItem extends JMenuItem {

        private JInternalFrame myFrame;

        public JInternalFrame getFrame() {
            return myFrame;
        }

        public void setFrame(JInternalFrame f) {
            myFrame = f;
        }

        public WindowMenuItem(JInternalFrame f) {
            super(f.getTitle());
            myFrame = f;
        }
    }

    /* ****END OF WINDOW MENU JUNK**** */
    /**
     * Returns if User has any sort of access to given MODULE, useful to see if
     * we should hide components completely if they have absolutely no
     * privilages to given MODULE...just a shortcut to
     * security_detail.checkSecurity...
     */
    public boolean checkSecurity(security_detail.MODULES m) {
        if (loginType.equalsIgnoreCase("NewEmployee")) {
            return (true);
        }

        return mySecurity.checkSecurity(m);
    }

    /**
     * Returns if User has given access to given MODULE...
     */
    public boolean checkSecurity(security_detail.MODULES m, security_detail.ACCESS a) {
        return mySecurity.checkSecurity(m, a);
    }

    /**
     * Sets up a menu of just the companies not the branch sub menus.
     *
     * @param parentMenu
     * @param activeForm
     * @param action
     */
    public void setUpMenuOfComps(JMenu parentMenu, final CompanyMenuInterface activeForm, final String action) {
        for (int i = 0; i < ActiveListOfCompanies.size(); i++) {
            final Company currComp = ActiveListOfCompanies.get(i);
            JMenuItem tmpEdit = new JMenuItem(currComp.getName());
            tmpEdit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        setWaitCursor(true);
                        activeForm.clickedMenu(action, currComp);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        setWaitCursor(false);
                    }
                }
            });
            parentMenu.add(tmpEdit);
        }
    }

    public void setUpMenuOfCompsAndBranches(JMenu parentMenu, final CompanyBranchMenuInterface activeForm, final String action, final boolean clickCompany) {
        for (int i = 0; i < ActiveListOfCompanies.size(); i++) {
            final Company currComp = ActiveListOfCompanies.get(i);
            final Vector<Branch> currBranches = currComp.getBranches();
            JMenuItem tmpEdit;
            if (currBranches.size() == 1) {
                tmpEdit = new JMenuItem(currComp.getName());
                tmpEdit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            setWaitCursor(true);
                            activeForm.clickedMenu(action, currComp.getName(), currBranches.get(0).getBranchName(), currComp.getId(), currBranches.get(0).getBranchId() + "");
                        } catch (Exception ex) {
                        } finally {
                            setWaitCursor(false);
                        }
                    }
                });
            } else {
                tmpEdit = new JMenu(currComp.getName());
                if (clickCompany) {
                    tmpEdit.addMouseListener(new MouseListener() {
                        public void mouseClicked(MouseEvent e) {
                            try {
                                setWaitCursor(true);
                                activeForm.clickedMenu(action, currComp.getName(), "", currComp.getId(), -1 + "");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            } finally {
                                setWaitCursor(false);
                            }
                        }

                        public void mousePressed(MouseEvent e) {
                        }

                        public void mouseReleased(MouseEvent e) {
                        }

                        public void mouseEntered(MouseEvent e) {
                        }

                        public void mouseExited(MouseEvent e) {
                        }
                    });
                }
                for (int b = 0; b < currBranches.size(); b++) {
                    final int br = b;
                    JMenuItem mi = new JMenuItem(currBranches.get(br).getBranchName());
                    mi.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            try {
                                setWaitCursor(true);
                                activeForm.clickedMenu(action, currComp.getName(), currBranches.get(br).getBranchName(), currComp.getId(), currBranches.get(br).getBranchId() + "");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            } finally {
                                setWaitCursor(false);
                            }
                        }
                    });
                    tmpEdit.add(mi);
                }
            }
            parentMenu.add(tmpEdit);
        }
    }

    /**
     * Takes in a form that uses the CompanyMenuInterface and generates a menu,
     * based on action passed in...
     */
    public void setUpMenuOfCompsAndBranches(JMenu parentMenu, final CompanyBranchMenuInterface activeForm, final String action) {
        setUpMenuOfCompsAndBranches(parentMenu, activeForm, action, false);
    }

    //sets cursor
    public static void setWaitCursor(boolean wait) {
        if (wait) {
            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
            Main_Window.parentOfApplication.setCursor(hourglassCursor);
        } else {
            // reset cursor
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            Main_Window.parentOfApplication.setCursor(normalCursor);
        }
    }

    public void setupEmployeeSecurity(JMenu parentMenu) {
        for (int i = 0; i < ActiveListOfCompanies.size(); i++) {
            JMenuItem mi = new JMenuItem(ActiveListOfCompanies.get(i).getName());
            mi.addActionListener(new EmployeeSecurityAction(ActiveListOfCompanies.get(i).getId(), ActiveListOfCompanies.get(i).getName()));
            parentMenu.add(mi);
        }
    }

    public void setUpRateCode(JMenu parentMenu) {
        for (int i = 0; i < ActiveListOfCompanies.size(); i++) {
            JMenuItem mi = new JMenuItem(ActiveListOfCompanies.get(i).getName());
            mi.addActionListener(new Rate_Codes_Action(ActiveListOfCompanies.get(i).getId(), ActiveListOfCompanies.get(i).getName()));
            parentMenu.add(mi);
        }
    }

    class SortableMenu extends JMenu implements Comparable {

        public SortableMenu(String text) {
            super(text);
        }

        public int compareTo(Object o) {
            SortableMenu compMenu = (SortableMenu) o;
            return getText().compareTo(compMenu.getText());
        }
    }

    class Rate_Codes_Action implements ActionListener {

        String company_id;
        String company_name;

        public Rate_Codes_Action(String i, String n) {
            company_id = i;
            company_name = n;
        }

        public void actionPerformed(ActionEvent e) {
            if (Main_Window.rateCodeEditWindow != null) {
                Main_Window.rateCodeEditWindow.dispose();
            }
            rmischedule.rate_codes.rate_code_edit re = new rmischedule.rate_codes.rate_code_edit(company_id, company_name);
            desktop.add(re);
            re.moveToFront();
            Main_Window.rateCodeEditWindow = re;
        }
    }

    private JPanel setUpMemoryAndCheckInButtons() {
        JPanel jHolder = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 2));

        //Check In Button
        cib = new CheckInButton();
        cib.addMouseListener(new Main_Window.CheckInAction(this));
        cib.setMaximumSize(new Dimension(140, 24));
        cib.setMinimumSize(new Dimension(140, 24));
        cib.setPreferredSize(new Dimension(140, 24));
        jHolder.add(cib);

        return jHolder;

    }

    private void createListOfCompanies() {
        get_client_branch_by_management_query myCompQuery = new get_client_branch_by_management_query();
        try {
            myCompQuery.update(this.getUser().getUserId(), Main_Window.compBranding.getCompany().getDB(), Main_Window.compBranding.getLoginType().toString());
        } catch (Exception e) {
            myCompQuery.update(this.getUser().getUserId(), "", Main_Window.compBranding.getLoginType().toString());
        }
        Record_Set comps = new Record_Set();
        for (int a = 0; a < 20; a++) {
            try {
                comps = new Connection().executeQuery(myCompQuery);
                a = 9999;
            } catch (Exception e) {
                try {
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    System.out.println("attempt failed");
                }
            }
        }

        String activeComp = comps.getString("company_id");
        String compareComp = comps.getString("company_id");
        ActiveListOfCompanies = new Vector<Company>();
        do {
            Company newCompany = new Company(comps);
            ActiveListOfCompanies.add(newCompany);
            activeComp = comps.getString("company_id");
            do {
                try {
                    Branch newBranch = new Branch(comps);
                    newCompany.addBranch(newBranch);
                    comps.moveNext();
                    compareComp = comps.getString("company_id");
                } catch (Exception e) {
                }
            } while (activeComp.equals(compareComp) && !activeComp.equals(""));
        } while (!comps.getEOF());

    }

    public Vector<Company> getListOfCompanies() {
        return ActiveListOfCompanies;
    }

    /**
     * Method to get the list of available branches used to be in Branch_Lb but
     * moved here to reduce network traffic...
     */
    private void getAccessibleBranches() {
        branch_list_query myListQuery = new branch_list_query();
        Connection myConn = new Connection();
        try {
            Record_Set branchRecords = new Record_Set();
            branchId = new Vector();
            branchName = new Vector();
            try {
                myListQuery.update(myUser.getUserId());
                branchRecords = myConn.executeQuery(myListQuery);
            } catch (Exception e) {
            }

            for (int i = 0; i < branchRecords.length(); i++) {
                branchId.add(branchRecords.getString("id"));
                branchName.add(branchRecords.getString("bname"));
                branchRecords.moveNext();
            }
        } catch (Exception e) {
        }
    }

    /**
     * Method to get the list of available companies used to be in Company_LB
     * but moved here to reduce network traffic..
     */
    private void getAccessibleCompanies() {
        company_list_query myListQuery = new company_list_query();
        try {

            Connection myConn = new Connection();
            Record_Set companyRecords = new Record_Set();
            companyId = new Vector();
            companyName = new Vector();
            try {
                myListQuery.update(myUser.getUserId());
                companyRecords = myConn.executeQuery(myListQuery);
            } catch (Exception e) {
            }

            for (int i = 0; i < companyRecords.length(); i++) {
                companyId.add(companyRecords.getString("id"));
                companyName.add(companyRecords.getString("name"));
                companyRecords.moveNext();
            }
        } catch (Exception e) {
        }
    }

    /**
     * Returns true if the user is a corporate user.
     *
     * @return
     */
    public boolean isUserAMemberOfGroups(Connection myConn, String... groupstr) {
        boolean retVal = false;
        get_user_groups_query groupQuery = new get_user_groups_query();
        groupQuery.setPreparedStatement(new Object[]{
            Integer.parseInt(Main_Window.parentOfApplication.myUser.getUserId())});
        try {
            ArrayList<Group> groups = new ArrayList<Group>();
            myConn.prepQuery(groupQuery);
            Record_Set rst = myConn.executeQuery(groupQuery);
            rst.decompressData();
            for (int r = 0; r < rst.length(); r++) {
                groups.add(new Group(rst));
                rst.moveNext();
            }
            for (int g = 0; g < groups.size(); g++) {
                for (int gr = 0; gr < groupstr.length; gr++) {
                    if (groups.get(g).getGroupName().equalsIgnoreCase(groupstr[gr])) {
                        retVal = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    /**
     * Nice helper function to get company name when you just have the id
     */
    public String getCompanyNameById(String id) {
        try {
            for (int i = 0; i < ActiveListOfCompanies.size(); i++) {
                try {
                    if (ActiveListOfCompanies.get(i).getId().equals(id)) {
                        return ActiveListOfCompanies.get(i).getName();
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * Nice helper function to get branch name when you just have the id
     */
    public String getBranchNameById(String id) {
        try {
            for (int i = 0; i < ActiveListOfCompanies.size(); i++) {
                Company currComp = ActiveListOfCompanies.get(i);
                for (int x = 0; x < currComp.getBranches().size(); x++) {
                    if (currComp.getBranches().get(x).getBranchId().toString().equals(id)) {
                        return currComp.getBranches().get(x).getBranchName();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * Get the branch by passing in a branch and company id...
     */
    public Branch getBranchById(String companyId, String branchId) {
        Company currComp = getCompanyById(companyId);
        try {
            for (int x = 0; x < currComp.getBranches().size(); x++) {
                if (currComp.getBranches().get(x).getBranchId().toString().equals(branchId)) {
                    return currComp.getBranches().get(x);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Get the company by passing in a company id
     */
    public static Company getCompanyById(String cid) {
        try {
            for (int i = 0; i < ActiveListOfCompanies.size(); i++) {
                if (ActiveListOfCompanies.get(i).getId().equals(cid)) {
                    return ActiveListOfCompanies.get(i);
                }
            }
        } catch (Exception e) {
        }
        //Ok couldn't find it through cache lookup, lets hit the db.
        try {
            CompanyController companyController = CompanyController.getInstance();
            return companyController.getCompanyById(Integer.parseInt(cid));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns Vector of all Branch IDs
     */
    public Vector getBranchId() {
        if (branchId == null) {
            this.getAccessibleBranches();
        }
        return branchId;
    }

    /**
     * Returns Vector of all Branch Names
     */
    public Vector getBranchNames() {
        if (branchId == null) {
            this.getAccessibleBranches();
        }
        return branchName;
    }

    /**
     * Returns Vector of all Company IDs
     */
    public Vector getCompanyId() {
        if (companyId == null) {
            this.getAccessibleCompanies();
        }
        return companyId;
    }

    /**
     * Returns Vector of all Company Names
     */
    public Vector getCompanyNames() {
        if (companyId == null) {
            this.getAccessibleCompanies();
        }
        return companyName;
    }

    public void addMenuItem(JMenu m, String t) {
        m.add(new JMenuItem(t));
    }

    public void addCheckInWindow() {
        ciw = new rmischedule.schedule.checkincheckout.CheckInCheckOutWindow();
        desktop.add(ciw);
    }

    public void addCheckboxMenuItem(JMenu m, String t) {
        m.add(new JCheckBoxMenuItem(t));
    }

    public JMenuItem addMenuItem(JMenu m, String t, ActionListener a) {
        JMenuItem mi = new JMenuItem(t);
        mi.addActionListener(a);
        m.add(mi);
        return mi;

    }

    public void addToolBar() {
    }

    public rmischedule.security.User getUser() {
        return myUser;
    }

    /**
     * Reloads the options from the database
     */
    public void reloadAllOptions() {
        newOptions = new rmischedule.options.OptionsDataClass();
    }

    /**
     * Small method used to instantiate a new Socket Client from the information
     * contained in the myUser object and the IP address...
     */
    public ClientConnection getSocketClientFromUserInfo() {
        try {
            if (!useLocalConnection) {
                return new SocketClients(myUser.getMD5(), myUser.getUserId(), myUser.getLogin(), myUser.getManageName(), myUser.getFirstName() + " " + myUser.getLastName(), InetAddress.getLocalHost().getHostAddress());
            } else {
                LocalConnection localConn = new LocalConnection();
                localConn.initValues(myUser.getMD5(), myUser.getUserId(), myUser.getLogin(), myUser.getManageName(), myUser.getFirstName() + " " + myUser.getLastName(), InetAddress.getLocalHost().getHostAddress());
                return localConn;
            }
        } catch (Exception e) {
            System.out.println("inside catch block");
            if (!useLocalConnection) {
                return new SocketClients(myUser.getMD5(), myUser.getUserId(), myUser.getLogin(), myUser.getManageName(), myUser.getFirstName() + " " + myUser.getLastName(), "Unknown IP");
            } else {
                LocalConnection localConn = new LocalConnection();
                localConn.initValues(myUser.getMD5(), myUser.getUserId(), myUser.getLogin(), myUser.getManageName(), myUser.getFirstName() + " " + myUser.getLastName(), "Unknown IP");
                return localConn;
            }
        }
    }

    /**
     * Method to add a new Heartbeat Query if it is detected this method will
     * test if it should be added or not... Rather fast at time of write only
     * takes in for boolean methods and one String length...shouldnt slow things
     * down too much.
     */
    public void addNewHeartbeatQueryToMainCopy(GeneralQueryFormat newQuery) {
        String myQueryType = new String();
        if (newQuery.isScheduleQuery()) {
            myQueryType = SocketClients.SCHEDULE_QUERY;
        } else if (newQuery.isCheckInQuery()) {
            myQueryType = SocketClients.CHECKIN_QUERY;
        } else if (newQuery.isAvailabilityQuery()) {
            myQueryType = SocketClients.AVAILABILITY_QUERY;
        } else if (newQuery.isBannedQuery()) {
            myQueryType = SocketClients.BANNED_QUERY;
        }
        if (myQueryType.length() > 0) {
            try {
                addQueryToObject(myQueryType, (GeneralQueryFormat) newQuery);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Should only be used by addNewHeartbeatQueryToMainCopy added to ensure
     * synchronized only after testing to reduce lockup time...
     */
    private synchronized void addQueryToObject(String myType, GeneralQueryFormat newQuery) {
        mySavedQueryInformation.addHeartbeatQuery(myType, newQuery);
    }

    /**
     * Returns local copy of client information....
     */
    public ClientConnection getMyHeartbeatInfo() {
        return mySavedQueryInformation;
    }

    /**
     * Returns the RMIScheduleServer instance, singleton pattern
     *
     * @return RMIScheduleServer
     */
    public static RMIScheduleServerImpl getServer() {
        if (Main_Window.myStaticServer == null) {
            Main_Window.myStaticServer = Main_Window.establishLocalConnection();
        }
        return Main_Window.myStaticServer;
    }

    public static Double getAmountForGoodProfit() {
        return new Double(64);
    }

    public static Double getAmountForFairProfit() {
        return new Double(68);
    }

    /**
     * Ok this is used to synch up With the RMI and Socket Server by sending
     * them your unique user Id...along with creating your copy of the heartbeat
     * queries that will be resent to the server if ever needed... in the
     * mySavedQueryInformation object...
     *
     * @param userId The id of the user / employee / client
     * @param companyDB The schema we are currently in, for user this does not
     * need to be supplied since it can be determined.
     */
    public void setUser(String userId, String companyDB) {
        myUser = new rmischedule.security.User(userId, companyDB);

        this.setupReturnConnectionToServer();

        try {
            reloadAllOptions();
            myOptionsWindow = new optionWindow(rmischedule.options.IndividualOption.USER);
            desktop.add(myOptionsWindow);
            myOptionsWindow.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not load options, loaded in as employee?");
        }
        createListOfCompanies();

        if (!loginType.equalsIgnoreCase("NewEmployee")) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    addMenu();
                    setTitle("Schedfox");
                }
            });
        }
    }

    /**
     * Sets up the return connection to the server (if there is actualy one) or
     * a local connection to the server if everything is run locally.
     */
    private void setupReturnConnectionToServer() {
        boolean good = false;
        mySavedQueryInformation = getSocketClientFromUserInfo();
        try {
            Main_Window.getServer().setScheduleServer(mySavedQueryInformation);
        } catch (Exception e) {
        }
        for (int a = 0; a < 30; a++) {
            try {
                String retString = Main_Window.getServer().registerClient(mySavedQueryInformation);
                if (retString.startsWith("id:")) {
                    Main_Window.gqfId = retString;
                } else {
                    Main_Window.terminateSchedFox(retString, "Login Error!");
                }

                good = true;
                break;
            } catch (Exception ex) {
                System.out.println("blah error: " + ex.toString());
                try {
                    Thread.sleep(1000);
                } catch (Exception exs) {
                }
            }

        }

        if (!good) {
            Main_Window.terminateSchedFox("Unable to connect to the SchedFox server.", "Connection Error!");
        }

        xSocketConnection.doPing = true;
    }

    public static xGroups getGroupForm() {
        if (Main_Window.groupEditWindow == null) {
            Main_Window.groupEditWindow = new xGroups();
            Main_Window.parentOfApplication.desktop.add(Main_Window.groupEditWindow);
        }
        return Main_Window.groupEditWindow;
    }

    public static xEmployeeEdit getEmployeeEditWindow() {
        if (Main_Window.employeeEditWindow == null) {
            Main_Window.employeeEditWindow = new xEmployeeEdit();
            Main_Window.parentOfApplication.desktop.add(Main_Window.employeeEditWindow);
        }
        return Main_Window.employeeEditWindow;
    }

    public static SchedFoxCustForm getPricingForm() {
//        if(Main_Window.pricingForm==null){
        Main_Window.pricingForm = new SchedFoxCustForm();
        Main_Window.parentOfApplication.desktop.add(Main_Window.pricingForm);
//        }

        return (Main_Window.pricingForm);
    }

    public static SchedFoxInvoices getInvoiceForm() {
//        if(Main_Window.pricingForm==null){
        Main_Window.invoiceForm = new SchedFoxInvoices();
//            Main_Window.xSchedFoxInvoices=new xSchedFoxInvoices();
        Main_Window.parentOfApplication.desktop.add(Main_Window.invoiceForm);
        Dimension dim = new Dimension(Main_Window.parentOfApplication.getWidth() - 200, Main_Window.parentOfApplication.getHeight() - 200);
        Main_Window.invoiceForm.setSize(dim);

//        }
        return (Main_Window.invoiceForm);
    }
//    public static xSchedFoxInvoices getInvoiceForm(){
//        Main_Window.invoiceForm=new SchedFoxInvoices();
//        Main_Window.xSchedFoxInvoices=new xSchedFoxInvoices();
//        Main_Window.parentOfApplication.desktop.add(Main_Window.xSchedFoxInvoices);
//
//        return(Main_Window.xSchedFoxInvoices);
//    }

    public static xEmployeeEdit getEmployeeEditWindow(String loginType) {
//        if (Main_Window.employeeEditWindow == null) {
        Main_Window.employeeEditWindow = new xEmployeeEdit(loginType);
//            Component add = Main_Window.parentOfApplication.desktop.add(Main_Window.employeeEditWindow);
        Main_Window.parentOfApplication.desktop.add(Main_Window.employeeEditWindow);
//        }
        return Main_Window.employeeEditWindow;
    }

    public static xClientEdit getClientEditWindow() {
        if (Main_Window.clientEditWindow == null) {
            Main_Window.clientEditWindow = new xClientEdit();
            Main_Window.parentOfApplication.desktop.add(Main_Window.clientEditWindow);
        }
        return Main_Window.clientEditWindow;
    }

    private Vector<DShift> helper() {
        Vector<DShift> returnSet = new Vector<DShift>();

        return returnSet;
    }

    public static GenericMessageBoard getMessageBoard() {
        if (Main_Window.messageBoard == null) {
            Main_Window.messageBoard = new GenericMessageBoard();
            Main_Window.parentOfApplication.desktop.add(Main_Window.messageBoard);
        }
        return Main_Window.messageBoard;
    }

    /**
     * Added by Jeffrey Davis for messaging system on 05/21/2010
     */
    public static xMessagingEdit getMessagingEditWindow() {
        if (Main_Window.messagingEditWindow == null) {
            Main_Window.messagingEditWindow = new xMessagingEdit();
            Main_Window.parentOfApplication.desktop.add(Main_Window.messagingEditWindow);
            try {
                Main_Window.messagingEditWindow.setSelected(true);
            } catch (PropertyVetoException ex) {
                ex.printStackTrace();
            }
        }
        if (Main_Window.sortParametersWindow != null && Main_Window.sortParametersWindow.isShowing()) {
            Main_Window.sortParametersWindow.reset();
            Main_Window.sortParametersWindow.setVisible(false);
            Main_Window.messagingEditWindow.resetSubForms();
        }
        return Main_Window.messagingEditWindow;
    }

    public static EquipmentReturnWindow getEquipmentReturnWindow() {
        if (Main_Window.equipmentReturnWindow == null) {
            Main_Window.equipmentReturnWindow = new EquipmentReturnWindow();
            java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            if (screenSize.width > 950 && screenSize.height > 509) {
                Main_Window.equipmentReturnWindow.setBounds((screenSize.width - 950) / 2, (screenSize.height - 509) / 2, 950, 509);
            } else {
                Main_Window.equipmentReturnWindow.setBounds(screenSize.width / 2, (screenSize.height) / 2, screenSize.width, screenSize.height);
            }
            Main_Window.parentOfApplication.desktop.add(Main_Window.equipmentReturnWindow);
        }
        try {
            Main_Window.equipmentReturnWindow.setSelected(true);
        } catch (PropertyVetoException ex) {
            ex.printStackTrace();
        }
        return Main_Window.equipmentReturnWindow;
    }

    public static xManagementForm getManagementForm() {
        if (Main_Window.manageEditWindow == null) {
            Main_Window.manageEditWindow = new xManagementForm();
            Main_Window.parentOfApplication.desktop.add(Main_Window.manageEditWindow);
        }
        return Main_Window.manageEditWindow;
    }

    public static Connected_Users getUserListWindow() {
        if (Main_Window.userListWindow == null) {
            Main_Window.userListWindow = new Connected_Users();
            Main_Window.parentOfApplication.desktop.add(userListWindow);
        }
        return Main_Window.userListWindow;
    }

    //  addition by Jeffrey Davis on 08/03/2010 for NewUser Email Alert
    public static NewUser_Alert_Screen getEmailAlertWindow() {
        if (Main_Window.emailAlertWindow == null) {
            Main_Window.emailAlertWindow = new NewUser_Alert_Screen();
            Main_Window.parentOfApplication.desktop.add(emailAlertWindow);
        } else {
            Main_Window.emailAlertWindow.reload();
        }
        return Main_Window.emailAlertWindow;
    }
    //  addition by Jeffrey Davis on 08/03/2010 complete

    /*  addition by Jeffrey Davis on 01/20/2011 for Database Operations */
    public static Database_Operations getDatabaseOperationsForm() {
        if (Main_Window.dbOperationsForm == null) {
            Main_Window.dbOperationsForm = new Database_Operations();
            Main_Window.parentOfApplication.desktop.add(dbOperationsForm);
        } else {
            Main_Window.dbOperationsForm.reload();
        }

        return Main_Window.dbOperationsForm;
    }
    /*  addition by Jeffrey Davis on 01/20/2011 for Database Operations complete    */

    /*  addition by Jeffrey Davis on 03/22/2011 for Client_Email    */
    public static Client_Email_Form getClientEmailForm() {
        if (Main_Window.clientEmailForm == null) {
            Main_Window.clientEmailForm = new Client_Email_Form();
            Main_Window.clientEmailForm.init();
            Main_Window.parentOfApplication.desktop.add(clientEmailForm);
        } else {
            Main_Window.clientEmailForm.init();
        }

        return Main_Window.clientEmailForm;
    }
    /*  addition by Jeffrey Davis on 03/22/2011 complete    */

    public static EmployeeSecurityGroups getEmployeeSecurityGroups() {
        if (Main_Window.employeeSecurityGroup == null) {
            Main_Window.employeeSecurityGroup = new EmployeeSecurityGroups();
            Main_Window.parentOfApplication.desktop.add(employeeSecurityGroup);
        }
        return Main_Window.employeeSecurityGroup;
    }

    public static xUsersForm getUserEditWindow() {
        if (Main_Window.userEditWindow == null) {
            Main_Window.userEditWindow = new xUsersForm();
            Main_Window.parentOfApplication.desktop.add(userEditWindow);
        }
        return Main_Window.userEditWindow;
    }

    public boolean isCheckInAlert() {
        return checkInAlert;
    }

    /**
     * The following method was added by Jeffrey Davis on 06/11/2010 to handle
     * Connected_Users
     */
    public static Connected_Users getConnectedUsersWindow() {
        if (Main_Window.connectedUsersWindow == null) {
            Main_Window.connectedUsersWindow = new Connected_Users();
            Main_Window.parentOfApplication.desktop.add(Main_Window.connectedUsersWindow);
        }
        return Main_Window.connectedUsersWindow;
    }

    /**
     * Addition by Jeffrey Davis on 06/11/2010 for Connected_USers complete
     */
    public void actionPerformed(ActionEvent e) {
    }

    //memory usage menuitem
    class MemoryUssage implements ActionListener {

        JButton menuItem;
        JProgressBar jpItem;

        public MemoryUssage(JButton jmi, JProgressBar jp) {
            menuItem = jmi;
            jpItem = jp;
        }

        public void actionPerformed(ActionEvent e) {
            System.gc();
            System.gc();
            System.gc();
            System.gc();
            Runtime rt = Runtime.getRuntime();
            long totalMemory, freeMemory, usedMemory, maxMemory;
            totalMemory = rt.totalMemory();
            freeMemory = rt.freeMemory();
            maxMemory = rt.maxMemory();
            usedMemory = totalMemory - freeMemory;
            jpItem.setString(
                    String.valueOf(usedMemory / 1048576) + "mb/" //                + String.valueOf(totalMemory/1048576) + "mb/"
                    + String.valueOf(maxMemory / 1048576) + "mb");
            jpItem.setValue((int) (usedMemory / 1048576));
            jpItem.setMaximum((int) (maxMemory / 1048576));

        }
    }

    public static RMIScheduleServerImpl establishLocalConnection() {
        RMIScheduleServerImpl myServer = null;
        try {
            myServer = RMIScheduleServerImpl.getInstance();
            myServer.reloadClientDatabases();
            LocalConnection localConn = new LocalConnection();
            localConn.initValues("init", "init", "init", "init", "init", "init");
            myServer.setScheduleServer(localConn);
            loadProgress += 40;
        } catch (Exception e) {
            System.out.println("There was a problem setting up the local connection  to " + "the server...");
        }
        return myServer;
    }

    /**
     * !VERY IMPORTANT METHOD USED TO ESTABLISH A CONNECTION BETWEEN CLIENT AND
     * SERVER...USED BY CONNECTION OBJECT!
     */
    public static RMIScheduleServer establishConnectionToServer(String ipToConnectTo) {
        loadText = new String("Initializing Connection To Server");
        boolean connected = false;
        RMIScheduleServer myServer = null;
        for (int a = 0; a < 30; a++) {
            try {
                String serverName = "//" + IPLocationFile.getLOCATION_OF_RMI_SERVER() + "/scheduleServer";
                System.setProperty("java.rmi.server.disableHttp", "true");
                System.setProperty("java.rmi.server.useCodebaseOnly", "true");
                myServer = (RMIScheduleServer) Naming.lookup(serverName);
                myServer.setScheduleServer(null);
                System.out.println("Client Connected To RMI Server On " + ipToConnectTo);
                myServer.reloadClientDatabases();
                mySocketConn = new xSocketConnection();
                connected = true;
                loadProgress += 40;
                break;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error with connection");
                try {
                    Thread.sleep(1000);
                } catch (Exception ex) {
                }
            }
        }

        if (!connected) {
            if (mySplash != null) {
                mySplash.dispose();
            }
            terminateSchedFox("Unable to connect to the SchedFox server.", "Connection Error!");
        }

        Main_Window.parentOfApplication.setTitle(Main_Window.parentOfApplication.getTitle() + "SchedFox  (" + IPLocationFile.getServerDescription() + ")");
        return myServer;
    }

    class EmployeeSecurityAction implements ActionListener {

        private String companyId;
        private String companyName;

        public EmployeeSecurityAction(String companyId, String companyName) {
            this.companyId = companyId;
            this.companyName = companyName;
        }

        public void actionPerformed(ActionEvent e) {
            Main_Window.getEmployeeSecurityGroups().setCompany(companyId);
            Main_Window.getEmployeeSecurityGroups().setVisible(true);
            try {
                Main_Window.getEmployeeSecurityGroups().setIcon(false);
            } catch (Exception ex) {
            }
            try {
                Main_Window.getEmployeeSecurityGroups().setSelected(true);
            } catch (Exception ex) {
            }
        }
    }

    class LogoutAction implements ActionListener {

        public LogoutAction(Main_Window parent) {
        }

        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    class SubscriptionCancelItem implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            /*CancelSubscription cansub =
             new CancelSubscription(parentOfApplication, Main_Window.parentOfApplication.desktop, Main_Window.parentOfApplication.myUser.getUserId());
             // parentOfApplication.getContentPane().add(sub);
             desktop.add(cansub);
             cansub.setVisible(true);*/
        }
    }

    class CreatePayment implements ActionListener {

        private int compId;

        public CreatePayment(String compId) {
            try {
                this.compId = Integer.parseInt(compId);
            } catch (Exception e) {
            }
        }

        public void actionPerformed(ActionEvent evt) {
            /* ChoosePaymentTypeScreen paymentScreen =
             new ChoosePaymentTypeScreen(Main_Window.parentOfApplication, true, Integer.parseInt(myUser.getUserId()), this.compId);
             paymentScreen.setVisible(true);*/
        }
    }

    class BillingSummaryItem implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            int[] companyIds = new int[ActiveListOfCompanies.size()];
            for (int c = 0; c < ActiveListOfCompanies.size(); c++) {
                companyIds[c] = Integer.parseInt(ActiveListOfCompanies.get(c).getId());
            }
            /*TransactionSummary bill =
             new TransactionSummary(parentOfApplication, companyIds, Integer.parseInt(myUser.getUserId()) + "");
             desktop.add(bill);
             bill.setVisible(true);*/
        }
    }

    class AdminUserAction implements ActionListener {

        Main_Window parent;

        public AdminUserAction(Main_Window Parent) {
            parent = Parent;
        }

        public void actionPerformed(ActionEvent e) {
            Main_Window.getUserEditWindow().setVisible(true);
            try {
                Main_Window.getUserEditWindow().setIcon(false);
            } catch (Exception ex) {
            }
            try {
                userEditWindow.setSelected(true);
            } catch (Exception ex) {
            }
        }
    }
    //modified code Jaya

    class AdminGroupAction implements ActionListener {

        Main_Window parent;

        public AdminGroupAction(Main_Window Parent) {
            parent = Parent;
        }

        public void actionPerformed(ActionEvent e) {
            getGroupForm().setVisible(true);
            try {
                getGroupForm().setIcon(false);
            } catch (Exception ex) {
            }
            try {
                getGroupForm().setSelected(true);
            } catch (Exception ex) {
            }
        }
    }

    class SearchEmployeeAction implements ActionListener {

        Main_Window parent;

        public SearchEmployeeAction(Main_Window Parent) {
            parent = Parent;
        }

        public void actionPerformed(ActionEvent e) {
            EnterSSNSearch ssnSearch = new EnterSSNSearch(Main_Window.parentOfApplication,
                    true, new Connection(), Main_Window.getEmployeeEditWindow());
            ssnSearch.setVisible(true);
        }
    }

    class AdminConnectedAction implements ActionListener {

        Main_Window parent;

        public AdminConnectedAction(Main_Window mw) {
            parent = mw;
        }

        public void actionPerformed(ActionEvent e) {
            getConnectedUsersWindow().setVisible(true);
        }
    }

    //  addition by Jeffrey Davis on 08/03/2010 for New User Email Alerts
    class AdminNewUserEmailAlertsAction implements ActionListener {

        Main_Window parent;

        public AdminNewUserEmailAlertsAction(Main_Window mw) {
            this.parent = mw;
        }

        public void actionPerformed(ActionEvent e) {
            getEmailAlertWindow().setVisible(true);
        }
    }

    class AdminCommissionCap implements ActionListener {

        public AdminCommissionCap(Main_Window mw) {
        }

        public void actionPerformed(ActionEvent e) {
            CommissionCapDialog commCapDialog = new CommissionCapDialog(Main_Window.parentOfApplication, true);
            //Main_Window.parentOfApplication.desktop.add(commCapDialog);
            commCapDialog.setVisible(true);
        }
    }

    /*  addition by Jeffrey Davis on 01/20/2011 for Database Operations */
    class AdminDatabaseOperationsAction implements ActionListener {

        Main_Window parent;

        public AdminDatabaseOperationsAction(Main_Window mw) {
            this.parent = mw;
        }

        public void actionPerformed(ActionEvent e) {
            getDatabaseOperationsForm().setVisible(true);
        }
    }

    class AdminMapAction implements ActionListener {

        public AdminMapAction() {
        }

        public void actionPerformed(ActionEvent e) {
            MappingFrame mapFrame = new MappingFrame();
            Main_Window.parentOfApplication.desktop.add(mapFrame);
            mapFrame.setVisible(true);
        }
    }

    class InvoicingAction implements ActionListener {

        public InvoicingAction() {
        }

        public void actionPerformed(ActionEvent e) {
            InvoiceClientManagement invoicing = new InvoiceClientManagement();
            Main_Window.parentOfApplication.desktop.add(invoicing);
            invoicing.setVisible(true);
        }
    }

    /*  addition by Jeffrey Davis on 03/22/2011 for Client_Email    */
    class ClientEmailAction implements ActionListener {

        Main_Window parent;

        public ClientEmailAction(Main_Window mw) {
            this.parent = mw;
        }

        public void actionPerformed(ActionEvent e) {
            try {
                Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
                setCursor(hourglassCursor);

                getClientEmailForm().setVisible(true);
            } catch (Exception ex) {  /* do nothing, exception handled at lower lever  */ } finally {
                Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                setCursor(normalCursor);
            }
        }
    }
    /*  addition by Jeffrey Davis on 03/22/2011 complete    */

    class AdminManagementAction implements ActionListener {

        Main_Window parent;

        public AdminManagementAction(Main_Window mw) {
            parent = mw;
        }

        public void actionPerformed(ActionEvent e) {
            getManagementForm().setVisible(true);
            try {
                getManagementForm().setIcon(false);
            } catch (Exception ex) {
            }
            try {
                getManagementForm().setSelected(true);
            } catch (Exception ex) {
            }
        }
    }

    class SchedTexting implements ActionListener {

        public SchedTexting() {
        }

        public void actionPerformed(ActionEvent e) {

            rmischedule.schedule.help.BareBonesBrowserLaunch.openURL(Main_Window.LOCATION_OF_TEXTING_CHANGE + "?new=" + myUser.getMD5());

        }
    }

    class ClientContactTypeEditAction implements ActionListener {

        private String compId;

        public ClientContactTypeEditAction(String cid) {
            compId = cid;
        }

        public void actionPerformed(ActionEvent e) {
            EditClientContactTypes myContactType = new EditClientContactTypes(compId);
            desktop.add(myContactType);
            myContactType.setVisible(true);
        }
    }

    class NoteTypeEditAction implements ActionListener {

        private String compId;

        public NoteTypeEditAction(String cid) {
            compId = cid;
        }

        public void actionPerformed(ActionEvent e) {
            EditNoteTypes myNoteType = new EditNoteTypes(compId);
            desktop.add(myNoteType);
            myNoteType.setVisible(true);
        }
    }

    class CertificationEditAction implements ActionListener {

        private String compId;

        public CertificationEditAction(String cid) {
            compId = cid;
        }

        public void actionPerformed(ActionEvent e) {
            EditCertificationsTypes myCertType = new EditCertificationsTypes(compId);
            desktop.add(myCertType);
            myCertType.setVisible(true);
        }
    }

    class AccrualEditAction implements ActionListener {

        private String compId;

        public AccrualEditAction(String cid) {
            this.compId = cid;
        }

        public void actionPerformed(ActionEvent e) {
            SetupTimeOffIntervalDialog setupDialog
                    = new SetupTimeOffIntervalDialog(Main_Window.parentOfApplication, true, compId);
            setupDialog.setVisible(true);
        }
    }

    private class ContactTypeContactAction implements ActionListener {

        private String compId;

        public ContactTypeContactAction(String cid) {
            this.compId = cid;
        }

        public void actionPerformed(ActionEvent e) {
            EventTypeContactDialog equipDialog
                    = new EventTypeContactDialog(Main_Window.parentOfApplication, true, compId);
            equipDialog.setVisible(true);
        }
    }

    class EquipmentEditAction implements ActionListener {

        private String compId;

        public EquipmentEditAction(String cid) {
            this.compId = cid;
        }

        public void actionPerformed(ActionEvent e) {
            ManageEquipmentDialog equipDialog
                    = new ManageEquipmentDialog(Main_Window.parentOfApplication, true, compId);
            equipDialog.setVisible(true);
        }
    }

    class CommissionsEditAction implements ActionListener {

        private String compId;

        public CommissionsEditAction(String cid) {
            this.compId = cid;
        }

        public void actionPerformed(ActionEvent e) {
            SetupCommissionsDialog setupDialog
                    = new SetupCommissionsDialog(Main_Window.parentOfApplication, true, compId);
            setupDialog.setVisible(true);
        }
    }

    class ManagementEditAction implements ActionListener {

        Main_Window parent;

        public ManagementEditAction(Main_Window Parent) {
            parent = Parent;
        }

        public void actionPerformed(ActionEvent e) {
            //schedule.client.Management_List cm = new schedule.client.Management_List(parent);
        }
    }

    class OptionAction implements ActionListener {

        Main_Window parent;

        public OptionAction(Main_Window Parent) {
            parent = Parent;
        }

        public void actionPerformed(ActionEvent e) {
            myOptionsWindow.setVisible(true);
        }
    }

    class EditAccountAction implements ActionListener {

        public EditAccountAction() {
        }

        public void actionPerformed(ActionEvent e) {
            xDisplayAccountInfoPanel myInfoPanel = new xDisplayAccountInfoPanel(parentOfApplication, true);
            myInfoPanel.setVisible(true);
        }
    }

    class CheckInAction extends MouseAdapter {

        Main_Window parent;

        public CheckInAction(Main_Window Parent) {
            parent = Parent;
        }

        public void mouseClicked(MouseEvent e) {
            ciw.setVisible(true);
            try {
                ciw.setMaximum(true);
            } catch (Exception ex) {
            }
            try {
                ciw.setIcon(false);
            } catch (Exception ex) {
            }
            try {
                ciw.setSelected(true);
            } catch (Exception ex) {
            }
        }
    }

    class OpenShiftAlert implements ActionListener {

        public OpenShiftAlert() {
        }

        public void actionPerformed(ActionEvent e) {
            myOpenShiftAlertWindow.showMe(true);
            crapoutserverquery myKillQuery = new crapoutserverquery();
            System.out.println("Starting kill query");
        }
    }

    class UskedImport implements ActionListener {

        Main_Window parent;

        public UskedImport(Main_Window Parent) {
            parent = Parent;
        }

        public void actionPerformed(ActionEvent e) {
            new rmischedule.data_import.ultra32.usked_main(parent);
        }
    }

    class PersonnelForm {

        private String companyId;
        private String branchId;

        public PersonnelForm(String companyId, String branchId) {
            this.companyId = companyId;
            this.branchId = branchId;

            PersonnelChangeForm personnelForm = new PersonnelChangeForm(parentOfApplication,
                    null, null, Integer.parseInt(companyId), Integer.parseInt(branchId), true);
            personnelForm.setVisible(true);
        }
    }

    /**
     * Exampe invoice creation!
     */
    class printExampleInvoice implements ActionListener {

        public printExampleInvoice() {
        }

        public void actionPerformed(ActionEvent e) {
        }
    }

    class UskedExport implements ActionListener {

        Main_Window parent;

        public UskedExport(Main_Window Parent) {
            parent = Parent;
        }

        public void actionPerformed(ActionEvent e) {
            new rmischedule.data_export.ultra32.usked_Main_Export(parent);
        }
    }

    class PrintTimeOffReport {

        public PrintTimeOffReport(String company, String branch) {
            try {
                String startWeek = "";
                String endWeek = "";
                GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(parentOfApplication, true, GetDatesForPrintDialog.DAY, true);
                Integer companyId = Integer.parseInt(company);
                Calendar[] returnval = myDateDialog.getDatesFromDialog(companyId);
                if (returnval[0] == null) {
                    return;
                }
                startWeek = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(returnval[0]);
                endWeek = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(returnval[1]);

                Company myCompany = getCompanyById(company);
                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/time_off_detail.jasper");

                Connection myConn = new Connection();
                myConn.myCompany = company;
                myConn.myBranch = branch;

                Hashtable parameters = new Hashtable();
                parameters.put("schema", myCompany.getDB());
                parameters.put("branch_id", new Integer(Integer.parseInt(branch)));
                parameters.put("startDate", startWeek);
                parameters.put("endDate", endWeek);

                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, RMIScheduleServerImpl.getConnection().generateConnection());
                if (AjaxSwingManager.isAjaxSwingRunning()) {
                    byte[] fileBytes = JasperExportManager.exportReportToPdf(report);
                    ClientAgent.getCurrentInstance().print(fileBytes);
                } else {
                    IReportViewer viewer = new IReportViewer(report);
                    Main_Window.parentOfApplication.desktop.add(viewer);
                    viewer.showForm();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class PrintCertificationMissing {

        public PrintCertificationMissing(String company, String branchId) {
            try {
                Company myCompany = getCompanyById(company);
                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/NoCertfications.jasper");

                Connection myConn = new Connection();
                myConn.myCompany = company;
                myConn.myBranch = branchId;

                ShowCertifications showCerts = new ShowCertifications(
                        Main_Window.parentOfApplication, true, myConn);
                showCerts.setVisible(true);
                Certification selectedCert = showCerts.getSelectedCert();

                Hashtable parameters = new Hashtable();
                parameters.put("active_db", myCompany.getDB());
                parameters.put("branch_id", new Integer(Integer.parseInt(branchId)));
                try {
                    parameters.put("cert_id", selectedCert.getCert_id());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    parameters.put("show_ssn", Main_Window.parentOfApplication.getUser().getCanViewSsn());
                } catch (Exception exe) {
                    parameters.put("show_ssn", false);
                }
                parameters.put("employee_types", showCerts.getSelectedEmployeeTypes());
                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, RMIScheduleServerImpl.getConnection().generateConnection());
                if (AjaxSwingManager.isAjaxSwingRunning()) {
                    byte[] fileBytes = JasperExportManager.exportReportToPdf(report);
                    ClientAgent.getCurrentInstance().print(fileBytes);
                } else {
                    IReportViewer viewer = new IReportViewer(report);
                    Main_Window.parentOfApplication.desktop.add(viewer);
                    viewer.showForm();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class PrintEmployeesHired {

        public PrintEmployeesHired(String company) {
            try {
                Company myCompany = getCompanyById(company);
                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/new_employees.jasper");

                Connection myConn = new Connection();
                myConn.myCompany = company;

                Hashtable parameters = new Hashtable();

                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, RMIScheduleServerImpl.getConnection().generateConnection());
                if (AjaxSwingManager.isAjaxSwingRunning()) {
                    byte[] fileBytes = JasperExportManager.exportReportToPdf(report);
                    ClientAgent.getCurrentInstance().print(fileBytes);
                } else {
                    IReportViewer viewer = new IReportViewer(report);
                    Main_Window.parentOfApplication.desktop.add(viewer);
                    viewer.showForm();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class PrintTerminatedEmployees {

        public PrintTerminatedEmployees(String company, String branchId) {
            try {
                Company myCompany = getCompanyById(company);
                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/EmployeeTerminated.jasper");

                Connection myConn = new Connection();
                myConn.myCompany = company;
                myConn.myBranch = branchId;

                GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(Main_Window.parentOfApplication, true, GetDatesForPrintDialog.WEEK, true);
                Calendar[] returnval = myDateDialog.getDatesFromDialog(Integer.parseInt(company));

                Hashtable parameters = new Hashtable();
                parameters.put("active_db", myCompany.getDB());
                parameters.put("branch_id", new Integer(Integer.parseInt(branchId)));
                parameters.put("start_date", returnval[0].getTime());
                parameters.put("end_date", returnval[1].getTime());
                try {
                    parameters.put("show_ssn", Main_Window.parentOfApplication.getUser().getCanViewSsn());
                } catch (Exception exe) {
                    parameters.put("show_ssn", false);
                }

                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, RMIScheduleServerImpl.getConnection().generateConnection());
                if (AjaxSwingManager.isAjaxSwingRunning()) {
                    byte[] fileBytes = JasperExportManager.exportReportToPdf(report);
                    ClientAgent.getCurrentInstance().print(fileBytes);
                } else {
                    IReportViewer viewer = new IReportViewer(report);
                    Main_Window.parentOfApplication.desktop.add(viewer);
                    viewer.showForm();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public class PrintCallLogReport {

        public PrintCallLogReport(String company) {
            try {
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

                Company myCompany = getCompanyById(company);
                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/call_log_report.jasper");

                Connection myConn = new Connection();
                myConn.myCompany = company;

                GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(Main_Window.parentOfApplication, true, GetDatesForPrintDialog.WEEK, true);
                Calendar[] returnval = myDateDialog.getDatesFromDialog(Integer.parseInt(company));

                Hashtable parameters = new Hashtable();
                parameters.put("schema", myCompany.getDB());
                parameters.put("startDate", myFormat.format(returnval[0].getTime()));
                parameters.put("endDate", myFormat.format(returnval[1].getTime()));

                java.sql.Connection myConnection = RMIScheduleServerImpl.getConnection().generateConnection();
                myConnection.prepareStatement("set search_path='champion_db';").execute();

                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, myConnection);
                if (AjaxSwingManager.isAjaxSwingRunning()) {
                    byte[] fileBytes = JasperExportManager.exportReportToPdf(report);
                    ClientAgent.getCurrentInstance().print(fileBytes);
                } else {
                    IReportViewer viewer = new IReportViewer(report);
                    Main_Window.parentOfApplication.desktop.add(viewer);
                    viewer.showForm();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class PrintCoporateCommReport {

        public PrintCoporateCommReport(String company) {
            try {
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

                Company myCompany = getCompanyById(company);
                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/problem_solver_range_report.jasper");

                Connection myConn = new Connection();
                myConn.myCompany = company;

                GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(Main_Window.parentOfApplication, true, GetDatesForPrintDialog.WEEK, true);
                Calendar[] returnval = myDateDialog.getDatesFromDialog(Integer.parseInt(company));

                Hashtable parameters = new Hashtable();
                parameters.put("schema", myCompany.getDB());
                parameters.put("startDate", myFormat.format(returnval[0].getTime()));
                parameters.put("endDate", myFormat.format(returnval[1].getTime()));

                java.sql.Connection myConnection = RMIScheduleServerImpl.getConnection().generateConnection();
                myConnection.prepareStatement("set search_path='champion_db';").execute();

                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, myConnection);
                if (AjaxSwingManager.isAjaxSwingRunning()) {
                    byte[] fileBytes = JasperExportManager.exportReportToPdf(report);
                    ClientAgent.getCurrentInstance().print(fileBytes);
                } else {
                    IReportViewer viewer = new IReportViewer(report);
                    Main_Window.parentOfApplication.desktop.add(viewer);
                    viewer.showForm();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class PrintCrossBranchEmployees {

        public PrintCrossBranchEmployees(String company) {
            try {
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

                Company myCompany = getCompanyById(company);
                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/cross_branch_report.jasper");

                Connection myConn = new Connection();
                myConn.myCompany = company;

                GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(Main_Window.parentOfApplication, true, GetDatesForPrintDialog.WEEK, true);
                Calendar[] returnval = myDateDialog.getDatesFromDialog(Integer.parseInt(company));

                Hashtable parameters = new Hashtable();
                parameters.put("schema", myCompany.getDB());
                parameters.put("startDate", myFormat.format(returnval[0].getTime()));
                parameters.put("endDate", myFormat.format(returnval[1].getTime()));

                java.sql.Connection myConnection = RMIScheduleServerImpl.getConnection().generateConnection();
                myConnection.prepareStatement("set search_path='champion_db';").execute();

                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, myConnection);
                if (AjaxSwingManager.isAjaxSwingRunning()) {
                    byte[] fileBytes = JasperExportManager.exportReportToPdf(report);
                    ClientAgent.getCurrentInstance().print(fileBytes);
                } else {
                    IReportViewer viewer = new IReportViewer(report);
                    Main_Window.parentOfApplication.desktop.add(viewer);
                    viewer.showForm();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class PrintCallingQueue {

        public PrintCallingQueue(String company) {
            try {
                Company myCompany = getCompanyById(company);
                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/DMMissingClientContact.jasper");

                Connection myConn = new Connection();
                myConn.myCompany = company;

                Hashtable<String, Vector<CompanyInformationObj>> companyViewOptions
                        = Main_Window.parentOfApplication.getCompanyViewOptions(myConn.myCompany);

                Hashtable parameters = new Hashtable();
                parameters.put("schema", myCompany.getDB());
                parameters.put("numberDays", Integer.parseInt(Main_Window.parentOfApplication.getCompanyInformation(companyViewOptions, CompanyLoginInformation.CALL_QUEUE_DM_ROTATION).getOption_value()));
                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, RMIScheduleServerImpl.getConnection().generateConnection());
                IReportViewer viewer = new IReportViewer(report);
                Main_Window.parentOfApplication.desktop.add(viewer);
                viewer.showForm();
            } catch (Exception e) {
            }
        }
    }

    class PrintSalesReport {

        public PrintSalesReport(String company) {
            try {
                GetDatesWithUserSelectionForPrintDialog myDateDialog = new GetDatesWithUserSelectionForPrintDialog(parentOfApplication, true,
                        GetDatesForPrintDialog.WEEK, true, company, true, "Sales Manager", "Sales");

                //myDateDialog.setToPreviousWeek();
                Integer companyId = Integer.parseInt(company);
                Calendar[] returnval = myDateDialog.getDatesFromDialog(companyId);
                if (returnval[0] == null) {
                    return;
                }
                String[] dates = new String[2];
                SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
                returnval[1].add(Calendar.DAY_OF_WEEK, -1);
                dates[0] = myFormat.format(returnval[0].getTime());
                dates[1] = myFormat.format(returnval[1].getTime());

                ArrayList<Integer> selectedDMs = myDateDialog.getSelectedUserIds();

                Company myCompany = getCompanyById(company);
                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/get_sales_figures_report.jasper");

                Connection myConn = new Connection();
                myConn.myCompany = company;

                Hashtable<String, Vector<CompanyInformationObj>> companyViewOptions
                        = Main_Window.parentOfApplication.getCompanyViewOptions(myConn.myCompany);

                Hashtable parameters = new Hashtable();
                parameters.put("active_db", myCompany.getDB());
                parameters.put("startDate", dates[0]);
                parameters.put("endDate", dates[1]);
                parameters.put("sales_list", selectedDMs);

                java.sql.Connection myConnect = RMIScheduleServerImpl.getConnection().generateConnection();
                try {
                    PreparedStatement pstmt = myConnect.prepareStatement("SET search_path='" + myCompany.getDB().replaceAll("'", "''") + "'");
                    pstmt.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, myConnect);
                IReportViewer viewer = new IReportViewer(report);
                Main_Window.parentOfApplication.desktop.add(viewer);
                viewer.showForm();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class PrintCorpSummary {

        public PrintCorpSummary(String company, HashMap<String, Object> params) {
            try {
                Company myCompany = getCompanyById(company);

                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/corporate_communicator_usage.jasper");

                params.put("schema", myCompany.getDB());
                JasperPrint report = JasperFillManager.fillReport(reportStream, params, RMIScheduleServerImpl.getConnection().generateConnection());
                IReportViewer viewer = new IReportViewer(report);
                Main_Window.parentOfApplication.desktop.add(viewer);
                viewer.showForm();

            } catch (Exception e) {
            }
        }
    }

    class PrintCCTableReport {

        public PrintCCTableReport(String company) {
            try {
                GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(Main_Window.parentOfApplication, true, GetDatesForPrintDialog.WEEK, true);
                Calendar[] returnval = myDateDialog.getDatesFromDialog(Integer.parseInt(company));

                if (returnval[0] == null) {
                    return;
                }
                String[] dates = new String[2];
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
                returnval[1].add(Calendar.DAY_OF_WEEK, -1);
                dates[0] = myFormat.format(returnval[0].getTime());
                dates[1] = myFormat.format(returnval[1].getTime());
                if (dates != null) {
                    Company myCompany = getCompanyById(company);
                    InputStream reportStream
                            = getClass().getResourceAsStream("/rmischedule/ireports/problem_solver_range_table_report.jasper");

                    Connection myConn = new Connection();
                    myConn.myCompany = company;

                    Hashtable parameters = new Hashtable();
                    parameters.put("schema", myCompany.getDB());
                    parameters.put("startDate", dates[0]);
                    parameters.put("endDate", dates[1]);
                    java.sql.Connection myConnect = RMIScheduleServerImpl.getConnection().generateConnection();
                    JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, myConnect);
                    IReportViewer viewer = new IReportViewer(report);
                    Main_Window.parentOfApplication.desktop.add(viewer);
                    viewer.showForm();
                }
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
    }

    class PrintCorporateCallingQueue {

        public PrintCorporateCallingQueue(String company) {
            try {
                Company myCompany = getCompanyById(company);
                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/CorpMissingClientContact.jasper");

                Connection myConn = new Connection();
                myConn.myCompany = company;

                Hashtable<String, Vector<CompanyInformationObj>> companyViewOptions
                        = Main_Window.parentOfApplication.getCompanyViewOptions(myConn.myCompany);

                Hashtable parameters = new Hashtable();
                parameters.put("schema", myCompany.getDB());
                parameters.put("numberDays", Integer.parseInt(Main_Window.parentOfApplication.getCompanyInformation(companyViewOptions, CompanyLoginInformation.CALL_QUEUE_CORP_ROTATION).getOption_value()));
                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, RMIScheduleServerImpl.getConnection().generateConnection());
                IReportViewer viewer = new IReportViewer(report);
                Main_Window.parentOfApplication.desktop.add(viewer);
                viewer.showForm();
            } catch (Exception e) {
            }
        }
    }

    class PrintEmployeeType {

        public PrintEmployeeType(String company, String branchId) {
            try {
                Company myCompany = getCompanyById(company);
                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/EmployeesByType.jasper");

                Connection myConn = new Connection();
                myConn.myCompany = company;
                myConn.myBranch = branchId;

                ShowEmployeeTypes employeeTypes = new ShowEmployeeTypes(
                        Main_Window.parentOfApplication, true, myConn);
                employeeTypes.setVisible(true);

                Hashtable parameters = new Hashtable();
                parameters.put("active_db", myCompany.getDB());
                parameters.put("branch_id", new Integer(Integer.parseInt(branchId)));
                parameters.put("employee_types", employeeTypes.getSelectedEmployeeTypes());
                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, RMIScheduleServerImpl.getConnection().generateConnection());
                IReportViewer viewer = new IReportViewer(report);
                Main_Window.parentOfApplication.desktop.add(viewer);
                viewer.showForm();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class PrintCertificationExp {

        public PrintCertificationExp(String company, String branchId) {
            try {
                Company myCompany = getCompanyById(company);
                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/CertificationExpiration.jasper");

                Hashtable parameters = new Hashtable();
                parameters.put("active_db", myCompany.getDB());
                parameters.put("branch_id", new Integer(Integer.parseInt(branchId)));
                try {
                    parameters.put("show_ssn", Main_Window.parentOfApplication.getUser().getCanViewSsn());
                } catch (Exception exe) {
                    parameters.put("show_ssn", false);
                }
                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, RMIScheduleServerImpl.getConnection().generateConnection());
                IReportViewer viewer = new IReportViewer(report);
                Main_Window.parentOfApplication.desktop.add(viewer);
                viewer.showForm();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class PrintEmployeeTerminationReport {

        public PrintEmployeeTerminationReport(String company) {
            try {
                GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(parentOfApplication, true, GetDatesForPrintDialog.WEEK);
                myDateDialog.setToPreviousWeek();
                Integer companyId = Integer.parseInt(company);
                Calendar[] returnval = myDateDialog.getDatesFromDialog(companyId);
                if (returnval[0] == null) {
                    return;
                }
                String[] dates = new String[2];
                SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
                returnval[1].add(Calendar.DAY_OF_WEEK, -1);
                dates[0] = myFormat.format(returnval[0].getTime());
                dates[1] = myFormat.format(returnval[1].getTime());
                if (dates != null) {
                    Company myCompany = getCompanyById(company);
                    InputStream reportStream
                            = getClass().getResourceAsStream("/rmischedule/ireports/employee_termination_report.jasper");

                    Connection myConn = new Connection();
                    myConn.myCompany = company;

                    Hashtable parameters = new Hashtable();
                    parameters.put("schema", myCompany.getDB());
                    parameters.put("branch_id", new Integer(-1));
                    parameters.put("start_date", dates[0]);
                    parameters.put("end_date", dates[1]);
                    java.sql.Connection myConnect = RMIScheduleServerImpl.getConnection().generateConnection();
                    JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, myConnect);
                    IReportViewer viewer = new IReportViewer(report);
                    Main_Window.parentOfApplication.desktop.add(viewer);
                    viewer.showForm();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class PrintFailedCommunication {

        public PrintFailedCommunication(String company) {
            try {
                Company myCompany = getCompanyById(company);

                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/FailedCommunication.jasper");

                Connection myConn = new Connection();
                myConn.myCompany = company;
                Hashtable parameters = new Hashtable();
                parameters.put("active_db", myCompany.getDB());
                java.sql.Connection myConnect = RMIScheduleServerImpl.getConnection().generateConnection();

                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, myConnect);
                IReportViewer viewer = new IReportViewer(report);
                Main_Window.parentOfApplication.desktop.add(viewer);
                viewer.showForm();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class PrintNewEmployeeLowAlert {

        public PrintNewEmployeeLowAlert(String company) {
            try {
                Company myCompany = getCompanyById(company);

                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/new_employee_low_hour_alert_report.jasper");

                Connection myConn = new Connection();
                myConn.myCompany = company;
                Hashtable parameters = new Hashtable();
                parameters.put("active_schema", myCompany.getDB());
                java.sql.Connection myConnect = RMIScheduleServerImpl.getConnection().generateConnection();

                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, myConnect);
                IReportViewer viewer = new IReportViewer(report);
                Main_Window.parentOfApplication.desktop.add(viewer);
                viewer.showForm();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class PrintRatingReport {

        public PrintRatingReport(String company) {
            try {
                Company myCompany = getCompanyById(company);

                GetDatesWithUserSelectionForPrintDialog myDateDialog = new GetDatesWithUserSelectionForPrintDialog(parentOfApplication, true,
                        GetDatesForPrintDialog.WEEK, true, myCompany.getId(), false, "District Manager", "Sales Manager");
                Integer companyIdInt = Integer.parseInt(myCompany.getId());
                Calendar[] returnval = myDateDialog.getDatesFromDialog(companyIdInt);
                ArrayList<Integer> selectedDMs = myDateDialog.getSelectedUserIds();

                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/client_rating.jasper");

                Connection myConn = new Connection();
                myConn.myCompany = company;
                Hashtable parameters = new Hashtable();
                parameters.put("active_schema", myCompany.getDB());
                parameters.put("dm_ints", selectedDMs);
                java.sql.Connection myConnect = RMIScheduleServerImpl.getConnection().generateConnection();

                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, myConnect);
                IReportViewer viewer = new IReportViewer(report);
                Main_Window.parentOfApplication.desktop.add(viewer);
                viewer.showForm();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class PrintCensusReport {

        public PrintCensusReport(String company) {
            InputStream reportStream
                    = getClass().getResourceAsStream("/rmischedule/ireports/census_report.jasper");

            Connection myConn = new Connection();
            myConn.myCompany = company;

            try {
                java.sql.Connection myConnect = RMIScheduleServerImpl.getConnection().generateConnection();

                GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(parentOfApplication, true, GetDatesForPrintDialog.WEEK, true);
                myDateDialog.setToPreviousWeek();
                Integer companyId = Integer.parseInt(company);
                Calendar[] returnval = myDateDialog.getDatesFromDialog(companyId);
                if (returnval[0] == null) {
                    return;
                }
                String[] dates = new String[2];
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
                returnval[1].add(Calendar.DAY_OF_WEEK, -1);
                dates[0] = myFormat.format(returnval[0].getTime());
                dates[1] = myFormat.format(returnval[1].getTime());

                HashMap params = new HashMap();
                params.put("startDate", dates[0]);
                params.put("endDate", dates[1]);

                JasperPrint report = JasperFillManager.fillReport(reportStream, params, myConnect);
                IReportViewer viewer = new IReportViewer(report);
                Main_Window.parentOfApplication.desktop.add(viewer);
                viewer.showForm();
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
    }

    class PrintPersonnelReport {

        public PrintPersonnelReport(HashMap parameters, String company) {
            InputStream reportStream
                    = getClass().getResourceAsStream("/rmischedule/ireports/personnel_change_report.jasper");

            Connection myConn = new Connection();
            myConn.myCompany = company;

            try {
                java.sql.Connection myConnect = RMIScheduleServerImpl.getConnection().generateConnection();

                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, myConnect);
                IReportViewer viewer = new IReportViewer(report);
                Main_Window.parentOfApplication.desktop.add(viewer);
                viewer.showForm();
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
    }

    class PrintBreakdownReport {

        public PrintBreakdownReport(String company) {
            try {
                Integer companyId = Integer.parseInt(company);

                GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(parentOfApplication, true, GetDatesForPrintDialog.DAY, true);
                Calendar startDate = Calendar.getInstance();
                Calendar endDate = Calendar.getInstance();
                startDate.add(Calendar.WEEK_OF_MONTH, -2);
                endDate.add(Calendar.WEEK_OF_MONTH, 2);
                startDate = StaticDateTimeFunctions.getBegOfWeek(startDate, companyId);
                endDate = StaticDateTimeFunctions.getEndOfWeek(endDate, companyId);
                myDateDialog.setDatesManually(startDate, endDate);
                Calendar[] returnval = myDateDialog.getDatesFromDialog(companyId);
                if (returnval[0] == null) {
                    return;
                }
                String[] dates = new String[2];
                SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");

                dates[0] = myFormat.format(returnval[0].getTime());
                dates[1] = myFormat.format(returnval[1].getTime());
                if (dates != null) {
                    Company myCompany = getCompanyById(company);
                    InputStream reportStream
                            = getClass().getResourceAsStream("/rmischeduleserver/ireports/OpenAndOvertimeShiftTotals.jasper");

                    Connection myConn = new Connection();
                    myConn.myCompany = company;
                    Hashtable parameters = new Hashtable();
                    parameters.put("schema", myCompany.getDB());
                    parameters.put("start_week", dates[0]);
                    parameters.put("end_week", dates[1]);
                    java.sql.Connection myConnect = RMIScheduleServerImpl.getConnection().generateConnection();
                    try {
                        PreparedStatement pstmt = myConnect.prepareStatement("SET search_path='" + myCompany.getDB().replaceAll("'", "''") + "'");
                        pstmt.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, myConnect);
                    IReportViewer viewer = new IReportViewer(report);
                    Main_Window.parentOfApplication.desktop.add(viewer);
                    viewer.showForm();
                }
            } catch (Exception exe) {
            }
        }
    }

    class PrintBranchReport {

        public PrintBranchReport(String company) {
            try {
                Integer companyId = Integer.parseInt(company);

                GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(parentOfApplication, true, GetDatesForPrintDialog.DAY, true);
                Calendar startDate = Calendar.getInstance();
                Calendar endDate = Calendar.getInstance();
                startDate.add(Calendar.WEEK_OF_MONTH, -10);
                startDate = StaticDateTimeFunctions.getBegOfWeek(startDate, companyId);
                endDate = StaticDateTimeFunctions.getEndOfWeek(endDate, companyId);
                myDateDialog.setDatesManually(startDate, endDate);
                Calendar[] returnval = myDateDialog.getDatesFromDialog(companyId);
                if (returnval[0] == null) {
                    return;
                }
                String[] dates = new String[2];
                SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");

                dates[0] = myFormat.format(returnval[0].getTime());

                dates[1] = myFormat.format(returnval[1].getTime());
                if (dates != null) {
                    Company myCompany = getCompanyById(company);
                    InputStream reportStream
                            = getClass().getResourceAsStream("/rmischedule/ireports/billing_breakdown_report.jasper");

                    Connection myConn = new Connection();
                    myConn.myCompany = company;
                    Hashtable parameters = new Hashtable();
                    parameters.put("schema", myCompany.getDB());
                    parameters.put("start_date", dates[0]);
                    parameters.put("end_date", dates[1]);
                    java.sql.Connection myConnect = RMIScheduleServerImpl.getConnection().generateConnection();
                    try {
                        PreparedStatement pstmt = myConnect.prepareStatement("SET search_path='" + myCompany.getDB().replaceAll("'", "''") + "'");
                        pstmt.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, myConnect);
                    IReportViewer viewer = new IReportViewer(report);
                    Main_Window.parentOfApplication.desktop.add(viewer);
                    viewer.showForm();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class PrintProfitLossReport {

        public PrintProfitLossReport(String company, String branchId) {
            try {
                GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(parentOfApplication, true, GetDatesForPrintDialog.WEEK);
                Integer companyId = Integer.parseInt(company);
                Calendar[] returnval = myDateDialog.getDatesFromDialog(companyId);
                if (returnval[0] == null) {
                    return;
                }
                String[] dates = new String[2];
                SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
                returnval[1].add(Calendar.DAY_OF_WEEK, -1);
                dates[0] = myFormat.format(returnval[0].getTime());
                dates[1] = myFormat.format(returnval[1].getTime());
                if (dates != null) {
                    Company myCompany = getCompanyById(company);
                    InputStream reportStream
                            = getClass().getResourceAsStream("/rmischedule/ireports/client_payment_ratio.jasper");

                    Connection myConn = new Connection();
                    myConn.myCompany = company;

                    Hashtable parameters = new Hashtable();
                    parameters.put("schema", myCompany.getDB());
                    parameters.put("start_date", dates[0]);
                    parameters.put("end_date", dates[1]);
                    parameters.put("branch_id", Integer.parseInt(branchId));
                    java.sql.Connection myConnect = RMIScheduleServerImpl.getConnection().generateConnection();
                    try {
                        PreparedStatement pstmt = myConnect.prepareStatement("SET search_path='" + myCompany.getDB().replaceAll("'", "''") + "'");
                        pstmt.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, myConnect);
                    IReportViewer viewer = new IReportViewer(report);
                    Main_Window.parentOfApplication.desktop.add(viewer);
                    viewer.showForm();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class PrintHiredEmployeeReport {

        public PrintHiredEmployeeReport(String company) {
            Connection myConnection = new Connection();
            myConnection.setCompany(company);
            GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(parentOfApplication, true, GetDatesForPrintDialog.WEEK, true);
            Integer companyId = Integer.parseInt(company);
            Calendar[] returnval = myDateDialog.getDatesFromDialog(companyId);
            if (returnval[0] == null) {
                return;
            }

            Company myCompany = getCompanyById(company);
            InputStream reportStream
                    = getClass().getResourceAsStream("/rmischedule/ireports/new_employee_report.jasper");

            Connection myConn = new Connection();
            myConn.myCompany = company;

            try {
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

                Hashtable parameters = new Hashtable();
                parameters.put("schema", myCompany.getDB());
                parameters.put("startDate", (returnval[0].getTime()));
                parameters.put("endDate", (returnval[1].getTime()));
                java.sql.Connection myConnect = RMIScheduleServerImpl.getConnection().generateConnection();
                try {
                    PreparedStatement pstmt = myConnect.prepareStatement("SET search_path='" + myCompany.getDB().replaceAll("'", "''") + "'");
                    pstmt.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, myConnect);
                IReportViewer viewer = new IReportViewer(report);
                Main_Window.parentOfApplication.desktop.add(viewer);
                viewer.showForm();
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
    }

    /**
     * Prints an extended Employee Report....
     */
    class PrintExtendeEmployeeReport {

        public PrintExtendeEmployeeReport(String company, String branch) {
            Connection myConnection = new Connection();
            myConnection.setBranch(branch);
            myConnection.setCompany(company);
            GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(parentOfApplication, true, GetDatesForPrintDialog.WEEK, true);
            Integer companyId = Integer.parseInt(company);
            Calendar[] returnval = myDateDialog.getDatesFromDialog(companyId);
            if (returnval[0] == null) {
                return;
            }

            Company myCompany = getCompanyById(company);
            InputStream reportStream
                    = getClass().getResourceAsStream("/rmischedule/ireports/multiple_weeks_employees.jasper");

            Connection myConn = new Connection();
            myConn.myCompany = company;

            try {
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

                Hashtable parameters = new Hashtable();
                parameters.put("schema", myCompany.getDB());
                parameters.put("startDate", myFormat.format(returnval[0].getTime()));
                parameters.put("endDate", myFormat.format(returnval[1].getTime()));
                parameters.put("SUBREPORT_DIR", "rmischedule/ireports/");
                java.sql.Connection myConnect = RMIScheduleServerImpl.getConnection().generateConnection();
                try {
                    PreparedStatement pstmt = myConnect.prepareStatement("SET search_path='" + myCompany.getDB().replaceAll("'", "''") + "'");
                    pstmt.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, myConnect);
                IReportViewer viewer = new IReportViewer(report);
                Main_Window.parentOfApplication.desktop.add(viewer);
                viewer.showForm();
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
    }

    private class myTimeFormatter extends formatterClass {

        public String formatInputString(String input) {
            return (Integer.parseInt(input) / 60) + " hrs";
        }
    }

    private class myTimeFormatter2 extends formatterClass {

        public String formatInputString(String input) {
            return StaticDateTimeFunctions.stringToFormattedTime(input, Main_Window.parentOfApplication.is12HourFormat());
        }
    }

    private class myTimeFormatter3 extends formatterClass {

        public String formatInputString(String input) {
            try {
                long timeMillis = Long.parseLong(input);
                if (timeMillis == 0) {
                    return "----";
                }
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(timeMillis);
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("HH:mm");
                return dateFormat.format(cal.getTime());
            } catch (Exception e) {
                return "";
            }
        }
    }

    private class myDateFormatter extends formatterClass {

        public String formatInputString(String input) {
            return StaticDateTimeFunctions.convertDatabaseDateToReadable(input);
        }
    }

    class GenericPrintReport {

        String myheader;
        String employees;
        String types;
        generic_assemble_schedule_query myQuery;

        public GenericPrintReport(String Header, String emps, String typs, generic_assemble_schedule_query myQ, String company, String branch) {
            super();
            myheader = Header;
            employees = emps;
            types = typs;
            myQuery = myQ;
            String startWeek = "";
            String endWeek = "";
            GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(parentOfApplication, true);
            Integer companyId = Integer.parseInt(company);
            Calendar[] returnval = myDateDialog.getDatesFromDialog(companyId);
            if (returnval[0] == null) {
                return;
            }
            startWeek = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(returnval[0]);
            endWeek = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(returnval[1]);
            client_query myClientQuery = new client_query();
            employee_query myEmployeeQuery = new employee_query();
            Connection myConnection = new Connection();
            myConnection.setBranch(branch);
            myConnection.setCompany(company);
            myClientQuery.update("", "", "");
            myEmployeeQuery.update("", 0, true);
            myQuery.update("", employees, startWeek, endWeek, types, "", true);
            String header = myheader + getCompanyNameById(company) + ", " + getBranchNameById(branch);
            xPrintData tableData = new xPrintData(myQuery, myClientQuery, myEmployeeQuery, startWeek, endWeek, myConnection.getServer(), myConnection.myCompany, myConnection.myBranch);
            tableData.setSortType(xPrintData.SORT_BY_CLIENT);
            //EmployeeReportOptions myOptions = new EmployeeReportOptions(tableData, returnval[0], returnval[1], company, branch);
            //myOptions.setVisible(true);

            try {
                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/ClientSchedule.jasper");

                Hashtable parameters = new Hashtable();
                parameters.put("SUBREPORT_DIR", "rmischedule/ireports/");

                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, tableData);
                IReportViewer viewer = new IReportViewer(report);
                Main_Window.parentOfApplication.desktop.add(viewer);
                viewer.showForm();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showMessage(SocketCommandStructure myCommand) {
        JOptionPane.showMessageDialog(((Component) this), myCommand.message, "Message Receieved!", JOptionPane.OK_OPTION);
    }

    public void printBranchSummaryReport(String company, String branch) {
        try {
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

            GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(Main_Window.parentOfApplication, true, GetDatesForPrintDialog.WEEK, true);
            Calendar[] returnval = myDateDialog.getDatesFromDialog(Integer.parseInt(company));

            InputStream reportStream
                    = getClass().getResourceAsStream("/rmischedule/ireports/branch_billing_summary.jasper");

            InputStream reportStream2
                    = getClass().getResourceAsStream("/rmischedule/ireports/branch_billing_summary_pg2.jasper");

            Calendar startDate = returnval[0];
            Calendar endDate = returnval[1];

            startDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            endDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            endDate.add(Calendar.DAY_OF_MONTH, 7);

            Company companyInfo = Main_Window.parentOfApplication.getCompanyById(company);

            Hashtable parameters = new Hashtable();
            parameters.put("schema", companyInfo.getDB());
            parameters.put("startDate", myFormat.format(startDate.getTime()));
            parameters.put("endDate", myFormat.format(endDate.getTime()));

            Company myCompany = getCompanyById(company);
            java.sql.Connection myConnect = RMIScheduleServerImpl.getConnection().generateConnection();
            try {
                PreparedStatement pstmt = myConnect.prepareStatement("SET search_path='" + myCompany.getDB().replaceAll("'", "''") + "'");
                pstmt.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (company.equals("2")) {
                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, myConnect);
                JasperPrint report2 = JasperFillManager.fillReport(reportStream2, parameters, myConnect);

                java.util.List mypages = report2.getPages();
                for (int p = 0; p < mypages.size(); p++) {
                    report.addPage((JRPrintPage) mypages.get(p));
                }

                IReportViewer viewer = new IReportViewer(report);
                Main_Window.parentOfApplication.desktop.add(viewer);
                viewer.showForm();
            } else {
                //I know how hacky, yes
                InputStream massagereportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/massage_branch_billing_summary.jasper");

                JasperPrint report = JasperFillManager.fillReport(massagereportStream, parameters, myConnect);

                IReportViewer viewer = new IReportViewer(report);
                Main_Window.parentOfApplication.desktop.add(viewer);
                viewer.showForm();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printContractRenewalReport(String company, String branch) {
        try {
            InputStream reportStream
                    = getClass().getResourceAsStream("/rmischedule/ireports/contract_renewal_report.jasper");

            Company companyInfo = Main_Window.parentOfApplication.getCompanyById(company);
            Branch branchInfo = Main_Window.parentOfApplication.getBranchById(company, branch);

            Hashtable parameters = new Hashtable();
            parameters.put("SUBREPORT_DIR", "rmischedule/ireports/");
            parameters.put("schema", companyInfo.getDB());

            JasperPrint report = JasperFillManager.fillReport(reportStream, parameters);
            IReportViewer viewer = new IReportViewer(report);
            Main_Window.parentOfApplication.desktop.add(viewer);
            viewer.showForm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printNoteType(String company, String branch) {
        String startWeek = "";
        String endWeek = "";
        GetDatesWithNoteTypeForPrintDialog myDateDialog = new GetDatesWithNoteTypeForPrintDialog(parentOfApplication, true, "", true, company);
        Integer companyId = Integer.parseInt(company);
        Calendar[] returnval = myDateDialog.getDatesFromDialog(companyId);
        Integer noteTypeId = myDateDialog.getSelectedDMIds().getNoteTypeId();
        if (returnval[0] == null) {
            return;
        }
        startWeek = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(returnval[0]);
        endWeek = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(returnval[1]);

        try {
            InputStream reportStream
                    = getClass().getResourceAsStream("/rmischedule/ireports/note_report.jasper");

            Company companyInfo = Main_Window.parentOfApplication.getCompanyById(company);
            Branch branchInfo = Main_Window.parentOfApplication.getBranchById(company, branch);

            Hashtable parameters = new Hashtable();
            parameters.put("active_db", companyInfo.getDB());
            parameters.put("note_type", noteTypeId);
            parameters.put("start_date", startWeek);
            parameters.put("end_date", endWeek);
            parameters.put("branch_id", Integer.parseInt(branch));

            JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, RMIScheduleServerImpl.getConnection().generateConnection());
            IReportViewer viewer = new IReportViewer(report);
            Main_Window.parentOfApplication.desktop.add(viewer);
            viewer.showForm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printEmployeeSchedule(String company, String branch) {
        String startWeek = "";
        String endWeek = "";
        GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(parentOfApplication, true);
        Integer companyId = Integer.parseInt(company);
        Calendar[] returnval = myDateDialog.getDatesFromDialog(companyId);
        if (returnval[0] == null) {
            return;
        }
        startWeek = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(returnval[0]);
        endWeek = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(returnval[1]);
        assemble_schedule_for_employee_report_query myQuery = new assemble_schedule_for_employee_report_query();
        client_query myClientQuery = new client_query();
        employee_query myEmployeeQuery = new employee_query();
        Connection myConnection = new Connection();
        myConnection.setBranch(branch);
        myConnection.setCompany(company);

        myClientQuery.update("", "", "");
        myEmployeeQuery.update("", 0, true);
        myQuery.update("", "", startWeek, endWeek, "", "", false);
        xPrintData tableData = new xPrintData(myQuery, myClientQuery, myEmployeeQuery, startWeek, endWeek, myConnection.getServer(), myConnection.myCompany, myConnection.myBranch);
        tableData.setSortType(xPrintData.SORT_BY_EMPLOYEE);

        try {
            InputStream reportStream
                    = getClass().getResourceAsStream("/rmischeduleserver/ireports/EmployeeSchedule.jasper");

            Company companyInfo = Main_Window.parentOfApplication.getCompanyById(company);
            Branch branchInfo = Main_Window.parentOfApplication.getBranchById(company, branch);

            Hashtable parameters = new Hashtable();
            parameters.put("SUBREPORT_DIR", "rmischeduleserver/ireports/");
            parameters.put("Company_Name", companyInfo.getName());
            parameters.put("Company_Address", branchInfo.getBranchInfo().getAddress());
            parameters.put("Company_City", branchInfo.getBranchInfo().getCity());
            parameters.put("Company_State", branchInfo.getBranchInfo().getState());
            parameters.put("Company_Zip", branchInfo.getBranchInfo().getZip());
            EmployeeReportOptions myOptions = new EmployeeReportOptions(parameters);
            myOptions.setVisible(true);

            JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, tableData);
            IReportViewer viewer = new IReportViewer(report);
            Main_Window.parentOfApplication.desktop.add(viewer);
            viewer.showForm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class PrintCertReport {

        public PrintCertReport(String coId, String brId) {
            CertReportDialog cd = new CertReportDialog(parentOfApplication, true);
            String[] dates = cd.getDatesFromDialog();

            if (dates != null) {
                get_expiring_certs_query query = new get_expiring_certs_query(brId, dates[0], dates[1]);
                Connection con = new Connection();
                con.setCompany(coId);
                con.setBranch(brId);
                con.prepQuery(query);
                Record_Set rs = con.executeQuery(query);

                String[] columns = {"ename", "certname", "expiration"};
                String[] headers = {"Employee Name", "Certification Name", "Expiration Date"};
                String co = getCompanyNameById(coId);
                String br = getBranchNameById(brId);

                Hashtable<String, formatterClass> formatter = new Hashtable();
                formatter.put("Expiration Date", new myDateFormatter());
                try {
                    InputStream reportStream
                            = getClass().getResourceAsStream("/rmischedule/ireports/GenericReport.jasper");

                    Hashtable parameters = new Hashtable();
                    DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
                    String title = "Certification Report for " + co + ", " + br;
                    parameters.put("DateRange", "(" + StaticDateTimeFunctions.convertDatabaseDateToReadable(dates[0]) + " - " + StaticDateTimeFunctions.convertDatabaseDateToReadable(dates[1]) + ")");
                    xGenericReportData reportData = new xGenericReportData(rs, headers, columns, new String[]{title}, formatter);

                    JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, reportData);
                    IReportViewer viewer = new IReportViewer(report);
                    Main_Window.parentOfApplication.desktop.add(viewer);
                    viewer.showForm();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Returns if there is an employee logged in.
     *
     * @return boolean
     */
    public static boolean isEmployeeLoggedIn() {
        if (Main_Window.compBranding.getLoginType().equals(CompanyBranding.LoginType.EMPLOYEE)) {
            return true;
        }
        return false;
    }

    /**
     * Returns if there is an client logged in.
     *
     * @return boolean
     */
    public static boolean isClientLoggedIn() {
        if (Main_Window.compBranding.getLoginType().equals(CompanyBranding.LoginType.CLIENT)) {
            return true;
        }
        return false;
    }

    private class PrintCheckInReport {

        protected String companyId;

        public PrintCheckInReport(String company) {
            companyId = company;

            GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(parentOfApplication, true, GetDatesForPrintDialog.DAY);
            Integer companyInt = Integer.parseInt(company);
            Calendar[] returnval = myDateDialog.getDatesFromDialog(companyInt);
            if (returnval[0] == null) {
                return;
            }

            check_in_query myQuery = new check_in_query();
            String startDate = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(returnval[0]);

            myQuery.update(startDate, startDate, "");
            Connection myConn = new Connection();
            myConn.setCompany(companyId);
            myConn.prepQuery(myQuery);
            ArrayList rs = new ArrayList(1);
            try {
                rs.add(myConn.executeQuery(myQuery));
            } catch (Exception ex) {
            }

            String[] columns = {"employee_name", "time_stamp", "username_checked_in", "time_stamp_out", "username_checked_out"};
            String[] headers = {"Employee Name", "In", "Checked In By", "Out", "Checked Out By"};
            String co = getCompanyNameById(company);

            Hashtable<String, formatterClass> formatter = new Hashtable();
            formatter.put("In", new myTimeFormatter3());
            formatter.put("Out", new myTimeFormatter3());
            try {
                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/GenericReport.jasper");

                Hashtable parameters = new Hashtable();
                String title = "Check-In/Check-Out on " + startDate + " for " + co;
                xGenericReportData reportData = new xGenericReportData(rs, headers, columns, new String[]{title}, formatter);

                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, reportData);
                IReportViewer viewer = new IReportViewer(report);
                Main_Window.parentOfApplication.desktop.add(viewer);
                viewer.showForm();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This is inheireted from our implementation of CompanyMenuInterface.
     *
     * @param action
     * @param company
     */
    public void clickedMenu(String action, Company company) {
        Integer companyId = 0;
        try {
            companyId = Integer.parseInt(company.getId());
        } catch (Exception e) {
        }
        if (action.equals(Main_Window.PRINT_CHECKIN_REPORT)) {
            new PrintCheckInReport(company.getId());
        } else if (action.equals(Main_Window.ACTIVE_EMP_COUNT_REPORT)) {
            GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(Main_Window.parentOfApplication, true, GetDatesForPrintDialog.WEEK, true);
            Calendar[] returnval = myDateDialog.getDatesFromDialog(companyId);
            HashMap hashMap = new HashMap();
            hashMap.put("start_week", returnval[0]);
            hashMap.put("end_week", returnval[1]);

            PrintEmpCountReport empCount = new PrintEmpCountReport();
            empCount.runReport(companyId + "", hashMap);
            JasperPrint print = empCount.getJasperPrint();
            IReportViewer viewer = new IReportViewer(print);
            Main_Window.parentOfApplication.desktop.add(viewer);
            viewer.showForm();
        } else if (action.equals(Main_Window.NEW_EMPLOYEE_LOW_ALERT)) {
            new PrintNewEmployeeLowAlert(company.getId());
        } else if (action.equals(Main_Window.RETURN_EMP_EQUIPMENT)) {
            getEquipmentReturnWindow().setInformation(company.getId());
            getEquipmentReturnWindow().setVisible(true);
        } else if (action.equals(Main_Window.PRINT_RATING_REPORT)) {
            new PrintRatingReport(company.getId());
        } else if (action.equals(Main_Window.PRINT_FAILED_COMMUNICATION)) {
            new PrintFailedCommunication(company.getId());
        } else if (action.equals(Main_Window.PRINT_CENSUS_REPORT)) {
            PrintCensusReport censusReport = new PrintCensusReport(company.getId());
        } else if (action.equals(Main_Window.PRINT_HIRED_EMP)) {
            new PrintHiredEmployeeReport(company.getId());
        } else if (action.equals(Main_Window.ADD_EVENT)) {
            EventLoggerInternalFrame iFrame = new EventLoggerInternalFrame(company.getId());
            Main_Window.parentOfApplication.desktop.add(iFrame);
            java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            if (screenSize.width > 950 && screenSize.height > 509) {
                iFrame.setBounds((screenSize.width - 950) / 2, (screenSize.height - 509) / 2, 950, 509);
            } else {
                iFrame.setBounds(screenSize.width / 2, (screenSize.height) / 2, screenSize.width, screenSize.height);
            }
            iFrame.setVisible(true);
        } else if (action.equals(Main_Window.PRINT_EMPLOYEE_HIRED_REPORT)) {
            new PrintEmployeesHired(company.getId());
        } else if (action.equals(Main_Window.PRINT_PERSONNEL_REPORT)) {
            GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(parentOfApplication, true, GetDatesForPrintDialog.WEEK, true);
            Integer companyIdInt = Integer.parseInt(company.getId());
            Calendar[] returnval = myDateDialog.getDatesFromDialog(companyIdInt);

            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

            HashMap hashMap = new HashMap();
            hashMap.put("startDate", myFormat.format(returnval[0].getTime()));
            hashMap.put("endDate", myFormat.format(returnval[1].getTime()));
            hashMap.put("schema", company.getCompDB());
            PrintPersonnelReport myReport = new PrintPersonnelReport(hashMap, company.getId());

        } else if (action.equals(Main_Window.CORPORATE_COMM_USAGE_REPORT)) {
            GetDatesWithUserSelectionForPrintDialog myDateDialog = new GetDatesWithUserSelectionForPrintDialog(parentOfApplication, true,
                    GetDatesForPrintDialog.WEEK, true, company.getId(), true, "District Manager", "Sales Manager");
            Integer companyIdInt = Integer.parseInt(company.getId());
            Calendar[] returnval = myDateDialog.getDatesFromDialog(companyIdInt);
            ArrayList<Integer> selectedDMs = myDateDialog.getSelectedUserIds();

            HashMap hashMap = new HashMap();
            hashMap.put("start_week", returnval[0]);
            hashMap.put("end_week", returnval[1]);
            hashMap.put("selectedDM", selectedDMs);
            PrintCoporateCommUsageReport myReport = new PrintCoporateCommUsageReport();
            myReport.runReport(company.getId(), hashMap);
            JasperPrint report = myReport.getJasperPrint();
            IReportViewer viewer = new IReportViewer(report);
            Main_Window.parentOfApplication.desktop.add(viewer);
            viewer.showForm();
        } else if (action.equals(Main_Window.PRINT_COMMISSION_REPORT)) {
            PrintCommission myTime = new PrintCommission();
            GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(Main_Window.parentOfApplication, true, GetDatesForPrintDialog.WEEK, true);
            Calendar[] returnval = myDateDialog.getDatesFromDialog(Integer.parseInt(company.getId()));
            if (returnval[0] == null) {
                return;
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("startDate", StaticDateTimeFunctions.convertCalendarToDatabaseFormat(returnval[0]));
            params.put("endDate", StaticDateTimeFunctions.convertCalendarToDatabaseFormat(returnval[1]));

            myTime.runReport(company.getId() + "", params);
            JasperPrint report = myTime.getJasperPrint();
            IReportViewer viewer = new IReportViewer(report);
            Main_Window.parentOfApplication.desktop.add(viewer);
            viewer.showForm();
        } else if (action.equals(Main_Window.PRINT_CORP_REPORT)) {
            new PrintCoporateCommReport(company.getId());
        } else if (action.equals(Main_Window.PRINT_LOG_REPORT)) {
            new PrintCallLogReport(company.getId());
        } else if (action.equals(Main_Window.PRINT_CROSS_EMPS)) {
            new PrintCrossBranchEmployees(company.getId());
        } else if (action.equals(Main_Window.PRINT_BRANCH_PAYROLL_REPORT)) {
            new PrintBranchReport(company.getId());
        } else if (action.equals(Main_Window.HOUR_BREAKDOWN_REPORT)) {
            new PrintBreakdownReport(company.getId());
        } else if (action.equals(Main_Window.PRINT_OVERTIME_SUMMARY_REPORT)) {
            this.printBranchSummaryReport(company.getId(), "");
        } else if (action.equals(Main_Window.COMPANY_EDIT)) {
            xCompanyEdit companyEdit = new xCompanyEdit();
            Main_Window.parentOfApplication.desktop.add(companyEdit);
            companyEdit.setVisible(true);
        } else if (action.equals(Main_Window.CLIENT_CALL_QUEUE)) {
            ConstantContact constantContact = new ConstantContact();
            constantContact.setInformation(company.getId(), "0");
            this.desktop.add(constantContact);
            constantContact.setVisible(true);
        } else if (action.equals(Main_Window.PROBLEM_SEARCH)) {
            SearchProblemSolverDialog problemDialog = new SearchProblemSolverDialog(this, true, company.getId());
            problemDialog.setVisible(true);
        } else if (action.equals(Main_Window.CLIENT_SEARCH)) {
            AllClientSearch clientSearch = new AllClientSearch(this, false, company.getId());
            //this.desktop.add(clientSearch);
            clientSearch.setVisible(true);
        } else if (action.equals(this.PRINT_CONTRACT_RENEWAL_REPORT)) {
            printContractRenewalReport(company.getId(), "");
        } else if (action.equals(Main_Window.PRINT_CALLING_QUEUE)) {
            new PrintCallingQueue(company.getId());
        } else if (action.equals(Main_Window.PRINT_SALES_REPORT)) {
            new PrintSalesReport(company.getId());
        } else if (action.equals(Main_Window.PRINT_CORP_CALLING_QUEUE)) {
            new PrintCorporateCallingQueue(company.getId());
        } else if (action.equals(Main_Window.PRINT_CC_TABLE_REPORT)) {
            new PrintCCTableReport(company.getId());
        } else if (action.equals(Main_Window.PRINT_CORP_SUMMARY_REPORT)) {
            GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(Main_Window.parentOfApplication, true, GetDatesForPrintDialog.WEEK, true);
            Calendar[] returnval = myDateDialog.getDatesFromDialog(Integer.parseInt(company.getId()));
            if (returnval[0] == null) {
                return;
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("startDate", StaticDateTimeFunctions.convertCalendarToDatabaseFormat(returnval[0]));
            params.put("endDate", StaticDateTimeFunctions.convertCalendarToDatabaseFormat(returnval[1]));

            new PrintCorpSummary(company.getId(), params);
        } else if (action.equals(Main_Window.EXPORT_GOVT_DATA)) {
            ExportController export = new ExportController(company.getId());
            ArrayList<Integer> myIds = new ArrayList<Integer>();
            myIds.add(6408);

            try {
                GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(Main_Window.parentOfApplication, true, GetDatesForPrintDialog.WEEK, false);
                Calendar[] returnval = myDateDialog.getDatesFromDialog(Integer.parseInt(company.getId()));
                if (returnval[0] == null) {
                    return;
                }

                returnval[0].set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                returnval[1].set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                
                FileDialog myFileDialog = new FileDialog(this, "Save To?", FileDialog.SAVE);
                myFileDialog.setVisible(true);
                myFileDialog.setFilenameFilter(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        if (name.endsWith(".xls")) {
                            return true;
                        }
                        return false;
                    }
                });
                File[] myFileLocation = myFileDialog.getFiles();
                
                export.generateXLSFile(myIds, myFileLocation[0].getAbsolutePath(), returnval[0].getTime(), returnval[1].getTime());
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        } else if (action.equals(Main_Window.ANALYTICS_HISTORICAL_HOURLY)) {
            HistoricalHoursFrame historicalFrame = new HistoricalHoursFrame(company.getId());
            Main_Window.parentOfApplication.desktop.add(historicalFrame);
            historicalFrame.setVisible(true);
        } else if (action.equals(Main_Window.ANALYTICS_PROFIT_ANALYSIS)) {
            ProfitAnalysisFrame profitFrame = new ProfitAnalysisFrame(company.getId());
            Main_Window.parentOfApplication.desktop.add(profitFrame);
            profitFrame.setVisible(true);
        } else if (action.equals(Main_Window.CLIENT_RATE_INCREASE)) {
            RateIncreaseFrame rateFrame = new RateIncreaseFrame(company.getId() + "");
            Main_Window.parentOfApplication.desktop.add(rateFrame);
            rateFrame.setVisible(true);
        } else if (action.equals(Main_Window.PRINT_EMP_TERMINATION_REPORT)) {
            new PrintEmployeeTerminationReport(company.getId());
        } else if (action.equals(Main_Window.HEALTHCARE_OPTIONS)) {
            HealthCareDialog healthDialog = new HealthCareDialog(Main_Window.parentOfApplication, true, company.getId());
            healthDialog.setVisible(true);
        } else if (action.equals(Main_Window.ALL_EMPLOYEES_MESSAGING_ACTION)) {
            getMessagingEditWindow().setInformation(company.getId(), "-1", getUser(), false);
            getMessagingEditWindow().setVisible(true);
        } else if (action.equals(Main_Window.PRINT_DEDUCTION_REPORT)) {
            DeductionReportFrame deductionFrame = new DeductionReportFrame(company.getId());
            Main_Window.parentOfApplication.desktop.add(deductionFrame);
            deductionFrame.setVisible(true);
        }
    }

    /**
     * This is inheireted from our implementation of CompanyBranchMenuInterface.
     */
    public void clickedMenu(String action, String companyName, String branchName, final String companyId, final String branchId) {
        if (action.equals(this.CLIENT_EDIT_ACTION)) {
            getClientEditWindow().setInformation(companyId, branchId);
            getClientEditWindow().setVisible(true);
            try {
                getClientEditWindow().setIcon(false);
            } catch (Exception ex) {
            }
            try {
                getClientEditWindow().setSelected(true);
            } catch (Exception ex) {
            }
        } else if (action.equals(this.EMPLOYEE_EDIT_ACTION)) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    try {
                        Main_Window.setWaitCursor(true);
                        getEmployeeEditWindow(loginType).setInformation(companyId, branchId, null);
                        getEmployeeEditWindow().setVisible(true);
                        getEmployeeEditWindow().setIcon(false);
                        getEmployeeEditWindow().setSelected(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        Main_Window.setWaitCursor(false);
                    }
                }
            });

        } else if (action.equals(this.EMPLOYEE_APPROVAL_ACTION)) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    try {
                        Main_Window.setWaitCursor(true);
                        getEmployeeEditWindow("empApproval").setInformation(companyId, branchId, null);
                        getEmployeeEditWindow().setVisible(true);
                        getEmployeeEditWindow().setIcon(false);
                        getEmployeeEditWindow().setSelected(true);
                    } catch (Exception ex) {
                    } finally {
                        Main_Window.setWaitCursor(false);
                    }
                }
            });

        } else if (action.equals(Main_Window.SCHEDULE_EDIT_ACTION)) {
            myScheduleForm.checkForAndLoadSchedule(branchId, companyId);
            try {
                myScheduleForm.setIcon(false);
            } catch (Exception ex) {
            }

            //    added by Jeffrey Davis on 05/21/2010 to handle messaging
        } else if (action.equals(Main_Window.MESSAGING_ACTION)) {
            getMessagingEditWindow().setInformation(companyId, branchId, getUser());
            getMessagingEditWindow().setVisible(true);
        } else if (action.equals(Main_Window.PRINT_CLIENT_PAYROLL)) {
            new PrintProfitLossReport(companyId, branchId);
        } else if (action.equals(Main_Window.PRINT_OVER_UNDER_REPORT)) {
            HashMap map = getStartAndEndTimes(Integer.parseInt(companyId));
            PrintOverUnder printOverUnder = new PrintOverUnder();
            printOverUnder.runReport(companyId, branchId, map);
            JasperPrint print = printOverUnder.getJasperPrint();
            IReportViewer viewer = new IReportViewer(print);
            Main_Window.parentOfApplication.desktop.add(viewer);
            viewer.showForm();
        } else if (action.equals(Main_Window.PRINT_TIME_OFF_REPORT)) {
            new PrintTimeOffReport(companyId, branchId);
        } else if (action.equals(Main_Window.CLIENT_BREAKDOWN_REPORT)) {

            PrintClientBreakdown myReport = new PrintClientBreakdown();
            myReport.runReport(companyId, branchId, new HashMap<String, Integer>());
            JasperPrint report = myReport.getJasperPrint();
            IReportViewer viewer = new IReportViewer(report);
            Main_Window.parentOfApplication.desktop.add(viewer);
            viewer.showForm();
        } else if (action.equals(Main_Window.PRINT_OVERTIME_REPORT)) {
            PrintOverTime myTime = new PrintOverTime();
            GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(Main_Window.parentOfApplication, true);
            Calendar[] returnval = myDateDialog.getDatesFromDialog(Integer.parseInt(companyId));
            if (returnval[0] == null) {
                return;
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("start_week", StaticDateTimeFunctions.convertCalendarToDatabaseFormat(returnval[0]));
            params.put("end_week", StaticDateTimeFunctions.convertCalendarToDatabaseFormat(returnval[1]));

            myTime.runReport(companyId, branchId, params);
            JasperPrint report = myTime.getJasperPrint();
            IReportViewer viewer = new IReportViewer(report);
            Main_Window.parentOfApplication.desktop.add(viewer);
            viewer.showForm();
        } else if (action.equals(Main_Window.PRINT_CERTIFICAION_EXP_REPORT)) {
            new PrintCertificationExp(companyId, branchId);
        } else if (action.equals(Main_Window.PRINT_EMPLOYEE_TYPE_REPORT)) {
            new PrintEmployeeType(companyId, branchId);
        } else if (action.equals(Main_Window.PRINT_CERTIFICAION_MISSING_REPORT)) {
            new PrintCertificationMissing(companyId, branchId);
        } else if (action.equals(Main_Window.PRINT_TERMINATED_EMPS)) {
            new PrintTerminatedEmployees(companyId, branchId);
        } else if (action.equals(this.EMAIL_SCHEDULE)) {
            emailScheduleReports report = new emailScheduleReports(branchId, companyId, parentOfApplication);
        } else if (action.equals(this.PRINT_PHONE_REPORT)) {
            phonelistreport myReport = new phonelistreport(branchId, companyId, parentOfApplication);
        } else if (action.equals(this.PRINT_EMP_REPORT)) {
            printEmployeeSchedule(companyId, branchId);
        } else if (action.equals(this.NOTE_TYPE_REPORT)) {
            printNoteType(companyId, branchId);
        } else if (action.equals(this.PRINT_TRAIN_REPORT)) {
            new GenericPrintReport("Training Report For ", "", "", new assemble_schedule_for_training_query(), companyId, branchId);
        } else if (action.equals(this.PRINT_LOCATION_REPORT)) {
            new GenericPrintReport("Location Report For ", "", "", new Generic_Report_Schedule_Query(), companyId, branchId);
        } else if (action.equals(this.PRINT_UNCON_REPORT)) {
            new GenericPrintReport("Unconfirmed Report For ", "", "", new assemble_schedule_for_unconfirmed_report(), companyId, branchId);
        } else if (action.equals(this.PRINT_OPEN_REPORT)) {
            new GenericPrintReport("Open Shift Report For ", "0", "", new Generic_Report_Schedule_Query(), companyId, branchId);
        } else if (action.equals(this.PRINT_EMP_ROLLCALL_REPORT)) {
            PrintEmployeeReport myTime = new PrintEmployeeReport();
            GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(parentOfApplication, true, GetDatesForPrintDialog.DAY);
            Calendar[] returnval = myDateDialog.getDatesFromDialog(Integer.parseInt(companyId));
            if (returnval[0] == null) {
                return;
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("start_week", returnval[0]);
            params.put("end_week", returnval[1]);

            myTime.runReport(companyId, branchId, params);
            JasperPrint report = myTime.getJasperPrint();
            IReportViewer viewer = new IReportViewer(report);
            Main_Window.parentOfApplication.desktop.add(viewer);
            viewer.showForm();
        } else if (action.equals(this.PRINT_TOTALS_BY_LOCATION_REPORT)) {
            //new PrintTotalsForLocation(companyId, branchId);
        } else if (action.equals(this.PRINT_EXTENDED_EMP_ROLLCALL_REPORT)) {
            new PrintExtendeEmployeeReport(companyId, branchId);
        } else if (action.equals(Main_Window.PRINT_CERT_REPORT)) {
            new PrintCertReport(companyId, branchId);
        } else if (action.equals(Main_Window.EXPORT_EMPLOYEE_SCHEDULE)) {
            new rmischedule.data_export.employee.EmployeeTimeExport(companyId, branchId);
        } else if (action.equals(Main_Window.PERSONNEL_FORM)) {
            new PersonnelForm(companyId, branchId);
        } else if (action.equals(Main_Window.IMPORT_EMPLOYEE_DATA)) {
            rmischedule.importexportData.ImportExportEmployeeFrame jf = new rmischedule.importexportData.ImportExportEmployeeFrame(this.getCompanyById(companyId), branchId);
            desktop.add(jf);
            jf.setVisible(true);
        } else if (action.equals(Main_Window.IMPORT_CLIENT_DATA)) {
            rmischedule.importexportData.ImportExportClientFrame jf = new rmischedule.importexportData.ImportExportClientFrame(this.getCompanyById(companyId), branchId);
            desktop.add(jf);
            jf.setVisible(true);
        } else if (action.equals(Main_Window.EXPORT_EMPLOYEE_DATA)) {
            Connection tempConnection = new Connection();
            tempConnection.myCompany = companyId;
            ExportDisplayOption myExportDisplay = new ExportDisplayOption(companyId, branchId, new EmployeeDBMapper(tempConnection.getServer(), companyId, branchId));
            desktop.add(myExportDisplay);
            myExportDisplay.setVisible(true);
        } else if (action.equals(Main_Window.EXPORT_CLIENT_DATA)) {
            Connection tempConnection = new Connection();
            tempConnection.myCompany = companyId;
            ExportDisplayOption myExportDisplay = new ExportDisplayOption(companyId, branchId, new ClientDBMapper(tempConnection.getServer(), companyId, branchId));
            desktop.add(myExportDisplay);
            myExportDisplay.setVisible(true);
        } else if (action.equals(Main_Window.EXPORT_CLIENT_RATECODES_DATA)) {
            Connection tempConnection = new Connection();
            tempConnection.myCompany = companyId;
            ExportDisplayOption myExportDisplay = new ExportDisplayOption(companyId, branchId, new ClientRateCodesDBMapper(tempConnection.getServer(), companyId, branchId));
            desktop.add(myExportDisplay);
            myExportDisplay.setVisible(true);
        } else if (action.equals(Main_Window.PRINT_ACTIVE_EMPS_REPORT)) {
            HashMap hashMap = getStartAndEndTimes(Integer.parseInt(companyId));
            PrintActiveEmps printEmps = new PrintActiveEmps();
            printEmps.runReport(companyId, branchId, hashMap);
            IReportViewer viewer = new IReportViewer(printEmps.getJasperPrint());
            Main_Window.parentOfApplication.desktop.add(viewer);
            viewer.showForm();
        } else if (action.equals(Main_Window.PRINT_DEMOGRAPHICS)) {
            PrintDemographics printDemo = new PrintDemographics();
            printDemo.runReport(companyId, branchId, new HashMap());
            JasperPrint report = printDemo.getJasperPrint();
            IReportViewer viewer = new IReportViewer(report);
            Main_Window.parentOfApplication.desktop.add(viewer);
            viewer.showForm();
        }
    }

    private HashMap getStartAndEndTimes(Integer companyId) {
        GetDatesForPrintDialog myDateDialog
                = new GetDatesForPrintDialog(Main_Window.parentOfApplication, true);
        Calendar[] returnval = myDateDialog.getDatesFromDialog(companyId);
        if (returnval[0] == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("start_week", returnval[0]);
        hashMap.put("end_week", returnval[1]);
        return hashMap;
    }

    public class myDesktopPane extends JDesktopPane {

        Main_Window parent;

        public myDesktopPane(Main_Window p) {
            parent = p;
        }

        public ImageIcon getMyBackground() {
            return myBackground;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (myBackground != null) {
                myBackground.paintIcon(parent, g, (getWidth() - myBackground.getIconWidth()) / 2,
                        (getHeight() - myBackground.getIconHeight() + 68) / 2);
            }
        }
    }
}
