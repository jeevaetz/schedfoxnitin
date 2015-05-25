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
public class save_address_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(Address address) {
        super.setPreparedStatement(new Object[]{
            address.getAddress1().trim(), address.getAddress2().trim(), address.getCity().trim(),
            address.getState().trim(), address.getZip().trim(),
            address.getAddress1().trim(), address.getAddress2().trim(), address.getCity().trim(),
            address.getState().trim(), address.getZip().trim(), address.getLatitude(),
            address.getLongitude()
        });
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM address_geocode ");
        sql.append("WHERE ");
        sql.append("address1 = ? AND address2 = ? AND city = ? AND state = ? AND zip = ?; ");
        sql.append("INSERT INTO address_geocode ");
        sql.append("(address1, address2, city, state, zip, latitude, longitude) ");
        sql.append("VALUES ");
        sql.append("(?, ?, ?, ?, ?, ?, ?); ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
