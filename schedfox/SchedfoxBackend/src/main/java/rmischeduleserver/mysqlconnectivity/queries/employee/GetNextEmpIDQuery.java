/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author vnguyen
 */
public class GetNextEmpIDQuery extends GeneralQueryFormat{

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String toString(){
        return "SELECT nextval('employee_seq') AS id";
    }

}
