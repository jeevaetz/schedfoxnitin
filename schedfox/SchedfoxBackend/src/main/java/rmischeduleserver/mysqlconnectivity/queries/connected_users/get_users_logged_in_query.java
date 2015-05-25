/**
 *  Filename:  get_users_logged_in_query.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  06/11/2010
 *  Date Last Modified:  06/11/2010
 *  Last Modified By:  Jeffrey N. Davis
 *  Purpose of File:  file contains a class for a query to hit the DB, and
 *      return any users currently logged on to SchedFox
 *  FINAL FORMAT:
 */

//  package declarations
package rmischeduleserver.mysqlconnectivity.queries.connected_users;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;


/**
 *  Class Name:  get_users_logged_in_query
 *  Purpose of Class:  class for a query to hit the DB, and return any users
 *      currently logged on to SchedFox
 *  Extends:  GeneralQueryFormat
 */
public class get_users_logged_in_query extends GeneralQueryFormat
{
    //  private variable declarations
    private StringBuffer query;

    //  public method implementations
    /**
     *  Method Name:  get_users_logged_in_query
     *  Purpose of Method:  creates an instance of this class
     *  Arguments:  none
     *  Returns:  none
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, query constructed
     */
    public get_users_logged_in_query()
    {
        //  initialize query
        query = new StringBuffer();

        //  construct query
        query.append("SELECT DISTINCT ");
        query.append("time_date_stamp as time, ");
        query.append("user_first_name as firstname, ");
        query.append("user_middle_initial as middleinitial, ");
        query.append("user_last_name as lastname, ");
        query.append("(SELECT * FROM control_db.get_company_info_for_user(control_db.user_branch_company.user_id)) as company, ");
        query.append("branch_name as branch ");
        query.append("FROM users_connected ");
        query.append("INNER JOIN control_db.user ");
        query.append("ON users_connected.user_id = control_db.user.user_id ");
        query.append("INNER JOIN control_db.user_branch_company ");
        query.append("ON control_db.user.user_id = " +
            "control_db.user_branch_company.user_id ");
        query.append("FULL JOIN control_db.company ");
        query.append("ON control_db.user_branch_company.user_id = " +
            "control_db.company.company_id ");
        query.append("FULL JOIN control_db.branch ");
        query.append("ON control_db.user_branch_company.user_id = " +
            "control_db.branch.branch_id ");
        query.append("WHERE CURRENT_TIMESTAMP AT TIME ZONE 'CST' - " +
            "INTERVAL '5' MINUTE  < time_date_stamp;");
    }

    //  abstract method implementations
    /**
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
        return query.toString();
    }

    /**
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