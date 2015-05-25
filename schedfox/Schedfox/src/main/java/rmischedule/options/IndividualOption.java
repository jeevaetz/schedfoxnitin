/*
 * IndividualOption.java
 *
 * Created on September 1, 2005, 1:32 PM
 *
 * Copyright: SchedFox 2005
 */
package rmischedule.options;

import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.mysqlconnectivity.queries.util.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.*;
import rmischedule.data_connection.*;

/**
 *
 * @author Ira Juneau
 *
 * This is our individual Option and it defines the read/write for the particular option...
 */
public abstract class IndividualOption implements Comparable<IndividualOption> {

    public static final int GLOBAL = 0;
    public static final int CLIENT = 1;
    public static final int USER = 2;
    public ArrayList<individualOptionData> dataICanReadFrom;
    public ArrayList<individualOptionData> dataICanWriteTo;
    private int managementCompany;
    private int userId;
    private String myName;
    private String myCategory;
    private String myDisplay;

    /** Creates a new instance of individualOptionClass */
    public IndividualOption(int managementCo, int UserId, String myOptionName, String category, java.lang.String displayName) {
        managementCompany = managementCo;
        userId = UserId;
        myName = myOptionName;
        myDisplay = displayName;
        myCategory = category;
        dataICanReadFrom = new ArrayList();
        dataICanWriteTo = new ArrayList();
    }

    public String getMyName() {
        return myName;
    }

    public String getMyCategory() {
        return myCategory;
    }

    public String getMyDisplay() {
        return myDisplay;
    }

    /**
     * Gets the query for this class...
     */
    public GeneralQueryFormat getQuery() {
        get_options_values_by_name myQuery = new get_options_values_by_name();
        myQuery.update(myName);
        return myQuery;
    }

    /**
     * Gets the graphical component for this class...
     */
    public abstract JPanel getGraphicalComponent(int AccessLevel);

    /**
     * After queries have been run will take data in from parent class and parse it all
     * figuring out what data is valid to read and write and so on....
     */
    public void parseData(Record_Set rs) {
        String managementOfDatabase = new String();
        String optionId = new String();
        String value = new String();
        for (int i = 0; i < rs.length(); i++) {
            managementOfDatabase = rs.getString("id");
            optionId = rs.getString("optid");
            value = rs.getString("val");
            individualOptionData myData = new individualOptionData(value, managementOfDatabase, optionId);
            if (myData.canRead()) {
                dataICanReadFrom.add(myData);
            }
            //if (myData.canWrite()) {
            dataICanWriteTo.add(myData);
            //}
            rs.moveNext();
        }
    }

    public Object read() {
        return read(-1);
    }

    /**
     * Requests to read this option for appropriate Access...or User...
     * Null if incorrect access...or if data does not exist.. Specifics are as follows
     * if you request a user it returns user, if user does not exist then setting for management
     * company, if non then returns root setting...
     */
    public Object read(int AccessLevel) {
        int position = -1;
        if (AccessLevel == GLOBAL || AccessLevel == CLIENT) {
            for (int i = 0; i < dataICanReadFrom.size(); i++) {
                if (dataICanReadFrom.get(i).getManagementCo() == managementCompany) {
                    position = i;
                } else if (dataICanReadFrom.get(i).getManagementCo() == 0 && position == -1) {
                    position = i;
                }
            }
        } else {
            for (int i = 0; i < dataICanReadFrom.size(); i++) {
                if (dataICanReadFrom.get(i).getUser() == userId) {
                    position = i;
                } else if (dataICanReadFrom.get(i).getManagementCo() == managementCompany && position == -1) {
                    position = i;
                } else if (dataICanReadFrom.get(i).getManagementCo() == 0 && position == -1) {
                    position = i;
                }
            }
        }
        if (position > -1) {
            return convertFromDataToObject(dataICanReadFrom.get(position).val);
        }
        return null;
    }

    /**
     * Requests to write this option for appropriate Access...or User...
     * Looks for existing option or if has privilages creates new one...
     */
    public void writeOption(Object write, int AccessLevel) {
        GeneralQueryFormat myQuery = null;
        try {
            myQuery = dataICanWriteTo.get(0).generateSQL(convertFromObjectToData(write), AccessLevel);
        } catch (Exception e) {
            myQuery = (new individualOptionData(convertFromObjectToData(write), managementCompany + "", "")).generateSQL(convertFromObjectToData(write), AccessLevel);
        }
        try {
            Connection myConn = new Connection();
            myConn.executeQuery(myQuery);
        } catch (Exception e) {
        }
    }

    /**
     * Ok this method should be overridden to define how to take data from the database
     * and convert it to necessary Object which can be a color, boolean or whatever the
     * hell you want...
     */
    public abstract Object convertFromDataToObject(String data);

    /**
     * This method provides the inverse of the other operation namely how to take a color or
     * whatever and convert it too data...fun fun...
     */
    public abstract String convertFromObjectToData(Object object);

    public String toString() {
        return "Option Name: " + myName + " Your Id: " + userId + " managementCompany: " + managementCompany;
    }

    @Override
    public int compareTo(IndividualOption o) {

        try {
            if (getMyCategory().equals(o.getMyCategory())) {
                return getMyName().compareTo(o.getMyName());
            } else {
                return (getMyCategory().compareTo(o.getMyCategory()));
            }
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * Actual indivual options used to determine what level the user can read/write to for each option...
     */
    private class individualOptionData {

        private static final int posOfManagement = 0;
        private static final int posOfClient = 1;
        private static final int posOfUser = 2;
        private ArrayList<Integer> myListOfManagements;
        private String val;
        private String id;

        public individualOptionData(String value, String managementId, String optionId) {
            StringTokenizer st = new StringTokenizer(managementId, ":");
            myListOfManagements = new ArrayList(st.countTokens());
            while (st.hasMoreTokens()) {
                myListOfManagements.add(Integer.parseInt(st.nextToken()));
            }
            val = value;
            id = optionId;
        }

        public int getManagementCo() {
            if (myListOfManagements.size() >= 1) {
                return myListOfManagements.get(posOfManagement);
            }
            return -1;
        }

        public int getUser() {
            if (myListOfManagements.size() >= 3) {
                return myListOfManagements.get(posOfUser);
            }
            return -1;
        }

        /**
         * Can user read from this data?
         */
        public boolean canRead() {
            if (managementCompany == OptionsDataClass.ROOT_USER) {
                return true;
            } else {
                if (posOfUser > myListOfManagements.size()) {
                    return true;
                } else {
                    if (myListOfManagements.get(posOfUser) == userId) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }

        /**
         * Can user write to this data?
         */
        public boolean canWrite() {
            if (managementCompany == OptionsDataClass.ROOT_USER) {
                return true;
            } else {
                if (posOfUser > myListOfManagements.size()) {
                    return false;
                } else {
                    if (myListOfManagements.get(posOfUser) == userId) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }

        public GeneralQueryFormat generateSQL(String val, int access) {
            if ((access == GLOBAL && managementCompany == 0) && id.length() > 0) {
                update_existing_options_query myQuery = new update_existing_options_query();
                myQuery.update(val, id);
                return myQuery;
            } else {
                create_new_option_query myQuery = new create_new_option_query();
                myQuery.update(managementCompany + ":0:" + userId, val, id);
                return myQuery;
            }
        }
    }
}
