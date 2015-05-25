/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control.model;

import java.lang.Integer;
import java.lang.String;
import java.util.Hashtable;

/**
 *
 * @author jchinta
 */
public class MappingClass {
    //employee
    public static String E_FirstName = "E_FirstName";
    public static String E_LastName = "E_LastName";
    public static String E_Minitial = "E_Minitial";
    public static String E_SSN = "E_SSN";
    public static String E_Addr1 = "E_Addr1";
    public static String E_Addr2 = "E_Addr2";
    public static String E_City = "E_City";
    public static String E_State = "E_State";
    public static String E_Zip = "E_Zip";
    public static String E_Ph1 = "E_Ph1";
    public static String E_Ph2 = "E_Ph2";
    public static String E_Cell = "E_Cell";
    public static String E_Pager = "E_Pager";
    public static String E_Email = "E_Email";
    public static String E_HireDate = "E_HireDate";
    public static String E_TermDate = "E_TermDate";
    public static String E_isDeleted = "E_isDeleted";
    public static String E_LastUpdated = "E_LastUpdated";
    public static String E_EmpType = "E_EmpType";
    public static String E_WebLogin = "E_WebLogin";
    public static String E_WebPw = "E_WebPw";
    public static String E_BirthDate = "E_BirthDate";
    public static String E_TypeId = "E_TypeId";
    public static String E_AccruedVacation = "E_AccruedVacation";
    public static String E_Login = "E_Login";
    public static String E_isloginAv = "E_isloginAv";
    public static String E_Password = "E_Password";
    public static String E_Email2 = "E_Email2";
    public static String E_Messaging = "E_Messaging";
    public static String E_SmsMessaging = "E_SmsMessaging";
    public static String E_WorkHrsperweek = "E_WorkHrsperweek";


    public static String  USC_ClientID="USC_ClientID";
    public static String C_ClientName="C_ClientName";
    public static String C_ClientPhone="C_ClientPhone";
    public static String C_ClientPhone2="C_ClientPhone2";
    public static String C_Client_fax="C_Client_fax";
    public static String C_Client_address="C_Client_address";
    public static String C_Client_address2="C_Client_address2";
    public static String C_Client_city="C_Client_city";
    public static String C_Client_state="C_Client_state";
    public static String C_Client_zip="C_Client_zip";
    public static String C_Management_id="C_Management_id";
    public static String C_Client_start_date="C_Client_start_date";
    public static String C_Client_end_date="C_Client_end_date";
    public static String C_Client_is_deleted="C_Client_is_deleted";
    public static String C_Client_type="C_Client_type";
    public static String C_Client_worksite="C_Client_worksite";
    public static String C_Client_training_time="C_Client_training_time";
    public static String C_Client_bill_for_training="C_Client_bill_for_training";
    public static String C_Client_worksite_order="C_Client_worksite_order";
    public static String C_Rate_code_id="C_Rate_code_id";
    public static String C_Client_break="C_Client_break";
    public static String C_Store_volume_id="C_Store_volume_id";
    public static String C_Store_remote_market_id="C_Store_remote_market_id";





    public static String E_rowValues[] = {E_FirstName, E_LastName, E_Minitial, E_SSN, E_Addr1, E_Addr2, E_City, E_State, E_Zip, E_Ph1, E_Ph2, E_Cell, E_Pager, E_Email, E_HireDate,
        E_TermDate, E_isDeleted, E_LastUpdated, E_EmpType, E_WebLogin, E_WebPw, E_BirthDate, E_TypeId, E_AccruedVacation, E_Login, E_Password, E_isloginAv, E_Email2, E_Messaging, E_SmsMessaging,E_WorkHrsperweek};


    public static String C_rowValues[] = {USC_ClientID,C_ClientName,C_ClientPhone,C_ClientPhone2,C_Client_fax,C_Client_address,C_Client_address2,C_Client_city,C_Client_state,C_Client_zip,C_Management_id,C_Client_start_date,
   C_Client_end_date,C_Client_is_deleted,C_Client_type,C_Client_worksite,C_Client_training_time,C_Client_bill_for_training,C_Client_worksite_order,
   C_Rate_code_id,C_Client_break,C_Store_volume_id, C_Store_remote_market_id};




    public Hashtable<String, Integer> mappings;

    public MappingClass() {
        mappings = new Hashtable<String, Integer>();
    }

    public void E_parseFirstRow(Object... firstRow) {
        for (int c = 0; c < firstRow.length; c++) {
            for (int rv = 0; rv < E_rowValues.length; rv++) {
                if (firstRow[c].equals(E_rowValues[rv])) {
                    mappings.put(E_rowValues[rv], c);
                }
            }
        }
    }


       public void C_parseFirstRow(Object... firstRow) {
        for (int c = 0; c < firstRow.length; c++) {
            for (int rv = 0; rv < C_rowValues.length; rv++) {
                if (firstRow[c].equals(C_rowValues[rv])) {
                    mappings.put(C_rowValues[rv], c);
                }
            }
        }
    }

    



    public int getLookupValue(String valueToLookup) throws Exception {
        if (mappings.get(valueToLookup) == null) {
            //throw new Exception("Value is not currently mapped!");
            return 99;
        }
        return mappings.get(valueToLookup);
    }
}
