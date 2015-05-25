/*
 * security_detail.java
 *
 * Created on May 12, 2005, 7:27 AM
 */

package rmischedule.security;
import rmischedule.data_connection.Connection;

import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.security.*;
import java.util.*;
import rmischedule.main.CompanyBranding;
import rmischedule.main.Main_Window;
import rmischeduleserver.mysqlconnectivity.queries.employee.security.get_security_settings_for_emp_query;
/**
 *  All this file is really for is to hold the definitions of our security
 *  Protocol. mostly int -> STATIC var
 *
 * @author jason.allen
 */
public class security_detail {

    private Hashtable<String, Boolean> userSecurity;
    private static Hashtable<Integer, Boolean> employeeSecurity = new Hashtable<Integer, Boolean>();;
    private String uid;

    /*
     * Base Op
     */
    public static enum ACCESS {
        READ    (1,     "Read"),
        ADD     (2,     "Add"),
        MODIFY  (4,     "Modify"),
        DELETE  (8,     "Delete"),
        ALL     (15,    "All");

        private int val;
        private String display;

        ACCESS(int value, String disp) {
            val = value;
            display = disp;
        }

        public int getVal() {
            return val;
        }

        public String getDisp() {
            return display;
        }
    }

    /*
     *  Admin
     */
    public static enum MODULES {
        ADMIN_USER      (11000, "Admin User"),
        ADMIN_GROUP     (12000, "Admin Group"),
        CLIENT_INFORMATION      (21100, "Client Edit"),
        CLIENT_EXPORT           (21500, "Client Export"),
        EMPLOYEE_EDIT           (31000, "Employee Edit"),
        EMPLOYEE_EXPORT         (31700, "Employee Export"),
        SCHEDULING_EDIT         (41000, "DDDSchedule"),
        SCHEDULING_SHIFT_EDIT   (41100, "Edit Shifts"),
        SCHEDULING_TIME_RECON   (41200, "Reconcile Shifts"),
        PAYROLL                 (5000, "Payroll"),
        RATE_CODE               (5100, "Rate Code"),
        BILLING                 (6000, "Billing"),

        //  added by Jeffrey Davis for messaging
        MESSAGING               (31000, "Employee Edit");

        private int val;
        private String disp;

        MODULES(int values, String display) {
            val = values;
            disp = display;
        }

        public int getVal() {
            return val;
        }

        public String getDisp() {
            return disp;
        }
    }

    public static enum EMPLOYEE_SEC {
        VIEW_OTHER_EMPS (11000, "Allow View Other Employees For Stores"),
        ALLOW_CHANGE_USER_LOGIN (11500, "Allow Employee to Change User Logins"),
        MOVE_SHIFTS_FROM_OTHER_EMPS (12000, "Allow Move Shifts from Other Employees"),
        ALLOW_TO_NOTE (13000, "Allow In Employee Notes Section"),
        ALLOW_TO_ADJUST_CERTS (14000, "Allow To Adjust Certifications"),
        ALLOW_TO_ADJUST_STATE_LICENSING (15000, "Allow adjustment of State Certs"),
        ALLOW_TO_ADJUST_STORE_LOC (16000, "Allow adjustment of Store Locations"),
        ALLOW_TO_VIEW_EMP_INFO (17000, "Allow user to view others contact info");
        
        private int val;
        private String disp;

        EMPLOYEE_SEC(int values, String display) {
            val = values;
            disp = display;
        }

        public int getVal() {
            return val;
        }

        public String getDisp() {
            return disp;
        }
    }

    /**
     * For a given string straight from the database it will return what module you are working on
     * ie: "11004" = ADMIN_USER, null is returned if we can't find a module...
     */
    public static MODULES getModule(String input) {
        int myVal = (Integer.parseInt(input)) - (Integer.parseInt(input) % 100);
        for (MODULES m : MODULES.values()) {
            if (m.getVal() == myVal) {
                return m;
            }
        }
        return null;
    }

    /**
     * Returns Module by the display name...
     */
    public static MODULES getModuleByName(String name) {
        for (MODULES m : MODULES.values()) {
            if (m.getDisp().equals(name)) {
                return m;
            }
        }
        return null;
    }

    /**
     * Breaks down data from database, works as follows if data passed in is
     * 11007, first time returns {true, true, true, false, false};
     */
    public static boolean[] getNextAccessLevel(String name) {
        boolean[] myVal = new boolean[ACCESS.values().length];
        for (int i = ACCESS.values().length - 1; i >= 0; i--) {
            myVal[i] = false;
        }
        return getNextAccessLevel(name, myVal);
    }

    public static boolean[] getNextAccessLevel(String name, boolean[] myBools) {
        int val = (Integer.parseInt(name) % 100);
        for (int i = ACCESS.values().length - 1; i >= 0; i--) {
            ACCESS a = ACCESS.values()[i];
            if (a.getVal() <= val) {
                myBools[i] = true;
                if (a.getVal() != ACCESS.ALL.getVal()) {
                    val -= a.getVal();
                }
            }
        }
        return myBools;
    }

