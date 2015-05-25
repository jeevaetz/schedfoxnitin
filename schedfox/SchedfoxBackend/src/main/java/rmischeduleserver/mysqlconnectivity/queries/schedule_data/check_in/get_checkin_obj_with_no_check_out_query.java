/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in;


import java.sql.Date;
import schedfoxlib.model.CheckIn;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_checkin_obj_with_no_check_out_query extends GeneralQueryFormat {
    
   @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(int timeCheckedOut,int personCheckedOut,int timeBuffer,String startDate, String endDate){
        //super.setPreparedStatement(new Object[]{endDate,startDate});
        System.out.println("Start Date: "+startDate);
        System.out.println("End Date: "+endDate);
        System.out.println("Time buffer is: "+timeBuffer);
        super.setPreparedStatement(new Object[]{startDate,endDate,0,0,timeBuffer});
        
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM checkin ");
        sql.append("WHERE ");
        sql.append("checkin_date ");
        sql.append("BETWEEN ");
        sql.append("DATE(?)");
        sql.append(" AND ");
        sql.append("DATE(?)");
        sql.append(" AND ");
        sql.append("time_stamp_out = ?");
        sql.append(" AND ");
        sql.append("person_checked_out = ?");
        sql.append(" AND ");
        sql.append("end_time > ?");
        
        
        return sql.toString();
        
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }


}
