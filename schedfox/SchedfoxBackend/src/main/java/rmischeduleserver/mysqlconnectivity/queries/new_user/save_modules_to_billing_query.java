/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.new_user;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_modules_to_billing_query extends GeneralQueryFormat {

    private int companyBillingId;
    private ArrayList<Integer> moduleIds;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(int companyBillingId, ArrayList<Integer> moduleIds) {
        this.companyBillingId = companyBillingId;
        this.moduleIds = moduleIds;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        for (int m = 0; m < moduleIds.size(); m++) {
            sql.append("INSERT INTO control_db.company_billing_info_modules ");
            sql.append("(company_billing_info_id, company_billing_module_id, start_date, end_date) ");
            sql.append(" VALUES ");
            sql.append("(" + companyBillingId + "," + moduleIds.get(m) + ", NOW(), NOW() + interval '10 year');");
        }
        return sql.toString();
    }

}
