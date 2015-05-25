/*
 * CustomSplitPaneDivider.java
 *
 * Created on August 22, 2006, 10:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rmischedule.schedule.components.graphiccomponents;

import javax.swing.plaf.basic.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * Silly little thing for our Schedule_View_Panel's split pane... only lets you collapse the
 * divider to the right, and from there only allows you to expand it back to where it was.
 *
 * @author shawn
 */
public class CustomSplitPaneDivider extends BasicSplitPaneDivider implements ActionListener {
    
    private DragController drag;
    
    /** Creates a new instance of CustomSplitPaneDivider */
    public CustomSplitPaneDivider(BasicSplitPaneUI ui) {
        super(ui);
    }
    
    protected void oneTouchExpandableChanged() {
        super.oneTouchExpandableChanged();
        this.rightButton.addActionListener(this);
        this.leftButton.addActionListener(this);
        this.leftButton.setVisible(false);
    }
    
    protected JButton createLeftOneTouchButton() {
        JButton button = super.createLeftOneTouchButton();
        button.setToolTipText("Click to show employee list");
        return button;
    }
    
    protected JButton createRightOneTouchButton() {
        JButton button = super.createRightOneTouchButton();
        button.setToolTipText("Click to hide employee list");
        return button;
    }
    
    public void actionPerformed(ActionEvent evt) {
        if(evt.getSource() == this.rightButton) {
            this.rightButton.setVisible(false);
            this.leftButton.setVisible(true);
            this.removeMouseListener(this.mouseHandler);
            this.removeMouseMotionListener(this.mouseHandler);
            this.splitPane.removeMouseListener(mouseHandler);
            this.splitPane.removeMouseMotionListener(mouseHandler);
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else {
            this.rightButton.setVisible(true);
            this.leftButton.setVisible(false);
            this.addMouseListener(this.mouseHandler);
            this.addMouseMotionListener(this.mouseHandler);
            this.splitPane.addMouseListener(this.mouseHandler);
            this.splitPane.addMouseMotionListener(this.mouseHandler);
            this.setCursor((orientation == JSplitPane.HORIZONTAL_SPLIT) ?
                Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR) :
                Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
        }
    }
}
