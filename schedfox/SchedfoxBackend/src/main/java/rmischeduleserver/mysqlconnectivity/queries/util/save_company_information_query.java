/*
 * save_company_information_query.java
 *
 * Created on April 28, 2005, 11:21 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import java.util.*;
/**
 *
 * @author ira
 */
public class save_company_information_query extends GeneralQueryFormat {
    
    private ArrayList bid;
    private ArrayList bname;
    private String bmanage;
    private ArrayList cdb;
    
    /** Creates a new instance of save_company_information_query */
    public save_company_information_query(String manage) {
        myReturnString = new String();
        bid = new ArrayList();
        bname = new ArrayList();
        cdb = new ArrayList();
        bmanage = manage;
        if(bmanage.equals("0"))
            bmanage = "(SELECT max(management_id) FROM management_clients)";
    }
    
    public void update(String companyid, String companyname, String db) {
        bid.add(companyid);
        bname.add(companyname.replaceAll("'", "''"));
        cdb.add(db);
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        StringBuilder mySQL = new StringBuilder();
        for (int i = 0; i < bid.size(); i++) {
            if (bid.get(i).equals("0")) {
                mySQL.append("INSERT INTO " + getManagementSchema() + ".company (company_id, company_name, company_db, company_management_id) VALUES ((SELECT MAX(company_id) + 1 FROM company), " +
                             " '" + bname.get(i) + "', '" + cdb.get(i) + "', " + bmanage + ");");
            } else {
                mySQL.append("UPDATE " + getManagementSchema() + ".company SET company_name = '" + bname.get(i) + "', company_db = '" + cdb.get(i) +
                             "', company_management_id = " + bmanage + " WHERE company_id = " + bid.get(i) + ";");
            }
        }
        return  mySQL.toString();
    }
    
}