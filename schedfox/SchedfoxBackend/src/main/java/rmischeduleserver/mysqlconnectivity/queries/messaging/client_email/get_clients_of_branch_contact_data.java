package rmischeduleserver.mysqlconnectivity.queries.messaging.client_email;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;


public final class get_clients_of_branch_contact_data extends GeneralQueryFormat {

    public get_clients_of_branch_contact_data() {
    }

    /**
     *  Constructs and returns the prepared statement.
     *  @return sqlQuery a string containing the prepared statement
     */
    @Override
    public String getPreparedStatementString() {
        StringBuilder returnQuery = new StringBuilder();

        returnQuery.append("SELECT client.client_id as clientid, * ");
//        returnQuery.append("client.client_id AS clientId, ");
//        returnQuery.append("client_name AS clientName, ");
//        returnQuery.append("client_phone AS clientPhonePrimary, ");
//        returnQuery.append("client_phone2 AS clientPhoneSecondary, ");
//        returnQuery.append("client_address AS clientAddressPrimary, ");
//        returnQuery.append("client_address2 AS clientAddressSecondary, ");
//        returnQuery.append("client_city AS clientCity, ");
//        returnQuery.append("client_state AS clientState, ");
//        returnQuery.append("client_zip AS clientZip, ");
//        returnQuery.append("client_is_deleted AS isClientActive, ");
//        returnQuery.append("client_contact_id AS clientcontactid, ");
//        returnQuery.append("client_contact_is_primary AS isprimary, ");
//        returnQuery.append("client_contact_title AS clientcontacttitle, ");
//        returnQuery.append("client_contact_first_name AS clientcontactfirstname, ");
//        returnQuery.append("client_contact_last_name AS clientcontactlastname, ");
//        returnQuery.append("client_contact_phone AS clientcontactphone, ");
//        returnQuery.append("client_contact_cell AS clientcontactcell, ");
//        returnQuery.append("client_contact_email AS clientcontactemail ");
        returnQuery.append("FROM client ");
        returnQuery.append("LEFT JOIN client_contact ON client_contact.client_id = client.client_id AND client_contact_is_deleted = 0 ");
        returnQuery.append("WHERE branch_id = ? ");
        returnQuery.append("ORDER BY client.client_id ");
        return returnQuery.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    /** Describes whether this object has database access
     *  <p><b>Default:  </b><i>TRUE</i>
     *  @return hasAccess a boolean describing if this object has access to database
     */
    @Override
    public boolean hasAccess() {
        return true;
    }
};
