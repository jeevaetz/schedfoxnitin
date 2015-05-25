/**
 *  Filename:  current_logged_in_query.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  06/09/2010
 *  Date Last Modified:  06/09/2010
 *  Last Modified By:  Jeffrey N. Davis
 *  Purpose of File:  File contains a query class extending GFQ that writes
 *      to control_db telling it that the current user is logged in
 *  FINAL FORMAT:  SELECT update_user_connected(<user_id>);
 */

//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.connected_users;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;


/**
 *  Class Name:  a query class extending GFQ that writes to control_db
 *      telling it that the current user is logged in
 */
public class current_logged_in_query extends GeneralQueryFormat
{
    //  private variables declarations
    private StringBuffer query;
    private int userId;
    
    //  public method implementations
    /**
     *  Method Name:  current_logged_in_query
     *  Purpose of Method:  creates a default instance of this class
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, variables initialized
     */
    public current_logged_in_query()
    {
        //  initialize class variables
        query = new StringBuffer();
        userId = 0 ;
    }

    /**
     *  Method Name:  current_logged_in_query
     *  Purpose of Method:  creates an instance of this class setting variables
     *      to arguments
     *  Arguments:  an int describing the userId
     *  Returns:  none
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, variables set
     */
    public current_logged_in_query(int tempUserId)
    {
        //  initialize private variables
        query = new StringBuffer();
        this.userId = tempUserId;

        //  construct query
        query.append("SELECT update_user_connected(");
        query.append(userId);
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