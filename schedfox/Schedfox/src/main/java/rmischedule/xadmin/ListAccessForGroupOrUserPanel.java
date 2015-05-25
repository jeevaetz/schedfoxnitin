/*
 * ListAccessForGroupOrUserPanel.java
 *
 * Created on September 5, 2005, 5:06 PM
 */

package rmischedule.xadmin;
import schedfoxlib.model.util.Record_Set;
import rmischedule.components.*;
import rmischedule.security.*;
import rmischeduleserver.data_connection_types.*;
import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.mysqlconnectivity.queries.util.*;

import javax.swing.*;
import java.util.*;
/**
 *
 * @author  Owner
 */
public class ListAccessForGroupOrUserPanel extends javax.swing.JPanel implements UserPanelInterface {
    
    private List_View listOfSecurities;
    private boolean isGroup;
    private int userorgroupid;
    private String ManageId;
    
    /** Creates new form ListAccessForGroupOrUserPanel */
    public ListAccessForGroupOrUserPanel(boolean isGroupList) {
        initComponents();
        ManageId = "0";
        isGroup = isGroupList;
        listOfSecurities = new List_View();
        listOfSecurities.addColumn("Area", List_View.STRING, true, true, 200);
        for (security_detail.ACCESS a : security_detail.ACCESS.values()) {
            listOfSecurities.addColumn(a.getDisp(), List_View.BOOLEAN, true, true);
        }
        listOfSecurities.buildTable();
        listOfSecurities.maximizeTable();
        add(listOfSecurities.myScrollPane);
        userorgroupid = 0;
    }
    
    /**
     * Returns Query, as defined in the interface UserPanelInterface...
     */
    public GeneralQueryFormat getQuery(String manageId, String usergroupId) {
        userorgroupid = Integer.parseInt(usergroupId);
        ManageId = manageId;
        get_group_access_security_query myQuery = new get_group_access_security_query();
        myQuery.update(usergroupId);
        return myQuery;
    }
    
    public void clearData() {
        for(int r = 0; r < this.listOfSecurities.getRowCount(); r++) {
            for(int c = 1; c < this.listOfSecurities.getColumnCount(); c++) {
                this.listOfSecurities.setValueAt(new Boolean(false), r, c);
            }
        }
        this.listOfSecurities.fireTableDataChanged();
        userorgroupid = 0;
    }
    
    public void setData(Record_Set rs) {
        listOfSecurities.clearRows();
        for (security_detail.MODULES m : security_detail.MODULES.values()) {
            ArrayList myList = new ArrayList();
            myList.add(m.getDisp());
            int accesspos = 0;
            for (security_detail.ACCESS a : security_detail.ACCESS.values()) {
                myList.add(new Boolean(false));
                accesspos++;
            }
            listOfSecurities.addRow(myList);
        }
        if (rs == null) {
            return;
        }
        String myData = null;
        security_detail.MODULES myModule;
        for (int i = 0; i < rs.length(); i++) {
            myData = rs.getString("id");
            myModule = security_detail.getModule(myData);
            for (int x = 0; x < listOfSecurities.getRowCount(); x++) {
                try {
                if (((String)listOfSecurities.getValueAt(x, 0)).equals(myModule.getDisp())) {
                    boolean[] myVals = security_detail.getNextAccessLevel(rs.getString("id"));
                    for (int y = 0; y < myVals.length; y++) {
                        listOfSecurities.setValueAt(myVals[y], x, y + 1);
                    }
                }
                } catch (Exception e) {}
            }
            rs.moveNext();
        }
        listOfSecurities.fireTableDataChanged();
    }
    public String getTitleOfTab() {
        return "Security Access";
    }

    public JPanel getComponent() {
        return this;
    }

    /**
     * Parses through our table converting the table to data that actually makes sense in the
     * world of databases.... gets each Module as defined in security_detail, loops through
     * access in tables adding up values and appending so that they can be placed in db...
     * Optional data, is provided in case we need some additional data...
     */
    public GeneralQueryFormat getSaveDataQuery(Object OptionalData) {
        ArrayList myListOfAccess = new ArrayList();
        security_detail.MODULES myCurrModule;
        security_detail.ACCESS  myCurrAccess;
        for (int i = 0; i < listOfSecurities.getRowCount(); i++) {
            int accessLevel = 0;
            myCurrModule = security_detail.getModuleByName((String)listOfSecurities.getValueAt(i, 0));
            for (int x = 1; x < listOfSecurities.getColumnCount(); x++) {
                if ((Boolean)listOfSecurities.getValueAt(i, x)) {
                    myCurrAccess = security_detail.getAccessByName(listOfSecurities.getColumnName(x));
                    accessLevel += myCurrAccess.getVal();
                }
            }
            if (accessLevel > security_detail.ACCESS.ALL.getVal()) {
                accessLevel = security_detail.ACCESS.ALL.getVal();
            }
            myListOfAccess.add((myCurrModule.getVal() + accessLevel) + "");
        }
        GeneralQueryFormat myQuery = null;
        if (isGroup) {
            save_group_access_query myGQuery = new save_group_access_query();
            myGQuery.update(myListOfAccess, userorgroupid + "", (String)OptionalData,  ManageId);
            myQuery = myGQuery;
        } else {

        }
        return myQuery;
    }

    public List_View getListView() {
        return listOfSecurities;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.GridLayout(1, 0));

    }
    // </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
