/*
 * HorizontalGraphObject.java
 *
 * Created on March 1, 2005, 10:06 AM
 */

package rmischedule.components.graphicalcomponents;

import rmischeduleserver.util.StaticDateTimeFunctions;
import java.awt.Graphics;
import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.FontMetrics;
import java.util.Vector;
import java.awt.Font;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import javax.swing.JSpinner.DateEditor;
import javax.swing.JSpinner;
import java.awt.Rectangle;
import java.awt.Container;
import java.awt.Point;

import rmischedule.components.*;
import rmischedule.main.Main_Window;
/**
 *
 * @author  ira
 */
public class HorizontalGraphObject extends javax.swing.JPanel {
    
    private int startT;
    private int endT;
    private int totalT;
    private int unitInc;
    private int majorM;
    private int sizeOfUnits;
    private boolean initialized;
    private Vector linesOnGraph;
    private int formatType;
    
    /** Creates new form HorizontalGraphObject */
    public HorizontalGraphObject(int startTime, int endTime, int unitIncrement, int majorMarkers, int timeFormat) {
        initComponents();
        formatType = timeFormat;
        initialized = false;
        startT = startTime;
        endT = endTime;
        totalT = endTime - startTime;
        unitInc = unitIncrement;
        majorM = majorMarkers;
        linesOnGraph = new Vector(20);
        /* Props To Mr Jason Allen For This Code */
        addComponentListener(
                new ComponentListener(){
            /* not used */
            public void componentHidden(ComponentEvent e){}
            public void componentMoved  (ComponentEvent e){}
            public void componentShown  (ComponentEvent e){}
            
            /* used */
            public void componentResized(ComponentEvent e){
                initializeMe();
                resizeAllLines();
                revalidate();
                repaint();
            }
        }
        );
        this.setSize(this.getSize());
    }
    
    /**
     * Called to actually set up graph, should not be called until after the graph has been
     * sized and added to object, also should be called on resize...
     */
    public void initializeMe() {
        sizeOfUnits = (this.getSize().width / (totalT / unitInc)) - 1;
        initialized = true;
        repaint();
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        //g.translate(20, 0);
        //if (initialized) {
        try {
            drawMajorMarkers(g);
            drawMinorMarkers(g);
        } catch (Exception e) {}
        //}
    }
    
    public int getUnitSize() {
        return sizeOfUnits;
    }
    
    public int getUnitInc() {
        return this.unitInc;
    }
    
    public void drawMajorMarkers(Graphics g) {
        int numOfMajorMarkers = endT / majorM;
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        FontMetrics fm = g.getFontMetrics();
        String display = "";
        int lengthOfString = 0;
        int lastSize = 0;
        for (int i = 0; i <= numOfMajorMarkers; i++) {
            display = formatString(i * majorM + "");
            lengthOfString = fm.charsWidth(display.toCharArray(), 0, display.length());
            lastSize = (i * (majorM / unitInc) * sizeOfUnits) - lengthOfString / 2;
            g.drawString(display, lastSize, GraphLineObject.getY() - 20);
            g.drawLine(i * (majorM / unitInc) * sizeOfUnits, GraphLineObject.getY() - 10, i * (majorM / unitInc) * sizeOfUnits, GraphLineObject.getY() - 2);
        }
        if (GraphLineObject.getBounds().width > lastSize + 5 + (lengthOfString / 2)) {
            Rectangle current = GraphLineObject.getBounds();
            current.width = lastSize + 5 + (lengthOfString / 2);
            GraphLineObject.setBounds(current);
            current = MainDisplayPanel.getBounds();
            current.width = lastSize + 5 + (lengthOfString / 2);
            MainDisplayPanel.setBounds(current);
        }
    }
    
    public void drawMinorMarkers(Graphics g) {
        int numOfMinorMarkers = endT / unitInc;
        for (int i = 0; i <= numOfMinorMarkers; i++) {
            g.drawLine(i * sizeOfUnits, BottomHeader.getY() + 0, i * sizeOfUnits, BottomHeader.getY() + 2);
        }
    }
    
