/**
 *  Filename:  sms_template_message_query.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  06/17/2010
 *  Date Last Modified:  06/17/2010
 *  Last Modified by:  Jeffrey N. Davis
 *  Purpose of File:  file contains a query class for retrieving templated
 *      messages from the DB; SMS specific
 *  FINAL FORMAT:
 */

//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.messaging;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *  Class Name:  sms_template_message_query
 *  Purpose of Class:  a query class for retrieving templated  messages from
 *      the DB; SMS specific
 */
public class sms_template_message_query extends GeneralQueryFormat
{
    //  private variable declarations
    StringBuffer query;

    //  public method implementations
    /**
     *  Method Name:  sms_template_message_query
     *  Purpose of Method:  default constructor for this class
     *  Arguments:  none
     *  Returns:  none
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, initial variables set, inital portion
     *      of query constructed
     */
    public sms_template_message_query()
    {
        //  initialize class variables
        query = new StringBuffer();

        //  construct initial portion of query
        query.append("SELECT * ");
        query.append("FROM message_sms_templates ");
        query.append("WHERE message_sms_template_id = ");
     }

    /**
     *  Method Name:  addTemplateId
     *  Purpose of Method:  adds the template_id onto the query, finalizes the
     *      query
     *  Arguments:  an int describing the template_id
     *  Returns:  void
     *  Preconditions:  initial portion of query constructed, not finalized
     *  Postconditions:  query finalized, template_id set
     */
    public void addTemplateId(int tempTemplateId)
    {
        //  add template_id to query
        this.query.append(tempTemplateId);

        //  finalize query
        this.query.append(";");
    }

    //  abstract method implementation
    /**
     *  Method Name:  toString
     *  Purpose of Method:  returns the query in a string format
     *  Arguments:  none
     *  Returns:  a string containing the current query
     *  Precondition:  query has at least initial format, desired by another
     *      piece of code
     *  Postcondition:  query has been returned
     *  Overrides:  toString from GeneralQueryFormat
     */
    @Override
    public String toString()
    {
        //  return query
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
     *  Overrides:  hasAccess from GeneralQueryFormat
    */
    @Override
    public boolean hasAccess()
    {
        return true;
    }
};