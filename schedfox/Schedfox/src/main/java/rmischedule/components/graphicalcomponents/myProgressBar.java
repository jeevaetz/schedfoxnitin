/*
 * myProgressBar.java
 *
 * Created on May 13, 2005, 12:43 PM
 */

package rmischedule.components.graphicalcomponents;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
/**
 *
 * @author ira
 */
public class myProgressBar extends JPanel {
    
    private JPanel myProgressLabel;
    private int maxValue;
    private int lastSize;
    FontMetrics fm;
    String myText;
    Rectangle2D myStringBounds;
    
    /** Creates a new instance of myProgressBar */
    public myProgressBar() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        myProgressLabel = new JPanel();
        myProgressLabel.setLayout(new GridLayout(2,1));
        myProgressLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        add(myProgressLabel);
        add(Box.createHorizontalGlue());
        addFadingComponents();
        myProgressLabel.setBackground(new Color(163, 163, 212));
        setMaximumValue(1);
        setValue(0);
        
        myProgressLabel.setPreferredSize(new Dimension(0, 10));
        myProgressLabel.setMinimumSize(new Dimension(0, 1));
        myProgressLabel.setMaximumSize(new Dimension(0, 1000));
    }
    
    public void setMaximumValue(int max) {
        maxValue = max;
    }
    
    public void addFadingComponents() {
        Container currentContainer = myProgressLabel;
        Color origColor = this.getBackground();
        Color fadedColor = new Color(origColor.getRed() /2, origColor.getGreen() / 2, origColor.getBlue() / 2, 85);
        for (int i = 0; i < 4; i++) {
            JPanel clearPanel = new JPanel();
            clearPanel.setOpaque(false);
            currentContainer.add(clearPanel);
            JPanel fadedPanel = new JPanel();
            fadedPanel.setBackground(fadedColor);
            currentContainer.add(fadedPanel);
            currentContainer = fadedPanel;
            currentContainer.setLayout(new GridLayout(2,1));
        }
    }
    
    public void setValue(int newVal) {
        float percentage = new Float(newVal) / new Float(maxValue);
        int fullX = getBounds().width;
        int newSize = new Float(fullX * (percentage)).intValue();
        if (lastSize != newSize) {
            lastSize = newSize;
            myProgressLabel.setPreferredSize(new Dimension(newSize, 10));
            myProgressLabel.setMinimumSize(new Dimension(newSize, 1));
            myProgressLabel.setMaximumSize(new Dimension(newSize, 1000));
            myProgressLabel.setBounds(0,0,newSize, myProgressLabel.getBounds().height);
            repaint();
            revalidate();
        }
    }
    
    public void paint(Graphics g){
        super.paint(g);
        if (myText != null) {
            g.setColor(Color.WHITE);
            fm = g.getFontMetrics();
            myStringBounds = fm.getStringBounds(myText, g);
            g.drawString(myText, new Double((getBounds().width - myStringBounds.getWidth()) / 2).intValue(),
                    new Double((getBounds().height + myStringBounds.getHeight()) / 2).intValue());
        }
    }
    
}
