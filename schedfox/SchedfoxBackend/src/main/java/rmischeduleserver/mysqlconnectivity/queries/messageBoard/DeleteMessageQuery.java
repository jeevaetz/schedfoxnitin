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
public class DeleteMessageQuery extends GeneralQueryFormat {

    private String queryString;

    public DeleteMessageQuery(String noteCompanyID, Vector<String> myCompaniesId,Vector<String> myCompDB) {
        for (int i = 0; i < myCompaniesId.size(); i++) {
            if (noteCompanyID.compareToIgnoreCase(myCompaniesId.get(i)) == 0) {
                this.queryString = "set search_path = '" + myCompDB.get(i) + "';";
            }
        }
        this.queryString += "DELETE FROM  internalemail where message_id = "+myCompDB+";";

    }
    public String toString(){
        return this.queryString;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }
}
