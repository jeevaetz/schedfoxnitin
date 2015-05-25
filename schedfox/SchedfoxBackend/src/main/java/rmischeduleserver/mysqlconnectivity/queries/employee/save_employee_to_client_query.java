/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_employee_to_client_query extends GeneralQueryFormat {
    private int employee_id;
    private ArrayList<Integer> client_ids;

    public save_employee_to_client_query() {
        client_ids = new ArrayList<Integer>();
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(int empId, ArrayList<Integer> cli_ids) {
        this.employee_id = empId;
        this.client_ids = cli_ids;
    }

    @Override
    public String toString() {
        String clientIdsStr = "'{";
        for (int e = 0; e < client_ids.size(); e++) {
            if (e > 0) {
                clientIdsStr = clientIdsStr + ",";
            }
            clientIdsStr = clientIdsStr + client_ids.get(e);
        }
        clientIdsStr = clientIdsStr + "}'::integer[]";
        return "SELECT f_save_employee_to_client_query(" + this.employee_id + "," +
                clientIdsStr + "," + client_ids.size() + ");";
    }
}
