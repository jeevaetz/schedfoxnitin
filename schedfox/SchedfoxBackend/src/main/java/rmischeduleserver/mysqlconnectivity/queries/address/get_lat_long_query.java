/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.address;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_lat_long_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder retVal = new StringBuilder();
        retVal.append("SELECT * FROM ");
        retVal.append("address_geocode ");
        retVal.append("WHERE ");
        retVal.append("upper(address1) = upper(?) AND upper(address2) = upper(?) AND ");
        retVal.append("upper(city) = upper(?) AND upper(state) = upper(?) AND upper(zip) = upper(?);");
        return retVal.toString();
    }
    
}
