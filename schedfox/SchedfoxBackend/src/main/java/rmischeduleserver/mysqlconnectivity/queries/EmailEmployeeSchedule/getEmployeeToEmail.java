/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.EmailEmployeeSchedule;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author vnguyen
 */
public class getEmployeeToEmail extends GeneralQueryFormat {

    private StringBuilder queryString = new StringBuilder();
    private String branchId;

    public getEmployeeToEmail(String branch_id) {
        this.branchId = branch_id;
        this.queryString.append("SELECT * FROM EMPLOYEE as e ");
        this.queryString.append("WHERE e.employee_is_deleted = 0 AND ");
        this.queryString.append(" e.branch_id = " + this.branchId + " AND");
        this.queryString.append(" ( (e.employee_email <> '') OR (e.employee_email2 <> '') ); ");


    }

    @Override
    public boolean hasAccess() {
        return true;
    }
    public String toString(){
        return this.queryString.toString();
    }
}
