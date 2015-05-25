/*
 *  Filename:  employee_state_certification_query.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  05/12/2010
 *  Date Last Modified:  05/12/2010
 *  Last Modified By:  Jeffrey N. Davis
 *  Purpose of File:  File contains the public class
 *      employee_state_certification_query which is simply a query created
 *      to ask the server to return what, if any, states the user is certified
 *      in
*/

//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.employee;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/*
 *  Class Name: employee_state_certification_query
 *  Purpose of Class:  A class designed for a query that returns any states the
 *      user is certified in
 *  Extends:  GeneralQueryFormat
*/
public class employee_state_certification_query extends GeneralQueryFormat
{
    //  private variable declaration
    private StringBuffer query;
    private String employee_id; // J.Y. added on May 2010
    
    //  public methods implementation
    /*
     *  Method Name:  employee_state_certification_query
     *  Purpose of Method:  Constructor for the
     *      employee_state_certification_query
     *  Arguments:  none
     *  Return Value:  none
     *  Precondition:  object not created
     *  Postcondition:  object created, initial part of query set
    */
    public employee_state_certification_query()
    {
        //  create query as stringbuffer
        query = new StringBuffer();

        //  set initial portion of query so that emp_id can be added by user
        query.append(
                "SELECT state_name,emp_id " +
                "FROM emp_cert_states as m " +
                "INNER JOIN employee " +
                "ON emp_id = employee_id " +
                "INNER JOIN states as s " +
                "ON m.state_id=s.state_id " +
                "WHERE emp_id = ");

    }

     /*
     *  Method Name:  addEmpID
     *  Purpose of Method:  adds the employee_ID to the previously created
     *      query, finalizing it
     *  Arguments:  a string containing the employee_ID
     *  Return Value:  void
     *  Precondition:  query created, employee_ID unknown
     *  Postcondition:  employee_ID added to query, query finalized
    */
    public void addEmpID(String empID)
    {
        query.append("'");
        query.append(empID);
        query.append("';");
        employee_id = empID; // J.Y. added on May 2010
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
                return "SELECT * FROM f_employee_state_certification_query(" + employee_id + ");"; 
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