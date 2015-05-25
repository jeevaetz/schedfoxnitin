/*
 * uskedDataSource.java
 *
 * Created on February 21, 2006, 12:56 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.data_import.dataImportClasses.usked;

import rmischedule.components.data.*;
import rmischedule.data_import.dataImportClasses.*;
import rmischedule.data_import.dataImportClasses.destinationTables.*;
import java.util.*;



/**
 *
 * @author Ira Juneau
 */
public class uskedDataSource extends GetDataFromSource {
    
    private String myCompanyName;
    private uskedTableDriver companyTable;
    private uskedTableDriver mainClient;
    private uskedTableDriver extClient;
    
    private uskedTableDriver mainEmployee;
    private uskedTableDriver extEmployee;
    
    private uskedTableDriver mainSchedule;
    
    /** Creates a new instance of uskedDataSource */
    public uskedDataSource(String sourceLocation) {
        super(sourceLocation);
        myCompanyName = "Unknown";
    }
    
    /**
     * Give me path of one individual folder containing one company only...
     */
    public void initializeSource(String sourceLocation) {
        sourceLocation = sourceLocation + "\\";
        companyTable = new uskedTableDriver(sourceLocation, "setmast.dbf");
        mainClient = new uskedTableDriver(sourceLocation, "cusmast.dbf", "CUCCUSMAST");
        extClient = new uskedTableDriver(sourceLocation, "cuscont.dbf", "CUCCUSMAST");
        
        mainEmployee = new uskedTableDriver(sourceLocation, "empmast.dbf", "EMCEMPMAST");
        extEmployee = new uskedTableDriver(sourceLocation, "empcont.dbf", "ECCEMPMAST");
        
        mainSchedule = new uskedTableDriver(sourceLocation, "schmast.dbf");
        
        mainClient.addJoinToTableOnJoinMethod(extClient);
        mainEmployee.addJoinToTableOnJoinMethod(extEmployee);
    }
    
    public TableClass getEmployeeTable() {
        return mainEmployee;
    }
    
    public TableClass getScheduleTable() {
        return mainSchedule;
    }
    
    public TableClass getClientTable() {
        return mainClient;
    }
    
    public ArrayList<String> getEmployeeData() {
        return null;
    }
    
    public ArrayList<String> getClientData() {
        return null;
    }
    
    public ArrayList<String> getScheduleData() {
        return null;
    }
    
    public SourceClass getCompanyName() {
        return new SourceClass(companyTable, "CDDNAME");
    }
    
    public SourceClass getClientName() {
        return new SourceClass(mainClient, "CUCNAME");
    }
    
    public SourceClass getClientAddress() {
        return new SourceClass(mainClient, "CUCADDR1");
    }
    
    public SourceClass getClientAddress2() {
        return new SourceClass(mainClient, "CUCADDR2");
    }
    
    public SourceClass getClientCity() {
        return new SourceClass(mainClient, "CUCCITY");
    }
    
    public SourceClass getClientState() {
        return new SourceClass(mainClient, "CUCSTATE");
    }
    
    public SourceClass getClientZip() {
        return new SourceClass(mainClient, "CUCZIPCODE");
    }
    
    public SourceClass getClientDeleted() {
        return new SourceClass(mainClient, "CUCCUSMAST");
    }
    
    public SourceClass getClientWorksite() {
        return new SourceClass(mainClient, "CUCCUSMAST");
    }
    
    public SourceClass getClientPhone() {
        return new SourceClass(extClient, "CCCPHONE1");
    }
    
    public SourceClass getClientPhone2() {
        return new SourceClass(extClient, "CCCPHONE2");
    }
    
    public SourceClass getClientCell() {
        return new SourceClass(extClient, "CCCPHONE3");
    }
    
    public SourceClass getClientEMail() {
        return new SourceClass(extClient, "CCCEMAIL");
    }
    
    public SourceClass getEmployeeId() {
        return new SourceClass(mainEmployee, "EMCEMPID");
    }
    
    public SourceClass getEmployeeFName() {
        return new SourceClass(mainEmployee, "EMCFNAME");
    }
    
    public SourceClass getEmployeeMName() {
        return new SourceClass(mainEmployee, "EMCMNAME");
    }
    
    public SourceClass getEmployeeLName() {
        return new SourceClass(mainEmployee, "EMCLNAME");
    }
    
    public SourceClass getEmployeeAddress() {
        return new SourceClass(mainEmployee, "EMCADDR1");
    }
    
    public SourceClass getEmployeeAddress2() {
        return new SourceClass(mainEmployee, "EMCADDR2");
    }
    
    public SourceClass getEmployeeCity() {
        return new SourceClass(mainEmployee, "EMCCITY");
    }
    
    public SourceClass getEmployeeState() {
        return new SourceClass(mainEmployee, "EMCSTATE");
    }
    
    public SourceClass getEmployeeZip() {
        return new SourceClass(mainEmployee, "EMCZIPCODE");
    }
    
    public SourceClass getEmployeeHire() {
        return new SourceClass(mainEmployee, "EMTHIRE");
    }
    
    public SourceClass getEmployeeTerm() {
        return new SourceClass(mainEmployee, "EMTTERM");
    }
    
    public SourceClass getEmployeeSSN() {
        return new SourceClass(mainEmployee, "EMCSSNO");
    }
    
    public SourceClass getEmployeeCell() {
        return new SourceClass(extEmployee, "EMCPHONE3");
    }
    
    public SourceClass getEmployeePhone() {
        return new SourceClass(extEmployee, "ECCPHONE1");
    }
    
    public SourceClass getEmployeePhone2() {
        return new SourceClass(extEmployee, "ECCPHONE2");
    }
    
    public SourceClass getEmployeeEMail() {
        return new SourceClass(extEmployee, "EMCEMAIL");
    }
    
    public SourceClass getEmployeeDeleted() {
        return new SourceClass(extEmployee, "EMCSTATUS");
    }
    
    public SourceClass getEmployeeBirthDate() {
        return new SourceClass(mainEmployee, "EMTBIRTH");
    }
    
    public SourceClass getScheduleDate() {
        return new SourceClass(mainSchedule, "SMTSHFTBEG");
    }
    
    public SourceClass getScheduleEnd() {
        return new SourceClass(mainSchedule, "SMTSHFTEND");
    }
    
    public SourceClass getScheduleStart() {
        return new SourceClass(mainSchedule, "SMTSHFTBEG");
    }
    
    public static void main(String args[]) {
        //uskedDataSource mySource = new uskedDataSource();
        //mySource.initializeSource("C:\\ultra32\\ultra32\\DATA01");
    }
}
