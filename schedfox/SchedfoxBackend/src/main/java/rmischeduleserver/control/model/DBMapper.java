/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.control.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ijuneau
 */
public abstract class DBMapper {
    protected RMIScheduleServerImpl myConn;
    protected ArrayList<String> columnHeaders;
    protected Vector<BoundMappings> boundMappings;
    protected ArrayList<ArrayList<String>> excelData;

    public abstract LinkedHashMap<DataMappingClass, Integer> getMappings();
    public abstract LinkedHashMap<DataMappingClass, Integer> getUskedMappings();
    public abstract void insertValuesIntoDB(LinkedHashMap<DataMappingClass, Integer> mappedColumns);

    protected String branchId;
    protected String companyId;

    public DBMapper(RMIScheduleServerImpl myConn, String companyId, String branchId) {
        this.myConn = myConn;
        this.branchId = branchId;
        this.companyId = companyId;
    }

    public DBMapper(ArrayList<String> columnHeaders, ArrayList<ArrayList<String>> excelData, RMIScheduleServerImpl myConn, String branchId) {
        this.columnHeaders = columnHeaders;
        this.excelData = excelData;
        this.myConn = myConn;
        this.branchId = branchId;
    }

    public GeneralQueryFormat generateQuery(Object[] params) {
        return null;
    }

    protected void helperUskedMapMethod(LinkedHashMap<DataMappingClass, Integer> retVal, String tableName, String column, String uskedId) {
        TableMapClass tUskedMap = new TableMapClass(tableName, column);
        DataMappingClass uskedDataMap = new DataMappingClass(tUskedMap, uskedId);
        uskedDataMap.setUskedId(uskedId);
        uskedDataMap.setShouldDisplayInManualExport(false);
        retVal.put(uskedDataMap, -1);
    }

    /**
     * Returns the column names for the employee import process
     * @return ArrayList<String>
     */
    public ArrayList<String> getColumnNames() {
        ArrayList<String> retVal = new ArrayList<String>();
        LinkedHashMap<DataMappingClass, Integer> dataClasses = this.getMappings();
        Iterator<DataMappingClass> keys = dataClasses.keySet().iterator();
        while (keys.hasNext()) {
            DataMappingClass dataClass = keys.next();
            retVal.add(dataClass.getFirstValue());

        }
        return retVal;
    }

    /**
     * If the user has manually set certain bound columns we set them here.
     * @param boundMappings
     */
    public void setBoundMappings(Vector<BoundMappings> boundMappings) {
        this.boundMappings = boundMappings;
    }

    /**
     * Returns the data mapping information for the provided table, so we can
     * generate our SQL to insert / update.
     * @param mappedColumns Our mapped columns.
     * @param tableName String the table
     * @return ArrayList<DataMappingClass>
     */
    public ArrayList<DataMappingClass> getDataMappingByTable(LinkedHashMap<DataMappingClass, Integer> mappedColumns, String tableName) {
        Iterator<DataMappingClass> keysIterator = mappedColumns.keySet().iterator();
        ArrayList<DataMappingClass> retVal = new ArrayList<DataMappingClass>();
        while (keysIterator.hasNext()) {
            DataMappingClass dataClass = keysIterator.next();
            if (dataClass.getTableMapping().getTableName().equalsIgnoreCase(tableName)) {
                retVal.add(dataClass);
            }
        }
        return retVal;
    }

    /**
     * This is used to parse through a collection of the DataMappingClass, for a particular column
     * and it retrieves the value.
     * @param columnName String the name of the column you want to retrieve
     * @param values The values of all of the columns we are searching through
     * @return DataMappingClass
     */
    public DataMappingClass getDataMappingClassByColumnName(String columnName, ArrayList<DataMappingClass> values) {
        DataMappingClass retVal = null;
        for (int v = 0; v < values.size(); v++) {
            if (values.get(v).getTableMapping().getColumnName().equalsIgnoreCase(columnName)) {
                retVal = values.get(v);
            }
        }
        return retVal;
    }

