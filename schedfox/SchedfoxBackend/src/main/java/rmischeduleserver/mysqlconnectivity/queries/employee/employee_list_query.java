/*
 * employee_list_query.java
 *
 * Created on January 21, 2005, 7:27 AM
 */
package rmischeduleserver.mysqlconnectivity.queries.employee;

import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.mysqlconnectivity.*;

/**
 *
 * @author ira
 */
public class employee_list_query extends GeneralQueryFormat {

    private String employee;
    private String search;
    private String deleted;
    private boolean canSeeOtherEmployees;
    private int employeeType;
    /** Creates a new instance of employee_list_query */
    public employee_list_query() {
        myReturnString = new String();
    }

    public boolean hasAccess() {
        return true;
    }

    public void update(String Search, String Deleted, String employee, boolean canSeeOtherEmployees) {
        update(Search,Deleted,employee,canSeeOtherEmployees,0);
    }

    public void update(String Search, String Deleted, String employee, boolean canSeeOtherEmployees,int empType) {
        search = Search;
        deleted = Deleted;
        employeeType=empType;
        this.employee = employee;
        if (this.deleted == null) {
            this.deleted = "";
        }
        if (this.search == null || this.search.length() == 0) {
            this.search = "";
        }
        this.canSeeOtherEmployees = canSeeOtherEmployees;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        int branch = 0;
        Integer employeeId = null;
        try {
            employeeId = Integer.parseInt(employee);
        } catch (Exception e) {}
        try {
            branch = Integer.parseInt(getBranch());
        } catch (Exception e) {}

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        if(employeeType==0){
            this.setPreparedStatement(new Object[]{search, branch, branch != 0, (deleted.equals("1")), (search.length() > 1), employeeId});
            sql.append("f_employee_list_query_alw(?, ?::integer, ?::boolean, ?::boolean, ?::boolean, ?::integer);");
        } else {
            this.setPreparedStatement(new Object[]{search, branch, branch = 0, (deleted.equals("1")), (search.length() > 1), employeeId,employeeType});
            sql.append("f_employee_list_query_approval(?, ?::integer, ?::boolean, ?::boolean, ?::boolean, ?::integer, ?::integer);");
        }
        return sql.toString();
    }
}
