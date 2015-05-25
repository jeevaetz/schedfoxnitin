/*
 * xFormInterface.java
 *
 * Created on August 19, 2005, 11:51 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package rmischedule.employee.components.xavailability;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import java.util.Vector;
import rmischedule.employee.xEmployeeEdit;
/**
 *
 * @author Ira Juneau
 */
public interface xFormInterface {
    public void displayAvailStats(xIndividualDayPanel indDayPanel);
    public void hideAvailStats();
    public void runOnClickDay(xIndividualDayPanel PanelTriggered);
    public JLayeredPane getLayeredPane();
    public JPanel getContentPanel();
    public Vector<AvailIconContainer> getVectorOfValidActions();
    public void hideSpecifyTimes(xAvailabilitySpecifyTimes myForm);
    public JPopupMenu getMyMenu();
    public void setActiveDayForMenu(xIndividualDayPanel myActivePanel);
    public xEmployeeEdit getMyParent();
    /** Returns avail id **/
    public String saveDayInformation(xIndividualDayPanel mySavePanel);
    public void deletePermenantDayInformation(xIndividualDayPanel myDelPanel);
    public void getData(String eid);
    public void loadData();
    public void clearData();
    public boolean isGettingData();
    public xMainAvailabilityPanel getMainAvailPanel();
}
