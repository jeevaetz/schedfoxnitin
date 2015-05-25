/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.util.Vector;
import schedfoxlib.model.util.Record_Set;

/**
 * Class to encapsulate Company/Branch relationship, many to one deal fun fun...
 */
public class Company implements Serializable {

    private String compId;
    private String compName;
    private String compDB;
    private Boolean alertLateCheckin;
    private Vector<Branch> myBranchList;

    public Company() {
        
    }
    
    public Company(Record_Set rs) {
        compId = rs.getString("company_id");
        compName = rs.getString("company_name");
        compDB = rs.getString("company_db");
        try {
            alertLateCheckin = rs.getBoolean("alert_late_checkin");
        } catch (Exception exe) {}
        myBranchList = new Vector<Branch>();
    }

    public String getId() {
        return getCompId();
    }
    
    public void setId(String id) {
        this.setCompId(id);
    }

    public String getName() {
        return getCompName();
    }
    
    public void setName(String name) {
        this.setCompName(name);
    }

    public String getDB() {
        return getCompDB();
    }

    public void addBranch(Branch newBranch) {
        getMyBranchList().add(newBranch);
    }

    public Vector<Branch> getBranches() {
        return getMyBranchList();
    }
    
    public void setBranches(Vector<Branch> branches) {
        this.setMyBranchList(branches);
    }
    
    /*  addition by Jeffrey Davis because you always override toString  */
    @Override
    public String toString()
    {
        return this.getCompName();
    }

    /**
     * @return the compId
     */
    public String getCompId() {
        return compId;
    }

    /**
     * @param compId the compId to set
     */
    public void setCompId(String compId) {
        this.compId = compId;
    }

    /**
     * @return the compName
     */
    public String getCompName() {
        return compName;
    }

    /**
     * @param compName the compName to set
     */
    public void setCompName(String compName) {
        this.compName = compName;
    }

    /**
     * @return the compDB
     */
    public String getCompDB() {
        return compDB;
    }

    /**
     * @param compDB the compDB to set
     */
    public void setCompDB(String compDB) {
        this.compDB = compDB;
    }

    /**
     * @return the myBranchList
     */
    public Vector<Branch> getMyBranchList() {
        return myBranchList;
    }

    /**
     * @param myBranchList the myBranchList to set
     */
    public void setMyBranchList(Vector<Branch> myBranchList) {
        this.myBranchList = myBranchList;
    }

    /**
     * @return the alertLateCheckin
     */
    public Boolean getAlertLateCheckin() {
        return alertLateCheckin;
    }

    /**
     * @param alertLateCheckin the alertLateCheckin to set
     */
    public void setAlertLateCheckin(Boolean alertLateCheckin) {
        this.alertLateCheckin = alertLateCheckin;
    }
         
}
