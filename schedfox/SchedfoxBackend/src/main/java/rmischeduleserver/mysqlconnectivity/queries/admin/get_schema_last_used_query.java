/**
 *  FileName:  get_schema_last_used_quer.java
 *  @author Jeffrey Davis
 *  Date Created:  11/16/2010
 *  Purpose of File:  file contains a simple query class for determining when
 *      the schema was last used
 */

//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.admin;

//  import decarlations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;


/**
 *  Class Name:  get_schema_last_used_query
 *  Purpose of Class:  a simple query class for determining when
 *      the schema was last used
 */
public class get_schema_last_used_query extends GeneralQueryFormat
{
    //  private variable declarations
    private StringBuffer query;

    //  public method implementations
    /** Create a default instance of this class */
    public get_schema_last_used_query() {}

    /**
     *  Method Name:  get_schema_last_used_query
     *  Purpose of Method:  creates an instance of this class, constructs query
     *  @param id - an int representing the company_management_id
     */
    public get_schema_last_used_query(int id)
    {
        query = new StringBuffer();

        //  construct query
        query.append("SELECT control_db.get_schedule_master_max_mod(");
        query.append(id);
        query.append(");");
    }

    /**
     *  Method Name:  toString
     *  Purpose of Method:  returns the query
     *  @return query - a string containing the query
     */
    @Override
    public String toString()   {return query.toString();}

    /**
     *  Method Name:  hasAccess
     *  Purpose of Method:  tells GQF if this class has access to DB
     *  @return true
     */
    @Override
    public boolean hasAccess()  {return true;}
};
