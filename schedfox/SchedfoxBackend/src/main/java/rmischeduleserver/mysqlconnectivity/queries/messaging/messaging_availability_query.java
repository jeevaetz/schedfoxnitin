/**
 *  Filename:  messaging_availability_query.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  06/28/2010
 *  Date Last Modified:  06/28/2010
 *  Last Modified By:  Jeffrey N. Davis
 *  Purpose of File:  file contains a query class for retrieving the
 *      availability information regarding a shift
 *  FINAL FORMAT:  SELECT *
 *      FROM f_messaging_availability(<date>, <start_time>, <end_time>;
 */

//  package declarations
package rmischeduleserver.mysqlconnectivity.queries.messaging;

//  import declarations

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;


/**
 *  Class Name:  messaging_availability_query
 *  Purpose of Class:  a query class for retrieving the availability
 *      information regarding a shift
 */
public class messaging_availability_query extends GeneralQueryFormat
{
    //  private variable declarations
    StringBuffer query;

    //  private method implementations
    /**
     *  Method Name:  formatDate
     *  Purpose of Method:  formats the date from the DShift into the proper
     *      format for the SQL function call
     *  Arguments:  a String containing the unformatted date
     *  Returns:  a string containing the formatted date
     *  Preconditions:  date unformatted
     *  Postconditions:  date formatted, returned
     */
    private String formatDate(String unFormattedDate)
    {
        //  declare string to return
        StringBuffer formatString = new StringBuffer();
        StringBuffer returnString = new StringBuffer();
        formatString.append(unFormattedDate);
        formatString.trimToSize();

        //  format date
        returnString.append(formatString.substring(6,10));
        returnString.append("-");
        returnString.append(formatString.substring(0,2));
        returnString.append("-");
        returnString.append(formatString.substring(3,5));

        //  return properly formatted string
        returnString.trimToSize();
        return returnString.toString();
    }

    //  public method implementations
    /**
     *  Method Name:  messaging_availability_query
     *  Purpose of Method:  constructor for this class; creates a default
     *      instances and builds the initial portion of the query
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, initial portion of query built
     */
    public messaging_availability_query()
    {
        //  initialize class variables
        query = new StringBuffer();

        //  build initial portion of query
        query.append("SELECT * ");
        query.append("FROM f_messaging_availability(DATE('");
    }

    /**
     *  Method Name:  addQueryParameters
     *  Purpose of Method:  adds the paramaters to the query, finalizes the
     *      query
     *  Arguments:  a string containing the UNFORMATTED DATE, an int
     *      describing the start time, an int describing the end time
     *  Returns:  void
     *  Preconditions:  initial portion of query built, not finalized
     *  Postconditions:  date formatted, query paramters added, query finalized
     */
    public void addQueryParamters(String date, int startTime, int endTime)
    {
        //  finalize query by adding parameters
        query.append(formatDate(date));
        query.append("'), ");
        query.append(startTime);
        query.append(", ");
        query.append(endTime);
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