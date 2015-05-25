/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.control;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.client.client_query;
import rmischeduleserver.mysqlconnectivity.queries.employee.employee_query;
import rmischeduleserver.mysqlconnectivity.queries.reports.assemble_schedule_for_employee_report_query;
import rmischeduleserver.util.xprint.xPrintData;
import schedfoxlib.model.Branch;
import schedfoxlib.model.Company;

/**
 *
 * @author ira
 */
public class ReportingController {
    
    private Company comp;
    private HashMap<String, Branch> branchCache = new HashMap<String, Branch>();
    
    public byte[] printEmployeeScheduleToFile(String empId, String startWeek, String endWeek, String branchId, RMIScheduleServerImpl myConn, String companyId) throws JRException {
        JasperPrint print = null;
        client_query myClientQuery = new client_query();
        employee_query myEmployeeQuery = new employee_query();
        assemble_schedule_for_employee_report_query myQuery = new assemble_schedule_for_employee_report_query();

        myClientQuery.setCompany(companyId);
        myEmployeeQuery.setCompany(companyId);
        myQuery.setCompany(companyId);

        if (!branchId.equals("-1")) {
            myClientQuery.setBranch(branchId);
            myEmployeeQuery.setBranch(branchId);
            myQuery.setBranch(branchId);
        }

        myClientQuery.update("", "", "");
        myEmployeeQuery.update("", Integer.parseInt(empId), true);

        try {
            myQuery.update("", empId + "", startWeek, endWeek, "", "", false);
            xPrintData tableData = new xPrintData(myQuery, myClientQuery, myEmployeeQuery, startWeek, endWeek, myConn, companyId, branchId);
            tableData.setSortType(tableData.SORT_BY_EMPLOYEE);

            try {
                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischeduleserver/ireports/EmployeeSchedule.jasper");

                Branch branchInfo = null;
                CompanyController compController = CompanyController.getInstance();
                if (comp == null) {
                    comp = compController.getCompanyById(Integer.parseInt(companyId));
                }
                if (branchCache.get(branchId) == null) {
                    branchCache.put(branchId, compController.getBranchById(Integer.parseInt(branchId)));
                }
                branchInfo = branchCache.get(branchId);

                Hashtable parameters = new Hashtable();
                parameters.put("SUBREPORT_DIR", "rmischeduleserver/ireports/");

                try {
                    parameters.put("Company_Name", comp.getName());
                    parameters.put("Company_Address", branchInfo.getBranchInfo().getAddress());
                    parameters.put("Company_City", branchInfo.getBranchInfo().getCity());
                    parameters.put("Company_State", branchInfo.getBranchInfo().getState());
                    parameters.put("Company_Zip", branchInfo.getBranchInfo().getZip());
                    parameters.put("showEnvelope", new Boolean(false));
                } catch (Exception e) {
                    System.out.println("Filled in company informatin since could not load!");
                    parameters.put("Company_Name", "");
                    parameters.put("Company_Address", "");
                    parameters.put("Company_City", "");
                    parameters.put("Company_State", "");
                    parameters.put("Company_Zip", "");
                    parameters.put("showEnvelope", new Boolean(false));
                }
                JasperPrint retVal = JasperFillManager.fillReport(reportStream, parameters, tableData);
                if (tableData.hasData()) {
                    print = retVal;
                } else {
                    print = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                print = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            print = null;
        }
        if (print == null) {
            return null;
        } else {
            return JasperExportManager.exportReportToPdf(print);
        }
    }
}
