/*
 * employee_save_query.java
 *
 * Created on January 26, 2005, 12:38 PM
 */

/**
 *  Altered on 05/27/2010 by Jeffrey Davis
 *  Alterations:  revoed "pager" field, added alt email, alt phone, email
 *      messaging, sms messaging, fixed cell/phon2 problem
 */


package rmischeduleserver.mysqlconnectivity.queries.employee;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class employee_save_query extends GeneralQueryFormat {
    
    private String FName;
    private String LName;
    private String MName;
    private String Phone;
    private String Phone2;
    private String Cell;
    //  Jeffrey Davis additions on 05/27/2010 begun
    private String emailMessage;
    private boolean smsMessage;
    private String Email2;
    //  Jeffrey Davis additiosn on 05/27/2010 completed
    private String Address;
    private String Address2;
    private String City;
    private String State;
    private String Zip;
    private String SSN;
    private String EMail;
    private String HireDate;
    private String TermDate;
    private String EmpID;
    private String Type;
    private boolean isDeleted;
    private String BirthDate;
    private String BranchId;
    
    /** Creates a new instance of employee_save_query */
    public employee_save_query() {
        myReturnString = new String();
    }
    
    public void update(String fName, String lName, String mName, String phone, String phone2, String cell, 
                       String address, String address2, String city, String state, String zip, String ssn, String email,
                       String altEmail, boolean isSMSMessage, String emailMessageType, String hiredate, String termdate, String EmpId, String type, boolean deleted, String bdate,
                       String bid) {
          FName = fName.replaceAll("'", "''");
          LName = lName.replaceAll("'", "''");
          MName = mName.replaceAll("'", "''");
          Phone = phone.replaceAll("'", "''");
          Phone2 = phone2.replaceAll("'", "''");
          Cell = cell.replaceAll("'", "''");
          
          Address = address.replaceAll("'", "''");
          Address2 = address2.replaceAll("'", "''");
          City = city.replaceAll("'", "''");
          State = state.replaceAll("'", "''");
          Zip = zip.replaceAll("'", "''");
          SSN = ssn;
          EMail = email.replaceAll("'", "''");
          Email2 = altEmail.replaceAll("'", "''");
          emailMessage = emailMessageType.replaceAll("'", "''");
          this.smsMessage = isSMSMessage;

          HireDate = hiredate;
          TermDate = termdate;
          try {
              EmpID = Integer.parseInt(EmpId) + "";
          } catch (Exception e) {
              EmpID = "0";
          }
          Type = type;
          this.isDeleted = deleted;
          BirthDate = bdate;
          BranchId = bid;
    }
    
    public boolean hasAccess() {
        return true;
    }

    @Override
    public int getUpdateStatus() { 
        return GeneralQueryFormat.UPDATE_SCHEDULE;                 
    }

    @Override
    public String toString() {
       /* String del = "0";
        if (isDeleted) {
            del = "1";
        }   */
        if(BranchId.equals("-1")) {
            BranchId = getBranch();
        }
        
        StringBuffer sql = new StringBuffer();
        
        sql.append("SELECT f_employee_save_query_3(" + EmpID + "," + BranchId + ", '" + FName + "', '" + LName +
                "', '" + MName + "', '" + Phone + "', '" + Phone2 + "', '" + Cell + "', '" + "''" + "', '" +
                SSN + "', '" + Address + "', '" + Address2 + "', '" + City + "', '" + State + "', '" + Zip +
                "', '" + EMail + "','"  + HireDate  + "', '"  + TermDate + "'," + Type + "," +
                 isDeleted + ", '" + BirthDate + "','" + Email2 + "','" + emailMessage + "'," + this.smsMessage + ");");

        return sql.toString();
    }
    
}
