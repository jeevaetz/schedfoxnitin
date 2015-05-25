/*
 * SourceToDestinationClass.java
 *
 * Created on February 21, 2006, 1:09 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.data_import.dataImportClasses;

import java.util.*;

/**
 *
 * @author Ira Juneau
 * Important file, this is overloaded to define which fields are tied to which....for each
 * import type...some are hard coded ie: Usked predefined fields...some will be defined 
 * by user ie: Custom Excel spreadsheets...
 */
public class SourceToDestinationClass {
    
    private String destinationFieldName;
    private TableClass destTableName;
    private SourceClass mySourceInfo;
    
    /** Creates a new instance of SourceToDestinationClass */
    public SourceToDestinationClass(TableClass destTable, String destName) {
        destinationFieldName = destName;
        destTableName = destTable;
        mySourceInfo = new SourceClass();
    }
    
    public void setSource(SourceClass newSource) {
        mySourceInfo = newSource;
    }
    
    /**
     * Does this provide source data or not?
     */
    public boolean hasSourceData() {
        return mySourceInfo.hasData();
    }
    
    /**
     * Gets the source field...ie for usked...CUCCUSTID is the cust ID
     */
    public String getSourceName() {
        return mySourceInfo.myColumn();
    }
    
    public String getSourceTable() {
        return mySourceInfo.getTableName();
    }
    
    /**
     * Gets the destination field...in the Table defined by destTable..ie:
     * Destination Name = employee_first_name in table employee....
     * like new SourceToDestinationClass("EMPID", "employee", "employee_first_name");
     */
    public String getDestinationName() {
        return destinationFieldName;
    }
    
    /**
     * What table is this shit contained in...god damn...
     */
    public String getDestinationTable() {
        return destTableName.getTable();
    }
    
}
