/*
 * User.java
 *
 * Created on February 4, 2004, 8:01 AM
 */

package rmischedule.security;

import schedfoxlib.model.util.Record_Set;
import java.util.ArrayList;
import java.util.HashMap;

import rmischedule.main.Main_Window ;
import rmischedule.data_connection.*;

import schedfoxlib.model.Company;
import rmischedule.main.CompanyBranding;
import rmischeduleserver.data_connection_types.*;
import schedfoxlib.model.Branch;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.security.security_level_for_user_and_groups_query;
import rmischeduleserver.mysqlconnectivity.queries.util.*;
/**
 *
 * @author  jason.allen
 */

public class User {
    
    private String md5;
    private String firstName;
    private String lastName;
    private String userId;
    private String userLogin;
    private String email;
    private Boolean canViewSsn;
    
    private String managementName;
    private String managementAddress1;
    private String managementAddress2;
    
    private HashMap security;
    private ArrayList company;
    private ArrayList branch; 

    private String companyDB;

    private Connection myConn;
    /** Creates a new instance of User */
    public User(){
        
    }    
    
    public User(String userID, String companyDB){
        this.myConn = new Connection();
        this.userId = userID;
        this.companyDB = companyDB;
        
        this.setUserInformation();
        this.setManagementCompanyInformation();

        security = new HashMap();
        Main_Window.mySecurity = new security_detail(userID, companyDB);
        updateSecurity();
    }

