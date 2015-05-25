/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.math.BigDecimal;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class ManagementClient {
    private Integer management_id;
    private String management_client_name;
    private String management_client_address;
    private String management_client_address2;
    private String management_client_city;
    private String management_client_state;
    private String management_client_zip;
    private String management_client_phone;
    private String management_client_email;
    private boolean management_is_deleted;
    private Date management_date_started;
    private BigDecimal amount_to_bill;
    private String bill_interval;
    private Date bill_start_date;
    private BigDecimal amount_per_employee;
    private String management_billing_email1;
    private String management_billing_email2;

    public ManagementClient() {
        
    }

    public ManagementClient(Record_Set rs) {
        management_id = rs.getInt("management_id");
        management_client_name = rs.getString("management_client_name");
        if (rs.hasColumn("management_client_address")) {
            management_client_address = rs.getString("management_client_address");
        } else {
            management_client_address = rs.getString("address");
        }
        if (rs.hasColumn("management_client_address2")) {
            management_client_address2 = rs.getString("management_client_address2");
        } else {
            management_client_address2 = rs.getString("address2");
        }
        if (rs.hasColumn("management_client_city")) {
            management_client_city = rs.getString("management_client_city");
        } else {
            management_client_city = rs.getString("city");
        }
        if (rs.hasColumn("management_client_state")) {
            management_client_state = rs.getString("management_client_state");
        } else {
            management_client_state = rs.getString("state");
        }
        if (rs.hasColumn("management_client_zip")) {
            management_client_zip = rs.getString("management_client_zip");
        } else {
            management_client_zip = rs.getString("zip");
        }
        if (rs.hasColumn("management_client_phone")) {
            management_client_phone = rs.getString("management_client_phone");
        } else {
            management_client_phone = rs.getString("phone");
        }
        if (rs.hasColumn("management_client_email")) {
            management_client_email = rs.getString("management_client_email");
        } else {
            management_client_email = rs.getString("email");
        }
        if (rs.hasColumn("management_billing_email1")) {
            management_billing_email1 = rs.getString("management_billing_email1");
        }
        if (rs.hasColumn("management_billing_email2")) {
            management_billing_email2 = rs.getString("management_billing_email2");
        }
        try {
            if (rs.hasColumn("management_is_deleted")) {
                management_is_deleted = rs.getBoolean("management_is_deleted");
            } else {
                management_is_deleted = rs.getBoolean("isdeleted");
            }
        } catch (Exception e) {}
        try {
            management_date_started = rs.getDate("management_date_started");
        } catch (Exception e) {}
        amount_to_bill = rs.getBigDecimal("amount_to_bill");
        bill_interval = rs.getString("bill_interval");
        try {
            this.amount_per_employee = rs.getBigDecimal("amount_per_employee");
        } catch (Exception e) {
            this.amount_per_employee = new BigDecimal(0);
        }
        try {
            bill_start_date = rs.getDate("bill_start_date");
        } catch (Exception e) {}
    }

    /**
     * @return the management_id
     */
    public Integer getManagement_id() {
        return management_id;
    }

    /**
     * @param management_id the management_id to set
     */
    public void setManagement_id(Integer management_id) {
        this.management_id = management_id;
    }

    /**
     * @return the management_client_name
     */
    public String getManagement_client_name() {
        return management_client_name;
    }

    /**
     * @param management_client_name the management_client_name to set
     */
    public void setManagement_client_name(String management_client_name) {
        this.management_client_name = management_client_name;
    }

    /**
     * @return the management_client_address
     */
    public String getManagement_client_address() {
        return management_client_address;
    }

    /**
     * @param management_client_address the management_client_address to set
     */
    public void setManagement_client_address(String management_client_address) {
        this.management_client_address = management_client_address;
    }

    /**
     * @return the management_client_address2
     */
    public String getManagement_client_address2() {
        return management_client_address2;
    }

    /**
     * @param management_client_address2 the management_client_address2 to set
     */
    public void setManagement_client_address2(String management_client_address2) {
        this.management_client_address2 = management_client_address2;
    }

    /**
     * @return the management_client_city
     */
    public String getManagement_client_city() {
        return management_client_city;
    }

    /**
     * @param management_client_city the management_client_city to set
     */
    public void setManagement_client_city(String management_client_city) {
        this.management_client_city = management_client_city;
    }

    /**
     * @return the management_client_state
     */
    public String getManagement_client_state() {
        return management_client_state;
    }

    /**
     * @param management_client_state the management_client_state to set
     */
    public void setManagement_client_state(String management_client_state) {
        this.management_client_state = management_client_state;
    }

    /**
     * @return the management_client_zip
     */
    public String getManagement_client_zip() {
        return management_client_zip;
    }

    /**
     * @param management_client_zip the management_client_zip to set
     */
    public void setManagement_client_zip(String management_client_zip) {
        this.management_client_zip = management_client_zip;
    }

    /**
     * @return the management_client_phone
     */
    public String getManagement_client_phone() {
        return management_client_phone;
    }

    /**
     * @param management_client_phone the management_client_phone to set
     */
    public void setManagement_client_phone(String management_client_phone) {
        this.management_client_phone = management_client_phone;
    }

    /**
     * @return the management_client_email
     */
    public String getManagement_client_email() {
        return management_client_email;
    }

    /**
     * @param management_client_email the management_client_email to set
     */
    public void setManagement_client_email(String management_client_email) {
        this.management_client_email = management_client_email;
    }

    /**
     * @return the management_is_deleted
     */
    public boolean isManagement_is_deleted() {
        return management_is_deleted;
    }

    /**
     * @param management_is_deleted the management_is_deleted to set
     */
    public void setManagement_is_deleted(boolean management_is_deleted) {
        this.management_is_deleted = management_is_deleted;
    }

    /**
     * @return the management_date_started
     */
    public Date getManagement_date_started() {
        return management_date_started;
    }

    /**
     * @param management_date_started the management_date_started to set
     */
    public void setManagement_date_started(Date management_date_started) {
        this.management_date_started = management_date_started;
    }

    /**
     * @return the amount_to_bill
     */
    public BigDecimal getAmount_to_bill() {
        return amount_to_bill;
    }

    /**
     * @param amount_to_bill the amount_to_bill to set
     */
    public void setAmount_to_bill(BigDecimal amount_to_bill) {
        this.amount_to_bill = amount_to_bill;
    }

    /**
     * @return the bill_interval
     */
    public String getBill_interval() {
        if (bill_interval == null || bill_interval.equals("")) {
            bill_interval = "1 mon";
        }
        return bill_interval;
    }

    /**
     * @param bill_interval the bill_interval to set
     */
    public void setBill_interval(String bill_interval) {
        this.bill_interval = bill_interval;
    }

    /**
     * @return the bill_start_date
     */
    public Date getBill_start_date() {
        return bill_start_date;
    }

    /**
     * @param bill_start_date the bill_start_date to set
     */
    public void setBill_start_date(Date bill_start_date) {
        this.bill_start_date = bill_start_date;
    }

    public String toString() {
        return this.management_client_name;
    }

    /**
     * @return the amount_per_employee
     */
    public BigDecimal getAmount_per_employee() {
        return amount_per_employee;
    }

    /**
     * @param amount_per_employee the amount_per_employee to set
     */
    public void setAmount_per_employee(BigDecimal amount_per_employee) {
        this.amount_per_employee = amount_per_employee;
    }

    /**
     * @return the management_billing_email1
     */
    public String getManagement_billing_email1() {
        return management_billing_email1;
    }

    /**
     * @param management_billing_email1 the management_billing_email1 to set
     */
    public void setManagement_billing_email1(String management_billing_email1) {
        this.management_billing_email1 = management_billing_email1;
    }

    /**
     * @return the management_billing_email2
     */
    public String getManagement_billing_email2() {
        return management_billing_email2;
    }

    /**
     * @param management_billing_email2 the management_billing_email2 to set
     */
    public void setManagement_billing_email2(String management_billing_email2) {
        this.management_billing_email2 = management_billing_email2;
    }
}
