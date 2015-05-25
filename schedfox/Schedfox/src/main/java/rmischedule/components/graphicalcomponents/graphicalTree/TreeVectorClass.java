/*
 * TreeVectorClass.java
 *
 * Created on November 22, 2005, 8:47 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package rmischedule.components.graphicalcomponents.graphicalTree;
import java.util.Vector;
import javax.swing.*;
/**
 *
 * @author Ira Juneau
 * A interface used by the Tree...
 */
public interface TreeVectorClass {
    public Vector<TreeVectorClass> getMyList();
    public boolean hasList();
    public String getDisplayName();
    public void setIconPanel(JLabel myPanel);
    public void refreshIcon();
    public void runOnClick();
}
