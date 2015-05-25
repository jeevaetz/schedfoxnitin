/*
 * xManagementForm.java
 *
 * Created on September 7, 2005, 10:32 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.xadmin;
import schedfoxlib.model.util.Record_Set;
import rmischedule.data_connection.*;
import rmischedule.main.Main_Window;
import rmischeduleserver.data_connection_types.*;
import rmischeduleserver.mysqlconnectivity.queries.util.*;
import javax.swing.*;
import rmischedule.components.graphicalcomponents.GenericTabbedEditForm;

/**
 *
 * @author Ira Juneau
 */
public class xManagementForm extends GenericTabbedEditForm {
    
    protected Main_Window parent;
    protected String newClientSQL = "(SELECT (max(management_id) + 1) FROM management_clients)";
    protected boolean showDeleted;
    protected boolean showSchedFoxCust=false;
    /** Creates a new instance of xManagementForm */
    public xManagementForm() {
        parent = Main_Window.parentOfApplication;
        showDeleted = false;
        super.addSubForm(new xManagementDetail());
        super.addSubForm(new xManagementCompanyEdit(this));
        super.addSubForm(new xManagementBranchEdit(this));
    }

    public void getData() {
        get_available_management_co_query myQuery = new get_available_management_co_query();
        myQuery.update(showDeleted);
        myQuery.showSchedfoxCust(showSchedFoxCust);
        Record_Set rs = new Record_Set();
        try {
            rs = new Connection().executeQuery(myQuery);
        } catch (Exception e) {}
        super.populateList(rs, "isdeleted", "t");
    }

    protected void showDeleted(boolean isPressed) {
        if (isPressed) {
            showDeleted = true;
        } else {
            showDeleted = false;
        }
        getData();
    }
    public boolean getToggleDeleted() {
        return true;
    }
    
    protected ImageIcon getDeletedUpIcon() {
        return Main_Window.Viewing_Active_Clients;
    }
    
    protected ImageIcon getDeletedDownIcon() {
        return Main_Window.Viewing_All_Clients;
    }
    
    public Connection getConnection() {
        return new Connection();
    }
    
    public String getDisplayNameForObject(Object o) {
        return ((ManagementData)o).Name;
    }
    
    public String getMyIdForSave() {
        if (currentSelectedObject == null) {
            return "0";
        }
        return ((ManagementData)currentSelectedObject).Id;
    }
    
    public Object createObjectForList(Record_Set input) {
        return new ManagementData(input.getString("management_id"), input.getString("management_client_name"));
    }
    
    public void addMyMenu(JMenuBar myMenu) {}

    public String getWindowTitle() {
        if (currentSelectedObject == null) {
            return "Adding New Management Company";
        }
        return "Editing Information For " + getDisplayNameForObject(currentSelectedObject);
    }
    
    /**
     * Overloaded to allow the server to reload all client db information if a new Management
     * company has been loaded...need to have it create db too... fun fun...
     */
    public void saveData() {
        super.saveData();
        new Connection().relaodClientInfoOnServer();
    }

    public void deleteData() {
        if(this.currentSelectedObject == null)
            return;
        
        if(this.selectedIsMarkedDeleted()) {
            if(JOptionPane.showConfirmDialog(this, "Are you SURE you want to delete this management company? (THIS CANNONT BE UNDONE!)", "Confirm Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                Record_Set dbList;
                GenericQuery dbQuery = new GenericQuery("SELECT company_db FROM control_db.company WHERE company_management_id = " + getMyIdForSave());
                try {
                    dbList = getConnection().executeQuery(dbQuery);
                    for(int i = 0; i < dbList.length(); i++) {
                        String companyDB = dbList.getString("company_db");
                        dbList.moveNext();
                        if(companyDB.length() != 1 || Character.getNumericValue(companyDB.charAt(0)) != -1) {
                            try { getConnection().executeUpdate(new GenericQuery("DROP SCHEMA " + companyDB + " CASCADE;")); }
                            catch(Exception ex) { }
                        }
                    }
                }
                catch(Exception ex) { }

                try { getConnection().executeUpdate(new GenericQuery("DELETE FROM control_db.management_clients WHERE management_id = " + getMyIdForSave() + ";")); }
                catch(Exception ex) { }
                getData();
            }
        }
        else
            JOptionPane.showMessageDialog(this, "You can only delete inactive management companies!", "Unable to delete management company", JOptionPane.INFORMATION_MESSAGE);
    }

    
    public class ManagementData {
        public String Name;
        public String Id;
        
        public ManagementData(String id, String name) {
            Name = name;
            Id = id;
        }
        
        public boolean equals(Object o) {
            try { return ((ManagementData)o).Id.equals(this.Id); }
            catch(Exception e) {}
            
            return false;
        }
    }
    
}
