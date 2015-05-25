/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.messaging.email;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author vnguyen
 */
public class ScheduleEmailData extends GeneralQueryFormat {

    private String company_id;
    private String txt;
    private int msgType;
    private String phoneNumber;
    private int employee_id;
    private int userSentId;
    private int messaging_mod_id;
    private String uniqueId;

    public ScheduleEmailData(String company_id,String txt,int msgType,String phoneNumber,String employee_id,int userSentId,int messaging_mod_id) {
        this.company_id = company_id;
        this.txt = txt;
        this.msgType = msgType;
        this.phoneNumber = phoneNumber;
        this.employee_id = Integer.parseInt(employee_id);
        this.userSentId =userSentId;
        this.messaging_mod_id = messaging_mod_id;
        try {
            this.setUniqueId(md5Hashed(uniqueId));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ScheduleEmailData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @Override
    public boolean hasAccess() {
        return true;
    }

    public String toString() {
        /*messaging_outbound_id integer NOT NULL DEFAULT nextval('control_db.messaging_outbound_seq'::regclass),
        message_type_id integer NOT NULL,
        createdon timestamp without time zone NOT NULL DEFAULT now(),
        company_id integer NOT NULL,
        sms_text text NOT NULL,
        sms_phone_number character varying(20),
        employee_id integer NOT NULL,
        user_sent_id integer NOT NULL,
        messaging_mod_id integer
         */
        this.setTxt(this.getTxt().replaceAll("'", "''"));
        Calendar calendar = Calendar.getInstance();
        Timestamp currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());
        setUniqueId(null);
        try {
            setUniqueId(md5Hashed(currentTimestamp.toString()));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ScheduleEmailData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "INSERT INTO control_db.messaging_outbound ( message_type_id, company_id," +
                "sms_text, sms_phone_number, employee_id,user_sent_id,messaging_mod_id, md5 )" +
                "VALUES(" + this.getMsgType() + "," + this.getCompany_id() + "," + this.getTxt() +
                "|| (SELECT currval('control_db.messaging_outbound_seq') from control_db.messaging_outbound)," +
                this.getPhoneNumber() + "," + this.getEmployee_id() + "," + this.getUserSentId() + "," + this.getMessaging_mod_id() + "," +
                getUniqueId() + " )";

    }

    private static String md5Hashed(String val) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        md.update(val.getBytes());
        byte[] digest = md.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashedTxt = bigInt.toString(16);
        while (hashedTxt.length() < 32) {
            hashedTxt = "0" + hashedTxt;
        }
        return hashedTxt;
    }

    /**
     * @return the company_id
     */
    public String getCompany_id() {
        return company_id;
    }

    /**
     * @param company_id the company_id to set
     */
    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    /**
     * @return the txt
     */
    public String getTxt() {
        return txt;
    }

    /**
     * @param txt the txt to set
     */
    public void setTxt(String txt) {
        this.txt = txt;
    }

    /**
     * @return the msgType
     */
    public int getMsgType() {
        return msgType;
    }

    /**
     * @param msgType the msgType to set
     */
    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the employee_id
     */
    public int getEmployee_id() {
        return employee_id;
    }

    /**
     * @param employee_id the employee_id to set
     */
    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    /**
     * @return the userSentId
     */
    public int getUserSentId() {
        return userSentId;
    }

    /**
     * @param userSentId the userSentId to set
     */
    public void setUserSentId(int userSentId) {
        this.userSentId = userSentId;
    }

    /**
     * @return the messaging_mod_id
     */
    public int getMessaging_mod_id() {
        return messaging_mod_id;
    }

    /**
     * @param messaging_mod_id the messaging_mod_id to set
     */
    public void setMessaging_mod_id(int messaging_mod_id) {
        this.messaging_mod_id = messaging_mod_id;
    }

    /**
     * @return the uniqueId
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * @param uniqueId the uniqueId to set
     */
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
