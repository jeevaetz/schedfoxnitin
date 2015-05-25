/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.sales;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.SalesItinerary;

/**
 *
 * @author ira
 */
public class save_sales_itinerary_query extends GeneralQueryFormat {

    private boolean isUpdate;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(SalesItinerary salesIt) {
        if (salesIt.getSalesItineraryId() == null) {
            isUpdate = false;
            super.setPreparedStatement(new Object[]{
                salesIt.getUserId(), salesIt.getDateOfItinerary(), salesIt.getStimeOfItinerary(), salesIt.getEtimeOfItinerary(),
                salesIt.getExternalGid(), salesIt.getSalesItineraryTypeId(), salesIt.getMeetingText(), salesIt.getActive(),
                salesIt.getSubject()
            });
        } else {
            isUpdate = true;
            super.setPreparedStatement(new Object[]{
                salesIt.getUserId(), salesIt.getDateOfItinerary(), salesIt.getStimeOfItinerary(), salesIt.getEtimeOfItinerary(),
                salesIt.getExternalGid(), salesIt.getSalesItineraryTypeId(), salesIt.getMeetingText(), salesIt.getActive(), 
                salesIt.getSubject(),
                salesIt.getSalesItineraryId()
            });
        }
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (!isUpdate) {
            sql.append("INSERT INTO ");
            sql.append("sales_itinerary ");
            sql.append("(user_id, date_of_itinerary, stime_of_itinerary, etime_of_itinerary, external_gid, sales_itinerary_type_id, meeting_text, active, subject) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?);");
        } else {
            sql.append("UPDATE ");
            sql.append("sales_itinerary ");
            sql.append("SET ");
            sql.append("user_id = ?, date_of_itinerary = ?, stime_of_itinerary = ?, etime_of_itinerary = ?, external_gid = ?, sales_itinerary_type_id = ?, meeting_text = ?, active = ?, subject = ? ");
            sql.append("WHERE ");
            sql.append("sales_itinerary_id = ?;");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
