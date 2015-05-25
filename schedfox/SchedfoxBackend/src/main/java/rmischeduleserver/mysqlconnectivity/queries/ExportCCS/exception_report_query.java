/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.ExportCCS;

import rmischeduleserver.mysqlconnectivity.queries.schedule_data.generic_assemble_schedule_query;

/**
 *
 * @author user
 */
public class exception_report_query  extends generic_assemble_schedule_query {

    /** Creates a new instance of new_export_schedule_query */
    public exception_report_query() {
        
    }

    public void update(String sdate, String edate) {
        super.update("", "", sdate,edate, "", "", false);
        super.setSelectedFields(super.myExportFields);
        super.ShowDeleted = false;
    }

    public String generateOrderBy() {
        return " WHERE isdeleted = '0' AND eid != 0;";
    }

    protected String getFinalSelectStatement() {
        String mainQuery = super.getFinalSelectStatement().replaceAll(";", "");
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DISTINCT employee.employee_first_name, employee.employee_last_name ");
        sql.append("FROM (");
        sql.append(mainQuery + ") as sched ");
        sql.append("LEFT JOIN employee ON employee.employee_id = sched.eid ");
        sql.append("LEFT JOIN usked_employee ON usked_employee.employee_id = sched.eid ");
        sql.append("WHERE ");
        sql.append("usked_emp_id IS NULL OR usked_emp_id = ''");
        return sql.toString();
    }
}
