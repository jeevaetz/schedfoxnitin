/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import schedfoxlib.model.Client;
import schedfoxlib.model.Employee;
import schedfoxlib.model.EmployeePaymentBreakdown;
import schedfoxlib.model.EmployeePaymentBreakdownWeek;
import schedfoxlib.model.EmployeePaymentDeduction;
import schedfoxlib.model.EmployeePaymentTaxes;
import schedfoxlib.model.EmployeePayments;

/**
 *
 * @author ira
 */
public class ExportController {

    private String companyId;

    public ExportController(String companyId) {
        this.companyId = companyId;
    }

    public static ExportController getInstance(String companyId) {
        return new ExportController(companyId);
    }

    public void generateXLSFile(ArrayList<Integer> clientIds, String fileName, Date startDate, Date endDate) throws IOException {
        File outputFile = new File(fileName);
        FileOutputStream fileOut = new FileOutputStream(outputFile);

        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        
        EmployeeController empController = EmployeeController.getInstance(companyId);

        ClientController cliController = ClientController.getInstance(companyId);

        ScheduleController schedController = new ScheduleController(companyId);

        HSSFWorkbook book = new HSSFWorkbook();
        HSSFSheet sheet = book.createSheet("Sheet1");

        HSSFRow headerRow = sheet.createRow(0);

        HSSFCell payrollNumHeader = headerRow.createCell(0);
        payrollNumHeader.setCellValue("payroll_number");

        HSSFCell projectCodeHeader = headerRow.createCell(1);
        projectCodeHeader.setCellValue("project_code");

        HSSFCell contractIdHeader = headerRow.createCell(2);
        contractIdHeader.setCellValue("contract_id");

        HSSFCell workOrderHeader = headerRow.createCell(3);
        workOrderHeader.setCellValue("work_order");

        HSSFCell weekEndHeader = headerRow.createCell(4);
        weekEndHeader.setCellValue("week_end_date");

        HSSFCell checkNumberHeader = headerRow.createCell(5);
        checkNumberHeader.setCellValue("check_num");

        HSSFCell ssnHeader = headerRow.createCell(6);
        ssnHeader.setCellValue("ssn");

        HSSFCell employeeIdHeader = headerRow.createCell(7);
        employeeIdHeader.setCellValue("employee_ID");

        HSSFCell classCodeHeader = headerRow.createCell(8);
        classCodeHeader.setCellValue("class_code");

        HSSFCell grossEmpHeader = headerRow.createCell(9);
        grossEmpHeader.setCellValue("gross_employee_pay");

        HSSFCell allProjectsHeader = headerRow.createCell(10);
        allProjectsHeader.setCellValue("all_projects");

        HSSFCell wagesPaidInLieuHeader = headerRow.createCell(11);
        wagesPaidInLieuHeader.setCellValue("wages_paid_in_lieu_of_fringes");

        HSSFCell totalPaidHeader = headerRow.createCell(12);
        totalPaidHeader.setCellValue("total_paid");

        HSSFCell stHrsDateHeader = headerRow.createCell(13);
        stHrsDateHeader.setCellValue("st_hrs_date1");

        HSSFCell stHrsDate2Header = headerRow.createCell(14);
        stHrsDate2Header.setCellValue("st_hrs_date2");

        HSSFCell stHrsDate3Header = headerRow.createCell(15);
        stHrsDate3Header.setCellValue("st_hrs_date3");

        HSSFCell stHrsDate4Header = headerRow.createCell(16);
        stHrsDate4Header.setCellValue("st_hrs_date4");

        HSSFCell stHrsDate5Header = headerRow.createCell(17);
        stHrsDate5Header.setCellValue("st_hrs_date5");

        HSSFCell stHrsDate6Header = headerRow.createCell(18);
        stHrsDate6Header.setCellValue("st_hrs_date6");

        HSSFCell stHrsDate7Header = headerRow.createCell(19);
        stHrsDate7Header.setCellValue("st_hrs_date7");

        HSSFCell ovtHrsDateHeader = headerRow.createCell(20);
        ovtHrsDateHeader.setCellValue("ov_hrs_date1");

        HSSFCell ovtHrsDate2Header = headerRow.createCell(21);
        ovtHrsDate2Header.setCellValue("ov_hrs_date2");

        HSSFCell ovtHrsDate3Header = headerRow.createCell(22);
        ovtHrsDate3Header.setCellValue("ov_hrs_date3");

        HSSFCell ovtHrsDate4Header = headerRow.createCell(23);
        ovtHrsDate4Header.setCellValue("ov_hrs_date4");

        HSSFCell ovtHrsDate5Header = headerRow.createCell(24);
        ovtHrsDate5Header.setCellValue("ov_hrs_date5");

        HSSFCell ovtHrsDate6Header = headerRow.createCell(25);
        ovtHrsDate6Header.setCellValue("ov_hrs_date6");

        HSSFCell ovtHrsDate7Header = headerRow.createCell(26);
        ovtHrsDate7Header.setCellValue("ov_hrs_date7");

        HSSFCell ovtHrsx2DateHeader = headerRow.createCell(27);
        ovtHrsx2DateHeader.setCellValue("ov_hrsx2_date1");

        HSSFCell ovtHrsx2Date2Header = headerRow.createCell(28);
        ovtHrsx2Date2Header.setCellValue("ov_hrsx2_date2");

        HSSFCell ovtHrsx2Date3Header = headerRow.createCell(29);
        ovtHrsx2Date3Header.setCellValue("ov_hrsx2_date3");

        HSSFCell ovtHrsx2Date4Header = headerRow.createCell(30);
        ovtHrsx2Date4Header.setCellValue("ov_hrsx2_date4");

        HSSFCell ovtHrsx2Date5Header = headerRow.createCell(31);
        ovtHrsx2Date5Header.setCellValue("ov_hrsx2_date5");

        HSSFCell ovtHrsx2Date6Header = headerRow.createCell(32);
        ovtHrsx2Date6Header.setCellValue("ov_hrsx2_date6");

        HSSFCell ovtHrsx2Date7Header = headerRow.createCell(33);
        ovtHrsx2Date7Header.setCellValue("ov_hrsx2_date7");

        HSSFCell epHawHeader = headerRow.createCell(34);
        epHawHeader.setCellValue("ep_haw");

        HSSFCell epPensionHeader = headerRow.createCell(35);
        epPensionHeader.setCellValue("ep_pension");

        HSSFCell epVacHeader = headerRow.createCell(36);
        epVacHeader.setCellValue("ep_vac_hol");

        HSSFCell epTrainHeader = headerRow.createCell(37);
        epTrainHeader.setCellValue("ep_train");

        HSSFCell epAllOtherHeader = headerRow.createCell(38);
        epAllOtherHeader.setCellValue("ep_all_other");

        HSSFCell volContPensionHeader = headerRow.createCell(39);
        volContPensionHeader.setCellValue("vol_cont_pension");

        HSSFCell volEmpPayHeader = headerRow.createCell(40);
        volEmpPayHeader.setCellValue("vol_emp_pay_med");

        HSSFCell dtsFedTaxHeader = headerRow.createCell(41);
        dtsFedTaxHeader.setCellValue("dts_fed_tax");

        HSSFCell dtsFicaHeader = headerRow.createCell(42);
        dtsFicaHeader.setCellValue("dts_fica");

        HSSFCell dtsMedicareHeader = headerRow.createCell(43);
        dtsMedicareHeader.setCellValue("dts_medicare");

        HSSFCell dtsStateTaxHeader = headerRow.createCell(44);
        dtsStateTaxHeader.setCellValue("dts_state_tax");

        HSSFCell dtsSDIHeader = headerRow.createCell(45);
        dtsSDIHeader.setCellValue("dts_sdi");

        HSSFCell dtsDuesHeader = headerRow.createCell(46);
        dtsDuesHeader.setCellValue("dts_dues");

        HSSFCell dtsSavingsHeader = headerRow.createCell(47);
        dtsSavingsHeader.setCellValue("dts_savings");

        HSSFCell dtsOtherHeader = headerRow.createCell(48);
        dtsOtherHeader.setCellValue("dts_other");

        HSSFCell dtsTotalHeader = headerRow.createCell(49);
        dtsTotalHeader.setCellValue("dts_total");

        HSSFCell travSubsHeader = headerRow.createCell(50);
        travSubsHeader.setCellValue("trav_subs");

        HSSFCell payRateHeader = headerRow.createCell(51);
        payRateHeader.setCellValue("pay_rate");

        HSSFCell otRateHeader = headerRow.createCell(52);
        otRateHeader.setCellValue("OT_rate");

        HSSFCell ot2RateHeader = headerRow.createCell(53);
        ot2RateHeader.setCellValue("2OT_rate");

        HSSFCell prNotes = headerRow.createCell(54);
        prNotes.setCellValue("prnotes");

        HSSFCell firstName = headerRow.createCell(55);
        firstName.setCellValue("first_name");

        HSSFCell lastName = headerRow.createCell(56);
        lastName.setCellValue("last_name");

        HSSFCell address = headerRow.createCell(57);
        address.setCellValue("address1");

        HSSFCell address2 = headerRow.createCell(58);
        address2.setCellValue("address2");

        HSSFCell city = headerRow.createCell(59);
        city.setCellValue("city");

        HSSFCell state = headerRow.createCell(60);
        state.setCellValue("state");

        HSSFCell empty = headerRow.createCell(61);
        empty.setCellValue("");

        HSSFCell zip = headerRow.createCell(62);
        zip.setCellValue("ZIP");

        HSSFCell phone = headerRow.createCell(63);
        phone.setCellValue("phone");

        HSSFCell gender = headerRow.createCell(64);
        gender.setCellValue("gender");

        HSSFCell ethnicity = headerRow.createCell(65);
        ethnicity.setCellValue("ethnicity");

        HSSFCell apprenticeId = headerRow.createCell(66);
        apprenticeId.setCellValue("apprentice_id");

        HSSFCell craftId = headerRow.createCell(67);
        craftId.setCellValue("craft_id");

        HSSFCell vacHolDuesRate = headerRow.createCell(68);
        vacHolDuesRate.setCellValue("vac_hol_dues_rate");

        HSSFCell empEpHaw = headerRow.createCell(69);
        empEpHaw.setCellValue("emp_ep_haw");

        HSSFCell empEpPension = headerRow.createCell(70);
        empEpPension.setCellValue("emp_ep_pension");

        HSSFCell trainingRate = headerRow.createCell(71);
        trainingRate.setCellValue("training_rate");

        HSSFCell volContPensionRate = headerRow.createCell(72);
        volContPensionRate.setCellValue("vol_cont_pension_rate");

        HSSFCell volContMedicalRate = headerRow.createCell(73);
        volContMedicalRate.setCellValue("vol_cont_medical_rate");

        HSSFCell inLieuRate = headerRow.createCell(74);
        inLieuRate.setCellValue("in_lieu_payment_rate");

        HSSFCell vacChkBox = headerRow.createCell(75);
        vacChkBox.setCellValue("vac_chk_box");

        HSSFCell fringePaidChk = headerRow.createCell(76);
        fringePaidChk.setCellValue("fringe_paid_chk_box");

        HSSFCell dateHired = headerRow.createCell(77);
        dateHired.setCellValue("date_hired");

        HSSFCell empStatus = headerRow.createCell(78);
        empStatus.setCellValue("emp_status");

        HSSFCell workCountry = headerRow.createCell(79);
        workCountry.setCellValue("work_county");

        HSSFCell isForeman = headerRow.createCell(80);
        isForeman.setCellValue("IsForeman");

        HSSFCell isDisadvanted = headerRow.createCell(81);
        isDisadvanted.setCellValue("IsDisadvantaged");

        HSSFCell VeteranStatus = headerRow.createCell(82);
        VeteranStatus.setCellValue("VeteranStatus");

        HSSFCell OtherDeductionNotes = headerRow.createCell(83);
        OtherDeductionNotes.setCellValue("OtherDeductionNotes");

        HSSFCell num_exempt = headerRow.createCell(84);
        num_exempt.setCellValue("num_exempt");

        HSSFCell DriversLicense = headerRow.createCell(85);
        DriversLicense.setCellValue("DriversLicense");

        HSSFCell DriversLicenseState = headerRow.createCell(86);
        DriversLicenseState.setCellValue("DriversLicenseState");

        for (int c = 0; c < clientIds.size(); c++) {
            try {
                Client client = cliController.getClientById(clientIds.get(c));
                ArrayList<EmployeePaymentBreakdown> breakdowns = empController.getBreakdownsForClientAndDate(client.getClientId(), endDate);
                
                
                HashMap<Integer, EmployeePaymentBreakdownWeek> consolidatedBreakdowns = new HashMap<Integer, EmployeePaymentBreakdownWeek>();
                for (int s = 0; s < breakdowns.size(); s++) {
                    EmployeePaymentBreakdown data = breakdowns.get(s);
                    ArrayList<EmployeePaymentDeduction> deductions = empController.getDeductions(data.getEmployee_payment_id());
                    ArrayList<EmployeePaymentTaxes> taxes = empController.getTaxes(data.getEmployee_payment_id());
                    EmployeePayments payment = empController.loadEmployeePayment(data.getEmployee_payment_id());
                    
                    if (consolidatedBreakdowns.get(payment.getEmployeeId()) == null) {
                        consolidatedBreakdowns.put(payment.getEmployeeId(), new EmployeePaymentBreakdownWeek(startDate));
                    }
                    consolidatedBreakdowns.get(payment.getEmployeeId()).getBreakdowns().add(data);
                    consolidatedBreakdowns.get(payment.getEmployeeId()).setPayment(payment);
                    consolidatedBreakdowns.get(payment.getEmployeeId()).setDeductions(deductions);
                    consolidatedBreakdowns.get(payment.getEmployeeId()).setTaxes(taxes);
                    
                }
                Iterator<EmployeePaymentBreakdownWeek> it = consolidatedBreakdowns.values().iterator();
                int s = 0;
                while (it.hasNext()) {
                    try {
                        HSSFRow newRow = sheet.createRow(s + 1);
                        
                        EmployeePaymentBreakdownWeek week = it.next();
                        
                        BigDecimal fedTax = new BigDecimal(0);
                        BigDecimal socialSecurity = new BigDecimal(0);
                        BigDecimal medicare = new BigDecimal(0);
                        BigDecimal stateTax = new BigDecimal(0);
                        BigDecimal stateUnemployment = new BigDecimal(0);
                        BigDecimal totalTax = new BigDecimal(0);
                        BigDecimal totalProject = new BigDecimal(0);
                        
                        BigDecimal totalDeductions = new BigDecimal(0);
                        
                        ArrayList<EmployeePaymentTaxes> taxes = week.getTaxes();
                        ArrayList<EmployeePaymentDeduction> deductions = week.getDeductions();
                        
                        BigDecimal allGross = week.getPayment().getGrossPay();
                        allGross = allGross.setScale(2, BigDecimal.ROUND_HALF_UP);
                        
                        
                        for (int d = 0; d < week.getBreakdowns().size(); d++) {
                            EmployeePaymentBreakdown myBreak = week.getBreakdowns().get(d);
                            if (clientIds.contains(myBreak.getClient_id())) {
                                totalProject = new BigDecimal(totalProject.doubleValue() + myBreak.getDbl_pay_amount() + myBreak.getOver_pay_amount() + myBreak.getReg_pay_amount());
                            }
                        }
                        totalProject = totalProject.setScale(2, BigDecimal.ROUND_HALF_UP);
                        
                        for (int d = 0; d < deductions.size(); d++) {
                            totalDeductions = new BigDecimal(totalDeductions.doubleValue() + deductions.get(d).getAmount().doubleValue());
                        }
                        totalDeductions = totalDeductions.setScale(2, BigDecimal.ROUND_HALF_UP);
                        
                        for (int t = 0; t < taxes.size(); t++) {
                            if (taxes.get(t).getTaxType().equals("SS")) {
                                socialSecurity = taxes.get(t).getDeAmount();
                                socialSecurity = socialSecurity.setScale(2, BigDecimal.ROUND_HALF_UP);
                            } else if (taxes.get(t).getTaxType().equals("MC")) {
                                medicare = taxes.get(t).getDeAmount();
                                medicare = medicare.setScale(2, BigDecimal.ROUND_HALF_UP);
                            } else if (taxes.get(t).getTaxType().equals("FE")) {
                                fedTax = taxes.get(t).getDeAmount();
                                fedTax = fedTax.setScale(2, BigDecimal.ROUND_HALF_UP);
                            } else if (taxes.get(t).getTaxType().equals("ST")) {
                                stateTax = taxes.get(t).getDeAmount();
                                stateTax = stateTax.setScale(2, BigDecimal.ROUND_HALF_UP);
                            } else if (taxes.get(t).getTaxType().equals("SU")) {
                                stateUnemployment = taxes.get(t).getDeAmount();
                                stateUnemployment = stateUnemployment.setScale(2, BigDecimal.ROUND_HALF_UP);
                            }
                            totalTax = new BigDecimal(totalTax.doubleValue() + taxes.get(t).getDeAmount().doubleValue());
                        }
                        totalTax = totalTax.setScale(2, BigDecimal.ROUND_HALF_UP);
                        
                        s++;
                        
                        Employee emp = empController.getEmployeeById(week.getPayment().getEmployeeId());

                        HSSFCell payrollNumRow = newRow.createCell(0);
                        payrollNumRow.setCellValue("");

                        //project_code
                        HSSFCell projectCodeRow = newRow.createCell(1);
                        projectCodeRow.setCellValue("");

                        HSSFCell contractIdRow = newRow.createCell(2);
                        contractIdRow.setCellValue("");

                        HSSFCell workOrderRow = newRow.createCell(3);
                        workOrderRow.setCellValue("");

                        HSSFCell weekEndRow = newRow.createCell(4);
                        weekEndRow.setCellValue(myFormat.format(week.getPayment().getDateOfTrans()));

                        HSSFCell checkNumberRow = newRow.createCell(5);
                        checkNumberRow.setCellValue(week.getPayment().getCheckNum());

                        HSSFCell ssnRow = newRow.createCell(6);
                        ssnRow.setCellValue(emp.getEmployeeSsn());

                        HSSFCell employeeIdRow = newRow.createCell(7);
                        employeeIdRow.setCellValue(emp.getEmployeeId());

                        HSSFCell classCodeRow = newRow.createCell(8);
                        classCodeRow.setCellValue("");

                        HSSFCell grossEmpRow = newRow.createCell(9);
                        //Needs to be changed to gross amount for Client
                        grossEmpRow.setCellValue(totalProject.toString());

                        HSSFCell allProjectsRow = newRow.createCell(10);
                        allProjectsRow.setCellValue(allGross.toString());

                        HSSFCell wagesPaidInLieuRow = newRow.createCell(11);
                        wagesPaidInLieuRow.setCellValue("");

                        HSSFCell totalPaidRow = newRow.createCell(12);
                        BigDecimal netPay = week.getPayment().getNetPay();
                        netPay = netPay.setScale(2, BigDecimal.ROUND_HALF_UP);
                        totalPaidRow.setCellValue(netPay.toString());

                        //st_hrs_date1
                        HSSFCell stHrsDateRow = newRow.createCell(13);
                        stHrsDateRow.setCellValue(week.getRegularHoursOnDay(Calendar.MONDAY));

                        //st_hrs_date2
                        HSSFCell stHrsDate2Row = newRow.createCell(14);
                        stHrsDate2Row.setCellValue(week.getRegularHoursOnDay(Calendar.TUESDAY));

                        //st_hrs_date3
                        HSSFCell stHrsDate3Row = newRow.createCell(15);
                        stHrsDate3Row.setCellValue(week.getRegularHoursOnDay(Calendar.WEDNESDAY));

                        //st_hrs_date4
                        HSSFCell stHrsDate4Row = newRow.createCell(16);
                        stHrsDate4Row.setCellValue(week.getRegularHoursOnDay(Calendar.THURSDAY));

                        //st_hrs_date5
                        HSSFCell stHrsDate5Row = newRow.createCell(17);
                        stHrsDate5Row.setCellValue(week.getRegularHoursOnDay(Calendar.FRIDAY));

                        //st_hrs_date6
                        HSSFCell stHrsDate6Row = newRow.createCell(18);
                        stHrsDate6Row.setCellValue(week.getRegularHoursOnDay(Calendar.SATURDAY));

                        //st_hrs_date7
                        HSSFCell stHrsDate7Row = newRow.createCell(19);
                        stHrsDate7Row.setCellValue(week.getRegularHoursOnDay(Calendar.SUNDAY));

                        //ov_hrs_date1
                        HSSFCell ovtHrsDateRow = newRow.createCell(20);
                        ovtHrsDateRow.setCellValue(week.getOvertimeHoursOnDay(1));

                        //ov_hrs_date2
                        HSSFCell ovtHrsDate2Row = newRow.createCell(21);
                        ovtHrsDate2Row.setCellValue(week.getOvertimeHoursOnDay(2));

                        //ov_hrs_date3
                        HSSFCell ovtHrsDate3Row = newRow.createCell(22);
                        ovtHrsDate3Row.setCellValue(week.getOvertimeHoursOnDay(3));

                        //ov_hrs_date4
                        HSSFCell ovtHrsDate4Row = newRow.createCell(23);
                        ovtHrsDate4Row.setCellValue(week.getOvertimeHoursOnDay(4));

                        //ov_hrs_date5
                        HSSFCell ovtHrsDate5Row = newRow.createCell(24);
                        ovtHrsDate5Row.setCellValue(week.getOvertimeHoursOnDay(5));

                        //ov_hrs_date6
                        HSSFCell ovtHrsDate6Row = newRow.createCell(25);
                        ovtHrsDate6Row.setCellValue(week.getOvertimeHoursOnDay(6));

                        //ov_hrs_date7
                        HSSFCell ovtHrsDate7Row = newRow.createCell(26);
                        ovtHrsDate7Row.setCellValue(week.getOvertimeHoursOnDay(7));

                        //ov_hrsx2_date1
                        HSSFCell ovtHrsx2DateRow = newRow.createCell(27);
                        ovtHrsx2DateRow.setCellValue(week.getDoubleHoursOnDay(1));

                        //ov_hrsx2_date2
                        HSSFCell ovtHrsx2Date2Row = newRow.createCell(28);
                        ovtHrsx2Date2Row.setCellValue(week.getDoubleHoursOnDay(2));

                        //ov_hrsx2_date3
                        HSSFCell ovtHrsx2Date3Row = newRow.createCell(29);
                        ovtHrsx2Date3Row.setCellValue(week.getDoubleHoursOnDay(3));

                        //ov_hrsx2_date4
                        HSSFCell ovtHrsx2Date4Row = newRow.createCell(30);
                        ovtHrsx2Date4Row.setCellValue(week.getDoubleHoursOnDay(4));

                        //ov_hrsx2_date5
                        HSSFCell ovtHrsx2Date5Row = newRow.createCell(31);
                        ovtHrsx2Date5Row.setCellValue(week.getDoubleHoursOnDay(5));

                        //ov_hrsx2_date6
                        HSSFCell ovtHrsx2Date6Row = newRow.createCell(32);
                        ovtHrsx2Date6Row.setCellValue(week.getDoubleHoursOnDay(6));

                        //ov_hrsx2_date7
                        HSSFCell ovtHrsx2Date7Row = newRow.createCell(33);
                        ovtHrsx2Date7Row.setCellValue(week.getDoubleHoursOnDay(7));

                        //ep_haw
                        HSSFCell epHawRow = newRow.createCell(34);
                        epHawRow.setCellValue("");

                        //ep_pension
                        HSSFCell epPensionRow = newRow.createCell(35);
                        epPensionRow.setCellValue(0);

                        //ep_vac_hol
                        HSSFCell epVacRow = newRow.createCell(36);
                        epVacRow.setCellValue("");

                        //ep_train
                        HSSFCell epTrainRow = newRow.createCell(37);
                        epTrainRow.setCellValue("");

                        //ep_all_other
                        HSSFCell epAllOtherRow = newRow.createCell(38);
                        epAllOtherRow.setCellValue("");

                        //vol_cont_pension
                        HSSFCell volContPensionRow = newRow.createCell(39);
                        volContPensionRow.setCellValue("");

                        //vol_emp_pay_med
                        HSSFCell volEmpPayRow = newRow.createCell(40);
                        volEmpPayRow.setCellValue("");

                        //dts_fed_tax
                        HSSFCell dtsFedTaxRow = newRow.createCell(41);
                        dtsFedTaxRow.setCellValue(fedTax.toString());

                        
                        //dts_fica
                        HSSFCell dtsFicaRow = newRow.createCell(42);
                        dtsFicaRow.setCellValue(socialSecurity.toString());

                        //dts_medicare
                        HSSFCell dtsMedicareRow = newRow.createCell(43);
                        dtsMedicareRow.setCellValue(medicare.toString());

                        //dts_state_tax
                        HSSFCell dtsStateTaxRow = newRow.createCell(44);
                        dtsStateTaxRow.setCellValue(stateTax.toString());

                        //dts_sdi
                        HSSFCell dtsSDIRow = newRow.createCell(45);
                        dtsSDIRow.setCellValue(stateUnemployment.toString());

                        //dts_dues
                        HSSFCell dtsDuesRow = newRow.createCell(46);
                        dtsDuesRow.setCellValue(0);

                        //dts_savings
                        HSSFCell dtsSavingsRow = newRow.createCell(47);
                        dtsSavingsRow.setCellValue(0);

                        //dts_other
                        HSSFCell dtsOtherRow = newRow.createCell(48);
                        dtsOtherRow.setCellValue(totalDeductions.toString());

                        //dts_total
                        HSSFCell dtsTotalRow = newRow.createCell(49);
                        dtsTotalRow.setCellValue(totalDeductions.doubleValue() + totalTax.doubleValue());

                        //trav_subs
                        HSSFCell travSubsRow = newRow.createCell(50);
                        travSubsRow.setCellValue(0);

                        //pay_rate
                        HSSFCell payRateRow = newRow.createCell(51);
                        try {
                            payRateRow.setCellValue(week.getBreakdowns().get(0).getReg_pay_rate());
                        } catch (Exception exe) {}

                        //OT_rate
                        HSSFCell otRateRow = newRow.createCell(52);
                        try {
                            otRateRow.setCellValue(week.getBreakdowns().get(0).getOver_pay_rate());
                        } catch (Exception exe) {}

                        //2OT_rate
                        HSSFCell ot2RateRow = newRow.createCell(53);
                        try {
                            ot2RateRow.setCellValue(week.getBreakdowns().get(0).getDbl_pay_rate());
                        } catch (Exception exe) {}

                        //prnotes
                        HSSFCell prNotesRow = newRow.createCell(54);
                        prNotesRow.setCellValue("");

                        HSSFCell firstNameRow = newRow.createCell(55);
                        firstNameRow.setCellValue(emp.getEmployeeFirstName());

                        HSSFCell lastNameRow = newRow.createCell(56);
                        lastNameRow.setCellValue(emp.getEmployeeLastName());

                        HSSFCell addressRow = newRow.createCell(57);
                        addressRow.setCellValue(emp.getEmployeeAddress());

                        HSSFCell address2Row = newRow.createCell(58);
                        address2Row.setCellValue(emp.getEmployeeAddress2());

                        HSSFCell cityRow = newRow.createCell(59);
                        cityRow.setCellValue(emp.getEmployeeCity());

                        HSSFCell stateRow = newRow.createCell(60);
                        stateRow.setCellValue(emp.getEmployeeState());

                        HSSFCell emptyRow = newRow.createCell(61);
                        emptyRow.setCellValue("");

                        HSSFCell zipRow = newRow.createCell(62);
                        zipRow.setCellValue(emp.getEmployeeZip());

                        HSSFCell phoneRow = newRow.createCell(63);
                        phoneRow.setCellValue(emp.getEmployeePhone());

                        //gender
                        HSSFCell genderRow = newRow.createCell(64);
                        genderRow.setCellValue(emp.getGender() == 1 ? "M" : "F");

                        //ethnicity
                        HSSFCell ethnicityRow = newRow.createCell(65);
                        ethnicityRow.setCellValue("");

                        //apprentice_id
                        HSSFCell apprenticeIdRow = newRow.createCell(66);
                        apprenticeIdRow.setCellValue("");

                        //craft_id
                        HSSFCell craftIdRow = newRow.createCell(67);
                        craftIdRow.setCellValue("");

                        //vac_hol_dues_rate
                        HSSFCell vacHolDuesRateRow = newRow.createCell(68);
                        vacHolDuesRateRow.setCellValue("");

                        //emp_ep_haw
                        HSSFCell empEpHawRow = newRow.createCell(69);
                        empEpHawRow.setCellValue("");

                        //emp_ep_pension
                        HSSFCell empEpPensionRow = newRow.createCell(70);
                        empEpPensionRow.setCellValue("");

                        //training_rate
                        HSSFCell trainingRateRow = newRow.createCell(71);
                        trainingRateRow.setCellValue("");

                        //vol_cont_pension_rate
                        HSSFCell volContPensionRateRow = newRow.createCell(72);
                        volContPensionRateRow.setCellValue("");

                        //vol_cont_medical_rate
                        HSSFCell volContMedicalRateRow = newRow.createCell(73);
                        volContMedicalRateRow.setCellValue("");

                        //in_lieu_payment_rate
                        HSSFCell inLieuRateRow = newRow.createCell(74);
                        inLieuRateRow.setCellValue("");

                        //vac_chk_box
                        HSSFCell vacChkBoxRow = newRow.createCell(75);
                        vacChkBoxRow.setCellValue("");

                        HSSFCell fringePaidChkRow = newRow.createCell(76);
                        fringePaidChkRow.setCellValue("");

                        HSSFCell dateHiredRow = newRow.createCell(77);
                        dateHiredRow.setCellValue(myFormat.format(emp.getEmployeeHireDate()));

                        HSSFCell empStatusRow = newRow.createCell(78);
                        empStatusRow.setCellValue(emp.isDeleted() ? "Inactive" : "Active");

                        HSSFCell workCountryRow = newRow.createCell(79);
                        workCountryRow.setCellValue("");

                        HSSFCell isForemanRow = newRow.createCell(80);
                        isForemanRow.setCellValue("");

                        HSSFCell isDisadvantedRow = newRow.createCell(81);
                        isDisadvantedRow.setCellValue("");

                        HSSFCell VeteranStatusRow = newRow.createCell(82);
                        VeteranStatusRow.setCellValue("");

                        HSSFCell OtherDeductionNotesRow = newRow.createCell(83);
                        OtherDeductionNotesRow.setCellValue("");

                        HSSFCell numExemptRow = newRow.createCell(84);
                        numExemptRow.setCellValue("");

                        HSSFCell DriversLicenseRow = newRow.createCell(85);
                        DriversLicenseRow.setCellValue("");

                        HSSFCell DriversLicenseStateRow = newRow.createCell(86);
                        DriversLicenseStateRow.setCellValue("");
                    } catch (Exception exe) {
                    }
                }
            } catch (Exception exe) {
            }
        }

        book.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }
}
