/*
 * Branch.java
 *
 * Created on November 22, 2005, 10:02 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.misc;
import rmischedule.components.graphicalcomponents.graphicalTree.TreeVectorClass;
import rmischeduleserver.mysqlconnectivity.queries.util.*;
import java.util.*;
import rmischedule.main.*;
import rmischedule.data_connection.*;
import javax.swing.*;
/**
 *
 * @author Ira Juneau
 */
public class BranchTreeObj implements TreeVectorClass {
    
    private String myId;
    private String myName;
    private String manageId;
    private JLabel iconPanel;
    private boolean hasAccess;
    private CompanyTreeObj myParent;
    
    /** Creates a new instance of Branch */
    public BranchTreeObj(String id, String name, String mid, boolean access, CompanyTreeObj parent) {
        myId = id;
        myName = name;
        manageId = mid;
        hasAccess = access;
        myParent = parent;
    }
    
    public Vector<TreeVectorClass> getMyList() {
        return null;
    }
    public boolean hasList() {
        return false;
    }
    
    public String getDisplayName() {
        return myName;
    }

    public boolean hasAccess() {
        return hasAccess;
    }
    
    public void setIconPanel(JLabel myPanel) {
        iconPanel = myPanel;
        refreshIcon();
    }

    public void refreshIcon() {
        if (iconPanel != null) {
            if (!hasAccess) {
                iconPanel.setIcon(Main_Window.parentOfApplication.BulletRed16x16Icon);
            } else {
                iconPanel.setIcon(Main_Window.parentOfApplication.BulletGreen16x16Icon);
            }
        }
    }

    public void setAccess(boolean newVal) {
        hasAccess = newVal;
        refreshIcon();
        myParent.refreshIcon();
        xSaveBranchCompanyAccessQuery mySaveQuery = new xSaveBranchCompanyAccessQuery();
        mySaveQuery.update(myParent.getUser(), myId, myParent.getId(), hasAccess);
        try {
            new Connection().executeQuery(mySaveQuery);
        } catch (Exception e) {}
    }
    
    public void runOnClick() {
        if (hasAccess) {
            setAccess(!hasAccess);
        } else {
            setAccess(!hasAccess);
        }
    }
}