    /**
     * Returns the list of columns that are not recognized by our import process.
     * @return ArrayList<String>
     */
    public Vector<String> findUnrecognizedColumnNames() {
        Vector<String> retVal = new Vector<String>();
        ArrayList<String> validColumnNames = new ArrayList<String>();
        LinkedHashMap<DataMappingClass, Integer> tempColumnNames = this.getMappings();
        Iterator<DataMappingClass> dataIt = tempColumnNames.keySet().iterator();
        while (dataIt.hasNext()) {
            validColumnNames.add(dataIt.next().getFirstValue());
        }
        for (int ch = 0; ch < columnHeaders.size(); ch++) {
            boolean found = false;
            for (int vc = 0; vc < validColumnNames.size(); vc++) {
                if (validColumnNames.get(vc).equalsIgnoreCase(columnHeaders.get(ch))) {
                    found = true;
                }
            }
            if (!found) {
                retVal.add(columnHeaders.get(ch));
            }
        }
        return retVal;
    }

    /**
     * Returns what column names we expect that are not currently being used.
     * @return ArrayList<String>
     */
    public Vector<String> getUnusedColumnNames() {
        Vector<String> retVal = new Vector<String>();
        ArrayList<String> validColumnNames = new ArrayList<String>();
        LinkedHashMap<DataMappingClass, Integer> tempColumnNames = this.getMappings();
        Iterator<DataMappingClass> dataIt = tempColumnNames.keySet().iterator();
        while (dataIt.hasNext()) {
            validColumnNames.add(dataIt.next().getFirstValue());
        }
        for (int ch = 0; ch < validColumnNames.size(); ch++) {
            boolean found = false;
            for (int vc = 0; vc < columnHeaders.size(); vc++) {
                if (validColumnNames.get(ch).equalsIgnoreCase(columnHeaders.get(vc))) {
                    found = true;
                }
            }
            if (!found) {
                retVal.add(validColumnNames.get(ch));
            }
        }
        return retVal;
    }

    public void processImport() {
        LinkedHashMap<DataMappingClass, Integer> mappedColumns = this.retrieveColumnPositioning();
        this.processData(mappedColumns);
        this.insertValuesIntoDB(mappedColumns);
    }

    /**
     * Small method to search through our columns for the provided value and returns
     * the position of the found value.
     * @param searchVal String we are searching for
     * @return The index of the found value or -1 if no value was matched.
     */
    private int searchColumnsForString(String searchVal) {
        int retVal = -1;
        for (int c = 0; c < this.columnHeaders.size(); c++) {
            if (columnHeaders.get(c).equalsIgnoreCase(searchVal)) {
                retVal = c;
            }
        }
        return retVal;
    }

    /**
     * This method is responsible for parsing through our columns and finding first the mapping
     * for any self mapped columns, and secondly the mapping for the regular mappings.
     * @return
     */
    public LinkedHashMap<DataMappingClass, Integer> retrieveColumnPositioning() {
        LinkedHashMap<DataMappingClass, Integer> retVal = this.getMappings();
        //First check any bound mappings specified by the user.
        if (boundMappings != null && boundMappings.size() > 0) {
            Iterator<DataMappingClass> keyIterator = retVal.keySet().iterator();
            while (keyIterator.hasNext()) {
                DataMappingClass currKey = keyIterator.next();
                //Loop through bound Mappings to see if we dealt with this column
                for (int bm = 0; bm < boundMappings.size(); bm++) {
                    BoundMappings boundMap = boundMappings.get(bm);
                    if (currKey.equals(boundMap.getUnusedMapping())) {
                        //Ok we manually linked these together lets no search the columns for the position.
                        retVal.put(currKey, searchColumnsForString(boundMap.getUnrecognizedMapping()));
                    }
                }
            }
        }
        //Now just check the regular mappings and map which column lies where
        Iterator<DataMappingClass> keyIterator = retVal.keySet().iterator();
        while (keyIterator.hasNext()) {
            DataMappingClass currKey = keyIterator.next();
            ArrayList<String> values = currKey.getMappingValues();
            for (int v = 0; v < values.size(); v++) {
                int pos = searchColumnsForString(values.get(v));
                if (pos != -1) {
                    retVal.put(currKey, pos);
                }
            }
        }
        return retVal;
    }

