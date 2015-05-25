/*
 * myToolBarIcons.java
 *
 * Created on July 1, 2005, 12:42 PM
 */

package rmischedule.components.graphicalcomponents;

import javax.swing.JToolBar;
import javax.swing.border.*;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.JToolTip;
import java.awt.Font;
import javax.swing.JButton;
import rmischedule.main.Main_Window;
import rmischedule.schedule.Schedule_View_Panel;
import rmischedule.schedule.components.SToolTip;
/**
 *
 * @author ira
 */
public class myToolBarIcons extends JButton {
    private SoftBevelBorder myToolBarBorder = new SoftBevelBorder(SoftBevelBorder.RAISED);
    private SoftBevelBorder myToolBarDownBorder = new SoftBevelBorder(SoftBevelBorder.LOWERED);
    private boolean isPressed;
    private boolean canBePressed = false;
    private ImageIcon dIcon;
    private ImageIcon uIcon;
    private String dString;
    private String uString;
    private Color overColor = new Color(210,220,255);
    private Color normColor = new Color(230,233,237);
    private long mouseDown;
    
    public myToolBarIcons() {
        canBePressed = false;
        setUp();
        mouseDown = System.currentTimeMillis();
    }

    public myToolBarIcons(ImageIcon whenUp, ImageIcon whenDown, String whenUpS, String whenDownS) {
        uIcon = whenUp;
        dIcon = whenDown;
        dString = whenDownS;
        uString = whenUpS;
        canBePressed = true;
        setUp();
    }
    
    /**
     * Sets the Text Yippy....
     */
    public void setText(String newText, Font newFont) {
        this.setFont(newFont);
        this.setText(newText);
    }
    
    /**
     * Hmm gee I wonder
     */
    public void setMySize(Dimension newSize) {
        this.setMinimumSize(newSize);
        this.setMaximumSize(newSize);
        this.setPreferredSize(newSize);
        setBounds(0,0,newSize.width, newSize.height);
    }
    
    public boolean getPressed() {
        return isPressed;
    }
    
    private void setUp() {
        setBorder(myToolBarBorder);
        //setBackground(normColor);
        setOpaque(true);
        isPressed = false;
        setIcon(uIcon);
        setToolTipText(uString);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                triggerPress();
                runOnClick();
                triggerRelease();
            }

        });
    }

    private void triggerPress() {
        if (!isEnabled()) {
            return;
        }

        if (!canBePressed) {
            setBorder(myToolBarDownBorder);
        }
        mouseDown = System.currentTimeMillis();
    }

    protected void runOnClick() {};

    private void triggerRelease() {
        if (!isEnabled()) {
            return;
        }

        if (!canBePressed) {
            setBorder(myToolBarBorder);
        }
        if (canBePressed && System.currentTimeMillis() - mouseDown < 1000) {
            if (isPressed) {
                setBorder(myToolBarDownBorder);
                setIcon(dIcon);
                setToolTipText(dString);
            } else {
                setBorder(myToolBarBorder);
                setIcon(uIcon);
                setToolTipText(uString);
            }
        }
    }

    public void setPressed(boolean val) {
        isPressed = val;
        if (isPressed) {
            setBorder(myToolBarDownBorder);
            setIcon(dIcon);
            setToolTipText(dString);
        } else {
            setBorder(myToolBarBorder);
            setIcon(uIcon);
            setToolTipText(uString);
        }
    }
    
    public JToolTip createToolTip() {
        return new SToolTip(60);
    }
    
    public void removeListeners() {
        MouseListener[] ml = this.getMouseListeners();       
        for(int i = 0; i < ml.length; i++) {
            this.removeMouseListener(ml[i]);
        }
        
        MouseMotionListener[] mml = this.getMouseMotionListeners();
        for(int i = 0; i < mml.length; i++) {
            this.removeMouseMotionListener(mml[i]);
        }
    }
}
