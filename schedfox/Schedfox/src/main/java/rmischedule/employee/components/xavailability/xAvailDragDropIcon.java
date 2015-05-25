/*
 * xAvailDragDropIcon.java
 *
 * Created on August 19, 2005, 1:34 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.employee.components.xavailability;
import rmischedule.components.graphicalcomponents.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import javax.swing.JButton;

/**
 *
 * @author Ira Juneau
 */
public class xAvailDragDropIcon extends DragAndDropLabel {
    
    public static final String SICK     =   "Sick (Doc)";
    public static final String VACATION =   "Vacation";
    public static final String HALF_VACATION =   "Half Day Vacation";
    public static final String NONAVAIL =   "Non Avail";
    public static final String AVAIL    =   "Available";
    public static final String PERSONAL =   "Personal";
    public static final String MILITARY =   "Military";
    public static final String PH = "Personal Holiday";
    public static final String CO = "Call Out";
    public static final String LOA = "Leave of Absence";
    public static final String JURY = "Jury Duty";
    
    private static final Dimension mySize = new Dimension(32,32);
    private Image   myImage;
    private Object  myVal;

    /** Creates a new instance of xAvailDragDropIcon */
    public xAvailDragDropIcon(xFormInterface myParent, Integer objToPass,
            ImageIcon myIcon, Color myBackground) {
        super(myParent.getLayeredPane(), myParent.getContentPanel(), objToPass);

        setMinimumSize(mySize);
        setMaximumSize(mySize);
        setPreferredSize(mySize);
        myVal = objToPass;
        myImage = myIcon.getImage();
        setBackground(myBackground);
    }
    
    /**
     * Returns the same val that will be passed via the drop method...
     */ 
    public Object getValue() {
        return myVal;
    }

    public Image getMyImage() {
        return myImage;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(myImage, 1,1, 33, 33, this);
    }
    
}
