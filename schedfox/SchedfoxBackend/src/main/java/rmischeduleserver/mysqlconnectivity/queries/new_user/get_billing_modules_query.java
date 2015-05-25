/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.new_user;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_billing_modules_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    public String toString() {
        return "SELECT * FROM  control_db.company_billing_module;";
    }

}
