/**
 *  Filename:  client_certifications_list_initial_query.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  06/06/2010
 *  Date Last Modified:  06/06/2010
 *  Last Modified By:  Jeffrey N. Davis
 *  Purpose of File:  File contains a class extending GQF so that the initial
 *      list of client certifications may be loaded on a new form
 *  FINAL FORMAT:  SELECT certification_id as cert_id,
 *      certification_name as cert_name,
 *      certification_description as cert_desc
 *      FROM certifications;
 */

//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.client;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;


/**
 *  Class Name:  client_certifications_list_initial_query
 *  Purpose of Class:  a class extending GQF so that the initial list of
 *      client certifications may be loaded on a new form
 *  Extends:  GeneralQueryFormat
 */
public class client_certifications_list_initial_query extends GeneralQueryFormat
{
    //  private variable declarations
    StringBuffer query;

    //  public method implementations
    /**
     *  Method Name:  client_certifications_list_initial_query
     *  Purpose of Method:  the default constructor for this class
     *  Arguments:  none
     *  Returns:  none
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, query built and properly formatted
     */
    public client_certifications_list_initial_query()
    {
        //  initialize class variables
        query = new StringBuffer();

        //  create query
        query.append("SELECT ");
        query.append("certification_id as cert_id, ");
        query.append("certification_name as cert_name, ");
        query.append("certification_description as cert_desc ");
        query.append("FROM certifications");
    }

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

    //  abstract method implementations
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