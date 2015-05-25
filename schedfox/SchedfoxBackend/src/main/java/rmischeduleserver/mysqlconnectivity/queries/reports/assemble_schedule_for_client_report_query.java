/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.reports;

import rmischeduleserver.mysqlconnectivity.queries.schedule_data.generic_assemble_schedule_query;

/**
 *
 * @author user
 */
public class assemble_schedule_for_client_report_query extends generic_assemble_schedule_query {

    private String clients;

    /** Creates a new instance of assemble_schedule_for_employee_report_query */
    public assemble_schedule_for_client_report_query() {
        super();
        super.setSelectedFields(myReportFields);
        super.setOrderByFields(myClientReportOrder);
    }

    @Override
    public void update(String clientsToInclude, String employeesToInclude, String sdate, String edate, String type, String lastupdated, boolean showDeleted) {
        super.update("","",sdate,edate,type,lastupdated,showDeleted);
        clients = clientsToInclude;
    }

    @Override
    public String generateCompleteWhereClause() {
        if (clients.trim().length() > 0) {
            return " WHERE cid = " + clients + " ";
        } else {
            return " ";
        }
    }
}
