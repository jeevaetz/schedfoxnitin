/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.address;

import schedfoxlib.model.Address;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class check_if_address_exists_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(Address address) {
        super.setPreparedStatement(new Object[] {
            address.getAddress1().trim(), address.getAddress2().trim(),
            address.getCity().trim(), address.getState().trim(),
            address.getZip().trim()
        });
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT latitude, longitude FROM ");
        sql.append("address_geocode ");
        sql.append("WHERE ");
        sql.append("UPPER(address1) = UPPER(?) AND UPPER(address2) = UPPER(?) AND ");
        sql.append("UPPER(city) = UPPER(?) AND UPPER(state) = UPPER(?) AND ");
        sql.append("UPPER(zip) = UPPER(?);");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
