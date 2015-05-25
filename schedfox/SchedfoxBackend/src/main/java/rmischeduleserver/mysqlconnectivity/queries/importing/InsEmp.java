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
public class InsEmp extends GeneralQueryFormat {

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
        StringBuilder cols = new StringBuilder();
        StringBuilder vals = new StringBuilder();
        cols.append("(");
        vals.append("(");
        for (int i = 0; i < this.data.size(); i++) {
            cols.append(data.get(i).col);
            vals.append("'" + data.get(i).val + "'");
            if (i != this.data.size() - 1) {
                cols.append(",");
                vals.append(",");
            } else {
                cols.append(")");
                vals.append(")");
            }
        }
        String queryString = "INSERT INTO employee " + cols.toString() + " VALUES " + vals.toString();
        System.out.println(queryString);
        return queryString;
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
