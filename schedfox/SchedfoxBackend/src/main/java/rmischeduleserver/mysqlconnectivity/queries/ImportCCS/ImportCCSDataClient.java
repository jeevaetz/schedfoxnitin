/*
 * ImportCCSDataClient.java
 *
 * Created on January 18, 2005, 3:33 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.ImportCCS;
import rmischeduleserver.mysqlconnectivity.queries.*;
import java.util.ArrayList;
/**
 *
 * @author ira
 */
public class ImportCCSDataClient extends GeneralQueryFormat {
    
    private boolean clearBuffer;
    private StringBuilder myQueryBuffer;
    private myArrayClass uskedId;
    private myArrayClass uskedWs;
    private myArrayClass Name;
    private myArrayClass Phone;
    private myArrayClass Phone2;
    private myArrayClass Fax;
    private myArrayClass Address;
    private myArrayClass Address2;
    private myArrayClass City;
    private myArrayClass State;
    private myArrayClass Zip;
    private myArrayClass Sitefax;
    private myArrayClass Sitephone;
    private myArrayClass Sitephone2;
    private myArrayClass SitePage;
    private myArrayClass SiteCell;
    private myArrayClass Start;
    private myArrayClass End;
    private myArrayClass Deleted;
    
    /** Creates a new instance of ImportCCSDataClient */
    public ImportCCSDataClient() {
        myReturnString = new String();
        clearBuffer = false;
        uskedId = new myArrayClass();
        uskedWs = new myArrayClass();
        Name = new myArrayClass();
        Phone = new myArrayClass();
        Phone2 = new myArrayClass();
        Fax = new myArrayClass();
        Address = new myArrayClass();
        Address2 = new myArrayClass();
        City = new myArrayClass();
        State = new myArrayClass();
        Zip = new myArrayClass();
        Sitefax = new myArrayClass();
        Sitephone = new myArrayClass();
        Sitephone2 = new myArrayClass();
        SitePage = new myArrayClass();
        SiteCell = new myArrayClass();
        Start = new myArrayClass();
        End = new myArrayClass();
        Deleted = new myArrayClass();
    }
    
    public void update(String uskedid, String uskedws, String name, String phone, String phone2, String fax, String address, String address2, String city,
            String state, String zip, String mid, String start, String end, String deleted, String sitephone, String sitepager,
            String sitefax, String sitephone_2, String sitepager_2, String sitefax_2, String clientWorkSite) {
        
        if (phone.length() > 19) {
            phone = phone.substring(0, 19);
        }
        if (phone2.length() > 19) {
            phone2 = phone2.substring(0, 19);
        }
        if (fax.length() > 19) {
            fax = fax.substring(0, 19);
        }
      
        if (end.equals("0")) {
            end = "1000-10-10";
        }
        if (start.equals("0")) {
            start = "2100-10-10";
        }
        uskedId.add(uskedid);
        uskedWs.add(uskedws);
        Name.add(name);
        Phone.add(phone);
        Phone2.add(phone2);
        Fax.add(fax);
        Address.add(address);
        Address2.add(address2);
        City.add(city);
        State.add(state);
        Zip.add(zip);
        Sitefax.add(sitefax);
        Sitephone.add(sitephone);
        Sitephone2.add(sitephone_2);
        SitePage.add(sitepager);
        SiteCell.add(sitepager);
        Start.add(start);
        End.add(end);
        Deleted.add(deleted);
    }
    
    public void clear() {
        uskedId = new myArrayClass();
        uskedWs = new myArrayClass();
        Name = new myArrayClass();
        Phone = new myArrayClass();
        Phone2 = new myArrayClass();
        Fax = new myArrayClass();
        Address = new myArrayClass();
        Address2 = new myArrayClass();
        City = new myArrayClass();
        State = new myArrayClass();
        Zip = new myArrayClass();
        Sitefax = new myArrayClass();
        Sitephone = new myArrayClass();
        Sitephone2 = new myArrayClass();
        SitePage = new myArrayClass();
        SiteCell = new myArrayClass();
        Start = new myArrayClass();
        End = new myArrayClass();
        Deleted = new myArrayClass();
    }
    
    public String toString() {
        StringBuilder myQuery = new StringBuilder();
        for (int i = 0; i < uskedId.size(); i++) {
            myQuery.append("INSERT INTO client (client_id, branch_id, client_name, client_phone, client_phone2, client_fax, " +
                    "client_address, client_address2, client_city, client_state, client_zip, " +
                    "client_start_date, client_end_date, client_is_deleted, client_worksite)VALUES(" +
                    "(CASE WHEN (SELECT MAX(client_id + 1) FROM client) IS NULL THEN 1 ELSE " +
                    "(SELECT MAX(client_id + 1) FROM client) END), " + getBranch() + "," +
                    "'" + Name.Get(i) + "', '" + Phone.Get(i) + "','" + Phone2.Get(i) + "','" + Fax.Get(i) + "','" + Address.Get(i) +
                    "','" + Address2.Get(i) + "','" + City.Get(i) + "','" + State.Get(i) + "','" + Zip.Get(i) + "','" + Start.Get(i) +
                    "','" + End.Get(i) + "'," + Deleted.Get(i) + ", (CASE WHEN (SELECT client_id FROM usked_client WHERE usked_cli_id = '" +
                    uskedId.Get(i) + "' AND usked_ws_id = '' LIMIT 1) IS NULL THEN 0 ELSE (SELECT client_id FROM usked_client " +
                    "WHERE usked_cli_id = '" + uskedId.Get(i) + "' AND usked_ws_id = '' LIMIT 1) END)); " +
                    "INSERT INTO usked_client (client_branch, usked_cli_id, client_id, usked_ws_id) VALUES (" +
                    "" + getBranch() + ",'" + uskedId.Get(i) + "', (SELECT (MAX(client_id)) FROM client), '" + uskedWs.Get(i) + "');");
        }
        return myQuery.toString();
    }

    public boolean hasAccess() {
        return true;
    }  

    private class myArrayClass extends ArrayList {
        public myArrayClass() {
            super();
        }
        
        public void add(String val) {
            val.replaceAll("'",  "");
            super.add((Object)val);
        }
        
        public String Get(int i) {
            return (String)super.get(i);
        }
    }
    
}
