//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.employee;

//  import declarations

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;


/**
 *  FileName:  employee_additional_field_required_query.java
 *  @author jdavis
 *  Date Created:  01/03/2011
 *  Modifications:
 *  Purpose of Class:  a query class for determining if an additional fields
 *      file is required
 */
public class employee_additional_field_required_query extends GeneralQueryFormat
{
    //  private variable declarations
    private StringBuilder query;
    private String fieldName;
    
    /**  Create default instance of class */
    public employee_additional_field_required_query()   {}
    
    /**
     *  Purpose of Method:  creates an instance of this class, constructs query
     *  @param fieldName -  a string representing the field name to search for
     */
    public employee_additional_field_required_query(String fieldName)
    {
        query = new StringBuilder();
        this.fieldName = fieldName;

        //  construct query
        query.append("SELECT dynamic_field_def.is_required ");
        query.append("FROM dynamic_field_def ");
        query.append("WHERE dynamic_field_def.dynamic_field_def_name = '");
        query.append(this.fieldName);
        query.append("'");
        query.append("AND dynamic_field_def.is_active = true;");
    }

    @Override
    public String toString()    {return query.toString();}

    @Override
    public boolean hasAccess() {    return true;}
};