    public String formatString(String input) {
        return StaticDateTimeFunctions.stringToFormattedTime(input, Main_Window.parentOfApplication.is12HourFormat());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        MainDisplayPanel = new javax.swing.JPanel();
        TopHeader = new javax.swing.JPanel();
        GraphLineObject = new javax.swing.JPanel();
        LineAreaPanel = new javax.swing.JPanel();
        BottomHeader = new javax.swing.JPanel();

        setLayout(new java.awt.GridLayout(1, 0));

        setOpaque(false);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        MainDisplayPanel.setLayout(new javax.swing.BoxLayout(MainDisplayPanel, javax.swing.BoxLayout.Y_AXIS));

        TopHeader.setLayout(null);

        TopHeader.setMinimumSize(new java.awt.Dimension(0, 45));
        MainDisplayPanel.add(TopHeader);

        GraphLineObject.setLayout(new java.awt.GridLayout(1, 0));

        GraphLineObject.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        GraphLineObject.setMaximumSize(new java.awt.Dimension(32767, 15));
        GraphLineObject.setMinimumSize(new java.awt.Dimension(50, 15));
        GraphLineObject.setPreferredSize(new java.awt.Dimension(4, 15));
        GraphLineObject.setRequestFocusEnabled(false);
        LineAreaPanel.setLayout(null);

        LineAreaPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addTestShift(evt);
            }
        });

        GraphLineObject.add(LineAreaPanel);

        MainDisplayPanel.add(GraphLineObject);

        BottomHeader.setLayout(null);

        MainDisplayPanel.add(BottomHeader);

        add(MainDisplayPanel);

    }//GEN-END:initComponents

    private void addTestShift(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addTestShift
        HorizontalGraphLine myLine = new HorizontalGraphLine(this, 200, 500);
        addLine(myLine);
    }//GEN-LAST:event_addTestShift

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        
    }//GEN-LAST:event_formComponentResized
    
    public void addLine(HorizontalGraphLine newLine) {
        int start = newLine.getStart();
        int end = newLine.getEnd();
        LineAreaPanel.add(newLine);
        newLine.setBounds((start / unitInc) * sizeOfUnits, 0, ((end - start) / unitInc) * sizeOfUnits, LineAreaPanel.getHeight());
        newLine.initMyComponents();
        linesOnGraph.add(newLine);
        TopHeader.add(newLine.StartTimeSpinner);
        BottomHeader.add(newLine.EndTimeSpinner);
        displayTextLabelsForLine(newLine);
        repaint();
    }
    
    public void resizeAllLines() {
        HorizontalGraphLine currLine;
        for (int i = 0; i < linesOnGraph.size(); i++) {
            currLine = (HorizontalGraphLine)linesOnGraph.get(i);
            resizeLine(currLine);
        }
    }
    
    public void resizeLine(HorizontalGraphLine myLine) {
        int start = myLine.getStart();
        int end = myLine.getEnd();
        myLine.setBounds((start / unitInc) * sizeOfUnits, 0, ((end - start) / unitInc) * sizeOfUnits, LineAreaPanel.getHeight());
        displayTextLabelsForLine(myLine);
        myLine.validate();
    }
    
    public void removeLine(HorizontalGraphLine myLine) {
        LineAreaPanel.remove(myLine);
        LineAreaPanel.repaint();
        linesOnGraph.remove(myLine);
        this.TopHeader.remove(myLine.StartTimeSpinner);
        this.BottomHeader.remove(myLine.EndTimeSpinner);
    }
    
    public boolean checkIfCanMoveBackwards(HorizontalGraphLine myLine) {
        HorizontalGraphLine tempLine;
        if (myLine.getStart() - unitInc <= startT) {
            return false;
        }
        
        for (int i = 0; i < linesOnGraph.size(); i++) {
            tempLine = (HorizontalGraphLine)linesOnGraph.get(i);
            if (tempLine.getEnd() == myLine.getStart()) {
                return false;
            }
        }
        return true;
    }
    
    public boolean checkIfCanMoveForwards(HorizontalGraphLine myLine) {
        HorizontalGraphLine tempLine;
        if (myLine.getEnd() >= endT) {
            return false;
        }
        
        for (int i = 0; i < linesOnGraph.size(); i++) {
            tempLine = (HorizontalGraphLine)linesOnGraph.get(i);
            if (tempLine.getStart() == myLine.getEnd()) {
                return false;
            }
        }
        return true;
    }
        
    public void displayTextLabelsForLine(HorizontalGraphLine myLine) {
        Rectangle myBounds = myLine.getBounds();
        myLine.StartTimeSpinner.setBounds(myBounds.x, TopHeader.getHeight() - 17, 50, 15);
        myLine.EndTimeSpinner.setBounds(myBounds.x + myBounds.width - 50, 2, 50, 15);
        repaint();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        JFrame myFrame = new JFrame();
        myFrame.setSize(400, 150);
        
        HorizontalGraphObject myGraph = new HorizontalGraphObject(0, 1440, 15, 120, StaticDateTimeFunctions.STANDARD_FORMAT);
        myFrame.setLayout(new GridLayout(1,1));
        myFrame.add(myGraph);
        myFrame.show();
        myGraph.initializeMe();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BottomHeader;
    private javax.swing.JPanel GraphLineObject;
    private javax.swing.JPanel LineAreaPanel;
    private javax.swing.JPanel MainDisplayPanel;
    private javax.swing.JPanel TopHeader;
    // End of variables declaration//GEN-END:variables
    
}
