/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class client_login_information_query extends GeneralQueryFormat {

    int client_id = 0;
    String client_Login=new String("");
    String sql=new String("SELECT client_id, url, cusername, cpassword from client ");
    String sqlWhereId=new String("where client_id=");
    String sqlWhereLogin=new String("where lower(cusername)=lower(");

    public void update(int client_id) {
        this.client_id = client_id;
    }
    public void update(String client_Login) {
        this.client_Login = client_Login;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String toString() {
        if(client_id!=0)
            return(sql+sqlWhereId+client_id);
        else if(client_Login.length()>0){
            String str=new String(sql+sqlWhereLogin+"'"+client_Login+"'");
            return(sql+sqlWhereLogin+"'"+client_Login+"')");
        } else
            return("");

//        StringBuffer sql = new StringBuffer();
//        sql.append("SELECT url, cusername, cpassword ");
//        sql.append("FROM client ");
//        sql.append("WHERE client_id = " + client_id);
//        return sql.toString();
    }
}
