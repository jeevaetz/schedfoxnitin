/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.importing;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author vnguyen
 */
public class updateEmployee extends GeneralQueryFormat {

    ArrayList<dataPoint> data = new ArrayList<dataPoint>();

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void addData(String col, String val) {
        dataPoint d = new dataPoint(col, val);
        this.data.add(d);
    }

    @Override
    public String toString() {
        StringBuilder queryString = new StringBuilder();
        queryString.append("UPDATE employee SET ");
        String ssn = null;
        String emp_id = null;
        for (int i = 0; i < this.data.size(); i++) {
            if (i != 0) {
                queryString.append(",");
            }
            dataPoint dp = this.data.get(i);
            queryString.append(dp.col + "='" + dp.val + "'");
            if (dp.col.compareToIgnoreCase("employee_id") == 0) {
                emp_id = dp.val;
            } else if (dp.col.compareToIgnoreCase("employee_ssn") == 0) {
                ssn = dp.val;
            }
        }
        queryString.append(" WHERE ");
        if (emp_id != null) {
            queryString.append("employee_id = " + emp_id);
        } else {
            queryString.append("employee_ssn = " + ssn);
        }
        return queryString.toString();
    }

    class dataPoint {

        String col;
        String val;

        private dataPoint(String col, String val) {
            this.col = col;
            this.val = val;
        }
    }
}
