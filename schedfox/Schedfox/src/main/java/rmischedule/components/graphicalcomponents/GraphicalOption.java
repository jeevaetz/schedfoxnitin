/*
 * GraphicalOption.java
 *
 * Created on June 30, 2005, 9:36 AM
 */

package rmischedule.components.graphicalcomponents;
import javax.swing.JRadioButton;
import rmischedule.main.Main_Window;
import javax.swing.event.ChangeEvent;
/**
 *
 * @author ira
 */
public class GraphicalOption extends JRadioButton {
    
    /** Creates a new instance of GraphicalOption */
    public GraphicalOption(boolean setIcon) {
        super();
        if (setIcon) {
            addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    ModifyStateChanged(evt);
                }
            });
            setIcon(Main_Window.Red_Bullet_Icon);
        }
        this.setFocusable(false);
    }
    
    public void ModifyStateChanged(ChangeEvent evt) {
        //super.fireStateChanged();
        if (isSelected()) {
            setIcon(Main_Window.Green_Bullet_Icon);
        } else {
            setIcon(Main_Window.Red_Bullet_Icon);
        }
    }
    
}
