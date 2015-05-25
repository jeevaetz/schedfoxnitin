/*
 * CustomSplitPaneUI.java
 *
 * Created on August 22, 2006, 11:07 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rmischedule.schedule.components.graphiccomponents;

import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.*;

/**
 *
 * @author shawn
 */
public class CustomSplitPaneUI extends BasicSplitPaneUI {
    
    /** Creates a new instance of CustomSplitPaneUI */
    public CustomSplitPaneUI() {
        super();
    }
    
    public BasicSplitPaneDivider createDefaultDivider() {
        return new CustomSplitPaneDivider(this);
    }
    
}
