/*
 * uskedTableDriver.java
 *
 * Created on February 22, 2006, 10:38 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.data_import.dataImportClasses.usked;
import rmischedule.components.data.*;
import rmischedule.data_import.dataImportClasses.destinationTables.*;
import rmischedule.data_import.dataImportClasses.*;
/**
 *
 * @author Ira Juneau
 * This class defines how to read from a Usked File System into a tableClass
 */
public class uskedTableDriver extends TableClass {
    
    private dbf_reader myUskedReader;
    
        /** Creates a new instance of uskedTableDriver */
    public uskedTableDriver(String path, String table, String columnOfUniqueKeys) {
        super(path, table, columnOfUniqueKeys);
    }
    
    public uskedTableDriver(String path, String table) {
        super(path, table);
    }
    
    protected void initializeConnectionToData() {
        myUskedReader = new dbf_reader(super.getPath() + super.getTable());
    }
    
    public String getPhysicalData(String colName) {
        return myUskedReader.getString(colName).trim();
    }
    
    public boolean goPrev() {
        return myUskedReader.movePrev();
    }
    
    public boolean goNext() {
        return myUskedReader.moveNext();
    }
    
    public boolean goFirst() {
        myUskedReader.moveFirst();
        return true;
    }
    
    public boolean goLast() {
        while (myUskedReader.moveNext()) {}
        return true;
    }
}
