/**
 * Filename: employee_messaging_list_query.java Author: Jeffrey N. Davis Date
 * Created: 05/27/2010 Date Last Modified: 05/27/2010 Purpose of File: contains
 * a query that will retrieve a list of employees for a particular branch of a
 * company for a list to message FINAL FORMAT OF QUERY: SELECT employee_id as
 * empid, branch_id as branchid, employee_first_name as firstname,
 * employee_last_name as lastname, employee_middle_initial as middleinitial,
 * employee_cell as empcell, employee_email as email0, employee_email2 as
 * email1, email_messaging as emailmessaging, sms_messaging as smsmessaging FROM
 * employee WHERE employee_is_deleted = 0 AND branch_id = <branch_id>;
 */
package rmischeduleserver.mysqlconnectivity.queries.messaging;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

public class employee_messaging_list_query extends GeneralQueryFormat {

    private boolean isEmailMessaging;
    private ArrayList<Integer> employeeIds;
    private String branchId;

    public employee_messaging_list_query() {

    }

    public void update(String branchId, Boolean isEmailMessaging, ArrayList<Integer> employeeIds) {
        this.isEmailMessaging = isEmailMessaging;
        this.employeeIds = employeeIds;
        this.branchId = branchId;

        ArrayList<Object> params = new ArrayList<Object>();
        if (branchId != null && !branchId.equals("-1")) {
            params.add(Integer.parseInt(branchId));
        }
        if (employeeIds != null) {
            for (int e = 0; e < employeeIds.size(); e++) {
                params.add(employeeIds.get(e));
            }
        }
        super.setPreparedStatement(params.toArray());
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT ");
        query.append("* ");
        query.append("FROM employee ");
        query.append("WHERE ");
        query.append("employee_is_deleted = 0 ");
        if (super.getPreparedStatementObjects().length > 0) {
            query.append("AND branch_id = ? ");
        }
        if (isEmailMessaging) {
            query.append("AND employee_email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$' AND email_contact = 1 ");
        } else {
            query.append("AND char_length(regexp_replace(employee_cell, '\\W+', '', 'g')) IN (10, 11) AND cell_contact IN ('1', '-1') ");
        }
        if (employeeIds != null && employeeIds.size() > 0) {
            query.append("AND employee_id IN (");
            for (int e = 0; e < employeeIds.size(); e++) {
                if (e > 0) {
                    query.append(",");
                }
                query.append("?");
            }
            query.append(") ");
        }
        query.append("ORDER BY employee_last_name, employee_first_name ");
        return query.toString();
    }

    @Override
    public boolean hasAccess() {
        return true;
    }
};
