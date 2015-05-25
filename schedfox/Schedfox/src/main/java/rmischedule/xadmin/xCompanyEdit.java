/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.xadmin;

import javax.swing.JMenuBar;
import rmischedule.components.graphicalcomponents.GenericTabbedEditForm;
import rmischedule.data_connection.Connection;
import schedfoxlib.model.Company;
import rmischedule.main.Main_Window;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.admin.get_companies_accessible_for_management_co_query;

/**
 *
 * @author user
 */
public class xCompanyEdit extends GenericTabbedEditForm {

    public xCompanyEdit() {
        super();
        super.addSubForm(new CompanyLoginInformation(this));
        super.addSubForm(new CompanyDynamicFields(this));
    }

    @Override
    public String getWindowTitle() {
        return "Edit Company Information";
    }

    @Override
    public Connection getConnection() {
        Connection myConn = new Connection();
        myConn.setCompany(this.getMyIdForSave());
        return myConn;
    }

    @Override
    public void getData() {
        get_companies_accessible_for_management_co_query myListQuery
                = new get_companies_accessible_for_management_co_query();
        Record_Set rs = new Record_Set();
        try {
            myListQuery.update(Integer.parseInt(Main_Window.parentOfApplication.myUser.getUserId()));
            getConnection().prepQuery(myListQuery);
            rs = getConnection().executeQuery(myListQuery);
            super.populateList(rs, "is_deleted", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDisplayNameForObject(Object o) {
        Company myCompany = (Company)o;
        return myCompany.getName();
    }

    @Override
    public Object createObjectForList(Record_Set input) {
        return new Company(input);
    }

    @Override
    public void addMyMenu(JMenuBar myMenu) {
        
    }

    @Override
    public String getMyIdForSave() {
        if (currentSelectedObject == null) {
            return "0";
        }
        return ((Company)currentSelectedObject).getId();
    }

    @Override
    public void deleteData() {
        
    }
    public void showSchedFoxCust(){
    }

}
