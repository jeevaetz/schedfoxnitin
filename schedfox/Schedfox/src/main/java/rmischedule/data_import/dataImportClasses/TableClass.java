/*
 * TableClass.java
 *
 * Created on February 21, 2006, 2:11 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.data_import.dataImportClasses;
import java.util.*;
/**
 *
 * @author Ira Juneau
 * Ok this class defines a Table very generically, the idea of this is the following.
 * Extend this class for the specific physical table type...ie Usked, Excel so on, to
 * define how to navigate through table, define goNext, all go Methods, in addition
 * to the actual connectionToData method the opens the file...then for each
 * table in your import all you need to do is really define how they are joined togther.
 * You may do this by 
 */
public abstract class TableClass {
    
    private String myPath;
    private String myTable;
    private String myKey;
    private ArrayList<TableClass> mySubTables;
    private ArrayList<SourceClass> myFieldsToSelect;
    private boolean bound;
    
    /** Creates a new instance of TableClass */
    public TableClass(String path, String table, String columnOfUniqueKeys) {
        myPath = path;
        myTable = table;
        bound = false;
        myKey = columnOfUniqueKeys;
        mySubTables = new ArrayList();
        myFieldsToSelect = new ArrayList();
        initializeConnectionToData();
    }
    
    /**
     * No Unique key may not bind this table to other tables....
     */
    public TableClass(String path, String table) {
        myPath = path;
        myTable = table;
        myKey = null;
        bound = false;
        mySubTables = new ArrayList();
        myFieldsToSelect = new ArrayList();
        initializeConnectionToData();
    }
    
    /**
     * Adds a field to the arraylist 
     */
    public void addFieldToTableSelect(SourceClass myField) {
        myFieldsToSelect.add(myField);
    }
    
    /**
     * Returns total field size including all joined tables....
     */
    public int getFieldSize() {
        int subTablesSize = 0;
        for (int i = 0; i < mySubTables.size(); i++) {
            subTablesSize += mySubTables.get(i).getFieldSize();
        }
        return myFieldsToSelect.size();
    }
    
    /**
     * Used to get Data From this particular table...and all of it's sub tables that are
     * joined together...
     */
    public ArrayList<String> getDataFromRow(ArrayList<SourceToDestinationClass> fieldsToGrab) {
        ArrayList<String> retArray = new ArrayList();
        for (int i = 0; i < myFieldsToSelect.size(); i++) {
            for (int y = 0; y < fieldsToGrab.size(); y++) {
                if (fieldsToGrab.get(y).getSourceName().equals(myFieldsToSelect.get(i).myColumn()) && 
                    fieldsToGrab.get(y).getSourceTable().equals(getTable())) {
                    retArray.add(getData(myFieldsToSelect.get(i)));
                }
            }
        }
        for (int i = 0; i < mySubTables.size(); i++) {
            if (mySubTables.get(i).isBoundCorrectly()) {
                retArray.addAll(mySubTables.get(i).getDataFromRow(fieldsToGrab));
            }
        }
        return retArray;
    }
    
    /**
     * Tells master table if current row is bound, or if we shoud just return blank
     * data off of master bind.
     */
    public boolean isBoundCorrectly() {
        return bound;
    }
    
    /**
     * Used by the join method to state if this table is bound correctly or not
     */
    public void setBoundCorrently(boolean isBound) {
        bound = isBound;
    }
    
    /**
     * Does this table posses a unique key, if not return false, and may not
     * be bound to other tables....
     */
    public boolean hasUniqueKey() {
        if (myKey == null) {
            return false;
        }
        return true;
    }
    
    /**
     * Use this if seperate physical tables are used....define how joined together using the 
     * getJoinId and everytime you traverse this table will also traverse joined tables to line
     * everything up and can pull data...isn't it great? BOTH TABLES MUST HAVE UNIQUE ID...
     * USE CORRECT CONSTRUCTOR
     */
    public void addJoinToTableOnJoinMethod(TableClass subTable) {
        if (!hasUniqueKey() || !subTable.hasUniqueKey()) {
            return;
        }
        mySubTables.add(subTable);
    }
    
    /**
     * Uses methods moveFirst...and moveNext on all sub tables along with 
     * getJoinId to join together all sub tables...great...Can overload if you 
     * for specific table type have a binary sort or something that is nicer than
     * traversing the table on each move, to find specific bind value...
     */
    protected boolean rebindTablesTogetherOnJoin() {
        boolean succeeded = true;
        for (int i = 0; i < mySubTables.size(); i++) {
            mySubTables.get(i).moveFirst();
            boolean foundJoin = false;
            do {
                if (getJoinId().compareTo(mySubTables.get(i).getJoinId()) == 0) {
                    foundJoin = true;
                }
            } while (mySubTables.get(i).moveNext() && !foundJoin);
            if (!foundJoin) {
                mySubTables.get(i).setBoundCorrently(false);
            } else {
                mySubTables.get(i).setBoundCorrently(true);
            }
        }
        return succeeded;
    }
    
    /**
     * Define abstracts to define how your table is structure physically...
     */
    
    /**
     * Overload to do just what it says...These methods define physical transactions
     * with specific table types....
     */
    protected abstract void initializeConnectionToData();
    protected abstract boolean goNext();
    protected abstract boolean goFirst();
    protected abstract boolean goLast();
    protected abstract boolean goPrev();
    public abstract String getPhysicalData(String columnName);
    
    /**
     * Ahhh gets physical data, using the column name defined in SourceClass...then processes it
     * as according to the method processData in the SourceClass...for the specific source...fun
     */
    public String getData(SourceClass getSource) {
        return getSource.processData(getPhysicalData(getSource.myColumn()));
    }
    
    /**
     * Important method overload if you need to do anything more in depth to get the 
     * join ID, by default it just calls getData on the column name provided and returns the
     * result...
     */
    public String getJoinId() {
        return getPhysicalData(myKey).trim();
    }
    
    public boolean moveNext() {
        boolean val = goNext();
        rebindTablesTogetherOnJoin();
        return val;
    }
    
    public boolean moveFirst() {
        boolean val = goFirst();
        rebindTablesTogetherOnJoin();
        return val;
    }
    
    public boolean moveLast() {
        boolean val = goLast();
        rebindTablesTogetherOnJoin();
        return val;
    }
    
    public boolean movePrev() {
        boolean val = goPrev();
        rebindTablesTogetherOnJoin();
        return val;
    }
    
    public String getPath() {
        return myPath;
    }
    
    public String getTable() {
        return myTable;
    }
    
}
