/**
 *  FileName:  employee_name_additional_field_query.java
 *  @author  Jeffrey Davis
 *  Date Created:  1/10/2011
 *  Modifications:
 */

//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.employee;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *  Class Name:  employee_name_additional_field_query
 *  @author Jeffrey Davis
 *  Purpose of Class:  a query class for determing an employee's first/last
 *      name for display in <code>Employee_View_Additional_Fields_Files</code>
 *  @see <code>Employee_View_Additional_Fields_Files</code>
 *  @see <code>GeneralQueryFormat</code>
 */
public class employee_name_additional_field_query extends GeneralQueryFormat
{
    //  private variable declarations
    private StringBuilder query;
    private String employeeId;

    /*  Create default instance */
    public employee_name_additional_field_query()
    {
        this.query = null;
        this.employeeId = null;
    }

    /**
     *  Purpose of Method:  creates instance of this object, initializes
     *      class variables, constructs and finalizes query
     *  @param employeeId - a string representing the employee id
     */
    public employee_name_additional_field_query(String employeeId)
    {
        //  initialize class variables
        this.employeeId = employeeId;
        this.query = new StringBuilder();

        //  construct query
        query.append("SELECT ");
        query.append("employee.employee_first_name AS first, ");
        query.append("employee.employee_last_name AS last ");
        query.append("FROM employee ");
        query.append("WHERE employee.employee_id = ");
        query.append(this.employeeId);
        query.append(";");
    }

    @Override
    public String toString()    {   return query.toString();}

    @Override
    public boolean hasAccess()  {   return true;}
};
