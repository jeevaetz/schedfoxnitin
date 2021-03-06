/*
 *  Filename:  store_services_nonvalid_query.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  05/17/2010
 *  Date Last Modified:  05/17/2010
 *  Last Modified By:  Jeffrey N. Davis
 *  Purpose of File:  File contains the public class
 *      store_services_nonvalid_query which is simply a query created
 *      to ask the server to return all information from store_services
 *  FINAL FORMAT:   The final format for the query is:  SELECT *
 *      store_services;
*/

//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.client;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/*
 *  Class Name: store_services_nonvalid_query
 *  Purpose of Class:  A class designed for a query that returns all data from
 *      store_services table
 *  Extends:  GeneralQueryFormat
*/
public class store_services_nonvalid_query extends GeneralQueryFormat
{
    //  private variable declarations
    private StringBuffer query;

    //  public methods implementation
    /*
     *  Method Name:  store_services_nonvalid_query
     *  Purpose of Method:  Constructor for the
     *      employee_state_noncertification_query
     *  Arguments:  none
     *  Return Value:  none
     *  Precondition:  object not created
     *  Postcondition:  object created, query set
     *  FINAL FORMAT:  The final format for the query is:  SELECT * 
     *      FROM store_services;
    */
    public store_services_nonvalid_query()
    {
        //  create query as new StringBuffer
        query = new StringBuffer();

        //  set query
        query.append("SELECT * FROM store_services;");
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
        return query.toString();
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