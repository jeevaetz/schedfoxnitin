/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.messageBoard;

import java.util.Vector;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author vnguyen
 */
public class GetAllContacts extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    private String queryString;
    //private to prevent calling of a non-overloaded constructor

    private GetAllContacts() {
    }

    public GetAllContacts(Vector<String> myCompaniesId) {
        this.queryString = "SELECT distinct u.user_id, company_id, u.user_first_name, " +
                "u.user_last_name FROM user_branch_company inner join \"user\" as u " +
                "on user_branch_company.user_id = u.user_id " +
                "where (";
        for (int i = 0; i < myCompaniesId.size(); i++) {
            this.queryString += "user_branch_company.company_id =" + myCompaniesId.get(i);
            if (i != (myCompaniesId.size() - 1)) {
                this.queryString += " or ";
            }
        }
        this.queryString += ") order by company_id";
    }
    @Override
    public String toString(){
        return this.queryString;
    }
}
