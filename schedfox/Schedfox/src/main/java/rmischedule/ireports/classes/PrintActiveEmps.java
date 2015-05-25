/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.ireports.classes;

import java.io.InputStream;
import rmischedule.data_connection.Connection;
import rmischedule.xprint.data.xGenericReportData;
import rmischeduleserver.util.StaticDateTimeFunctions;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import rmischedule.components.graphicalcomponents.ChooseFieldsForGenericReportFactory;
import rmischedule.ireports.RunReportInterface;
import rmischedule.main.Main_Window;
import rmischedule.xprint.templates.genericreportcomponents.formatterClass;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
import rmischeduleserver.mysqlconnectivity.queries.reports.active_employees_query;

/**
 *
 * @author user
 */
public class PrintActiveEmps extends RunReportInterface {

    private JasperPrint report;

    public void runReport(String company, String branch, Map params) { 
        Calendar startCalendar = getCalendarFromParams(params, "start_week");
        Calendar endCalendar = getCalendarFromParams(params, "end_week");
        String endWeek = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(startCalendar);

        active_employees_query myQuery = new active_employees_query();
        myQuery.setPreparedStatement(new Object[]{endWeek, Integer.parseInt(branch), Integer.parseInt(branch), endWeek, endWeek});

        ArrayList rs = new ArrayList();
        Connection myConnection = new Connection();
        myConnection.setBranch(branch);
        
        myConnection.setCompany(company);
        formatterClass[] myFormatter = {null, null, new mySocialFormatter(), new myEndDateFormatter(), new myAgeFormatter(), new myEndDateFormatter(), new myEndDateFormatter(), null, null, null, null, null, null, null, null, null, null, new GenderFormatter()};
        String[] columns = {"lname", "fname", "ssn", "hire", "dob", "dob", "term", "address1", "address2", "city", "state", "zip", "phone1", "phone2", "cell", "pager", "email", "gender"};
        String[] headers = {"Last Name", "First Name", "Social Sec", "Hire Date", "Age", "DOB", "Term Date", "Address", "Address2", "City", "State", "Zip", "Phone", "Phone 2", "Cell", "Pager", "EMail", "Gender"};
        
        if (Main_Window.parentOfApplication.isUserAMemberOfGroups(myConnection, "ADMIN", "Payroll", "Corporate User")) {
            columns = new String[]{"lname", "fname", "ssn", "hire", "dob", "dob", "term", "address1", "address2", "city", "state", "zip", "phone1", "phone2", "cell", "pager", "email", "gender", "week_gross", "year_gross"};
            headers = new String[]{"Last Name", "First Name", "Social Sec", "Hire Date", "Age", "DOB", "Term Date", "Address", "Address2", "City", "State", "Zip", "Phone", "Phone 2", "Cell", "Pager", "EMail", "Gender", "Week Gross Pay", "Year Gross Pay"};
            myFormatter = new formatterClass[]{null, null, new mySocialFormatter(), new myEndDateFormatter(), new myAgeFormatter(), new myEndDateFormatter(), new myEndDateFormatter(), null, null, null, null, null, null, null, null, null, null, new GenderFormatter(), null, null};
        }
        int[] sizes = new int[columns.length];

        //TODO: REMOVE THIS!
        ChooseFieldsForGenericReportFactory changeFields = new ChooseFieldsForGenericReportFactory(null, true, headers, columns, sizes, myFormatter);
        ArrayList dataToReturn = changeFields.displayFormAndReturnData();

        String co = Main_Window.parentOfApplication.getCompanyNameById(company);
        String br = Main_Window.parentOfApplication.getBranchNameById(branch);


        Hashtable<String, formatterClass> formatter = new Hashtable();
        headers = (String[]) dataToReturn.get(0);
        columns = (String[]) dataToReturn.get(1);
        formatterClass[] tempFormatters = (formatterClass[]) dataToReturn.get(3);

        RunQueriesEx myQueryEx = new RunQueriesEx();
        boolean containsWeeklyGross = false;
        boolean containsYearlyGross = false;
        for (int c = 0; c < columns.length; c++) {
            if (columns[c].equals("week_gross")) {
                containsWeeklyGross = true;
            }
            if (columns[c].equals("year_gross")) {
                containsYearlyGross = true;
            }
        }
        myQuery.update(containsWeeklyGross, containsYearlyGross);
        myConnection.prepQuery(myQuery);
        myQueryEx.add(myQuery);
        
        for (int i = 0; i < headers.length; i++) {
            if (tempFormatters[i] != null) {
                formatter.put(headers[i], tempFormatters[i]);
            }
        }

        try {
            rs = myConnection.executeQueryEx(myQueryEx);
        } catch (Exception e) {
        }
        
        try {
            InputStream reportStream =
                    getClass().getResourceAsStream("/rmischedule/ireports/GenericReport.jasper");

            Hashtable parameters = new Hashtable();
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
            String title = "Active Employees for " + co + ", " + br;
            parameters.put("DateRange", "(" + df.format(startCalendar.getTime()) + " - " + df.format(endCalendar.getTime()) + ")");
            xGenericReportData reportData = new xGenericReportData((Record_Set) rs.get(0), headers, columns, new String[]{title}, formatter);

            report = JasperFillManager.fillReport(reportStream, parameters, reportData);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runReport(String company, Map params) {
    }

    public JasperPrint getJasperPrint() {
        return report;
    }

    @Override
    public String getName() {
        return "active_rep";
    }

    @Override
    public String getDisplayName() {
        return "Active Employee Report";
    }

    @Override
    public String getDescription() {
        return "This report provides basic information about currently active employees.";
    }

    @Override
    public Map<String, Class> getRequiredParameters() {
        HashMap<String, Class> parameters = new HashMap<String, Class>();
        parameters.put("start_week", Date.class);
        parameters.put("end_week", Date.class);
        return parameters;
    }
}
