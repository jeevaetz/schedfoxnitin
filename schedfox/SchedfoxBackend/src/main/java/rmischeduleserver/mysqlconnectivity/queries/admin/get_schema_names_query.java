/**
 *  FileName:  get_schema_names_query.java
 *  @author  Jeffrey N. Davis
 *  Date Created:  11/15/2010
 *  Purpose of File:  file contains a simple query class for retrieving schema names
 */

//  package declarations
package rmischeduleserver.mysqlconnectivity.queries.admin;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;


/**
 *  Class Name:  get_schema_names_query
 *  Purpose of Class:  a simple query class for retrieving schema names
 */
public class get_schema_names_query extends GeneralQueryFormat
{
    //  private variable declarations
    private static final String query = "SELECT nspname AS \"name\" FROM pg_namespace;";

    //  public method implementations
    /** Create an instance of this class */
    public get_schema_names_query()  {}

    /**
     *  Method Name:  toString
     *  Purpose of Method:  returns the query
     *  @return query - a string containing the query
     */
    @Override
    public String toString()   {return query;}

    /**
     *  Method Name:  hasAccess
     *  Purpose of Method:  tells GQF if this class has access to DB
     *  @return true
     */
    @Override
    public boolean hasAccess()
    {
        return true;
    }
};
