/*
 *  Filename:  employee_state_certification_save_query.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  05/12/2010
 *  Date Last Modified:  05/12/2010
 *  Last Modified By:  Jeffrey N. Davis
 *  Purpose of File:  File contains the public class
 *      employee_state_certification_save_query which is simply a query created
 *      to save any states that the user is certified in to the emp_cert_states
 *      table
 *  Final Fomat:  Once the user has finished adding states to save, the
 *      final format of the SQL statement is as follows:  DELETE FROM
 *      emp_cert_states WHERE emp_id = <employee_ID>;
 *      <the following will appear as many times as there are states to save>
 *       INSERT INTO emp_cer_states VALUES ('<employee_ID>', '<state_ID>');
*/

//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.employee;

//  import declarations
import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/*
 *  Class Name: employee_state_certification_save_query
 *  Purpose of Class:  A class designed for a query that saves the states
 *      the user is certified in to the emp_cert_states table
 *  Extends:  GeneralQueryFormat
*/
public class employee_state_certification_save_query extends GeneralQueryFormat
{

    private String employeeID;
    private ArrayList<Integer> stateId;
    //  public method implementations
    /*
     *  Method Name:  employee_state_certification_save_query
     *  Purpose of Method:  Constructor for the
     *      employee_state_certification_save_query
     *  Arguments:  a string containing the employee ID
     *  Return Value:  none
     *  Precondition:  object not created
     *  Postcondition:  object created, initial part of query set, employeeID
     *      assigned
     * FINAL FORMAT:  After this method is called, a the following will be
     *      added to the query:  DELETE FROM emp_cert_states WHERE emp_id =
     *      <employee_ID>;
    */
    public employee_state_certification_save_query(String empID, ArrayList<Integer> stateId)
    {
        this.stateId = stateId;
        this.employeeID = empID;
    }

    

    /*
     *  Method Name:  toString
     *  Purpose of Method:  returns the query as a string
     *  Arguments:  none
     *  Return Value:  returns the query as a string
     *  Precondition:  query known internally, not by object user
     *  Postcondition:  query returned as a string
     *  NOTE:  This is an overriden abstract method from GeneralQueryFormat
    */
    @Override
    public String toString()
    {
        String stateIdsStr = "'{";
        for (int e = 0; e < stateId.size(); e++) {
            if (e > 0) {
                stateIdsStr = stateIdsStr + ",";
            }
            stateIdsStr = stateIdsStr + stateId.get(e);
        }
        stateIdsStr = stateIdsStr + "}'::integer[]";
        return "SELECT * FROM f_employee_state_certification_save_query(" + employeeID + "," + stateIdsStr +  "," + this.stateId.size() + ");";
    }

     /*
     *  Method Name:  hasAccess
     *  Purpose of Method:  a method to check whether or not the object has
     *      access
     *  Arguments:  none
     *  Return value:  returns a boolean value describing whether this object
     *      has access
     *  Precondition:  access unknown by user
     *  Postcondition:  access known by user
     *  NOTE:  This method overrides the abstract hasAccess method in
     *      GeneralQueryFormat
    */
    @Override
    public boolean hasAccess()
    {
        return true;
    }
};