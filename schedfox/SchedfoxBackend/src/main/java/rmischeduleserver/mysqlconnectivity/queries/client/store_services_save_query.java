/*
 *  Filename:  store_services_save_query.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  05/17/2010
 *  Date Last Modified:  05/17/2010
 *  Last Modified By:  Jeffrey N. Davis
 *  Purpose of File:  File contains the public class
 *      store_services_save_query which is a query created to save any 
 *      store services the user has selected to store_services_valid table
 *  Final Fomat:  Once the user has finished adding states to save, the
 *      final format of the SQL statement is as follows:  DELETE FROM
 *      store_services_valid WHERE client_id = <client_ID>;
 *      <the following will appear as many times as there are states to save>
 *       INSERT INTO store_services_valid VALUES ('<client_ID>',
 *      '<store_services_id>');
*/

//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.client;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/*
 *  Class Name: store_services_save_query
 *  Purpose of Class:  A class designed for a query that saves the store
 *      services to the store_services_valid
 *  Extends:  GeneralQueryFormat
*/
public class store_services_save_query extends GeneralQueryFormat
{
    //  private variable declarations
    private StringBuffer query;
    private String clientID;

    //  public method implementations
    /*
     *  Method Name:  store_services_save_query
     *  Purpose of Method:  Constructor for the
     *      store_services_save_query class
     *  Arguments:  a string containing the client ID
     *  Return Value:  none
     *  Precondition:  object not created
     *  Postcondition:  object created, initial part of query set, clientID
     *      assigned
     * FINAL FORMAT:  After this method is called, a the following will be
     *      added to the query:  DELETE FROM store_services_valid WHERE
     *      client_ID = <client_ID>;
    */
    public store_services_save_query(String tempClientID)
    {
        //  create query as stringbuffer, set clientID
        query = new StringBuffer();
        clientID = tempClientID;

        //  set initial portion of query so states (to be saved) can be added
        //      later
        query.append("DELETE FROM store_services_valid WHERE client_id = ");
        query.append(clientID);
        query.append("; ");
    }

    /*
     *  Method Name:  addStoreServiceToSave
     *  Purpose of Method:  takes the store service ID associated with a
     *      particular store service and builds each individual "line"
     *      of the INSERT portion of the query
     *  Arguments:  an int representing the store service ID
     *  Return Value:  void
     *  Precondition:  store service ID known, next line of query not added
     *  Postcondition:  next line of query created
     *  FINAL FORMAT:  After this method is called, a the following will be
     *      added to the query:  INSERT INTO store_services_valid VALUES ('
     *      <client_ID>', '<store_services_ID>');
    */
    public void addStoreServiceToSave(int storeServiceID)
    {
        //  add first part of SQL statement to query
        query.append(" INSERT INTO store_services_valid VALUES ('");

        //  add employee ID as first of two values to be saved
        query.append(clientID);

        //  add more formatting to query
        query.append("', '");

        //  add state ID to query
        query.append(storeServiceID);

        //  add final formatting for line
        query.append("'); ");
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