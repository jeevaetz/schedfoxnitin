/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.client;

import schedfoxlib.model.Client;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class SaveClientObjectQuery extends GeneralQueryFormat {

    private boolean newClient = false;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void updateWithClient(Client cli, boolean newClient) {
        this.newClient = newClient;
        String reportTime = cli.getReportTime();
        if (reportTime == null || reportTime.isEmpty()) {
            reportTime = "08:00:00";
        }
        Boolean canLoginToRoute = cli.getLogIntoRoute();
        if (canLoginToRoute == null) {
            canLoginToRoute = false;
        }
        if (this.newClient) {
            super.setPreparedStatement(cli.getClientId(), cli.getBranchId(), cli.getClientAddress(), cli.getClientAddress2(),
                    cli.getClientCity(), cli.getClientState(), cli.getClientZip(), cli.getClientFax(), cli.getClientPhone(),
                    cli.getClientPhone2(), cli.getClientName(), cli.getClientStartDate(), cli.getClientEndDate(),
                    cli.getClientType(), cli.getClientTrainingTime(), cli.getClientBillForTraining(), cli.getClientBreak(),
                    cli.getManagementId(), cli.getRateCodeId(), cli.getClientIsDeleted(), cli.getDefaultNonBillable(),
                    cli.getCheckOutOptionId(), cli.getCheckinFromPostPhone(), cli.getcUserName(), cli.getcPassword(),
                    cli.getDisplayClientInCallQueue(), reportTime, canLoginToRoute
                    );
        } else {
            super.setPreparedStatement(cli.getBranchId(), cli.getClientAddress(), cli.getClientAddress2(),
                    cli.getClientCity(), cli.getClientState(), cli.getClientZip(), cli.getClientFax(), cli.getClientPhone(),
                    cli.getClientPhone2(), cli.getClientName(), cli.getClientStartDate(), cli.getClientEndDate(),
                    cli.getClientType(), cli.getClientTrainingTime(), cli.getClientBillForTraining(), cli.getClientBreak(),
                    cli.getManagementId(), cli.getRateCodeId(), cli.getClientIsDeleted(), cli.getDefaultNonBillable(),
                    cli.getCheckOutOptionId(), cli.getCheckinFromPostPhone(), cli.getcUserName(), cli.getcPassword(),
                    cli.getDisplayClientInCallQueue(), reportTime, canLoginToRoute,
                    cli.getClientId());
        }
    }

    @Override
    public String getPreparedStatementString() {

        StringBuffer sql = new StringBuffer();
        if (newClient) {
            //This is a new employee.
            sql.append("INSERT INTO client ");
            sql.append("(client_id, branch_id, client_address, client_address2, client_city, client_state, ");
            sql.append(" client_zip, client_fax, client_phone, client_phone2, client_name, client_start_date, ");
            sql.append(" client_end_date, client_type, client_training_time, client_bill_for_training, ");
            sql.append(" client_break, management_id, rate_code_id, client_is_deleted, default_non_billable, ");
            sql.append(" check_out_option_id, checkin_from_post_phone, cusername, cpassword, display_client_in_call_queue, ");
            sql.append(" report_time, log_into_route ");
            sql.append(" )");
            sql.append("    VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
            sql.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
            sql.append(" ?, ?, ?, ?, ?, ?, ?::time without time zone, ");
            sql.append(" ?);");
        } else {
            sql.append("UPDATE client ");
            sql.append("SET ");
            sql.append("branch_id = ?, client_address = ?, client_address2 = ?, client_city = ?, ");
            sql.append("client_state = ?, client_zip = ?, client_fax = ?, client_phone = ?, client_phone2 = ?, ");
            sql.append("client_name = ?, client_start_date = ?, client_end_date = ?, client_type = ?, ");
            sql.append("client_training_time = ?, client_bill_for_training = ?, client_break = ?, ");
            sql.append("client_last_updated = NOW(), management_id = ?, rate_code_id = ?, client_is_deleted = ?, ");
            sql.append("default_non_billable = ?, check_out_option_id = ?, checkin_from_post_phone = ?, ");
            sql.append("cusername = ?, cpassword = ?, display_client_in_call_queue = ?, report_time = ?::time without time zone, ");
            sql.append("log_into_route = ? ");
            sql.append("WHERE ");
            sql.append("client_id = ?;");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