    public void setUserInformation() {
        getUserIdQuery myUserIdQuery = new getUserIdQuery();

        myUserIdQuery.update(this.userId);
        Record_Set rs = new Record_Set();
        try {
            rs = myConn.executeQuery(myUserIdQuery);
            firstName = rs.getString("user_first_name");
            lastName  = rs.getString("user_last_name");
            userLogin = rs.getString("user_login");
            md5       = rs.getString("user_md5");
            email     = rs.getString("email");
            canViewSsn = rs.getBoolean("can_view_ssn");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This method sets the management company information for this user object.
     */
    public void setManagementCompanyInformation() {
        if (Main_Window.compBranding.getLoginType().equals(CompanyBranding.LoginType.USER)) {
            get_available_management_co_query myManagementCo = new get_available_management_co_query();
            myManagementCo.update(Main_Window.parentOfApplication.getManagementId());
            try {
                Record_Set manage = new Record_Set();
                manage = myConn.executeQuery(myManagementCo);
                Main_Window.parentOfApplication.setManagementId(manage.getString("management_id"));
                managementName = manage.getString("management_client_name");
                managementAddress1 = manage.getString("address");
                managementAddress2 = manage.getString("city") + ", " + manage.getString("state") + " " + manage.getString("zip");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            managementName = "";
            managementAddress1 = "";
            managementAddress2 = "";
        }
    }

    public String getManageName() {
        return managementName;
    }
    
    public String getManageAddress1() {
        return managementAddress1;
    }
    
    public String getManageAddress2() {
        return managementAddress2;
    }
    
    public void   setMD5(String MD5){md5 = MD5;}
    
    public String getUserId()   {
        
        return userId;
    }
    public String getMD5()      {return md5;}
    public String getFirstName(){return firstName;}
    public String getLastName() {return lastName;}
    public String getLogin()    {return (userLogin == null) ? "unkown" : userLogin;}
    
    /*
     * The following allow for us to determine access by passing access_id
     */
    public boolean canRead(int aid){
        if(!security.containsKey(aid)){
            return false;
        }
        return ((security_level)security.get(aid)).read;
    }
    
    public boolean canAdd(int aid){
        if(!security.containsKey(aid)){
            return false;
        }
        return ((security_level)security.get(aid)).add;
    }
    
    public boolean canModify(int aid){
        if(!security.containsKey(aid)){
            return false;
        }
        return ((security_level)security.get(aid)).modify;
    }

    public boolean canDelete(int aid){
        if(!security.containsKey(aid)){
            return false;
        }
        return ((security_level)security.get(aid)).delete;
    }
    
    public void updateSecurity(){
        ArrayList myAl = null;        
        GeneralQueryFormat branchQuery = null;
        GeneralQueryFormat companyQuery = null;
        GeneralQueryFormat securityQuery = null;

        if (Main_Window.compBranding.getLoginType().equals(CompanyBranding.LoginType.USER)) {
            branchQuery = new branch_list_query();
            companyQuery = new company_list_query();
            securityQuery = new security_level_for_user_and_groups_query();

            ((branch_list_query)branchQuery).update(this.userId);
            ((company_list_query)companyQuery).update(this.userId);
            ((security_level_for_user_and_groups_query)securityQuery).update(this.userId);
        } else if (Main_Window.compBranding.getLoginType().equals(CompanyBranding.LoginType.EMPLOYEE)) {
            branchQuery = new employee_branch_list_query();
            companyQuery = new employee_company_list_query();
            securityQuery = new security_level_for_user_and_groups_query();

            ((employee_branch_list_query)branchQuery).update(this.companyDB, Integer.parseInt(this.userId));
            ((employee_company_list_query)companyQuery).update(this.companyDB);
            ((security_level_for_user_and_groups_query)securityQuery).update(this.userId);
        } else if (Main_Window.compBranding.getLoginType().equals(CompanyBranding.LoginType.CLIENT)) {
            branchQuery = new company_branch_list_query();
            companyQuery = new client_company_list_query();
            securityQuery = new security_level_for_user_and_groups_query();

            ((company_branch_list_query)branchQuery).update(this.companyDB, Integer.parseInt(this.userId));
            ((client_company_list_query)companyQuery).update(this.companyDB);
            ((security_level_for_user_and_groups_query)securityQuery).update(this.userId);
        }
        try{
            getBranch(myConn.executeQuery(branchQuery));
            getCompany(myConn.executeQuery(companyQuery));
            getSecurityLevel(myConn.executeQuery(securityQuery));
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public ArrayList<Company> getCompanies() {
        return company;
    }

    public void getCompany(Record_Set rs){        
        int i = rs.length();      
        company = new ArrayList();
        for(int c=0;c<i;c++){
            company.add(
                new Company(rs)
            );
        }
    }

    public ArrayList<Branch> getBranches() {
        return branch;
    }

    public void getBranch(Record_Set rs){
        int i = rs.length();        
        branch = new ArrayList();
        for(int c=0;c<i;c++){
            branch.add(
                new Branch(rs)
            );
        }
    }
    
    public void getSecurityLevel(Record_Set rs){
        int i = rs.length();        
        for(int c=0;c<i;c++){
            security_level sl = new security_level(rs.getInt("access_id"));
            if(!security.containsKey(sl.level)){
                security.put(sl.level,sl);
            }else{
                security_level sl2 = (security_level)security.get(sl.level);
                
                sl2.add    = sl2.add    || sl.add;
                sl2.read   = sl2.read   || sl.read;
                sl2.modify = sl2.modify || sl.modify;
                sl2.delete = sl2.delete || sl.delete;
            }
            
        }
    }
    
    public boolean checkSecurity(String lvl){
        int c, i;
        
        c = security.size();
        
        for(i=0;i<c;i++){
            if((((String)(security.get(i))).compareTo(lvl)) == 0){
                return true;
            }
        }
        
        return true;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the canViewSsn
     */
    public Boolean getCanViewSsn() {
        return canViewSsn;
    }

    /**
     * @param canViewSsn the canViewSsn to set
     */
    public void setCanViewSsn(Boolean canViewSsn) {
        this.canViewSsn = canViewSsn;
    }

    class security_level{
        public int id;
        public int level;
        
        public boolean read   = false;
        public boolean add    = false;
        public boolean modify = false;
        public boolean delete = false;
        
        public security_level(int sec_id){
            level = getPlace(2,sec_id) + 1;
            id = sec_id - level;
            
            int tl = level;
            
            // delete == 8
            if(tl >= security_detail.ACCESS.DELETE.getVal()){
                delete = true;
                tl -= security_detail.ACCESS.DELETE.getVal();
            }
            
            // modify == 4
            if(tl >= security_detail.ACCESS.MODIFY.getVal()){
                modify = true;           
                tl -= security_detail.ACCESS.MODIFY.getVal();
            }
            
            // add == 2
            if(tl >= security_detail.ACCESS.ADD.getVal()){
                add = true;
                tl -= security_detail.ACCESS.ADD.getVal();
            }
            
            // read == 1
            if(tl == security_detail.ACCESS.READ.getVal()){
                add = true;
                tl -= security_detail.ACCESS.READ.getVal();
            }            
        }

        private int getPlace(int place, int val) {
            return ((val / place) % 10) * place;
        }        
    }       
}
