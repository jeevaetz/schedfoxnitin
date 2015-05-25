/**
 *  Filename:  get_messaging_company_branch_query.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  06/17/2010
 *  Date Last Modified:  06/17/2010
 *  Last Modified by:  Jeffrey N. Davis
 *  Purpose of File:  file contains a query class for retrieving company and
 *      branch information
 *  FINAL FORMAT:
 */

//  package declarations
package rmischeduleserver.mysqlconnectivity.queries.messaging;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;


/**
 *  Class Name:  a query class for retrieving company and branch information
 */
public class get_messaging_company_branch_query extends GeneralQueryFormat
{
    //  private variable declarations
    StringBuffer query;

    //  public method implementations
    /**
     *  Method Name:  get_messaging_company_branch_query
     *  Purpose of Method:  default constructor for this class
     *  Arguments:  none
     *  Returns:  none
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, initial variables set, inital portion
     *      of query constructed
     */
    public get_messaging_company_branch_query()
    {
        //  initialize variables
        query = new StringBuffer();

        //  construct initial portion of query
        query.append("(SELECT company_name, branch_name ");
        query.append("FROM control_db.company ");
        query.append("INNER JOIN ");
        query.append("control_db.branch ON ");
    }

    /**
     *  Method Name:  addCompanyBranchId
     *  Purpose of Method:  adds the CompanyBranchId to the query, finalizes
     *      the query
     *  Arguments:  two strings containing the company/branch ids
     *  Returns:  void
     *  Preconditions:  query initially constructed, ids not added
     *  Postconditions:  ids added, query finalized
     */
    public void addCompanyBranchId(String companyId, String branchId)
    {
        //  add branch id to query
        query.append("branch_id = ");
        query.append(branchId);
                
        //  add company id to query
        query.append("WHERE company_id = ");
        query.append(companyId);

        //  finalize query.
        query.append(")");
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