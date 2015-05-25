/*
 * CompanyVector.java
 *
 * Created on November 22, 2005, 10:00 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.misc;
import java.util.*;
import javax.swing.*;
import rmischedule.main.*;
import rmischedule.components.graphicalcomponents.graphicalTree.TreeVectorClass;
/**
 *
 * @author Ira Juneau
 */
public class CompanyTreeObj implements TreeVectorClass {
    
    public enum myAccess {FULL, PARTIAL, NONE};
    private myAccess accessLevel;
    private Vector<TreeVectorClass> branchVector;
    private String myId;
    private String myName;
    private String myDB;
    private String manageId;
    private String userId;
    private boolean hasAccess;
    private JLabel iconPanel;
    
    /** Creates a new instance of CompanyVector */
    public CompanyTreeObj() {
        instantiate("", "", "", "", "", false);
    }
    
    public CompanyTreeObj(String id, String name, String mid, String currUser) {
        instantiate(id, name, "", mid, currUser, false);
    }
    
    public String getId() {
        return myId;
    }
    
    public String getUser() {
        return userId;
    }
    
    private void instantiate(String id, String name, String db, String mid, String uid, boolean access) {
        branchVector = new Vector();
        myId = id;
        myName = name;
        myDB = db;
        userId = uid;
        manageId = mid;
        hasAccess = access;
    }
    
    public String getDisplayName() {
        return myName;
    }
    
    public void setIconPanel(JLabel myPanel) {
        iconPanel = myPanel;
        refreshIcon();
    }
    
    public void addBranch(TreeVectorClass myBranch) {
        branchVector.add(myBranch);
    }
    
    public void setAccess(boolean hasAccess) {
        for (int i = 0; i < branchVector.size(); i++) {
            try {
                BranchTreeObj currBranch = (BranchTreeObj)branchVector.get(i);
                currBranch.setAccess(hasAccess);
            } catch (Exception e) {}
        }
    }
    
    public void runOnClick() {
        if (accessLevel == myAccess.FULL ||
            accessLevel == myAccess.PARTIAL) {
            setAccess(false);
        } else {
            setAccess(true);
        }
    }
    
    public Vector<TreeVectorClass> getMyList() {
        return branchVector;
    }
    
    public void refreshIcon() {
        boolean hasFullAccess = true;
        boolean hasPartAccess = false;
        for (int i = 0; i < branchVector.size(); i++) {
            try {
                BranchTreeObj myBranch = (BranchTreeObj)branchVector.get(i);
                if (myBranch.hasAccess()) {
                    hasPartAccess = true;
                } else {
                    hasFullAccess = false;
                }
            } catch (Exception e) {}
        }
        if (hasFullAccess) {
            iconPanel.setIcon(Main_Window.parentOfApplication.BulletGreen16x16Icon);
            accessLevel = myAccess.FULL;
        } else if (hasPartAccess) {
            iconPanel.setIcon(Main_Window.parentOfApplication.BulletYellow16x16Icon);
            accessLevel = myAccess.PARTIAL;
        } else {
            iconPanel.setIcon(Main_Window.parentOfApplication.BulletRed16x16Icon);
            accessLevel = myAccess.NONE;
        }
    }
    
    public boolean hasList() {
        if (branchVector.size() > 0) {
            return true;
        }
        return false;
    }
    
}
