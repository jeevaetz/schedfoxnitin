/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.address;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.AddressGeocodeDistance;

/**
 *
 * @author user
 */
public class save_address_geocode_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(AddressGeocodeDistance address) {
        super.setPreparedStatement(new Object[]{
            address.getAddressGeocode1(), address.getAddressGeocode2(),
            address.getTravelDuration(), address.getTravelDistance()
        });
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO address_geocode_distance ");
        sql.append("(address_geocode1, address_geocode2, travel_duration, travel_distance) ");
        sql.append("VALUES ");
        sql.append("(?, ?, ?, ?); ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
