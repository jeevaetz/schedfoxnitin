/**
 *  Filename:  save_users_to_alert_query.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  08/03/2010
 *  Purpose of File:  File contains a query class extending GFQ that saves a
 *      row to the new_user_alert table
 *  Modifications:
 *  FINAL FORMAT:
 *      DELETE
 *      FROM new_user_alert
 *      WHERE new_user_alert.user_ssn = <user_ssn>;
 *      INSERT INTO new_user_alert
 *      VALUES
 *      (<user_ssn>, <user_first_name>, <user_last_name>, <user_primary_email>,
 *          <user_alternate_email>, <user_text_number>, <user_send_text>,
 *          <user_use_alternate_email>, <user_use_both_email>);
 */

//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.new_user_alert;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *  Class Name:  save_users_to_alert_query
 *  Purpose of Class: a query class extending GFQ that saves a
 *      row to the new_user_alert table
 */
public class save_users_to_alert_query extends GeneralQueryFormat
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
    public save_users_to_alert_query()
    {
        //  initialize class variables
        query = new StringBuffer();

        //  set first part of query
        query.append("DELETE ");
        query.append("FROM new_user_alert ");
        query.append("WHERE new_user_alert.user_ssn = ");
    }

    /**
     *  Method Name:  updateQuery
     *  Purpose of Method:  takes numerous arguments, and sets them in the
     *      proper place within the query, then finalizes it
     *  Arguments:  an int for ssn, a string for firstName, a string for
     *      lastName, a string for company, a string for primaryEmail, a string for alternateEmail,
     *      a string for textNumber, a boolean for sendText, a boolean for
     *      useAlternateEmail, a boolean for useBothEmail
     *  Returns:  void
     *  Preconditions:  query initializes, final pieces not added
     *  Postconditions:  query finalized, ready to be executed
     */
    public void updateQuery(int ssn, String firstName, String lastName, String company,
        String primaryEmail, String alternateEmail, String textNumber,
        boolean isSendText, boolean isUseAlternateEmail, boolean isUseBothEmail)
    {
        //  construct remaining query
        query.append(ssn);
        query.append("; ");
        query.append("INSERT INTO new_user_alert ");
        query.append("VALUES (");
        query.append(ssn);
        query.append(", '");
        query.append(firstName);
        query.append("', '");
        query.append(lastName);
        query.append("', '");
        query.append(company);
        query.append("', '");
        query.append(primaryEmail);
        query.append("', '");
        query.append(alternateEmail);
        query.append("', '");
        query.append(textNumber);
        query.append("', ");

        //  determine booleans, append accordingly
        if(isSendText)
            query.append("true, ");
        else
            query.append("false, ");
        if(isUseAlternateEmail)
            query.append("true, ");
        else
            query.append("false, ");
        if(isUseBothEmail)
            query.append("true");
        else
            query.append("false");

        //  finalize query
        query.append(");");
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