/*
 * client_save_query.java
 *
 * Created on January 26, 2005, 5:02 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.client;
import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author jason.allen
 */
public class client_save_query extends GeneralQueryFormat{
    
    private int CID;
    private String NAME;
    private String PHONE;
    private String PHONE2;
    private String FAX;
    private String ADDRESS;
    private String ADDRESS2;
    private String CITY;
    private String STATE;
    private String ZIP;
    private String MID;
    private String START;
    private String END;
    private String DELETED;
    private String SITE_PHONE;
    private String SITE_PAGER;
    private String SITE_FAX;
    private String SITE_PHONE_2;
    private String SITE_PAGER_2;
    private String SITE_FAX_2;
    private String WS;
    private String TRAININGTIME;
    private String BILLFORTRAINING;
    private String RATE_CODE;
    private String ORDER;
    private boolean remoteInd;
    private Integer volumeInd;
    private boolean markNonBillable;
    
    /** Creates a new instance of client_save_query */
    public client_save_query() {
        myReturnString = "";
    }
    
    /**
     * Update/Insert with rate code / order
     */    
    public void update(
          String cid,           String name,            String phone,         
          String phone2,        String fax,             String address,       
          String address2,      String city,            String state,         
          String zip,           String mid,             String start,
          String end,           String deleted,         String site_phone,
          String site_pager,    String site_fax,        String site_phone_2,  
          String site_pager_2,  String site_fax_2,      String ws,
          String trainingTime,  String billForTraining, String rate_code,
          String order,
          boolean remoteMarket,     Integer storeVolume, boolean markNonBillable
    ){
      if (start.length() == 0)  { start = "NOW()"; }
      else                      { start = "'" + start + "'"; }
      
      if (end.length() == 0)    { end = "(NOW() + interval '20 years')"; }
      else                      { end = "'" + end + "'"; }
      
      if (mid.length() == 0) {
          mid = "0";
      }
      if (deleted.length() == 0) {
          deleted = "0";
      }
      if (ws.length() == 0) {
          ws = "0";
      }
      if (trainingTime.length() == 0) {
          trainingTime = "0";
      }
      try {
          CID = Integer.parseInt(cid);
      } catch (Exception e) {
          CID = 0;
      }
      NAME = name.replaceAll("'", "''");
      PHONE = phone.replaceAll("'", "''");
      PHONE2 = phone2.replaceAll("'", "''");
      FAX = fax.replaceAll("'", "''");
      ADDRESS = address.replaceAll("'", "''");
      ADDRESS2 = address2.replaceAll("'", "''");
      CITY = city.replaceAll("'", "''");
      STATE = state.replaceAll("'", "''");
      ZIP = zip.replaceAll("'", "''");
      MID = mid;
      START = start;
      END = end;
      DELETED = deleted;
      SITE_PHONE = site_phone.replaceAll("'", "''");
      SITE_PHONE_2 = site_phone_2.replaceAll("'", "''");
      SITE_PAGER = site_pager.replaceAll("'", "''");
      SITE_FAX = site_fax.replaceAll("'", "''");
      SITE_PAGER_2 = site_pager_2.replaceAll("'", "''");
      SITE_FAX_2 = site_fax_2.replaceAll("'", "''");
      WS = ws;
      TRAININGTIME = trainingTime;
      BILLFORTRAINING = billForTraining;
      RATE_CODE = rate_code;
      ORDER = order;
      this.remoteInd = remoteMarket;
      this.volumeInd = storeVolume;
      this.markNonBillable = markNonBillable;
    }      
    public void update(
          String cid,           String name,            String phone,
          String phone2,        String fax,             String address,
          String address2,      String city,            String state,
          String zip,           String mid,             String start,
          String end,           String deleted,         String site_phone,
          String site_pager,    String site_fax,        String site_phone_2,
          String site_pager_2,  String site_fax_2,      String ws,
          String trainingTime,  String billForTraining, String rate_code,
          String order
    ){
      if (start.length() == 0)  { start = "NOW()"; }
      else                      { start = "'" + start + "'"; }

      if (end.length() == 0)    { end = "(NOW() + interval '20 years')"; }
      else                      { end = "'" + end + "'"; }

      if (mid.length() == 0) {
          mid = "0";
      }
      if (deleted.length() == 0) {
          deleted = "0";
      }
      if (ws.length() == 0) {
          ws = "0";
      }
      if (trainingTime.length() == 0) {
          trainingTime = "0";
      }
      try {
          CID = Integer.parseInt(cid);
      } catch (Exception e) {
          CID = 0;
      }
      NAME = name.replaceAll("'", "''");
      PHONE = phone.replaceAll("'", "''");
      PHONE2 = phone2.replaceAll("'", "''");
      FAX = fax.replaceAll("'", "''");
      ADDRESS = address.replaceAll("'", "''");
      ADDRESS2 = address2.replaceAll("'", "''");
      CITY = city.replaceAll("'", "''");
      STATE = state.replaceAll("'", "''");
      ZIP = zip.replaceAll("'", "''");
      MID = mid;
      START = start;
      END = end;
      DELETED = deleted;
      SITE_PHONE = site_phone.replaceAll("'", "''");
      SITE_PHONE_2 = site_phone_2.replaceAll("'", "''");
      SITE_PAGER = site_pager.replaceAll("'", "''");
      SITE_FAX = site_fax.replaceAll("'", "''");
      SITE_PAGER_2 = site_pager_2.replaceAll("'", "''");
      SITE_FAX_2 = site_fax_2.replaceAll("'", "''");
      WS = ws;
      TRAININGTIME = trainingTime;
      BILLFORTRAINING = billForTraining;
      RATE_CODE = rate_code;
      ORDER = order;
     }
    
    public boolean hasAccess(){
        return true;
    }

    @Override
    public int getUpdateStatus() { 
        return GeneralQueryFormat.UPDATE_SCHEDULE;                 
    }
    
    @Override
    public String toString() {
        String value = "false";
        if (markNonBillable) {
            value = "true";
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("f_insert_cli(" + getBranch() + "," + CID + ", '" + NAME + "', '");
        sql.append(PHONE + "', '" + PHONE2 + "', '" + FAX + "', '" + ADDRESS + "', '");
        sql.append(ADDRESS2 + "', '" + CITY + "', '" + STATE + "', '" + ZIP + "', " + MID + ", DATE(");
        sql.append(START + "), DATE(" + END + "), " + DELETED + ", '" + PHONE + "', '" + FAX + "', '");
        sql.append(PHONE2 + "', '', '', '" + FAX + "', " + WS + ", " + TRAININGTIME + ", ");
        sql.append(BILLFORTRAINING + ",0," + RATE_CODE + "," + value +");");

        return sql.toString();
    }
    
}
