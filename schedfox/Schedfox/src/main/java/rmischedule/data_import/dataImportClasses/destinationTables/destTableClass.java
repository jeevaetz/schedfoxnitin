/*
 * destTableClass.java
 *
 * Created on February 22, 2006, 10:51 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.data_import.dataImportClasses.destinationTables;
import rmischedule.data_import.dataImportClasses.*;
/**
 *
 * @author Ira Juneau
 */
public class destTableClass extends TableClass {
    
    /** Creates a new instance of destTableClass */
    /**
     * No Unique key may not bind this table to other tables....
     */
    public destTableClass(String path, String table) {
        super(path, table);
    }
    
    /**
     * Will be building queries out of this...dont' need
     */
    protected void initializeConnectionToData() {}
    protected boolean goNext() {return true;}
    protected boolean goFirst() {return true;}
    protected boolean goLast() {return true;}
    protected boolean goPrev() {return true;}
    public String getPhysicalData(String columnName) {return "";}
}
