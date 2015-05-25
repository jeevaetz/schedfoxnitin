/*
 * OptionsDataClass.java
 *
 * Created on September 1, 2005, 1:26 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.options;
import schedfoxlib.model.util.Record_Set;
import rmischedule.main.Main_Window;
import rmischedule.options.optiontypeclasses.*;
import rmischedule.data_connection.Connection;
import rmischeduleserver.mysqlconnectivity.queries.util.*;
import rmischeduleserver.mysqlconnectivity.queries.*;
import java.util.*;
/**
 *
 * @author Ira Juneau
 *
 * Ahh this class provides a interface between our options data and everything else.... beautiful
 */
public class OptionsDataClass {
    
    private int userId;
    private int managementId;
    
    private LinkedHashMap<String, IndividualOption> myOptionsHashtable;
    private ArrayList<IndividualOption> myOptionsArrayList;
    
    public static final int ROOT_USER = 0;
    public static final String ROOT_USER_DBASE = "0";
    
    private options_query myListQuery;
    
    public static final String OPTIONS_BOOLEAN = "1";
    public static final String OPTIONS_INT     = "2";
    public static final String OPTIONS_STRING  = "3";
    public static final String OPTIONS_COLOR   = "4";
    public static final String OPTIONS_TIME    = "5";
    public static final String OPTIONS_COMBO   = "6";
    
    public static final String numweeksinpast = "numofweekspast";
    public static final String showratecodes = "showratecodes";
    public static final String numweeksinfuture = "numofweeksfuture";
    public static final String is12Hour = "12hour";
    public static final String hoursforConflict = "conflictbuffer";
    public static final String markUncon = "showunconfirmed";
    public static final String numHoursNewEmp = "dbnewemptime";
    public static final String showManagement = "cshowmanagement";
    public static final String showRate = "cshowrate";
    public static final String showTrain = "cshowtrain";
    public static final String DisplayTotalShiftsOrTimes = "DisplayTotalShiftsOrTimes";
    public static final String notification8hrOvertime = "8hournotification";
    public static final String showClientExport = "cshowuskedclientexp";
    public static final String showEmployeeExport = "eshowuskedclientexp";
    
    /** Creates a new instance of OptionsDataClass */
    public OptionsDataClass() {
        userId = Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId());
        try {
            managementId = Integer.parseInt(Main_Window.parentOfApplication.getManagementId());
        } catch (Exception e) {}
        managementId = -1;
        myOptionsHashtable = new LinkedHashMap();
        myOptionsArrayList = new ArrayList();
        GrabAndParseData();
        FillOptionsWithData();
    }
    
    /**
     * Sorts data for visual display by category and alphebetize them, returns the 
     * array of components...
     */
    public ArrayList<IndividualOption> getArrayOfComponents() {
        try {
            Collections.sort(myOptionsArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myOptionsArrayList;
    }
    
    /**
     * Grabs new data from our database and makes all options etc...
     */
    public void GrabAndParseData() {
        myListQuery = new options_query();
        Record_Set myListOfOptions = new Record_Set();
        Connection myConnection = new Connection();
        try {
            myListOfOptions = myConnection.executeQuery(myListQuery);
        } catch (Exception e) {}
        for (int i = 0; i < myListOfOptions.length(); i++) {
            String myName = myListOfOptions.getString("name_field");
            String myType = myListOfOptions.getString("type_field");
            String category = myListOfOptions.getString("category_field");
            String display = myListOfOptions.getString("display_field");
            IndividualOption myClass = chooseOptionClassOnType(myName, myType, category, display);
            myOptionsHashtable.put(myName, myClass);
            myOptionsArrayList.add(myClass);
            myListOfOptions.moveNext();
        }
    }
    
    public IndividualOption getOptionByName(String name) {
        return myOptionsHashtable.get(name);
    }
    
    /**
     * This method grabs each individual option getting it's appropriate query, and 
     * loading info into each one....
     */
    public void FillOptionsWithData() {
        RunQueriesEx myCompleteOptionQuery = new RunQueriesEx();
        Connection myConn = new Connection();
        ArrayList myList = new ArrayList(myOptionsArrayList.size());
        for (int i = 0; i < myOptionsArrayList.size(); i++) {
            myCompleteOptionQuery.add(myOptionsArrayList.get(i).getQuery());
        }
        try {
            myList = myConn.executeQueryEx(myCompleteOptionQuery);
        } catch (Exception e) {}
        for (int i = 0; i < myOptionsArrayList.size(); i++) {
            myOptionsArrayList.get(i).parseData((Record_Set)myList.get(i));
        }
    }
    
    /**
     * Gets appropriate sub class with read and write methods overridden to easily convert
     * between string data and objects...
     */
    private IndividualOption chooseOptionClassOnType(String name, String type, String category, String display) {
        if (type.equals(OPTIONS_STRING)) {
            return new StringOptionClass(managementId, userId, name, category, display);
        } else if (type.equals(OPTIONS_BOOLEAN)) {
            return new BooleanOptionClass(managementId, userId, name, category, display); 
        } else if (type.equals(OPTIONS_INT)) {
            return new IntegerOptionClass(managementId, userId, name, category, display);
        } else if (type.equals(OPTIONS_STRING)) {
            return new StringOptionClass(managementId, userId, name, category, display);
        } else if (type.equals(OPTIONS_COLOR)) {
            return new ColorOptionClass(managementId, userId, name, category, display);
        } else if (type.equals(OPTIONS_TIME)) {
            return new TimeOptionClass(managementId, userId, name, category, display);
        } else if (type.equals(OPTIONS_COMBO)) {
            return new ComboOptionClass(managementId, userId, name, category, display);
        }
        return null;
    }
    
    public String toString() {
        StringBuilder myString = new StringBuilder(100);
        for (int i = 0; i < myOptionsArrayList.size(); i++) {
            myString.append(myOptionsArrayList.get(i).toString() + "\n");
        }
        return myString.toString();
    }
}
