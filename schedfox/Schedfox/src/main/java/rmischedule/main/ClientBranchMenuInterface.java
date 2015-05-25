/*
 * ClientBranchMenuInterface.java
 *
 * Created on April 7, 2005, 9:15 AM
 */

package rmischedule.main;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JMenuItem;
/**
 *
 * @author ira
 */
public abstract class ClientBranchMenuInterface implements ActionListener {
    
    private ArrayList menuTextArray;
    private ArrayList<String> clientBranch;
    private ArrayList<String> clientCompany;
    private Main_Window parent;
    
    public ClientBranchMenuInterface() {
        menuTextArray = new ArrayList();
        clientBranch = new ArrayList();
        clientCompany = new ArrayList();
    }
    
    public void setData(Main_Window mw, JMenuItem source, String b, String c) {
        menuTextArray.add(source);
        clientBranch.add(b);
        clientCompany.add(c);
        parent = mw;
    }
    
    public void actionPerformed(ActionEvent e) {
        JMenuItem sourceMenu = (JMenuItem)e.getSource();
        for (int i = 0; i < menuTextArray.size(); i++) {
            if (sourceMenu == (JMenuItem)menuTextArray.get(i)) {
                actionCaught(parent, (String)clientBranch.get(i), (String)clientCompany.get(i));
            }
        }
    }
    
    public abstract void actionCaught(Main_Window p, String b, String c);
    
}
