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
public class AssembleCheckinScheduleType {
    private String sid; //Schedule ID
    private Integer cid; //Client ID
    private String cname; // Client Name
    private String ename; // Employee Name
    private Integer smid;
    private Integer eid; // Employee ID
    private Integer dow; //Day of week
    private Integer start_time;
    private Integer end_time;
    private Date lu; //last updated
    private Date sdate;
    private Date edate;
    private BigDecimal gp;
    private String pay_opt;
    private String bill_opt;
    private Integer rate_code_id;
    private Date date;
    private Integer trainerid;
    private Integer branch_id;
    private Integer isdeleted;
    private Integer type;
    private String emp_first_name;
    private String emp_last_name;
    private String emp_phone;
    private String emp_cell;
    private String emp_address;
    private String emp_address2;
    private String emp_city;
    private String emp_state;
    private String emp_zip;
    private String client_name;
    private boolean checkComm;
    private String client_phone;
    private String client_phone2;
    private String client_address;
    private String client_address2;
    private String client_city;
    private String client_state;
    private String client_zip;
    private String person_checked_in;
    private String person_checked_out;
    private Integer check_out_option_id;
    private boolean checkedIn;
    private boolean checkedOut;
    
    private String[] allowedCommunication;

    private String companyId;

    public AssembleCheckinScheduleType() {
        
    }
    
    public AssembleCheckinScheduleType(Record_Set rst) {
        try {
            sid = rst.getString("sid");
        } catch (Exception e) {}
        try {
            cid = rst.getInt("cid");
        } catch (Exception e) {}
        try {
            cname = rst.getString("cname");
        } catch (Exception e) {}
        try {
            ename = rst.getString("ename");
        } catch (Exception e) {}
        try {
            smid = rst.getInt("smid");
        } catch (Exception e) {}
        try {
            eid = rst.getInt("eid");
        } catch (Exception e) {}
        try {
            dow = rst.getInt("dow");
        } catch (Exception e) {}
        try {
            start_time = rst.getInt("start_time");
        } catch (Exception e) {}
        try {
            end_time = rst.getInt("end_time");
        } catch (Exception e) {}
        try {
            lu = rst.getDate("lu");
        } catch (Exception e) {}
        try {
            sdate = rst.getDate("sdate");
        } catch (Exception e) {}
        try {
            edate = rst.getDate("edate");
        } catch (Exception e) {}
        try {
            gp = rst.getBigDecimal("gp");
        } catch (Exception e) {}
        try {
            pay_opt = rst.getString("pay_opt");
        } catch (Exception e) {}
        try {
            bill_opt = rst.getString("bill_opt");
        } catch (Exception e) {}
        try {
            rate_code_id = rst.getInt("rate_code_id");
        } catch (Exception e) {}
        try {
            date = rst.getDate("date");
        } catch (Exception e) {}
        try {
            trainerid = rst.getInt("trainerid");
        } catch (Exception e) {}
        try {
            branch_id = rst.getInt("branch_id");
        } catch (Exception e) {}
        try {
            isdeleted = rst.getInt("isdeleted");
        } catch (Exception e) {}
        try {
            type = rst.getInt("type");
        } catch (Exception e) {}
        try {
            emp_first_name = rst.getString("emp_first_name");
        } catch (Exception e) {}
        try {
            emp_last_name = rst.getString("emp_last_name");
        } catch (Exception e) {}
        try {
            emp_phone = rst.getString("emp_phone");
        } catch (Exception e) {}
        try {
            emp_cell = rst.getString("emp_cell");
        } catch (Exception e) {}
        try {
            emp_address = rst.getString("emp_address");
        } catch (Exception e) {}
        try {
            emp_address2 = rst.getString("emp_address2");
        } catch (Exception e) {}
        try {
            emp_city = rst.getString("emp_city");
        } catch (Exception e) {}
        try {
            emp_state = rst.getString("emp_state");
        } catch (Exception e) {}
        try {
            client_name = rst.getString("client_name");
        } catch (Exception e) {}
        try {
            client_phone = rst.getString("client_phone");
        } catch (Exception e) {}
        try {
            client_phone2 = rst.getString("client_phone2");
        } catch (Exception e) {}
        try {
            client_address = rst.getString("client_address");
        } catch (Exception e) {}
        try {
            client_address2 = rst.getString("client_address2");
        } catch (Exception e) {}
        try {
            client_city = rst.getString("client_city");
        } catch (Exception e) {}
        try {
            client_state = rst.getString("client_state");
        } catch (Exception e) {}
        try {
            client_zip = rst.getString("client_zip");
        } catch (Exception e) {}
        try {
            person_checked_in = rst.getString("person_checked_in");
        } catch (Exception e) {}
        try {
            person_checked_out = rst.getString("person_checked_out");
        } catch (Exception e) {}
        try {
            check_out_option_id = rst.getInt("check_out_option_id");
        } catch (Exception e) {}
    }

