/*
 * ExportData.java
 *
 * Created on January 14, 2005, 2:55 PM
 */

package rmischedule.data_export.ultra32;

import rmischeduleserver.util.StaticDateTimeFunctions;
import schedfoxlib.model.util.Record_Set;
import java.io.*;
import java.util.*;

import rmischedule.components.*;
import schedfoxlib.model.ShiftOptionsClass;
import rmischeduleserver.data_connection_types.*;
import schedfoxlib.model.ShiftTypeClass;
import javax.swing.JOptionPane;
import rmischedule.main.*;

import org.apache.poi.hssf.usermodel.*;
/**
 *
 * @author ira
 */
public class ExportData {
    
    private HSSFWorkbook wb;
    private HSSFSheet s;
    private HSSFRow r;
    private HSSFCell c;
    private Hashtable<String, ClientInfo> clientHash;
    private Hashtable employeeHash;
    private Hashtable extEmployeeHash;
    
    private Vector clientInfo;
    private Vector employeeInfo;
    private Vector extEmployeeInfo;
    
    FileOutputStream out;
    
    
    /** Creates a new instance of ExportData */
    public ExportData(String fileName) {
        openFile(fileName);
    }
    
    private void openFile(String fileName) {
        try { 
            out = new FileOutputStream(fileName);
            wb  = new HSSFWorkbook();
            s   = wb.createSheet();
            r   = null;
            c   = null;
        } catch (Exception e) {}
    }
    
