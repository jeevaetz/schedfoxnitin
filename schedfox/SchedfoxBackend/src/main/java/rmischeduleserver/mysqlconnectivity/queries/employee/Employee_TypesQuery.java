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
public class Employee_TypesQuery extends GeneralQueryFormat{

    @Override
    public boolean hasAccess() {
       return true;
    }
    public String toString(){
        return "select * from employee_types;";
    }

}