    /**
     * Processes the data, and loads it into our hash objects.
     * @param mappedColumns
     */
    protected void processData(LinkedHashMap<DataMappingClass, Integer> mappedColumns) {
        //Loop through the rows
        for (ArrayList<String> currRow : this.excelData) {
            //Loop through the columns of the current row.
            for (int i = 0; i < currRow.size(); i++) {
                //The column will match up with the value in the hashmap.
                Iterator<DataMappingClass> keys = mappedColumns.keySet().iterator();
                while (keys.hasNext()) {
                    DataMappingClass currKey = keys.next();
                    Integer columnPos = mappedColumns.get(currKey);
                    if (columnPos == i) {
                        currKey.setValue(currRow.get(i));
                    }
                }
            }
        }
    }

    protected class UskedMaritalColumn extends TableMapClass {
        public UskedMaritalColumn(String tableName, String columnName) {
            super(tableName, columnName);
        }

        //Override to customize values.
        @Override
        public Object translateValue(String value) {
            return "S";
        }
    }

    protected class UskedFedStatusColumn extends TableMapClass {
        public UskedFedStatusColumn(String tableName, String columnName) {
            super(tableName, columnName);
        }

        //Override to customize values.
        @Override
        public Object translateValue(String value) {
            if (value == null || value.length() == 0 || value.equals("0")) {
                return "S";
            }
            return "S";
        }
    }

    protected class UskedDirectDepositAccountColumn extends TableMapClass {

        private boolean hasData;

        public UskedDirectDepositAccountColumn(String tableName, String columnName) {
            super(tableName, columnName);
            this.hasData = false;
        }

        //Override to customize values.
        @Override
        public Object translateValue(String value) {
            this.hasData = false;
            if (value == null || value.equalsIgnoreCase("0")) {
                return "";
            }
            hasData = true;
            return value;
        }

        public boolean hasData() {
            return this.hasData;
        }
    }

    protected class UskedDirectDepositRoutingColumn extends TableMapClass {

        private UskedDirectDepositAccountColumn directDepositAccount;

        public UskedDirectDepositRoutingColumn(String tableName, String columnName, UskedDirectDepositAccountColumn directDepositAccount) {
            super(tableName, columnName);
            this.directDepositAccount = directDepositAccount;
        }

        //Override to customize values.
        @Override
        public Object translateValue(String value) {
            if (value != null && !value.equalsIgnoreCase("0") && this.directDepositAccount.hasData()) {
                return value;
            }
            return "";
        }
    }

    protected class UskedDirectDepositTypeColumn extends TableMapClass {

        private UskedDirectDepositAccountColumn directDepositAccount;

        public UskedDirectDepositTypeColumn(String tableName, String columnName, UskedDirectDepositAccountColumn directDepositAccount) {
            super(tableName, columnName);

            this.directDepositAccount = directDepositAccount;
        }

        //Override to customize values.
        @Override
        public Object translateValue(String value) {
            if (value != null && !value.equalsIgnoreCase("0") && this.directDepositAccount.hasData()) {
                return "C";
            }
            return "";
        }
    }

    protected class UskedDirectDepositPrenoteColumn extends TableMapClass {

        private UskedDirectDepositAccountColumn accountNumber;

        public UskedDirectDepositPrenoteColumn(String tableName, String columnName, UskedDirectDepositAccountColumn accountNumber) {
            super(tableName, columnName);
            this.accountNumber = accountNumber;
        }

        //Override to customize values.
        @Override
        public Object translateValue(String value) {
            if (this.accountNumber.hasData()) {
                return "N";
            }
            return "";
        }
    }

    protected class UskedHolidayPayColumn extends TableMapClass {
        public UskedHolidayPayColumn(String tableName, String columnName) {
            super(tableName, columnName);
        }

        //Override to customize values.
        @Override
        public Object translateValue(String value) {
            return "M";
        }
    }

    protected class UskedTaxCodeColumn extends TableMapClass {
        private boolean hasData = false;

        public UskedTaxCodeColumn(String tableName, String columnName) {
            super(tableName, columnName);
        }

        //Override to customize values.
        @Override
        public Object translateValue(String value) {
            if (value == null || value.equalsIgnoreCase("0")) {
                return "";
            }
            hasData = true;
            return value;
        }

        public boolean hasData() {
            return hasData;
        }
    }