    public void exportData(Record_Set dataToExport, Record_Set client, Record_Set employee, Record_Set employeeInfors) {
        clientInfo      = new Vector(client.length());
        employeeInfo    = new Vector(employee.length());
        clientHash      = new Hashtable(client.length());
        employeeHash    = new Hashtable(employee.length());
        extEmployeeInfo = new Vector(employeeInfors.length());
        extEmployeeHash = new Hashtable(employeeInfors.length());
        
        for (int i = 0; i < client.length(); i++) {
            clientHash.put(client.getString("client_id"), new ClientInfo(client.getString("cid"), client.getString("client_id"), client.getString("worksite")));
            client.moveNext();
        }
        
        for (int i = 0; i < employee.length(); i++) {
            EmployeeInfo myEmp = new EmployeeInfo(employee.getString("eid"), employee.getString("ueid"));
            employeeInfo.add(i, myEmp);
            try {
                employeeHash.put(employee.getString("eid"), myEmp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            employee.moveNext();
        }
        
        for (int i = 0; i < employeeInfors.length(); i++) {
            extEmployeeInfo.add(i, new ExtEmployeeInfo(employeeInfors.getString("id"), employeeInfors.getString("lastname")));
            extEmployeeHash.put(employeeInfors.getString("id"), extEmployeeInfo.get(i));
            employeeInfors.moveNext();
        }
        
        int i = 0;
        
        dataToExport.moveToFront();
        
        while (!dataToExport.getEOF()) {
            //probably need sanity checking here for dates
            r = s.createRow(i);
            i++;
            processOneScheduleRecord(dataToExport, i);
            dataToExport.moveNext();
        }
        
        try {
            wb.write(out);
            out.close();
        } catch (Exception e) {}
    }
    
    private void processOneScheduleRecord(Record_Set rs, int recNum) {
        for (short x = 0; x < 38; x++) {
            c = r.createCell(x);
            ShiftTypeClass stc = new ShiftTypeClass(rs.getString("type"));
            
            ShiftOptionsClass spo = new ShiftOptionsClass();
            ShiftOptionsClass sbo = new ShiftOptionsClass();
            
            spo.parse(rs.getString("pay_opt"));
            sbo.parse(rs.getString("bill_opt"));

            switch (x) {
                case 1:
                    /*
                     * This is set static but MUST be updated to use the
                     * Options.
                     */
                    if(stc.isShiftType(stc.SHIFT_TRAINING_SHIFT)){
                        c.setCellValue("9998");
                    } else if (stc.isShiftType(stc.SHIFT_TRAINER_SHIFT)) {
                        c.setCellValue("9998");
                    }else{
                        c.setCellValue(clientHash.get(rs.getString("cid")).UskedId);
                    }
                    break;
                case 2:
                    //Client Name no longer needed...
                    break;
                case 3:
                    if(stc.isShiftType(stc.SHIFT_TRAINING_SHIFT)){
                        
                    } else if(stc.isShiftType(stc.SHIFT_TRAINING_SHIFT)){

                    } else if(stc.isShiftType(stc.SHIFT_NON_BILLABLE)) {

                    } else {
                        c.setCellValue(clientHash.get(rs.getString("cid")).worksite);
                    }
                    break;
                case 4:
                    if (employeeHash.get(rs.getString("eid")) == null) {
                        if (rs.getString("eid").equals("0") || rs.getString("eid").trim().length() == 0) {
                            if (!rs.getString("ename").equals("0")) {
                                JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Missing information for " + rs.getString("ename"));
                            }
                            c.setCellValue("");
                        } else {
                            c.setCellValue("");
                        }
                    } else {
                        c.setCellValue(((EmployeeInfo)employeeHash.get(rs.getString("eid"))).ueid);
                    }
                    break;
                case 5:
                    try {
                        c.setCellValue(((ExtEmployeeInfo)extEmployeeHash.get(rs.getString("eid"))).ename);
                    } catch (Exception e) {}
                    break;
                case 6:
                    //Rate Code Yay
                    try {
                        if (rs.getString("urc").equals("0")) {
                            c.setCellValue("U");
                        } else if(rs.getString("urc").trim().length() > 0){
                            c.setCellValue(rs.getString("urc"));
                        }else{
                            c.setCellValue("U");
                        }
                    } catch (Exception e) {
                        c.setCellValue("U");
                    }
                    break;
                case 7:
                    try {
                        String date = StaticDateTimeFunctions.convertDatabaseDateToReadable(rs.getString("date"));
                        c.setCellValue(date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    try {
                        c.setCellType(c.CELL_TYPE_STRING);
                        String time = StaticDateTimeFunctions.stringToMilitaryTime(rs.getString("start_time"));
                        if (time.equals("2400")) {
                            time = "2359";
                        }
                        
                        c.setCellValue(time + "");
                    } catch (Exception e) {}
                    break;
                case 9:
                    try {
                        c.setCellType(c.CELL_TYPE_STRING);
                        String time = StaticDateTimeFunctions.stringToMilitaryTime(rs.getString("end_time"));
                        if (time.equals("2400")) {
                            time = "0000";
                        }

                        c.setCellValue(time);
                    } catch (Exception e) {}
                    break;
                //case 10:
                //    //c.setCellValue("Position Code");
                //    break;
                case 11:
                    break;
                case 12:
                    break;
                case 13: //Should pay employee, set to NOH for not
                    if(stc.isShiftType(stc.SHIFT_NON_PAYABLE)){
                        c.setCellValue("NOH");                       
                    } else if (stc.isShiftType(stc.SHIFT_PAYABLE_OT)) {
                        c.setCellValue("OT");
                    }
                    break;
                case 16: //Set Pay Break Minutes to zero to not allow ccs to validate non info
                    c.setCellType(c.CELL_TYPE_NUMERIC);
                    c.setCellValue(Double.parseDouble(spo.getBreakLengthString()));
                    c.setCellType(c.CELL_TYPE_NUMERIC);
                    break;
                case 17:
                    c.setCellType(c.CELL_TYPE_STRING);
                    String time = StaticDateTimeFunctions.stringToMilitaryTime(rs.getString("start_time"));
                    if (time.equals("2400")) {
                        time = "0000";
                    }
                    c.setCellValue(time);
                    break;
                case 18: //Should bill client, set to NOH for not
                    if(stc.isShiftType(stc.SHIFT_NON_BILLABLE)){
                        c.setCellValue("NOH");                       
                    } else if (stc.isShiftType(stc.SHIFT_BILLABLE_OT)) {
                        c.setCellValue("OT");
                    }
                    //
                    break;
                case 21: //Set Bill Break Minutes to zero to not allow ccs to validate non info
                    c.setCellType(c.CELL_TYPE_NUMERIC);
                    c.setCellValue(Double.parseDouble(sbo.getBreakLengthString()));
                    break;
                case 22:
                    c.setCellType(c.CELL_TYPE_STRING);
                    String atime = StaticDateTimeFunctions.stringToMilitaryTime(rs.getString("start_time"));
                    if (atime.equals("2400")) {
                        atime = "0000";
                    }
                    c.setCellValue(atime);
                    break;
            }
        }
    }
    
    public void exportEmployeeInformation(Record_Set rs) {
        openFile("z:/empinfo.xls");
        rs.moveToFront();
        int i = 0;
        while (!rs.getEOF()) {
            r = s.createRow(i);
            i++;
            for (short x = 0; x < 38; x++) {
                c = r.createCell(x);
                switch (x) {
                    case 0:
                        c.setCellValue(rs.getString("usid"));
                        break;
                    case 1:
                        if (rs.getString("isdel").equals("1")) {
                            c.setCellValue("I");
                        } else {
                            c.setCellValue("A");
                        }
                        break;
                    case 2:
                        c.setCellValue(rs.getString("lname"));
                        break;
                    case 3:
                        c.setCellValue(rs.getString("fname"));
                        break;
                    case 4:
                        c.setCellValue(rs.getString("mname"));
                        break;
                    case 5:
                        c.setCellValue(rs.getString("address"));
                        break;
                    case 6:
                        c.setCellValue(rs.getString("address2"));
                        break;
                    case 7:
                        c.setCellValue(rs.getString("city"));
                        break;
                    case 8:
                        c.setCellValue(rs.getString("state"));
                        break;
                    case 9:
                        c.setCellValue(rs.getString("zip"));
                        break;
                    case 10:
                        c.setCellValue("USA");
                        break;
                    case 11:
                        c.setCellValue(rs.getString("ssn"));
                        break;
                    case 12: //SSN LAST NAME????
                        c.setCellValue("");
                        break;
                    case 13: //SSN First name???
                        c.setCellValue("");
                        break;
                    case 14: //SSN middle name???
                        c.setCellValue("");
                        break;
                    case 15: //1099 TIN 
                        c.setCellValue("");
                        break;
                    case 16: //1099 DBA/LLC Name
                        c.setCellValue("");
                        break;
                    case 17: //OFFICE CODE
                        c.setCellValue("");
                        break;
                    case 18: //Department Code always ten says Laura...dunno...
                        c.setCellValue("10");
                        break;
                    case 19: //LOcation Code...
                        c.setCellValue("");
                        break;
                    case 20: //Salesman 1 Code
                        c.setCellValue("");
                        break;
                    case 21: //Salesman 2 Code
                        c.setCellValue("");
                        break;
                    case 22: //Rank Code
                        c.setCellValue("");
                        break;
                    case 23: //Work Code
                        c.setCellValue("");
                        break;
                    case 24: //Transportation Code
                        c.setCellValue("");
                        break;
                    case 25: //External System Id
                        c.setCellValue("");
                        break;
                    case 26: //Marital Status fucka
                        c.setCellValue("");
                        break;
                    case 27: //Race
                        c.setCellValue("");
                        break;
                    case 28: //Sex
                        c.setCellValue("");
                        break;
                    case 29: //Birthdate
                        c.setCellValue("");
                        break;
                    case 30: //Hired On
                        c.setCellValue(StaticDateTimeFunctions.convertDatabaseDateToReadable(rs.getString("hdate")));
                        break;
                    case 31: //Rehired On
                        c.setCellValue("");
                        break;
                    case 32: //FIRST CHECK DATE MAY BE IMPORTANT
                        c.setCellValue("");
                        break;
                    case 33: //LAST CHECK DATE MAY BE IMPORTANT
                        c.setCellValue("");
                        break;
                    case 34: //Term Date
                        c.setCellValue(StaticDateTimeFunctions.convertDatabaseDateToReadable(rs.getString("tdate")));
                        break;
                    case 35: //FLAGS ANY TERM emp as No Call no show....
                        c.setCellValue("");
                        break;
//                    case 7:
//                        try {
//                            Calendar dateCal = StaticDateTimeFunctions.setCalendarToString(rs.getString("date"));
//                            dateCal.add(Calendar.MONTH, -1);
//                            String date = StaticDateTimeFunctions.convertCalendarToReadableFormat(dateCal);
//                            //StringTokenizer st = new StringTokenizer(date,"/");
//                            
//                            //Integer monthOfYear = Integer.parseInt(st.nextToken());
//                            //int moy = monthOfYear.intValue() - 1;
//                            //date = moy + "/";
//                            //Integer dayOfMonth = Integer.parseInt(st.nextToken());
//                            //date = date + dayOfMonth + "/" + st.nextToken();
//                            c.setCellValue(date);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        break;
//                    case 8:
//                        try {
//                            c.setCellType(c.CELL_TYPE_STRING);
//                            String time = StaticDateTimeFunctions.stringToMilitaryTime(rs.getString("start_time"));
//                            if (time.equals("2400")) {
//                                time = "0000";
//                            }
//                            c.setCellValue(time);
//                        } catch (Exception e) {}
//                        break;
//                    case 9:
//                        try {
//                            c.setCellType(c.CELL_TYPE_STRING);
//                            String time = StaticDateTimeFunctions.stringToMilitaryTime(rs.getString("end_time"));
//                            if (time.equals("2400")) {
//                                time = "0000";
//                            }
//                            c.setCellValue(time);
//                        } catch (Exception e) {}
//                        break;
//                        //case 10:
//                        //    //c.setCellValue("Position Code");
//                        //    break;
//                    case 11:
//                        break;
//                    case 12:
//                        break;
//                    case 13: //Should pay employee, set to NOH for not
//                        break;
//                    case 16: //Set Pay Break Minutes to zero to not allow ccs to validate non info
//                        c.setCellValue("0");
//                        break;
//                    case 18: //Should bill client, set to NOH for not
//                        break;
//                    case 21: //Set Bill Break Minutes to zero to not allow ccs to validate non info
//                        c.setCellValue("0");
//                        break;
                }
            }
            rs.moveNext();
        }
        try {
            wb.write(out);
            out.close();
        } catch (Exception e) {}
    }
    
    private class EmployeeInfo {
        public String eid;
        public String ueid;
        public EmployeeInfo(String empid, String empuid) {
            eid = empid;
            ueid = empuid;
        }
    }
    
    private class ClientInfo {
        public String UskedId;
        public String schedfoxId;
        public String worksite;
        
        public ClientInfo(String uskedId, String clientId, String worksiteId) {
            UskedId = uskedId;
            schedfoxId = clientId;
            worksite = worksiteId;
        }
    }
    
    private class ExtEmployeeInfo {
        public String eid;
        public String ename;
        
        public ExtEmployeeInfo(String empid, String empname) {
            eid = empid;
            ename = empname;
            
        }
    }

    
}
