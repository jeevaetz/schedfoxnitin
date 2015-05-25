
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
public class MarkAsReadQuery extends GeneralQueryFormat {

    String queryString;

    public MarkAsReadQuery(String noteCompanyId, Vector<String> myCompaniesId, Vector<String> myCompaniesDB) {
        for (int i = 0; i < myCompaniesId.size(); i++) {
            if (noteCompanyId.compareToIgnoreCase(myCompaniesId.get(i)) == 0) {
                this.queryString = "set search_path = '" + myCompaniesDB.get(i) + "';";
            }
        }
        this.queryString += "UPDATE internalemail set read = true WHERE message_id = " +noteCompanyId + ";";
    }

    @Override
    public boolean hasAccess() {
        return true;
    }
    //private to force overload
    private MarkAsReadQuery() {
    }
    @Override
    public String toString(){
        return this.queryString;
    }
}
