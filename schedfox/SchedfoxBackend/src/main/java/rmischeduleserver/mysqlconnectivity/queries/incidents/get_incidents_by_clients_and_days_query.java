/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.incidents;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_incidents_by_clients_and_days_query extends GeneralQueryFormat {

    private Integer sizeOfArray;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    public void update(ArrayList<Integer> clientIds, Integer numberOfDays) {
        sizeOfArray = clientIds.size();
        ArrayList<Object> params = new ArrayList<Object>();
        params.addAll(clientIds);
        params.add(numberOfDays);
        super.setPreparedStatement(params.toArray());
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("employee.*, incident_report_type.*, incident_report.*, ");
        sql.append("(SELECT COUNT(*) FROM incident_report_documents WHERE incident_report_documents.incident_report_id = incident_report.incident_report_id) as image_count ");
        sql.append("FROM ");
        sql.append("incident_report ");
        sql.append("LEFT JOIN employee ON employee.employee_id = incident_report.employee_id ");
        sql.append("INNER JOIN incident_report_type ON incident_report_type.incident_report_type_id = incident_report.incident_report_type_id ");
        sql.append("WHERE ");
        if (sizeOfArray > 0) {
            sql.append("client_id IN (");
            for (int s = 0; s < sizeOfArray; s++) {
                if (s > 0) {
                    sql.append(",");
                }
                sql.append("?");
            }
            sql.append(") AND ");
        }
        sql.append("NOW() - interval '1 day' * ? <= date_entered ");
        sql.append("ORDER BY ");
        sql.append("date_entered DESC ");
        return sql.toString();
    }
    
}
