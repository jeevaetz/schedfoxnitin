/*
 *  Filename:  store_services_valid_query.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  05/17/2010
 *  Date Last Modified:  05/17/2010
 *  Last Modified By:  Jeffrey N. Davis
 *  Purpose of File:  File contains the public class
 *      store_services_valid_query which is simply a query created
 *      to ask the server to return what, if any, services that store has active
 *  FINAL FORMAT:
*/

//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.client;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/*
 *  Class Name: store_services_valid_query
 *  Purpose of Class:  A class designed for a query that returns any services
 *      the store has active
 *  Extends:  GeneralQueryFormat
*/
public class store_services_valid_query extends GeneralQueryFormat
{
    //  private variable declaration
    private StringBuffer query;

    //  public methods implementation
    /*
     *  Method Name:  store_services_valid_query
     *  Purpose of Method:  Constructor for the store_services_valid_query
     *  Arguments:  none
     *  Return Value:  none
     *  Precondition:  object not created
     *  Postcondition:  object created, initial part of query set
     *  FINAL FORMAT:  
    */
    public store_services_valid_query()
    {
        //  create query as stringbuffer
        query = new StringBuffer();

        //  set initial portion of query so that client_id can be added by user
        query.append(
                "SELECT * " +
                "FROM  store_services AS T " +
                "INNER JOIN store_services_valid AS T2 " +
                "ON T.store_services_id = T2.store_services_id " +
                "WHERE T2.client_id = ");
    }

     /*
     *  Method Name:  addClientID
     *  Purpose of Method:  adds the client_ID to the previously created
     *      query, finalizing it
     *  Arguments:  a string containing the client_ID
     *  Return Value:  void
     *  Precondition:  query created, client_ID unknown
     *  Postcondition:  client_ID added to query, query finalized
    */
    public void addClientID(String tempClientID)
    {
        query.append("'");
        query.append(tempClientID);
        query.append("';");
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