    /**
     * Breaks down data from database, only returns ACCESS that you have access to no others!
     */
    public static Vector<ACCESS> getVectorOfAccess(String name) {
        int val = (Integer.parseInt(name) % 100);
        Vector<ACCESS> myVector = new Vector();
        for (int i = ACCESS.values().length - 1; i >= 0; i--) {
            ACCESS a = ACCESS.values()[i];
            if (a.getVal() <= val) {
                myVector.add(a);
                if (a.getVal() != ACCESS.ALL.getVal()) {
                    val -= a.getVal();
                }
            }
        }
        return myVector;
    }

    /**
     * Returns Access by the display name...
     */
    public static ACCESS getAccessByName(String name) {
        for (ACCESS a : ACCESS.values()) {
            if (a.getDisp().equals(name)) {
                return a;
            }
        }
        return null;
    }

    /**
     * Completely rebuilds the hash given the user id and the group stuff... rules are really
     * like this....if they have access anywhere be it in user or groups they have access, you can
     * not deny access in one spot and have it overwrite access in another...
     */
    public void GenerateSecurityByUserId() {
        security_level_for_user_and_groups_query myQuery = new security_level_for_user_and_groups_query();
        userSecurity = new Hashtable(ACCESS.values().length * MODULES.values().length);
        for (int i = 0; i < MODULES.values().length; i++) {
            for (int x = 0; x < ACCESS.values().length; x++) {
                userSecurity.put(MODULES.values()[i].getDisp() + " " + ACCESS.values()[x].getDisp(), false);
            }
        }
        myQuery.update(uid);
        Record_Set completeSec = new Record_Set();
        try {
            completeSec = (new Connection()).executeQuery(myQuery);
        } catch (Exception e) {}
        for (int i = 0; i < completeSec.length(); i++) {
            boolean[] myAccess;
            Vector<ACCESS> myActiveAccess;
            MODULES myMod;
            if (completeSec.getString("access_id") != null) {
                myMod =    getModule(completeSec.getString("access_id"));
                myActiveAccess = getVectorOfAccess(completeSec.getString("access_id"));
                for (int x = 0; x < myActiveAccess.size(); x++) {
                    try {
                        String myVal = myMod.getDisp() + " " + myActiveAccess.get(x).getDisp();
                        userSecurity.remove(myVal);
                        userSecurity.put(myVal, true);
                    } catch (Exception e) {}
                }
            }
            completeSec.moveNext();
        }
    }

    /**
     * Check employee access
     * @param valToCheck
     * @return
     */
    public static boolean doesEmployeeHaveAccess(Integer valToCheck) {
        boolean retVal = false;
        try {
            if (employeeSecurity.get(valToCheck)) {
                retVal = employeeSecurity.get(valToCheck);
            }
        } catch (Exception e) {
        
        }
        return retVal;
    }

    public void GenerateSecurityForEmployee(String coporation_db) {
        get_security_settings_for_emp_query empQuery = new get_security_settings_for_emp_query();
        empQuery.update(uid, coporation_db);
        Record_Set completeSec = new Record_Set();
        try {
            completeSec = (new Connection()).executeQuery(empQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
        employeeSecurity = new Hashtable<Integer, Boolean>();
        for (int i = 0; i < completeSec.length(); i++) {
            int myMod;
            if (completeSec.getString("security_setting") != null) {
                myMod = completeSec.getInt("security_setting");
                for (int x = 0; x < EMPLOYEE_SEC.values().length; x++) {
                    try {
                        if (myMod % 2 == 1) {
                            employeeSecurity.put(myMod - 1, true);
                        }
                    } catch (Exception e) {}
                }
            }
            completeSec.moveNext();
        }
    }

    /**
     * Pass in Module and Access returns if you have privaleges or not!
     */
    public boolean checkSecurity(MODULES m, ACCESS a) {
        boolean retVal = false;
        try {
            retVal = userSecurity.get(m.getDisp() + " " + a.getDisp());
        } catch (Exception e) {}
        return retVal;
    }

    /**
     * Checks to see if user has any access to MODULE, read, add , whatever
     * only returns false if has none...
     */
    public boolean checkSecurity(MODULES m) {
        boolean hasAccess = false;
        try {
            for (int i = 0; i < ACCESS.values().length; i++) {
                hasAccess = hasAccess || userSecurity.get(m.getDisp() + " " + ACCESS.values()[i].getDisp());
            }
        } catch (Exception e) {}
        return hasAccess;
    }

    /** Creates a new instance of security_detail */
    public security_detail(String userId, String schema) {
        uid = userId;

        try {
        if (!Main_Window.compBranding.getLoginType().equals(CompanyBranding.LoginType.USER)) {
            GenerateSecurityForEmployee(schema);
        } else {            
            GenerateSecurityByUserId();
        }
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }



}
