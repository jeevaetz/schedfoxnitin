/**
 *  Filename:  get_user_name_messaging_query.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  06/18/2010
 *  Date Last Modified:  06/18/2010
 *  Last Modified By:  Jeffrey N. Davis
 *  Purpose of File:  file contains a query class for retrieving the user name
 *      for insertion into a templated message
 *  FINAL FORMAT:  SELECT
 *      control_db.user.user_first_name AS first_name,
 *      control_db.user.user_last_name AS last_name
 *      FROM control_db.user
 *      WHERE control_db.user.user_id = <user_id>;
 */

//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.messaging;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;


/**
 *  Class Name:  get_user_name_messaging_query
 *  Purpose of Class:  a query class for retrieving the user name for
 *      insertion into a templated message
 */
public class get_user_name_messaging_query extends GeneralQueryFormat
{
    //  private variable declarations
    StringBuffer query;
    
    //  public method implementations
    /**
     *  Method Name:  get_user_name_messaging_query
     *  Purpose of method:  creates an instance of this class, builds query
     *  Arguments:  an int describing the userId
     *  Returns:  none
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, query built
     */
    public get_user_name_messaging_query(int userId)
    {
        //  initialize class variables
        query = new StringBuffer();
        
        //  construct query
        query.append("SELECT ");
        query.append("control_db.user.user_first_name AS first_name, ");
        query.append("control_db.user.user_last_name AS last_name ");
        query.append("FROM control_db.user ");
        query.append("WHERE control_db.user.user_id = ");
        
        //  add userId
        query.append(userId);
        
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