    /**
     * @return the sid
     */
    public String getSid() {
        return sid;
    }

    /**
     * @param sid the sid to set
     */
    public void setSid(String sid) {
        this.sid = sid;
    }

    /**
     * @return the cid
     */
    public Integer getCid() {
        return cid;
    }

    /**
     * @param cid the cid to set
     */
    public void setCid(Integer cid) {
        this.cid = cid;
    }

    /**
     * @return the cname
     */
    public String getCname() {
        return cname;
    }

    /**
     * @param cname the cname to set
     */
    public void setCname(String cname) {
        this.cname = cname;
    }

    /**
     * @return the ename
     */
    public String getEname() {
        return ename;
    }

    /**
     * @param ename the ename to set
     */
    public void setEname(String ename) {
        this.ename = ename;
    }

    /**
     * @return the smid
     */
    public Integer getSmid() {
        return smid;
    }

    /**
     * @param smid the smid to set
     */
    public void setSmid(Integer smid) {
        this.smid = smid;
    }

    /**
     * @return the eid
     */
    public Integer getEid() {
        return eid;
    }

    /**
     * @param eid the eid to set
     */
    public void setEid(Integer eid) {
        this.eid = eid;
    }

    /**
     * @return the dow
     */
    public Integer getDow() {
        return dow;
    }

    /**
     * @param dow the dow to set
     */
    public void setDow(Integer dow) {
        this.dow = dow;
    }

    /**
     * @return the start_time
     */
    public Integer getStart_time() {
        return start_time;
    }

    /**
     * @param start_time the start_time to set
     */
    public void setStart_time(Integer start_time) {
        this.start_time = start_time;
    }

    /**
     * @return the end_time
     */
    public Integer getEnd_time() {
        return end_time;
    }

    /**
     * @param end_time the end_time to set
     */
    public void setEnd_time(Integer end_time) {
        this.end_time = end_time;
    }

    /**
     * @return the lu
     */
    public Date getLu() {
        return lu;
    }

    /**
     * @param lu the lu to set
     */
    public void setLu(Date lu) {
        this.lu = lu;
    }

    /**
     * @return the sdate
     */
    public Date getSdate() {
        return sdate;
    }

    /**
     * @param sdate the sdate to set
     */
    public void setSdate(Date sdate) {
        this.sdate = sdate;
    }

    /**
     * @return the edate
     */
    public Date getEdate() {
        return edate;
    }

    /**
     * @param edate the edate to set
     */
    public void setEdate(Date edate) {
        this.edate = edate;
    }

    /**
     * @return the gp
     */
    public BigDecimal getGp() {
        return gp;
    }

    /**
     * @param gp the gp to set
     */
    public void setGp(BigDecimal gp) {
        this.gp = gp;
    }

    /**
     * @return the pay_opt
     */
    public String getPay_opt() {
        return pay_opt;
    }

