/**
 *  Filename:  delete_user_to_alert_query.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  08/04/2010
 *  Purpose of File:  File contains a query class extending GFQ deletes
 *      a user from the table
 *  Modifications:
 *  FINAL FORMAT:
 *      DELETE
 *      FROM new_user_alert
 *      WHERE new_user_alert.user_ssn = <user_ssn>;
 */

//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.new_user_alert;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *  Class Name:  get_users_to_alert_query
 *  Purpose of Class: a query class extending GFQ deletes
 *      a user from the table
 */
public class delete_user_to_alert_query extends GeneralQueryFormat
{
    //  private variables declarations
    private StringBuffer query;

    //  public method implementations
    /**
     *  Method Name:  get_users_to_alert_query
     *  Purpose of Method:  creates a default instance of this class
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, variables initialized
     */
    public delete_user_to_alert_query()
    {
        //  initialize class variables
        query = new StringBuffer();

        //  set query
        query.append("DELETE ");
        query.append("FROM new_user_alert ");
        query.append("WHERE new_user_alert.user_ssn = ");
    }

    /**
     *  Method Name:  updateQuery
     *  Purpose of Method:  adds on the user ssn, then finalizes the query
     *  Arguments:  an int describing the user_ssn
     *  Returns:  void
     *  Preconditions:  query initialized, user_ssn not attached
     *  Postconditions:  user_ssn attached, query finalized
     */
    public void updateQuery(int ssn)
    {
        //  append user_ssn
        query.append(ssn);

        //  finalize query
        query.append(";");
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

    /*(
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