    protected class UskedRateInfoColumn extends TableMapClass {
        private boolean hasData = false;

        public UskedRateInfoColumn(String tableName, String columnName) {
            super(tableName, columnName);
        }

        //Override to customize values.
        @Override
        public Object translateValue(String value) {
            Object val = value;
            if (value == null || value.equalsIgnoreCase("0")) {
                return "";
            }
            try {
                val = Double.parseDouble(value);
            } catch (Exception e) {}
            hasData = true;
            return val;
        }

        public boolean hasData() {
            return hasData;
        }
    }

    protected class UskedTaxStatusColumn extends TableMapClass {

        private UskedTaxCodeColumn taxCode;

        public UskedTaxStatusColumn(String tableName, String columnName, UskedTaxCodeColumn taxCode) {
            super(tableName, columnName);
            this.taxCode = taxCode;
        }

        //Override to customize values.
        @Override
        public Object translateValue(String value) {
            if (value == null || !taxCode.hasData()) {
                return "";
            }
            return value;
        }
    }

    protected class UskedTaxExemptionsColumn extends TableMapClass {

        private UskedTaxCodeColumn taxCode;

        public UskedTaxExemptionsColumn(String tableName, String columnName, UskedTaxCodeColumn taxCode) {
            super(tableName, columnName);
            this.taxCode = taxCode;
        }

        //Override to customize values.
        @Override
        public Object translateValue(String value) {
            if (value == null || !taxCode.hasData()) {
                return "";
            }
            return value;
        }
    }

    protected class UskedTaxWithholdingsColumn extends TableMapClass {

        private UskedTaxCodeColumn taxCode;

        public UskedTaxWithholdingsColumn(String tableName, String columnName, UskedTaxCodeColumn taxCode) {
            super(tableName, columnName);
            this.taxCode = taxCode;
        }

        //Override to customize values.
        @Override
        public Object translateValue(String value) {
            if (value == null || !taxCode.hasData()) {
                return "";
            }
            return value;
        }
    }

    protected class UskedActiveColumn extends TableMapClass {
        public UskedActiveColumn(String tableName, String columnName) {
            super(tableName, columnName);
        }

        //Override to customize values.
        @Override
        public Object translateValue(String value) {
            if (value.equals("0")) {
                return "A";
            } else {
                return "I";
            }
        }
    }

    protected class UskedSSNColumn extends TableMapClass {
        public UskedSSNColumn(String tableName, String columnName) {
            super(tableName, columnName);
        }

        //Override to customize values.
        @Override
        public Object translateValue(String value) {
            if (value.length() == 9) {
                return value.substring(0, 3) + "-" + value.substring(3, 5) + "-" + value.substring(5, 9);
            }
            return value;
        }
    }

    protected class UskedDateColumn extends TableMapClass {

        private SimpleDateFormat databaseFormat = new SimpleDateFormat("yyyy-MM-dd mm:hh:ss");
        private SimpleDateFormat databaseFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        private SimpleDateFormat exportFormat = new SimpleDateFormat("MM/dd/yyyy");

        public UskedDateColumn(String tableName, String columnName) {
            super(tableName, columnName);
        }

        //Override to customize values.
        @Override
        public Object translateValue(String value) {
            Date currDate = new Date();
            Date minDate = new Date("01/01/1910");
            try {
                Date tempDate = databaseFormat.parse(value);
                if (currDate.after(tempDate) && minDate.before(tempDate)) {
                    return exportFormat.format(tempDate);
                }
            } catch (Exception e) {
                try {
                    Date tempDate = databaseFormat2.parse(value);
                    if (currDate.after(tempDate) && minDate.before(tempDate)) {
                        return exportFormat.format(tempDate);
                    }
                } catch (Exception exe) {
                    return "";
                }
            }
            return "";
        }
    }

    protected class UskedCurrentDateColumn extends TableMapClass {

        private SimpleDateFormat exportFormat = new SimpleDateFormat("MM/dd/yyyy");

        public UskedCurrentDateColumn(String tableName, String columnName) {
            super(tableName, columnName);
        }

        //Override to customize values.
        @Override
        public Object translateValue(String value) {
            return exportFormat.format(new Date());
        }
    }
}