    /**
     * @param pay_opt the pay_opt to set
     */
    public void setPay_opt(String pay_opt) {
        this.pay_opt = pay_opt;
    }

    /**
     * @return the bill_opt
     */
    public String getBill_opt() {
        return bill_opt;
    }

    /**
     * @param bill_opt the bill_opt to set
     */
    public void setBill_opt(String bill_opt) {
        this.bill_opt = bill_opt;
    }

    /**
     * @return the rate_code_id
     */
    public Integer getRate_code_id() {
        return rate_code_id;
    }

    /**
     * @param rate_code_id the rate_code_id to set
     */
    public void setRate_code_id(Integer rate_code_id) {
        this.rate_code_id = rate_code_id;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the trainerid
     */
    public Integer getTrainerid() {
        return trainerid;
    }

    /**
     * @param trainerid the trainerid to set
     */
    public void setTrainerid(Integer trainerid) {
        this.trainerid = trainerid;
    }

    /**
     * @return the branch_id
     */
    public Integer getBranch_id() {
        return branch_id;
    }

    /**
     * @param branch_id the branch_id to set
     */
    public void setBranch_id(Integer branch_id) {
        this.branch_id = branch_id;
    }

    /**
     * @return the isdeleted
     */
    public Integer getIsdeleted() {
        return isdeleted;
    }

    /**
     * @param isdeleted the isdeleted to set
     */
    public void setIsdeleted(Integer isdeleted) {
        this.isdeleted = isdeleted;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the emp_first_name
     */
    public String getEmp_first_name() {
        return emp_first_name;
    }

    /**
     * @param emp_first_name the emp_first_name to set
     */
    public void setEmp_first_name(String emp_first_name) {
        this.emp_first_name = emp_first_name;
    }

    /**
     * @return the emp_last_name
     */
    public String getEmp_last_name() {
        return emp_last_name;
    }

    /**
     * @param emp_last_name the emp_last_name to set
     */
    public void setEmp_last_name(String emp_last_name) {
        this.emp_last_name = emp_last_name;
    }

    /**
     * @return the emp_phone
     */
    public String getEmp_phone() {
        return emp_phone;
    }

    /**
     * @param emp_phone the emp_phone to set
     */
    public void setEmp_phone(String emp_phone) {
        this.emp_phone = emp_phone;
    }

    /**
     * @return the emp_cell
     */
    public String getEmp_cell() {
        return emp_cell;
    }

    /**
     * @param emp_cell the emp_cell to set
     */
    public void setEmp_cell(String emp_cell) {
        this.emp_cell = emp_cell;
    }

    /**
     * @return the emp_address
     */
    public String getEmp_address() {
        return emp_address;
    }

    /**
     * @param emp_address the emp_address to set
     */
    public void setEmp_address(String emp_address) {
        this.emp_address = emp_address;
    }

    /**
     * @return the emp_address2
     */
    public String getEmp_address2() {
        return emp_address2;
    }

    /**
     * @param emp_address2 the emp_address2 to set
     */
    public void setEmp_address2(String emp_address2) {
        this.emp_address2 = emp_address2;
    }

    /**
     * @return the emp_city
     */
    public String getEmp_city() {
        return emp_city;
    }

    /**
     * @param emp_city the emp_city to set
     */
    public void setEmp_city(String emp_city) {
        this.emp_city = emp_city;
    }

    /**
     * @return the emp_state
     */
    public String getEmp_state() {
        return emp_state;
    }

    /**
     * @param emp_state the emp_state to set
     */
    public void setEmp_state(String emp_state) {
        this.emp_state = emp_state;
    }

    /**
     * @return the emp_zip
     */
    public String getEmp_zip() {
        return emp_zip;
    }

    /**
     * @param emp_zip the emp_zip to set
     */
    public void setEmp_zip(String emp_zip) {
        this.emp_zip = emp_zip;
    }

    /**
     * @return the client_name
     */
    public String getClient_name() {
        return client_name;
    }

    /**
     * @param client_name the client_name to set
     */
    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    /**
     * @return the client_phone
     */
    public String getClient_phone() {
        return client_phone;
    }

    /**
     * @param client_phone the client_phone to set
     */
    public void setClient_phone(String client_phone) {
        this.client_phone = client_phone;
    }

    /**
     * @return the client_phone2
     */
    public String getClient_phone2() {
        return client_phone2;
    }

    /**
     * @param client_phone2 the client_phone2 to set
     */
    public void setClient_phone2(String client_phone2) {
        this.client_phone2 = client_phone2;
    }

    /**
     * @return the client_address
     */
    public String getClient_address() {
        return client_address;
    }

    /**
     * @param client_address the client_address to set
     */
    public void setClient_address(String client_address) {
        this.client_address = client_address;
    }

    /**
     * @return the client_address2
     */
    public String getClient_address2() {
        return client_address2;
    }

    /**
     * @param client_address2 the client_address2 to set
     */
    public void setClient_address2(String client_address2) {
        this.client_address2 = client_address2;
    }

    /**
     * @return the client_city
     */
    public String getClient_city() {
        return client_city;
    }

    /**
     * @param client_city the client_city to set
     */
    public void setClient_city(String client_city) {
        this.client_city = client_city;
    }

    /**
     * @return the client_state
     */
    public String getClient_state() {
        return client_state;
    }

    /**
     * @param client_state the client_state to set
     */
    public void setClient_state(String client_state) {
        this.client_state = client_state;
    }

    /**
     * @return the client_zip
     */
    public String getClient_zip() {
        return client_zip;
    }

    /**
     * @param client_zip the client_zip to set
     */
    public void setClient_zip(String client_zip) {
        this.client_zip = client_zip;
    }

    /**
     * @return the person_checked_in
     */
    public String getPerson_checked_in() {
        return person_checked_in;
    }

    /**
     * @param person_checked_in the person_checked_in to set
     */
    public void setPerson_checked_in(String person_checked_in) {
        this.person_checked_in = person_checked_in;
    }

    /**
     * @return the companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the person_checked_out
     */
    public String getPerson_checked_out() {
        return person_checked_out;
    }

    /**
     * @param person_checked_out the person_checked_out to set
     */
    public void setPerson_checked_out(String person_checked_out) {
        this.person_checked_out = person_checked_out;
    }

    /**
     * @return the check_out_option_id
     */
    public Integer getCheck_out_option_id() {
        return check_out_option_id;
    }

    /**
     * @param check_out_option_id the check_out_option_id to set
     */
    public void setCheck_out_option_id(Integer check_out_option_id) {
        this.check_out_option_id = check_out_option_id;
    }

    /**
     * @return the checkedIn
     */
    public boolean isCheckedIn() {
        if(!person_checked_in.equals("0") && !person_checked_in.equals("")){
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param checkedIn the checkedIn to set
     */
    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    /**
     * @return the checkedOut
     */
    public boolean isCheckedOut() {
        if(!person_checked_out.equals("0") && !person_checked_out.equals("")){
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param checkedOut the checkedOut to set
     */
    public void setCheckedOut(boolean checkedOut) {

        this.checkedOut = checkedOut;
    }

    /**
     * @return the cidCheck
     */
    public boolean isCheckComm() {
        return checkComm;
    }

    /**
     * @param cidCheck the cidCheck to set
     */
    public void setCheckComm(boolean checkComm) {
        this.checkComm = checkComm;
    }

    /**
     * @return the allowedCommunication
     */
    public String[] getAllowedCommunication() {
        return allowedCommunication;
    }

    /**
     * @param allowedCommunication the allowedCommunication to set
     */
    public void setAllowedCommunication(String[] allowedCommunication) {
        this.allowedCommunication = allowedCommunication;
    }
}
