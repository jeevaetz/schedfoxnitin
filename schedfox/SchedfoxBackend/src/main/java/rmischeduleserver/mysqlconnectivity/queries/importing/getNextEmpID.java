/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.importing;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author vnguyen
 */
public class getNextEmpID extends GeneralQueryFormat{

    @Override
    public boolean hasAccess() {
        return true;
    }
    public String toString(){
        return "SELECT nextval('employee_seq') AS id FROM control_db.messaging_outbound LIMIT 1";
    }

}
