/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.employee.security;

import javax.swing.JMenuBar;
import rmischedule.components.graphicalcomponents.GenericEditForm;
import rmischedule.data_connection.Connection;
import rmischedule.security.security_detail;
import rmischedule.security.security_detail.MODULES;
import rmischedule.security.security_detail.ACCESS;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.employee.security.get_security_for_employees_query;

/**
 *
 * @author user
 */
public class EmployeeSecurityGroups extends GenericEditForm {

    private String companyId; 

    public EmployeeSecurityGroups() {
        super.addSubForm(new EmployeeSecurityPanel(this));
    }

    @Override
    public String getWindowTitle() {
        return "Emloyee Security Groups";
    }

    @Override
    public Connection getConnection() {
        Connection myConn = new Connection();
        myConn.setCompany(companyId);
        return myConn;
    }

    public void setCompany(String id) {
        this.companyId = id;
        get_security_for_employees_query myListQuery = new get_security_for_employees_query();
        Record_Set rs = new Record_Set();
        try {
            Connection conn = this.getConnection();
            conn.prepQuery(myListQuery);
            rs = conn.executeQuery(myListQuery);
        } catch (Exception e) {}
        super.populateList(rs, "employee_security_is_deleted", "1");
    }

    @Override
    public void getData() {
//        try {
//            int id = ((SecurityGroups)currentSelectedObject).getId();
//        } catch (Exception e) {}
//        this.removeAll();
//        for (MODULES m : security_detail.MODULES.values()) {
//            this.add(new IndSecuritySettingPanel(m, 0));
//        }
    }

    @Override
    public String getDisplayNameForObject(Object o) {
        SecurityGroups secGroup = (SecurityGroups)o;

        return secGroup.getSecurityNames();
    }

    @Override
    public Object createObjectForList(Record_Set input) {
        SecurityGroups tempVal = new SecurityGroups();
        tempVal.setId(input.getInt("employee_security_group_id"));
        tempVal.setIsDeleted(input.getBoolean("employee_security_is_deleted"));
        tempVal.setSecurityNames(input.getString("employee_security_group_name"));
        return tempVal;
    }

    @Override
    public void addMyMenu(JMenuBar myMenu) {
        
    }

    @Override
    public String getMyIdForSave() {
        if (currentSelectedObject == null) {
            return "0";
        }
        return ((SecurityGroups)currentSelectedObject).getId()+ "";
    }

    @Override
    public void deleteData() {
        
    }

    public class SecurityGroups {
        private int id;
        private String securityNames;
        private boolean isDeleted;

        public SecurityGroups() {
            securityNames = new String();
            isDeleted = false;
            id = 0;
        }

        /**
         * @return the id
         */
        public int getId() {
            return id;
        }

        /**
         * @param id the id to set
         */
        public void setId(int id) {
            this.id = id;
        }

        /**
         * @return the securityNames
         */
        public String getSecurityNames() {
            return securityNames;
        }

        /**
         * @param securityNames the securityNames to set
         */
        public void setSecurityNames(String securityNames) {
            this.securityNames = securityNames;
        }

        /**
         * @return the isDeleted
         */
        public boolean isIsDeleted() {
            return isDeleted;
        }

        /**
         * @param isDeleted the isDeleted to set
         */
        public void setIsDeleted(boolean isDeleted) {
            this.isDeleted = isDeleted;
        }
    }

}
