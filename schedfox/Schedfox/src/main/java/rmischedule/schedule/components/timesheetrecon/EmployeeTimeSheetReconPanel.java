/*
 * EmployeeTimeSheetReconPanel.java
 *
 * Created on April 25, 2005, 8:53 AM
 */

package rmischedule.schedule.components.timesheetrecon;
import rmischedule.components.graphicalcomponents.GraphicalListComponent;
import rmischedule.components.graphicalcomponents.GraphicalListParent;
import rmischedule.main.Main_Window;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import java.awt.Container;
/**
 *
 * @author ira
 */
public class EmployeeTimeSheetReconPanel extends GraphicalListComponent {
    
    /** Creates a new instance of EmployeeTimeSheetReconPanel */
    public EmployeeTimeSheetReconPanel(Object ObjectToReturn, GraphicalListParent parentPanel, String text) {
        super(ObjectToReturn, parentPanel, text);
    }
    
    public EmployeeTimeSheetReconPanel(Object ObjectToReturn, GraphicalListParent parentPanel, String text,
                                       JLayeredPane panelToDisplayOn, Container panelContainingDropContainers, Object objectToPassOnDrop) {
        super(ObjectToReturn, parentPanel, text, panelToDisplayOn, panelContainingDropContainers, objectToPassOnDrop);
    }
    
    protected void setToSelectedColor() {
        IconLabel.setIcon(Main_Window.Time_Sheet_Check_Icon);
    }
    
    protected void setToUnselectedColor() {
        IconLabel.setIcon(Main_Window.Time_Sheet_Icon);
    }
    
    protected void setToMouseOverColor() {
        IconLabel.setIcon(Main_Window.Time_Sheet_Over_Icon);
    }
    
    /**
     * Handle check out here g....
     */
    protected void toggleSelected() {
        if(!AmISelected || (AmISelected && (parentP.getUnSelectable() && allowUnSelected))){
            setSelected(!AmISelected);
            parentP.toggleSelectionOfElement(this);
        }
    }
}
