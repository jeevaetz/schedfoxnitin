/**
 *  FileName:  get_schema_last_used_quer.java
 *  @author Jeffrey Davis
 *  Date Created:  11/18/2010
 *  Purpose of File:  file contains a simple query class for determining the
 *      company_management_id given a specific schema name
 */

//  package declarations
package rmischeduleserver.mysqlconnectivity.queries.admin;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;


/**
 *  Class Name:  get_company_management_id_schemaOPS_query
 *  Purpose of Class:  a simple query class for determining the
 *      company_management_id given a specific schema name
 */
public class get_company_management_id_schemaOPS_query extends GeneralQueryFormat
{
    //  private variable declarations
    private StringBuffer query;

    //  public method implementations
    /** Create a default instance of this class */
    public get_company_management_id_schemaOPS_query() {}

    /**
     *  Method Name:  get_company_management_id_schemaOPS_query
     *  Purpose of Method:  creates an instance of this class, constructs query
     *  @param schema - a string representing the schema name to search
     */
    public get_company_management_id_schemaOPS_query(String schema)
    {
        query = new StringBuffer();

        //  construct query
        query.append("SELECT company.company_management_id AS id, ");
        query.append("company.company_name AS company ");
        query.append("FROM company ");
        query.append("WHERE company.company_db = \'");
        query.append(schema);
        query.append("\';");

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
